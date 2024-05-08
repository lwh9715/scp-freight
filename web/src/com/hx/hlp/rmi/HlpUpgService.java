package com.hx.hlp.rmi;  

import java.util.List;
import java.util.Map;
  
public interface HlpUpgService { 
	
	//0.检查异常中断
	boolean checkPause(String csno ,int fixVerNo);
	
	//1.检查版本号 小版本号
	boolean checkVersion(String csno ,int fixVerNo);
	
	//2.获取sql并执行执行
	List<Map<String, String>> getUpgSql(String csno , int fixVerNo); 
	
	//2.1.sql执行反馈
	String responseUpgSql(String csno , String pkId , String response); 
	
	//3.获取zip并下载,返回url
	String getUpgZip(String csno , int fixVerNo);  
	
	//4.解压后重启
	String cliRestart(String csno , int fixVerNo);  
	
	//5. getUserAndModule
	String checkUserAndModule(String csno); 
	
	//6. execShell
	String execShell(String csno, int fixVerNo); 
    
	//6. response execShell
	String responseExecShell(String pkId, String response); 
}  