package org.generator.mapper;

import org.generator.dto.ProfessorDTO;
import org.generator.entities.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessorMapper {
    ProfessorDTO toDTO(Professor professor);
    Professor toEntity(ProfessorDTO professorDTO);
}
