package com.badminton.store.form.seller;

import com.badminton.store.validation.impl.password.PasswordStrong;
import com.badminton.store.validation.impl.phone.PhoneVN;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "UpdateSellerProfile", description = "Form cập nhật hồ sơ, nhớ thêm password để xác thực đúng người")
@Data
public class UpdateSellerProfileForm {
    @ApiModelProperty(value = "mật khẩu cũ", required = true)
    @NotEmpty(message = "Old password must be provided to verify identity")
    private String oldPassword;

    @ApiModelProperty(value = "mật khẩu mới")
    @PasswordStrong(message = "New password must be strong if you wish to change it")
    private String password; // Để trống nếu không đổi, nhưng nếu nhập thì phải mạnh

    @ApiModelProperty(value = "phone cật nhập")
    @PhoneVN(message = "New phone number must be a valid Vietnam format")
    private String phone;

//    @NotEmpty(message = "Full name must not be empty")
    private String fullName;

//    @NotEmpty(message = "Shop name must not be empty")
    private String shopName;

    private String shopDescription;
    private String gstIn;
    private String avatarPath;
}