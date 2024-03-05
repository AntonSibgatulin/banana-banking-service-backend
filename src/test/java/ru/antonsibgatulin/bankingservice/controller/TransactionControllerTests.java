package ru.antonsibgatulin.bankingservice.controller;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.antonsibgatulin.bankingservice.controller.transaction.TransactionController;
import ru.antonsibgatulin.bankingservice.controller.transaction.TransactionService;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransactionDto;
import ru.antonsibgatulin.bankingservice.dto.transaction.response.TransactionStatusDto;
import ru.antonsibgatulin.bankingservice.entity.transaction.TransactionStatus;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;


public class TransactionControllerTests {


    @Mock
    private TransactionService transactionService;


    @InjectMocks
    private TransactionController transactionController;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testTrans_Success() {
        // Arrange

        String username = "testUser";

        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setAmount(BigDecimal.TEN);

        Authentication authentication = new Authentication() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("USER"));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override

            public String getName() {

                return username;

            }

        };


        TransactionStatusDto transactionStatusDto = new TransactionStatusDto();

        transactionStatusDto.setStatus(TransactionStatus.COMPLETED);

        when(transactionService.trans(authentication, transactionDto)).thenReturn(transactionStatusDto);


        // Act

        ResponseEntity<TransactionStatusDto> response = transactionController.trans(transactionDto, authentication);


        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(transactionStatusDto, response.getBody());

    }


    @Test
    public void testTrans_Failed() {

        // Arrange
        String username = "testUser";
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.TEN);
        Authentication authentication = new Authentication() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("USER"));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override

            public String getName() {

                return username;

            }

        };
        TransactionStatusDto transactionStatusDto = new TransactionStatusDto();
        transactionStatusDto.setStatus(TransactionStatus.FAILED);
        when(transactionService.trans(authentication, transactionDto)).thenReturn(transactionStatusDto);
        // Act

        ResponseEntity<TransactionStatusDto> response = transactionController.trans(transactionDto, authentication);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(transactionStatusDto, response.getBody());

    }


}