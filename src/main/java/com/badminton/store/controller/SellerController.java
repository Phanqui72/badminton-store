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
import com.badminton.store.form.seller.UpdateSellerProfileForm;
import com.badminton.store.mapper.SellerMapper;
import com.badminton.store.model.Account;
import com.badminton.store.model.Group;
import com.badminton.store.model.Seller;
import com.badminton.store.model.criteria.SellerCriteria;
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

    @Autowired private SellerRepository sellerRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SellerMapper sellerMapper;

    @ApiOperation(value = "Tạo mới tài khoản Seller")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_C')")
    @Transactional
    public ApiMessageDto<Void> create(@Valid @RequestBody CreateSellerForm form, BindingResult bindingResult) {

        // 1. Kiểm tra Username tồn tại
        if (accountRepository.existsByUsername(form.getUsername())) {
            throw new BadRequestException("Username is already existed!", ErrorCode.ACCOUNT_ERROR_USERNAME_EXISTED);
        }

        // 2. Kiểm tra Email tồn tại (Chỉ check nếu form có gửi email lên)
        if (StringUtils.isNotBlank(form.getEmail()) && accountRepository.existsByEmail(form.getEmail())) {
            throw new BadRequestException("Email is already existed!", ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED);
        }

        // 3. Kiểm tra Số điện thoại tồn tại (Chỉ check nếu form có gửi phone lên)
        if (StringUtils.isNotBlank(form.getPhone()) && accountRepository.existsByPhone(form.getPhone())) {
            throw new BadRequestException("Phone number is already existed!", ErrorCode.ACCOUNT_ERROR_PHONE_EXISTED);
        }

        // 4. Kiểm tra Group quyền
        Group group = groupRepository.findById(form.getGroupId())
                .orElseThrow(() -> new NotFoundException("Group not found!", ErrorCode.GROUP_ERROR_NOT_FOUND));

        // 5. Khởi tạo và lưu thông tin Account trước
        Account account = new Account();
        account.setUsername(form.getUsername());
        account.setPassword(passwordEncoder.encode(form.getPassword()));
        account.setEmail(form.getEmail());
        account.setPhone(form.getPhone());
        account.setFullName(form.getFullName());
        account.setKind(USER_KIND_SELLER); // Thường là 3
        account.setGroup(group);
        account.setStatus(STATUS_ACTIVE); // Thường là 1
        accountRepository.save(account);

        // 6. Khởi tạo và lưu thông tin Seller (Liên kết qua @MapsId)
        Seller seller = new Seller();
        seller.setAccount(account); // Gán quan hệ 1-1, ID của Seller sẽ tự động lấy từ ID của Account vừa save
        seller.setShopName(form.getShopName());
        seller.setShopDescription(form.getShopDescription());
        seller.setGstIn(form.getGstIn());
        seller.setIsVerified(false); // Mặc định chưa xác thực
        seller.setStatus(STATUS_ACTIVE);
        sellerRepository.save(seller);

        // Trả về thông báo thành công sử dụng helper từ ABasicController
        return makeSuccessResponse("Create seller account successful.");
    }

    @ApiOperation(value = "Xem Profile chính mình (Token)")
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SellerDto> getProfile() {
        Seller seller = sellerRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Seller not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        return makeSuccessResponse(sellerMapper.fromEntityToDto(seller), "Get profile successful");
    }

    @ApiOperation(value = "Cập nhật Profile chính mình (Token)")
    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateSellerProfileForm form, BindingResult bindingResult) {
        Seller seller = sellerRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Seller not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        // Kiểm tra pass cũ để bảo mật
        if (!passwordEncoder.matches(form.getOldPassword(), seller.getAccount().getPassword())) {
            throw new BadRequestException("Old password must be correct", ErrorCode.ACCOUNT_ERROR_WRONG_PASSWORD);
        }

        sellerMapper.mappingUpdateProfileToEntity(form, seller);

        if (StringUtils.isNotBlank(form.getPassword())) {
            seller.getAccount().setPassword(passwordEncoder.encode(form.getPassword()));
        }

        sellerRepository.save(seller);
        return makeSuccessResponse("Update profile successful.");
    }

    @ApiOperation(value = "Xóa mềm Seller (Status -2)")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_D')")
    @Transactional
    public ApiMessageDto<Void> delete(@PathVariable Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        seller.getAccount().setStatus(STATUS_DELETE); // Set status -2 cho account
        accountRepository.save(seller.getAccount());

        return makeSuccessResponse("Delete seller successful.");
    }

    @ApiOperation(value = "Lấy danh sách Seller")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_L')")
    public ApiMessageDto<ResponseListDto<List<SellerDto>>>  list(SellerCriteria criteria, Pageable pageable) {
        Page<Seller> page = sellerRepository.findAll(criteria.getSpecification(), pageable);

        // Sử dụng helper makeResponseListDto lồng vào makeSuccessResponse
        return makeSuccessResponse(makeResponseListDto(page, sellerMapper::fromEntityListToDtoList), "Get list seller successful.");
    }
//    Đối tượng Page chứa 3 thông tin chính mà Frontend luôn cần:
//            page.getContent(): Đây là danh sách thực tế các Seller (ví dụ 10 người) của trang hiện tại.
//            page.getTotalElements(): Tổng số lượng Seller có trong Database (ví dụ có 500 người). Frontend dùng số này để biết có bao nhiêu trang.
//            page.getTotalPages(): Tổng số trang dựa trên kích thước trang bạn chọn (Ví dụ: 500 người, mỗi trang 10 người => 50 trang).

    @ApiOperation(value = "Xem chi tiết Seller (Admin)")
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SEL_V')")
    public ApiMessageDto<SellerDto> get(@PathVariable Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        return makeSuccessResponse(sellerMapper.fromEntityToDto(seller), "Get seller successful");
    }
}