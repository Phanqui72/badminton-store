package com.badminton.store.form.order;

import com.badminton.store.validation.impl.phone.PhoneVN;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateOrderForm {
    @NotEmpty(message = "Shipping address is required")
    private String shippingAddress;
    @NotEmpty(message = "Receiver name is required")
    private String receiverName;
    @NotEmpty(message = "Phone is required")
    @PhoneVN // Custom annotation bạn đã tạo
    private String receiverPhone;
    private String couponCode;
}