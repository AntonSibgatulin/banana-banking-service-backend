package ru.antonsibgatulin.bankingservice.dto.transaction.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.antonsibgatulin.bankingservice.entity.transaction.TransactionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transaction Status DTO")
public class TransactionStatusDto {
    @Schema(description = "Message describing the transaction status")
    private String message;

    @Schema(description = "Transaction status")
    private TransactionStatus status;

    @Schema(description = "ID of the transaction")
    private Long transactionId;
}