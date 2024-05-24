package com.scp.login;

import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIDrawImage;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.LoginException;
import com.scp.model.sys.SysLoginctrl;
import com.scp.service.LoginService;
import com.scp.service.ServiceContext;
import com.scp.service.sysmgr.SysLoginCtrlService;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@ManagedBean(name = "login.loginBean", scope = ManagedBeanScope.REQUEST)
public class LoginBean {

	@ManagedProperty("#{loginService}")
	public LoginService loginService;
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;
	
	@ManagedProperty("#{sysLoginCtrlService}")
	public SysLoginCtrlService sysLoginCtrlService;
	
	@ManagedProperty("#{comboxBean}")
	protected CommonComBoxBean commonComBoxBean;

	@Inject
	protected PartialUpdateManager update;

	@Inject(value = "l")
	protected MultiLanguageBean l;

	@Bind
	@SaveState
	@Accessible
	public String multilanguage = "en";

	@Bind
	public String tips;
	
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> userComs;
	
//	@Bind
//	@SaveState
//	public UIWindow showtimeWindow;
	/**
	 * 记住登陆信息选择
	 */
	@Bind
	@SaveState
	private boolean isReme = false;

	@Bind
	public String isRemeMe;

	@Bind
	public String username;

	@Bind
	public String branch;

	@Bind
	public UICombo branchCom;

	@Bind
	public UICombo userCom;

	@Bind
	public UICombo multiLanguageCom;

	@Bind
	public String password;
	
	@Bind
	@SaveState
	@Accessible
	public Date aftertime ;
	
//	@Bind
//	public UIWindow WindowShowMessage;
	
//	@Bind
//	@SaveState
//	public  ServletContext application = AppUtils.getHttpServletRequest().getSession().getServletContext();
	
	@BeforeRender
	public void beforeRender(boolean isPostBack)throws Exception {
		if (!isPostBack) {
			createValidateCode();
			this.getCookieInfo();
			changeLanguage();
			
			if(AppUtils.isDebug == true) {
				password = "123";
				validateCode = "123";
			}
			String src = AppUtils.getReqParam("src");
			String action = AppUtils.getReqParam("action");
			String mlType = AppUtils.getReqParam("lan");
			
			String branch = (String)this.branchCom.getValue();
			if(branch != null && !"".equals(branch)) {
				if(userComs == null) {
					userComs = this.queryUserComs(Long.parseLong(branch));
				}
			}
			this.update.markUpdate(UpdateLevel.Data, this.userCom);
			if(!StrUtils.isNull(src) && src.equals("clilogin")){
				processCliLogin(action , mlType);
				return;
			}
		}
	}
	
	@Action
    private void changeUserCom() {
		String branch = AppUtils.getReqParam("branchCom");
        if(branch !=null && !"".equals(branch)) {
        	this.branch = branch;
        	this.userComs = queryUserComs(Long.parseLong(this.branch));
        	this.userCom.setValue(null);
        	this.update.markUpdate(UpdateLevel.Data, this.userCom);
        }
    }
	
	private List<SelectItem> queryUserComs(Long branch) {
		if(branch != null) {
			List<SelectItem> selectItems = commonComBoxBean.getLoginUserByBranchCom(branch);
			selectItems.add(new SelectItem(null,""));
			return selectItems;
		}
		return null;
    }
	
