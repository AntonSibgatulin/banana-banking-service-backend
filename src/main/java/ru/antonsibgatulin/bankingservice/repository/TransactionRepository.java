package ru.antonsibgatulin.bankingservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.antonsibgatulin.bankingservice.entity.transaction.Transaction;
import ru.antonsibgatulin.bankingservice.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findBySenderAndReceivedAndAmount(User userByUsername, User userById, BigDecimal amount);

    List<Transaction> findAllByReceivedOrSenderOrderByTransactionTimeDesc(User received, User sender, Pageable pageable);
    List<Transaction> findAllByReceivedOrSender(User received, User sender, Pageable pageable);
}
