package com.example.main.models;

public class BankAccount {
    private int balance = 300000;
    private int fardaeiDollar = 0;

    public int getBalance() {
        return this.balance;
    }
    
    public void deposit(int amount) {
        this.balance += amount;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public void setFardaeiDollar(int amount) {
        this.fardaeiDollar = amount;
    }

    public void depositFardaei() {
        this.balance += this.fardaeiDollar;
        this.fardaeiDollar = 0;
    }
}
