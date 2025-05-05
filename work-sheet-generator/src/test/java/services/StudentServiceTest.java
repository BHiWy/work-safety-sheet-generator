package services;

import lombok.extern.slf4j.Slf4j;
import org.generator.services.ExcelService;
import org.generator.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StudentServiceTest {
    private final StudentService studentService;
    public StudentServiceTest(StudentService studentService) {
        this.studentService = studentService;
    }
    private static final String FILE_PATH = "files/grupe-an-III-AIA-2024_2025.xls";

    @Test void testExtractNameDetails(){

    }
}
