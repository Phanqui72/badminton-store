package com.badminton.store.dto.cart;

import com.badminton.store.dto.cartItem.CartItemDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "CartDto", description = "Thông tin toàn bộ giỏ hàng")
public class CartDto {
    @ApiModelProperty(notes = "ID giỏ hàng (trùng ID User)")
    private Long id;
    @ApiModelProperty(notes = "Danh sách sản phẩm trong giỏ")
    private List<CartItemDto> items;
    @ApiModelProperty(notes = "Tổng tiền thanh toán")
    private Double totalPrice;
    @ApiModelProperty(notes = "Tổng số lượng sản phẩm")
    private Integer totalItem;
}