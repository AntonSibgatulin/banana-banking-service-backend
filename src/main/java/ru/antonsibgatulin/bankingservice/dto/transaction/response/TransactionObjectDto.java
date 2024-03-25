package ru.antonsibgatulin.bankingservice.dto.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.antonsibgatulin.bankingservice.entity.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionObjectDto {
    private String receivedPhone;
    private String senderPhone;
    private Long senderId;
    private Long receivedId;
    private BigDecimal amount;
    private TransactionStatus status;
    private Date transaction_time;

}
