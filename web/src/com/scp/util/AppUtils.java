package com.scp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.scp.base.AppSessionLister;
import com.scp.base.LMapBase;
import com.scp.base.UserSession;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.LessAuthorizedUsersException;
import com.scp.exception.NoRowException;
import com.scp.exception.NoSessionException;
import com.scp.model.sys.SysGridDef;
import com.scp.service.ServiceContext;

/**
 * @author Neo
 * 
 */
public class AppUtils extends ApplicationUtilBase {


	public static ServiceContext getServiceContext() {
		return (ServiceContext) getBeanFromSpringIoc("serviceContext");
	}

	public static String getContextPath() {
		// HttpServletRequest request = getHttpServletRequest();
		// ApplicationUtils.debug("getRequestURL: "+request.getRequestURL());
		// ApplicationUtils.debug("getRequestURI: "+request.getRequestURI());
		// ApplicationUtils.debug("getQueryString: "+request.getQueryString());
		// ApplicationUtils.debug("getRemoteAddr: "+request.getRemoteAddr());
		// ApplicationUtils.debug("getRemoteHost: "+request.getRemoteHost());
		// ApplicationUtils.debug("getRemotePort: "+request.getRemotePort());
		// ApplicationUtils.debug("getRemoteUser: "+request.getRemoteUser());
		// ApplicationUtils.debug("getLocalAddr: "+request.getLocalAddr());
		// ApplicationUtils.debug("getLocalName: "+request.getLocalName());
		// ApplicationUtils.debug("getLocalPort: "+request.getLocalPort());
		// ApplicationUtils.debug("getMethod: "+request.getMethod());
		// ApplicationUtils.debug("-------request.getParamterMap()-------");
		return getHttpServletRequest().getContextPath();
	}

