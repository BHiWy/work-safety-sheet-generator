package org.generator.resource;


import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@link org.generator.entities.Group} resources.
 * Provides endpoints for retrieving group and student data.
 */
@RestController
@RequestMapping("/group")
public class GroupResource {
    private final GroupService groupService;


    /**
     * Constructor for {@code GroupResource} which injects the {@link GroupService}.
     *
     * @param groupService the service used to handle group-related operations.
     */
    public GroupResource(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Retrieves all groups.
     * @return A {@link ResponseEntity} containing a list of {@link GroupDTO}s and an HTTP OK status.
     */
    @GetMapping("")
    public ResponseEntity<List<GroupDTO>> getAll() {
        List<GroupDTO> students = this.groupService.getAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Retrieves all students belonging to a specific group code.
     * @param groupCode The code of the group to find students for.
     * @return A {@link ResponseEntity} containing a list of {@link GroupDTO}s and an HTTP OK status.
     */
    @GetMapping("/find-students/{groupCode}")
    public ResponseEntity<List<StudentDTO>> getAllStudentsByGroupCode(@PathVariable("groupCode") String groupCode) {
        List<StudentDTO> students = this.groupService.findStudentsByGroupCode(groupCode);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
