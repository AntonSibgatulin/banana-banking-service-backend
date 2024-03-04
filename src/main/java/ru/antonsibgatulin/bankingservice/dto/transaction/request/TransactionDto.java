package ru.antonsibgatulin.bankingservice.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Transaction DTO")
public class TransactionDto {
    @Schema(description = "ID of the received wallet")
    private Long receivedWalletId;

    @Schema(description = "Phone number of the receiver")
    private String phoneReceived;

    @Schema(description = "ID of the received user")
    private Long receivedUserId;

    @DecimalMin(value = "1.0", inclusive = false, message = "Amount must be greater than one")
    @Schema(description = "Amount to be transferred")
    private BigDecimal amount;
}