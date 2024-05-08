package com.scp.view.base;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Base64;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;
import org.operamasks.faces.component.widget.ExtConfig;
import org.operamasks.faces.component.widget.UIToolBar;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.sys.SysLogDao;
import com.scp.exception.NoRowException;
import com.scp.exception.NoSessionException;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysUser;
import com.scp.service.LoginService;
import com.scp.service.sysmgr.SysMessageService;
import com.scp.util.AppUtils;
import com.scp.util.CfgUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.util.TagPanelUtil;
import com.ufms.base.utils.Md5Util;

@ManagedBean(name = "main.indexBean", scope = ManagedBeanScope.REQUEST)
public class IndexBean {

	

	@ManagedProperty("#{loginService}")
	public LoginService loginService;

	@ManagedProperty("#{sysMessageService}")
	public SysMessageService sysMessageService;
	
	
	@Bind
	public UITree navTree;

	@Bind
	public boolean tipStatus;
	
	@Inject(value = "l")
	private MultiLanguageBean l;

	@Bind
	public UIIFrame showMessageIframe;
	
	@Bind
	@SaveState
	public Long userid = 0l;
	
	@Bind
	@SaveState
	public String csno = "";

	@Action
	public void openNavTreeAjax() {
		String index = (String) AppUtils.getReqParam("index");
		TreeDataProvider dataProvider = new TreeDataProvider();
		dataProvider.initTreeAll(l);
		navTree.setValue(dataProvider);
		// navTree.setRootVisible(false);
		navTree.setLoadAllNodes(true);
		navTree.repaint();
		// update.markUpdate("navTree");
	}

	/**
	 * 绑定页面上的ToolBar组件
	 */
	@Bind
	public UIToolBar toolBar;

	@Bind
	public UITabLayout centerPanel;

	@Bind
	public UIPanel center;
	
	@Bind
	public UIWindow WindowShowMessage;

	@Inject(value = "constantBean.getContextPath()")
	protected String contexPath;
	
	@Bind
	@SaveState
	public String isSaas = "";
	
	@Bind
	@SaveState
	public String bgcolor = "";
	
	@Bind
	@SaveState
	public Long intervaltime;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			
			
	        
			
			//String ver = AppUtils.getReqParam("version");
			//if(ver.equals("false")){
			//	WindowShowMessage.show();
			//}
			try {
				if (!AppUtils.isLogin()) {
					Browser.execClientScript("openNewFrame('../login/login.faces');");
					return;
				}
			} catch (Exception e) {
				// e.printStackTrace();
				Browser.execClientScript("openNewFrame('../login/login.faces');");
				return;
			}
			// SkinManager.getInstance(ctxt).setDefaultSkin("green");
			 
			isSaas = ConfigUtils.findSysCfgVal("sys_cfg_saas");
			csno = ConfigUtils.findSysCfgVal("CSNO");
			if(!StrUtils.isNull(isSaas) && isSaas.equals("Y")){//SAAS模式下不接收消息且影藏图片
				Browser.execClientScript("$('#activemqImg').hide()");
			}
			
			long startTime=System.currentTimeMillis();
	        //System.out.println("执行代码块/方法");
	        
			TreeDataProvider dataProvider = new TreeDataProvider();
			dataProvider.initTreeAll(l);
			
			long endTime=System.currentTimeMillis();
	        //System.out.println("程序运行时间： "+(endTime - startTime)+"ms");
	        
			navTree.setValue(dataProvider);
			// navTree.repaint();
			this.desktop();
			tipStatus = false;
			userid = AppUtils.getUserSession().getUserid();
			
