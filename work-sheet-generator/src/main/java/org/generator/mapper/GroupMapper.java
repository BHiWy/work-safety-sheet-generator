package org.generator.mapper;

import org.generator.dto.GroupDTO;
import org.generator.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between {@link Group} entity and {@link GroupDTO} data transfer object.
 * Uses Spring's component model for integration and ignores any unmapped target properties during the mapping process.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    GroupDTO toDTO(Group group);
    Group toEntity(GroupDTO groupDTO);
}
