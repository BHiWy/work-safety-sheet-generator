package org.generator;

import org.generator.dto.GroupDTO;
import org.generator.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

/**
 * Main Spring Boot application class responsible for initializing and running the application.
 * Implements {@link GroupDTO} to execute specific code after the application context is loaded.
 */
@SpringBootApplication
public class Generator implements CommandLineRunner {
    private final ExcelService excelService;

    /**
     * Autowired constructor to inject dependencies for {@link ExcelService}.
     * @param  excelService - Service for handling Excel file operations.
     */
    @Autowired
    Generator(ExcelService excelService){
        this.excelService = excelService;
    }

    /**
     * Main entry point of the application.
     * @param args - Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Generator.class, args);
    }

    /**
     * Callback method that is executed after the Spring application context is loaded.
     * Here, it calls the {@link ExcelService#readStudentsFromExcel()} method of the {@link ExcelService}.
     * @param  args - Command line arguments passed to the application.
     */
    @Override
    public void run(String... args) {
        excelService.readStudentsFromExcel();
        excelService.readProfessorsFromExcel();
    }
}
