package ru.antonsibgatulin.bankingservice.blocking;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.Callable;

import java.util.concurrent.ExecutionException;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

import java.util.concurrent.Future;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountTest {

    @Test
    public void testThreadSafety() throws InterruptedException, ExecutionException {

        Account account = new Account(100);
        int numberOfThreads = 100;
        int numberOfOperations = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            tasks.add(() -> {
                for (int j = 0; j < numberOfOperations; j++) {
                    account.deposit(1);
                    account.withdraw(1);
                }
                return null;
            });
        }

        List<Future<Void>> futures = executorService.invokeAll(tasks);

        for (Future<Void> future : futures) {
            future.get();
        }

        executorService.shutdown();
        assertEquals(100, account.getBalance());
    }

}