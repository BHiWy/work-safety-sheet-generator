package org.generator.mapper;

import org.generator.dto.StudentDTO;
import org.generator.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between {@link Student} entity and {@link StudentDTO} data transfer object.
 * Uses Spring's component model for integration and ignores any unmapped target properties during the mapping process.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    StudentDTO toDTO(Student student);
    Student toEntity(StudentDTO studentDTO);
}
