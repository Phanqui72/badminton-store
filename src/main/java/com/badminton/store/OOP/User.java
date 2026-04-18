package com.badminton.store.OOP;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private Wallet wallet;
    private List<Bill> bills;
    private List<PaymentScheduler> schedulers;

    public User(String username) {
        this.username = username;
        this.wallet = new Wallet(); // Tạo ví mới khi tạo User
        this.bills = new ArrayList<>();
        this.schedulers = new ArrayList<>();
    }

    // Các phương thức để User thực hiện hành động
    public void addBill(Bill bill) {
        this.bills.add(bill);
    }

    public void createSchedule(String billId, int day, boolean isActive, int scheduled) {
        this.schedulers.add(new PaymentScheduler(billId, day,isActive,scheduled ));
    }
}
