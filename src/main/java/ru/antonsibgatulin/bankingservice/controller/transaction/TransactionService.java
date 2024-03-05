package ru.antonsibgatulin.bankingservice.controller.transaction;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransactionDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransferManagerDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.response.TransactionStatusDto;
import ru.antonsibgatulin.bankingservice.entity.transaction.Transaction;
import ru.antonsibgatulin.bankingservice.entity.transaction.TransactionStatus;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
import ru.antonsibgatulin.bankingservice.entity.user.User;
import ru.antonsibgatulin.bankingservice.entity.wallet.Wallet;
import ru.antonsibgatulin.bankingservice.except.NotFoundException;
import ru.antonsibgatulin.bankingservice.mapper.UserMapper;

import ru.antonsibgatulin.bankingservice.repository.PhoneNumberRepository;
import ru.antonsibgatulin.bankingservice.repository.TransactionRepository;
import ru.antonsibgatulin.bankingservice.repository.UserRepository;
import ru.antonsibgatulin.bankingservice.repository.WalletRepository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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

        return runTransaction(transactionDto, senderUserWallet, receivedUserWallet, senderUser, receivedUser, null);

    }


    @Transactional
    public TransactionStatusDto trans(TransferManagerDto transferManagerDto) {
        var senderUser = userRepository.getUserById(transferManagerDto.getSenderId());
        var receivedUser = userRepository.getUserById(transferManagerDto.getReceivedId());
        var senderUserWallet = walletRepository.getWalletByUser(senderUser);
        var receivedUserWallet = walletRepository.getWalletByUser(receivedUser);

        var transactionDto = new TransactionDto();
        transactionDto.setReceivedUserId(receivedUser.getId());
        transactionDto.setAmount(transferManagerDto.getAmount());

        if (senderUserWallet.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            return new TransactionStatusDto("Не достаточно денег на счете для перевода", TransactionStatus.FAILED, null);
        }

        return runTransaction(transactionDto, senderUserWallet, receivedUserWallet, senderUser, receivedUser, null);
    }

    @Transactional
    public TransactionStatusDto runTransaction(TransactionDto transactionDto, Wallet senderUserWallet, Wallet receivedUserWallet, User senderUser, User receivedUser, Transaction transaction) {
        if (senderUserWallet == null) {
            senderUserWallet = walletRepository.getWalletByUser(senderUser);
        }
        if (receivedUserWallet == null) {
            receivedUserWallet = walletRepository.getWalletByUser(receivedUser);
        }
        try {


            entityManager.lock(senderUserWallet, LockModeType.PESSIMISTIC_WRITE);
            entityManager.lock(receivedUserWallet, LockModeType.PESSIMISTIC_WRITE);

            if (senderUserWallet.getBalance().compareTo(transactionDto.getAmount()) >= 0) {
                senderUserWallet.setBalance(senderUserWallet.getBalance().subtract(transactionDto.getAmount()));
                receivedUserWallet.setBalance(receivedUserWallet.getBalance().add(transactionDto.getAmount()));

                if (transaction == null) {
                    transaction = new Transaction(senderUser, receivedUser, transactionDto.getAmount());
                    transaction.setTransactionStatus(TransactionStatus.PENDING);
                } else {
                    transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                }
                transactionRepository.save(transaction);

                var result = new TransactionStatusDto("Вам перевели " + transactionDto.getAmount().toString() + " ID пользователя: " + receivedUser.getId(), TransactionStatus.COMPLETED, transaction.getId());

                simpMessagingTemplate.convertAndSendToUser(
                        String.valueOf(receivedUser.getId()),
                        "/queue/message",
                        result
                );
                return result;

            } else {

                return new TransactionStatusDto("Не достаточно денег на счете", TransactionStatus.FAILED, null);

            }

        } catch (Exception e) {

            if (transaction == null) {
                transaction = new Transaction(senderUser, userRepository.getUserById(transactionDto.getReceivedUserId()), transactionDto.getAmount());
                transaction.setTransactionStatus(TransactionStatus.PENDING);
                transactionRepository.save(transaction);
            }
            transactionQueue.add(new TransactionData(senderUser, receivedUser, transactionDto, transaction));

            return new TransactionStatusDto("Ожидайте перевода", TransactionStatus.PENDING, transaction.getId());

        }
    }


    public void processTransactionQueue() {

        while (true) {
            if (!transactionQueue.isEmpty()) {
                var transactionData = transactionQueue.poll();

                var transactionStatusDto = runTransaction(transactionData.getTransactionDto(), null, null, transactionData.getSender(), transactionData.getReceived(), transactionData.getTransaction());
                if (transactionStatusDto.getStatus() == TransactionStatus.COMPLETED) {
                    simpMessagingTemplate.convertAndSendToUser(
                            String.valueOf(transactionData.getSender().getId()),
                            "/queue/message",
                            new TransactionStatusDto("Перевод " + transactionData.getTransactionDto().getAmount().toString() + " успешно выполнен ID пользователя: " + transactionData.getReceived().getId(), TransactionStatus.COMPLETED, transactionData.getTransaction().getId())
                    );
                }
            }
        }

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


    @Data
    @AllArgsConstructor
    private class TransactionData {
        private User sender;
        private User received;
        private TransactionDto transactionDto;
        private Transaction transaction;
    }


}