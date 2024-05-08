package com.scp.view.module.finance.edi;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.swing.filechooser.FileSystemView;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.MessageUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.finance.edi.ufBean", scope = ManagedBeanScope.REQUEST)
public class UFBean  extends EdiGridView{
	
	@Bind
	@SaveState
	private String startDate;

	@Bind
	@SaveState
	private String endDate;

	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		try {
			if (!isPostBack) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	@Action
	public void exporting(){
		
	}
	
	public boolean running(String jobids){
		try {
			String urlArgs2 = "";
			String sqlQry = "SELECT * FROM f_edi_vch_uf('"+urlArgs2 +"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			Object object = m.get("f_sdi_inttra");
			String str = "";
			if(object!=null){
				str = object.toString();
			}
			return writer(str);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return false;
		}
	}
	
	
	/*
	 * 写入文件
	 * */
	public  boolean writer(String str){
		Random ra =new Random();
		//获得当前系统桌面路径
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String absolutePath = desktopDir.getAbsolutePath(); 
		String randomString = getRandomString(3);
		File file = new File(absolutePath+"/edi/inttra");
		if (!file .exists()  && !file .isDirectory()) {     
			file.mkdirs();
		}
		String path=absolutePath+"/edi/vch/kis"+randomString+".txt";
		File newFile = newFile(path);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
			bw.write(str);
			bw.newLine();
			//关闭资源
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
		
	}
	
}
