package com.scp.dao.ship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.scp.util.AppUtils;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		long lo = (new Date()).getTime();
//		MessageUtils.alert("导入中...");
//		try {
//			String sql = readInfo();
//			
//			//链接数据库插入数据
//			ServiceContext serviceContext = (ServiceContext)AppUtils.getBeanFromSpringIoc("serviceContext");
//			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		} catch (Exception e) {
//			System.out.println("错误："+(new Date()).getTime());
//			e.printStackTrace();
//		}finally{
//		}
//		System.out.println("共计耗时（ms）："+((new Date()).getTime()-lo));
	}
	
	public static String readInfo(){
		InputStreamReader read;
		 String info = "";
		try {
			String exportFileName = "新增新的开票信息.txt";
			// 模版所在路径
			String fromFileUrl = AppUtils.getHttpServletRequest().getSession().getServletContext().getRealPath("")
					+ File.separator
					+ "upload"
					+ File.separator
					+ "finace"
					+ File.separator + exportFileName;
			File file = new File("D:\\JavaWorkSpace-SCP\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\scp\\upload\\finace\\新增新的开票信息.txt");
			
			read = new InputStreamReader(new FileInputStream(new File(fromFileUrl)),"GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
	        String lineTxt = null;
	       
	        while((lineTxt = bufferedReader.readLine()) != null){
//	        	System.out.println(lineTxt);
	        	info = info+lineTxt;
	        }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

}
