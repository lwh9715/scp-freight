package com.scp.login_new;

import java.awt.Graphics;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.LocalString;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIDrawImage;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetbyunionidRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.hx.hlp.rmi.HlpUpgService;
import com.scp.base.AppSessionLister;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysLogDao;
import com.scp.exception.LessAuthorizedUsersException;
import com.scp.exception.LoginException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysLoginctrl;
import com.scp.model.sys.SysUser;
import com.scp.service.LoginService;
import com.scp.service.ServiceContext;
import com.scp.service.sysmgr.SysLoginCtrlService;
import com.scp.util.AppUtilBase;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.Base64;
import com.scp.util.CfgUtil;
import com.scp.util.CommonUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.DesUtil;
import com.scp.util.EMailSendUtil;
import com.scp.util.EncoderHandler;
import com.scp.util.HttpClientUtil;
import com.scp.util.MessageUtils;
import com.scp.util.RandomStr;
import com.scp.util.StrUtils;
import com.scp.view.sysmgr.cfg.BaseCfgBean;
import com.taobao.api.ApiException;

/**
 * 登陆页面托管Bean
 */
@ManagedBean(name="login_new.loginBean", scope=ManagedBeanScope.REQUEST)
public class LoginBean extends BaseCfgBean{
	
	@ManagedProperty("#{sysLoginCtrlService}")
	public SysLoginCtrlService sysLoginCtrlService;
	
	/**
	 * 国际化资源串
	 */
	@LocalString
	private Map<String, String> messages;
	
	/**
	 * 登陆名称
	 */
	@Bind(id="username", attribute="value")
	private String username;
	
	/**
	 * 登陆密码
	 */
	@Bind(id="password", attribute="value")
	private String password;
	
	/**
	 * 验证码
	 */
	@Bind(id="validateCode", attribute="value")
	private String validateCode;
	
	/**
	 * 登陆提示
	 */
	@Bind(id = "tips", attribute = "value")
	private String tips;

	@Resource
	public SysLogDao sysLogDao;
	
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if(!isPostback) {
			findSysLoginTips();
			String src = AppUtils.getReqParam("src");
			String action = AppUtils.getReqParam("action");
			String mlType = AppUtils.getReqParam("lan");
			if(!StrUtils.isNull(src) && src.equals("clilogin")){
				processCliLogin(action , mlType);
				return;
			}
			
			try {
				initData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			createValidateCode();
			initCookieInfo();
			changeLanguage();
			isLoginByOpenidOnly = ConfigUtils.findSysCfgVal("login_by_openid_only");
			isOpenLoginBg = ConfigUtils.findSysCfgVal("login_bg_img_open");
			loginTmpCodeUrl = ConfigUtils.findSysCfgVal("login_tmp_code_url");
			//ddCodeUrl = ConfigUtils.findSysCfgVal("dd_code_url")+"http://47.112.190.46:8080/ddScanLogin";
			ddCodeUrl = ConfigUtils.findSysCfgVal("dd_code_url")+"http://8.129.68.2:8989/scp/dingTalkCode";
			
			this.update.markUpdate(UpdateLevel.Data, "isLoginByOpenidOnly");
			
			if(AppUtils.isDebug){
				username = "demo";
			}
		}
	}
	
