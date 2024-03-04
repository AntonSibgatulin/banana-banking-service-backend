package ru.antonsibgatulin.bankingservice.entity.transaction;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.antonsibgatulin.bankingservice.entity.user.User;

import java.math.BigDecimal;
import java.util.Date;


@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User received;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private Date transactionTime;


    public Transaction(User sender, User received, BigDecimal amount) {
        this.sender = sender;
        this.received = received;
        this.amount = amount;
        transactionStatus = TransactionStatus.COMPLETED;
        transactionTime = new Date(System.currentTimeMillis());
    }
}
