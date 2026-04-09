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
    @PreAuthorize("hasRole(N_C)")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNationForm createNationForm, BindingResult bindingResult) {
        Nation nation = nationMapper.fromCreateNationFormToEntity(createNationForm);
        if (nationRepository.findByName(createNationForm.getName()).isPresent()) {
            throw new BadRequestException("Nation name already exists", ErrorCode.NATION_ERROR_EXIST);
        }
        if (createNationForm.getParentId() != null) {
            Nation parent = nationRepository.findById(createNationForm.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));
            nation.setParent(parent);
        }
        nationRepository.save(nation);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create Nation success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('N-U', 'ADMIN')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateNationForm updateForm, BindingResult bindingResult) {

        Nation nation = nationRepository.findById(updateForm.getId())
                .orElseThrow(() -> new NotFoundException("Nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));
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
        NationDto nationDto = nationMapper.fromEntityToDto(nation);
        ApiMessageDto<NationDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(nationDto);
        apiMessageDto.setMessage("Get nation success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('N-D', 'ADMIN')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nation not found", ErrorCode.NATION_ERROR_NOT_FOUND));

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
        Page<Nation> page = nationRepository.findAll(criteria.getSpecification(), pageable);
        List<NationDto> listDto = nationMapper.fromEntityListToDtoList(page.getContent());
        ResponseListDto<List<NationDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(listDto);
        responseListDto.setTotalElements(page.getTotalElements());
        responseListDto.setTotalPages(page.getTotalPages());
        ApiMessageDto<ResponseListDto<List<NationDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get nation list success");

        return apiMessageDto;
    }
}