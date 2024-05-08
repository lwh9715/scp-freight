package com.scp.base;

import com.scp.util.AppUtils;

public class ApplicationConf {
	
	static{
		System.setProperty("java.rmi.server.hostname" , "hangxun.vicp.io" );
		//System.out.println("*************"+System.getProperty("java.rmi.server.hostname"));
		String os = System.getProperty("os.name");  
    	if(!os.toLowerCase().startsWith("win")){  
    	  //System.out.println(os + "");  
    	  System.setProperty("java.io.tmpdir", "/www/t7/temp/");
    	  //System.out.println("*************"+System.getProperty("java.io.tmpdir"));
    	}  
//    	System.setProperty("java.awt.headless", "false");
	}

	private boolean isDebug;
	private boolean isReadOnlyDB;
	private boolean isAutoFillLs;
	private boolean isUseDzz;
	private boolean isStartActiveMq;
	
	private boolean isSaas;
	
//	private String rptUrl;
	
	
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
		//标记是否是调试模式
		AppUtils.isDebug = isDebug;
	}

	public boolean isReadOnlyDB() {
		return isReadOnlyDB;
	}

	public void setReadOnlyDB(boolean isReadOnlyDB) {
		this.isReadOnlyDB = isReadOnlyDB;
		//标记是否是只读数据库
		AppUtils.isReadOnlyDB = isReadOnlyDB;
	}

	public boolean isAutoFillLs() {
		return isAutoFillLs;
	}

	public void setAutoFillLs(boolean isAutoFillLs) {
		this.isAutoFillLs = isAutoFillLs;
		//标记是否自动填充多语言
		AppUtils.isAutoFillLs = isAutoFillLs;
	}

//	public String getRptUrl() {
//		return rptUrl;
//	}

//	public void setRptUrl(String rptUrl) {
//		this.rptUrl = rptUrl;
//		//AppUtils.getRptUrl() = AppUtils.isDebug?"http://192.168.0.188:8888/scp":"http://120.25.241.190:8888/scp";
//		AppUtils.getRptUrl() = rptUrl;
//	}

	public boolean getIsUseDzz() {
		return isUseDzz;
	}

	public void setIsUseDzz(boolean isUseDzz) {
		this.isUseDzz = isUseDzz;
	}

	public boolean getIsStartActiveMq() {
		return isStartActiveMq;
	}

	public void setisStartActiveMq(boolean isStartActiveMq) {
		this.isStartActiveMq = isStartActiveMq;
	}

	public boolean isSaas() {
		return isSaas;
	}

	public void setSaas(boolean isSaas) {
		this.isSaas = isSaas;
	}

	
}
