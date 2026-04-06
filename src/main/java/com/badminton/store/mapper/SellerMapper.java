package com.badminton.store.mapper;

import com.badminton.store.dto.seller.SellerDto;
import com.badminton.store.form.seller.CreateSellerForm;
import com.badminton.store.form.seller.UpdateSellerForm;
import com.badminton.store.form.seller.UpdateSellerProfileForm;
import com.badminton.store.model.Seller;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface SellerMapper {

    // 1. Map từ CreateForm sang Entity (Lưu vào Account bên trong Seller)
    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "password", target = "account.password")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "gstIn", target = "gstIn")
    Seller fromCreateFormToEntity(CreateSellerForm form);

    // 2. Map từ Entity sang DTO (Lấy dữ liệu từ Account ra DTO)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "account.kind", target = "kind")
    @Mapping(source = "account.username", target = "username")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.status", target = "status")
    @Mapping(source = "account.lastLogin", target = "lastLogin")
    @Mapping(source = "account.avatarPath", target = "avatar")
    @Mapping(source = "account.isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "account.group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "isVerified", target = "isVerified")
    @Mapping(source = "gstIn", target = "gstIn")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDto")
    SellerDto fromEntityToDto(Seller seller);

    // 3. Map để Update Profile
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "gstIn", target = "gstIn")
    void mappingUpdateProfileToEntity(UpdateSellerProfileForm form, @MappingTarget Seller seller);

    @IterableMapping(elementTargetType = SellerDto.class, qualifiedByName = "fromEntityToDto")
    List<SellerDto> fromEntityListToDtoList(List<Seller> list);
}