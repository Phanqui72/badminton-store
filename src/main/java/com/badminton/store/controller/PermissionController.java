package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.permission.PermissionDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.form.permission.CreatePermissionForm;
import com.badminton.store.mapper.PermissionMapper;
import com.badminton.store.model.Permission;
import com.badminton.store.model.criteria.PermissionCriteria;
import com.badminton.store.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/permission")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@ApiIgnore
public class PermissionController extends ABasicController {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PER_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreatePermissionForm createPermissionForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Permission permission = permissionRepository.findFirstByName(createPermissionForm.getName());
        if (permission != null) {
            throw new BadRequestException("Permission name is existed!", ErrorCode.PERMISSION_ERROR_NAME_EXISTED);
        }
        boolean permissionCodeExist = permissionRepository.existsByPermissionCode(createPermissionForm.getPermissionCode());
        if (permissionCodeExist) {
            throw new BadRequestException("Permission code is existed!", ErrorCode.PERMISSION_ERROR_CODE_EXISTED);
        }
        permission = permissionMapper.fromCreatePermissionFormToEntity(createPermissionForm);
        permissionRepository.save(permission);
        apiMessageDto.setMessage("Create a new permission success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PER_L')")
    public ApiMessageDto<ResponseListDto<PermissionDto>> listPermissions(PermissionCriteria permissionCriteria) {
        ApiMessageDto<ResponseListDto<PermissionDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Permission> page = permissionRepository.findAll(permissionCriteria.getSpecification(), PageRequest.of(0, 1000, Sort.by(new Sort.Order(Sort.Direction.DESC, "createdDate"))));
        ResponseListDto<PermissionDto> responseListDto = new ResponseListDto(permissionMapper.fromEntityToPermissionDtoList(page.getContent()), page.getTotalElements(), page.getTotalPages());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("List permissions success.");
        return apiMessageDto;
    }
}
