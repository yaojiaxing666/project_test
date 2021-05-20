package com.test.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@ResponseBody
@RequestMapping("/excel")
public class ExcelController {

    public String readExcel(MultipartFile file){
        try {
            // 获得文件名:
            String fileName = file.getOriginalFilename();
            // 获得文件的扩展名:
            String extName = fileName.substring( fileName.lastIndexOf(".")+1 );
            //事项名称，对应Excel里的数据
            List<String> list = new ArrayList<>();
            Sheet sheet=null;
            if ("xls".equals(extName)){//excel 2003 版
                HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
                //sheet = workbook.getSheet("water_surface");
                sheet = workbook.getSheetAt(0);
            } else if ("xlsx".equals(extName)){//excel 2007 版
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                //sheet = workbook.getSheet("water_surface");
                sheet = workbook.getSheetAt(0);
            } else {
                return "文件类型不匹配！";
            }
            for (Row row : sheet) {
                //row.getRowNum() == 0 表示是第一行（标题行），跳过
                if (row.getRowNum() == 0){
                    continue;
                }
                String itemName = row.getCell(3) == null ? null : row.getCell(3).getStringCellValue();
                list.add(itemName);
            }
            return list.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
