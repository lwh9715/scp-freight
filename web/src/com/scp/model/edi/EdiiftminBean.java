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
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.edi.ediiftminBean", scope = ManagedBeanScope.REQUEST)
public class EdiiftminBean extends EdiGridView{
	@Bind
	@SaveState
	private String sendercode = "";
	@Bind
	@SaveState
	private String acceptcode = "PENTJN";
	@Bind
	@SaveState
	private Date senddate = new Date();
	
	@Bind
	public boolean isSales=false;
	
	@SaveState
	private Map<String, String> cfgDataMap;
	
	@Bind
	@SaveState
	public boolean isFTP;
	
	@Bind
	@SaveState
	private String filetype="S";
	
	@SaveState
	public String randomString;
	
	@SaveState
	public String path ;
	
	@Bind
	@SaveState
	String dynamicClauseWhere  = "";
	
	@Bind
	@SaveState
	private String vessel="";
	@Bind
	@SaveState
	private String voyage="";
	
	@Bind
	@SaveState
	private String declarenos="";
	
	@Bind
	@SaveState
	public  String billid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				initData();
				rptnameSubmit();
				super.applyGridUserDef();
				
				String jobid = AppUtils.getReqParam("jobid");
				this.billid = AppUtils.getReqParam("billid");
				if(!StrUtils.isNull(jobid) && StrUtils.isNumber(jobid)){
					try {
						Long id = Long.valueOf(jobid);
						FinaJobs jobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(id);
						String nos = jobs.getNos();
						qryMap.put("nos", nos);
						gridLazyLoad = true;
						this.grid.reload();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				gridLazyLoad = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			Date d = new Date();
		}
	}
	
	protected void initData() throws Exception {
		String cfgDataMap [] = {"edi_iftmin_ftp_host","edi_iftmin_ftp_port","edi_iftmin_ftp_sername","edi_iftmin_ftp_possword"
								,"edi_iftmin_ftp_uploadpath","edi_iftmin_ftp_downloadpath","edi_iftmin_ftp_localpath","edi_iftmin_ftp_sendcode"
								,"edi_iftmin_ftp_recivecode","edi_iftmin_ftp_companyid","edi_iftmin_ftp_corporatename"};
		this.cfgDataMap = ConfigUtils.findUserCfgVals(cfgDataMap, AppUtils.getUserSession().getUserid());
		sendercode=(this.cfgDataMap.get("edi_iftmin_ftp_sendcode")==null?"":this.cfgDataMap.get("edi_iftmin_ftp_sendcode"));
		acceptcode=(this.cfgDataMap.get("edi_iftmin_ftp_recivecode")==null?"PENTJN":this.cfgDataMap.get("edi_iftmin_ftp_recivecode"));
	}
	