	private void processCliLogin(String action , String mlTypeStr) {
		try {
//			if("login".equals(action)&& loginService.getNewVersion()== true){
			if("login".equals(action)){
		 	    String usr = AppUtils.getReqParam("usr");//客户端登录，获得用户名
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
				Browser.execClientScript("openNewFrame('" + url + "');");
				}
//		 }else{
//			 WindowShowMessage.show();
		 }
		} catch (LoginException e) {
			e.printStackTrace();
			String url = AppUtils.getContextPath()+"/login/login.xhtml";
			Browser.execClientScript("openNewFrame('" + url + "');");
			MessageUtils.showMsg(e.getLocalizedMessage());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	/**
	 * 验证码
	 */
	@Bind
	public String validateCode;
	
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

	@Bind(id = "validateCodeImg", attribute = "binding")
	private UIDrawImage validateImg;

	/**
	 * 绑定页面id为validateImg的DrawImage组件的onclick事件， 实现验证码图片重新刷新
	 */
	@Action(id = "refreshValidateCode", immediate = true)
	public void change() {
		createValidateCode();
		validateImg.refresh();
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
	}
	
	@Action(id = "loginBtn")
	public String loginBtn() throws Exception {
		try {
			if(loginService.checkLogin(this.username)){
			loginService.check(username);
			checkValidateCode();
			loginService.loginView(username, password ,l.m.getMlType());
		}
		} catch (LoginException e) {
				e.printStackTrace();
				//MsgUtil.showMsg(e.getLocalizedMessage());
				this.showTips(e.getLocalizedMessage());
				password = "";
				this.update.markUpdate(UpdateLevel.Data, "password");
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			//MsgUtil.showMsg(e.getLocalizedMessage());
			this.showTips(e.getLocalizedMessage());
			return null;
		}
		String url = "";
		if (AppUtils.getUserSession().isAdmin()) {
			url = "../main/adminindex.xhtml?id=6000&login=true";
			loginService.setLogincount(this.username);
			loginService.sendMail();
		} else {
			url = "../main/index.xhtml?id=1000&login=true";
			loginService.setLogincount(this.username);
		}
		Browser.execClientScript("openNewFrame('" + url + "');");
		rememberAction();
		
		return null;
	}
	
	
	@Bind 
	public UIWindow showTips;

	private void showTips(String tips) {
		this.tips = tips;
		this.update.markUpdate(UpdateLevel.Data, "tips");
		showTips.show();
	}

	private void checkValidateCode() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String randomCode = (String)session.get("LOGIN_VALIDATE_CODE");
		if(!randomCode.equalsIgnoreCase(validateCode)) {
			throw new LoginException("Wrong Validate Code");
		}
	}

	// private String openIndex(){
	// UserSession userSession = new UserSession();
	// userSession.setLogin(true);
	// userSession.setUsername(username);
	// userSession.setLogintime(Calendar.getInstance().getTime().toString());
	// AppUtil.getHttpSession().setAttribute("UserSession", userSession);
	//		
	// return url;
	// }

	/**
	 * 勾选下次记住我
	 */
	// @Action
	private void rememberAction() {
		String prefix = "_erp_";
		// 添加cookie信息
		HttpServletResponse response = AppUtils.getHttpServletResponse();
		Cookie cisReme = new Cookie(prefix + "isReme", isReme == true ? "Y"
				: "N");
		cisReme.setMaxAge(30 * 24 * 60 * 60);
		response.addCookie(cisReme);
		if (isReme) {
			if (!StrUtils.isNull(branch)) {
				Cookie cUsername = new Cookie(prefix + "branch", branch);
				cUsername.setMaxAge(30 * 24 * 60 * 60);
				response.addCookie(cUsername);
			}
			if (!StrUtils.isNull(username)) {
				Cookie cUsername = new Cookie(prefix + "username", username);
				cUsername.setMaxAge(30 * 24 * 60 * 60);
				response.addCookie(cUsername);
			}
			if (!StrUtils.isNull(multilanguage)) {
				Cookie cUsername = new Cookie(prefix + "multilanguage",
						multilanguage);
				cUsername.setMaxAge(30 * 24 * 60 * 60);
				response.addCookie(cUsername);
			}
		} else {
			response.addCookie(new Cookie(prefix + "username", null));
			response.addCookie(new Cookie(prefix + "multilanguage", null));
		}
	}

	/**
	 * 初始化从cookie获取数据
	 */
	private void getCookieInfo() {
		// 初始化从cookie获取数据
		String prefix = "_erp_";
		HttpServletRequest request = AppUtils.getHttpServletRequest();
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			try {
				for (Cookie cookie : cookies) {
					String cname = cookie.getName();
					String cvalue = cookie.getValue();
					if (cvalue != null && ((prefix + "branch").equals(cname))) {
						branchCom.setValue(cvalue);
					}
					if (cvalue != null && ((prefix + "username").equals(cname))) {
						userCom.setValue(cvalue);
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
	
	
	@Action(id = "logout")
	public void logout() {
		try {
			loginService.logout();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
