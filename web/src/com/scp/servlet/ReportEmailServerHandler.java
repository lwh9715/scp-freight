package com.scp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

public class ReportEmailServerHandler {
	
	
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request,HttpServletResponse response){
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			if("geturl".equals(action)) {
				result = this.geturl(request,response);
			}
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	
	private String geturl(HttpServletRequest request,HttpServletResponse response) {
		try {
			String url = request.getParameter("url");
			String type = request.getParameter("type");
			type = StrUtils.isNull(type) ? "pdf" : type;
			if(!StrUtils.isNull(url)){
				response.sendRedirect("pages/module/other/sendemail.xhtml?url="+getFile(url.replaceAll("%26", "&"),type));
			}
			
		} catch (NoRowException e) {
			
		} catch (NullPointerException e) {
			
		} catch (Exception e) {
			
		}
		return "发送成功,请关闭本窗口";
	}
	
	
	public String getFile(String urlstr,String filetype) throws URISyntaxException, IOException{
		URL url = new URL(urlstr);
		HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(3000);//超时时间
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36");
		InputStream is =  connection.getInputStream();
		byte[] readbyte = readInputStream(is);
		String filename = String.valueOf(Calendar.getInstance().getTime().getTime())+"."+filetype;
		String filepach = System.getProperty("java.io.tmpdir") + filename;
		File tt = new File(filepach);
		if(!tt.exists()){
			tt.createNewFile();
		}else{
			tt.deleteOnExit();
			tt.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(tt);
		fos.write(readbyte);
		fos.flush();
		fos.close();
		return filename;
	}
	
	
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();
    }  
}
