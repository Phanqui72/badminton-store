package com.badminton.store.form.user;

import com.badminton.store.enums.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateUserForm {
    @NotNull(message = "User ID is required")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "fullName")
    private String fullName;

    @ApiModelProperty(name = "phone")
    private String phone;

    @ApiModelProperty(name = "gender")
    private Gender gender;

    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;
}
