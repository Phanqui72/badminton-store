package com.badminton.store.mapper;

import com.badminton.store.dto.seller.SellerDto;
import com.badminton.store.form.seller.CreateSellerForm;
import com.badminton.store.form.seller.UpdateSellerForm;
import com.badminton.store.model.Seller;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface SellerMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "gstIn", target = "gstIn")
    Seller fromCreateFormToEntity(CreateSellerForm form);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "gstIn", target = "gstIn")
    void mappingUpdateFormToEntity(UpdateSellerForm form, @MappingTarget Seller seller);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "isVerified", target = "isVerified")
    @Mapping(source = "gstIn", target = "gstIn")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDto")
    SellerDto fromEntityToDto(Seller seller);

    @IterableMapping(elementTargetType = SellerDto.class, qualifiedByName = "fromEntityToDto")
    List<SellerDto> fromEntityListToDtoList(List<Seller> list);
}