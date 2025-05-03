package org.generator.mapper;

import org.generator.dto.ProfessorDTO;
import org.generator.entities.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between {@link Professor} entity and {@link ProfessorDTO} data transfer object.
 * Uses Spring's component model for integration and ignores any unmapped target properties during the mapping process.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessorMapper {
    ProfessorDTO toDTO(Professor professor);
    Professor toEntity(ProfessorDTO professorDTO);
}
