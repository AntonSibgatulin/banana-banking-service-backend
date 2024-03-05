package ru.antonsibgatulin.bankingservice.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Schema(description = "Transfer Manager DTO")
public class TransferManagerDto {

    @Schema(description = "ID of the sender")
    private Long senderId;

    @Schema(description = "ID of the receiver")
    private Long receivedId;

    @DecimalMin(value = "1.0", inclusive = false, message = "Amount must be greater than one")
    @Schema(description = "Amount to be transferred")
    private BigDecimal amount;

    public TransferManagerDto(Long senderId, Long receivedId, BigDecimal amount) {
        this.senderId = senderId;
        this.receivedId = receivedId;
        this.amount = amount;
    }
}