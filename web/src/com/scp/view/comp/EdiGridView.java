package com.scp.view.comp;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.component.layout.impl.UIWindow;

/**
 * 导EDI
 */
public class EdiGridView extends GridView {
	
	@Bind
	public UIWindow showsetFIPWindow;
	
	protected String getQuerySql() {
		return 
		"\nSELECT * " +
		"\nFROM sys_config " +
		"\nWHERE 1=1 ";
	}
	
	/** 创建新文件 */
	public  File newFile(String path){
		//创建文件对象
		File file = new File(path);
		//判断
		if(!file.exists()){ //该文件不存在
			//是目录
			if(file.isDirectory()){
				file.mkdirs();
			}
			//是文件
			if(file.isFile()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return file;
	}
	
	//产生随机数
	public  String getRandomString(int length) { 
		StringBuilder buffer = new StringBuilder("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
	    StringBuilder sb = new StringBuilder(); 
	    Random random = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(random.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
	
	//查出流水号
	public  String getcounts(){
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT SUM(expcount) FROM edi_logs");
		String str = m.get("sum").toString();
		for(int i = 0;i<3-str.length();i++){
			str = "0" + str;
		}
		str = "_" + str;
		return str;
	}
	
	//删除指定文件夹下所有文件
	//param path 文件夹完整绝对路径
	public boolean delAllFile(String path) {
       boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
         return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
          if (path.endsWith(File.separator)) {
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          if (temp.isFile()) {
             temp.delete();
          }
          if (temp.isDirectory()) {
             delAllFile(path + "/" + tempList[i]);//删除文件夹里面的文件
             flag = true;
          }
       }
       return flag;
     }
	
	@Action
	public void setFIP(){
		showsetFIPWindow.show();
	}
}
