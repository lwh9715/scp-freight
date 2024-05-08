package com.scp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadNinboCostExcle {
	private List<String[]> list = new ArrayList<String[]>();
	
    public List<String[]> readMainfest(File file) {
    	List<String[]> list = new ArrayList<String[]>();
    	String[] str = null;
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        String cellData = null;
        wb = readExcel(file.toString());
        if(wb != null){
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            //int rownum = sheet.getPhysicalNumberOfRows();
            for (int r = 4; r <= sheet.getLastRowNum(); r++) {
				////System.out.println("开始读取第"+r+"行:");
				row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1){
					continue;
				}
				int count = 0;
				str = new String[row.getLastCellNum()-row.getFirstCellNum()-1];
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					////System.out.print("第"+r+"行第"+c+"个:");
					Cell cell = row.getCell(c);
					
					//合并行只统计一次
					if(c == 0){
						continue;
					}
					//判断防止科学计数
					if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC && c != 2){
					   long longVal = Math.round(cell.getNumericCellValue());
					   Double doubleVal = cell.getNumericCellValue();
					   if (Double.parseDouble(longVal + ".0") == doubleVal){
					      cell.setCellType(Cell.CELL_TYPE_STRING);
					   }
					}
					if(c == 2){
						str[count] = String.valueOf(cell.getDateCellValue());
					}else{
						str[count] = cell.getStringCellValue();
					}
					count++;
				}
				list.add(str);
			}
        }
        return list;
    }
    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC:{
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            }
            case Cell.CELL_TYPE_FORMULA:{
                //判断cell是否为日期格式
                if(DateUtil.isCellDateFormatted(cell)){
                    //转换为日期格式YYYY-mm-dd
                    cellValue = cell.getDateCellValue();
                }else{
                    //数字
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            case Cell.CELL_TYPE_STRING:{
                cellValue = cell.getRichStringCellValue().getString();
                break;
            }
            default:
                cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
	
//	/**
//	 * 验证excel是否全部为空
//	 * @param row 当前行
//	 * @param firstRow 第一行标题行
//	 * @return
//	 */
//	public static boolean isAllRowEmpty(Row row) {
//		int count = 0;
//		//单元格数量
//		int rowCount = row.getLastCellNum() - row.getFirstCellNum();
//		//判断多少个单元格为空
//		for (int c = 0; c < rowCount; c++) {
//			Cell cell = row.getCell(c);
//			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK || "".equals((cell+"").trim())){
//				count += 1;
//			}
//		}
// 
//		if (count == rowCount) {
//			return true;
//		}
// 
//		return  false;
//	}
}