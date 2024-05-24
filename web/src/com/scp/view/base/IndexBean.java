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
			try {
				if (!AppUtils.isLogin()) {
					Browser.execClientScript("openNewFrame('../login/login.faces');");
					return;
				}
			} catch (Exception e) {
				Browser.execClientScript("openNewFrame('../login/login.faces');");
				return;
			}
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
			ExtConfig config2 = new ExtConfig();
			config2.set("border", false);
			config2.set("fit", true);
			try {
				MLType mlType = AppUtils.getUserSession().getMlType();
				String sysbpm = ConfigUtils.findSysCfgVal("sys_bpm");
				if(StrUtils.isNull(sysbpm))sysbpm = "BPM";
				centerPanel.addTab("FCL报价查询", AppUtils.getContextPath() + "/pages/module/price/qryfcl.xhtml", "commontabico_tpcls", config2);
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
		ExtConfig config2 = new ExtConfig();
		config2.set("border", false);
		config2.set("fit", true);
		config2.set("border", false);
		config2.set("fit", true);
		if ("委托单".equals(tabName) || "箱量统计新".equals(tabName) || "利润统计新".equals(tabName) || "应收应付统计新".equals(tabName)) {
			url += "?userid=" + AppUtils.getUserSession().getUserid();
		}
		String iconClass = TagPanelUtil.getTabPanelIconCls(ico);
		centerPanel.addTab(tabName, url, iconClass, config2);
	}

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

	public void openTab(String code) {

	}
	
	@Action
	public void showUserConfig() {
		Browser.execClientScript("showUserInfo();");
	}

	@Action
	public void help() {
		String openUrl = "http://note.youdao.com/noteshare?id=07b4e59a76cc8b72eb617c3e03b0c3c3";
		AppUtils.openWindow(System.currentTimeMillis()+"", openUrl);
	}
	
	@Action(id = "logout")
	public String logout() {
		try {
			String fromUrl = AppUtils.getUserSession().getFromUrl();
			if(!StrUtils.isNull(fromUrl)){
				FacesContext.getCurrentInstance().getExternalContext().redirect(fromUrl);
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
		Browser.execClientScript("window.open('" + url+ "');");
	}
	
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
