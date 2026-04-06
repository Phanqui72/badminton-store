package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.address.CreateAddressForm;
import com.badminton.store.form.nation.CreateNationForm;
import com.badminton.store.mapper.AddressMapper;
import com.badminton.store.model.Address;
import com.badminton.store.model.Nation;
import com.badminton.store.model.User;
import com.badminton.store.repository.AddressRepository;
import com.badminton.store.repository.NationRepository;
import com.badminton.store.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/nation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
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
}
