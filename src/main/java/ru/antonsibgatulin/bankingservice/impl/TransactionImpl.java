package ru.antonsibgatulin.bankingservice.impl;

import ru.antonsibgatulin.bankingservice.dto.transaction.response.TransactionObjectDto;
import ru.antonsibgatulin.bankingservice.entity.transaction.Transaction;
import ru.antonsibgatulin.bankingservice.mapper.TransactionMapper;

import java.util.ArrayList;
import java.util.List;

public class TransactionImpl implements TransactionMapper {
    @Override
    public TransactionObjectDto getTransactionObjectFromTransaction(Transaction transaction) {

        TransactionObjectDto transactionObjectDto = new TransactionObjectDto();
        transactionObjectDto.setReceivedPhone(transaction.getReceived().getPhoneNumbers().get(0).getNumber());
        transactionObjectDto.setSenderPhone(transaction.getSender().getPhoneNumbers().get(0).getNumber());
        transactionObjectDto.setSenderId(transaction.getSender().getId());
        transactionObjectDto.setReceivedId(transaction.getReceived().getId());
        transactionObjectDto.setAmount(transaction.getAmount());
        transactionObjectDto.setStatus(transaction.getTransactionStatus());
        transactionObjectDto.setTransaction_time(transaction.getTransactionTime());
        return transactionObjectDto;

    }

    @Override
    public List<TransactionObjectDto> fromListTransactionToTransactionObjectDto(List<Transaction> transactions) {
        List<TransactionObjectDto> transactionObjectDtoList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionObjectDtoList.add(getTransactionObjectFromTransaction(transaction));
        }
        return transactionObjectDtoList;
    }

}
