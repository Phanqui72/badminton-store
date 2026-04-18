package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.nation.NationDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.nation.CreateNationForm;
import com.badminton.store.form.nation.UpdateNationForm;
import com.badminton.store.mapper.NationMapper;
import com.badminton.store.model.Nation;
import com.badminton.store.model.criteria.NationCriteria;
import com.badminton.store.repository.NationRepository;
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
@RequestMapping("/v1/nation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NationController extends ABasicController {

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private NationMapper nationMapper;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('N-C', 'ADMIN')") // Nên cho phép cả ADMIN
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNationForm createNationForm, BindingResult bindingResult) {

        // 1. Kiểm tra lỗi validate (Nên đẩy ra GlobalExceptionHandler nếu có thể)
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + " " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException(errorMessage, ErrorCode.NATION_ERROR_INVALID);
        }

        // 2. Map từ Form sang Entity
        Nation nation = nationMapper.fromCreateNationFormToEntity(createNationForm);

        // 3. Kiểm tra trùng tên (Nên có để tránh DataIntegrityViolationException)
        if (nationRepository.findByName(createNationForm.getName()).isPresent()) {
            throw new BadRequestException("Nation name already exists", ErrorCode.NATION_ERROR_EXIST);
        }

        // 4. Xử lý Parent-Child
        if (createNationForm.getParentId() != null) {
            Nation parent = nationRepository.findById(createNationForm.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));

            // Ràng buộc logic: Tỉnh không thể có parent là Xã (ví dụ vậy)
            nation.setParent(parent);
        }

        // 5. Lưu vào DB
        nationRepository.save(nation);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create Nation success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('N-U', 'ADMIN')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateNationForm updateForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid data", ErrorCode.NATION_ERROR_INVALID);
        }

        Nation nation = nationRepository.findById(updateForm.getId())
                .orElseThrow(() -> new NotFoundException("Nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));

        // Update các field
        nationMapper.updateNationFromForm(updateForm, nation);

        if (updateForm.getParentId() != null) {
            Nation parent = nationRepository.findById(updateForm.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent not found", ErrorCode.NATION_ERROR_NOT_FOUND));
            nation.setParent(parent);
        }

        nationRepository.save(nation);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update Nation success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('N-G', 'ADMIN')")
    public ApiMessageDto<NationDto> get(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));
        // Bước 1: Chuyển đổi Entity từ Database sang DTO để hiển thị (tránh lộ thông tin nhạy cảm)
        NationDto nationDto = nationMapper.fromEntityToDto(nation);

        // Bước 2: Khởi tạo ApiMessageDto và truyền dữ liệu đã convert vào
        ApiMessageDto<NationDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(nationDto);
        apiMessageDto.setMessage("Get nation success");

        // Bước 3: Trả về đối tượng bọc cuối cùng
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('N-D', 'ADMIN')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));

        // Kiểm tra xem có Nation con hoặc Address nào đang tham chiếu tới không trước khi xóa
        try {
            nationRepository.delete(nation);
        } catch (Exception e) {
            throw new BadRequestException("Cannot delete nation being used", ErrorCode.NATION_ERROR_CANNOT_DELETE);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete Nation success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<NationDto>>> list(NationCriteria criteria, Pageable pageable) {

        // Bước 1: Thực hiện truy vấn có phân trang và lọc theo tiêu chí (Criteria)
        Page<Nation> page = nationRepository.findAll(criteria.getSpecification(), pageable);

        // Bước 2: Chuyển đổi danh sách Entity sang danh sách DTO
        List<NationDto> listDto = nationMapper.fromEntityListToDtoList(page.getContent());

        // Bước 3: Đóng gói vào ResponseListDto (chứa data + thông tin phân trang)
        ResponseListDto<List<NationDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(listDto);
        responseListDto.setTotalElements(page.getTotalElements());
        responseListDto.setTotalPages(page.getTotalPages());

        // Bước 4: Trả về kết quả cuối cùng qua ApiMessageDto
        ApiMessageDto<ResponseListDto<List<NationDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get nation list success");

        return apiMessageDto;
    }
}