	public static HttpServletRequest getHttpServletRequest() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		return request;
	}

	public static String getReqParam(String parameterName) {
		Object req = getHttpServletRequest().getParameter(parameterName);
		String ret = req == null ? "" : req.toString();
		return ret;
	}
	
	public static String getReqParamDealChinese(String parameterName) {
		return StrUtils.decode(getReqParam(parameterName));
	}

	public static HttpServletResponse getHttpServletResponse() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		return response;
	}


	public static String getUploadPath() throws Exception {
		String serverName = getContextPath().replace("/", "");
		String path = getWebApplicationPath() + File.separator + "webapps";
		path += File.separator + serverName;
		path += File.separator + "upload" + File.separator;
		FileOperationUtil.newFolder(path);

		return path;
	}

	public static String getAttachFilePath() throws Exception {
		//neo 20210528 此处一般是默认，基本不会改动。schedule里面收取邮件附件部分需要处理附件路径，context是null这里兼容处理
		String serverName = "scp";
		try {
			serverName = getContextPath().replace("/", "");
		} catch (Exception e) {
		}
		
		String path = getWebApplicationPath() + File.separator + "upload";
		FileOperationUtil.newFolder(path);

		path += File.separator + serverName;
		FileOperationUtil.newFolder(path);

		path += File.separator + "attachfile" + File.separator;
		FileOperationUtil.newFolder(path);

		return path;
	}
	
	
	public static HttpSession getHttpSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null){
			throw new NoSessionException("FacesContext is null");
		}
		ExternalContext externalContext = context.getExternalContext();
		HttpSession session = (HttpSession) externalContext.getSession(false);
		return session;
	}

	public static UserSession getUserSession() {
		HttpSession httpSession = getHttpSession();
		if(httpSession == null)
			throw new NoSessionException("HttpSession is null");
		UserSession userSession = (UserSession) httpSession.getAttribute(
				"UserSession");
		if (userSession == null)
			throw new NoSessionException("userSession is null");
		return userSession;
	}

	public static boolean isLogin() {
		UserSession userSession = getUserSession();
		if (userSession == null || userSession.isLogin() == false)
			return false;
		return true;
	}

	/**
	 * 获取网站的BasePath
	 * 
	 * @return 网站的BasePath的字符串
	 */
	public static String getBasePath() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext eContext = context.getExternalContext();
		ServletRequest req = (ServletRequest) eContext.getRequest();
		return (req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + eContext.getRequestContextPath() + "/");
	}

	// 获取帐套信息
	public static String getActsetcode() throws FsActSetNoSelectException {
		return getActsetcode(AppUtils.getUserSession().getActsetid());
	}

	// 重构 NEO
	public static String getActsetcode(Long actsetid) throws FsActSetNoSelectException {
		if(actsetid==null || actsetid <=0)return "";
		return getServiceContext().commonDBCache.getActsetcode(actsetid);
	}

	// 获取帐套信息 不加 year period
	public static String getActsetcode2() {
		Long actsetid = AppUtils.getUserSession().getActsetid();
		if(actsetid==null || actsetid <=0)return "";
		return getServiceContext().commonDBCache.getActsetcode2(actsetid);
	}

	/**
	 * 按客户组过滤客户 处理客户联系人有两个表
	 * 
	 * @return
	 */
	public static String custCtrlClauseWhere() {
		// String sql = "\nAND (EXISTS" +
		// "\n				(SELECT " +
		// "\n					1 " +
		// "\n				FROM sys_custlib_cust y , sys_custlib_user z " +
		// "\n				WHERE y.custlibid = z.custlibid " +
		// "\n				AND y.corpid = a.id " +
		// "\n				AND z.userid = " + AppUtil.getUserSession().getUserid()+ ")" +
		// //xx.linktype = 'C' AND
		// "\n	OR EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = a.id AND xx.userid = "
		// + AppUtil.getUserSession().getUserid()+ ")"+
		// "\n)";
		String customerSQl = "a.id";
		return custCtrlClauseWhere(customerSQl);
	}

	public static String custCtrlClauseWhere(String customerSQl) {
		String sql = "\nAND ((EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
				+ "\n				WHERE y.custlibid = z.custlibid "
				+ "\n				AND y.corpid = "
				+ customerSQl
				+ "\n				AND z.userid = "
				+ AppUtils.getUserSession().getUserid()
				+ ")"
				
				//neo 20170928 增加组关联业务员提取条件 begin--------------------------
				+"\n			OR EXISTS(SELECT 1"
				+"\n					FROM sys_custlib x , sys_custlib_role y "
				+"\n					WHERE y.custlibid = x.id "
				+"\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) "
				+"\n					AND x.libtype = 'S' "
				+"\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = a.salesid)"
				+"\n				)"
				//neo 20170928 增加组关联业务员提取条件end--------------------------
				
				// xx.linktype = 'C' AND
				+"\n	OR EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = "
				+ customerSQl + " AND xx.userid = "
				+ AppUtils.getUserSession().getUserid() + ")" + "\n)"
				+ "\n	OR a.inputer = '"
				+ AppUtils.getUserSession().getUsercode() + "'"
				+ "\n	OR a.salesid = " + AppUtils.getUserSession().getUserid()
				+ ")";
		return sql;
	}

	/**
	 * 原因 ,处理客户联系人有两个表 一个 as a ,x 但是工作单那边是 t
	 * 
	 * @param customerSQl
	 * @return
	 */
	public static String custCtrlClauseWhere2(String customerSQl) {
		String sql = "\nAND ((EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
				+ "\n				WHERE y.custlibid = z.custlibid "
				+ "\n				AND y.corpid = "
				+ customerSQl
				+ "\n				AND z.userid = "
				+ AppUtils.getUserSession().getUserid()
				+ ")"
				
				//neo 20170928 增加组关联业务员提取条件 begin--------------------------
				+"\n			OR EXISTS(SELECT 1"
				+"\n					FROM sys_custlib x , sys_custlib_role y "
				+"\n					WHERE y.custlibid = x.id "
				+"\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) "
				+"\n					AND x.libtype = 'S' "
				+"\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.salesid)"
				+"\n				)"
				//neo 20170928 增加组关联业务员提取条件end--------------------------
				
				// xx.linktype = 'C' AND
				+"\n	OR EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = "
				+ customerSQl + " AND xx.userid = "
				+ AppUtils.getUserSession().getUserid() + ")" + "\n)"
				+ "\n	OR t.inputer = '"
				+ AppUtils.getUserSession().getUsercode() + "'"
				+ "\n	OR t.salesid = " + AppUtils.getUserSession().getUserid()
				+ ")"

		;
		return sql;
	}

