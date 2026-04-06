package com.badminton.store.dto.address;

import com.badminton.store.dto.nation.NationDto;
import com.badminton.store.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "street")
    private String street;
    @ApiModelProperty(name = "zipCode")
    private String zipCode;
    @ApiModelProperty(name = "province")
    private NationDto province;
    @ApiModelProperty(name = "district")
    private NationDto district;
    @ApiModelProperty(name = "commune")
    private NationDto commune;
    @ApiModelProperty(name = "isDefault")
    private Boolean isDefault;
    @ApiModelProperty(name = "user_id")
    private Long userId;


}
