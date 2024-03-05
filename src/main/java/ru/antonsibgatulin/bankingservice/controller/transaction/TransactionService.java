package ru.antonsibgatulin.bankingservice.controller.transaction;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TransactionRequiredException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransactionDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransferManagerDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.response.TransactionStatusDto;
import ru.antonsibgatulin.bankingservice.entity.transaction.Transaction;
import ru.antonsibgatulin.bankingservice.entity.transaction.TransactionStatus;
import ru.antonsibgatulin.bankingservice.entity.user.User;
import ru.antonsibgatulin.bankingservice.entity.wallet.Wallet;
import ru.antonsibgatulin.bankingservice.except.NotFoundException;

import ru.antonsibgatulin.bankingservice.repository.PhoneNumberRepository;
import ru.antonsibgatulin.bankingservice.repository.TransactionRepository;
import ru.antonsibgatulin.bankingservice.repository.UserRepository;
import ru.antonsibgatulin.bankingservice.repository.WalletRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Service
public class TransactionService implements Runnable {


    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final PlatformTransactionManager transactionManager;


    private final Queue<TransactionData> transactionQueue = new ConcurrentLinkedQueue<>();

    private Thread transactionQueueThread = new Thread(this);

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing TransactionService...");
        transactionQueueThread.start();
    }

    @Transactional
    public TransactionStatusDto trans(Authentication authentication, TransactionDto transactionDto) {

        var username = authentication.getName();
        var senderUser = userRepository.getUserByUsername(username);
        var senderUserWallet = walletRepository.getWalletByUser(senderUser);


        var receivedUser = getReceivedUser(transactionDto);
        var receivedUserWallet = walletRepository.getWalletByUser(receivedUser);
        LOGGER.debug("Sender user: {}, sender wallet: {}, received user: {}, received wallet: {}",
                senderUser, senderUserWallet, receivedUser, receivedUserWallet);
        if (senderUserWallet.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            return new TransactionStatusDto("Не достаточно денег на счете для перевода", TransactionStatus.FAILED, null);
        }

        return runTransaction(transactionDto, senderUser, receivedUser, null);

    }


    @Transactional
    public TransactionStatusDto trans(TransferManagerDto transferManagerDto) {
        var senderUser = userRepository.getUserById(transferManagerDto.getSenderId());
        var receivedUser = userRepository.getUserById(transferManagerDto.getReceivedId());
        if (senderUser == null || receivedUser == null) {
            return new TransactionStatusDto("Not found received or sender", TransactionStatus.FAILED, null);
        }
        var senderUserWallet = walletRepository.getWalletByUser(senderUser);
        var receivedUserWallet = walletRepository.getWalletByUser(receivedUser);

        LOGGER.debug("Sender user: {}, sender wallet: {}, received user: {}, received wallet: {}",
                senderUser, senderUserWallet, receivedUser, receivedUserWallet);


        var transactionDto = new TransactionDto();
        transactionDto.setReceivedUserId(receivedUser.getId());
        transactionDto.setAmount(transferManagerDto.getAmount());

        if (senderUserWallet.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            return new TransactionStatusDto("Не достаточно денег на счете для перевода", TransactionStatus.FAILED, null);
        }

        return runTransaction(transactionDto, senderUser, receivedUser, null);
    }

    @Transactional
    public TransactionStatusDto runTransaction(TransactionDto transactionDto, User senderUser, User receivedUser, Transaction transaction) {

        org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            TransactionStatusDto result = doTransaction(transactionDto, senderUser, receivedUser, transaction);

            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
            throw e;
        }

    }

    @Transactional
    public TransactionStatusDto doTransaction(TransactionDto transactionDto, User senderUser, User receivedUser, Transaction transaction) {
        var senderWallet = walletRepository.getWalletByUser(senderUser);
        var receivedWallet = walletRepository.getWalletByUser(receivedUser);

        try {

            if (entityManager.contains(senderWallet) && !entityManager.isJoinedToTransaction()) {
                return getTransactionStatusDto(transactionDto, senderUser, receivedUser, transaction);
            }
            if (entityManager.contains(receivedWallet) && !entityManager.isJoinedToTransaction()) {
                return getTransactionStatusDto(transactionDto, senderUser, receivedUser, transaction);
            }

            entityManager.merge(senderWallet);
            entityManager.merge(receivedWallet);

            entityManager.lock(senderWallet, LockModeType.PESSIMISTIC_WRITE);
            entityManager.lock(receivedWallet, LockModeType.PESSIMISTIC_WRITE);


            if (senderWallet.getBalance().compareTo(transactionDto.getAmount()) >= 0) {
                senderWallet.setBalance(senderWallet.getBalance().subtract(transactionDto.getAmount()));
                receivedWallet.setBalance(receivedWallet.getBalance().add(transactionDto.getAmount()));

                if (transaction == null) {
                    transaction = new Transaction(senderUser, receivedUser, transactionDto.getAmount());
                } else {
                    transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                }


                transactionRepository.save(transaction);


                var resultSender = new TransactionStatusDto("Вы перевели " + transactionDto.getAmount().toString() + " ID получателя: " + receivedUser.getId(), TransactionStatus.COMPLETED, transaction.getId());
                var resultReceived = new TransactionStatusDto("Вам перевели " + transactionDto.getAmount().toString() + " ID отправителя: " + senderUser.getId(), TransactionStatus.COMPLETED, transaction.getId());

                simpMessagingTemplate.convertAndSendToUser(
                        String.valueOf(receivedUser.getId()),
                        "/queue/message",
                        resultReceived
                );
                simpMessagingTemplate.convertAndSendToUser(
                        String.valueOf(senderUser.getId()),
                        "/queue/message",
                        resultSender
                );

                LOGGER.debug("Sended user: {}, sender wallet: {}, received user: {}, received wallet: {}",
                        senderUser, receivedUser);


                ;
                return resultSender;

            } else {

                return new TransactionStatusDto("Не достаточно денег на счете", TransactionStatus.FAILED, null);

            }

        } catch (Exception e) {
            return getTransactionStatusDto(transactionDto, senderUser, receivedUser, transaction);
        }
    }

    private TransactionStatusDto getTransactionStatusDto(TransactionDto transactionDto, User senderUser, User receivedUser, Transaction transaction) {
        if (transaction == null) {
            transaction = new Transaction(senderUser, receivedUser, transactionDto.getAmount());
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transactionRepository.save(transaction);
        }
        transactionQueue.add(new TransactionData(senderUser, receivedUser, transactionDto, transaction));

        return new TransactionStatusDto("Переводится, ожидайте", TransactionStatus.PENDING, transaction.getId());
    }


    private User getReceivedUser(TransactionDto transactionDto) {
        if (transactionDto.getPhoneReceived() != null) {
            var phoneNumber = phoneNumberRepository.getPhoneNumberByNumber(transactionDto.getPhoneReceived());
            if (phoneNumber != null) return userRepository.getUserById(phoneNumber.getUser().getId());

        }
        if (transactionDto.getReceivedUserId() != null) {
            var user = userRepository.getUserById(transactionDto.getReceivedUserId());
            if (user != null) return user;
        }
        if (transactionDto.getReceivedWalletId() != null) {
            var wallet = walletRepository.getWalletById(transactionDto.getReceivedWalletId());
            if (wallet != null) {
                return wallet.getUser();
            }
        }
        throw new NotFoundException("Received not found");
    }

    @Override
    public void run() {
        LOGGER.info("Processing transaction queue...");
        processTransactionQueue();
    }


    public void processTransactionQueue() {

        if (true == false)
            return;

        while (true) {
            if (!transactionQueue.isEmpty()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                var transactionData = transactionQueue.poll();

                org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                TransactionStatusDto transactionStatusDto = null;
                try {
                    // var transactionStatusDto = doTransaction(transactionData.getTransactionDto(), /* ... */);
                    transactionStatusDto = doTransaction(transactionData.getTransactionDto(), transactionData.getSenderUser(), transactionData.getReceivedUser(), transactionData.getTransaction());
                    transactionManager.commit(status);
                } catch (Exception e) {
                    transactionManager.rollback(status);
                }
                //runTransaction(transactionData.getTransactionDto(), null, null, transactionData.getSender(), transactionData.getReceived(), transactionData.getTransaction());
                //trans(new TransferManagerDto(transactionData.getSender().getId(), transactionData.getReceived().getId(), transactionData.getTransactionDto().getAmount()));
                if (transactionStatusDto != null && transactionStatusDto.getStatus() == TransactionStatus.COMPLETED) {

                    simpMessagingTemplate.convertAndSendToUser(
                            String.valueOf(transactionData.getSenderUser().getId()),
                            "/queue/message",
                            new TransactionStatusDto("Перевод " + transactionData.getTransactionDto().getAmount().toString() + " успешно выполнен ID пользователя: " + transactionData.getReceivedUser().getId(), TransactionStatus.COMPLETED, transactionData.getTransaction().getId())
                    );
                }
            }


        }
    }


    @Data
    @AllArgsConstructor
    private class TransactionData {
        private User senderUser;
        private User receivedUser;
        private TransactionDto transactionDto;
        private Transaction transaction;
    }

}