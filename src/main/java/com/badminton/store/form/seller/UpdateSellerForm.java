package com.badminton.store.form.seller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(value = "UpdateSellerForm", description = "Form cập nhật thông tin Seller")
public class UpdateSellerForm {

    @ApiModelProperty(value = "ID của seller", required = true)
    private Long id;

    @ApiModelProperty(value = "Mật khẩu mới (bỏ trống nếu không đổi)")
    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;

    @ApiModelProperty(value = "Tên đầy đủ")
    private String fullName;

    @ApiModelProperty(value = "Group ID")
    private Long groupId;

    @ApiModelProperty(value = "Trạng thái: 1-Active, 0-Pending, -1-Deleted")
    private Integer status;

    @ApiModelProperty(value = "Đường dẫn đến avatar")
    private String avatarPath;

    @ApiModelProperty(value = "Tên shop")
    private String shopName;

    @ApiModelProperty(value = "Mô tả shop")
    private String shopDescription;

    @ApiModelProperty(value = "Mã số thuế")
    private String gstIn;
}