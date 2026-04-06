package com.badminton.store.form.address;

import javax.validation.constraints.NotNull;

public class UpdateAddressForm {
    @NotNull(message = "Address id is required")
    private Long id;

    private String street;
    private String zipCode;
    private Boolean isDefault;

    private Long provinceId;
    private Long districtId;
    private Long communeId;
}
