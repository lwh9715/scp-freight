package com.scp.view.module.finance.edi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.scp.util.ReadExcel;

public class SapExprot {
	/**
	 *  导出SAP到EXCLE
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean exportSapExcle(File fromFile,File toFile,JSONArray array, JSONArray array2) throws FileNotFoundException, IOException {
		if(array==null||array2 == null){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		//导出EXCEL第一个sheet(Header)
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			//遍历导出行
			for (int r = 3; r < array.size()+3; r++) {
				JSONObject json = new JSONObject();
				json = array.getJSONObject(r-3);
				Row row = sheet.getRow(r);
				if (row == null) {
					row = sheet.createRow(r);
				}
				//导出列
				Cell cell = row.getCell(0);
				if (cell == null || "空".equals(cell)) {
					cell = row.createCell(0);
					for (int i = 1; i < 11; i++) {
						row.createCell(i);
					}
				}
				cell.setCellValue(json.get("nos").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(1);
				cell.setCellValue(json.get("actsetcode").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(2);
				cell.setCellValue("KD");
				cell.setCellStyle(cellStyle);
				cell = row.getCell(3);
				cell.setCellValue(json.get("yearperiod").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(4);
				cell.setCellValue(json.get("date2").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(5);
				cell.setCellValue(json.get("period").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(6);
				cell.setCellValue(json.get("cyid").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(7);
				cell.setCellValue(json.get("xrate").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(8);
				cell.setCellValue(json.get("piece").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(9);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.getCell(10);
				cell.setCellValue(json.get("vchdesc").toString());
				cell.setCellStyle(cellStyle);
			}
		}else{
			return false;
		}
		sheet = wb.getSheetAt(1);
		if(sheet != null){
			//遍历导出行
			for (int r = 3; r < array2.size()+3; r++) {
				JSONObject json = new JSONObject();
				json = array2.getJSONObject(r-3);
				Row row = sheet.getRow(r);
				if (row == null) {
					row = sheet.createRow(r);
				}
				//导出列
				Cell cell = row.getCell(0);
				if (cell == null || "空".equals(cell)) {
					cell = row.createCell(0);
					for (int i = 1; i < 35; i++) {
						row.createCell(i);
					}
				}
				cell.setCellValue(json.get("nos").toString()+"");
				cell = row.getCell(1);
				cell.setCellValue(null == json.get("cell2") ? "" : json.get("cell2").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(2);
				cell.setCellValue(null== json.get("cell3")?"":json.get("cell3").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(3);
				cell.setCellValue(null == json.get("cell4") ? "" : json.get("cell4").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(4);
				cell.setCellValue(null== json.get("cell5")?"":json.get("cell5").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(5);
				cell.setCellValue(null == json.get("cell6") ? "" : json.get("cell6").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(6);
				cell.setCellValue(null == json.get("oamt") ? "" : json.get("oamt").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(7);
				cell.setCellValue(null == json.get("damt") ? "" : json.get("damt").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(8);
				cell.setCellValue(null == json.get("cell9") ? "" : json.get("cell9").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(9);
				cell.setCellValue(null == json.get("cell10") ? "" : json.get("cell10").toString());//成本中心
				cell.setCellStyle(cellStyle);
				cell = row.getCell(10);
				cell.setCellValue(null == json.get("cell11") ? "" : json.get("cell11").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(11);
				cell.setCellValue(null == json.get("cell12") ? "" : json.get("cell12").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(12);
				cell.setCellValue(null == json.get("cell13") ? "" : json.get("cell13").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(13);
				cell.setCellValue(null == json.get("cell14") ? "" : json.get("cell14").toString());//基准日期
				cell.setCellStyle(cellStyle);
				cell = row.getCell(14);
				cell.setCellValue(null == json.get("cell15") ? "" : json.get("cell15").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(15);
				cell.setCellValue(null == json.get("cell16") ? "" : json.get("cell16").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(16);
				cell.setCellValue(null == json.get("cell17") ? "" : json.get("cell17").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(17);
				cell.setCellValue(null == json.get("cell18") ? "" : json.get("cell18").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(18);
				cell.setCellValue(null == json.get("cell19") ? "" : json.get("cell19").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(19);
				cell.setCellValue(null == json.get("cell20") ? "" : json.get("cell20").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(20);
				cell.setCellValue(null == json.get("cell21") ? "" : json.get("cell21").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(21);
				cell.setCellValue(null == json.get("cell22") ? "" : json.get("cell22").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(22);
				cell.setCellValue(null == json.get("cell23") ? "" : json.get("cell23").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(23);
				cell.setCellValue(null == json.get("cell24") ? "" : json.get("cell24").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(24);
				cell.setCellValue(null == json.get("cell25") ? "" : json.get("cell25").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(25);
				cell.setCellValue(null == json.get("cell26") ? "" : json.get("cell26").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(26);
				cell.setCellValue(null == json.get("cell27") ? "" : json.get("cell27").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(27);
				cell.setCellValue(null == json.get("cell28") ? "" : json.get("cell28").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(28);
				cell.setCellValue(null == json.get("cell29") ? "" : json.get("cell29").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(29);
				cell.setCellValue(null == json.get("cell30") ? "" : json.get("cell30").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(30);
				cell.setCellValue(null == json.get("cell31") ? "" : json.get("cell31").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(31);
				cell.setCellValue(null == json.get("cell32") ? "" : json.get("cell32").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(32);
				cell.setCellValue(null == json.get("cell33") ? "" : json.get("cell33").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(33);
				cell.setCellValue(null == json.get("cell34") ? "" : json.get("cell34").toString());
				cell.setCellStyle(cellStyle);
				cell = row.getCell(34);
				cell.setCellValue(null == json.get("cell35") ? "" : json.get("cell35").toString());
				cell.setCellStyle(cellStyle);
			}
		}else{
			return false;
		}
		
		if(!toFile.exists()){
			toFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(toFile);
		wb.write(fos);
		//wb.close();
		fos.close();
		return true;
	}
	
	/**
	 *  导出 分组后的 SAP到EXCLE
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
//	public static boolean exportSapExcle2(File fromFile,File toFile,JSONArray array, JSONArray array2) throws FileNotFoundException, IOException {
//		if(array==null||array2 == null){
//			return false;
//		}
//		Workbook wb = ReadExcel.createWb(fromFile);
//		CellStyle cellStyle = wb.createCellStyle();
//		cellStyle.setAlignment((short) 1);
//		cellStyle.setVerticalAlignment((short) 0);
//		cellStyle.setWrapText(true);
//		Integer num = 1000000000;
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		List<String[]> list = new ArrayList<String[]>();
//		String[] strs = new String[11];
//		int count = 3;
//		Sheet sheet = wb.getSheetAt(1);
//		if(sheet != null){
//			//遍历导出行
//			for (int r = 0; r < array2.size(); r++) {
//				JSONObject json = new JSONObject();
//				json = array2.getJSONObject(r);
//				Row row = sheet.getRow(count);
//				if (row == null) {
//					row = sheet.createRow(count);
//				}
//				//导出列
//				Cell cell = row.getCell(0);
//				if (cell == null || "空".equals(cell)) {
//					cell = row.createCell(0);
//					for (int i = 1; i < 35; i++) {
//						row.createCell(i);
//					}
//				}
//				
//				//统计第一个sheet数据
//				num += 1;
//				strs = new String[11];
//				strs[0] = num+"";
//				strs[1] = json.get("账套代码")+"";		//head公司代码
//				strs[2] = "KD";
//				strs[3] = json.get("yearperiod")+"";	//凭证日期
//				strs[4] = json.get("yearperiod")+"";	//过账日期
//				strs[5] = json.get("期间")+"";
//				strs[6] = json.get("币种")+"";
//				strs[7] = json.get("xrate")+"";
//				strs[8] = "2";
//				strs[9] = "";
//				strs[10] = "UFMS系统结算";
//				list.add(strs);
//				
//				cell.setCellValue(num);
//				cell = row.getCell(1);
//				cell.setCellValue(null == json.get("cell2") ? "" : json.get("cell2").toString());
//				cell = row.getCell(2);
//				cell.setCellValue(null== json.get("cell3")?"":json.get("cell3").toString());
//				cell = row.getCell(3);
//				cell.setCellValue(null == json.get("cell4") ? "" : json.get("cell4").toString());
//				cell = row.getCell(4);
//				String code = "1122010001";
//				if(null != json.get("客商编码") && String.valueOf(json.get("客商编码")).getBytes().length == 4){//内部关联方010001 客商编码四位数则为内部关联方
//					if((null== json.get("cell5")?"":json.get("cell5").toString()).indexOf("1122")>-1){
//						cell.setCellValue("1122010001");
//						code = "1122010001";
//					}else{
//						cell.setCellValue("2202010001");
//						code = "2202010001";
//					}
//				}else{
//					//屏幕科目不为四位数则总账科目为1122030001
//					if((null== json.get("cell5")?"":json.get("cell5").toString()).indexOf("1122")>-1){
//						cell.setCellValue("1122030001");
//						code = "1122030001";
//					}else{
//						cell.setCellValue("2202030001");
//						code = "2202030001";
//					}
//				}
//				cell = row.getCell(5);
//				cell.setCellValue(null == json.get("cell6") ? "" : json.get("cell6").toString());
//				cell = row.getCell(6);
//				cell.setCellValue(null == json.get("oamt") ? "" : json.get("oamt").toString());
//				cell = row.getCell(7);
//				cell.setCellValue(null == json.get("damt") ? "" : json.get("damt").toString());
//				cell = row.getCell(8);
//				cell.setCellValue(null == json.get("cell9") ? "" : json.get("cell9").toString());
//				if(code.indexOf("1122")>-1){
//					cell = row.getCell(24);
//					cell.setCellValue(null == json.get("利润中心") ? "" : json.get("利润中心").toString());//利润中心
//				}else if(code.indexOf("2202")>-1){
//					cell = row.getCell(9);
//					cell.setCellValue(null == json.get("成本中心") ? "" : json.get("成本中心").toString());//成本中心
//				}
//				cell = row.getCell(10);
//				cell.setCellValue(null == json.get("cell11") ? "" : json.get("cell11").toString());
//				cell = row.getCell(11);
//				cell.setCellValue(null == json.get("cell12") ? "" : json.get("cell12").toString());
//				cell = row.getCell(12);
//				cell.setCellValue(null == json.get("cell13") ? "" : json.get("cell13").toString());
//				cell = row.getCell(13);
//				cell.setCellValue(null == json.get("yearperiod") ? "" : json.get("yearperiod").toString());//基准日期
//				cell = row.getCell(14);
//				cell.setCellValue(null == json.get("cell15") ? "" : json.get("cell15").toString());
//				cell = row.getCell(15);
//				cell.setCellValue(null == json.get("cell16") ? "" : json.get("cell16").toString());
//				cell = row.getCell(16);
//				cell.setCellValue(null == json.get("cell17") ? "" : json.get("cell17").toString());
//				cell = row.getCell(17);
//				cell.setCellValue(null == json.get("cell18") ? "" : json.get("cell18").toString());
//				cell = row.getCell(18);
//				cell.setCellValue(null == json.get("cell19") ? "" : json.get("cell19").toString());
//				cell = row.getCell(19);
//				cell.setCellValue(null == json.get("cell20") ? "" : json.get("cell20").toString());
//				cell = row.getCell(20);
//				cell.setCellValue(null == json.get("cell21") ? "" : json.get("cell21").toString());
//				cell = row.getCell(21);
//				cell.setCellValue(null == json.get("cell22") ? "" : json.get("cell22").toString());
//				cell = row.getCell(22);
//				cell.setCellValue(null == json.get("cell23") ? "" : json.get("cell23").toString());
//				cell = row.getCell(23);
//				cell.setCellValue(null == json.get("cell24") ? "" : json.get("cell24").toString());
//				cell = row.getCell(25);
//				cell.setCellValue(null == json.get("cell27") ? "" : json.get("cell27").toString());
//				cell = row.getCell(26);
//				// cell.setCellValue(null == json.get("cell27") ? "" : json.get("cell27").toString());
//				cell = row.getCell(27);
//				
//				cell.setCellValue("");//2202，1122不需要贸易伙伴	
//				cell = row.getCell(28);
//				cell.setCellValue(null == json.get("cell29") ? "" : json.get("cell29").toString());
//				cell = row.getCell(29);
//				cell.setCellValue(null == json.get("cell30") ? "" : json.get("cell30").toString());
//				cell = row.getCell(30);
//				cell.setCellValue(null == json.get("cell31") ? "" : json.get("cell31").toString());
//				if(null != json.get("cell5") && StrUtils.isNull(String.valueOf(json.get("cell5"))) && String.valueOf(json.get("cell5")).indexOf("2202")==-1){//总账科目2202开头的不需要供应商
//					cell.setCellValue(null == json.get("cell32") ? "" : json.get("cell32").toString());//供应商
//				}
//				cell = row.getCell(31);
//				
//				cell = row.getCell(32);
//				cell.setCellValue(null == json.get("cell33") ? "" : json.get("cell33").toString());
//				cell = row.getCell(33);
//				cell.setCellValue(null == json.get("cell34") ? "" : json.get("cell34").toString());
//				cell = row.getCell(34);
//				if(null != json.get("cell5") && StrUtils.isNull(String.valueOf(json.get("cell5"))) && String.valueOf(json.get("cell5")).indexOf("2202")>-1){
//					cell.setCellValue(null == json.get("cell35") ? "" : json.get("cell35").toString());//客户
//				}
//				cell = row.getCell(37);
//				if(null == cell){
//					cell = row.createCell(37);
//				}
//				cell.setCellValue(null == json.get("cell38") ? "" : json.get("cell38").toString());
//				count += 1;
//				
//				row = sheet.getRow(count);
//				if (row == null) {
//					row = sheet.createRow(count);
//				}
//				//导出列
//				cell = row.getCell(0);
//				if (cell == null || "空".equals(cell)) {
//					cell = row.createCell(0);
//					for (int i = 1; i < 35; i++) {
//						row.createCell(i);
//					}
//				}
//				cell.setCellValue(num);
//				cell = row.getCell(1);
//				if(null == json.get("cell2")){
//					cell.setCellValue("");
//				}
//				if("01".equals(String.valueOf(json.get("cell2")))){
//					cell.setCellValue("50");
//				}else if("11".equals(String.valueOf(json.get("cell2")))){
//					cell.setCellValue("40");
//				}else if("31".equals(String.valueOf(json.get("cell2")))){
//					cell.setCellValue("40");
//				}else if("21".equals(String.valueOf(json.get("cell2")))){
//					cell.setCellValue("50");
//				}
//				String str2 = "";//新生成数据的总账科目、屏幕科目
//				if((null== json.get("cell5")?"":json.get("cell5").toString()).indexOf("1122010001")>-1){
//					str2 = "6001010001";
//				}else if((null== json.get("cell5")?"":json.get("cell5").toString()).indexOf("1122030001")>-1){
//					str2 = "6001030001";
//				}else if((null== json.get("cell5")?"":json.get("cell5").toString()).indexOf("2202010001")>-1){
//					str2 = "6401010001";
//				}else if((null== json.get("cell5")?"":json.get("cell5").toString()).indexOf("2202030001")>-1){
//					str2 = "6401030001";
//				}
//				cell = row.getCell(2);
//				cell.setCellValue(str2);
//				cell = row.getCell(3);
//				cell.setCellValue(null == json.get("cell4") ? "" : json.get("cell4").toString());
//				cell = row.getCell(4);
//				cell.setCellValue(str2);
//				cell = row.getCell(5);
//				cell.setCellValue(null == json.get("cell6") ? "" : json.get("cell6").toString());
//				cell = row.getCell(6);
//				cell.setCellValue(null == json.get("oamt") ? "" : json.get("oamt").toString());
//				cell = row.getCell(7);
//				cell.setCellValue(null == json.get("damt") ? "" : json.get("damt").toString());
//				cell = row.getCell(8);
//				cell.setCellValue(null == json.get("cell9") ? "" : json.get("cell9").toString());
//				if(str2.indexOf("6401")>-1){//6401为应付，需要成本中心
//					cell = row.getCell(9);
//					cell.setCellValue(null == json.get("成本中心") ? "" : json.get("成本中心").toString());//成本中心
//				}
//				cell = row.getCell(10);
//				cell.setCellValue(null == json.get("cell11") ? "" : json.get("cell11").toString());
//				cell = row.getCell(11);
//				cell.setCellValue(null == json.get("cell12") ? "" : json.get("cell12").toString());
//				cell = row.getCell(12);
//				cell.setCellValue(null == json.get("cell13") ? "" : json.get("cell13").toString());
//				cell = row.getCell(13);
//				cell.setCellValue(null == json.get("yearperiod") ? "" : json.get("yearperiod").toString());//基准日期
//				cell = row.getCell(14);
//				cell.setCellValue(null == json.get("cell15") ? "" : json.get("cell15").toString());
//				cell = row.getCell(15);
//				cell.setCellValue(null == json.get("cell16") ? "" : json.get("cell16").toString());
//				cell = row.getCell(16);
//				cell.setCellValue(null == json.get("cell17") ? "" : json.get("cell17").toString());
//				cell = row.getCell(17);
//				cell.setCellValue(null == json.get("cell18") ? "" : json.get("cell18").toString());
//				cell = row.getCell(18);
//				cell.setCellValue(null == json.get("cell19") ? "" : json.get("cell19").toString());
//				cell = row.getCell(19);
//				cell.setCellValue(null == json.get("cell20") ? "" : json.get("cell20").toString());
//				cell = row.getCell(20);
//				cell.setCellValue(null == json.get("cell21") ? "" : json.get("cell21").toString());
//				cell = row.getCell(21);
//				cell.setCellValue(null == json.get("cell22") ? "" : json.get("cell22").toString());
//				cell = row.getCell(22);
//				cell.setCellValue(null == json.get("cell23") ? "" : json.get("cell23").toString());
//				cell = row.getCell(23);
//				cell.setCellValue(null == json.get("cell24") ? "" : json.get("cell24").toString());
//				if(str2.indexOf("6001")>-1){//6001为应收，需要利润中心
//					cell = row.getCell(24);
//					cell.setCellValue(null == json.get("利润中心") ? "" : json.get("利润中心").toString());//利润中心
//				}
//				cell = row.getCell(25);
//				cell.setCellValue(null == json.get("cell26") ? "" : json.get("cell26").toString());
//				
//				cell = row.getCell(27);//贸易伙伴
//				String str = "";
//				if(null == json.get("客商编码")){//内部关联方取账套代码，外部关联方取客商编码
//					cell.setCellValue("客商编码为空");
//				}else if(String.valueOf(json.get("客商编码")).getBytes().length == 4){
//					cell.setCellValue(null == json.get("账套代码") ? "账套代码为空" : String.valueOf(json.get("账套代码")));
//				}else{
//					cell.setCellValue(String.valueOf(json.get("客商编码")));
//					str	= String.valueOf(json.get("客商编码"));
//				}
//				if(str2.indexOf("6401")>-1){//64开头需要税码J0
//					cell = row.getCell(25);
//					cell.setCellValue("J0");
//				}else{//60开头税码固定X0
//					cell = row.getCell(25);
//					cell.setCellValue("X0");
//				}
//				cell = row.getCell(29);
//				cell.setCellValue("B128");//物料组，新生成的数据物料组固定B128
//				cell = row.getCell(30);
//				cell.setCellValue(null == json.get("cell31") ? "" : json.get("cell31").toString());
//				cell = row.getCell(31);
//				if(str2.indexOf("6401")>-1){//供应商，包含6401另取客商编码
//					//cell.setCellValue(null == json.get("客商编码") ? "" : json.get("客商编码").toString());
//					cell.setCellValue(null == json.get("cell32") ? "" : json.get("cell32").toString());
//				}else{
//					//cell.setCellValue(null == json.get("cell32") ? "" : json.get("cell32").toString());
//					cell = row.getCell(34);
//					cell.setCellValue(null == json.get("cell35") ? "" : json.get("cell35").toString());
//				}
//				cell = row.getCell(32);
//				cell.setCellValue(null == json.get("cell33") ? "" : json.get("cell33").toString());
//				cell = row.getCell(33);
//				cell.setCellValue(null == json.get("cell34") ? "" : json.get("cell34").toString());
//				cell = row.getCell(37);
//				if(null == cell){
//					cell = row.createCell(37);
//				}
//				cell.setCellValue(null == json.get("cell38") ? "" : json.get("cell38").toString());
//				count += 1;
//			}
//		}else{
//			return false;
//		}
//		
//		sheet = wb.getSheetAt(0);
//		for (int r = 3; r < list.size()+3; r++) {
//			Row row = sheet.getRow(r);
//			if (row == null) {
//				row = sheet.createRow(r);
//			}
//			strs = list.get(r-3);
//			for (int j = 0; j < strs.length; j++) {
//				Cell cell = row.getCell(j) == null ? row.createCell(j) : row.getCell(j);
//				cell.setCellValue(strs[j]);
//			}
//		}
//		
//		if(!toFile.exists()){
//			toFile.createNewFile();
//		}
//		FileOutputStream fos = new FileOutputStream(toFile);
//		wb.write(fos);
//		//wb.close();
//		fos.close();
//		return true;
//	}
	
	/**
	 *  导出 分组后的 SAP到EXCLE
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean exportSapReport(File fromFile,File toFile,JSONArray array, JSONArray array2) throws FileNotFoundException, IOException {
		if(array==null||array2 == null){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		
		Sheet hksheet = wb.getSheetAt(2);//香港利润成本中心
		
		Integer num = 1000000000;
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<String[]> list = new ArrayList<String[]>();
		String[] strs = new String[11];
		int count = 3;
		Sheet sheet = wb.getSheetAt(1);
		if(sheet != null){
			//遍历导出行
			for (int r = 0; r < array2.size(); r++) {
				JSONObject json = new JSONObject();
				json = array2.getJSONObject(r);
				Row row = sheet.getRow(count);
				if (row == null) {
					row = sheet.createRow(count);
				}
				//导出列
				Cell cell = row.getCell(0);
				if (cell == null || "空".equals(cell)) {
					cell = row.createCell(0);
					for (int i = 1; i < 35; i++) {
						row.createCell(i);
					}
				}
				
				//第一个sheet Header
				num += 1;
				strs = new String[11];
				strs[0] = num+"";						//凭证序号
				strs[1] = json.get("公司代码")+"";		//公司代码
				strs[2] = "KD";							//凭证类型
				strs[3] = json.get("过账日期")+"";		//凭证日期
				strs[4] = json.get("过账日期")+"";		//过账日期
				strs[5] = json.get("期间")+"";			//期间
				strs[6] = json.get("币种")+"";			//币种
				strs[7] = json.get("汇率")+"";			//汇率
				strs[8] = "2";							//附件张数
				strs[9] = "";							//参照
				strs[10] = json.get("凭证类别")+"";				//凭证抬头文本
				list.add(strs);
				
				//第二个sheet Item
				cell.setCellValue(num);																			//凭证序号
				cell = row.getCell(1);
				cell.setCellValue(null == json.get("记帐代码") ? "" : json.get("记帐代码").toString());			//记帐代码
				cell = row.getCell(2);
				cell.setCellValue(null == json.get("屏幕科目") ? "" : json.get("屏幕科目").toString());			//屏幕科目
				cell = row.getCell(3);
				cell.setCellValue(null == json.get("特别总帐标识") ? "" : json.get("特别总帐标识").toString());	//特别总帐标识
				cell = row.getCell(4);
				cell.setCellValue(null == json.get("总账科目") ? "" : json.get("总账科目").toString());			//总账科目
				cell = row.getCell(5);
				cell.setCellValue(null == json.get("事物类型") ? "" : json.get("事物类型").toString());			//事物类型
				cell = row.getCell(6);
				cell.setCellValue(null == json.get("凭证货币金额") ? "" : json.get("凭证货币金额").toString());	//凭证货币金额
				cell = row.getCell(7);
				cell.setCellValue(null == json.get("本位币金额") ? "" : json.get("本位币金额").toString());		//本位币金额
				cell = row.getCell(8);
				cell.setCellValue(null == json.get("居民货币金额") ? "" : json.get("居民货币金额").toString());	//居民货币金额
//				cell = row.getCell(9);
//				cell.setCellValue(null == json.get("成本中心编号") ? "" : json.get("成本中心编号").toString());	//成本中心
				cell = row.getCell(13);
				cell.setCellValue(null == json.get("过账日期") ? "" : json.get("过账日期").toString());			//基准日期
				cell = row.getCell(14);
				cell.setCellValue(null == json.get("原因代码") ? "" : json.get("原因代码").toString());			//原因代码
//				cell = row.getCell(19);
//				cell.setCellValue(null == json.get("参考码3") ? "" : json.get("参考码3").toString());			//参考码3
				cell = row.getCell(20);
				cell.setCellValue(null == json.get("项目文本") ? "" : json.get("项目文本").toString());			//项目文本
				cell = row.getCell(21);
				cell.setCellValue(null == json.get("反记账标识") ? "" : json.get("反记账标识").toString());		//反记账标识
				cell = row.getCell(23);
				cell.setCellValue(null == json.get("分配") ? "" : json.get("分配").toString());					//分配
				cell = row.getCell(24);																			//利润中心
				String code1 = "";
				String code2 = "";
				if("否".equals(json.get("ishk").toString())){
					cell.setCellValue(null == json.get("利润中心") ? "" : json.get("利润中心").toString());			//利润中心
				}else{
					if(hksheet != null){
						for (int i = 0; i < hksheet.getLastRowNum(); i++) {
							Row hkrow = hksheet.getRow(i);
							for (int j = 0; j < hkrow.getLastCellNum(); j++) {
								Cell hkcell = hkrow.getCell(j);
								if(json.get("dept").toString().equals(String.valueOf(hkcell))){
									code1 = String.valueOf(hkrow.getCell(j+1));	//香港成本中心
									code2 = String.valueOf(hkrow.getCell(j+2));	//香港利润中心
									break;
								}
							}
						}
					}
					cell.setCellValue(code2);
				}																
				cell = row.getCell(28);
				cell.setCellValue(null == json.get("款项内容") ? "" : json.get("款项内容").toString());			//款项内容
				cell = row.getCell(30);
				cell.setCellValue(null == json.get("付款条件") ? "" : json.get("付款条件").toString());			//付款条件
//				cell = row.getCell(31);
//				cell.setCellValue(null == json.get("供应商") ? "" : json.get("供应商").toString());				//供应商
//				cell = row.getCell(34);
//				cell.setCellValue(null == json.get("客户") ? "" : json.get("客户").toString());					//客户
				cell = null == row.getCell(37) ? row.createCell(37) : row.getCell(37);
				cell.setCellValue(null == json.get("凭证号") ? "" : json.get("凭证号").toString());				//凭证号
				count += 1;
				
				row = sheet.getRow(count);
				if (row == null) {
					row = sheet.createRow(count);
				}
				//根据已有凭证生成60,64凭证
				cell = row.getCell(0);
				if (cell == null || "空".equals(cell)) {
					cell = row.createCell(0);
					for (int i = 1; i < 35; i++) {
						row.createCell(i);
					}
				}
				cell.setCellValue(num);																			//凭证序号
				cell = row.getCell(1);
				switch (Integer.valueOf(String.valueOf(json.get("记帐代码")))) {									//记账代码
					case 1:
						cell.setCellValue("50");
						break;
					case 11:
						cell.setCellValue("40");
						break;
					case 31:
						cell.setCellValue("40");
						break;
					case 21:
						cell.setCellValue("50");
						break;
					default:
						cell.setCellValue("50");
						break;
				}
				
				String str2 = "";//新生成数据的总账科目、屏幕科目
				if((null== json.get("总账科目")?"":json.get("总账科目").toString()).indexOf("1122010001")>-1){
					str2 = "6001010001";
				}else if((null== json.get("总账科目")?"":json.get("总账科目").toString()).indexOf("1122030001")>-1){
					str2 = "6001030001";
				}else if((null== json.get("总账科目")?"":json.get("总账科目").toString()).indexOf("2202010001")>-1){
					str2 = "6401010001";
				}else if((null== json.get("总账科目")?"":json.get("总账科目").toString()).indexOf("2202030001")>-1){
					str2 = "6401030001";
				}
				cell = row.getCell(2);
				cell.setCellValue(str2);																		//屏幕科目
				cell = row.getCell(4);
				cell.setCellValue(str2);																		//总账科目
				cell = row.getCell(6);
				cell.setCellValue(null == json.get("凭证货币金额") ? "" : json.get("凭证货币金额").toString());	//凭证货币金额
				cell = row.getCell(7);
				cell.setCellValue(null == json.get("本位币金额") ? "" : json.get("本位币金额").toString());		//本位币金额
				cell = row.getCell(8);
				cell.setCellValue(null == json.get("居民货币金额") ? "" : json.get("居民货币金额").toString());	//居民货币金额
				cell = row.getCell(9);
				
				if("否".equals(json.get("ishk").toString())){
					cell.setCellValue(null == json.get("成本中心编号") ? "" : json.get("成本中心编号").toString());	//成本中心
				}else{
					cell.setCellValue(code1);
				}
				cell = row.getCell(13);
				cell.setCellValue(null == json.get("过账日期") ? "" : json.get("过账日期").toString());				//基准日期
				cell = row.getCell(14);
				cell.setCellValue(null == json.get("原因代码") ? "" : json.get("原因代码").toString());				//原因代码
				cell = row.getCell(19);
				cell.setCellValue(null == json.get("参考码3") ? "" : json.get("参考码3").toString());				//参考码3
				cell = row.getCell(20);
				cell.setCellValue(null == json.get("项目文本") ? "" : json.get("项目文本").toString());			//项目文本
				cell = row.getCell(21);
				cell.setCellValue(null == json.get("反记账标识") ? "" : json.get("反记账标识").toString());		//反记账标识
				cell = row.getCell(23);
				cell.setCellValue(null == json.get("分配") ? "" : json.get("分配").toString());					//分配
				cell = row.getCell(24);
				if("否".equals(json.get("ishk").toString())){
					cell.setCellValue(null == json.get("利润中心") ? "" : json.get("利润中心").toString());		//利润中心
				}else{
					cell.setCellValue(code2);
				}
//				cell.setCellValue(null == json.get("利润中心") ? "" : json.get("利润中心").toString());			//利润中心
				cell = row.getCell(25);																			//税码
				if(str2.indexOf("6401")>-1){//64开头需要税码J0
					cell.setCellValue("J0");
				}else{						//60开头税码固定X0
					cell.setCellValue("X0");
				}
				cell = row.getCell(27);
				cell.setCellValue(null == json.get("贸易伙伴") ? "" : json.get("贸易伙伴").toString());			//贸易伙伴
				cell = row.getCell(29);
				cell.setCellValue("B128");																		//物料组
				cell = row.getCell(31);
				cell.setCellValue(null == json.get("供应商") ? "" : json.get("供应商").toString());				//供应商
				cell = row.getCell(34);
				cell.setCellValue(null == json.get("客户") ? "" : json.get("客户").toString());					//客户
				cell = null == row.getCell(37) ? row.createCell(37) : row.getCell(37);
				cell.setCellValue(null == json.get("凭证号") ? "" : json.get("凭证号").toString());				//凭证号
				count += 1;
			}
		}else{
			return false;
		}
		
		sheet = wb.getSheetAt(0);
		for (int r = 3; r < list.size()+3; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				row = sheet.createRow(r);
			}
			strs = list.get(r-3);
			for (int j = 0; j < strs.length; j++) {
				Cell cell = row.getCell(j) == null ? row.createCell(j) : row.getCell(j);
				cell.setCellValue(strs[j]);
			}
		}
		
		if(!toFile.exists()){
			toFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(toFile);
		wb.write(fos);
		//wb.close();
		fos.close();
		return true;
	}

	public static boolean synExport(File fromFile,File toFile, JSONArray array, HttpServletResponse response) {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" +toFile);
        try {
        	XSSFWorkbook workbook = new XSSFWorkbook();
        	XSSFSheet sheet = workbook.createSheet("sheet1");
        	
        	for (int i = 0; i < array.size(); i++) {//第0行是表头
        		XSSFRow row = sheet.createRow(i);
        		
        		JSONObject obj = array.getJSONObject(i);
            	Iterator<String> it = obj.keys();
            	String[] tit = new String[obj.size()];
            	int t = 0;
        		while(it.hasNext()){
        			if(i==0){
        				row.createCell(t).setCellValue(it.next());// 表头
        				t++;
        				continue;
        			}else{
        				row.createCell(t).setCellValue(obj.get(it.next()).toString());// 遍历内容
        				t++;
        				continue;
        			}
            	}
			}
        	if(!toFile.exists()){
    			toFile.createNewFile();
    		}
    		FileOutputStream fos = new FileOutputStream(toFile);
    		workbook.write(fos);
    		//wb.close();
    		fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
