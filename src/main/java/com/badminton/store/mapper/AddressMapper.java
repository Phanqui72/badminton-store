package com.badminton.store.mapper;

import com.badminton.store.dto.address.AddressDto;
import com.badminton.store.dto.nation.NationDto;
import com.badminton.store.form.address.CreateAddressForm;
import com.badminton.store.form.address.UpdateAddressForm;
import com.badminton.store.form.user.UpdateUserForm;
import com.badminton.store.model.Address;

import com.badminton.store.model.Nation;
import org.mapstruct.*;

import java.util.List;
import java.util.Locale;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {NationMapper.class})
public interface AddressMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "provinceId", target = "province.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "communeId", target = "commune.id")
    Address fromCreateFormToEntity(CreateAddressForm form);

    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "provinceId", target = "province.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "id", target = "id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromForm(UpdateAddressForm form, @MappingTarget Address entity);

    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "id", target = "id")
    AddressDto fromEntityToDto(Address address);


    // MapStruct sẽ tự động gọi hàm fromEntityToDto cho từng phần tử trong list
    List<AddressDto> fromEntityListToDtoList(List<Address> address);

}
