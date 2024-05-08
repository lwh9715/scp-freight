package com.scp.model.edi;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.edi.ediinttraBean", scope = ManagedBeanScope.REQUEST)
public class EdiinttraBean  extends EdiGridView{
	@Bind
	@SaveState
	private String startDate;

	@Bind
	@SaveState
	private String endDate;
	@Bind
	@SaveState
	private String vessel="";
	@Bind
	@SaveState
	private String voyage="";
	@Bind
	@SaveState
	private String filetype="5";
	@Bind
	@SaveState
	private String sendercode="";
	@Bind
	@SaveState
	private String acceptcode="";
	@Bind
	@SaveState
	private String senddate="";
	@Bind
	@SaveState
	private String specification;
	@Bind
	@SaveState
	public String hblnosubs="";
	
	@Bind
	@SaveState
	String dynamicClauseWhere  ="";
	
	@Bind
	public UIWindow showsetFIPWindow;
	
	@SaveState
	private Map<String, String> cfgDataMap;
	
	@Bind
	@SaveState
	public boolean isFTP;
	
	@SaveState
	public String path ;
	
	@SaveState
	public String randomString;
	
	@Bind
	@SaveState
	public String fileName;
	@Bind
	@SaveState
	public String contentType;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		try {
			initData();
			if (!isPostBack) {
				super.applyGridUserDef();
				
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void initData() throws Exception {
		this.cfgDataMap = new HashMap();
		String str1 = getQuerySql();
		if (!StrUtils.isNull(str1)) {
			String str2 = getClauseWhere();
			if (StrUtils.isNull(str2)) return;
			str1 = getQuerySql() + getClauseWhere();
			
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> localList = daoIbatisTemplate.queryWithUserDefineSql(str1);
			if (localList != null) {
				Iterator localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					Map localMap = (Map) localIterator.next();
					this.cfgDataMap.put(String.valueOf(localMap
							.get("key")), String.valueOf(localMap
							.get("val")));
				}
			}
		}
	}
	
	
	
	private String getClauseWhere() {
		Vector<String> localVector = defineCfgKey();
		if ((localVector == null) || (localVector.size() <= 0))
			return null;
		String str1 = "";
		Object localObject = localVector.iterator();
		while (((Iterator) localObject).hasNext()) {
			String str2 = (String) ((Iterator) localObject).next();
			str1 = str1 + "'" + str2 + "'" + ",";
		}
		str1 = str1.substring(0, str1.length() - 1);
		localObject = "\nAND key IN (" + str1 + ")";
		return (String) localObject;
	}
	
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add("edi_inttra_ftp_host");
		vector.add("edi_inttra_ftp_port");
		vector.add("edi_inttra_ftp_sername");
		vector.add("edi_inttra_ftp_possword");
		vector.add("edi_inttra_ftp_uploadpath");
		vector.add("edi_inttra_ftp_downloadpath");
		vector.add("edi_inttra_ftp_localpath");
		return vector;
	}