	/**
	 * FTP配置保存
	 * */
	@Action
	public void savesetFIP(){
		try {
			ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
			sendercode=(this.cfgDataMap.get("edi_iftmin_ftp_sendcode")==null?"":this.cfgDataMap.get("edi_iftmin_ftp_sendcode"));
			acceptcode=(this.cfgDataMap.get("edi_iftmin_ftp_recivecode")==null?"PENTJN":this.cfgDataMap.get("edi_iftmin_ftp_recivecode"));
			update.markUpdate(true, UpdateLevel.Data, "sendercode");
			update.markUpdate(true, UpdateLevel.Data, "acceptcode");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Map<String, String> getCfgDataMap() {
		return this.cfgDataMap;
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
			ftpClient.connect(this.cfgDataMap.get("edi_iftmin_ftp_host"));
			boolean login = ftpClient.login(this.cfgDataMap.get("edi_iftmin_ftp_sername"), this.cfgDataMap.get("edi_iftmin_ftp_possword")); 
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
	
	@SaveState
	public String jobidfirst = "";//记录首次勾选id
	
	@Action
	public void uploadFtp() {
		
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
        try { 
        	exportingbutton();
           
            
        	ftpClient.connect(this.cfgDataMap.get("edi_iftmin_ftp_host")); 
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_iftmin_ftp_sername"), this.cfgDataMap.get("edi_iftmin_ftp_possword")); 
            if(!login){
            	this.alert("FTP登录失败！");
            	return;
            }
            //设置上传目录 
            ftpClient.changeWorkingDirectory("/"+this.cfgDataMap.get("edi_iftmin_ftp_uploadpath"));
            System.out.println("3.ftp login"+path);
            File srcFile = new File(path); 
            fis = new FileInputStream(srcFile); 
           // ftpClient.changeWorkingDirectory("/"); 
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
        }catch (java.io.FileNotFoundException e){
        	this.alert("导出文件失败！");
        	e.printStackTrace();
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
	
	@Action
	public void exportingbutton(){
		saveMasterbutton();
		String[] ids = this.grid.getSelectedIds();
		boolean running = false;
		if (ids == null || ids.length == 0 || ids.length > 1 ) {
			MessageUtils.alert("请选择一条记录");
			path = "";
			return;
		}
		String jobids ="";
		for(int i=0;i<ids.length;i++){
			jobids = jobids+ids[i]+",";
		}
		jobids = jobidfirst + ","+jobids.replace(jobidfirst, "");
		jobids = jobids.replaceAll("^,*|,*$", "");//正则去掉两边的逗号
		//System.out.println(jobids);
		try {
			running = running(jobids);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("生成失败!");	
			return;
		}
		if(running){
			MessageUtils.alert("txt文件生成成功");	
			this.grid.reload();
		}else{
			MessageUtils.alert("生成失败!");	
		}
	}
	
	@Action
	public void exporting(){
		String[] ids = this.grid.getSelectedIds();
		boolean running = false;
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择至少一条记录");
			return;
		}
		String jobids ="";
		for(int i=0;i<ids.length;i++){
			jobids = jobids+ids[i]+",";
		}
//		//System.out.println(jobids.substring(0,jobids.length()-1));
		jobids = jobids.substring(0,jobids.length()-1);
		String[] strs = jobids.split(",");
		for(String s:strs) {
			running = running(s);
		}
		
		if(running){
			if(isFTP){
				MessageUtils.alert("TXT文件生成成功");
			}else{
				MessageUtils.alert("["+ids.length+"]个TXT文件生成成功，已经保存到桌面edi文件夹中");
			}
		}
		else{
			if(isFTP){
				
			}else{
				//MessageUtils.alert("导出失败,请看结果信息");
				path = "";
			}
		}
	}
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String contentType;
	
	@Bind
	@SaveState
	private String ediStrTextArea;
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
    	try {
    		InputStream input = new FileInputStream(path);
    		return input;
    	} catch (java.io.FileNotFoundException e){
    		return null;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}finally{
			//获得当前系统桌面路径
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			String absolutePath = desktopDir.getAbsolutePath(); 
			//删除该文件夹下所有文件
			delAllFile(absolutePath+"/edi/iftmin");
		}
    }
	
	public boolean running(String jobids){
		Long userid = AppUtils.getUserSession().getUserid();
		Map<String, String> argsMap = new HashMap<String, String>();
		String urlArgs2= "";
//			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(senddate);
			argsMap.put("jobids", jobids);
			argsMap.put("sendercode", sendercode);
			argsMap.put("acceptcode", acceptcode);
			argsMap.put("senddate", new SimpleDateFormat("yyyyMMdd:HHmm").format(senddate));
			argsMap.put("filetype", filetype);
			argsMap.put("userid", userid.toString());
			argsMap.put("billid", this.billid);
			urlArgs2 = AppUtils.map2Url(argsMap, ";");
		try {
			Object object = null;
			if(filetype.equals("S")){
				String sqlQry = "SELECT * FROM f_esi_iftmin('"+urlArgs2+"')";
			//String sqlQry = "SELECT * FROM test('"+urlArgs2+"')";
				Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
				object = m.get("f_esi_iftmin");
				//object = m.get("test");
			}else if(filetype.equals("D")){
				String sqlQry = "SELECT * FROM f_edi_iftmin('"+urlArgs2+"')";
				Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
				object = m.get("f_edi_iftmin");
			}
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
				return writer(str,jobids);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return false;
		}
	}
	/*
	 * 写入文件
	 * */
	public  boolean writer(String str,String jid){
		Random ra =new Random();
		//获得当前系统桌面路径
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String absolutePath = desktopDir.getAbsolutePath(); 
		Format format = new SimpleDateFormat("yyyyMMddHHmmsss");
//		randomString = format.format(new Date())+getcounts();
		String sql = "SELECT mblno,vescode,voyage FROM bus_shipping WHERE jobid = " + this.grid.getSelectedIds()[0];
		if(!StrUtils.isNull(this.billid)){
			sql = "SELECT mblno FROM bus_ship_bill WHERE id ="+this.billid;
		}
		Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String mblno = map.get("mblno").toString();
		randomString = mblno+"-"+format.format(new Date())+getcounts();
		
		contentType = "text/plain";
		File file = new File(absolutePath+"/edi/iftmin");
		if (!file .exists()  && !file .isDirectory()) {     
			file.mkdirs();
		}
		fileName = randomString+".txt";
		path=absolutePath+"/edi/iftmin/"+fileName;
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
		if(isFTP){
			return testUpload();
		}else{
			return true;
		}
	}
	
	public  boolean testUpload () { 
        FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 

        try { 
        	ftpClient.connect(this.cfgDataMap.get("edi_iftmin_ftp_host")); 
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_iftmin_ftp_sername"), this.cfgDataMap.get("edi_iftmin_ftp_possword")); 
            if(login){
	            File srcFile = new File(path); 
	            fis = new FileInputStream(srcFile); 
	            //设置上传目录 
	            ftpClient.changeWorkingDirectory("/"+this.cfgDataMap.get("edi_iftmin_ftp_uploadpath")); 
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
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//拼接申报号
		if(declarenos !=""){
			qry += "\nAND EXISTS (SELECT 1 FROM bus_ship_container WHERE isdelete = FALSE AND jobid = t.id AND declareno LIKE '%"+declarenos+"%')";
		}
		m.put("qry", qry);
		//初始化
		dynamicClauseWhere = " 1=1 ";
		//船名航次拼接
		if(vessel!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.vessel LIKE '%"+vessel+"%')";
		}
		if(voyage!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.voyage LIKE '%"+voyage+"%')";
		}
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		
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
	

	@Action
	public void show(){
		String[] ids = this.grid.getSelectedIds();
		boolean running = false;
		if (ids == null || ids.length != 1 ) {
			MessageUtils.alert("请选择一条记录");
			return;
		}
		Long userid = AppUtils.getUserSession().getUserid();
		Map<String, String> argsMap = new HashMap<String, String>();
		String urlArgs2= "";
//		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(senddate);
		argsMap.put("jobids", ids[0]);
		argsMap.put("sendercode", sendercode);
		argsMap.put("acceptcode", acceptcode);
		argsMap.put("senddate", new SimpleDateFormat("yyyyMMdd:HHmm").format(senddate));
		argsMap.put("filetype", filetype);
		argsMap.put("userid", userid.toString());
		urlArgs2 = AppUtils.map2Url(argsMap, ";"); 
		try {
			String sqlQry = "SELECT f_esi_iftmin('"+urlArgs2+"') AS edi";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			ediStrTextArea = StrUtils.getMapVal(m, "edi");
			update.markUpdate(true, UpdateLevel.Data, "ediStrTextArea");
			//System.out.println(ediStrTextArea);
			Browser.execClientScript("showWindowJsVar.show()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	public String rptname = "长荣";
	
	@Action
	public void rptnameSubmit(){
		try {
			String jsonSql = ConfigUtils.findSysCfgVal("esi_config_json");
			JSONArray json = JSONArray.fromObject(jsonSql);
			for(int i=0; i<json.size(); i++){
				JSONObject job = json.getJSONObject(i);
				if(rptname.equals(job.get("rptname"))){
					cfgDataMap.put("edi_iftmin_ftp_sendcode", job.getString("sendcode"));//发送方代码
					cfgDataMap.put("edi_iftmin_ftp_recivecode", job.getString("reccode"));//接受方代码
					cfgDataMap.put("edi_iftmin_filetype", job.getString("filetype"));//生成报文类型
					cfgDataMap.put("edi_iftmin_state", job.getString("state"));//EDI状态
					cfgDataMap.put("edi_iftmin_ftp_sername", job.getString("ftpuser"));//FTP用户名
					cfgDataMap.put("edi_iftmin_ftp_possword", job.getString("ftppwd"));//FTP密码
					cfgDataMap.put("edi_iftmin_ftp_host", job.getString("ftp"));//FTP主机
					cfgDataMap.put("edi_iftmin_ftp_port", job.getString("port"));//FTP端口
					cfgDataMap.put("edi_iftmin_ftp_uploadpath", job.getString("uploadpath"));//FTP路径
					
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
}
