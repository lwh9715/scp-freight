package com.scp.view.sysmgr.rpt;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.FacesException;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.sys.SysReportDao;
import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.FileOperationUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.db.SqlObject;

@ManagedBean(name = "pages.sysmgr.rpt.rptMgrBean", scope = ManagedBeanScope.REQUEST)
public class RptMgr extends GridFormView {
	
	@SaveState
	public SysReport data = new SysReport();
	
	@Resource
	public SysReportDao sysReportDao;
	
	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysReportMgrService.sysReportDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.sysReportMgrService.saveData(data);
	} 
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("filter", "\nAND modcode NOT LIKE 'analyse%'");
		return map;
	}
	
	@Override
	public void add() {
		super.add();
		data = new SysReport();
		data.setId(0l);
		
		this.dPkVal = this.getGridSelectId();
    	if(dPkVal==-1l){
    	}else{
    		SysReport sysReport = serviceContext.sysReportMgrService.sysReportDao.findById(dPkVal);
    		data.setFilepath(FileOperationUtil.getFileNameNoEx(sysReport.getFilepath())+"_copy"+"."+FileOperationUtil.getExtensionName(sysReport.getFilepath()));
    		data.setCode(sysReport.getCode()+"_copy");
    		data.setModcode(sysReport.getModcode());
    		data.setModid(sysReport.getModid());
    		data.setNamec(sysReport.getNamec()+"_copy");
    		data.setNamee(sysReport.getNamee()+"_copy");
    		
    		data.setIsleaf(sysReport.getIsleaf());
    		data.setIspublic(sysReport.getIspublic());
    		data.setIsreadonly(sysReport.getIsreadonly());
    	}
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		StringBuilder builder = new StringBuilder();
		for(int j=0;j<ids.length;j++){
			String id = ids[j];
			builder.append("UPDATE sys_report SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";");
		}
		this.serviceContext.userMgrService.sysUserDao.executeSQL(builder.toString());
		this.alert("OK!");
		this.refresh();
	}
	
	@Action
	public void getFilePath(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null) {
				MessageUtils.alert("请勾选一条记录!");
				return;
			}
			StringBuilder builder = new StringBuilder();
			for(int j=0;j<ids.length;j++){
				String id = ids[j];
				builder.append("UPDATE sys_report SET filepath = NULL WHERE id = " + id + ";");
			}
			this.serviceContext.userMgrService.sysUserDao.executeSQL(builder.toString());
			File file = new File(AppUtils.getReportFilePath());  
			showAllFiles(file);
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Action
	public void lockRaq(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null) {
				MessageUtils.alert("请勾选一条记录!");
				return;
			}
			StringBuilder builder = new StringBuilder();
			String updater = AppUtils.getUserSession().getUsercode();
			builder.append("UPDATE sys_report SET islock = TRUE,updater = '" + updater + "' WHERE id IN("+StrUtils.array2List(ids)+");");
			this.serviceContext.userMgrService.sysUserDao.executeSQL(builder.toString());
			File file = new File(AppUtils.getReportFilePath());  
			showAllFiles(file);
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Action
	public void unlockRaq(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null) {
				MessageUtils.alert("请勾选一条记录!");
				return;
			}
			StringBuilder builder = new StringBuilder();
			String updater = AppUtils.getUserSession().getUsercode();
			builder.append("UPDATE sys_report SET islock = FALSE,updater = '" + updater + "' WHERE id IN("+StrUtils.array2List(ids)+");");
			this.serviceContext.userMgrService.sysUserDao.executeSQL(builder.toString());
			File file = new File(AppUtils.getReportFilePath());  
			showAllFiles(file);
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showAllFiles(File dir) throws Exception{
		File[] fs = dir.listFiles();
		for(int i=0; i<fs.length; i++){
			if(fs[i].isDirectory()){
				showAllFiles(fs[i]);
			}else{
				String[] ids = this.grid.getSelectedIds();
				for(int j=0;j<ids.length;j++){
					int index=fs[i].getAbsolutePath().indexOf("reportFiles");
					String result=fs[i].getAbsolutePath().substring(index + "reportFiles".length());
					if(result.endsWith(".raq")){
						String id = ids[j];
						Long sid = Long.valueOf(id);
						SysReport sy = serviceContext.sysReportMgrService.sysReportDao.findById(sid);
						if(!StrUtils.isNull(sy.getFilename()) && result.endsWith(sy.getFilename())){
							sy.setFilepath(result);
							serviceContext.sysReportMgrService.saveData(sy);
							//System.out.println(result);
						}
					}
				}
			}
		}	
	}
	
	
	@Bind
	@SaveState
	private Long dPkVal;
	
	@Bind
	@SaveState
	public String fileName;
	
    @Action
	public void download(){
    	this.dPkVal = this.getGridSelectId();
    	if(dPkVal==-1l){
    		MessageUtils.alert("请勾选一条记录");
    		return;
    	}else{
    		SysReport sysReport = serviceContext.sysReportMgrService.sysReportDao.findById(dPkVal);
			this.fileName = sysReport.getFilename();
	    	this.update.markUpdate(UpdateLevel.Data,"dPkVal");
    	}
	}
    
    @Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
    	try {
			return this.serviceContext.sysReportMgrService.readFile(dPkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
    
    @Bind
	public UIWindow reportfileWindow;
    
    @Bind
	public UIFileUpload fileUpload1;
    
    @Action
	public void upload() {
    	this.dPkVal = this.getGridSelectId();
      	if(dPkVal==-1l){
      		MessageUtils.alert("请勾选一条记录");
      		return;
      	}else{
      		reportfileWindow.show();
      	}
	}
	
    public void processUpload1(FileUploadItem fileUploadItem) {
    	InputStream input = null;
    	FileOutputStream output = null;
        try {
        	SysReport sysReport = serviceContext.sysReportMgrService.sysReportDao.findById(dPkVal);
         	String fileName = fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getReportFilePath() + sysReport.getFilepath();
            File f = new File(filepath);
           // //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length);
            }
            output.flush();
            sysReport.setFilename(fileName);
            serviceContext.sysReportMgrService.saveData(sysReport);
         } catch (Exception e) {
             throw new FacesException(e);
         } finally {
        	 if (input != null){
            	 try {
            		 input.close();
                 } catch (IOException e) {
                	 e.printStackTrace();
                 }
        	 }
        	 if (output != null){
            	 try {
                     output.close();
                 } catch (IOException e) {
                	 e.printStackTrace();
                 }
        	 }
         }
    }
    
    public void processUploadimg(FileUploadItem fileUploadItem) {
    	InputStream input = null;
    	FileOutputStream output = null;
        try {
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getReportFilePath()+File.separator+"image"+File.separator+fileUploadItem.getName();
            File f = new File(filepath);
            output = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length);
            }
            output.flush();
         } catch (Exception e) {
             throw new FacesException(e);
         } finally {
        	 if (input != null){
            	 try {
            		 input.close();
                 } catch (IOException e) {
                	 e.printStackTrace();
                 }
        	 }
        	 if (output != null){
            	 try {
                     output.close();
                 } catch (IOException e) {
                	 e.printStackTrace();
                 }
        	 }
         }
    }

    
    
    @Action
	public void saveLocal(){
    	this.dPkVal = this.getGridSelectId();
    	if(dPkVal==-1l){
    		MessageUtils.alert("请勾选一条记录");
    		return;
    	}else{
    		try {
    			//String localDir = "local"; //Neo 20180308 暂时放到当前目录，其他目录会有问题
    			String localDir = "";
    			SysReport sysReport = serviceContext.sysReportMgrService.sysReportDao.findById(dPkVal);
    			String filePath = sysReport.getFilepath();
    			if(StrUtils.isNull(filePath)){
    				MessageUtils.alert("File path can not be null!");
    				return;
    			}
				File file = new File(AppUtils.getReportFilePath() + sysReport.getFilepath());
				////System.out.println(file.getAbsolutePath());
				File fileParent = file.getParentFile();
				////System.out.println(fileParent.getAbsolutePath());
				FileOperationUtil.newFolder(fileParent.getAbsolutePath() + File.separator + localDir);
				String targetFile = fileParent.getAbsolutePath() + File.separator + localDir + File.separator + FileOperationUtil.getFileNameNoEx(file.getName())+"_copy"+"."+FileOperationUtil.getExtensionName(file.getName());
				////System.out.println(targetFile);
				FileOperationUtil.copyFile(file.getAbsolutePath(), targetFile);
				
				SysReport sysReportNew = new SysReport();
				sysReportNew.setFilename(FileOperationUtil.getFileNameNoEx(file.getName())+"_copy"+"."+FileOperationUtil.getExtensionName(file.getName()));
				
				int index=targetFile.indexOf("reportFiles");
				String result=targetFile.substring(index + "reportFiles".length());
				
				sysReportNew.setFilepath(result);
				sysReportNew.setCode(sysReport.getCode()+"_copy");
				sysReportNew.setModcode(sysReport.getModcode());
				sysReportNew.setModid(sysReport.getModid());
				sysReportNew.setNamec(sysReport.getNamec()+"_copy");
				sysReportNew.setNamee(sysReport.getNamee()+"_copy");
				sysReportNew.setInfo(sysReport.getInfo());
				
				sysReportNew.setId(0l);
				sysReportNew.setIsleaf(sysReport.getIsleaf());
				sysReportNew.setIspublic(sysReport.getIspublic());
				sysReportNew.setIsreadonly(sysReport.getIsreadonly());
				
				serviceContext.sysReportMgrService.saveData(sysReportNew);
				
				MessageUtils.alert("OK!");
				this.refresh();
				
			} catch (Exception e) {
				MessageUtils.showException(e);
				e.printStackTrace();
			}  
    	}
	}

	
    
    /**
	 * 保存格式文件到数据库
	 */
    @Action
    public void saveDatabase(){
    	String[] ids = this.grid.getSelectedIds();
    	if(ids == null || ids.length < 1){
    		MessageUtils.alert("请至少勾选一条记录");
    		return;
    	}
    	try{
    		for (String id : ids) {
    			InputStream ips = null;
    			try {  	
	    			SysReport sy = serviceContext.sysReportMgrService.sysReportDao.findById(Long.valueOf(id));
	    			if(!StrUtils.isNull(sy.getFilename())) {  
			        	String fpath = AppUtils.getReportFilePath() + sy.getFilepath();
			        	File file = new File(fpath);
			        	if(StrUtils.isNull(fpath))continue;
			        	ips = new FileInputStream(file);
			        	byte[] byteData = new byte[(int) file.length()];  
			        	ips.read(byteData, 0, byteData.length);  
			            sy.setFilebyte(byteData);
			            serviceContext.sysReportMgrService.saveData(sy);
	    			}
    			} catch (Exception ex) {  
		        	ex.printStackTrace(System.out);  
		        } finally {  
		        	if(ips != null)ips.close();  
		        }  
    		}
    		MessageUtils.alert("OK!");
    		this.refresh();
    	}catch (Exception ex){
        	ex.printStackTrace();
        }
    }
    
    
    
    /**
	 * 更新数据库格式文件到本地
	 */
    @Action
    public void updateDataLocal(){
    	String[] ids = this.grid.getSelectedIds();
    	if(ids == null || ids.length < 1){
    		MessageUtils.alert("请至少勾选一条记录");
    		return;
    	}
    	for (String id : ids) {
			SysReport sy = serviceContext.sysReportMgrService.sysReportDao.findById(Long.valueOf(id));
			OutputStream ops = null;  
			BufferedOutputStream stream = null;
			try {  
				String fpath = AppUtils.getReportFilePath() + sy.getFilepath();
				if(StrUtils.isNull(fpath))continue;
				if(sy.getFilebyte() == null || sy.getFilebyte().length <= 0)continue;
		        File file = new File(fpath);  
		        try {                            
		           byte[] buffer = new byte[1024];
		           ops = new FileOutputStream(file);  
		           stream = new BufferedOutputStream(ops);  
		           stream.write(sy.getFilebyte());
		           stream.flush();
		        } catch (Exception ex) {  
		        	ex.printStackTrace(System.out);  
		        } finally {  
		        	if(stream != null) stream.close();
		        	if(ops != null) ops.close();  
		        }  
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }  
    	}
    	MessageUtils.alert("OK!");
    	this.refresh();
    }

    
    @Bind
	public String exportJsonText;
    
    @Action
    public void export2json(){
    	try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length < 1){
				MessageUtils.alert("请至少勾选一条记录");
				return;
			}
			String querySql = 
				"\nSELECT id, code, namec, namee, info, parentid, modid, filename, isleaf, " +
				"\n       modcode, remarks, 'convert_from(decode('||encode(convert_to(templete, 'UTF-8'),'base64')||',''base64''),''UTF-8'')' AS templete, userid, ispublic, isdelete, isreadonly, " +
				"\n       corpid, filepath, inputer, inputtime, updater, updatetime, isinuse, " +
				"\n       filebyte, islock, extjs" +
				"\n  FROM sys_report" +
				"\nWHERE id IN("+StrUtils.array2List(ids)+")";
			querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ") AS T";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			exportJsonText = StrUtils.getMapVal(map, "json");
			this.update.markUpdate(UpdateLevel.Data,"exportJsonText");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
    @Action
    public void json2db(){
    	try {
			//export2json();
			if(StrUtils.isNull(exportJsonText) || "[]".equals(exportJsonText))return;
			SqlObject sqlObject = new SqlObject("sys_report" , exportJsonText , AppUtils.getUserSession().getUsercode());
			sqlObject.setOpType("I");
			String sql = sqlObject.toSqls();
			sql = sql.replace("'convert_from(decode(", "convert_from(decode('");
			sql = sql.replace(",''base64''),''UTF-8'')'", "','base64'),'UTF-8')");
			//System.out.println("\n"+sql);
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
    @Action
    public void json2sqlInsert(){
    	try {
			//export2json();
			SqlObject sqlObject = new SqlObject("sys_report" , exportJsonText , AppUtils.getUserSession().getUsercode());
			sqlObject.setOpType("I");
			String sql = sqlObject.toSqls();
			sql = sql.replace("'convert_from(decode(", "convert_from(decode('");
			sql = sql.replace(",''base64''),''UTF-8'')'", "','base64'),'UTF-8')");
			//System.out.println("\n"+sql);
			exportJsonText = sql;
			this.update.markUpdate(UpdateLevel.Data,"exportJsonText");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
    
    @Action
    public void json2sqlUpdate(){
    	try {
			//export2json();
			SqlObject sqlObject = new SqlObject("sys_report" , exportJsonText , AppUtils.getUserSession().getUsercode());
			sqlObject.setOpType("U");
			String sql = sqlObject.toSqls();
			sql = sql.replace("'convert_from(decode(", "convert_from(decode('");
			sql = sql.replace(",''base64''),''UTF-8'')'", "','base64'),'UTF-8')");
			//System.out.println("\n"+sql);
			exportJsonText = sql;
			this.update.markUpdate(UpdateLevel.Data,"exportJsonText");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
}
