package com.badminton.store.form.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "AddToCartForm", description = "Form thêm sản phẩm vào giỏ")
public class AddToCartForm {
    @NotNull(message = "Product ID must not be null")
    @ApiModelProperty(required = true, example = "9382966602661888")
    private Long productId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @ApiModelProperty(required = true, example = "1")
    private Integer quantity;
}