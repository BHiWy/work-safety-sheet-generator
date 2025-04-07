package org.generator.mapper;

import org.generator.dto.GroupDTO;
import org.generator.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    GroupDTO toDTO(Group group);
    Group toEntity(GroupDTO groupDTO);
}
