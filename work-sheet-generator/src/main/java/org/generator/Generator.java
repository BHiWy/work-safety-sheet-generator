package org.generator;

import org.generator.dto.GroupDTO;
import org.generator.dto.ProfessorDTO;
import org.generator.dto.StudentDTO;
import org.generator.mapper.ProfessorMapper;
import org.generator.mapper.StudentMapperImpl;
import org.generator.services.GroupService;
import org.generator.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.generator.services.StudentService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Generator implements CommandLineRunner {
    private final StudentService studentService;
    private final GroupService groupService;
    private final ProfessorService professorService;
    private final StudentMapperImpl studentMapperImpl;
    private final ProfessorMapper professorMapper;

    @Autowired
    Generator(
            StudentService studService,
            GroupService groupService,
            ProfessorService profService,
            StudentMapperImpl studentMapperImpl, ProfessorMapper professorMapper){
        this.studentService = studService;
        this.groupService = groupService;
        this.professorService = profService;
        this.studentMapperImpl = studentMapperImpl;
        this.professorMapper = professorMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(Generator.class, args);
    }

    @Override
    public void run(String... args) {
        newStudent();
    }

    public void newStudent(){
        System.out.println("incep creare studenti");

        StudentDTO student1 = new StudentDTO();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@gmail.com");
        student1.setPaternalInitial("A");

        this.studentService.save(studentMapperImpl.toEntity(student1));

        StudentDTO student2 = new StudentDTO();
        student2.setFirstName("Alma");
        student2.setLastName("Ora");
        student2.setEmail("alma@gmail.com");
        student2.setPaternalInitial("F");
        this.studentService.save(studentMapperImpl.toEntity(student2));

        ArrayList<StudentDTO> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        System.out.println("incep creare grupa");

        GroupDTO group = new GroupDTO();
        group.setCode("1302A");
        group.setStudents(students);
        group.setYear(2);
        group.setGroupLeader(student1);

        System.out.println("salvare grupa");
        this.groupService.save(group);

        System.out.println("salvat");

        ProfessorDTO professor = new ProfessorDTO();
        professor.setFullName("Aleodor Ioan");
        professor.setRank("Lector Universitar");
        professor.setEmail("aleodorio@gmail.com");
        List<String> courses = new ArrayList<>();
        courses.add("Limbaj assembly");
        courses.add("C++");
        courses.add("MAP");
        professor.setCourses(courses);

        this.professorService.save(professorMapper.toEntity(professor));
    }
}
