package ru.antonsibgatulin.bankingservice.dto.transaction;

import java.math.BigDecimal;

public class TransactionDto {
    private Long received;
    private String phoneReceived;
    private Long receivedUserId;
    private BigDecimal amount;
}
