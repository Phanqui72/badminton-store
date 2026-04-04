package com.badminton.store.form.seller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(value = "CreateSellerForm", description = "Form đăng ký tài khoản Seller mới")
public class CreateSellerForm {

    // Account field
    @NotEmpty(message = "username cannot be empty")
    @Size(min = 4, max = 50, message = "username must be between 4 and 50 characters")
    @ApiModelProperty(value = "Tên đăng nhập", required = true, example = "seller01")
    private String username;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 6, max = 100, message = "password must be at least 6 characters")
    @ApiModelProperty(value = "Mật khẩu", required = true, example = "123456")
    private String password;

    @Email(message = "Invalid email format")
    @ApiModelProperty(value = "Email tài khoản", example = "seller@gmail.com")
    private String email;

    @Pattern(regexp = "^[0-9]{9,11}$", message = "Invalid phone number")
    @ApiModelProperty(value = "Số điện thoại", example = "0987654321")
    private String phone;

    @NotEmpty(message = "fullName cannot be empty")
    @ApiModelProperty(value = "Họ và tên", required = true, example = "Nguyen Van Seller")
    private String fullName;

    @NotNull(message = "groupId cannot be null")
    @ApiModelProperty(value = "ID nhóm quyền", required = true)
    private Long groupId;

    // ================= SELLER =================
    @NotEmpty(message = "shopName cannot be empty")
    @ApiModelProperty(value = "Tên shop", required = true, example = "Badminton World Shop")
    private String shopName;

    @ApiModelProperty(value = "Mô tả shop", example = "Chuyên cung cấp vợt cầu lông chính hãng")
    private String shopDescription;

    @ApiModelProperty(value = "Mã số thuế", example = "GST123456789")
    private String gstIn;
}