package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.category.CategoryDto;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.category.CreateCategoryForm;
import com.badminton.store.form.category.UpdateCategoryForm;
import com.badminton.store.mapper.CategoryMapper;
import com.badminton.store.model.Category;
import com.badminton.store.model.criteria.CategoryCriteria;
import com.badminton.store.repository.CategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.badminton.store.constant.MgrConstant.STATUS_ACTIVE;
import static com.badminton.store.constant.MgrConstant.STATUS_DELETE;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@Api(tags = "Category Controller")
public class CategoryController extends ABasicController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @ApiOperation(value = "Tạo mới Category")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_C')")
    @Transactional
    public ApiMessageDto<Void> create(@Valid @RequestBody CreateCategoryForm form, BindingResult bindingResult) {
        Category category = categoryMapper.fromCreateFormToEntity(form);
        category.setStatus(STATUS_ACTIVE);

        categoryRepository.save(category);
        return makeSuccessResponse("Create category success.");
    }

    @ApiOperation(value = "Cập nhật Category")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_U')")
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCategoryForm form, BindingResult bindingResult) {
        Category category = categoryRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        categoryMapper.mappingUpdateFormToEntity(form, category);

        categoryRepository.save(category);
        return makeSuccessResponse("Update category success.");
    }

    @ApiOperation(value = "Lấy chi tiết Category")
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_V')")
    public ApiMessageDto<CategoryDto> get(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        return makeSuccessResponse(categoryMapper.fromEntityToDto(category), "Get category success.");
    }

    @ApiOperation(value = "Danh sách Category có bộ lọc và phân trang")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_L')")
    public ApiMessageDto<ResponseListDto<List<CategoryDto>>> list(CategoryCriteria criteria, Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(criteria.getSpecification(), pageable);

        // Sử dụng helper cực mạnh từ ABasicController để map sang DTO list
        return makeSuccessResponse(makeResponseListDto(page, categoryMapper::fromEntityListToDtoList), "Get list success.");
    }

    @ApiOperation(value = "Xóa mềm Category (Status -2)")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_D')")
    @Transactional
    public ApiMessageDto<Void> delete(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        // Thực hiện xóa mềm giống Seller
        category.setStatus(STATUS_DELETE);
        categoryRepository.save(category);

        return makeSuccessResponse("Delete category success.");
    }
}