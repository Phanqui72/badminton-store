package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.product.ProductDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.product.CreateProductForm;
import com.badminton.store.form.product.UpdateProductForm;
import com.badminton.store.mapper.ProductMapper;
import com.badminton.store.model.Product;
import com.badminton.store.model.Seller;
import com.badminton.store.model.criteria.ProductCriteria;
import com.badminton.store.repository.ProductRepository;
import com.badminton.store.repository.SellerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static com.badminton.store.constant.MgrConstant.STATUS_ACTIVE;
import static com.badminton.store.constant.MgrConstant.STATUS_DELETE;

@RestController
@RequestMapping("/v1/product")
@Api(tags = "Product Controller")
public class ProductController extends ABasicController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired private SellerRepository sellerRepository;
    @Autowired private ProductMapper productMapper;

    @ApiOperation(value = "Tạo sản phẩm (Dành cho Seller)")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_C')")
    @Transactional
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductForm form, BindingResult bindingResult) {
        // Lấy Seller ID từ Token của người đang đăng nhập
        Seller seller = sellerRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Seller account not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        Product product = productMapper.fromCreateFormToEntity(form);
        product.setSeller(seller);
        product.setStatus(STATUS_ACTIVE);

        productRepository.save(product);
        return makeSuccessResponse("Create product successful.");
    }

    @ApiOperation(value = "Cập nhật sản phẩm")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_U')")
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductForm form, BindingResult bindingResult) {
        Product product = productRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Product not found", ErrorCode.PRODUCT_ERROR_NOT_FOUND));

        // Bảo mật: Nếu không phải Admin, Seller chỉ được sửa hàng của mình
        if (!isSuperAdmin() && !product.getSeller().getId().equals(getCurrentUser())) {
            throw new BadRequestException("You don't have permission to update this product", ErrorCode.PERMISSION_ERROR_NOT_FOUND);
        }

        productMapper.mappingUpdateFormToEntity(form, product);
        productRepository.save(product);
        return makeSuccessResponse("Update product successful.");
    }

    @ApiOperation(value = "Xóa mềm sản phẩm")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_D')")
    @Transactional
    public ApiMessageDto<Void> delete(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found", ErrorCode.PRODUCT_ERROR_NOT_FOUND));

        if (!isSuperAdmin() && !product.getSeller().getId().equals(getCurrentUser())) {
            throw new BadRequestException("Permission denied", ErrorCode.PERMISSION_ERROR_NOT_FOUND);
        }

        product.setStatus(STATUS_DELETE);
        productRepository.save(product);
        return makeSuccessResponse("Delete product successful.");
    }

    @ApiOperation(value = "Lấy danh sách sản phẩm (Công khai hoặc có lọc)")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ProductDto>>> list(ProductCriteria criteria, Pageable pageable) {
        Page<Product> page = productRepository.findAll(criteria.getSpecification(), pageable);
        return makeSuccessResponse(makeResponseListDto(page, productMapper::fromEntityListToDtoList), "List success.");
    }

    @ApiOperation(value = "Lấy chi tiết sản phẩm")
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductDto> get(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found", ErrorCode.PRODUCT_ERROR_NOT_FOUND));
        return makeSuccessResponse(productMapper.fromEntityToDto(product), "Get success.");
    }
}