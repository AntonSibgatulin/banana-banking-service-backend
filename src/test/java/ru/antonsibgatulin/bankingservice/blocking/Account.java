package ru.antonsibgatulin.bankingservice.blocking;

public class Account {

    private int balance;


    public Account(int balance) {

        this.balance = balance;

    }


    public synchronized void deposit(int amount) {

        balance += amount;

    }


    public synchronized void withdraw(int amount) {

        if (balance >= amount) {

            balance -= amount;

        } else {

            throw new IllegalArgumentException("Insufficient balance");

        }

    }


    public int getBalance() {

        return balance;

    }

}