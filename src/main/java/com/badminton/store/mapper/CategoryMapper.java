package com.badminton.store.mapper;

import com.badminton.store.dto.category.CategoryDto;
import com.badminton.store.form.category.CreateCategoryForm;
import com.badminton.store.form.category.UpdateCategoryForm;
import com.badminton.store.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Category fromCreateFormToEntity(CreateCategoryForm form);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    void mappingUpdateFormToEntity(UpdateCategoryForm form, @MappingTarget Category category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDto")
    CategoryDto fromEntityToDto(Category category);

    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromEntityToDto")
    List<CategoryDto> fromEntityListToDtoList(List<Category> list);
}