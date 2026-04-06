package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.nation.CreateNationForm;
import com.badminton.store.mapper.NationMapper;
import com.badminton.store.model.Nation;
import com.badminton.store.repository.AddressRepository;
import com.badminton.store.repository.NationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Na;
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
public class NationController extends ABasicController{
    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private NationMapper nationMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('N-C')")
    public ApiMessageDto<String> create (@Valid @RequestBody CreateNationForm createNationForm, BindingResult bindingResult){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if (bindingResult.hasErrors()) {
            // Duyệt qua tất cả các lỗi và nối thành một chuỗi
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            throw new BadRequestException(errorMessage, ErrorCode.NATION_ERROR_INVALID);
        }
        // Bước 1: Dùng Mapper để khởi tạo đối tượng
        Nation nation = nationMapper.fromCreateNationFormToEntity(createNationForm);

        // Bước 2: Xử lý logic Parent
        if (createNationForm.getParentId() != null) {
            // Tìm parent thật từ DB
            Nation parentNation = nationRepository.findById(createNationForm.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));

            // Gán object parent đầy đủ vào nation (ghi đè lên cái parent chỉ có ID mà Mapper đã tạo)
            nation.setParent(parentNation);
        } else {
            // Đảm bảo nếu không có parentId thì field parent phải là null
            nation.setParent(null);
        }

        // Bước 3: Lưu
        nationRepository.save(nation);
        apiMessageDto.setMessage("Create Nation success");
        return apiMessageDto;
    }


}
