package ru.antonsibgatulin.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antonsibgatulin.bankingservice.entity.transaction.Transaction;
import ru.antonsibgatulin.bankingservice.entity.user.User;

import java.math.BigDecimal;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findBySenderAndReceivedAndAmount(User userByUsername, User userById, BigDecimal amount);
}