	private void processCliLogin(String action , String mlTypeStr) {
		try {
//			if("login".equals(action)&& loginService.getNewVersion()== true){
			if("login".equals(action)){
		 	    String usr = AppUtils.getReqParam("usr");//客户端登录，获得用户名
		 	    
				//System.out.println("解密前："+usr);
		        try {
		        	String SKEY = "UFMSufms";
		            Charset CHARSET = Charset.forName("UTF-8");
		            usr = DesUtil.decrypt(usr, CHARSET, SKEY);
		            //System.out.println("解密后："+usr);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		 	    
				if(loginService.checkLogin(usr)){
				String mac = AppUtils.getReqParam("mac");
				String cpuid = AppUtils.getReqParam("cpuid");
				String diskid = AppUtils.getReqParam("diskid");
				String pwd = AppUtils.getReqParam("pwd");
				String pubip = AppUtils.getHttpServletRequest().getRemoteAddr();// + request.getRemoteHost() + request.getRemoteUser() + request.getRemotePort();
				String innerip = AppUtils.getReqParam("innerip");
				String pcname = AppUtils.getReqParam("pcname");
				String pcloginusr = AppUtils.getReqParam("loginusr");
				String logversion = AppUtils.getReqParam("ver");
				
				SysLoginctrl data = new SysLoginctrl();
				data.setMac(mac);
				data.setCpuid(cpuid);
				data.setDiskid(diskid);
				data.setPubip(pubip);
				data.setInnerip(innerip);
				data.setPcname(pcname);
				
				data.setLastloginusr(usr);
				data.setLastlogintime(Calendar.getInstance().getTime());
				
				data.setPcloginusr(pcloginusr);
				data.setUsername(usr);
				data.setPassword(pwd);
				
				//AppUtils.debug("mac:"+mac);
				//AppUtils.debug("cpuid:"+cpuid);
				//AppUtils.debug("diskid:"+diskid);
				//AppUtils.debug("usr:"+usr);
				//AppUtils.debug("pwd:"+pwd);
				//AppUtils.debug("pubip:"+pubip);
				//AppUtils.debug("innerip:"+innerip);
				//AppUtils.debug("pcname:"+pcname);
				//AppUtils.debug("pcloginusr:"+pcloginusr);
				//AppUtils.debug("logversion:"+logversion);
				MLType mlType = MLType.ch;
				if("EN".equals(mlTypeStr)) {
					mlType = MLType.en;
					multilanguage = "en";
					changeLanguage();
				}
				sysLoginCtrlService.login(data , mlType);	
				String url = "";
				if (AppUtils.getUserSession().isAdmin()) {
					url = "../main/adminindex.xhtml?id=6000&login=true";
					loginService.setLogincount(usr);
					loginService.sendMail();
				} else {
					boolean version = loginService.getNewVersion();
					url = "../main/index.xhtml?id=1000&login=true&version="+version+"";
					loginService.setLogincount(usr);
				}
				Browser.execClientScript("window.location.href='" + url + "'");
				}
//		 }else{
//			 WindowShowMessage.show();
		 }
		} catch (LoginException e) {
			e.printStackTrace();
			String url = AppUtils.getContextPath()+"/login/login.xhtml";
			Browser.execClientScript("openNewFrame('" + url + "');");
			MessageUtils.showMsg(e.getLocalizedMessage());
			tipsValue = "\n" + MessageUtils.returnException(e) + "\n\n-----------------------------------------" + tipsValue;
			return;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 查询系统设置中登录提示是否有值
	 */
	public void findSysLoginTips(){
		String loginTops = ConfigUtils.findSysCfgVal("sys_login_tips");
		if(!StrUtils.isNull(loginTops)){
			tipsValue = tipsValue + "\n" + loginTops + "\n";
		}
	}
	
	@Bind
	public String tipsValue = "";
	
	@Bind
	public UICombo multiLanguageCom;
	
	
	/**
	 * 初始化从cookie获取数据
	 */
	private void initCookieInfo() {
		// 初始化从cookie获取数据
		String prefix = "_SCP_";
		HttpServletRequest request = AppUtils.getHttpServletRequest();
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			try {
				for (Cookie cookie : cookies) {
					String cname = cookie.getName();
					cname = URLDecoder.decode(cname,"UTF-8");
					String cvalue = cookie.getValue();
					cvalue = URLDecoder.decode(cvalue,"UTF-8");
					if (cvalue != null && ((prefix + "username").equals(cname))) {
						username = cvalue ;
					}
					if (cvalue != null
							&& ((prefix + "multilanguage").equals(cname))) {
						multilanguage = cvalue;
						multiLanguageCom.setValue(cvalue);
					}
					if (cvalue != null && ((prefix + "isReme").equals(cname))) {
						if ("Y".equals(cvalue)) {
							isReme = true;
						} else {
							isReme = false;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建验证码
	 */
	private void createValidateCode() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		String randCode = CommonUtil.getRandom(4);//RandomStr.randomStr(4);
		session.put("LOGIN_VALIDATE_CODE", randCode);
	}
	
	/**
	 * 页面DrawImage组件draw属性绑定的绘图操作函数
	 * 
	 * @param g
	 *            Graphics
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 */
	public void draw(Graphics g, int width, int height) {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		String randomCode = (String) session
				.get("LOGIN_VALIDATE_CODE");

		if (randomCode != null) {
			CommonUtil.drawRandomPicture(g, width, height, randomCode);
		}
	}
	
	@Bind(id="validateCodeImg", attribute="binding")
	private UIDrawImage validateImg; 
	
	/**
	 * 绑定页面id为validateImg的DrawImage组件的onclick事件，
	 * 实现验证码图片重新刷新
	 */
	@Action(id="refreshValidateCode", immediate=true)
	public void change() {
		createValidateCode();
		validateImg.refresh();
	}
	
	
	@Inject
	protected PartialUpdateManager update;
	
	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	@Bind
	public UIWindow useronlineWindow;
	
	@Bind
	public UIIFrame useronlineFrame;
	
	@Bind
	@SaveState
	public String isLoginByOpenidOnly;
	
	@Bind
	@SaveState
	public String isOpenLoginBg;

	@Bind
	public String ddCodeUrl;

	@Bind
	public String loginTmpCodeUrl;

	/**
	 * 绑定页面的id为loginBtn的Button组件的点击事件，
	 * 进行登陆验证
	 */
	@Action(id="loginBtn")
	public String loginBtn() {
		try {
			if(loginService.checkLogin(this.username)){
				loginService.check(username);
				checkValidateCode();
				loginService.loginView(username, password ,l.m.getMlType());
			}
			
			if("Y".equalsIgnoreCase(isLoginByOpenidOnly) && !"system".equalsIgnoreCase(this.username) && !"demo".equalsIgnoreCase(this.username)){
				Boolean isopenid = loginService.isOpenidByUserid(AppUtils.getUserSession().getUserid());
				if(isopenid){
					MessageUtils.alert("请使用微信扫码登录!");
					Browser.execClientScript("wxLogin();");
					return null;
				}
				wxbindtimeraction();
				Browser.execClientScript("refreshWxbind()");
				return null;
			}
			
			String url = "";
			if (AppUtils.getUserSession().isAdmin()) {
				url = "../index/adminindex?id=6000&login=true";
				loginService.setLogincount(this.username);
			} else {
				url = "../main/index?id=1000&login=true";
				loginService.setLogincount(this.username);
			}
			rememberAction();
			AppSessionLister.addSession(AppUtils.getHttpSession());
			Long userid = AppUtils.getUserSession().getUserid();
			
			//1532 系统登录后，在个人设置表里面增加一条记录，标记当前账号用的语言
			String sql = "SELECT f_inert_sys_configuser('userid="+AppUtils.getUserSession().getUserid()+";language="+multiLanguageCom.getValue()+"')";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc("Login");
			syslog.setLogtime(new Date());
			syslog.setLogtype("BI");
			sysLogDao.create(syslog);
			return "view:redirect:" + url;
		}catch(LessAuthorizedUsersException e){
			String blankUrl = AppUtils.getContextPath()
			+ "/login_new/useronline.xhtml";
			useronlineFrame.load(blankUrl);
			useronlineWindow.show();
			return null;
		}catch (LoginException e) {
			e.printStackTrace();
			MessageUtils.showMsg(e.getLocalizedMessage());
			//this.showTips(e.getLocalizedMessage());
			password = "";
			this.update.markUpdate(UpdateLevel.Data, "password");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showMsg(e.getLocalizedMessage());
			//this.showTips(e.getLocalizedMessage());
			return null;
		}
	}
	
	@ManagedProperty("#{loginService}")
	public LoginService loginService;
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;
	
	
	private void checkValidateCode() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if(session == null || session.get("LOGIN_VALIDATE_CODE") == null){
			throw new LoginException("No Session or can't get validate code!");
		}
		String randomCode = (String)session.get("LOGIN_VALIDATE_CODE");
		if(AppUtils.isDebug == false && !randomCode.equalsIgnoreCase(validateCode)) {
			throw new LoginException("Wrong Validate Code");
		}
	}
	
	
	/**
	 * 记住登陆信息选择
	 */
	@Bind
	private boolean isReme = false;
	
	@Bind
	public String multilanguage = "ch";
	
	/**
	 * 勾选下次记住我
	 */
	// @Action
	private void rememberAction() {
		String prefix = "_SCP_";
		// 添加cookie信息
		HttpServletResponse response = AppUtils.getHttpServletResponse();
		Cookie cisReme;
		try {
			cisReme = new Cookie(prefix + "isReme", URLEncoder.encode(isReme == true ? "Y": "N", "UTF-8"));
			cisReme.setMaxAge(30 * 24 * 60 * 60);
			response.addCookie(cisReme);
			if (isReme) {
				if (!StrUtils.isNull(username)) {
					Cookie cUsername = new Cookie(prefix + "username", URLEncoder.encode(username,   "UTF-8"));
					cUsername.setMaxAge(30 * 24 * 60 * 60);
					response.addCookie(cUsername);
				}
				if (!StrUtils.isNull(multilanguage)) {
					Cookie cUsername = new Cookie(prefix + "multilanguage",URLEncoder.encode(multilanguage,   "UTF-8"));
					cUsername.setMaxAge(30 * 24 * 60 * 60);
					response.addCookie(cUsername);
				}
			} else {
				response.addCookie(new Cookie(prefix + "username", null));
				response.addCookie(new Cookie(prefix + "multilanguage", null));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	@Action
	public void changeLanguage() {
		if (multilanguage.equals("en")) {
			l.m.initLocal(MLType.en);
			l.m.setMlType(MLType.en);
		} else if (multilanguage.equals("tw")) {
			l.m.initLocal(MLType.tw);
			l.m.setMlType(MLType.tw);
		} else {
			l.m.initLocal(MLType.ch);
			l.m.setMlType(MLType.ch);
		}
		this.update.markUpdate(UpdateLevel.Data, "loginBtn");
		this.update.markUpdate(UpdateLevel.Data, "isReme");
		this.update.markUpdate(UpdateLevel.Data, "isRemeMe");
		this.update.markUpdate(UpdateLevel.Data, "refreshValidateCode");
		this.update.markUpdate(UpdateLevel.Data, "north");
		this.update.markUpdate(UpdateLevel.Data, "titles");
		this.update.markUpdate(UpdateLevel.Data, "resetPwd");
		this.update.markUpdate(UpdateLevel.Data, "resuser");
		this.update.markUpdate(UpdateLevel.Data, "resemail");
		this.update.markUpdate(UpdateLevel.Data, "upg");
		this.update.markUpdate(UpdateLevel.Data, "version");
		this.update.markUpdate(UpdateLevel.Data, "Cli_version");
		this.update.markUpdate(UpdateLevel.Data, "DB_version");
		this.update.markUpdate(UpdateLevel.Data, "csno");
	}
	
	
	@Bind
	public String isRemeMe;
	
	
	@Bind
    public String upglogs;
	
	
	@Action
	public void getLic(){
		try {
			HlpUpgService hlpTransMonitorService = (HlpUpgService)AppUtils.getBeanFromSpringIoc("hlpUpgService");
			String csno = CfgUtil.findSysCfgVal("CSNO");
			String fixVerNoStr = CfgUtil.findSysCfgVal("DB_Version");
			Integer fixVerNo = Integer.parseInt(fixVerNoStr);
			
			
			try {
				String sql = hlpTransMonitorService.checkUserAndModule(csno);
				DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
				daoIbatisTemplate.updateWithUserDefineSql(sql);
				//System.out.println("~~~~~~~~~~~~~checkUserAndModule OK;");
				upglogs = csno + "~~~~~~~~~checkUserAndModule OK!" + "\n";
				update.markUpdate(UpdateLevel.Data,"upglogs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			upglogs = e.getLocalizedMessage();
			update.markUpdate(UpdateLevel.Data,"upglogs");
		}
	}
    
    @Action
    public void upg(){
    	try {
			HlpUpgService hlpTransMonitorService = (HlpUpgService)AppUtils.getBeanFromSpringIoc("hlpUpgService");
			String csno = CfgUtil.findSysCfgVal("CSNO");
			String fixVerNoStr = CfgUtil.findSysCfgVal("DB_Version");
			Integer fixVerNo = Integer.parseInt(fixVerNoStr);
			
			
			try {
				String sql = hlpTransMonitorService.checkUserAndModule(csno);
				DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
				daoIbatisTemplate.updateWithUserDefineSql(sql);
				//System.out.println("~~~~~~~~~~~~~checkUserAndModule OK;");
				upglogs = csno + "~~~~~~~~~checkUserAndModule OK!" + "\n";
				update.markUpdate(UpdateLevel.Data,"upglogs");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			boolean check = hlpTransMonitorService.checkPause(csno, fixVerNo);
			if(check)return;
			List<Map<String, String>> lists = hlpTransMonitorService.getUpgSql(csno, fixVerNo);
			
			if(lists != null){
				if(lists.size() == 0){
					upglogs += csno + ":无升级语句!";
					update.markUpdate(UpdateLevel.Data,"upglogs");
				}
				StringBuilder stringBuilder = new StringBuilder();
				for (Map<String, String> map : lists) {
					String sql = StrUtils.getMapVal(map, "upgsql");
					String pkId = StrUtils.getMapVal(map, "pkid");
					String indexno = StrUtils.getMapVal(map, "indexno");
					
					String response = "OK";
					//System.out.println(map);
					DaoIbatisTemplate daoIbatisTemplate;
					try {
						daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
						daoIbatisTemplate.updateWithUserDefineSql(sql);
						stringBuilder.append(sql);
						upglogs += indexno + "~~~~~~~~~~~~~~~~~~~~~" + response + "\n";
						//System.out.println(upglogs);
						update.markUpdate(UpdateLevel.Data,"upglogs");
					} catch (Exception e1) {
						//System.out.println(e1.getLocalizedMessage());
						response = e1.getLocalizedMessage();
						upglogs += indexno + "~~~~~~~~~~~~~~~~~~~~~" + response + "/n";
						update.markUpdate(UpdateLevel.Data,"upglogs");
					}
					String ret = hlpTransMonitorService.responseUpgSql(csno, pkId, response);
					//System.out.println(ret);
				}
			}
			
			String pkId = "";
			try {
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		        Calendar calendar = Calendar.getInstance();
		        Date theDate = calendar.getTime();
		        String dayupg = df.format(theDate);
				ConfigUtils.refreshSysCfg("Version", dayupg, 0l);
				
				String shells = hlpTransMonitorService.execShell(csno, fixVerNo);
				if(StrUtils.isNull(shells)){
					upglogs += "shell:~~~~~~~~~~~~~no shell need to exec:";
					//System.out.println(upglogs);
					return;
				}
				String[] shellArray = shells.split(":");
				pkId = shellArray[0];
				String shellCmd = shellArray[1];
				//System.out.println(shellCmd);
				hlpTransMonitorService.responseExecShell(pkId, "OK,Start at:"+Calendar.getInstance().getTime().toGMTString());
				AppUtilBase.process(shellCmd);
				upglogs = "3.~~~~~~~~~~~~~shell:" +  shells;
				
			} catch (Exception e) {
				e.printStackTrace();
				hlpTransMonitorService.responseExecShell(pkId, StrUtils.getSqlFormat(e.getLocalizedMessage()));
				upglogs += "\n3.~~~~~~~~~~~~~shell:"+e.getLocalizedMessage();
				update.markUpdate(UpdateLevel.Data,"upglogs");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			upglogs = e.getLocalizedMessage();
			update.markUpdate(UpdateLevel.Data,"upglogs");
		}
    	
    }

    @Override
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add(ConfigUtils.SysCfgKey.rpt_srv_url.name());
		vector.add(ConfigUtils.SysCfgKey.login_leftimg.name());
		vector.add(ConfigUtils.SysCfgKey.login_logo.name());
		vector.add("Version");
		vector.add("Cli_Version");
		vector.add("DB_Version");
		vector.add("CSNO");
		vector.add("Licenses");
		//vector.add("sys_ICP_NO");
		vector.add("sys_logo_height");
		vector.add("bgImg1");
		vector.add("bgImg2");
		return vector;
	}


	@Override
	protected String getQuerySql() {
		return 
		"\nSELECT * " +
		"\nFROM sys_config " +
		"\nWHERE 1=1 ";
	}
	
//	@AfterPhase 
//    public void afterPhase(PhaseEvent event){
//        String log = "\n" + event.getPhaseId().toString() + "阶段结束";
//        //AppUtils.debug(log);
//    }
	
	@Bind
	public String getLogo(){
		String logoname = ConfigUtils.findSysCfgVal(ConfigUtils.SysCfgKey.login_logo.name());
		if(StrUtils.isNull(logoname))return "";
		String result = "";
		try {
			String url = AppUtils.getAttachFilePath() + logoname;
			//System.out.println(url);
			String os = System.getProperty("os.name").toLowerCase();
			//if(os.indexOf("linux")>=0){
			//	result = "/scp/attachfile/"+logoname;
			//	System.out.println(result);
			//}else{
				result = "data:image/png;base64,"+Base64.getImageStr(AppUtils.getAttachFilePath() + logoname);
			//}
			//result = "/scp/attachfile/"+logoname;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	@Bind
	@SaveState
	public String usernameReSet;
	
	
	@Bind
	@SaveState
    public String emailReSet;
    
    @Action
    public void resetPwd(){
		try {
			if(StrUtils.isNull(usernameReSet) || StrUtils.isNull(emailReSet)){
				MessageUtils.showMsg("User name or email can not be null!");
				return;
			}
			if(!emailReSet.contains("@")){
				MessageUtils.showMsg("email illegal!");
				return;
			}
			String usernames = usernameReSet.replaceAll("'", "''");
			String usernameUpper = usernames.toUpperCase();
			List<SysUser> userList = loginService.sysUserDao.findAllByClauseWhere(" (UPPER(code)='" + usernameUpper + "' OR UPPER(namec)='" + usernameUpper + "' OR UPPER(namee)='" + usernameUpper + "') AND  isinvalid = TRUE AND isdelete = FALSE");
			if (userList == null || userList.size() == 0) {
				MessageUtils.showMsg("User name and email is not match!");
				return;
			}
			if (userList.size() > 1) {
				MessageUtils.showMsg("User name and email is not match!");
				return;
			}
			if (userList.size() == 1) {
				SysUser sysUser = userList.get(0);
				String email = sysUser.getEmail1();
				emailReSet = emailReSet.trim();
				email = email.trim();
				if(!emailReSet.equalsIgnoreCase(email)){
					MessageUtils.showMsg("User name and email is not match!");
					return;
				}
				String pwd = RandomStr.randomStr(8);
				
				String salt = CommonUtil.getRandom(5);
				sysUser.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(pwd)+salt));
				sysUser.setSecretkey(salt);
				loginService.sysUserDao.modify(sysUser);
					
				String mailContext = 
					"尊敬的："+sysUser.getNamec()+" "+
					"\n\n\t     您已申请密码重置，新密码为："+pwd + " "+
					"\n\t     如非本人操作，请留意账号安全!"+
					"\n\t     进入UFMS系统后，可以在这里修改密码，系统---个人面板--修改密码"+
					"\n "+
					"\n\t任何疑问请联系管理员";
				
				EMailSendUtil.sendEmailByAdmin(mailContext, "UFMS系统，重置密码", email, "");
				
				//重设密码后设置logincount为0
				String sql = "UPDATE sys_user SET logincount = 0 WHERE UPPER(code)='"
						+ usernameUpper
						+ "' AND isinvalid = TRUE AND isdelete = FALSE ";
				loginService.sysUserDao.executeSQL(sql);
				MessageUtils.showMsg("OK!请查收邮箱");
			}
		} catch (Exception e) {
			MessageUtils.showMsg("reset password error , please contact administrator!");
			e.printStackTrace();
		}
    }
    
    @Bind
    public UIWindow wxloginWindow;
    
    @SaveState
    public String loginuuid;
    
    /*
	 * 二维码Url
	 */
	@Bind
	private String codeUrl;
	
    @Action
	public void wxLoginAction(){
		String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
		String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
		String bindUrl = ConfigUtils.findSysCfgVal("WeiXin_BindUrl");//http://www.ufms.cn/
		String backurl = ConfigUtils.findSysCfgVal("WeiXin_CallBackUrl");//http://113.116.6.90:8081/scp/
		
		loginuuid = AppUtils.getReqParam("uuid");
		
		backurl += loginuuid +"/scan_login/";
		
		//scope分为两种：一种是静默方式（snsapi_base）；一种是非静默方式（snsapi_userinfo），需要用户去手动点击同意才能获取用户的信息。
		codeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appID +
				"&redirect_uri="+bindUrl+"servlet/OAuthAPIServlet?backurl="+backurl+"" + 
				"&response_type=code" +
				"&scope=snsapi_userinfo" +
				"&state=STATE#wechat_redirect";
		wxloginWindow.show();
		String js = "refreshQrCode('"+codeUrl+"')";
		Browser.execClientScript(js);
	}
    
    @Action
	public String timerAction(){
    	String usercode = "";
    	try {
    		System.out.println("loginuuid:"+loginuuid);
			String querySql = 
					"SELECT u.id AS userid , u.code AS usercode FROM sys_useronline o , sys_user u "+
					"\nWHERE o.loginuuid is not null  "+
					"\n	AND o.openid = u.opneid "+
					"\n	and o.loginuuid = '"+loginuuid+"' "+ 
					"\n	ORDER BY o.logintime DESC LIMIT 1;";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			usercode = StrUtils.getMapVal(map, "usercode");
			System.out.println(" usercode：" + usercode);
		} catch (NoRowException e) {
			System.out.println("uuid没有绑定微信");
    		return null;
		} catch (MoreThanOneRowException e) {
			System.out.println("uuid查到多条记录");
    		return null;
		}
    	
    	try{
//    		loginuuid = "6391a7f0-59d6-4dca-8caf-5f0795a1eb36";
//			loginService.getUserCodeOnUuid(loginuuid);
			if(!StrUtils.isNull(usercode)){//如果openid已经赋值就显示已绑定，就按照openid查找对应的用户去登录
				if(loginService.checkLogin(usercode)){
					loginService.check(usercode);
					//checkValidateCode();
					loginService.loginView(usercode, "" ,l.m.getMlType());
				}
				String url = "";
				if (AppUtils.getUserSession().isAdmin()) {
					url = "../index/adminindex?id=6000&login=true";
					loginService.setLogincount(this.username);
					//loginService.sendMail();
				} else {
					url = "../main/index?id=1000&login=true";
					loginService.setLogincount(this.username);
				}
				AppSessionLister.addSession(AppUtils.getHttpSession());
				Long userid = AppUtils.getUserSession().getUserid();
				//1532 系统登录后，在个人设置表里面增加一条记录，标记当前账号用的语言
//				System.out.println("loginBtn.inert_sys_configuser5.....");
				String sql = "SELECT f_inert_sys_configuser('userid="+AppUtils.getUserSession().getUserid()+";language="+multiLanguageCom.getValue()+"')";
				DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
				daoIbatisTemplate.updateWithUserDefineSql(sql);
				
				SysLog syslog = new SysLog();
				syslog.setInputer(AppUtils.getUserSession().getUsername());
				syslog.setLogdesc("Login by weixin scan authorize");
				syslog.setLogtime(new Date());
				syslog.setLogtype("BI");
				sysLogDao.create(syslog);
				return "view:redirect:" + url;
			}else{
				MessageUtils.showMsg("没有绑定微信，不能通过微信扫码登录");
				return null;
			}
    	} catch (NoRowException e) {
    		System.out.println(" NoRowException：" + e.getLocalizedMessage());
    		return null;
    	} catch (MoreThanOneRowException e) {
    		System.out.println(" MoreThanOneRowException：" + e.getLocalizedMessage());
    		return null;
    	} catch (LoginException e) {
			//	e.printStackTrace();
			//String url = AppUtils.getContextPath()+"/login/login.xhtml";
			//Browser.execClientScript("openNewFrame('" + url + "');");
			//MessageUtils.showMsg(e.getLocalizedMessage());
			//tipsValue = "\n" + MessageUtils.returnException(e) + "\n\n-----------------------------------------" + tipsValue;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			//MessageUtils.showException(e);
			return null;
		}
	}
    
    /*
	 * 绑定微信二维码Url
	 */
	@Bind
	private String wxbindcodeUrl;
	
	@Action
	public String wxbindtimeraction(){
		
		String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
		String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
		String bindUrl = ConfigUtils.findSysCfgVal("WeiXin_BindUrl");//http://www.ufms.cn/
		String backurl = ConfigUtils.findSysCfgVal("WeiXin_CallBackUrl");//http://113.116.6.90:8081/scp/
		
		backurl += AppUtils.getUserSession().getUserid() +"/so_user/";
		
		//scope分为两种：一种是静默方式（snsapi_base）；一种是非静默方式（snsapi_userinfo），需要用户去手动点击同意才能获取用户的信息。
		wxbindcodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appID +
				"&redirect_uri="+bindUrl+"servlet/OAuthAPIServlet?backurl="+backurl+"" + 
				"&response_type=code" +
				"&scope=snsapi_userinfo" +
				"&state=STATE#wechat_redirect";
		
		String url = "";
		if (AppUtils.getUserSession().isAdmin()) {
			url = "../index/adminindex?id=6000&login=true";
			loginService.setLogincount(this.username);
			//loginService.sendMail();
		} else {
			url = "../main/index?id=1000&login=true";
			loginService.setLogincount(this.username);
		}
		update.markUpdate(true,UpdateLevel.Data, "wxbindcodeUrl");
		Boolean isopenid = loginService.isOpenidByUserid(AppUtils.getUserSession().getUserid());
		if(isopenid){
			return "view:redirect:" + url;
		}
		return null;
	}
	
	/**
	 * 钉钉扫码登录方式
	 * @return
	 */
	@Action
	public String ddLoginAction() {
		try {
			String ddTmpUrl = AppUtils.getReqParam("urlcode");
			Map<String ,String> map = ddLoginUserInfo(ddTmpUrl);
	        
			String url = "";
			loginService.ddLoginView(map.get("name"), map.get("idCode"),map.get("mobile"),MLType.ch);
			if (AppUtils.getUserSession().isAdmin()) {
				url = "../index/adminindex?id=6000&login=true";
				loginService.setLogincount(map.get("name"));
			} else {
				url = "../main/index?id=1000&login=true";
				loginService.setLogincount(map.get("name"));
			}
			rememberAction();
			AppSessionLister.addSession(AppUtils.getHttpSession());

			String sql = "SELECT f_inert_sys_configuser('userid="+AppUtils.getUserSession().getUserid()+";language="+multiLanguageCom.getValue()+"')";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc("Login");
			syslog.setLogtime(new Date());
			syslog.setLogtype("BI");
			sysLogDao.create(syslog);
			return "view:redirect:" + url;
		}catch(LessAuthorizedUsersException e){
			String blankUrl = AppUtils.getContextPath()
			+ "/login_new/useronline.xhtml";
			useronlineFrame.load(blankUrl);
			useronlineWindow.show();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showMsg(e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 钉钉打开自动认证登录
	 * @return
	 */
	@Action
	public String dingTalkAction() {
		try {
			String authCode = AppUtils.getReqParam("dingTalkCode");
			Map<String ,String> map = dingTalkUserInfo(authCode);
			String url = "";
			loginService.ddLoginView(map.get("name"), map.get("idCode"),map.get("mobile"),MLType.ch);
			if (AppUtils.getUserSession().isAdmin()) {
				url = "../index/adminindex?id=6000&login=true";
				loginService.setLogincount(map.get("name"));
			} else {
				url = "../main/index?id=1000&login=true";
				loginService.setLogincount(map.get("name"));
			}
			rememberAction();
			AppSessionLister.addSession(AppUtils.getHttpSession());

			String sql = "SELECT f_inert_sys_configuser('userid="+AppUtils.getUserSession().getUserid()+";language="+multiLanguageCom.getValue()+"')";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			daoIbatisTemplate.updateWithUserDefineSql(sql);

			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc("Login");
			syslog.setLogtime(new Date());
			syslog.setLogtype("BI");
			sysLogDao.create(syslog);
			return "view:redirect:" + url;
		}catch(LessAuthorizedUsersException e){
			String blankUrl = AppUtils.getContextPath()
					+ "/login_new/useronline.xhtml";
			useronlineFrame.load(blankUrl);
			useronlineWindow.show();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showMsg(e.getLocalizedMessage());
			return null;
		}
	}

	
	/**
	 * 钉钉临时码获取用户资料
	 * @param url
	 * @return
	 */
	public Map<String, String> ddLoginUserInfo(String url){
		try {
			String ddTmpUrl = HttpClientUtil.doGet(url, null);
			String DDAppID = ConfigUtils.findSysCfgVal("dingDing_AppKey");
			String DDAppsecret = ConfigUtils.findSysCfgVal("dingDing_AppSecret");
			//获取accessToken
			OapiGettokenResponse response = getOapiGettokenResponse(DDAppID, DDAppsecret);
			// 通过临时授权码获取授权用户的个人信息
	        DefaultDingTalkClient client2 = new DefaultDingTalkClient("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
	        OapiSnsGetuserinfoBycodeRequest reqBycodeRequest = new OapiSnsGetuserinfoBycodeRequest();
	        // 通过扫描二维码，跳转指定的redirect_uri后，向url中追加的code临时授权码
	        reqBycodeRequest.setTmpAuthCode(ddTmpUrl);
	        OapiSnsGetuserinfoBycodeResponse bycodeResponse = client2.execute(reqBycodeRequest, DDAppID, DDAppsecret);
	        // 根据unionid获取userid
	        String unionid = bycodeResponse.getUserInfo().getUnionid();
	        DingTalkClient clientDingTalkClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/getbyunionid");
	        OapiUserGetbyunionidRequest reqGetbyunionidRequest = new OapiUserGetbyunionidRequest();
	        reqGetbyunionidRequest.setUnionid(unionid);
	        OapiUserGetbyunionidResponse oapiUserGetbyunionidResponse = clientDingTalkClient.execute(reqGetbyunionidRequest, response.getAccessToken());
	        // 根据userId获取用户信息
	        String userid = oapiUserGetbyunionidResponse.getResult().getUserid();
			return getDingTalkInfo(response, userid);
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 钉钉code获取用户资料
	 * @param code
	 * @return
	 */
	public Map<String, String> dingTalkUserInfo(String code){
		try {
			String DDAppID = ConfigUtils.findSysCfgVal("dingDing_AppKey");
			String DDAppsecret = ConfigUtils.findSysCfgVal("dingDing_AppSecret");
			//获取accessToken
			OapiGettokenResponse response = getOapiGettokenResponse(DDAppID, DDAppsecret);
			//获取userID
	        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
	        OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
	        req.setCode(code);
	        OapiV2UserGetuserinfoResponse oapiV2UserGetuserinfoResponse;
            oapiV2UserGetuserinfoResponse = client.execute(req, response.getAccessToken());
			if (oapiV2UserGetuserinfoResponse.isSuccess()) {
                OapiV2UserGetuserinfoResponse.UserGetByCodeResponse userGetByCodeResponse = oapiV2UserGetuserinfoResponse.getResult();
    			// 根据userId获取用户信息
    			String userid = userGetByCodeResponse.getUserid();
				return getDingTalkInfo(response, userid);
            }else {
				throw new LoginException("DingTalk 校验不通过");
			}
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取企业认证token
	 * @param DDAppID
	 * @param DDAppsecret
	 * @return
	 * @throws ApiException
	 */
	private OapiGettokenResponse getOapiGettokenResponse(String DDAppID, String DDAppsecret) throws ApiException {
		DingTalkClient tokenClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
		OapiGettokenRequest request = new OapiGettokenRequest();
		request.setAppkey(DDAppID);
		request.setAppsecret(DDAppsecret);
		request.setHttpMethod("GET");
		return tokenClient.execute(request);
	}

	/**
	 * 根据userid获取钉钉用户信息
	 * @param response
	 * @param userid
	 * @return
	 * @throws ApiException
	 */
	private Map<String,String> getDingTalkInfo(OapiGettokenResponse response, String userid) throws ApiException {
		DingTalkClient clientDingTalkClient2 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
		OapiV2UserGetRequest reqGetRequest = new OapiV2UserGetRequest();
		reqGetRequest.setUserid(userid);
		reqGetRequest.setLanguage("zh_CN");
		OapiV2UserGetResponse rspGetResponse = clientDingTalkClient2.execute(reqGetRequest, response.getAccessToken());
        JSONObject ddBody = JSONObject.parseObject(rspGetResponse.getBody()).getJSONObject("result");
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("name", ddBody.getString("name"));
        userMap.put("mobile", ddBody.getString("mobile"));
        userMap.put("idCode", ddBody.getString("job_number"));
		return userMap;
	}
}
