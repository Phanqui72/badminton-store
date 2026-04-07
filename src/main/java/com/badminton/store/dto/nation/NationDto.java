package com.badminton.store.dto.nation;

import com.badminton.store.model.Nation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NationDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "parent")
    private NationDto parent;
}
