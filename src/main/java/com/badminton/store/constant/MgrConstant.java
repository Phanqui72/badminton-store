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

    private MgrConstant() {
        throw new IllegalStateException("Utility class");
    }
}
