package com.badminton.store.mapper;

import com.badminton.store.dto.product.ProductDto;
import com.badminton.store.form.product.CreateProductForm;
import com.badminton.store.form.product.UpdateProductForm;
import com.badminton.store.model.Product;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "seller.shopName", target = "shopName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "images", target = "images")
    @Mapping(source = "status", target = "status")
    ProductDto fromEntityToDto(Product product);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "images", target = "images")
    Product fromCreateFormToEntity(CreateProductForm form);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    void mappingUpdateFormToEntity(UpdateProductForm form, @MappingTarget Product product);

    List<ProductDto> fromEntityListToDtoList(List<Product> list);
}