	@Action
	public void exporting(){
		String[] ids = this.grid.getSelectedIds();
		boolean running = false;
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择至少一条记录");
			return;
		}
		for(int i=0;i<ids.length;i++){
			String jobids = ids[i];
			running = running(jobids);
		}
		if(running){
			if(isFTP){
				MessageUtils.alert("txt文件生成成功");
			}else{
				MessageUtils.alert("txt文件生成成功，已经保存到桌面edi文件夹中");
			}
		}
		else{
//			MessageUtils.alert("条款不符合规范！");
			if(isFTP){
				
			}else{
				//MessageUtils.alert("导出失败,请看结果信息");
				path = "";
			}
		}
	}
	
	public boolean running(String jobids){
		Long userid = AppUtils.getUserSession().getUserid();
		Map<String, String> argsMap = new HashMap<String, String>();
		String urlArgs2= "";
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(senddate);
			argsMap.put("jobids", jobids);
			argsMap.put("sendercode", sendercode);
			argsMap.put("senddate", new SimpleDateFormat("yyMMdd:hhmm").format(date));
			argsMap.put("filetype", filetype);
			argsMap.put("userid", userid.toString());
			urlArgs2 = AppUtils.map2Url(argsMap, ";");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			String sqlQry = "SELECT * FROM f_sdi_inttra('"+urlArgs2+"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			Object object = m.get("f_sdi_inttra");
			String str = "";
			if(object!=null){
				str = object.toString();
			}
			if(str.equals("erro")){
				this.grid.reload();
				return false;
			}else{
				this.grid.reload();
				return writer(str);
			}
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
		randomString = getRandomString(3);
		Format format = new SimpleDateFormat("yyyyMMdd");
		randomString = format.format(new Date())+getcounts();
		contentType = "text/plain";
		File file = new File(absolutePath+"/edi/inttra");
		if (!file .exists()  && !file .isDirectory()) {     
			file.mkdirs();
		}
		fileName = randomString+".PMS";
		path=absolutePath+"/edi/inttra/"+fileName;
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
	
	@Action
	public void savesetFIP(){
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				ConfigUtils.refreshSysCfg(key, this.getCfgDataMap().get(key) , AppUtils.getUserSession().getUserid());
			}
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	public Map<String, String> getCfgDataMap() {
		return this.cfgDataMap;
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		//初始化
		dynamicClauseWhere = " 1=1 ";
		dynamicClauseWhere  += "\nAND jobdate BETWEEN '"
		+ (StrUtils.isNull(startDate) ? "0001-01-01" : startDate)
		+ "' AND '"
		+ (StrUtils.isNull(endDate) ? "9999-12-31" : endDate)
		+ "'";
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
	/**
	 * FTP不放文件测试
	 * */
	@Action
	public void testUploads2(){
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
		try {
			ftpClient.connect(this.cfgDataMap.get("edi_inttra_ftp_host"));
			boolean login = ftpClient.login(this.cfgDataMap.get("edi_inttra_ftp_sername"), this.cfgDataMap.get("edi_inttra_ftp_possword")); 
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
	/** 
     * FTP上传单个文件测试 
     */
	@Action
	public void testUploads(){
		//获得当前系统桌面路径
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String absolutePath = desktopDir.getAbsolutePath(); 
		
		File file = new File(absolutePath+"/text.txt");
		try {
			file.createNewFile();
			
			path = absolutePath+"/text.txt";
			if (!file .exists()  && !file.isDirectory()) {     
				file.mkdirs();
			}
			FileWriter fw = new FileWriter(path);
			fw.write("该文件为测试文件，请删除！");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(testUpload()){
			 this.alert("FTP配置配置正确！");
		}else{
//			this.alert("FTP配置配置有误！");
		}
		file.delete();
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
	  /** 
     * FTP下载单个文件测试 
     */ 
	@Action
    public  void testDownload() { 
        FTPClient ftpClient = new FTPClient(); 
        FileOutputStream fos = null; 
        try { 
            ftpClient.connect("192.168.0.101"); 
            ftpClient.login("admin", "123"); 
            String remoteFileName = "/admin/pic/3.gif"; 
            fos = new FileOutputStream("c:/down.gif"); 
            ftpClient.setBufferSize(1024); 
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftpClient.retrieveFile(remoteFileName, fos); 
        } catch (IOException e) { 
            e.printStackTrace(); 
            this.alert("FTP客户端出错！");
            throw new RuntimeException("FTP客户端出错！", e); 
        } finally { 
            IOUtils.closeQuietly(fos); 
            //System.out.println("55555555555555");
            try { 
                ftpClient.disconnect(); 
                //System.out.println("66666666666666");
            } catch (IOException e) { 
                e.printStackTrace(); 
                this.alert("关闭FTP连接发生异常！");
                throw new RuntimeException("关闭FTP连接发生异常！", e); 
            } 
        } 
    } 
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
    	try {
    		InputStream input = new FileInputStream(path);
    		return input;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}finally{
			//获得当前系统桌面路径
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			String absolutePath = desktopDir.getAbsolutePath(); 
			//删除该文件夹下所有文件
			delAllFile(absolutePath+"/edi/inttra");
		}
    }
}
