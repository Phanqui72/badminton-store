package com.badminton.store.form.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.*;
import java.util.List;

@Data
@ApiModel(value = "CreateProductForm", description = "Form tạo mới sản phẩm")
public class CreateProductForm {
    @NotEmpty(message = "Title must not be empty")
    @ApiModelProperty(required = true, example = "Vợt Cầu Lông Yonex Astrox 88D")
    private String title;

    @ApiModelProperty(example = "Mô tả chi tiết về sản phẩm...")
    private String description;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be >= 0")
    @ApiModelProperty(required = true, example = "2500000")
    private Double price;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be >= 0")
    @ApiModelProperty(required = true, example = "50")
    private Integer quantity;

    @ApiModelProperty(notes = "Danh sách link ảnh sản phẩm")
    private List<String> images;
}