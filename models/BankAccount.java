package models;

public class BankAccount {
    private int balance = 3000;

    public int getBalance() {
        return this.balance;
    }
    
    public void deposit(int amount) {
        this.balance += amount;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }
}
