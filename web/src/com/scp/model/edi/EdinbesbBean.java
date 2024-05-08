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
import java.util.TreeMap;

import javax.swing.filechooser.FileSystemView;

import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

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
@ManagedBean(name = "pages.module.edi.edinbesbBean", scope = ManagedBeanScope.REQUEST)
public class EdinbesbBean extends EdiGridView{
	
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
	private String filetype="json";
	
	@Bind
	@SaveState
	private String interfaceURL="testInterface";
	
	@Bind
	@SaveState
	private String sendercode="";
	
	@Bind
	@SaveState
	private String dockName;
	
	@Bind
	@SaveState
	private String acceptcode="";
	/*@Bind
	@SaveState
	private Date senddate;*/
	@Bind
	@SaveState
	private String specification;
	@Bind
	@SaveState
	public String hblnosubs="";
	
	@Bind
	@SaveState
	String dynamicClauseWhere  ="";
	
	
//	@SaveState
//	public String path;
	
	@SaveState
	public String path2;
	
	@SaveState
	public String randomString;
	
	@Bind
	@SaveState
	public String fileName;
	@Bind
	@SaveState
	public String contentType;
	
	@Bind
	@SaveState
	public String userid;
	
	
	@Bind
	@SaveState
	public String apikey;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				//senddate = Calendar.getInstance().getTime();
				super.applyGridUserDef();
				initData();
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
				update.markUpdate(true, UpdateLevel.Data, "editPanel");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	
	
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() {
		if(StrUtils.isNull(path2)){
			return null;
		}
		InputStream input = null;
    	try {
    		input = new FileInputStream(path2);
    		return input;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}finally{
			
		}
    }
	
	
	
