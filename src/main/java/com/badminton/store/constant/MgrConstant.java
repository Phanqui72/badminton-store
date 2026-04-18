package com.badminton.store.constant;

public class MgrConstant {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_SELLER = 3;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    // Username: 4-50 ký tự, chỉ cho phép chữ cái, số và dấu gạch dưới
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{4,50}$";

    // Password mạnh: Ít nhất 8 ký tự, bao gồm chữ HOA, chữ thường, số và ký tự đặc biệt (@$!%*?&)
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    // Số điện thoại Việt Nam: Hỗ trợ đầu 0 hoặc +84, tiếp theo là 9 chữ số (Tổng 10 số)
    public static final String REGEX_PHONE_VN = "^(0|\\+84)(\\d{9})$";

    // Order Status
    public static final int ORDER_STATUS_PENDING = 0;   // Chờ xác nhận
    public static final int ORDER_STATUS_CONFIRMED = 1; // Đã xác nhận
    public static final int ORDER_STATUS_SHIPPING = 2;  // Đang giao hàng
    public static final int ORDER_STATUS_COMPLETED = 3; // Hoàn thành
    public static final int ORDER_STATUS_CANCELLED = -1; // Đã hủy

    // Product Status
    public static final int PRODUCT_STATUS_CANCELLED = 0;   // Chờ xác nhận
    public static final int PRODUCT_STATUS_ACTIVE = 1; // Đã xác nhận

    // Coupon Type
    public static final int COUPON_TYPE_FIXED = 1;      // Giảm tiền mặt
    public static final int COUPON_TYPE_PERCENTAGE = 2; // Giảm %

    // Order Constant
    public static final double TOTAL_ORDER_PRICE_DEFAULT = 0.0;
    public static final int TOTAL_ITEM_DEFAULT = 0;

    private MgrConstant() {
        throw new IllegalStateException("Utility class");
    }
}
