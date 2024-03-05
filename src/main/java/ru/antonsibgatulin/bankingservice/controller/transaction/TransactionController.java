package ru.antonsibgatulin.bankingservice.controller.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransactionDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransferManagerDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.response.TransactionStatusDto;
import ru.antonsibgatulin.bankingservice.entity.transaction.TransactionStatus;

@Validated
@Tag(name = "Transaction Controller", description = "API for managing transactions")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {


    private final TransactionService transactionService;

    @SecurityRequirement(name = "JWT")
    @PostMapping("/")
    @Operation(summary = "Transfer money", description = "Transfers money between two users from self to him")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Transaction successful"), @ApiResponse(responseCode = "400", description = "Invalid transaction data"), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404", description = "User not found"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<TransactionStatusDto> trans(@RequestBody TransactionDto transactionDto, Authentication authentication) {
        TransactionStatusDto transactionStatusDto = transactionService.trans(authentication, transactionDto);

        if (transactionStatusDto.getStatus() == TransactionStatus.FAILED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(transactionStatusDto);
        } else {
            return ResponseEntity.ok(transactionStatusDto);
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Transfer money by ids", description = "Transfers money between two users by ids")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Transaction successful"), @ApiResponse(responseCode = "400", description = "Invalid transaction data"), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404", description = "User not found"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/transfer")
    public ResponseEntity<TransactionStatusDto> transfer(@RequestBody TransferManagerDto transferManagerDto) {
        TransactionStatusDto transactionStatusDto = transactionService.trans(transferManagerDto);
        if (transactionStatusDto.getStatus() == TransactionStatus.FAILED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(transactionStatusDto);
        } else {
            return ResponseEntity.ok(transactionStatusDto);
        }

    }

}