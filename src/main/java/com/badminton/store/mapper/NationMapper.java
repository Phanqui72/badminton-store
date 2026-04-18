package com.badminton.store.mapper;


import com.badminton.store.dto.nation.NationDto;
import com.badminton.store.form.nation.CreateNationForm;
import com.badminton.store.form.nation.UpdateNationForm;
import com.badminton.store.model.Nation;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NationMapper {
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parentId", target = "parent.id")
    Nation fromCreateNationFormToEntity(CreateNationForm form);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNationFromForm(UpdateNationForm form, @MappingTarget Nation entity);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent.id", target = "parentId")
    NationDto fromEntityToDto(Nation nation);

    // MapStruct sẽ tự động gọi hàm fromEntityToDto cho từng phần tử trong list
    List<NationDto> fromEntityListToDtoList(List<Nation> nations);
}