			String str = ConfigUtils.findUserSysCfgVal("subscribe_interval_time", AppUtils.getUserSession().getUserid());
			if(StrUtils.isNull(str)){
				intervaltime = 30L;
			}else{
				Long size = 30l;
				try {
					size = Long.parseLong(str);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				intervaltime = size;
			}
			update.markUpdate(UpdateLevel.Data,"intervaltime");
			update.markUpdate(UpdateLevel.Data,"csno");
			update.markUpdate(UpdateLevel.Data,"tipStatus");
			
			bgcolor = ConfigUtils.findUserSysCfgVal("bg_color", AppUtils.getUserSession().getUserid());
			update.markUpdate(UpdateLevel.Data,"bgcolor");
		}
	}

	/**
	 * 绑定备忘录按钮点击事件
	 */
	@Action(id = "desktop")
	public void desktop() {
//		centerPanel.addTab(l.find("我的工作台"), AppUtils.getContextPath()
//				+ "/pages/sysmgr/user/myworkbench.xhtml", "commontabico_tpcls",
//				config2);
		// centerPanel.addTab(l.find("导航"),
		// "../../../pages/module/main/desktop.xhtml",
		// "commontabico_tpcls", config2);
		// centerPanel.setActiveTab(0);
		//if(AppUtils.isDebug){
			ExtConfig config2 = new ExtConfig();
			config2.set("border", false);
			config2.set("fit", true);
			
			try {
				MLType mlType = AppUtils.getUserSession().getMlType();
				String sysbpm = ConfigUtils.findSysCfgVal("sys_bpm");
				if(StrUtils.isNull(sysbpm))sysbpm = "BPM";
				
				centerPanel.addTab("Tools", AppUtils.getContextPath() + "//common/logoChange.htm?userid="+AppUtils.getUserSession().getUserid()+"&usercode="+AppUtils.getUserSession().getUsercode()+"&language="+mlType+"&sysbpm="+sysbpm, "commontabico_tpcls", config2);
				
				centerPanel.addTab("Dash Board", AppUtils.getContextPath() + "/main/dashboard.jsp?userid="+AppUtils.getUserSession().getUserid()+"&usercode="+AppUtils.getUserSession().getUsercode()+"&language="+mlType+"&sysbpm="+sysbpm, "commontabico_tpcls", config2);
			} catch (NoSessionException e) {
				//e.printStackTrace();
				System.out.println("HttpSession is null..........");
			}
		//}
	}
	
	@Resource
	public SysLogDao sysLogDao;

	@Action
	public void navTree_onclick() {
		UITreeNode node = navTree.getEventNode();
		if (node != null && node.getUserData() != null) {
			TreeNode selectedNode = (TreeNode) node.getUserData();

			String url = selectedNode.getUrl();
			if (StrUtils.isNull(url))
				return;
			// centerPanel.addTab(selectedNode.getName(), url, TagPanelUtil
			// .getTabPanelIconCls(selectedNode.getIcon()), config2);
			String icon = selectedNode.getIcon();
			String name = selectedNode.getName();
			addTab(name, url, icon);
			
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc("OpenModule:"+name);
			syslog.setLogtime(new Date());
			syslog.setLogtype("BI");
			sysLogDao.create(syslog);
			
		}
	}

	public void addTab(String tabName, String url, String ico) {
//		ico = AppUtils.getContextPath() + "/main/img/tools.png";
		
//		ExtConfig config = new ExtConfig();
		ExtConfig config2 = new ExtConfig();
		config2.set("border", false);
		config2.set("fit", true);
//		static {
//			config.set("closable", false);
//			config.set("border", false);
//			config.set("fit", true);
			config2.set("border", false);
			config2.set("fit", true);
//		}

		if ("委托单".equals(tabName) || "箱量统计新".equals(tabName) || "利润统计新".equals(tabName) || "应收应付统计新".equals(tabName)) {
			url += "?userid=" + AppUtils.getUserSession().getUserid();
		}
		String iconClass = TagPanelUtil.getTabPanelIconCls(ico);
		centerPanel.addTab(tabName, url, iconClass, config2);
	}

