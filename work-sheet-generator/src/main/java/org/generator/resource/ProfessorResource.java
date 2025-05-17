package org.generator.resource;

import org.generator.dto.ProfessorDTO;
import org.generator.services.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/professor")
public class ProfessorResource {
    private final ProfessorService professorService;

    /**
     * Constructor for {@code ProfessorResource} which injects the {@link ProfessorService}.
     *
     * @param professorService the service used to handle group-related operations.
     */
    public ProfessorResource(ProfessorService professorService) {
        this.professorService = professorService;
    }

    /**
     * Retrieves all distinct professors by a rank filter.
     * @param rank The rank of the professor.
     * @return A {@link ResponseEntity} containing a list of {@link ProfessorDTO}s and an HTTP OK status.
     */
    @GetMapping("/{rank}")
    public ResponseEntity<List<ProfessorDTO>> getAllByRank(@PathVariable String rank) {
        List<ProfessorDTO> professors = this.professorService.findAllByRank(rank);
        return new ResponseEntity<>(professors, HttpStatus.OK);
    }

    /**
     * Retrieves the list of courses taught by a specific professor.
     * @param professorId The unique identifier of the professor.
     * @return A {@link ResponseEntity} containing a list of {@link String} representing the course names and an HTTP OK status.
     */
    @GetMapping("/get-courses/{professorId}")
    public ResponseEntity<List<String>> getCoursesByProfessor(@PathVariable long professorId) {
        return new ResponseEntity<>(this.professorService.findCoursesByProfessor(professorId), HttpStatus.OK);
    }
}
