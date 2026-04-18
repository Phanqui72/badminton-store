package com.badminton.store.form.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCategoryForm {
    @NotNull(message = "id is required")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotEmpty(message = "name is required")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "status")
    private Integer status;
}