//	public static IWorkflowSession getWorkflowSession() {
//		return AppUtils.getServiceContext().runtimeContext.getWorkflowSession();
//	}

	public static String getUserCode(Long userid) {
		String sql = "SELECT code FROM sys_user WHERE isdelete = FALSE AND id = "
				+ userid + "";
		Map m = getServiceContext().daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (String) m.get("code");

	}

	public static String getServerHttPort() {
		String ip = AppUtils.getHttpServletRequest().getLocalAddr();
		int remotePort = AppUtils.getHttpServletRequest().getLocalPort();
		String str = "http://" + ip + ":" + remotePort;
		return str;
	}

	public static String map2Url(Map<String, String> map, String type) {
		String url = "";
		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
		if (":".equals(type)) {
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				url += entry.getKey() + "=" + entry.getValue() + ":";
			}
		} else if(";".equals(type)){
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				url += entry.getKey() + "=" + entry.getValue() + ";";
			}
		} else {// &
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				url += "&" + entry.getKey() + "=" + entry.getValue();
			}
		}
		return url;
	}

	/**
	 * 模块按钮权限
	 * 
	 * @param modulepid
	 *            模块id
	 * @return
	 */
	public static List<String> getUserRoleModuleCtrl(Long modulepid) {
		return getServiceContext().commonDBCache.getUserRoleModuleCtrl(modulepid,getUserSession().getUserid());
	}

	/**
	 * 发送消息通用方法
	 * @param receiveCodes  接收人codes
	 * @param remindTitle 消息提醒标题
	 * @param remindContext 消息提醒内容
	 * @param url 消息超链接
	 * @param sendContext 消息内容
	 * @return
	 */
	public static void sendMessage(List<String> receiveCodes,
			String remindTitle, String remindContext, String url,
			String sendContext) {
		Long uid = getServiceContext().userMgrService.sysUserDao.findById(
				AppUtils.getUserSession().getUserid()).getFmsid();
		sendMessage(uid, receiveCodes, remindTitle, remindContext, url, sendContext);
	}
	
	/**
	 * 重载发送消息通用方法 neo 20161221 uid不从facecontext里面取，线程中取不到context
	 * @param receiveCodes  接收人codes
	 * @param remindTitle 消息提醒标题
	 * @param remindContext 消息提醒内容
	 * @param url 消息超链接
	 * @param sendContext 消息内容
	 * @return
	 */
	public static void sendMessage(Long uid , List<String> receiveCodes,
			String remindTitle, String remindContext, String url,
			String sendContext) {
		if (receiveCodes == null) {
			MessageUtils.alert("接收人为空,请检查!");
			return;
		}
		try {
			String ids = "'";
			for (String rc : receiveCodes) {
				ids += getServiceContext().userMgrService.sysUserDao.findOneRowByClauseWhere("code='" + rc + "'").getFmsid()+ ",";
			}
			ids = ids.substring(0, ids.lastIndexOf(",")) + "'";
//			getServiceContext().dzzService.sendMessage(uid, ids, sendContext,remindTitle, remindContext, url);
		} catch (NoRowException e){
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public static String base64Encoder(String code) {
		String key1 = "";
		for (int i = 0; i < 8; i++) {
			key1 = key1 + (int) (Math.random() * 10);
		}
		String key2 = "";
		for (int i = 0; i < 56; i++) {
			key2 = key2 + (int) (Math.random() * 10);
		}
		code = key1 + code + key2;
		byte[] encode = new byte[code.length()];
		for (int i = 0; i < code.length(); i++) {
			encode[i] = (byte) code.charAt(i);
		}
		return new BASE64Encoder().encodeBuffer(encode).replaceAll("[\\t\\n\\r]", "");
	}

	public static String base64Decoder(String code) {
		if (code == null || code.length() < 88) {
			return code;
		}
		StringBuffer sb = new StringBuffer();
		try {
			byte[] decode = new BASE64Decoder().decodeBuffer(code);
			for (byte b : decode) {
				sb.append((char) b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.length() == 0 ? null : sb.substring(8, sb.length() - 56);
	}

	
	public static String getDzzUrl(){
    	String url = ""; 
    	url = AppUtils.isDebug?"http://192.168.0.143/gsit/":getUserSession().getDzzUrl();
    	return url;
    }
    
    public static String getScpUrl(){
    	String url = ""; 
    	url = AppUtils.isDebug?"http://192.168.0.188:8888/scp":getUserSession().getScpUrl();
    	return url;
    }
    /**
     * table sys_griddef
     * 新增或修改列表排序
     * @param grid grid
     * @param colorder 排序sql如:nos DESC,jobdate ASC
     */
    public static void createOrModifyUserColorder(String grid,String colorder){
    	if(StrUtils.isNull(grid)){
    		return;
    	}
    	Long userid = getUserSession().getUserid();
    	SysGridDef sysGridDef = null;
    	try {
    		sysGridDef = getServiceContext().sysGridDefService.sysGridDefDao.findOneRowByClauseWhere("userid = " + userid + " AND gridid = '"+grid+"'");
    		sysGridDef.setColorder(colorder);
    		getServiceContext().sysGridDefService.sysGridDefDao.modify(sysGridDef);
		} catch (NoRowException e) {//insert
			sysGridDef = new SysGridDef();
			sysGridDef.setColorder(colorder);
			sysGridDef.setGridid(grid);
			sysGridDef.setUserid(userid);
			getServiceContext().sysGridDefService.sysGridDefDao.create(sysGridDef);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * table sys_griddef
     * 获取列表排序
     * @param grid grid
     * @param colorder 排序sql如:nos DESC,jobdate ASC
     * @return colorder
     */
    public static String getUserColorder(String grid){
    	if(StrUtils.isNull(grid)){
    		return null;
    	}
    	try {
    		Long userid = getUserSession().getUserid();
    		return getServiceContext().sysGridDefService.sysGridDefDao.findOneRowByClauseWhere("userid = " + userid + " AND gridid = '"+grid+"'").getColorder();
		} catch (NoRowException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
    }
    
	//public static String rptUrl;
	
	/**
	 * 替换textarea文本框中除A-Za-z0-9,_,\u4E00-\u9FA5编码的中文和空格,大部分英文中文符号外字符
	 * @param in
	 * @return
	 */
	public static String replaceStringByRegEx(String in){
//		return in.replaceAll("[^\\w\\u4E00-\\u9FA5\\s\\.,:\\-\\+\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\=\\[\\]\\{\\}\\;\\'\\\\\\?\\|\\/\\<\\>\\\"\\`\\·\\！\\￥\\（\\）\\【\\】\\《\\》āáǎàōóǒòêēéěèīíǐìūúǔùǖǘǚǜüńňǹ]*", "");
		//Ş，Ö ，İ，Ü上面的正则会把这几个字符过滤掉，所以暂时改成替换所有存在unicode中的字符
		return in.replaceAll("[^\u0000-\uffff]","").replaceAll("[\u2000-\u206F]","");//u2000-\u206F 为不可见字符编码范围
	}
	
	/**
	 * 最开始写在spring配置，那个改了要重启。后来写到系统设置，然后现在是个人设置和系统设置要并存，个人设置优先
	 */
	public static String getRptUrl(){
		String rptUrl = ConfigUtils.findUserSysCfgVal("rpt_srv_url", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(rptUrl)){
			rptUrl = "/scp";
		}
		return rptUrl;
	}
	
	public static boolean checkUserCount() {
		int count = getSysUser();
//		String sql = "SELECT count(DISTINCT sessionid) AS nlinenumber from  sys_useronline WHERE isdelete = false AND isonline = 'Y' AND isvalid = TRUE  " ;
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
//		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		String nlinenumber = m.get("nlinenumber").toString();
		
		int currentUserCount = getOnLineUser();
		if(currentUserCount >= count){
			String sql = "SELECT string_agg(logintime || ':'|| u.code , '\n') AS userlists " +
					"\nfrom sys_useronline o , sys_user u" +
					"\nWHERE o.isdelete = false " +
					"\nAND o.isonline = 'Y' AND o.userid = u.id" +
					"\nAND o.isvalid = TRUE";
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String userlists = StrUtils.getMapVal(m, "userlists");
			throw new LessAuthorizedUsersException("授权用户数不足[当前在线总用户数："+currentUserCount+"][授权总用户数："+count+"]，请联系航迅软件申请添加用户！\n"+userlists);
		}
		return true;
	}
	
	public static int getOnLineUser(){
		return AppSessionLister.getOnLineUser();
	}
	
	/**
	 * 通用列表数据按选择的分公司过滤数据
	 * @param map
	 * @param tblname
	 * @return Map $qry$
	 */
	public static Map filterByCorperChoose(Map map , String tblname) {
		String qry = (String)map.get("qry");
		qry += "\nAND "+tblname+".corpid =ANY(SELECT DISTINCT corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")";
		map.put("qry", qry);
		return map;
	}
	
	//获取报表文件路径
	public static String getReportFilePath() throws Exception {
		// 读取配置初始化数据源
		File file = null;
		// WEB-INF\classes
		file = new File(Application.class.getResource("/").toURI());
		// web-inf
		File rootDir = file.getParentFile();

		// reportFiles
		rootDir = FileOperationUtil.findPathByFolderName(file.getPath(), "reportFiles",true);

		if (rootDir == null) {
			throw new Exception("Can not find folder:'reportFiles'");
		}
		return rootDir.getAbsolutePath();
	}

	//获取导入报表模板
	public static String getImportTemplateFilePath() throws Exception {
		// 读取配置初始化数据源
		File file = null;
		// WEB-INF\classes
		file = new File(Application.class.getResource("/").toURI());
		// web-inf
		File rootDir = file.getParentFile();

		// upload
		rootDir = FileOperationUtil.findPathByFolderName(file.getPath(), "upload",true);

		if (rootDir == null) {
			throw new Exception("Can not find folder:'upload'");
		}
		return rootDir.getAbsolutePath();
	}
	
	//获取提单文件路径
	public static String getHblReportFilePath() throws Exception {
		// 读取配置初始化数据源
		File file = null;
		// WEB-INF\classes
		file = new File(Application.class.getResource("/").toURI());
		// web-inf
		File rootDir = file.getParentFile();

		// reportFiles
		rootDir = FileOperationUtil.findPathByFolderName(file.getPath(), "reportEdit",true);

		if (rootDir == null) {
			throw new Exception("Can not find folder:'reportFiles'");
		}
		return rootDir.getAbsolutePath();
	}
	
	
	 public static void main(String[] args) {
		 
//			url: "http://ufms.vip:8280/webchat/service?src=qq&action=send&sendid="+<%=userid%>+"&receiveid="+talkTo+"&msg="+textarea+"",
//			
//			.sendPost("http://localhost:8080/test/post", 
//			           "title="+newString(java.net.URLEncoder.encode(title,"utf-8").getBytes(),"ISO-8859-1")+"); " +
			
			
	}
	 
	public static void sendIMMsg(String sendid, String receiveid , String msg) {
		//"title="+newString(java.net.URLEncoder.encode(title,"utf-8").getBytes(),"ISO-8859-1")+"); " 
		String baseUrl = CfgUtil.findSysCfgVal("sys_public_url");
		String url = "http://ufms.cn/webchat/service";
		//if(isDebug){
			//url = "http://ufms.vip:8280/webchat/service";
		//}
		try {
			msg = URLEncoder.encode(URLEncoder.encode(msg, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		url= baseUrl + "/scp/webChat?method=process";
		String csno = CfgUtil.findSysCfgVal("CSNO");
		String param = "src=qq&action=send&sendid="+sendid+"&receiveid="+receiveid+"&msg="+msg+"&csno="+csno;
		AppUtils.sendPost(url, param);
	}
	
	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @paramurl 发送请求的URL
	 *@returnURL所代表远程资源的响应
	 */
	public static String sendPost(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//设置超时
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		}catch(SocketTimeoutException e){
			e.printStackTrace();
			MessageUtils.alert("请求超时！");
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * @param url 发送请求的URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + (param);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Authorization","Bearer 9d38b8f4-7be8-43b7-bf99-34f5f11d2242");
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			//设置超时
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				//System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 判断当前session中对应的语言，返回中文或英文栏目
	 * @param zh
	 * @param en
	 * @return
	 */
	public static String getColumnByCurrentLanguage(String zh,
			String en) {
		MLType mlType = AppUtils.getUserSession().getMlType();
		return mlType.equals(LMapBase.MLType.en)?en:zh;
	}

	public static boolean isContainChinese(String str) {
		String regex = "[\u4e00-\u9fa5]";   //汉字的Unicode取值范围
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        return match.find();
	}
	
	//半角转全角
	 public static String half2Full(String value) {
        if (StrUtils.isNull(value)) {
            return "";
        }
    	char[] cha = value.toCharArray();
        for (int i = 0; i < cha.length; i++) {
            if (cha[i] == 32) {
                cha[i] = (char) 12288;
            } else if (cha[i] < 127) {
                cha[i] = (char) (cha[i] + 65248);
            }
        }
        return new String(cha);
    }
	 
	 public static String modelBeanToJSON(final List<String> filterField, List<?> list){ 
		 JSONArray jsonObjects = new JSONArray();
		 int i=0;
		 Iterator<?> it = list.iterator();
		 while(it.hasNext()){
			 JSONObject jsonObject = new JSONObject();
			 Object obj = it.next();//这里的Obj对象只是list中的存放的对象类型
			 JsonConfig jsonConfig = new JsonConfig(); 
			  
			 PropertyFilter filter = new PropertyFilter() { 
			 public boolean apply(Object source, String name, Object value) { 
				 boolean isFiltered=false;
				 for(String string:filterField){
					 if(string.equals(name)){
						 isFiltered=true;
					 }
				 }
				 if (isFiltered) { 
				 return true;
				 } 
				 	return false; 
				 } 
			 }; 
			 jsonConfig.setJsonPropertyFilter(filter);
			 jsonConfig.registerJsonValueProcessor(java.util.Date.class,new JsonDateValueProcessor());
			 jsonObject=JSONObject.fromObject(obj, jsonConfig);
			 jsonObjects.add(i++, jsonObject);
		}
		 return jsonObjects.toString();
	}
	 
	 /**
	 * 向马士基指定URL发送GET方法的请求
	 * @param url 发送请求的URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGetToMSJ(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + (param);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpsURLConnection connection = (HttpsURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			//connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			String sys_maersk_qryprice_key = ConfigUtils.findSysCfgVal("sys_maersk_qryprice_key");
			if(StrUtils.isNull(sys_maersk_qryprice_key)){
				sys_maersk_qryprice_key = "Atmosphere atmosphere_app_id=evertrust-2ktuWjuvUadPGYNDHXCbA64a5Tx5FFsT";
			}
			connection.setRequestProperty("Authorization", sys_maersk_qryprice_key);//马士基秘钥
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			
			 //创建SSL对象
            TrustManager[] tm = {new X509TrustManager(){
				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
            }};
            //SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            connection.setSSLSocketFactory(ssf);
			
			//设置超时
			connection.setConnectTimeout(500000);
			connection.setReadTimeout(500000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			//Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			//for (String key : map.keySet()) {
				//System.out.println(key + "--->" + map.get(key));
			//}
			int code = connection.getResponseCode();
			//System.out.println("connection.getResponseCode:"+code); 
			//System.out.println("connection.getResponseMessage:"+connection.getResponseMessage()); 
			// 定义 BufferedReader输入流来读取URL的响应
			if(200==code){
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}else{
				InputStream errorStream = connection.getErrorStream();
				in = new BufferedReader(new InputStreamReader(errorStream));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				System.out.println("错误返回码"+code);
				System.out.println(result);
				JSONObject fromObject = JSONObject.fromObject(result);
				MessageUtils.alert(fromObject.getString("message"));
			}
			//
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
}
