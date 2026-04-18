package com.badminton.store.OOP;

public class Wallet {
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Nạp tiền thành công!");
        }
    }
    public boolean pay(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
