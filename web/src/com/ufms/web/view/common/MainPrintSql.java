package com.ufms.web.view.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainPrintSql {

	/**
	 * 小程序用于跑SQL
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        String cellData = null;
        StringBuffer str = new StringBuffer();
        String filePath = "C:/Users/Administrator/Desktop/MDG1.xlsx";
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
        	  wb = new HSSFWorkbook(is);
          } else if(POIXMLDocument.hasOOXMLHeader(is)){
              //当excel是2007时
        	  wb = new XSSFWorkbook(is);
          }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(wb != null){
            //获取第一个sheet
//            sheet = wb.getSheetAt(0);
        	sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //设置头信息
            for (int r = 1; r < rownum; r++) {
            	row = sheet.getRow(r);
//            	if (row == null || row.getLastCellNum() < 1){
//					continue;
//				}
//				if (row.getCell(1) == null || row.getCell(0)==null) {
//					continue;
//				}
				String name = String.valueOf(row.getCell(0)).replace("'", "''''").trim();
				String code =String.valueOf(row.getCell(1)).replace("'", "''''").trim();
				if(code.length()==8){
					code = "00"+code;
				}
//				if(StrUtils.isNull(code)){
//					continue;
//				}
//				str.append("UPDATE sys_corporation SET NAMEC = '"+newname+"',UPDATER = 'demo' WHERE namec = '"+name+"' AND isdelete = false;"+"\n");
            	//SAP导出新增表START
//            	String cell[] = new String[14];
//            	for (int i = 0; i < 14; i++) {
//            		try {
//            			Cell c = row.getCell(i);
//            			if(null == row.getCell(i) || "".equals(row.getCell(i))){
//            				cell[i] = "''";
//            			}else{
//            				cell[i] = "".equals(String.valueOf(row.getCell(i))) ? "''" :"'"+String.valueOf(row.getCell(i)).replace(".0", "")+"'";
//            			}
//					} catch (Exception e) {
//						e.printStackTrace();
//						System.out.println(r+":"+i);
//					}
//				}
            	
//            	str.append("INSERT INTO fs_actext (actcode,bkcodelend,bkcodeloan,tstypelend,tstypeloan,specialledgermark,iscostcenter,isprofitcenter,arreasoncode,apreasoncode,taxcode,tradepartner,iscust,issup)");
//            	str.append("VALUES ("+cell[13]+","+cell[0]+","+cell[1]+","+cell[2]+","+cell[3]+","+cell[4]+","+("'需要'".equals(cell[5]) ? true : false)+","+("'需要'".equals(cell[6]) ? true : false)+","+cell[7]+","+cell[8]+","+cell[9]+","+("'需要'".equals(cell[10]) ? true : false)+","+("'需要'".equals(cell[11]) ? true : false)+","+("'需要'".equals(cell[12]) ? true : false)+"); "+"\n");
            	//SAP导出新增表END
            	//客商编码批量导入
				str.append("INSERT INTO sys_corpext    (id, customerid, merchantcode) ");
				str.append("SELECT getid(), (select id from sys_corporation where namec='"+name+"' LIMIT 1),'"+code+"'  FROM _virtual ");
				str.append(" WHERE  exists (select id from sys_corporation where namec='"+name+"' LIMIT 1) and not exists (select sct.id from sys_corporation sc,sys_corpext sct where sc.id=sct.customerid and namec='"+name+"' LIMIT 1) ; "+"\n");
            }
            System.out.print(str.toString());
        }
	}

}
