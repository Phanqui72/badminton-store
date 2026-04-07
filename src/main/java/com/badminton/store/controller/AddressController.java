package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.address.AddressDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.address.CreateAddressForm;
import com.badminton.store.form.address.UpdateAddressForm;
import com.badminton.store.mapper.AddressMapper;
import com.badminton.store.model.Address;
import com.badminton.store.model.Nation;
import com.badminton.store.model.User;
import com.badminton.store.model.criteria.AddressCriteria;
import com.badminton.store.repository.AddressRepository;
import com.badminton.store.repository.NationRepository;
import com.badminton.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;




@RestController
@RequestMapping("/v1/address")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController extends ABasicController{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NationRepository nationRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-C')")
    public ApiMessageDto<String> create (@Valid @RequestBody CreateAddressForm form, BindingResult bindingResult){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if (bindingResult.hasErrors()) {
            // Duyệt qua tất cả các lỗi và nối thành một chuỗi
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            throw new BadRequestException(errorMessage, ErrorCode.NATION_ADDRESS_INVALID);
        }
        // 2. Check User tồn tại
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_ERROR_NOT_FOUND));
        Nation province = nationRepository.findById(form.getProvinceId())
                .orElseThrow(() -> new NotFoundException("Province not found", ErrorCode.NATION_ERROR_NOT_FOUND));
        Nation district = nationRepository.findById(form.getDistrictId())
                .orElseThrow(() -> new NotFoundException("District not found", ErrorCode.NATION_ERROR_NOT_FOUND));
        Nation commune = nationRepository.findById(form.getCommuneId())
                .orElseThrow(() -> new NotFoundException("Commune not found", ErrorCode.NATION_ERROR_NOT_FOUND));

        // 4. Xử lý logic isDefault (Quan trọng)
        if (form.getIsDefault()) {
            // Tìm và bỏ mặc định của tất cả địa chỉ cũ của User này
            addressRepository.unsetDefaultByUserId(user.getId());
        }
        Address address = addressMapper.fromCreateFormToEntity(form);
        address.setUser(user);
        address.setProvince(province);
        address.setDistrict(district);
        address.setCommune(commune);

        addressRepository.save(address);
        apiMessageDto.setMessage("Create Nation success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateAddressForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException(errorMessage, ErrorCode.NATION_ADDRESS_INVALID);
        }

        // 1. Tìm address cũ
        Address address = addressRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));

        // 2. Xử lý logic isDefault (Nếu set cái này là default thì gỡ các cái khác của User đó)
        if (form.getIsDefault() != null && form.getIsDefault()) {
            addressRepository.unsetDefaultByUserId(address.getUser().getId());
        }

        // 3. Map các field cơ bản (street, name, isDefault...)
        addressMapper.updateAddressFromForm(form, address);

        // 4. Update các liên kết Nation nếu có truyền ID mới
        if (form.getProvinceId() != null) {
            address.setProvince(nationRepository.findById(form.getProvinceId()).orElse(null));
        }
        if (form.getDistrictId() != null) {
            address.setDistrict(nationRepository.findById(form.getDistrictId()).orElse(null));
        }
        if (form.getCommuneId() != null) {
            address.setCommune(nationRepository.findById(form.getCommuneId()).orElse(null));
        }

        addressRepository.save(address);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update Address success");
        return apiMessageDto;
    }

    // --- GET DETAIL ---
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-G')")
    public ApiMessageDto<AddressDto> get(@PathVariable("id") Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));

        AddressDto addressDto = addressMapper.fromEntityToDto(address);

        ApiMessageDto<AddressDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(addressDto);
        apiMessageDto.setMessage("Get address success");
        return apiMessageDto;
    }

    // --- DELETE ---
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));

        addressRepository.delete(address);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete Address success");
        return apiMessageDto;
    }

    // --- LIST (PHÂN TRANG & FILTER) ---
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-L')")
    public ApiMessageDto<ResponseListDto<List<AddressDto>>> list(AddressCriteria criteria, Pageable pageable) {
        // 1. Query từ DB
        Page<Address> page = addressRepository.findAll(criteria.getSpecification(), pageable);

        // 2. Convert sang DTO List
        List<AddressDto> listDto = addressMapper.fromEntityListToDtoList(page.getContent());

        // 3. Đóng gói kết quả
        ResponseListDto<List<AddressDto>> response = new ResponseListDto<>();
        response.setContent(listDto);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        ApiMessageDto<ResponseListDto<List<AddressDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(response);
        apiMessageDto.setMessage("Get address list success");
        return apiMessageDto;
    }

}
