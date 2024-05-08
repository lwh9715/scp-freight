package com.scp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadManifestExcle {
	private static Map<String, List<String[]>> mapInfo = new HashMap<String, List<String[]>>();
	
    public Map<String, List<String[]>> readManifest(File file) {
    	mapInfo = new HashMap<String, List<String[]>>();
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
            
            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				////System.out.println("开始读取第"+r+"行:");
				row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1){
					continue;
				}
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					////System.out.print("第"+r+"行第"+c+"个:");
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					cellData = (String) getCellFormatValue(row.getCell(c));
					if(cellData.indexOf("分票统计数据 - (系统自动合成)") >= 0){
						differentiates_vote(sheet, r);
					//}else if("按箱统计数据 - (系统自动合成)".equals(cellData)){//明细已经包含按箱数据
						//map = differentiates_box(sheet, r, map);
					}else if("总票统计数据 - (系统自动合成)".equals(cellData)){
						vote_total(sheet, r);
					}else if("明细品名及数据".equals(cellData)){
						itemized_info(sheet, r);
					}else if("VGM数据".equals(cellData)){
						info_VGM(sheet, r);
					}else if(cellData.contains("发货人")){
						List<String[]> list =  new ArrayList<String[]>();
						String[] str = new String[6];
						str[0] = String.valueOf(getCellFormatValue(sheet.getRow(r).getCell(c+2))).replace("\\u", "A");
						str[1] = String.valueOf(getCellFormatValue(sheet.getRow(r+1).getCell(c+2))).replace("\\u", "A");
						str[2] = String.valueOf(getCellFormatValue(sheet.getRow(r+2).getCell(c+2))).replace("\\u", "A");
						str[3] = String.valueOf(getCellFormatValue(sheet.getRow(r+3).getCell(c+2))).replace("\\u", "A");
						str[4] = String.valueOf(getCellFormatValue(sheet.getRow(r+4).getCell(c+2))).replace("\\u", "A");
						str[5] = String.valueOf(getCellFormatValue(sheet.getRow(r+5).getCell(c+2))).replace("\\u", "A");
						list.add(convertUnicodeToCh(str));
						mapInfo.put("shipper", list);
					}else if(cellData.contains("收货人")){
						List<String[]> list =  new ArrayList<String[]>();
						String[] str = new String[8];
						str[0] = (String) getCellFormatValue(sheet.getRow(r).getCell(c+2));
						str[1] = (String) getCellFormatValue(sheet.getRow(r+1).getCell(c+2));
						str[2] = (String) getCellFormatValue(sheet.getRow(r+2).getCell(c+2));
						str[3] = (String) getCellFormatValue(sheet.getRow(r+3).getCell(c+2));
						str[4] = (String) getCellFormatValue(sheet.getRow(r+4).getCell(c+2));
						str[5] = (String) getCellFormatValue(sheet.getRow(r+5).getCell(c+2));
						str[6] = (String) getCellFormatValue(sheet.getRow(r+6).getCell(c+2));
						str[7] = (String) getCellFormatValue(sheet.getRow(r+7).getCell(c+2));
						list.add(convertUnicodeToCh(str));
						mapInfo.put("consignee", list);
					}else if(cellData.contains("通知人")){
						List<String[]> list =  new ArrayList<String[]>();
						String[] str = new String[6];
						str[0] = (String) getCellFormatValue(sheet.getRow(r).getCell(c+2));
						str[1] = (String) getCellFormatValue(sheet.getRow(r+1).getCell(c+2));
						str[2] = (String) getCellFormatValue(sheet.getRow(r+2).getCell(c+2));
						str[3] = (String) getCellFormatValue(sheet.getRow(r+3).getCell(c+2));
						str[4] = (String) getCellFormatValue(sheet.getRow(r+4).getCell(c+2));
						str[5] = (String) getCellFormatValue(sheet.getRow(r+5).getCell(c+2));
						list.add(convertUnicodeToCh(str));
						mapInfo.put("notifier", list);
					}
				}
			}
            List<String[]> li = new ArrayList<String[]>();
            String[] total_list_mblno = new String[1];
            String cell = (String) getCellFormatValue(sheet.getRow(4).getCell(1));//总单提单号
            if(cell.isEmpty()){
            	cell = (String) getCellFormatValue(sheet.getRow(3).getCell(1));//总单提单号
            }
            total_list_mblno[0] = cell;
            li.add(total_list_mblno);
            mapInfo.put("total_list_mblno", li);
        }
        return mapInfo;
    }
    
    
    /**
     * 将unicode字符串转为正常字符串
     *
     * @param str unicode字符串（目前针对"\u00a0,\uff08,\uff09"）
     * @return 转换后的字符串
     */
    private static String[] convertUnicodeToCh(String[] str) {
    	for (int i = 0; i < str.length; i++) {
    		if(null == str[i]){
    			continue;
    		}
    		str[i] = str[i].replace("\u00a0", " ").replace("\uff08", "(").replace("\uff09", ")");
		}
        
        return str;
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
            
          //当excel是2003时
          //is是文件的InputStream
          boolean isExcel2003 = true;
          if(!is.markSupported()) {
              is = new PushbackInputStream(is, 8);
          }
          if (POIFSFileSystem.hasPOIFSHeader(is)) {
        	  return wb = new HSSFWorkbook(is);
          } else if(POIXMLDocument.hasOOXMLHeader(is)){
              //当excel是2007时
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
    
	/**
	 * 分票统计数据
	 * row excle行数
	 * cellStyle excle样式
	 * map 数据
	 */
	public void differentiates_vote(Sheet sheet, int r){
		List<String[]> list = new ArrayList<String[]>();
		String[] str = new String[7];
		Row row = null;
		int count = 0;//记录当前行（合并行算一行）
		String cellData = null;
		//从 “分票统计数据 - (系统自动合成) ”行的下下行
		for (int i = r+2; i <= sheet.getLastRowNum(); i++) {
			////System.out.println("开始读取第"+r+"行:");
			row = sheet.getRow(i);
			if (row == null || row.getLastCellNum() < 1){
				break;
			}
			if(isAllRowEmpty(row)){//当前行全为空时停止
				break;
			}
			count = 0;
			str = new String[7];
			for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				if(c == 1 || c == 3 || c == 4 || c == 5 || c == 7 || c == 8){
					continue;
				}
				cell.setCellType(Cell.CELL_TYPE_STRING);
				str[count] = cell.getStringCellValue();
				count++;
			}
			list.add(convertUnicodeToCh(str));
		}
		mapInfo.put("mblno",list);
	}
	
	/**
	 * 总票统计数据
	 * row excle行数
	 * cellStyle excle样式
	 * map 数据
	 */
	public  void vote_total(Sheet sheet, int r){
		List<String[]> list = new ArrayList<String[]>();
		String[] str = new String[8];
		boolean flag = false;
		int count = 0;//记录当前行（合并行算一行）
		Row row = null;
		//从 “分票统计数据 - (系统自动合成) ”行的下下行
		for (int i = r+2; i <= sheet.getLastRowNum(); i++) {
			////System.out.println("开始读取第"+r+"行:");
			row = sheet.getRow(i);
			if (row == null || row.getLastCellNum() < 1){
				break;
			}
			if(isAllRowEmpty(row)){//当前行全为空时停止
				break;
			}
			count = 0;
			str = new String[8];
			for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				if(c == 2 || c == 4 || c == 5 || c == 7 || c == 8){
					continue;
				}
				
				cell.setCellType(Cell.CELL_TYPE_STRING);
				str[count] = cell.getStringCellValue();
				count++;
			}
			list.add(convertUnicodeToCh(str));
		}
		mapInfo.put("total", list);
	}
	
	/**
	 * 明细品名及数据
	 * row excle行数
	 * cellStyle excle样式
	 */
	public void itemized_info(Sheet sheet, int r){
		List<String[]> list = new ArrayList<String[]>();
		String[] str = null;
		Map<String, String> map = new HashMap<String, String>();
		boolean flag = false;
		int count = 0;//记录当前行（合并行算一行）
		Row row = null;
		String cellData = null;
		//从 “分票统计数据 - (系统自动合成) ”行的下下行
		for (int i = r+2; i <= sheet.getLastRowNum(); i++) {
			////System.out.println("开始读取第"+r+"行:");
			row = sheet.getRow(i);
			if (row == null || row.getLastCellNum() < 1){
				break;
			}
			if(isAllRowEmpty(row)){//当前行全为空时停止
				break;
			}
			count = 0;
			str = new String[20];
			for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				str[count] = cell.getStringCellValue();
				count++;
			}
			list.add(convertUnicodeToCh(str));
		}
		mapInfo.put("itemized", list);
	}
	
	/**
	 * VGM数据
	 * row excle行数
	 * cellStyle excle样式
	 * map 数据
	 */
	public  void info_VGM(Sheet sheet, int r){
		List<String[]> list =  mapInfo.get("itemized");
		List<String[]> list2 = new ArrayList<String[]>();
		if(null != list && list.size() > 0){
			boolean flag = false;
			int count = 13;//因为明细品与VGM拼成一行，起标从13开始
			Row row = null;
			String cellData = null;
			String cntno = null;//箱号
			for (int j = 0; j < list.size(); j++) {
				String[] str = list.get(j);
				//从 “分票统计数据 - (系统自动合成) ”行的下下行
				for (int i = r+2; i <= sheet.getLastRowNum(); i++) {//i 行循环
					////System.out.println("开始读取第"+r+"行:");
					row = sheet.getRow(i);
					if (row == null || row.getLastCellNum() < 1){
						break;
					}
					if(isAllRowEmpty(row)){//当前行全为空时停止
						break;
					}
					cntno = row.getCell(0).getStringCellValue();//获取VGM第一行箱号
					if(null == cntno || "".equals(cntno)){//没有箱号数据不正常continue
						continue;
					}
					count = 13;
					if(cntno.equals(str[1])){
						for (int c = row.getFirstCellNum()+3; c < row.getLastCellNum(); c++) {//c 列循环
							Cell cell = row.getCell(c);
							if(c == 6 || c == 7 || c == 10){
								continue;
							}
							cell.setCellType(Cell.CELL_TYPE_STRING);
							str[count] = cell.getStringCellValue();
							count++;
						}
						list2.add(convertUnicodeToCh(str));
						break;
					}
				}
			}
			mapInfo.put("itemized", (null == list2 || list2.size() == 0) ? list : list2);
		}
	}
	
	/**
	 * 验证excel是否全部为空
	 * @param row 当前行
	 * @param firstRow 第一行标题行
	 * @return
	 */
	public static boolean isAllRowEmpty(Row row) {
		int count = 0;
		//单元格数量
		int rowCount = row.getLastCellNum() - row.getFirstCellNum();
		//判断多少个单元格为空
		for (int c = 0; c < rowCount; c++) {
			Cell cell = row.getCell(c);
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK || "".equals((cell+"").trim())){
				count += 1;
			}
		}
 
		if (count == rowCount) {
			return true;
		}
 
		return  false;
	}
}