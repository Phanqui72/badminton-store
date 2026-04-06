package com.badminton.store.dto.seller;

import com.badminton.store.dto.account.AccountDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SellerDto extends AccountDto {
    @ApiModelProperty(notes = "Tên cửa hàng")
    private String shopName;
    @ApiModelProperty(notes = "Mô tả cửa hàng")
    private String shopDescription;
    @ApiModelProperty(notes = "Trạng thái xác thực")
    private Boolean isVerified;
    @ApiModelProperty(notes = "Mã số thuế/GST")
    private String gstIn;
}