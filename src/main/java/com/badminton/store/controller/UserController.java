package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.user.UserDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.user.CreateUserForm;
import com.badminton.store.form.user.UpdateUserForm;
import com.badminton.store.mapper.UserMapper;
import com.badminton.store.model.Group;
import com.badminton.store.model.User;
import com.badminton.store.repository.GroupRepository;
import com.badminton.store.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.badminton.store.constant.MgrConstant.USER_KIND_USER;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController extends ABasicController{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('U_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateUserForm createUserForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        User existingUser = userRepository.findExistingUser(
                createUserForm.getUsername(),
                createUserForm.getEmail(),
                createUserForm.getPhone()
        );
        if (existingUser != null) {
            if (StringUtils.equals(existingUser.getAccount().getUsername(), createUserForm.getUsername())) {
                throw new BadRequestException("Username already exists!", ErrorCode.USER_ERROR_USERNAME_EXISTED);
            }
            if (StringUtils.equals(existingUser.getAccount().getEmail(), createUserForm.getEmail())) {
                throw new BadRequestException("Email already exists!", ErrorCode.USER_ERROR_EMAIL_EXISTED);
            }
            if (StringUtils.equals(existingUser.getAccount().getPhone(), createUserForm.getPhone())) {
                throw new BadRequestException("Phone already exists!", ErrorCode.USER_ERROR_PHONE_EXISTED);
            }
        }
        //Set default usser kind and user group
        User user = userMapper.fromCreateUserFormToEntity(createUserForm);
        user.getAccount().setGroup(new Group("User", "Group of User",2,false,null));
        user.getAccount().setKind(USER_KIND_USER);
        userRepository.save(user);

        apiMessageDto.setMessage("Create user success.");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('U_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult) {
        if(!isSuperAdmin() && !updateUserForm.getId().equals(getCurrentUser())){
            throw new BadRequestException("You don't have permission to update user", ErrorCode.USER_ERROR_PERMISSION);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(updateUserForm.getId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND);
        }
        user.getAccount().setFullName(updateUserForm.getFullName());
        user.getAccount().setPhone(updateUserForm.getPhone());
        user.setGender(updateUserForm.getGender());
        if (StringUtils.isNoneBlank(updateUserForm.getAvatarPath())) {
            user.getAccount().setAvatarPath(updateUserForm.getAvatarPath());
        }
        userRepository.save(user);
        apiMessageDto.setMessage("Update user success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('U_V')")
    public ApiMessageDto<UserDto> get(@PathVariable("id") Long id) {
        ApiMessageDto<UserDto> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND);
        }
        apiMessageDto.setData(userMapper.fromUserEntityToDto(user));
        apiMessageDto.setMessage("Get user success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<UserDto> profile() {
        ApiMessageDto<UserDto> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(getCurrentUser()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND);
        }
        apiMessageDto.setData(userMapper.fromUserEntityToDto(user));
        apiMessageDto.setMessage("Get user success.");
        return apiMessageDto;
    }
}
