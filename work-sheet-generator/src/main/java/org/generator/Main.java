

package org.generator;


import org.generator.entities.Student;
import org.generator.service.ExcelService;

import java.util.List;

//import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello, World!");
        ExcelService excelService = new ExcelService();
        excelService.readExcel();

    }
}


