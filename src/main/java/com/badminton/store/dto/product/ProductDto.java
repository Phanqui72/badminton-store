package com.badminton.store.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(value = "ProductDto", description = "Thông tin chi tiết sản phẩm trả về")
public class ProductDto {
    @ApiModelProperty(notes = "ID sản phẩm")
    private Long id;
    @ApiModelProperty(notes = "Tiêu đề sản phẩm")
    private String title;
    @ApiModelProperty(notes = "Mô tả sản phẩm")
    private String description;
    @ApiModelProperty(notes = "Giá bán")
    private Double price;
    @ApiModelProperty(notes = "Số lượng trong kho")
    private Integer quantity;
    @ApiModelProperty(notes = "Danh sách URL hình ảnh")
    private List<String> images;
    @ApiModelProperty(notes = "ID của người bán")
    private Long sellerId;
    @ApiModelProperty(notes = "Tên shop bán hàng")
    private String shopName;
    @ApiModelProperty(notes = "Trạng thái (1: Active, -2: Delete)")
    private Integer status;
}