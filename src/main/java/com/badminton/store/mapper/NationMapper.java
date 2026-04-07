package com.badminton.store.mapper;

import com.badminton.store.dto.nation.NationDto;
import com.badminton.store.form.nation.CreateNationForm;
import com.badminton.store.model.Nation;
import io.swagger.annotations.ApiModelProperty;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NationMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent", target = "parent")
    Nation formNationDtoToEntity(NationDto nationDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent", target = "parent")
    NationDto fromNationEntityToDto(Nation nation);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent", target = "parent")
    @Named("fromEntityToNationDto")
    NationDto fromEntityToNationDto(Nation nation);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parentId", target = "parent.id")
    Nation fromUpdateFormToEntity(CreateNationForm form);
}
