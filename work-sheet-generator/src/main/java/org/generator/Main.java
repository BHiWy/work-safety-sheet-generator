package org.generator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello, World!");
        ExcelService excelService = new ExcelService();
        excelService.citesteDinExcel();
    }
}

public class ExcelService{

    public void citesteDinExcel()throws FileNotFoundException  {
        String path="C:\\Users\\Alexandra\\Desktop\\proiect\\work-safety-sheet-generator\\work-sheet-generator\\src\\main\\resources\\grupe-an-III-AIA-2024_2025.xls";
        FileInputStream fileInputStream= new FileInputStream(path);
        Workbook Workbook= new XSSFWorkbook(fileInputStream) {
            Sheet sheet = Workbook.getSheetAt(0);
            List<Grupa> grupe = new ArrayList<>();
            Row row;
             for(row : sheet)

            {
                row = sheet.getRow(0);
                if (row.getRowNum() == 0) {
                    Cell cellGrupa = row.getCell(0);
                    Cell cellStudent = row.getCell(1);

                }
            }
        };
    }
}