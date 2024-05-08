package com.scp.view.sysmgr.init;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.BaseDaoImpl;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.ExcelUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.sysmgr.init.impinitBean", scope = ManagedBeanScope.REQUEST)
public class ImpInitBean extends FormView {
	
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		if(StrUtils.isNull(type)){
			MessageUtils.showMsg("Please choose type!");
			return;
		}
		
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	
	@Bind
	@SaveState
	public String type = "";

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			Vector<String> sqlBatch = new Vector<String>();
			try {
				String callFunction = "f_imp_init";
				String args = "'"+type+"'" + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				importDataText = importDataText.replaceAll("'", "''");
				List<Map> ret = ExcelUtil.convertTxt2Maps(importDataText , 0);
				
				
				for (Map map : ret) {
					Set<String> set = map.keySet();
					StringBuffer stringBuffer = new StringBuffer();
					for (String key : set) {
						stringBuffer.append(key + "=" + map.get(key));
						stringBuffer.append(";");
					}
					String sql = "SELECT " + callFunction + "('"+ stringBuffer.toString() + "', " + args + " );\n";
					sqlBatch.add(sql);
				}
				//System.out.println(sqlBatch.toString());
				//BaseDaoImpl sysUserDao = (BaseDaoImpl) ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
				//sysUserDao.executeQueryBatchByJdbc(sqlBatch);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
			for (String string : sqlBatch) {
				try {
					daoIbatisTemplate.queryWithUserDefineSql(string);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			MessageUtils.alert("OK!");
		}
	}
	
	@Action
	public void importDataBatchFromLocalFile() {
		String psqlIn = "C:\\Users\\Administrator\\Desktop\\shipper\\inport.sql";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		
		try {
			String str = "";
			String str1 = "";
			fis = new FileInputStream(psqlIn);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis,"unicode");// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			int i = 0;
			String title = "代码	简称	中文名	英文名	地址中文	地址英文	电话	传真	email	联系人	主页	备注	发票抬头	营业执照号	税务登记号	主键";
			StringBuffer sqlBuffer = new StringBuffer();
			Vector<String> sqlBatch = new Vector<String>();
			while ((str = br.readLine()) != null) {
				//str1 += str + "\n";
				if(StrUtils.isNull(str)) {
					i++;
					continue;//下一行
				}
				
				if(StrUtils.isNull(str)) {
					continue;
				}
				
				//if(str.trim().endsWith(");")) {
				str = str.replaceAll("'", "''");
					sqlBuffer.append(str);
					if(sqlBuffer.toString() != null && !"".equals(sqlBuffer.toString()) && !".".equals(sqlBuffer.toString())){
						////System.out.println(sqlBuffer);
						
						List<Map> ret = ExcelUtil.convertTxt2Maps(title + "\n"+ sqlBuffer.toString() , 0);
						
						
						for (Map map : ret) {
							Set<String> set = map.keySet();
							StringBuffer stringBuffer = new StringBuffer();
							for (String key : set) {
								stringBuffer.append(key + "=" + map.get(key));
								stringBuffer.append(";");
							}
							String callFunction = "f_imp_init";
							String args = "'"+type+"'" + ",'"
									+ AppUtils.getUserSession().getUsercode() + "'";
							String sql = "SELECT " + callFunction + "('"+ stringBuffer.toString() + "', " + args + " );\n";
							sqlBatch.add(sql);
						}
						//System.out.println(sqlBatch.toString());
					}
					sqlBuffer = new StringBuffer();
					
					if(sqlBatch.size()>0 && sqlBatch.size() == 100){
						BaseDaoImpl sysUserDao = (BaseDaoImpl) ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
						sysUserDao.executeQueryBatchByJdbc(sqlBatch);
						sqlBatch.clear();
					}
					
					continue;
				}
				if(sqlBatch.size()>0 && sqlBatch.size() < 100){
					BaseDaoImpl sysUserDao = (BaseDaoImpl) ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
					sysUserDao.executeQueryBatchByJdbc(sqlBatch);
					sqlBatch.clear();
				}
//				sqlBuffer.append(str);
			//}
			
		} catch (FileNotFoundException e) {
			//System.out.println("找不到指定文件");
		} catch (IOException e) {
			//System.out.println("读取文件失败");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
