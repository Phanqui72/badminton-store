package com.badminton.store.mapper;

import com.badminton.store.dto.address.AddressDto;
import com.badminton.store.form.address.CreateAddressForm;
import com.badminton.store.form.address.UpdateAddressForm;
import com.badminton.store.form.user.UpdateUserForm;
import com.badminton.store.model.Address;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {NationMapper.class})
public interface AddressMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "province", target = "province")
    @Mapping(source = "district", target = "district")
    @Mapping(source = "commune", target = "commune")
    @Mapping(source = "user.id", target = "userId")
    @Named("fromEntityToAddressMapper")
    AddressDto fromEntityToAddressMapper(Address address);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToNationDto")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToNationDto")
    @Mapping(source = "commune", target = "commune", qualifiedByName = "fromEntityToNationDto")
    @Mapping(source = "userId", target = "user.id")
    Address fromAdressDtoToEntity(AddressDto addressDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToNationDto")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToNationDto")
    @Mapping(source = "commune", target = "commune", qualifiedByName = "fromEntityToNationDto")
    @Mapping(source = "user.id", target = "userId")
    AddressDto fromAddressEntityToDto(Address address);

    @IterableMapping(qualifiedByName = "fromEntityToAddressMapper")
    List<AddressDto> toAddressDtoList(List<Address> addresses);

    @Mapping(source = "street", target = "street")
    @Mapping(source = "provinceId", target = "province.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "userId", target = "user.id")
    Address fromCreateFormToEntity(CreateAddressForm form);

}