//	public static void main(String[] args) {
//		String iconClass = TagPanelUtil.getTabPanelIconCls("./img/tools.png");
//		//AppUtils.debug(iconClass);
//	}

	@Inject
	private PartialUpdateManager update;

	@Bind
	public UIIFrame contentPanelFrame;

	@Bind(id = "contentPanelFrame", attribute = "src")
	public String contentSrc = null;

	private void openWindow(String openUrl) {
		contentSrc = contexPath + "/common/blank.html";
		long winId = System.currentTimeMillis();
		update.markAttributeUpdate(contentPanelFrame, "src");
		Browser.execClientScript("window.open('" + openUrl + "','" + winId
				+ "','toolbar=no, menubar=no, "
				+ "scrollbars=yes, resizable=yes, location=no, status=no');");
	}

	private String userInfo;

	public String getUserInfo() {
		String usercode = "un login!";
		try {
			if (AppUtils.isLogin()) {
				if(l.m.getMlType()==MLType.en){
					usercode = AppUtils.getUserSession().getUsercode();
				}else if(l.m.getMlType()==MLType.ch){
					usercode = AppUtils.getUserSession().getUsername();
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		userInfo = usercode + "";
		return userInfo;
	}

//	private String tipInfo;
//
//	public String getTipInfo() {
//		String usercode = "un login!";
//		try {
//			if (AppUtils.isLogin()) {
//				if(l.m.getMlType()==MLType.en){
//					usercode = AppUtils.getUserSession().getUsercode();
//				}else if(l.m.getMlType()==MLType.ch){
//					usercode = AppUtils.getUserSession().getUsername();
//				}
//			}
//		} catch (Exception e) {
//			// e.printStackTrace();
//		}
//		tipInfo = usercode + "";
//		return tipInfo;
//	}

	public void openTab(String code) {

	}
	
	@Action
	public void showUserConfig() {
		Browser.execClientScript("showUserInfo();");
	}

	@Action
	public void help() {
		String openUrl = "http://note.youdao.com/noteshare?id=07b4e59a76cc8b72eb617c3e03b0c3c3";
//		Browser.execClientScript("window.open('" + openUrl + "','" + "_help"
//				+ "','toolbar=no, menubar=no, "
//				+ "scrollbars=yes, resizable=yes, location=no, status=no');");
		AppUtils.openWindow(System.currentTimeMillis()+"", openUrl);
	}
	
	@Action(id = "logout")
	public String logout() {
		try {
			String fromUrl = AppUtils.getUserSession().getFromUrl();
			if(!StrUtils.isNull(fromUrl)){
				//AppUtils.getHttpServletResponse().sendRedirect(fromUrl);
				FacesContext.getCurrentInstance().getExternalContext().redirect(fromUrl);
				//AppUtils.getHttpServletResponse().sendRedirect(fromUrl);
				//FacesContext.getCurrentInstance().responseComplete();
				return null;
			}
			return loginService.logout();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "view:redirect:login";
	}
	
	@Bind
	public UIWindow showWindow;
	
	@Bind
	public UIWindow getNoticeWindow;
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Bind
	private UIIFrame getNoticeIFrame;
	
	@Action(id = "changeCorp")
	public void changeCorp() {
		dtlIFrame.setSrc("/pages/sysmgr/user/userchangecorp.aspx?id="+AppUtils.getUserSession().getUserid());
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlIFrame);
		showWindow.setTitle(l.find("切换公司"));
		showWindow.show();
	}
	

//	@Action
//	public void refreshTips() {
//		AutoTips autoTips = sysMessageService.findAutoTips();
//		if (autoTips == null)
//			return;
//		String titleVal = autoTips.getTitle();
//		String contextVal = autoTips.getContex();
//		Long id = autoTips.getId();
//		Browser.execClientScript("showTimeoutTip('" + titleVal + "','"
//				+ contextVal + "'," + id + ");");
//	}
	
//	@Action
//	public void refreshTips() {
//		AutoTips autoTips = this.findAutoTips1();
//		if (autoTips == null)
//			return;
//		String titleVal = autoTips.getTitle();
//		String contextVal = autoTips.getContex();
//		Long id = autoTips.getId();
//		Browser.execClientScript("showTimeoutTip('" + titleVal + "','"
//				+ contextVal + "'," + id + ");");
//	}

	/*
	 * 设置不再提示（已读）
	 */
	@Action
	public void noPromptAction() {
		String ischeck = AppUtils.getReqParam("ischeck");//不再提示的勾选值
		String msgid = AppUtils.getReqParam("msgid");
		Long userid = AppUtils.getUserSession().getUserid();
		String msgSql = "SELECT msgtype FROM sys_message WHERE id = " + msgid;
		Map m = AppUtils.getServiceContext().daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(msgSql);
		String msgtype = "G";// 默认为公共消息
		if (m != null) {
			msgtype = m.get("msgtype").toString();
		}
		String sql = "";
		if ("G".equals(msgtype)) {
			// 如果是公共消息点击了不再提示，则在sys_message_ref 补一个记录，并标记为已读
			sql = "INSERT INTO sys_message_ref (id,messageid,userid,isread) VALUES(getid(),"
					+ msgid + "," + userid + ",TRUE)";
		} else if ("P".equals(msgtype)) { //如果是个人消息，在sys_message_ref对应的标记为已读
			sql = "UPDATE sys_message_ref SET isread = " + ischeck
					+ " WHERE messageid = " + msgid + " AND userid = " + userid
					+ ";";
		}

		try {
			if (!"".equals(sql)) {
				AppUtils.getServiceContext().sysMessageService.sysMessageDao
						.executeSQL(sql);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

//	public AutoTips findAutoTips1() {
//		AutoTips autoTips = new AutoTips();
//		String sql = "(SELECT m.* FROM sys_message m WHERE EXISTS(SELECT 1 FROM sys_message_ref WHERE userid = "
//			+ AppUtils.getUserSession().getUserid()+" AND messageid = m.id AND isread = FALSE) AND m.isdelete = false AND m.msgtype='P' ORDER BY m.inputtime DESC)";
//		sql += "\nUNION ALL\n(SELECT m.* FROM sys_message m WHERE NOT EXISTS(SELECT 1 FROM sys_message_ref WHERE userid = "
//			+ AppUtils.getUserSession().getUserid()+" AND messageid = m.id AND isread = TRUE) AND m.isdelete = false AND m.msgtype='G' ORDER BY m.inputtime DESC)";
//		List<Map> list = AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql(sql);
//		
//		if(list == null || list.size() < 1)return null;
//		Map sysMessageMap = list.get(0);
//		autoTips.setTitle(String.valueOf(sysMessageMap.get("title")));
//		String text = dealHtml(String.valueOf(sysMessageMap.get("content")));
//		autoTips.setContex(text);
//		autoTips.setUser(String.valueOf(sysMessageMap.get("inputer")));
//		autoTips.setUrl(String.valueOf(sysMessageMap.get("msgurl")));
//		autoTips.setId(Long.valueOf(String.valueOf(sysMessageMap.get("id"))));
//		return autoTips;
//	}

//	private String dealHtml(String html) {
//		html = html.replaceAll("<br/>", "\n");
//		Document doc = Jsoup.parse(html);
//		String text = doc.body().text();
//		text = text.trim();
//		if (text.length() > 90) {
//			text = text.substring(0, 90);
//			text += "...";
//		}
//		return text;
//	}

	// public void message1(){
	//		
	// centerPanel.addTab(l.find("短信"),this.contentSrc,
	// "commontabico_tpcls", config2);
	//
	// }
	@Action(id = "message")
	public void message() {
		String openUrl = AppUtils.getContextPath()
				+ "/pages/sysmgr/sms/smssend.xhtml";
		Browser.execClientScript("window.open('" + openUrl + "','" + "_message"
				+ "','toolbar=no, menubar=no, "
				+ "scrollbars=yes, resizable=yes, location=no, status=no');");
	}

	@Action(id = "email")
	public void email() {
		String openUrl = AppUtils.getContextPath()
				+ "/pages/sysmgr/mail/emailsend.xhtml";
		Browser.execClientScript("window.open('" + openUrl + "','" + "_email"
				+ "','toolbar=no, menubar=no, "
				+ "scrollbars=yes, resizable=yes, location=no, status=no');");

	}
	
	@Autowired
	public ApplicationConf applicationConf;
	
	
	@Bind
	public String tipsMessageContent;
	
	@Bind
	public String tipsMessageTitle;
	
	
	@Action(id = "waitforrqa")
	public void waitforrqa() {
		String sysbpm = ConfigUtils.findSysCfgVal("sys_bpm");
		if("BPM".equals(sysbpm)){
			addTab(l.find("待办"), "/scp/bpm/bpmtasktodo.xhtml", "./image/file2.png");
		}else{
			//MLType mlType = AppUtils.getUserSession().getMlType();
			addTab(l.find("付款申请(待办)"), "/pages/ff/todo/payapplytodo.xhtml", "./image/file2.png");
		}
	}
	
	@Action(id = "waitforgoodsa")
	public void waitforgoodsa() {
		//MLType mlType = AppUtils.getUserSession().getMlType();
		addTab(l.find("取单放货申请"), "/pages/ff/todo/releasebill.xhtml", "./img/ccquery.png");
	}
	
	private String version ;
	public String getVersion() {
		String versions = ConfigUtils.findSysCfgVal("Cli_Version");
		return versions!=null?versions:"";
	}
	
	/**
	 * 将UFMSURL 地址，拼上参数uid=NDExOA==&pwd=ED5385 作为这个链接的地址，然后点击的时候，整个页面重定向到这个url
	 * uid=当前账号对应的pkid_remote 用base64编码
	 * pwd=uid md5后，取3-9位值
	 * */
	@Action
	public void openUfms(){
		String openUrl = ConfigUtils.findSysCfgVal("ufms_url");
		String uid = "";
		String pwd = "";
		String companyid = "-1";
		SysUser user = AppUtils.getServiceContext().userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
		if(user!=null&&user.getPkid_remote()!=null){
			try{  
	            byte[] encodeBase64 = Base64.encodeBase64(user.getPkid_remote().getBytes("UTF-8"));  
	            uid = new String(encodeBase64); 
	            pwd = (Md5Util.MD5AddSalt(user.getPkid_remote())).substring(3, 9);
	            if(!StrUtils.isNull(user.getSysCorporation().getPkidRemote())){;
	            	companyid = user.getSysCorporation().getPkidRemote();
	            }
	        } catch(Exception e){  
	            e.printStackTrace();  
	        }  
		}
		String url = openUrl +"?companyid="+companyid+"&uid=" +uid+"&pwd="+pwd;
//		System.out.println(url);
		Browser.execClientScript("window.open('" + url+ "');");
	}
	
	
	
	//CommonDBCache l2 = (CommonDBCache) AppUtils.getBeanFromSpringIoc("commonDBCache");
	
	@Action
	public void timerjudge(){
		waitfor();
	}
	
	@Bind
	public String waitforrq = "";
	
	
	@Bind
	public String waitforgoods = "";
	
	public String getwaitforgoods() {
		try {
			if (AppUtils.isLogin()) {
				String sql = null;
				String sysbpm = ConfigUtils.findSysCfgVal("sys_bpm");
				if("BPM".equals(sysbpm)){
					 sql = "";
					 waitforgoods="";
					 return waitforgoods;
				}else{
					 sql = "SELECT COUNT(1) AS counts FROM _wf_jobs t WHERE t.workitemState IN (0,1) AND t.processId = 'ReleaseBillProcess'"
						+"\nAND (actor = '" + AppUtils.getUserSession().getUsercode() +"'" +")";
					 //" OR processcreatorid = '"+AppUtils.getUserSession().getUsercode()+"')";
				}
				Map map = AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map.get("counts").toString().equals("0")){
					waitforgoods="";
				}else{
					waitforgoods = l.find("放货") + "(" + map.get("counts") + ")";
				}
			}
		} catch(NoRowException e){
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return waitforgoods;
	}
	
	public String getwaitforrq() {
		try {
			if (AppUtils.isLogin()) {
				String sql = null;
				String sysbpm = ConfigUtils.findSysCfgVal("sys_bpm");
				if("BPM".equals(sysbpm)){
					 sql = "select "
						 + "\n			 	count(1) "
						 + "\n			FROM  (select 	"
						 + "\n					 t.actorid,state,instancestate"
						 + "\n	 			FROM  _bpm_task t LEFT JOIN fina_jobs j ON(t.refid::BIGINT=j.id AND j.isdelete = false AND j.isclose = FALSE))  as T"
						 + "\n			WHERE "
						 + "\n				1=1" 
						 + "\n	AND actorid ILIKE ('%'||'" + AppUtils.getUserSession().getUsercode() +"'||'%')AND ((CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'bpm_task_filterbyorg' AND val = 'Y') THEN"
						 + "\n		t.actorid = ANY(ARRAY(WITH RECURSIVE rc AS ("
						 + "\n			SELECT * FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()
						 + "\n			UNION "
						 + "\n			SELECT a.* FROM sys_user a,rc WHERE  a.parentid = rc.id"
						 + "\n		)"
						 + "\n		SELECT code FROM rc ))"
						 + "\n	ELSE 1=1 END)"
						 + "\n	 OR EXISTS(SELECT 1 FROM sys_userinrole a,sys_role b WHERE a.roleid =  b.id AND a.userid = "+AppUtils.getUserSession().getUserid()+" AND b.code = 'admin_branch'))"
						 + "\n			AND t.state != 9 AND t.instancestate != 9"
						 + "\n				AND t.state != 3"
						 + "\n				AND t.state != 8";
				}else{
					sql = "SELECT count(1) FROM _wf_payreq j WHERE j.workitemState IN (0,1) AND j.processId = 'PaymentRequest'"
						+"\nAND (actor = '" + AppUtils.getUserSession().getUsercode() +"' " + ") AND suspended = FALSE";
					//"OR processcreatorid = '"+AppUtils.getUserSession().getUsercode()+"')";
				}
				Map map = AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map.get("count").toString().equals("0")){
					waitforrq = "" ;
				}else{
					if("BPM".equals(sysbpm)){
						waitforrq = l.find("任务") + "(" + map.get("count") + ")";
					}else{
						waitforrq = l.find("付款") + "(" + map.get("count") + ")";
					}
				}
			}
		} catch(NoRowException e){
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return waitforrq;
	}
	
//	public String time = "";
//	
//	public String gettime() {
//		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		time = dateformat.format(new Date());
//		return time;
//	}
	@Action
	public void waitfor(){
		getwaitforgoods();
		getwaitforrq();
//		gettime();
		gettobedealt();
		update.markUpdate(true, UpdateLevel.Data, "waitforPanel");
		Browser.execClientScript("processTipsShowUp()");
	}
	
	@Bind
	public String tobedealt = "";
	
	public String gettobedealt(){
		getwaitforgoods();
		getwaitforrq();
		if(waitforgoods.equals("")&&waitforrq.equals("")){
			tobedealt = "";
		}else{
			tobedealt = l.find("待办") + ":";
		}
		return tobedealt;
	}
	
	@Bind
	public String ufmsurl = "";
	public String getufmsurl(){
		String ufms_url = ConfigUtils.findSysCfgVal("ufms_url");
		if(StrUtils.isNull(ufms_url)){
			return "";
		}
		return l.find("运价管理");
	}
	
	public String getFromurl(){
		return AppUtils.getUserSession().getFromUrl();
	}
	
	@Action
	public void activemqAction(){
		//System.out.println("点击聊天图标");
		MLType mlType = AppUtils.getUserSession().getMlType();
		String openUrl = AppUtils.getContextPath()+ "/im/im-ufms.jsp";
		String csno = CfgUtil.findSysCfgVal("CSNO");
		getNoticeIFrame.setSrc(openUrl + "?userid="+AppUtils.getUserSession().getUserid()+ "&usernamec="+AppUtils.getUserSession().getUsername()+"&isSaas="+isSaas + "&csno=" + csno+"&language="+mlType);
		update.markAttributeUpdate(getNoticeIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, getNoticeIFrame);
		getNoticeWindow.setTitle("UFMS Web Chat");
		getNoticeWindow.show();
		
//		Browser.execClientScript("window.open('" + openUrl + "?userid="+AppUtils.getUserSession().getUserid()+ "&usernamec="+AppUtils.getUserSession().getUsername()+"','" 
//		+ "_message"+ "','toolbar=no, menubar=no, "+ "scrollbars=yes, resizable=yes, location=no, status=no');");
	}
	
	@Action
	public void getNoticeWindowclose(){//关闭window后摇重新设置iform的url，不然消息网页里的触发器还在请求
		String openUrl = AppUtils.getContextPath()+ "/pages/module/common/blank.html";
		getNoticeIFrame.setSrc(openUrl);
		update.markAttributeUpdate(getNoticeIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, "getNoticeWindow");
		update.markUpdate(true, UpdateLevel.Data, getNoticeIFrame);
		getNoticeWindow.close();
	}
	
	
	@Action
	public void winUI(){
		String csno = ConfigUtils.findSysCfgVal("CSNO");
		String winUIurl = AppUtils.getContextPath()+"/main_win/indexUi.jsp?userid="+AppUtils.getUserSession().getUserid()+"&usercode="+AppUtils.getUserSession().getUsercode()+ "&usernamec="+AppUtils.getUserSession().getUsername()+"&isSaas="+isSaas + "&csno=" + csno;
		Browser.execClientScript("window.open('" + winUIurl+ "');");
	}
	
	@Bind
	public String winuiurl = "";
	
	public String getwinuiurl(){
		MLType mlType = AppUtils.getUserSession().getMlType();
		if(mlType != null && mlType.equals(LMapBase.MLType.en)){
			//return "A new version of the desktop";
			return "";//Neo 英文不显示,暂时不处理英文环境下的桌面
		}else{
			return "新版桌面";
		}
	}
	
	
}
