package com.scp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 
 * @author Bruce
 * 
 */
public class ReadExcel{
	
	public static List<List<Object[]>> read(File file) throws FileNotFoundException, IOException {
		//String url = "." + File.separatorChar + "imp_shipquote_fee.xls";
		// 得到Excel工作簿对象
		Workbook wb = ReadExcel.createWb(file);
		List<List<Object[]>> list1 = new ArrayList<List<Object[]>>();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			List<Object[]> list = listFromSheet(wb.getSheetAt(i));
			list1.add(list);
			
			/*for (Object[] o : list) {
				for (Object oo : o) {
						//System.out.print(oo.toString());
				}
				ApplicationUtils.debug();
			}*/
		}
		return list1;
	}

	/**
	 * Excel导入工作单（托书）
	 * @param file
	 * @return 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> importJobsForExcel(File file) throws FileNotFoundException, IOException {
		Workbook wb = ReadExcel.createWb(file);
		Sheet sheet = null;
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			if("Jobs Templete".equals(wb.getSheetAt(i).getSheetName())){
				sheet = wb.getSheetAt(i);
			}
		}
		if(sheet != null){
			int rowTotal = sheet.getPhysicalNumberOfRows();
			//System.out.println(sheet.getSheetName() + "一共有" + rowTotal + "行数据");
			
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				////System.out.println("开始读取第"+r+"行:");
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					////System.out.print("第"+r+"行第"+c+"个:");
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					////System.out.print(cells[c].toString());
					////System.out.print(";");
					if(r==1&&c==0){//发货人
						map.put("cnortitle",getValueFromCell(cell).toString());
					}else if(r==3&&c==0){//收货人
						map.put("cneetitle",getValueFromCell(cell).toString());
					}else if(r==5&&c==0){//通知人
						map.put("notifytitle",getValueFromCell(cell).toString());
					}else if(r==7&&c==0){//前程运输
						map.put("pretrans",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==7&&c==2){//收货地
						map.put("poa",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==9&&c==0){//船名
						map.put("vessel",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==9&&c==2){//航次
						map.put("voyage",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==0){//装货港
						map.put("pol",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==2){//卸货港
						map.put("pdd",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==4){//目的港
						map.put("pod",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==7){//目的地
						map.put("destination",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==14&&c==0){//标记与号码
						map.put("cntinfos",getValueFromCell(cell).toString());
					}else if(r==14&&c==3){//货物描述
						map.put("goodsinfo",getValueFromCell(cell).toString());
					}else if(r==14&&c==8){//毛重
						map.put("grswgt",getValueFromCell(cell).toString());
					}else if(r==14&&c==9){//体积
						map.put("cbm",getValueFromCell(cell).toString());
					}else if(r==26&&c==0){//目的港代理
						map.put("agentitle",getValueFromCell(cell).toString());
					}
				}
			}
		}
		//System.out.println("读取完毕,共读取"+map.size()+"个数据.");
		return map;
	}
	

	/**
	 * Excel导入工作单（托书）
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> importJobsForExcelAir(File file) throws FileNotFoundException, IOException {
		Workbook wb = ReadExcel.createWb(file);
		Sheet sheet = null;
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			if("Jobs Templete".equals(wb.getSheetAt(i).getSheetName())){
				sheet = wb.getSheetAt(i);
			}
		}
		if(sheet != null){
			int rowTotal = sheet.getPhysicalNumberOfRows();
			//System.out.println(sheet.getSheetName() + "一共有" + rowTotal + "行数据");

			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				////System.out.println("开始读取第"+r+"行:");
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					////System.out.print("第"+r+"行第"+c+"个:");
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					////System.out.print(cells[c].toString());
					////System.out.print(";");
					if(r==1&&c==0){//发货人
						map.put("cnortitle",getValueFromCell(cell).toString());
					}else if(r==3&&c==0){//收货人
						map.put("cneetitle",getValueFromCell(cell).toString());
					}else if(r==5&&c==0){//通知人
						map.put("notifytitle",getValueFromCell(cell).toString());
					}else if(r==7&&c==0){//前程运输
						map.put("pretrans",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==7&&c==2){//收货地
						map.put("poa",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==9&&c==0){//船名
						map.put("vessel",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==9&&c==2){//航次
						map.put("voyage",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==0){//装货港
						map.put("pol",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==2){//卸货港
						map.put("pdd",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==4){//目的港
						map.put("pod",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==11&&c==7){//目的地
						map.put("destination",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==14&&c==0){//标记与号码
						map.put("cntinfos",getValueFromCell(cell).toString());
					}else if(r==14&&c==3){//货物描述
						map.put("goodsinfo",getValueFromCell(cell).toString());
					}else if(r==14&&c==8){//毛重
						map.put("grswgt",getValueFromCell(cell).toString());
					}else if(r==14&&c==9){//体积
						map.put("cbm",getValueFromCell(cell).toString());
					}else if(r==28&&c==0){//目的港代理
						map.put("agentdescode",getValueFromCell(cell).toString());
					}
				}
			}
		}
		//System.out.println("读取完毕,共读取"+map.size()+"个数据.");
		return map;
	}

	/**
	 * 提箱单PDF
	 * @param file
	 * @return 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> importSuitcaseList(File file) throws FileNotFoundException, IOException {
//		使用PDFBox提取PDF中的文本内容
		String content = null;
		PDDocument pdfdocument = null;
		FileInputStream is = new FileInputStream(file.getAbsolutePath());
		PDFParser parser = new PDFParser(is);
		parser.parse();
		pdfdocument = parser.getPDDocument();
		PDFTextStripper stripper = new PDFTextStripper();
		content = stripper.getText(pdfdocument);
		Map<String, String> map = new HashMap<String, String>();
		String vevo = (content.substring(content.indexOf("船名/航次(Vessel):"), content.indexOf("船舶劳氏编码:"))).replaceAll("\r|\n", "");
		map.put("vessel", vevo.substring(vevo.indexOf(": ")+2, vevo.lastIndexOf("/")));//船名
		map.put("voyage", vevo.substring(vevo.lastIndexOf("/")+1, vevo.length()));//航次
		String terminal = (content.substring(content.indexOf("挂靠码头: "), content.indexOf("开港时间(CY open): "))).replaceAll("\r|\n", "");
		map.put("mblterminal", terminal.substring(terminal.lastIndexOf("):")+6, terminal.length()));//挂靠码头
		String mblcyopen = (content.substring(content.indexOf("开港时间(CY open):"), content.indexOf("(集装箱开始入港时间)"))).replaceAll("\r|\n", "");
		map.put("mblcyopen", mblcyopen.substring(mblcyopen.lastIndexOf("):")+3, mblcyopen.length()-2));//开港时间
		String clstime = (content.substring(content.indexOf("截止时间 :"), content.indexOf("(请在此时间前完成集装箱入港)"))).replaceAll("\r|\n", "");
		map.put("clstime", clstime.substring(clstime.indexOf(":")+6, clstime.length()-2));//截关时间
		String sidate = (content.substring(content.indexOf("(入港清单)截止时间:"),content.indexOf("(入港清单)截止时间:")+28)).replaceAll("\r|\n", "");
		map.put("sidate", sidate.substring(sidate.indexOf(":")+2, sidate.length()));//截补料时间
		String vgmdate = (content.substring(content.indexOf("船代 VGM 截止时间:"),content.indexOf("船代 VGM 截止时间:")+28)).replaceAll("\r|\n", "");
		map.put("vgmdate", vgmdate.substring(vgmdate.indexOf(":")+1, vgmdate.length()));//截VGM时间
		String etd = (content.substring(content.indexOf("Price Calculation Date:")+23,content.indexOf("Price Calculation Date:")+34)).replaceAll("\r|\n", "");
		map.put("etd", etd);//截VGM时间
//		System.out.println(etd+"、、、、、、、、");
		
		//PDFBox坐标读取方法
//		PDDocument document = null;
//        try
//        {
//            document = PDDocument.load( file);
//            if( document.isEncrypted() )
//            {
//                try {
//					document.decrypt( "" );
//				} catch (CryptographyException e) {
//					e.printStackTrace();
//				}
//            }
//            PDFTextStripperByArea strippers = new PDFTextStripperByArea();
//            strippers.setSortByPosition( true );
//            Rectangle rect = new Rectangle(400, 600, 80, 20);
//            strippers.addRegion( "class1", rect );
//            List allPages = document.getDocumentCatalog().getAllPages();
//            PDPage firstPage = (PDPage)allPages.get(0);
//            strippers.extractRegions( firstPage );
//            map.put("eta", strippers.getTextForRegion( "class1" ).replaceAll("\r|\n", ""));
//            PDFTextStripperByArea strippers1 = new PDFTextStripperByArea();
//            strippers1.setSortByPosition( true );
//            Rectangle rect1 = new Rectangle(480, 600, 80, 20);
//            strippers1.addRegion( "class1", rect1 );
//            List allPages1 = document.getDocumentCatalog().getAllPages();
//            PDPage firstPage1 = (PDPage)allPages1.get(0);
//            strippers1.extractRegions( firstPage1 );
//            map.put("etd", strippers1.getTextForRegion( "class1" ).replaceAll("\r|\n", ""));
//        }
//        finally
//        {
//            if( document != null )
//            {
//                document.close();
//            }
//        }
		return map;
	}
	
	/**
	 * Excel导入工作单（样单）
	 * @param file
	 * @return 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> importListForExcel(File file) throws FileNotFoundException, IOException {
		Workbook wb = ReadExcel.createWb(file);
		Sheet sheet = null;
		StringBuffer sbsql1 = new StringBuffer();
		StringBuffer sbsql2 = new StringBuffer();
		StringBuffer sbsql3 = new StringBuffer();
		StringBuffer sbsql4 = new StringBuffer();
		StringBuffer sbsql5 = new StringBuffer();
		StringBuffer sbsql6 = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		//for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				//sheet = wb.getSheetAt(i);
		//}
		sheet = wb.getSheetAt(0); //只取第一个sheet
		if(sheet != null){
			int rowTotal = sheet.getPhysicalNumberOfRows();
			//System.out.println(sheet.getSheetName() + "一共有" + rowTotal + "行数据");
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				////System.out.println("开始读取第"+r+"行:");
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					////System.out.print("第"+r+"行第"+c+"个:");
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==4&&c==0){//发货人
						map.put("cnortitle",getValueFromCell(cell).toString());
					}else if(r==10&&c==0){//收货人
						map.put("cneetitle",getValueFromCell(cell).toString());
					}else if(r==15&&c==0){//通知人
						map.put("notifytitle",getValueFromCell(cell).toString());
					}else if(r==21&&c==0){//船名
						map.put("vessel",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==21&&c==2){//航次
						map.put("voyage",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==21&&c==4){//装货港
						map.put("pol",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==24&&c==0){//卸货港
						map.put("pdd",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==24&&c==4){//目的地
						map.put("destination",replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c).toString());
					}else if(r==27&&c==0){//标记与号码
						map.put("cntinfos",getValueFromCell(cell).toString());
					}else if(r==27&&c==2){//总件数
						map.put("piece",getValueFromCell(cell).toString());
					}else if(r==27&&c==3){//货物描述
						map.put("goodsinfo",getValueFromCell(cell).toString());
					}else if(r==27&&c==5){//总重
						map.put("grswgt",getValueFromCell(cell).toString());
					}else if(r==27&&c==6){//总体积
						map.put("cbm",getValueFromCell(cell).toString());
					}else if(r>36){
						try {
							for(int i = 37;i < rowTotal ; i++){
								if(r==i&&c==0){//柜号
									sbsql1.append(getValueFromCell(cell).toString()+",");
								}else if(r==i&&c==1){//封条号
									sbsql2.append(getValueFromCell(cell).toString()+",");
								}else if(r==i&&c==4){//件数
									sbsql3.append(getValueFromCell(cell).toString()+",");
								}else if(r==i&&c==5){//毛重
									sbsql5.append(getValueFromCell(cell).toString()+",");
								}else if(r==i&&c==6){//体积
									sbsql6.append(getValueFromCell(cell).toString()+",");
								}else if(r==i&&c==7){//VGM
									sbsql4.append(getValueFromCell(cell).toString()+",");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					String sqlBody1 = sbsql1.toString();
					sqlBody1 = sqlBody1.length() < 1 ? "" : sqlBody1.substring(0, sqlBody1.length() - 1);
					String sqlBody2 = sbsql2.toString();
					sqlBody2 = sqlBody2.length() < 1 ? "" : sqlBody2.substring(0, sqlBody2.length() - 1);
					String sqlBody3 = sbsql3.toString();
					sqlBody3 = sqlBody3.length() < 1 ? "" : sqlBody3.substring(0, sqlBody3.length() - 1);
					String sqlBody4 = sbsql4.toString();
					sqlBody4 = sqlBody4.length() < 1 ? "" : sqlBody4.substring(0, sqlBody4.length() - 1);
					String sqlBody5 = sbsql5.toString();
					sqlBody5 = sqlBody5.length() < 1 ? "" : sqlBody5.substring(0, sqlBody5.length() - 1);
					String sqlBody6 = sbsql6.toString();
					sqlBody6 = sqlBody6.length() < 1 ? "" : sqlBody6.substring(0, sqlBody6.length() - 1);
					map.put("cntno",sqlBody1);
					map.put("sealno",sqlBody2);
					map.put("piecee",sqlBody3);
					map.put("vgm",sqlBody4);
					map.put("gross",sqlBody5);
					map.put("measurement",sqlBody6);
					
				}
			}
		}
		//System.out.println("读取完毕,共读取"+map.size()+"个数据.");
		//System.out.println("map---->"+map);
		return map;
	}
	
	/**
	 *  导出样单
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean exportJobsListForExcel(File fromFile,File toFile,Map map) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==4&&c==0){//发货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							editCell.setCellValue(map.get("cnortitle").toString());
						}
					}else if(r==10&&c==0){//收货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}else if(r==15&&c==0){//通知人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}else if(r==10&&c==4){//提单要求
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("claim_bill") && map.get("claim_bill") != null){
							editCell.setCellValue(map.get("claim_bill").toString());
						}
					}else if(r==21&&c==0){//船名
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("vessel") && map.get("vessel") != null){
							editCell.setCellValue(map.get("vessel").toString());
						}
					}else if(r==21&&c==2){//航次
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("voyage") && map.get("voyage") != null){
							editCell.setCellValue(map.get("voyage").toString());
						}
					}else if(r==21&&c==4){//装货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==24&&c==0){//卸货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pdd") && map.get("pdd") != null){
							editCell.setCellValue(map.get("pdd").toString());
						}
					}else if(r==24&&c==4){//目的地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("destination") && map.get("destination") != null){
							editCell.setCellValue(map.get("destination").toString());
						}
					}else if(r==27&&c==0){//标记与号码
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("marksno") && map.get("marksno") != null){
							editCell.setCellValue(map.get("marksno").toString());
						}
					}else if(r==27&&c==2){//总件数
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("piece") && map.get("piece") != null){
							editCell.setCellValue(map.get("piece").toString());
						}
					}else if(r==27&&c==3){//货物描述
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==27&&c==5){//总重
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("grswgt") && map.get("grswgt") != null){
							editCell.setCellValue(map.get("grswgt").toString());
						}
					}else if(r==27&&c==6){//总体积
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cbm") && map.get("cbm") != null){
							editCell.setCellValue(map.get("cbm").toString());
						}
					}else if(r > 36){
						String[] strArray1 = null;
						if(map.get("cntno") != null){
							String str1 = map.get("cntno").toString();
							strArray1 = str1.split(",");
						}
						String[] strArray2 = null;
						if(map.get("sealno") != null){
							String str2 = map.get("sealno").toString();
							strArray2 = str2.split(",");
						}
						String[] strArray3 = null;
						if(map.get("piecee") != null){
							String str3 = map.get("piecee").toString();
							strArray3 = str3.split(",");
						}
						String[] strArray4 = null;
						if(map.get("grswgtc") != null){
							String str4 = map.get("grswgtc").toString();
							strArray4 = str4.split(",");
						}
						String[] strArray5 = null;
						if(map.get("cbmc") != null){
							String str5 = map.get("cbmc").toString();
							strArray5 = str5.split(",");
						}
						String[] strArray6 = null;
						if(map.get("vgm") != null){
							String str6 = map.get("vgm").toString();
							strArray6 = str6.split(",");
						}
						int j = strArray1.length;
						for(int i = 37;i < 37+j ; i++){
							int k = i-37;
							//System.out.println("K--->"+k);
							if(r==i&&c==0){//柜号
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("cntno") && map.get("cntno") != null){
									editCell.setCellValue(strArray1[k]);
								}
							}else if(r==i&&c==1){//封号
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("sealno") && map.get("sealno") != null){
									editCell.setCellValue(strArray2[k]);
								}
							}else if(r==i&&c==4){//件数
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("piecee") && map.get("piecee") != null){
									editCell.setCellValue(strArray3[k]);
								}
							}else if(r==i&&c==5){//毛重
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("grswgtc") && map.get("grswgtc") != null){
									editCell.setCellValue(strArray4[k]);
								}
							}else if(r==i&&c==6){//体积
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("cbmc") && map.get("cbmc") != null){
									editCell.setCellValue(strArray5[k]);
								}
							}else if(r==i&&c==7){//VGM
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("vgm") && map.get("vgm") != null){
									editCell.setCellValue(strArray6[k]);
								}
							}
							
						}
					
					}
				}
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
	 *  导出宁波泽世托书
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean exportJobsForExcel2(File fromFile,File toFile,Map map) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==1&&c==0){//发货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							editCell.setCellValue(map.get("cnortitle").toString());
						}
					}else if(r==3&&c==0){//收货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}else if(r==5&&c==0){//通知人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}else if(r==5&&c==4){//订舱备注
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("remark_booking") && map.get("remark_booking") != null){
							editCell.setCellValue(map.get("remark_booking").toString());
						}
					}else if(r==7&&c==0){//前程运输
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pretrans") && map.get("pretrans") != null){
							editCell.setCellValue(map.get("pretrans").toString());
						}
					}else if(r==7&&c==2){//收货地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("poa") && map.get("poa") != null){
							editCell.setCellValue(map.get("poa").toString());
						}
					}else if(r==7&&c==4){//工作单号
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("nos") && map.get("nos") != null){
							editCell.setCellValue(map.get("nos").toString());
						}
					}else if(r==9&&c==0){//船名
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("vessel") && map.get("vessel") != null){
							editCell.setCellValue(map.get("vessel").toString());
						}
					}else if(r==9&&c==2){//航次
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("voyage") && map.get("voyage") != null){
							editCell.setCellValue(map.get("voyage").toString());
						}
					}else if(r==9&&c==4){//ETD
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("etd") && map.get("etd") != null){
							editCell.setCellValue(map.get("etd").toString());
						}
					}else if(r==11&&c==0){//装货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==11&&c==2){//卸货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pdd") && map.get("pdd") != null){
							editCell.setCellValue(map.get("pdd").toString());
						}
					}else if(r==11&&c==4){//目的港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pod") && map.get("pod") != null){
							editCell.setCellValue(map.get("pod").toString());
						}
					}else if(r==11&&c==7){//目的地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("destination") && map.get("destination") != null){
							editCell.setCellValue(map.get("destination").toString());
						}
					}else if(r==14&&c==0){//标记与号码
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("marksno") && map.get("marksno") != null){
							editCell.setCellValue(map.get("marksno").toString());
						}
					}else if(r==14&&c==3){//货物描述
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==14&&c==8){//毛重
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("grswgt") && map.get("grswgt") != null){
							editCell.setCellValue(map.get("grswgt").toString());
						}
					}else if(r==14&&c==9){//体积
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cbm") && map.get("cbm") != null){
							editCell.setCellValue(map.get("cbm").toString());
						}
					}else if(r==28&&c==0){//目的港代理
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentitle") && map.get("agentitle") != null){
							editCell.setCellValue(map.get("agentitle").toString());
						}
					}else if(r==21&&c==0){//托运条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("carryitem") && map.get("carryitem") != null){
							editCell.setCellValue(map.get("carryitem").toString());
						}
					}else if(r==23&&c==0){//运费条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("freightitem") && map.get("freightitem") != null){
							editCell.setCellValue(map.get("freightitem").toString());
						}
					}else if(r==25&&c==0){//付款条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("paymentitem") && map.get("paymentitem") != null){
							editCell.setCellValue(map.get("paymentitem").toString());
						}
					}else if(r==1&&c==4){//抬头
						if(map.containsKey("corporation") && !StrUtils.isNull(StrUtils.getMapVal(map, "corporation"))){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)11);
							ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("corporation").toString());
						}
					}
				}
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

	public static boolean exportJobsForExcel3(File fromFile,File toFile,Map map) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==1&&c==0){//发货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							editCell.setCellValue(map.get("cnortitle").toString());
						}
					}else if(r==3&&c==0){//收货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}else if(r==5&&c==0){//通知人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}else if(r==5&&c==4){//订舱备注
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("remark_booking") && map.get("remark_booking") != null){
							editCell.setCellValue(map.get("remark_booking").toString());
						}
					}else if(r==7&&c==0){//前程运输
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pretrans") && map.get("pretrans") != null){
							editCell.setCellValue(map.get("pretrans").toString());
						}
					}else if(r==7&&c==2){//收货地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("poa") && map.get("poa") != null){
							editCell.setCellValue(map.get("poa").toString());
						}
					}else if(r==7&&c==4){//工作单号
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("nos") && map.get("nos") != null){
							editCell.setCellValue(map.get("nos").toString());
						}
					}else if(r==9&&c==0){//船名
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("vessel") && map.get("vessel") != null){
							editCell.setCellValue(map.get("vessel").toString());
						}
					}else if(r==9&&c==2){//航次
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("voyage") && map.get("voyage") != null){
							editCell.setCellValue(map.get("voyage").toString());
						}
					}else if(r==9&&c==4){//ETD
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("etd") && map.get("etd") != null){
							editCell.setCellValue(map.get("etd").toString());
						}
					}else if(r==11&&c==0){//装货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==11&&c==2){//卸货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pdd") && map.get("pdd") != null){
							editCell.setCellValue(map.get("pdd").toString());
						}
					}else if(r==11&&c==4){//目的港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pod") && map.get("pod") != null){
							editCell.setCellValue(map.get("pod").toString());
						}
					}else if(r==11&&c==7){//目的地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("destination") && map.get("destination") != null){
							editCell.setCellValue(map.get("destination").toString());
						}
					}else if(r==14&&c==0){//标记与号码
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("marksno") && map.get("marksno") != null){
							editCell.setCellValue(map.get("marksno").toString());
						}
					}else if(r==14&&c==3){//货物描述
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==14&&c==8){//毛重
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("grswgt") && map.get("grswgt") != null){
							editCell.setCellValue(map.get("grswgt").toString());
						}
					}else if(r==14&&c==9){//体积
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cbm") && map.get("cbm") != null){
							editCell.setCellValue(map.get("cbm").toString());
						}
					}else if(r==28&&c==0){//目的港代理
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentitle") && map.get("agentitle") != null){
							editCell.setCellValue(map.get("agentitle").toString());
						}
					}else if(r==21&&c==0){//托运条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("carryitem") && map.get("carryitem") != null){
							editCell.setCellValue(map.get("carryitem").toString());
						}
					}else if(r==23&&c==0){//运费条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("freightitem") && map.get("freightitem") != null){
							editCell.setCellValue(map.get("freightitem").toString());
						}
					}else if(r==25&&c==0){//付款条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("paymentitem") && map.get("paymentitem") != null){
							editCell.setCellValue(map.get("paymentitem").toString());
						}
					}else if(r==1&&c==4){//抬头
						// if(map.containsKey("corporation") && !StrUtils.isNull(StrUtils.getMapVal(map, "corporation"))){
						// 	Cell editCell = row.createCell(c);
						// 	Font ztFont = wb.createFont();
						// 	ztFont.setFontName("微软雅黑");
						// 	ztFont.setFontHeightInPoints((short)11);
						// 	ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
						// 	CellStyle cellStyle2 = wb.createCellStyle();
						// 	cellStyle2.setFont(ztFont);
						// 	cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
						// 	cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
						// 	cellStyle2.setWrapText(true);
						// 	editCell.setCellStyle(cellStyle2);
						// 	// editCell.setCellValue(map.get("corporation").toString());
						// }
					}
				}
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
	 *  导出托书
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean exportJobsForExcel(File fromFile,File toFile,Map map) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==1&&c==0){//发货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							editCell.setCellValue(map.get("cnortitle").toString());
						}
					}else if(r==3&&c==0){//收货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}else if(r==5&&c==0){//通知人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}else if(r==7&&c==0){//前程运输
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pretrans") && map.get("pretrans") != null){
							editCell.setCellValue(map.get("pretrans").toString());
						}
					}else if(r==7&&c==2){//收货地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("poa") && map.get("poa") != null){
							editCell.setCellValue(map.get("poa").toString());
						}
					}else if(r==9&&c==0){//船名
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("vessel") && map.get("vessel") != null){
							editCell.setCellValue(map.get("vessel").toString());
						}
					}else if(r==9&&c==2){//航次
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("voyage") && map.get("voyage") != null){
							editCell.setCellValue(map.get("voyage").toString());
						}
					}else if(r==11&&c==0){//装货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==11&&c==2){//卸货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pdd") && map.get("pdd") != null){
							editCell.setCellValue(map.get("pdd").toString());
						}
					}else if(r==11&&c==4){//目的港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pod") && map.get("pod") != null){
							editCell.setCellValue(map.get("pod").toString());
						}
					}else if(r==11&&c==7){//目的地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("destination") && map.get("destination") != null){
							editCell.setCellValue(map.get("destination").toString());
						}
					}else if(r==14&&c==0){//标记与号码
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("marksno") && map.get("marksno") != null){
							editCell.setCellValue(map.get("marksno").toString());
						}
					}else if(r==14&&c==3){//货物描述
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==14&&c==8){//毛重
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("grswgt") && map.get("grswgt") != null){
							editCell.setCellValue(map.get("grswgt").toString());
						}
					}else if(r==14&&c==9){//体积
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cbm") && map.get("cbm") != null){
							editCell.setCellValue(map.get("cbm").toString());
						}
					}else if(r==28&&c==0){//目的港代理
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentitle") && map.get("agentitle") != null){
							editCell.setCellValue(map.get("agentitle").toString());
						}
					}else if(r==16&&c==0){//订舱备注
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("remark_booking") && map.get("remark_booking") != null){
							editCell.setCellValue(map.get("remark_booking").toString());
						}
					}else if(r==21&&c==0){//托运条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("carryitem") && map.get("carryitem") != null){
							editCell.setCellValue(map.get("carryitem").toString());
						}
					}else if(r==23&&c==0){//运费条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("freightitem") && map.get("freightitem") != null){
							editCell.setCellValue(map.get("freightitem").toString());
						}
					}else if(r==25&&c==0){//付款条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("paymentitem") && map.get("paymentitem") != null){
							editCell.setCellValue(map.get("paymentitem").toString());
						}
					}else if(r==1&&c==4){//抬头
						if(map.containsKey("corporation") && !StrUtils.isNull(StrUtils.getMapVal(map, "corporation"))){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)11);
							ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("corporation").toString());
						}
					}
				}
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
	 *  导出托书
	 * @param fromFile模版文件
	 * @param toFile下载的临时文件
	 * @param map等待填入的数据
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean exportJobsForExcelAir(File fromFile,File toFile,Map map) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==1&&c==0){//发货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							editCell.setCellValue(map.get("cnortitle").toString());
						}
					}else if(r==3&&c==0){//收货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}else if(r==5&&c==0){//通知人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}else if(r==7&&c==0){//前程运输
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pretrans") && map.get("pretrans") != null){
							editCell.setCellValue(map.get("pretrans").toString());
						}
					}else if(r==7&&c==2){//收货地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("poa") && map.get("poa") != null){
							editCell.setCellValue(map.get("poa").toString());
						}
					}else if(r==9&&c==0){//船名
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("vessel") && map.get("vessel") != null){
							editCell.setCellValue(map.get("vessel").toString());
						}
					}else if(r==9&&c==2){//航次
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("voyage") && map.get("voyage") != null){
							editCell.setCellValue(map.get("voyage").toString());
						}
					}else if(r==11&&c==0){//装货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==11&&c==2){//卸货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pdd") && map.get("pdd") != null){
							editCell.setCellValue(map.get("pdd").toString());
						}
					}else if(r==11&&c==4){//目的港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pod") && map.get("pod") != null){
							editCell.setCellValue(map.get("pod").toString());
						}
					}else if(r==11&&c==7){//目的地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("destination") && map.get("destination") != null){
							editCell.setCellValue(map.get("destination").toString());
						}
					}else if(r==14&&c==0){//标记与号码
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("marksno") && map.get("marksno") != null){
							editCell.setCellValue(map.get("marksno").toString());
						}
					}else if(r==14&&c==3){//货物描述
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==14&&c==8){//毛重
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("grswgt") && map.get("grswgt") != null){
							editCell.setCellValue(map.get("grswgt").toString());
						}
					}else if(r==14&&c==9){//体积
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cbm") && map.get("cbm") != null){
							editCell.setCellValue(map.get("cbm").toString());
						}
					}else if(r==28&&c==0){//目的港代理
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentdescode") && map.get("agentdescode") != null){
							editCell.setCellValue(map.get("agentdescode").toString());
						}
					}else if(r==16&&c==0){//订舱备注
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("remark_booking") && map.get("remark_booking") != null){
							editCell.setCellValue(map.get("remark_booking").toString());
						}
					}else if(r==21&&c==0){//托运条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("carryitem") && map.get("carryitem") != null){
							editCell.setCellValue(map.get("carryitem").toString());
						}
					}else if(r==23&&c==0){//运费条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("freightitem") && map.get("freightitem") != null){
							editCell.setCellValue(map.get("freightitem").toString());
						}
					}else if(r==25&&c==0){//付款条款
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("paymentitem") && map.get("paymentitem") != null){
							editCell.setCellValue(map.get("paymentitem").toString());
						}
					}else if(r==1&&c==4){//抬头
						if(map.containsKey("corporation") && !StrUtils.isNull(StrUtils.getMapVal(map, "corporation"))){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)11);
							ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("corporation").toString());
						}
					}
				}
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

	public static boolean exportAirJobsForExcel(File fromFile,File toFile,Map map) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==11&&c==2){
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							//ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)6);
							//ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}if(r==4&&c==2){
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							//ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)6);
							//ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("cnortitle").toString());
							
						}
						
					}/*else if(r==10&&c==17){//Consignee's Account Number
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneename") && map.get("cneename") != null){
							editCell.setCellValue(map.get("cneename").toString());
						}
					}else if(r==11&&c==2){//Consignee's Name and Address
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneeadress") && map.get("cneeadress") != null){
							editCell.setCellValue(map.get("cneeadress").toString());
						}
					}else if(r==22&&c==2){//Agent's IATA Code
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("aircode") && map.get("aircode") != null){
							editCell.setCellValue(map.get("aircode").toString());
						}
					}*//*else if(r==17&&c==2){//Issuing Carrier's Agent Name and City
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentdesabbr") && map.get("agentdesabbr") != null){
							editCell.setCellValue(map.get("agentdesabbr").toString());
						}
					}*/else if(r==24&&c==2){//Airport of Departure (Addr. of First Carrier) and Requested Routing
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==18&&c==29){//To
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentdesabbr") && map.get("agentdesabbr") != null){
							editCell.setCellValue(map.get("agentdesabbr").toString());
						}
					}else if(r==0&&c==2){//To
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("mawbno1") && map.get("mawbno1") != null){
							editCell.setCellValue(map.get("mawbno1").toString());
						}
					}else if(r==27&&c==2){//To
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("podcode") && map.get("podcode") != null){
							editCell.setCellValue(map.get("podcode").toString());
						}
					}else if(r==27&&c==5){//by1
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("flight") && map.get("flight") != null){
							editCell.setCellValue(map.get("flight").toString());
						}
					}else if(r==27&&c==38){//by1
						
						if(map.containsKey("ppccpaytype") && map.get("ppccpaytype") != null){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							//ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)6);
							//ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("ppccpaytype").toString());
						}
					}else if(r==27&&c==32){//by1
						
						if(map.containsKey("ppccgoods") && map.get("ppccgoods") != null){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							//ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)6);
							//ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("ppccgoods").toString());
						}
					}else if(r==27&&c==35){//by1
						
						if(map.containsKey("ppccothfee") && map.get("ppccothfee") != null){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							//ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)6);
							//ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("ppccothfee").toString());
						}
					}/*else if(r==27&&c==19){//to
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("to2") && map.get("to2") != null){
							editCell.setCellValue(map.get("to2").toString());
						}
					}else if(r==27&&c==20){//by
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("by2") && map.get("by2") != null){
							editCell.setCellValue(map.get("by2").toString());
						}
					}else if(r==27&&c==23){//to
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("to3") && map.get("to3") != null){
							editCell.setCellValue(map.get("to3").toString());
						}
					}else if(r==27&&c==25){//by
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("by3") && map.get("by3") != null){
							editCell.setCellValue(map.get("by3").toString());
						}
					}*/else if(r==29&&c==2){//Airport of Destination
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pod") && map.get("pod") != null){
							editCell.setCellValue(map.get("pod").toString());
						}
					}else if(r==29&&c==17){//Requested Flight/Date
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("flightno1") && map.get("flightno1") != null){
							editCell.setCellValue(map.get("flightno1").toString());
						}
					}else if(r==29&&c==21){//Requested Flight/Date
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("flightdate1") && map.get("flightdate1") != null){
							editCell.setCellValue(map.get("flightdate1").toString());
						}
					}else if(r==37&&c==2){//No. Of Pieces RCP
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("piece") && map.get("piece") != null){
							editCell.setCellValue(map.get("piece").toString());
						}
					}else if(r==49&&c==2){//No. Of Pieces RCP
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("piece") && map.get("piece") != null){
							editCell.setCellValue(map.get("piece").toString());
						}
					}else if(r==37&&c==5){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("weight") && map.get("weight") != null){
							editCell.setCellValue(map.get("weight").toString());
						}
					}/*else if(r==37&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("markno") && map.get("markno") != null){
							editCell.setCellValue(map.get("markno").toString());
						}
					}*/else if(r==37&&c==19){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("chargeweight") && map.get("chargeweight") != null){
							editCell.setCellValue(map.get("chargeweight").toString());
						}
					}/*else if(r==69&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("podfee") && map.get("podfee") != null){
							editCell.setCellValue(map.get("podfee").toString());
						}
					}*/else if(r==64&&c==2){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("feesumpp") && map.get("feesumpp") != null){
							editCell.setCellValue(map.get("feesumpp").toString());
						}
					}else if(r==64&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("feesumcc") && map.get("feesumcc") != null){
							editCell.setCellValue(map.get("feesumcc").toString());
						}
					}/*else if(r==56&&c==2){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("taxfeepp") && map.get("taxfeepp") != null){
							editCell.setCellValue(map.get("taxfeepp").toString());
						}
					}else if(r==56&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("taxfeecc") && map.get("taxfeecc") != null){
							editCell.setCellValue(map.get("taxfeecc").toString());
						}
					}else if(r==58&&c==2){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentothfeepp") && map.get("agentothfeepp") != null){
							editCell.setCellValue(map.get("agentothfeepp").toString());
						}
					}else if(r==58&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("agentothfeecc") && map.get("agentothfeecc") != null){
							editCell.setCellValue(map.get("agentothfeecc").toString());
						}
					}else if(r==60&&c==2){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("carrothfeepp") && map.get("carrothfeepp") != null){
							editCell.setCellValue(map.get("carrothfeepp").toString());
						}
					}else if(r==60&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("carrothfeecc") && map.get("carrothfeecc") != null){
							editCell.setCellValue(map.get("carrothfeecc").toString());
						}
					}else if(r==69&&c==22){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("sumto") && map.get("sumto") != null){
							editCell.setCellValue(map.get("sumto").toString());
						}
					}else if(r==54&&c==2){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("valueaddpp") && map.get("valueaddpp") != null){
							editCell.setCellValue(map.get("valueaddpp").toString());
						}
					}else if(r==54&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("valueaddcc") && map.get("valueaddcc") != null){
							editCell.setCellValue(map.get("valueaddcc").toString());
						}
					}else if(r==24&&c==29){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("refno") && map.get("refno") != null){
							editCell.setCellValue(map.get("refno").toString());
						}
					}*/else if(r==0&&c==40){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("hawbno") && map.get("hawbno") != null){
							editCell.setCellValue("HAWB No："+map.get("hawbno").toString());
						}
					}else if(r==29&&c==29){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("insuranceamt") && map.get("insuranceamt") != null){
							editCell.setCellValue(map.get("insuranceamt").toString());
						}
					}else if(r==27&&c==39){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
							editCell.setCellValue("NVD");
					}else if(r==27&&c==43){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
							editCell.setCellValue("NCV");
					}else if(r==24&&c==2){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}/*else if(r==66&&c==13){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("podcyid") && map.get("podcyid") != null){
							editCell.setCellValue(map.get("podcyid").toString());
						}
					}*/else if(r==37&&c==41){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==0&&c==5){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("polcode") && map.get("polcode") != null){
							editCell.setCellValue(map.get("polcode").toString());
						}
					}else if(r==0&&c==7){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("mawbno2") && map.get("mawbno2") != null){
							editCell.setCellValue(map.get("mawbno2").toString());
						}
					}/*else if(r==27&&c==29){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("polcyid") && map.get("polcyid") != null){
							editCell.setCellValue(map.get("polcyid").toString());
						}
					}*/else if(r==65&&c==22){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("jobdate") && map.get("jobdate") != null){
							editCell.setCellValue(map.get("jobdate").toString());
						}
					}else if(r==65&&c==41){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("assigner") && map.get("assigner") != null){
							editCell.setCellValue(map.get("assigner").toString());
						}
					}else if(r==59&&c==22){
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("corpnamee") && map.get("corpnamee") != null){
							editCell.setCellValue(map.get("corpnamee").toString());
						}
					}else if(r==17&&c==2){
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							//ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)6);
							//ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}
					/*else if(r==1&&c==4){
						if(map.containsKey("corporation") && !StrUtils.isNull(StrUtils.getMapVal(map, "corporation"))){
							Cell editCell = row.createCell(c);
							Font ztFont = wb.createFont();
							ztFont.setFontName("微软雅黑");
							ztFont.setFontHeightInPoints((short)11);
							ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
							CellStyle cellStyle2 = wb.createCellStyle();
							cellStyle2.setFont(ztFont);
							cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
							cellStyle2.setWrapText(true);
							editCell.setCellStyle(cellStyle2);
							editCell.setCellValue(map.get("corporation").toString());
						}
					}*/
				}
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
	
	
	public static Workbook createWb(File file) throws FileNotFoundException,
			IOException {
		if (file.getName().trim().toLowerCase().endsWith("xls")) {
			return new HSSFWorkbook(new FileInputStream(file));
		} else if (file.getName().trim().toLowerCase().endsWith("xlsx")) {
			return new XSSFWorkbook(new FileInputStream(file));
		}else if (file.getName().trim().toLowerCase().endsWith("pdf")) {
			return new XSSFWorkbook(new FileInputStream(file));
		} else {
			// throw new IllegalArgumentException("仅支持xls,xlsx文件");
			return new HSSFWorkbook(new FileInputStream(file));
		}
	}

	/**
	 * 创建工作对象
	 * 
	 * @param filePath
	 *            (Url路径)
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Workbook createWb(String filePath)
			throws FileNotFoundException, IOException {
		if (filePath.trim().toLowerCase().endsWith("xls")) {
			return new HSSFWorkbook(new FileInputStream(filePath));
		} else if (filePath.trim().toLowerCase().endsWith("xlsx")) {
			return new XSSFWorkbook(new FileInputStream(filePath));
		} else {
			throw new IllegalArgumentException("仅支持xls,xlsx文件");
		}
	}

	public Sheet getSheet(Workbook wb, String sheetName) {
		return wb.getSheet(sheetName);
	}

	public Sheet getSheet(Workbook wb, int index) {
		return wb.getSheetAt(index);
	}

	/**
	 * 获取当前Sheet全部数据
	 * 
	 * @param sheet
	 * @return
	 */
	public static List<Object[]> listFromSheet(Sheet sheet) {
		int rowTotal = sheet.getPhysicalNumberOfRows();
		//AppUtils.debug(sheet.getSheetName() + "一共有" + rowTotal + "行数据");
		List<Object[]> list = new ArrayList<Object[]>();
		for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null || row.getLastCellNum() < 1)
				continue;
			Object[] cells = new Object[row.getLastCellNum()];
			for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				if (cell == null) {
					cells[c] = "";
					continue;
				}
				cells[c] = replaceBlank(getValueFromCell(cell),row.getLastCellNum()-c);
			}
			list.add(cells);
		}
		return list;
	}
	/**
	 * 去除字符串中的table、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str,int c) {
        String dest = "";
        if (str!=null) {
        	if(1==c) {
	            Pattern p = Pattern.compile("\t|\r");
	            Matcher m = p.matcher(str);
	            dest = m.replaceAll("");
        	}else {
        		Pattern p = Pattern.compile("\t|\r|\n");
	            Matcher m = p.matcher(str);
	            dest = m.replaceAll("");
        	}
            
        }
        return dest;
    }
	/**
	 * 获取Cell中数据
	 * 
	 * @param cell
	 * @return
	 */
	public static String getValueFromCell(Cell cell) {
		String value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:// 数字
			if (HSSFDateUtil.isCellDateFormatted(cell)) { // 如果是日期类型
				value = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
			} else
				value = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:// 字符串
			value = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:// 空白
			value = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN://boolean
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA://公式
			double numericValue = cell.getNumericCellValue();
			if (HSSFDateUtil.isValidExcelDate(numericValue)) { // 如果是日期类型
				value = new SimpleDateFormat().format(cell.getDateCellValue());
			} else
				value = String.valueOf(numericValue);
			break;
		case Cell.CELL_TYPE_ERROR://错误
			value = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			value = "";
			break;
		}
		return value;
	}
	
	//委托下面的邮件中勾选样单，直接将导出样单添加到邮件的附件中
	public static boolean importJobsListForEmail(File fromFile,File toFile,Map map,String filename) throws FileNotFoundException, IOException {
		if(map==null || map.size() == 0||!fromFile.exists()){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		Sheet sheet = wb.getSheetAt(0);
		if(sheet != null){
			for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (row == null || row.getLastCellNum() < 1)
					continue;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell == null) {
						continue;
					}
					if(r==4&&c==0){//发货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cnortitle") && map.get("cnortitle") != null){
							editCell.setCellValue(map.get("cnortitle").toString());
						}
					}else if(r==10&&c==0){//收货人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cneetitle") && map.get("cneetitle") != null){
							editCell.setCellValue(map.get("cneetitle").toString());
						}
					}else if(r==15&&c==0){//通知人
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("notifytitle") && map.get("notifytitle") != null){
							editCell.setCellValue(map.get("notifytitle").toString());
						}
					}else if(r==10&&c==4){//提单要求
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("claim_bill") && map.get("claim_bill") != null){
							editCell.setCellValue(map.get("claim_bill").toString());
						}
					}else if(r==21&&c==0){//船名
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("vessel") && map.get("vessel") != null){
							editCell.setCellValue(map.get("vessel").toString());
						}
					}else if(r==21&&c==2){//航次
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("voyage") && map.get("voyage") != null){
							editCell.setCellValue(map.get("voyage").toString());
						}
					}else if(r==21&&c==4){//装货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pol") && map.get("pol") != null){
							editCell.setCellValue(map.get("pol").toString());
						}
					}else if(r==24&&c==0){//卸货港
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("pdd") && map.get("pdd") != null){
							editCell.setCellValue(map.get("pdd").toString());
						}
					}else if(r==24&&c==4){//目的地
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("destination") && map.get("destination") != null){
							editCell.setCellValue(map.get("destination").toString());
						}
					}else if(r==27&&c==0){//标记与号码
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("marksno") && map.get("marksno") != null){
							editCell.setCellValue(map.get("marksno").toString());
						}
					}else if(r==27&&c==2){//总件数
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("piece") && map.get("piece") != null){
							editCell.setCellValue(map.get("piece").toString());
						}
					}else if(r==27&&c==3){//货物描述
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("goodsdesc") && map.get("goodsdesc") != null){
							editCell.setCellValue(map.get("goodsdesc").toString());
						}
					}else if(r==27&&c==5){//总重
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("grswgt") && map.get("grswgt") != null){
							editCell.setCellValue(map.get("grswgt").toString());
						}
					}else if(r==27&&c==6){//总体积
						Cell editCell = row.createCell(c);
						editCell.setCellStyle(cellStyle);
						if(map.containsKey("cbm") && map.get("cbm") != null){
							editCell.setCellValue(map.get("cbm").toString());
						}
					}else if(r > 36){
						String[] strArray1 = null;
						if(map.get("cntno") != null){
							String str1 = map.get("cntno").toString();
							strArray1 = str1.split(",");
						}
						String[] strArray2 = null;
						if(map.get("sealno") != null){
							String str2 = map.get("sealno").toString();
							strArray2 = str2.split(",");
						}
						String[] strArray3 = null;
						if(map.get("piecee") != null){
							String str3 = map.get("piecee").toString();
							strArray3 = str3.split(",");
						}
						String[] strArray4 = null;
						if(map.get("grswgtc") != null){
							String str4 = map.get("grswgtc").toString();
							strArray4 = str4.split(",");
						}
						String[] strArray5 = null;
						if(map.get("cbmc") != null){
							String str5 = map.get("cbmc").toString();
							strArray5 = str5.split(",");
						}
						String[] strArray6 = null;
						if(map.get("vgm") != null){
							String str6 = map.get("vgm").toString();
							strArray6 = str6.split(",");
						}
						int j = strArray1.length;
						for(int i = 37;i < 37+j ; i++){
							int k = i-37;
							//System.out.println("K--->"+k);
							if(r==i&&c==0){//柜号
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("cntno") && map.get("cntno") != null){
									editCell.setCellValue(strArray1[k]);
								}
							}else if(r==i&&c==1){//封号
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("sealno") && map.get("sealno") != null){
									editCell.setCellValue(strArray2[k]);
								}
							}else if(r==i&&c==4){//件数
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("piecee") && map.get("piecee") != null){
									editCell.setCellValue(strArray3[k]);
								}
							}else if(r==i&&c==5){//毛重
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("grswgtc") && map.get("grswgtc") != null){
									editCell.setCellValue(strArray4[k]);
								}
							}else if(r==i&&c==6){//体积
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("cbmc") && map.get("cbmc") != null){
									editCell.setCellValue(strArray5[k]);
								}
							}else if(r==i&&c==7){//VGM
								Cell editCell = row.createCell(c);
								editCell.setCellStyle(cellStyle);
								if(map.containsKey("vgm") && map.get("vgm") != null){
									editCell.setCellValue(strArray6[k]);
								}
							}
							
						}
					
					}
				}
			}
		}else{
			return false;
		}
		
		if(!toFile.exists()){
			toFile.createNewFile();
		}
		try {
			String	attachPath = AppUtils.getAttachFilePath();
			FileOutputStream fos = new FileOutputStream(attachPath +filename);
			wb.write(fos);
			//wb.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
}
