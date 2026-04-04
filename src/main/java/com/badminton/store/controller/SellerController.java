package com.badminton.store.controller;

import com.badminton.store.constant.MgrConstant;
import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ApiResponse;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.seller.SellerDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.seller.CreateSellerForm;
import com.badminton.store.form.seller.UpdateSellerForm;
import com.badminton.store.mapper.SellerMapper;
import com.badminton.store.model.Account;
import com.badminton.store.model.Group;
import com.badminton.store.model.Seller;
import com.badminton.store.repository.AccountRepository;
import com.badminton.store.repository.GroupRepository;
import com.badminton.store.repository.SellerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.badminton.store.constant.MgrConstant.*;

@RestController
@RequestMapping("/v1/seller")
@Api(tags = "Seller Controller")
public class SellerController extends ABasicController {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SellerMapper sellerMapper;

    @ApiOperation(value = "Tạo tài khoản Seller")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_C')")
    @Transactional
    public ApiResponse<String> create(@Valid @RequestBody CreateSellerForm form, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        if (accountRepository.existsByUsername(form.getUsername())) {
            throw new BadRequestException("Username is existed!", ErrorCode.ACCOUNT_ERROR_USERNAME_EXISTED);
        }

        if (StringUtils.isNoneBlank(form.getEmail()) && accountRepository.existsByEmail(form.getEmail())) {
            throw new BadRequestException("Email is existed!", ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED);
        }

        Group group = groupRepository.findById(form.getGroupId())
                .orElseThrow(() -> new NotFoundException("Group not found!", ErrorCode.GROUP_ERROR_NOT_FOUND));

        Seller seller = sellerMapper.fromCreateFormToEntity(form);
        seller.setPassword(passwordEncoder.encode(form.getPassword()));
        seller.setKind(USER_KIND_SELLER);
        seller.setGroup(group);
        seller.setStatus(STATUS_ACTIVE);

        sellerRepository.save(seller);
        apiMessageDto.setMessage("Create seller success.");
        return apiMessageDto;
    }

    @ApiOperation(value = "Cập nhật tài khoản Seller")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_U')")
    @Transactional
    public ApiResponse<String> update(@Valid @RequestBody UpdateSellerForm form, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        Seller seller = sellerRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Seller not found!", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        Group group = groupRepository.findById(form.getGroupId())
                .orElseThrow(() -> new NotFoundException("Group not found!", ErrorCode.GROUP_ERROR_NOT_FOUND));

        sellerMapper.mappingUpdateFormToEntity(form, seller);

        if (StringUtils.isNoneBlank(form.getPassword())) {
            seller.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        seller.setGroup(group);
        sellerRepository.save(seller);

        apiMessageDto.setMessage("Update seller success.");
        return apiMessageDto;
    }

    @ApiOperation(value = "Lấy chi tiết Seller")
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_V')")
    public ApiResponse<SellerDto> get(@PathVariable Long id) {
        ApiResponse<SellerDto> apiMessageDto = new ApiResponse<>();
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found!", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        apiMessageDto.setData(sellerMapper.fromEntityToDto(seller));
        apiMessageDto.setMessage("Get seller success.");
        return apiMessageDto;
    }

    @ApiOperation(value = "Xóa tài khoản Seller (Soft delete)")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_D')")
    @Transactional
    public ApiMessageDto<Void> delete(@PathVariable Long id) {
        // 1. Tìm kiếm Seller
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found!", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        // 2. Cập nhật trạng thái về -2 (STATUS_DELETE)
        seller.setStatus(STATUS_DELETE);
        sellerRepository.save(seller);

        // 3. Trả về thành công sử dụng helper từ ABasicController
        // Hàm này sẽ tạo ApiMessageDto với data = null
        return makeSuccessResponse("Delete seller success.");
    }

    @ApiOperation(value = "Danh sách Seller")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_L')")
    public ApiResponse<ResponseListDto<List<SellerDto>>> list(Pageable pageable) {
        ApiResponse<ResponseListDto<List<SellerDto>>> apiMessageDto = new ApiResponse<>();

        Page<Seller> page = sellerRepository.findAll(pageable);

        // Sử dụng helper makeResponseListDto từ ABasicController
        ResponseListDto<List<SellerDto>> responseListDto = makeResponseListDto(page, sellerMapper::fromEntityListToDtoList);

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list seller success.");
        return apiMessageDto;
    }
}