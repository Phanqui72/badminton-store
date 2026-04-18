package com.badminton.store.OOP;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EWalletService {
    private Wallet userWallet = new Wallet();
    private List<Bill> bills = new ArrayList<>();
    private List<PaymentScheduler> schedules = new ArrayList<>();

    // Tính năng: Xem các hóa đơn sắp tới hạn (trong vòng 1 tháng)
    public List<Bill> getUpcomingBills() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);

        return bills.stream()
                .filter(b -> !b.isPaid() &&
                        b.getDueDate().isAfter(today) &&
                        b.getDueDate().isBefore(nextMonth))
                .collect(Collectors.toList());
    }

    // Tính năng: Xử lý Scheduler (Chạy định kỳ)
    public void processScheduledPayments() {
        int today = LocalDate.now().getDayOfMonth();
        for (PaymentScheduler s : schedules) {
            if (s.getScheduledDay() == today) {
                // Thực hiện logic thanh toán tự động ở đây
            }
        }
    }
}
