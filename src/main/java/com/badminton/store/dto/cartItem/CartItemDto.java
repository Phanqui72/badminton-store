package com.badminton.store.dto.cartItem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CartItemDto", description = "Chi tiết từng sản phẩm trong giỏ")
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productTitle;
    private String productImage; // Lấy ảnh đầu tiên
    private Integer quantity;
    private Double price;
    @ApiModelProperty(notes = "Thành tiền của item này")
    private Double subTotal;
}