	@Action
	public void exporting(){
		
//		if(StrUtils.isNull(dockName)){
//			MessageUtils.alert("出运码头不能为空");
//			return;
//		}
		
		
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
		try {
			jobids = jobids.substring(0,jobids.length()-1);
			String[] strs = jobids.split(",");
			for(String s:strs) {
				running = running(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MessageUtils.alert("【"+ids.length+"】个TXT文件生成成功，已经保存到桌面edi文件夹中");	
	}
	
	
	public boolean running(String jobids) throws Exception{
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			Map<String, String> argsMap = new HashMap<String, String>();
			String urlArgs2= "";
			argsMap.put("jobids", jobids);
			//argsMap.put("sendercode", sendercode);
			//argsMap.put("senddate", senddate.toGMTString());
			//argsMap.put("filetype", filetype);
			argsMap.put("dockName", dockName);
			//System.out.println("dockName-->"+dockName);
			argsMap.put("userid", userid.toString());
			urlArgs2 = AppUtils.map2Url(argsMap, ";");
			String sqlQry = "SELECT * FROM f_edi_nbesb('"+urlArgs2+"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			Object object = m.get("f_edi_nbesb");
			String str = "";
			if(object!=null){
				str = object.toString();
			}
			if(str.equals("erro")){
				this.grid.reload();
				return false;
			}else{
				this.grid.reload();
				if("xml".equals(filetype)){
					return writer(json2xml(str),jobids);
				}else{
					return writer(str,jobids);
				}
				
			}
		} catch (Exception e) {
			throw e;
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
		//Format format = new SimpleDateFormat("yyyyMMddHHmmsss");
		//randomString = format.format(new Date())+getcounts();
		contentType = "text/plain";
		File file = new File(absolutePath+"/edi/nbesb");
		if (!file .exists()  && !file .isDirectory()) {     
			file.mkdirs();
		}
		fileName = jid+".json";
		String path=absolutePath+"/edi/nbesb/"+fileName;

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
	/*
	 * 写入文件
	 * */
	public boolean writerFile(String str){
		Random ra =new Random();
		randomString = getRandomString(3);
		Format format = new SimpleDateFormat("yyyyMMdd");
		randomString = format.format(new Date())+getcounts();
		contentType = "text/plain";
		
		fileName = randomString+"."+filetype;
		
		//将EDI文件保存到服务器临时文件夹
		File newFile2 = null;
		try {
			String syspath2 = AppUtils.getWebApplicationPath();
			String rootFileDir = syspath2 + File.separator + "edi" + File.separator + "nbesb";
			File file2 = new File(rootFileDir);
			if (!file2.exists()  && !file2.isDirectory()) {     
				file2.mkdirs();
			}
			path2 = rootFileDir + File.separator + fileName;
			System.out.println("1.writerFile path:"+path2);
			newFile2 = newFile(path2);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(newFile2));
			bufferedWriter.write(str);
			bufferedWriter.newLine();
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
	public void uploadFtp() {
		
		exporting();
		
        FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 

        try { 
        	ftpClient.connect(this.cfgDataMap.get("edi_neolink_ftp_host")); 
            boolean login = ftpClient.login(this.cfgDataMap.get("edi_neolink_ftp_sername"), this.cfgDataMap.get("edi_neolink_ftp_possword")); 
            if(!login){
            	this.alert("FTP登录失败！");
            	return;
            }
            System.out.println("3.ftp login"+path2);
            File srcFile = new File(path2); 
            fis = new FileInputStream(srcFile); 
            //设置上传目录 
            //ftpClient.changeWorkingDirectory("/"+this.cfgDataMap.get("edi_neolink_ftp_uploadpath")); 
            //上传路径设置为固定临时文件夹路径
            ftpClient.changeWorkingDirectory(path2); 
            ftpClient.setBufferSize(1024); 
            ftpClient.setControlEncoding("GBK"); 
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            String ftpFileName = randomString+"."+filetype;
            System.out.println("3.5.ftp storeFile start"+ftpFileName);
            ftpClient.storeFile(ftpFileName, fis);
            System.out.println("4.ftp upload file:"+ftpFileName);
            this.alert("上传成功！文件名:"+ftpFileName);
        }catch (SocketException e) {
			this.alert("FTP配置配置错误2！");
			e.printStackTrace();
		}catch (IOException e) { 
        	e.printStackTrace(); 
            throw new RuntimeException("FTP客户端出错！", e);
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
	
	
	
	
	
	 /**
     * JSON(数组)字符串转换成XML字符串
     * （必须引入 xom-1.1.jar）
     * @param jsonString
     * @return
     */
    private String json2xml(String jsonString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setTypeHintsEnabled(false);
        return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
    }
    
    @SaveState
	private Map<String, String> cfgDataMap;
    
	@Bind
	@SaveState
	public boolean isFTP;
	
	
	protected void initData() throws Exception {
		String cfgDataMap [] = {"edi_nbesb_userid","edi_nbesb_apikey","edi_nbesb_url"};
		
		this.cfgDataMap = ConfigUtils.findUserCfgVals(cfgDataMap, AppUtils.getUserSession().getUserid());
		
		userid=(this.cfgDataMap.get("edi_nbesb_userid")==null?"":this.cfgDataMap.get("edi_nbesb_userid"));
		//password=(this.cfgDataMap.get("edi_nbesb_password")==null?"":this.cfgDataMap.get("edi_nbesb_password"));
		apikey=(this.cfgDataMap.get("edi_nbesb_apikey")==null?"":this.cfgDataMap.get("edi_nbesb_apikey"));
		interfaceURL=(this.cfgDataMap.get("edi_nbesb_url")==null?"":this.cfgDataMap.get("edi_nbesb_url"));
	}
	
	@Action
	public void btnSave(){
		try {
			ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
			userid=(this.cfgDataMap.get("edi_nbesb_userid")==null?"":this.cfgDataMap.get("edi_nbesb_userid"));
			apikey=(this.cfgDataMap.get("edi_nbesb_apikey")==null?"":this.cfgDataMap.get("edi_nbesb_apikey"));
			update.markUpdate(true, UpdateLevel.Data, "userid");
			update.markUpdate(true, UpdateLevel.Data, "apikey");
			
			
			
			interfaceURL=(this.cfgDataMap.get("edi_nbesb_url")==null?"":this.cfgDataMap.get("edi_nbesb_url"));
			update.markUpdate(true, UpdateLevel.Data, "interfaceURL");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    /**
	 * FTP配置保存
	 * */
	@Action
	public void savesetFTP(){
		try {
			ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
			
			update.markUpdate(true, UpdateLevel.Data, "sendercode");
			update.markUpdate(true, UpdateLevel.Data, "acceptcode");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public Map<String, String> getCfgDataMap() {
		return this.cfgDataMap;
	}
	
	
	/*public String getURL(){
		String url = "";
		if("testInterface".equals(interfaceURL)){
			url = "http://api.trainer.nbeport.com/router/rest";
		}else{
			url = "http://api.nbeport.com/router/rest";
		}	
		return url;		
	}*/
	
	public String getJson(String id){
		Long userid = AppUtils.getUserSession().getUserid();
		Map<String, String> argsMap = new HashMap<String, String>();
		String urlArgs2= "";
		argsMap.put("jobids", id);
		
		argsMap.put("dockName", dockName);
		argsMap.put("userid", userid.toString());
		urlArgs2 = AppUtils.map2Url(argsMap, ";");
		String sqlQry = "SELECT * FROM f_edi_nbesb('"+urlArgs2+"')";
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
		Object object = m.get("f_edi_nbesb");
		String str = "";
		if(object!=null){
			str = object.toString();
		}
		return str;
	}
	
	@Action
	public void sendJson(){
		try{
			Long id = getGridSelectId();
			String dataJson = getJson(id.toString());
			if(id <= 0){
				MessageUtils.alert("请选择至少一条记录");
				return ;
			}else{
				//dataJson = "[{\"billNo\":\"YHYH0718535033\",\"vesselName\":\"WAN HAI 501\",\"voyageNo\":\"185W\",\"loadPort\":\"NINGBO\",\"loadPortCode\":\"CNNGB\",\"destinationPort\":\"PORT KELANG NORTH\",\"destinationPortCode\":\"MYPKG\",\"deliveryPlace\":\"PORT KELANG NORTH\",\"deliveryPlaceCode\":\"MYPKG\",\"receiptPlace\":\"NINGBO\",\"transitPort\":\"MYPKG\",\"clearingCode\":\"SHARE\",\"carrierCode\":\"CISA\",\"forwarderContacts\":\"小王\",\"forwarderName\":\"测试货代\",\"forwarderTel\":\"88101234\",\"ctnType\":\"22G1\",\"email\":\"248203044@qq.com\",\"costcoNo\":\"NJIS_123123_ASDD\",\"place\":\"宁波\",\"firstForwarderName\":\"中创物流（宁波）有限公司\",\"contactEmail\":\"424208576@qq.com\",\"dataSource\":\"4\",\"cargoList\":[{\"cargoName\":\"书本\",\"grossWeight\":\"15000\",\"otherBillNo\":\"YHYH0718535033\",\"packageType\":\"BAG\",\"qty\":\"150\",\"volume\":\"68\",\"remark\":\"无\"}]},{\"billNo\":\"YHYH0718535033\",\"vesselName\":\"WAN HAI 501\",\"voyageNo\":\"185W\",\"loadPort\":\"NINGBO\",\"loadPortCode\":\"CNNGB\",\"destinationPort\":\"PORT KELANG NORTH\",\"destinationPortCode\":\"MYPKG\",\"deliveryPlace\":\"PORT KELANG NORTH\",\"deliveryPlaceCode\":\"MYPKG\",\"receiptPlace\":\"NINGBO\",\"transitPort\":\"MYPKG\",\"clearingCode\":\"SHARE\",\"carrierCode\":\"CISA\",\"forwarderContacts\":\"小王\",\"forwarderName\":\"测试货代\",\"forwarderTel\":\"88101234\",\"ctnType\":\"22G1\",\"email\":\"248203044@qq.com\",\"costcoNo\":\"NJIS_321321_ASDD\",\"place\":\"宁波\",\"firstForwarderName\":\"中创物流（宁波）有限公司\",\"contactEmail\":\"424208576@qq.com\",\"dataSource\":\"4\",\"cargoList\":[{\"cargoName\":\"书本\",\"grossWeight\":\"15000\",\"otherBillNo\":\"YHYH0718535033\",\"packageType\":\"BAG\",\"qty\":\"150\",\"volume\":\"68\",\"remark\":\"无\"}]}]";
				
				//String url = "http://api.nbeport.com/router/rest";

				String url = interfaceURL;
				String user_id = userid;
				String api_scrt = apikey;
				
				/*String user_id = "SZZS1608";
				String api_scrt = "d05993f3-e352-47db-961e-9c096cc8a99b";*/
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timestamp = sdf.format(new Date());
				
				Map<String, String> map = new TreeMap<String, String>();
				map.put("user_id", user_id);
				map.put("timestamp", timestamp);
				map.put("method", "eportyun.packlist.upload.clp");
				map.put("format", "json");
				map.put("version", "2.0");
				//map.put("data", "[{\"billNo\":\"IAlIN30256\",\"vesselName\":\"VIRGO\",\"voyageNo\":\"V.005W\",\"lineCode\":\"\",\"loadPort\":\"NINGBO\",\"loadPortCode\":\"CNNGB\",\"deliveryPlace\":\"MOMBASA,KENYA\",\"deliveryPlaceCode\":\"KEMBA\",\"destinationPort\":\"MOMBASA,KENYA\",\"destinationPortCode\":\"KEMBA\",\"receiptPlace\":\"\",\"transitPort\":\"KEMWA\",\"clearingCode\":\"SYWL\",\"carrierCode\":\"HPL赫伯罗特\",\"carrier\":\"HPL赫伯罗特\",\"ctnOperatorCode\":\"\",\"forwarderContacts\":\"MYT\",\"forwarderTel\":\"0574-89089857\",\"forwarderName\":\"SYWL\",\"email\":\"myt@sinaocean.com\",\"consignmentNo\":\"SYWL2056185\",\"ctnType\":\"40HC\",\"costcoNo\":\"99LUCPXHHYB_SYWL20561851\",\"firstForwarderName\":\"SYWL\",\"contactEmail\":\"\",\"dataSource\":\"4\",\"bookingOrgName\":\"宁波申洋物流有限公司\",\"clientCode\":\"\",\"carrierType\":\"\",\"cargoType\":\"\",\"place\":\"\",\"ctnLoadRequire\":\"\",\"ctnLoadTime\":null,\"ctnLoadContact\":\"\",\"ctnLoadTel\":\"\",\"ctnRemark\":\"\",\"customerName\":\"ZY\",\"customerTel\":\"0574-87750896\",\"customerEmail\":\"zhuyan@sinaocean.com\",\"documentName\":\"\",\"documentTel\":\"\",\"documentEmail\":\"\",\"closingDate\":\"2020-10-29 20:00:00\",\"cutOffTime\":null,\"isShutOut\":\"N\",\"pickYardName\":\"\",\"dockName\":\"BLCT3\",\"unCode\":\"\",\"tradeFlag\":\"W\",\"fleetName\":\"\",\"contractNo\":\"\",\"contractCustomer\":\"\",\"etd\":\"2020/10/31 00:00:00\",\"bookingOrgCode\":\"HPL赫伯罗特\",\"busClientCode\":\"\",\"yardClosingTime\":null,\"amsClosingTime\":null,\"jobNo\":\"SYWL2056185\",\"sendType\":\"00\",\"vesselNameBig\":\"\",\"voyageNoBig\":\"\",\"billNoBig\":\"HLCUNG12010CKWQ8\",\"cargoList\":[{\"otherBillNo\":\"HLCSIA30256\",\"packageType\":\"CTNS\",\"packageTypeCode\":\"CT\",\"qty\":\"1160\",\"grossWeight\":\"8800\",\"volume\":\"68\",\"cargoName\":\"CHILD SHOES\\r\\nLADIES SHOES\\r\\nBAG\\r\\nHS:850164\",\"remark\":\"N/M\"}]}]");
				map.put("data", dataJson);
				StringBuffer sb = new StringBuffer();
				for (String key:map.keySet()) {
					sb.append(key+"="+map.get(key)+"&");
				}
				sb.append("key="+api_scrt);
				String sign = PostUtil.encodeMD5(sb.toString());
				map.put("sign", sign);
				System.out.println(sb.toString());
				System.out.println(sign);
				try {
					String result = PostUtil.doPost(url, map);
					System.out.print(result);
					MessageUtils.alert(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * FTP不放文件测试
	 * */
	@Action
	public void testUploads2(){
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
		try {
			ftpClient.connect(this.cfgDataMap.get("edi_neolink_ftp_host"));
			boolean login = ftpClient.login(this.cfgDataMap.get("edi_neolink_ftp_sername"), this.cfgDataMap.get("edi_neolink_ftp_possword")); 
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
		shipperinfoIFrame.load("./editrans.xhtml?jobid=" + jobid);
		shipperinfoWindow.show();
		
	}
	
	
	
	@Action
	public void santovaedi(){
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
		try {
			running = running2(jobids.substring(0,jobids.length()-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		MessageUtils.alert("txt文件生成成功");	
	}
	
	
	
	public boolean running2(String jobids) throws Exception{
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			Map<String, String> argsMap = new HashMap<String, String>();
			String urlArgs2= "";
			argsMap.put("jobids", jobids);
			//argsMap.put("sendercode", sendercode);
			//argsMap.put("senddate", senddate.toGMTString());
			//argsMap.put("filetype", filetype);
			argsMap.put("dockName", dockName);
			argsMap.put("userid", userid.toString());
			System.out.println(dockName);
			urlArgs2 = AppUtils.map2Url(argsMap, ";");
			String sqlQry = "SELECT * FROM f_edi_nbesb('"+urlArgs2+"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			Object object = m.get("f_edi_nbesb");
			String str = "";
			if(object!=null){
				str = object.toString();
			}
			if(str.equals("erro")){
				this.grid.reload();
				return false;
			}else{
				this.grid.reload();
				if("xml".equals(filetype)){
					return writerFile(json2xml(str));
				}else{
					return writerFile(str);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Bind(id="fileDownLoad2", attribute="src")
    private InputStream getDownload52() {
		if(StrUtils.isNull(path2)){
			return null;
		}
		InputStream input = null;
    	try {
    		input = new FileInputStream(path2);
    		return input;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}finally{
			
		}
    }
	
	
}
