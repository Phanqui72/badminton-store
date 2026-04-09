package com.badminton.store.mapper;

import com.badminton.store.dto.user.UserDto;
import com.badminton.store.form.user.CreateUserForm;
import com.badminton.store.form.user.UpdateUserForm;
import com.badminton.store.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import javax.validation.Valid;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AddressMapper.class})
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "address", target = "address",qualifiedByName = "fromEntityToAddressMapper")
    UserDto fromUserEntityToDto(User User);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "address", target = "address",qualifiedByName = "fromEntityToAddressMapper")
    User fromUserDtoToEntity(UserDto userDto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "addressDtoList", target = "address",qualifiedByName = "fromEntityToAddressDtoList")
    User fromCreateUserFormToEntity(@Valid CreateUserForm createUserForm);


    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "addressDtoList", target = "address",qualifiedByName = "fromEntityToAddressDtoList")
    User fromUpdateUserFormToEntity(@Valid UpdateUserForm form);
}
