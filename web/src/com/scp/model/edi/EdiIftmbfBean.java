package com.scp.model.edi;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.filechooser.FileSystemView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.edi.ediiftmbfBean", scope = ManagedBeanScope.REQUEST)
public class EdiIftmbfBean  extends EdiGridView{
	
	@SaveState
	public Map<String, String> cfgDataMap;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				initData();
				rptnameSubmit();
				String jobid = AppUtils.getReqParam("jobid");
				if(!StrUtils.isNull(jobid) && StrUtils.isNumber(jobid)){
					try {
						Long id = Long.valueOf(jobid);
						FinaJobs jobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(id);
						String nos = jobs.getNos();
						qryMap.put("nos", nos);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				gridLazyLoad = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	protected void initData() throws Exception {
		String cfgDataMapkey [] = {"edi_iftmbf_sendcode","edi_iftmbf_recivecode","edi_iftmbf_sendpolcode","edi_iftmbf_recivepolcode","edi_iftmbf_course"
								,"edi_iftmbf_cnttype","edi_iftmbf_filetype","edi_iftmbf_wharf","edi_iftmbf_cntcode","edi_iftmbf_cntypetype"
								,"edi_iftmbf_autograph","edi_iftmbf_contact","edi_iftmbf_mail","edi_iftmbf_state"
								,"edi_iftmbf_ftp_host","edi_iftmbf_ftp_port","edi_iftmbf_ftp_sername","edi_iftmbf_ftp_possword"};
		this.cfgDataMap = ConfigUtils.findUserCfgVals(cfgDataMapkey, AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(cfgDataMap.get("edi_iftmbf_course"))){
			cfgDataMap.put("edi_iftmbf_course", "E");//航向默认为东
		}
		if(StrUtils.isNull(cfgDataMap.get("edi_iftmbf_filetype"))){
			cfgDataMap.put("edi_iftmbf_filetype", "1");
		}
		if(StrUtils.isNull(cfgDataMap.get("edi_iftmbf_cnttype"))){
			cfgDataMap.put("edi_iftmbf_cnttype", "1");
		}
		if(StrUtils.isNull(cfgDataMap.get("edi_iftmbf_sendcode"))){
			cfgDataMap.put("edi_iftmbf_sendcode", "SZHZ");
		}
		if(StrUtils.isNull(cfgDataMap.get("edi_iftmbf_state"))){
			cfgDataMap.put("edi_iftmbf_state", "9");
		}
	}
	
	@Bind
	public UIIFrame shipperinfoIFrame;
	
	@Bind
	public UIWindow shipperinfoWindow;
	
	/**
	 * 校正收发通资料
	 * */
	@Action
	public void regulate(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择一条记录");
			return;
		}
		String id = ids[0];
		Long jobid = Long.valueOf(id);
		//System.out.println(jobid);
		shipperinfoIFrame.load("./editrans.xhtml?jobid=" + jobid);
		shipperinfoWindow.show();
		
	}
	
	@Action
	public void saveMasterbutton(){
		try {
			ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
//			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		m.put("corpfilter", corpfilter);
		return m;
	}
	
	@SaveState
	public String path ;
	
	@SaveState
	public String jobidfirst = "";//记录首次勾选id
	
	@Action
	public void exportingbutton(){
		saveMasterbutton();
		String[] ids = this.grid.getSelectedIds();
		boolean flag = false;
		if(ids == null || ids.length == 0 || (ids.length > 1 && rptname.indexOf("舱单") == -1 && rptname.indexOf("预配") == -1 && rptname.indexOf("ZH托单-ZH") == -1)){
			MessageUtils.alert("只能选择一条记录");
			path = "";
			return;
		}
		String jobids ="";
		for(int i=0;i<ids.length;i++){
			jobids = "".equals(jobids)?ids[i]:jobids+","+ids[i];
		}
		//System.out.println(jobids);
		try {
			flag = exportEdi(jobids);
		} catch (Exception e) {
			e.printStackTrace();
			//MessageUtils.alert("生成失败!");	
			MessageUtils.showException(e);
			return;
		}
		if(flag){
			MessageUtils.alert("txt文件生成成功");	
			this.grid.reload();
		}else{
			MessageUtils.alert("生成失败!");	
		}
	}
	
	@Action
	public void rptnameSubmit(){		
		try {
			String jsonSql = ConfigUtils.findSysCfgVal("esi_config_json");
			JSONArray json = JSONArray.fromObject(jsonSql);
			for(int i=0; i<json.size(); i++){
				JSONObject job = json.getJSONObject(i);
				if(rptname.equals(job.get("rptname"))){
					cfgDataMap.put("edi_iftmbf_sendcode", job.getString("sendcode"));
					cfgDataMap.put("edi_iftmbf_recivecode", job.getString("reccode"));
					cfgDataMap.put("edi_iftmbf_filetype", job.getString("filetype"));
					cfgDataMap.put("edi_iftmbf_state", job.getString("state"));
					cfgDataMap.put("edi_iftmbf_ftp_sername", job.getString("ftpuser"));
					cfgDataMap.put("edi_iftmbf_ftp_possword", job.getString("ftppwd"));
					cfgDataMap.put("edi_iftmbf_ftp_host", job.getString("ftp"));
					
					update.markUpdate(true,UpdateLevel.Data,"sendcode");
					update.markUpdate(true,UpdateLevel.Data,"recivecode");
					
					update.markUpdate(true,UpdateLevel.Data,"host");
					update.markUpdate(true,UpdateLevel.Data,"sername");
					update.markUpdate(true,UpdateLevel.Data,"possword");
					
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	public boolean exportEdi(String jobids) throws Exception{
		Long userid = AppUtils.getUserSession().getUserid();
		Map<String, String> argsMap = new HashMap<String, String>();
		String urlArgs2= "";
		
		argsMap.put("jobids", jobids);
		argsMap.put("sendcode", cfgDataMap.get("edi_iftmbf_sendcode"));
		argsMap.put("recivecode", cfgDataMap.get("edi_iftmbf_recivecode"));
		argsMap.put("sendpolcode", cfgDataMap.get("edi_iftmbf_sendpolcode"));
		argsMap.put("recivepolcode", cfgDataMap.get("edi_iftmbf_recivepolcode"));
		argsMap.put("course", cfgDataMap.get("edi_iftmbf_course"));
		argsMap.put("cnttype", cfgDataMap.get("edi_iftmbf_cnttype"));
		argsMap.put("wharf", cfgDataMap.get("edi_iftmbf_wharf"));
		argsMap.put("cntcode", cfgDataMap.get("edi_iftmbf_cntcode"));
		argsMap.put("cntypetype", cfgDataMap.get("edi_iftmbf_cntypetype"));
		argsMap.put("autograph", cfgDataMap.get("edi_iftmbf_autograph"));
		argsMap.put("contact", cfgDataMap.get("edi_iftmbf_contact"));
		argsMap.put("mail", cfgDataMap.get("edi_iftmbf_mail"));
		argsMap.put("state", cfgDataMap.get("edi_iftmbf_state"));
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		urlArgs2 = AppUtils.map2Url(argsMap, ";");
		try {
			String sqlQry = "";

			
			if(cfgDataMap.get("edi_iftmbf_filetype").equals("1")){
				sqlQry = "SELECT * FROM f_edi_iftmbf_vgm('"+urlArgs2+"') AS iftmbf";
			}else if(cfgDataMap.get("edi_iftmbf_filetype").equals("2")){
				sqlQry = "SELECT * FROM f_edi_iftmbf_booking('"+urlArgs2+"') AS iftmbf";
			}else if(cfgDataMap.get("edi_iftmbf_filetype").equals("3")){
				sqlQry = "SELECT * FROM f_edi_iftmbf_prein('"+urlArgs2+"') AS iftmbf";
			}else if(cfgDataMap.get("edi_iftmbf_filetype").equals("4")){
				sqlQry = "SELECT * FROM f_edi_iftmbf_bookwharf('"+urlArgs2+"type="+rptname+";reason="+cfgDataMap.get("edi_iftmbf_reason")+"') AS iftmbf";
			}
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			Object object;
			object = m.get("iftmbf");
			
			String str = "";
			if(object!=null){
				str = object.toString();
			}
			if(str.equals("erro")){
				this.grid.reload();
				path = "";
				return false;
			}else{
				this.grid.reload();
				return writer(str);
			}
		} catch (Exception e) {
			//MessageUtils.showException(e);
			throw e;
		}
	}
	
	@SaveState
	public String randomString;
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String rptname = "NPEDI";
	
	@Bind
	@SaveState
	public String contentType;
	
	@Bind
	@SaveState
	public boolean isFTP;
	
	/*
	 * 写入文件
	 * */
	public  boolean writer(String str){
		Random ra =new Random();
		//获得当前系统桌面路径
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String absolutePath = desktopDir.getAbsolutePath(); 
		Format format = new SimpleDateFormat("yyyyMMdd");
		randomString = format.format(new Date())+getcounts();
		String sql = "SELECT mblno,vessel,voyage FROM bus_shipping WHERE jobid = " + this.grid.getSelectedIds()[0];
		Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String mblno = StrUtils.getMapVal(map, "mblno");
		String vessel = StrUtils.getMapVal(map, "vescode");
		String voyage = StrUtils.getMapVal(map, "voyage");
		if(rptname.contains("预配")){
			randomString = "YP" + "-" +mblno;
		}
		if(rptname.contains("订舱")){
			randomString = "SO"+ "-" +mblno;
		}
		if(rptname.contains("VGM")){
			randomString = "VGM"+ "-" +mblno;
		}
		if(rptname.contains("NPEDI")){
			randomString = "NPEDI"+ "-" +mblno;
		}
		if(rptname.contains("舱单")){
			randomString = vessel + "-" +voyage;
		}
		if(rptname.contains("托单")){
			randomString = "TD"+ "-" +mblno;
		}
		contentType = "text/plain";
		fileName = randomString+".txt";
		
		//将EDI文件保存到服务器临时文件夹
		File newFile2 = null;
		try {
			String syspath2 = AppUtils.getWebApplicationPath();
			String rootFileDir = syspath2 + File.separator + "edi" + File.separator + "iftmbf";
			File file2 = new File(rootFileDir);
			if (!file2.exists()  && !file2.isDirectory()) {
				file2.mkdirs();
			}
			path = rootFileDir + File.separator + fileName;
			System.out.println("1.writerFile path:"+path);
			newFile2 = newFile(path);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(newFile2));
			bufferedWriter.write(str);
			//关闭资源
			bufferedWriter.flush();
			System.out.println("2.writerFile sucess:"+fileName);
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (Exception e) {
				}
			}
		}
		return true;
		
	}
	
	@Action
    public  boolean testUpload () { 
        FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 

        try { 
        	ftpClient.connect(this.cfgDataMap.get("edi_inttra_ftp_host")); 
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_inttra_ftp_sername"), this.cfgDataMap.get("edi_inttra_ftp_possword")); 
            if(login){
	            File srcFile = new File(path); 
	            fis = new FileInputStream(srcFile); 
	            //设置上传目录 
	            ftpClient.changeWorkingDirectory("/"+this.cfgDataMap.get("edi_inttra_ftp_uploadpath")); 
	            ftpClient.setBufferSize(1024); 
	            ftpClient.setControlEncoding("GBK"); 
	            //设置文件类型（二进制） 
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
	            ftpClient.storeFile("intiarsi"+randomString+".txt", fis);
	            return true;
            }else{
//            	this.alert("FTP配置填写错误！");
            	return false;
            } 
        }catch (SocketException e) {
			this.alert("FTP配置配置错误2！");
			e.printStackTrace();
			return false;
		}
        catch (IOException e) { 
        	e.printStackTrace(); 
            throw new RuntimeException("FTP客户端出错！", e);
        } finally { 
            IOUtils.closeQuietly(fis);
            try { 
                ftpClient.disconnect(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
                this.alert("关闭FTP连接发生异常！");
                return false;
                //throw new RuntimeException("关闭FTP连接发生异常！", e); 
            } 
        } 
    } 
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
    	try {
    		InputStream input = new FileInputStream(path);
    		return input;
		} catch (Exception e) {
//			MessageUtils.showException(e);
			return null;
		}finally{
			//获得当前系统桌面路径
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			String absolutePath = desktopDir.getAbsolutePath(); 
			//删除该文件夹下所有文件
			delAllFile(absolutePath+"/edi/iftmbf");
		}
    }
	
	 /**
	 * FTP配置保存
	 * */
	@Action
	public void savesetFTP(){
		saveMasterbutton();
		MessageUtils.alert("OK!");
	}
	
	/**
	 * FTP不放文件测试
	 * */
	@Action
	public void testUploads2(){
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
		try {
			ftpClient.connect(this.cfgDataMap.get("edi_iftmbf_ftp_host"));
			boolean login = ftpClient.login(this.cfgDataMap.get("edi_iftmbf_ftp_sername"), this.cfgDataMap.get("edi_iftmbf_ftp_possword")); 
			if(login){
				this.alert("FTP配置配置正确！");
				return;
			}else{
				this.alert("账号或密码错误1！");
			}
		} catch (SocketException e) {
			this.alert("FTP配置配置错误2！");
			e.printStackTrace();
		} catch (IOException e) {
			this.alert("FTP配置配置错误3！");
			e.printStackTrace();
		} 
	}
	
	
	
	@Action
	public void uploadFtp() {
		
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
        try { 
        	exportingbutton();
           
        	ftpClient.connect(this.cfgDataMap.get("edi_iftmbf_ftp_host")); 
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_iftmbf_ftp_sername"), this.cfgDataMap.get("edi_iftmbf_ftp_possword")); 
            if(!login){
            	this.alert("FTP登录失败！");
            	return;
            }
            System.out.println("3.ftp login"+path);
            File srcFile = new File(path); 
            fis = new FileInputStream(srcFile); 
            ftpClient.changeWorkingDirectory("/in"); 
            ftpClient.setBufferSize(1024); 
            ftpClient.setControlEncoding("GBK"); 
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            String ftpFileName = randomString+".txt";
            System.out.println("3.5.ftp storeFile start"+ftpFileName);
            ftpClient.storeFile(ftpFileName, fis);
            System.out.println("4.ftp upload file:"+ftpFileName);
            this.alert("上传成功！文件名:"+ftpFileName);
        }catch (SocketException e) {
			this.alert("FTP配置配置错误2！");
			e.printStackTrace();
		}catch (Exception e) { 
        	e.printStackTrace(); 
            //throw new RuntimeException("FTP客户端出错！", e);
            MessageUtils.showException(e);
        } finally { 
            try { 
            	IOUtils.closeQuietly(fis);
                ftpClient.disconnect(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
                this.alert("关闭FTP连接发生异常！");
                //throw new RuntimeException("关闭FTP连接发生异常！", e); 
            } 
        } 
    } 
	
	
}
