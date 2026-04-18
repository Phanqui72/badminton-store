package com.badminton.store.OOP;

public class PaymentScheduler {
    private String billId;
    private int dayOfMonth;
    private boolean isActive;
    private int scheduledDay;

    public PaymentScheduler(String billId, int dayOfMonth, boolean isActive, int scheduledDay) {
        this.billId = billId;
        this.dayOfMonth = dayOfMonth;
        this.isActive = isActive;
        this.scheduledDay = scheduledDay;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getScheduledDay() {
        return scheduledDay;
    }

    public void setScheduledDay(int scheduledDay) {
        this.scheduledDay = scheduledDay;
    }
}
