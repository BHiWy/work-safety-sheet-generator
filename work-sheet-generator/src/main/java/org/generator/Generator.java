package org.generator;

import org.generator.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class Generator implements CommandLineRunner {
    private final ExcelService excelService;

    @Autowired
    Generator(ExcelService excelService){
        this.excelService = excelService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Generator.class, args);
    }

    @Override
    public void run(String... args) {
        excelService.readExcel();
    }
}
