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
            if (StringUtils.equals(existingUser.getUsername(), createUserForm.getUsername())) {
                throw new BadRequestException("Username already exists!", ErrorCode.USER_ERROR_USERNAME_EXISTED);
            }
            if (StringUtils.equals(existingUser.getEmail(), createUserForm.getEmail())) {
                throw new BadRequestException("Email already exists!", ErrorCode.USER_ERROR_EMAIL_EXISTED);
            }
            if (StringUtils.equals(existingUser.getPhone(), createUserForm.getPhone())) {
                throw new BadRequestException("Phone already exists!", ErrorCode.USER_ERROR_PHONE_EXISTED);
            }
        }
        //Check group
        Group group = groupRepository.findById(createUserForm.getGroupId())
                .orElseThrow(() -> new NotFoundException("Group not found!", ErrorCode.GROUP_ERROR_NOT_FOUND));

        User user = userMapper.fromCreateUserFormToEntity(createUserForm);
        user.setGroup(group);
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
        user.setFullName(updateUserForm.getFullName());
        user.setPhone(updateUserForm.getPhone());
        user.setGender(updateUserForm.getGender());
        if (StringUtils.isNoneBlank(updateUserForm.getAvatarPath())) {
            user.setAvatarPath(updateUserForm.getAvatarPath());
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
