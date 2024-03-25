package ru.antonsibgatulin.bankingservice.mapper;

import ru.antonsibgatulin.bankingservice.dto.transaction.response.TransactionObjectDto;
import ru.antonsibgatulin.bankingservice.entity.transaction.Transaction;

import java.util.List;

public interface TransactionMapper {
    TransactionObjectDto getTransactionObjectFromTransaction(Transaction transaction);
    List<TransactionObjectDto> fromListTransactionToTransactionObjectDto(List<Transaction> transactions);
}
