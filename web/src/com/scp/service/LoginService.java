package com.scp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.scp.base.AppSessionLister;
import com.scp.base.ApplicationConf;
import com.scp.base.UserSession;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.sys.SysLogDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.dao.sys.SysUseronlineDao;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.LoginException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.exception.UnAuthModuleException;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.SysUseronline;
import com.scp.service.sysmgr.UserMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.EncoderHandler;
import com.scp.util.GetAddressByIp;
import com.scp.util.StrUtils;



@Component
public class LoginService{

	@Resource
	public SysUserDao sysUserDao;

	@Resource
	public SysUseronlineDao sysUseronlineDao;

	@Resource
	public UserMgrService userMgrService;

	@Resource
	public ApplicationConf applicationConf;

	//简单的验证用户名和密码是否相同
	public void loginView(String username, Object password){
		username = username.replaceAll("'", "''").trim();
		String usernameUpper = username.toUpperCase();
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("UPPER(code)='" + usernameUpper + "' AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			 long userid = sysUser.getId();
			String ciphertext = sysUser.getCiphertext();
			String secretkey = sysUser.getSecretkey();
			String encodePassword = userMgrService.decrypt(ciphertext, secretkey);
			initApplicationConf();
			if(!sysUser.getIsinvalid()) {
				throw new LoginException("用户已经无效，请联系管理员！(User is not valid,please contect admin!)");
			}

			if(!StrUtils.isNull(encodePassword) && encodePassword.equals(password)){
				//验证成功
			}else{
				throw new LoginException(username+"密码错误,请重新输入");
			}

			}
	}


	/**
	 * dzz登录
	 * @param uid
	 * @return
	 */
	public String loginViewByDzzUid(String uid){
		String errMsg = null;

		UserSession userSession = new UserSession();
		userSession.setSessionid(AppUtils.getHttpSession().getId());
//		userSession.setUsername(username);

		HttpSession curSession = AppUtils.getHttpSession();
		userSession.setSessionid(curSession.getId());
		curSession.setAttribute("UserSession", userSession);
		// 在ZLP数据库中验证的用户名和密码是否匹配
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("fmsid=" + uid + " AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			if(!sysUser.getIsinvalid()) {
				throw new LoginException("用户已经无效，请联系管理员！(User is not valid,please contect admin!)");
			}
			//if(AppUtil.isDebug == true || encodePassword.equals(password) ){
				userSession.setDzzuid(uid);
				userSession.setLogin(true);
				userSession.setUserid(sysUser.getId());
				userSession.setUsercode(sysUser.getCode());
				userSession.setUsername(sysUser.getNamec());
				userSession.setMlType(MLType.ch);
//				userSession.setCustomerid(sysUser.getCustomerid());
				userSession.setCorpid(sysUser.getSysCorporation().getId());
				userSession.setActsetid(sysUser.getActsetid());
				userSession.setBaseCurrency(sysUser.getSysCorporation().getBasecurrency());

				HttpServletRequest request = AppUtils.getHttpServletRequest();
				String ip = request.getRemoteAddr();
				if(StrUtils.isNull(ip))ip="0.0.0.0";

				this.onlinecount(sysUser);

				String ipaddr = GetAddressByIp.GetAddressByIp(ip);
				SysUseronline sysUseronline = new SysUseronline();
				sysUseronline.setUserid(sysUser.getId());
				sysUseronline.setIsonline("Y");
				sysUseronline.setLogintime(new Date());
				sysUseronline.setIp(ip);
				sysUseronline.setSessionid(userSession.getSessionid());
				sysUseronline.setIpaddr(ipaddr);
				sysUseronline.setIsvalid(true);

				AppSessionLister.addSession(curSession);

				sysUseronlineDao.create(sysUseronline);

				initApplicationConf();
				initUserConfig();
		}else{
			throw new LoginException("不存在用户:" + uid+ "(Not exists user)");
		}
		return errMsg;
	}

	/**
	 * UFMS登录
	 * @param uid
	 * @return
	 */
	public String loginViewByUFMSUid(String uid){
		String errMsg = null;

		UserSession userSession = new UserSession();
		userSession.setSessionid(AppUtils.getHttpSession().getId());
//		userSession.setUsername(username);

		HttpSession curSession = AppUtils.getHttpSession();
		userSession.setSessionid(curSession.getId());
		curSession.setAttribute("UserSession", userSession);
		// 在ZLP数据库中验证的用户名和密码是否匹配
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("pkid_remote ='" + uid + "' AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			if(!sysUser.getIsinvalid()) {
				throw new LoginException("用户已经无效，请联系管理员！(User is not valid,please contect admin!)");
			}
			//if(AppUtil.isDebug == true || encodePassword.equals(password) ){
				userSession.setDzzuid(uid);
				userSession.setLogin(true);
				userSession.setUserid(sysUser.getId());
				userSession.setUsercode(sysUser.getCode());
				userSession.setUsername(sysUser.getNamec());
				userSession.setMlType(MLType.ch);
//				userSession.setCustomerid(sysUser.getCustomerid());
				try {
					userSession.setCorpid(sysUser.getSysCorporation().getId());
					userSession.setActsetid(sysUser.getActsetid());
					userSession.setBaseCurrency(sysUser.getSysCorporation().getBasecurrency());
				} catch (Exception e) {
					e.printStackTrace();
				}


				HttpServletRequest request = AppUtils.getHttpServletRequest();
				String ip = request.getRemoteAddr();
				if(StrUtils.isNull(ip))ip="0.0.0.0";

				//Neo20180329 暂时拿掉这个处理，ufms里面跳转如果刚标记为失效，马上又申请session会偶尔有错误
				//this.onlinecount(sysUser);

				String ipaddr = GetAddressByIp.GetAddressByIp(ip);
				SysUseronline sysUseronline = new SysUseronline();
				sysUseronline.setUserid(sysUser.getId());
				sysUseronline.setIsonline("Y");
				sysUseronline.setLogintime(new Date());
				sysUseronline.setIp(ip);
				sysUseronline.setSessionid(userSession.getSessionid());
				sysUseronline.setIpaddr(ipaddr);
				sysUseronline.setIsvalid(true);

				AppSessionLister.addSession(curSession);

				sysUseronlineDao.create(sysUseronline);

				initApplicationConf();
				initUserConfig();

		}else{
			throw new LoginException("不存在用户:" + uid+ "(Not exists user)");
		}
		return errMsg;
	}


	public String loginLinkView(String username, String password) {
		String errMsg = null;

		UserSession userSession = new UserSession();
		userSession.setSessionid(AppUtils.getHttpSession().getId());
		userSession.setUsername(username);

		HttpSession curSession = AppUtils.getHttpSession();
		userSession.setSessionid(curSession.getId());
		curSession.setAttribute("UserSession", userSession);
		// 在ZLP数据库中验证的用户名和密码是否匹配
		username = username.replaceAll("'", "''").trim();
		String usernameUpper = username.toUpperCase();
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("UPPER(code)='" + usernameUpper + "' AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			 long userid = sysUser.getId();
			String ciphertext = sysUser.getCiphertext();
			String secretkey = sysUser.getSecretkey();

			String encodePassword = userMgrService.decrypt(ciphertext, secretkey);

			if(!sysUser.getIsinvalid()) {
				throw new LoginException("用户已经无效，请联系管理员！(User is not valid,please contect admin!)");
			}

			if(AppUtils.isDebug == true ||encodePassword.equals(password)){
//				if(encodePassword.equals(password)) {//测试控制登录
				userSession.setLogin(true);
				userSession.setUserid(sysUser.getId());
				userSession.setUsercode(sysUser.getCode());
				userSession.setMlType(MLType.ch);
//				userSession.setCustomerid(sysUser.getCustomerid());
				userSession.setCorpid(sysUser.getSysCorporation().getId());
				userSession.setActsetid(sysUser.getActsetid());
				userSession.setBaseCurrency(sysUser.getSysCorporation().getBasecurrency());

				HttpServletRequest request = AppUtils.getHttpServletRequest();
				String ip = request.getRemoteAddr();
				if(StrUtils.isNull(ip))ip="0.0.0.0";
//				userSession.setIp(ip);
//				userSession.setLogintime(new Date().toLocaleString());
				SysUseronline sysUseronline = new SysUseronline();
				sysUseronline.setUserid(sysUser.getId());
				sysUseronline.setIsonline("Y");
				sysUseronline.setLogintime(new Date());
				sysUseronline.setIp(ip);
				sysUseronline.setIsvalid(true);
				sysUseronline.setSessionid(userSession.getSessionid());

				sysUseronlineDao.create(sysUseronline);

				initApplicationConf();
				initUserConfig();

				if("admin".equals(username)){
					userSession.setAdmin(true);
				}
			}else{
				 long count = sysUser.getLogincount();
				count = count +1;
				String sql = "UPDATE sys_user SET logincount = "+count+" WHERE id = "+userid+" AND  isinvalid = TRUE AND isdelete = FALSE";
				String sql2= "SELECT f_log('"+username+"','登录失败"+count+"',"+userid+");";
				sysUserDao.executeSQL(sql);
				sysUserDao.executeQuery(sql2);
				if(count<5){
					throw new LoginException("密码错误" + count + "次，还有"+(5-count)+"机会哦.(提示：如果密码连续输错5次，帐号将被锁定10分钟。请慎重输入!\n(Password error "+ count +", You have "+ (5 - count) +" opportunity oh. (hint: if the wrong password for two times, the account will be locked for 10 minutes. Please input!))");
				}else{
					throw new LoginException("您密码输入错误5次,此帐号将被锁定10分钟,请10分钟以后输入!\n(Your password input error 5 times, this account will be locked for 10 minutes, please enter 10 minutes later)");
				}
			}
		}else{
			username = username.replaceAll("'", "");
			throw new LoginException("不存在用户名:" + username+ "(Not exists user)");
		}
		return errMsg;

	}

	public String loginView(String username, Object password, MLType mlType){
		String errMsg = null;

		UserSession userSession = new UserSession();
		userSession.setSessionid(AppUtils.getHttpSession().getId());
		HttpSession curSession = AppUtils.getHttpSession();
		userSession.setSessionid(curSession.getId());
		curSession.setAttribute("UserSession", userSession);
		username = username.replaceAll("'", "''").trim();
		password = password.toString().trim();
		String usernameUpper = username.toUpperCase();
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("(UPPER(code) ='" + usernameUpper + "' OR UPPER(namec)='" + usernameUpper + "' OR UPPER(namee)='" + usernameUpper + "' OR UPPER(email1)='" + usernameUpper + "') AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			long userid = sysUser.getId();
			String ciphertext = sysUser.getCiphertext();//password
			String secretkey = sysUser.getSecretkey();//salt

			if(StrUtils.isNull(ciphertext)) {
				throw new LoginException("用户密码为空，请联系管理员！(User password is null ,please contect admin!)");
			}

			if(!sysUser.getIsinvalid()) {
				throw new LoginException("用户已经无效，请联系管理员！(User is not valid,please contect admin!)");
			}

			String isAccountLogin = ConfigUtils.findSysCfgVal("open_account_login_auth");

			if (isAccountLogin.equals("Y") && AppUtils.isDebug != true) {
				//等钉钉扫码过渡期结束后，在账号登录处添加改判断
				if(!sysUser.getIsaccuser()) {
					throw new LoginException("请使用钉钉扫码登陆，如有问题，请联系管理员！(Please use nail scanning code to log in. If you have any questions, please contact the administrator!)");
				}
			}

			if(AppUtils.isDebug == true || ciphertext.equals(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(String.valueOf(password))+secretkey))){
//				if(encodePassword.equals(password)) {//测试控制登录
				userSession.setLogin(true);
				userSession.setUserid(sysUser.getId());
				userSession.setUsercode(sysUser.getCode());
				userSession.setUsername(sysUser.getNamec());
				userSession.setMlType(mlType);
//				userSession.setCustomerid(sysUser.getCustomerid());
				userSession.setCorpid(sysUser.getSysCorporation().getId());
				userSession.setActsetid(sysUser.getActsetid());
				userSession.setBaseCurrency(sysUser.getSysCorporation().getBasecurrency());

				HttpServletRequest request = AppUtils.getHttpServletRequest();
				String ip = request.getRemoteAddr();
				if(StrUtils.isNull(ip))ip="0.0.0.0";

				SysUseronline sysUseronline = new SysUseronline();
				sysUseronline.setUserid(sysUser.getId());
				sysUseronline.setIsonline("Y");
				sysUseronline.setLogintime(new Date());
				sysUseronline.setIp(ip);
				sysUseronline.setSessionid(userSession.getSessionid());
				sysUseronline.setIsvalid(true);
				sysUseronlineDao.create(sysUseronline);

				initApplicationConf();
				initUserConfig();

				if("admin".equals(username)){
					userSession.setAdmin(true);
				}
			}else{
				 long count = sysUser.getLogincount();
				count = count +1;
				String sql = "UPDATE sys_user SET logincount = "+count+" WHERE id = "+userid+" AND  isinvalid = TRUE AND isdelete = FALSE";
				String sql2= "SELECT f_log('"+username+"','登录失败"+count+"',"+userid+");";
				sysUserDao.executeSQL(sql);
				sysUserDao.executeQuery(sql2);
				if(count<5){
					throw new LoginException("密码错误" + count + "次，还有"+(5-count)+"机会哦.(提示：如果密码连续输错5次，帐号将被锁定30分钟。请慎重输入!\n(Password error "+ count +", You have "+ (5 - count) +" opportunity oh. (hint: if the wrong password for two times, the account will be locked for 30 minutes. Please input!))");
				}else{
					throw new LoginException("您密码输入错误5次,此帐号将被锁定30分钟,请30分钟以后输入!\n(Your password input error 5 times, this account will be locked for 30 minutes, please enter 30 minutes later)");}
			}
		}else{
			username = username.replaceAll("'", "");
			throw new LoginException("不存在用户名:" + username+ "(Not exists user)");
		}
		return errMsg;
	}



	public String loginView(String username, Object password, MLType mlType,HttpSession  curSession){
		String errMsg = null;

		UserSession userSession = new UserSession();
		//userSession.setSessionid(AppUtil.getHttpSession().getId());
		userSession.setSessionid(curSession.getId());
		userSession.setUsername(username);

		//HttpSession curSession = AppUtil.getHttpSession();

		curSession.setAttribute("UserSession", userSession);
		// 验证的用户名和密码是否匹配
		username = username.replaceAll("'", "''").trim();
		String usernameUpper = username.toUpperCase();
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("UPPER(code)='" + usernameUpper + "' AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			String ciphertext = sysUser.getCiphertext();
			String secretkey = sysUser.getSecretkey();

			if(!sysUser.getIsinvalid()) {
				throw new LoginException("用户已经无效，请联系管理员！(User is not valid,please contect admin!)");
			}
			String encodePassword = userMgrService.decrypt(ciphertext, secretkey);
			if(AppUtils.isDebug == true ||encodePassword.equals(password)) {
//			if(encodePassword.equals(password)) {//测试控制登录
				userSession.setLogin(true);
				userSession.setUserid(sysUser.getId());
				userSession.setUsercode(sysUser.getCode());
				userSession.setMlType(mlType);
				userSession.setCorpid(sysUser.getSysCorporation().getId());
				userSession.setActsetid(sysUser.getActsetid());

				SysUseronline sysUseronline = new SysUseronline();
				sysUseronline.setUserid(sysUser.getId());
				sysUseronline.setIsonline("Y");
				sysUseronline.setLogintime(new Date());
				sysUseronline.setSessionid(userSession.getSessionid());
				sysUseronline.setIsvalid(true);
				sysUseronlineDao.create(sysUseronline);

				initApplicationConf();
				initUserConfig();

				if("admin".equals(username)){
					userSession.setAdmin(true);
				}
			}else{
				throw new LoginException("密码错误(Wrong password!)");
			}
		}else{
			username = username.replaceAll("'", "");
			throw new LoginException("不存在用户名:" + username+ "(Not exists user)");
		}
		return errMsg;
	}

	private void initUserConfig() {

	}

	@Resource
	public SysLogDao sysLogDao;

	public String logout() {
		List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere("sessionid = '" + AppUtils.getUserSession().getSessionid() + "' AND isonline = 'Y'");
		if(sysUseronlines != null && sysUseronlines.size() > 0) {
			for (SysUseronline sysUseronline : sysUseronlines) {
				sysUseronline.setIsonline("N");
				sysUseronline.setLogouttime(new Date());
				sysUseronline.setIsvalid(false);
				sysUseronlineDao.modify(sysUseronline);
			}
		}
		SysLog syslog = new SysLog();
		syslog.setInputer(AppUtils.getUserSession().getUsername());
		syslog.setLogdesc("Logout");
		syslog.setLogtime(new Date());
		syslog.setLogtype("BI");
		sysLogDao.create(syslog);
		AppSessionLister.deleteSession(AppUtils.getHttpSession());
		//AppUtils.getHttpSession().setAttribute("UserSession", null);
		AppUtils.getHttpSession().invalidate();
		return "view:redirect:/login_new/login";
	}

	public void check(String username) {
		//设置：启用安全登录客户端
		if("Y".equals(ConfigUtils.findSysCfgVal("sys_loginsafe"))) {
			SysUser sysUser = checkUser(username);
			if(3 != sysUser.getSecuritylevel()) {//3级为无限制级别，3以下都需要通过客户端登陆
				throw new LoginException("当前用户安全级别较低，不能使用web方式登录，请下载客户端，并通过桌面程序登陆，如未授权，请联系管理员授权开通"
						+"\nThe current user security level is low, can not login by web, download the client and login by the desktop program, if not authorized, please contact the administrator");
			}
		}
	}

	public SysUser checkUser(String usercode) {
		SysUser sysUser;
		try {
			String usernameUpper = usercode.toUpperCase();
			sysUser = sysUserDao.findOneRowByClauseWhere("(UPPER(code) = '" + usernameUpper + "' OR UPPER(namec)='" + usernameUpper + "' OR UPPER(namee)='" + usernameUpper + "' OR UPPER(email1)='" + usernameUpper + "') AND isinvalid = TRUE AND isdelete = FALSE");
			return sysUser;
		} catch (NoRowException e) {
			throw new LoginException("用户名不存在" + "("+usercode+")"+ "(Not exists user)");
		} catch (MoreThanOneRowException e) {
			throw new LoginException("用户名无效" + "("+usercode+")"+ "(Not exists user)");
		}
	}


	/**
	 * 按照用户ID和模块Code检查权限
	 * @param userid
	 * @param moduleCode
	 * @return
	 */
	public void checkModule(Long userid , String moduleCode) {
		String sql =
			"\nSELECT"+
			"\n1"+
			"\nFROM sys_module m, sys_modinrole i, sys_role r, sys_userinrole o"+
			"\nWHERE m.id = i.moduleid"+
			"\nAND UPPER(m.code) =  '" + moduleCode.toUpperCase() + "'" +
			"\nAND i.roleid = r.id"+
			"\nAND r.id = o.roleid"+
			"\nAND m.isctrl = 'N'"+
			"\nAND o.userid = "+userid+
			"\nAND m.isdelete = false";
		List list = sysUserDao.executeQuery(sql);
		if(list != null && list.size() >= 1) {

		}else {
			throw new UnAuthModuleException(moduleCode);
		}
	}

	public boolean checkLogin(String username) {
		AppUtils.checkUserCount();
		String usernames = username.replaceAll("'", "''").trim();
		String usernameUpper = usernames.toUpperCase();
		List<SysUser> userList = sysUserDao.findAllByClauseWhere(" (UPPER(code)='" + usernameUpper + "' OR UPPER(namec)='" + usernameUpper + "' OR UPPER(namee)='" + usernameUpper + "' OR UPPER(email1)='" + usernameUpper + "') AND  isinvalid = TRUE AND isdelete = FALSE");
		if (userList != null && userList.size() > 0) {
			SysUser sysUser = userList.get(0);
			long count = sysUser.getLogincount();
			if (count == 5) {
				long userid = sysUser.getId();
				Date lasttime = null;
				String sql = " SELECT  logtime  FROM sys_log  WHERE  refid = "
						+ userid
						+ "  AND  isdelete = FALSE ORDER BY logtime DESC ";
				List list = sysUserDao.executeQuery(sql);
				if (list != null && list.size() > 0) {
					lasttime = (Date) list.get(0);
				} else {
					throw new LoginException("密码输错超过5次，并且发生异常,没有找到历史登录时间,无法登录");
				}
				Date date = lasttime;// 得到最后登录时间
				date.setMinutes(date.getMinutes() + 31);// 得到限定时间
				Date now = new Date();
				if (now.before(date)) {
					long second = ((date.getTime() - now.getTime()) / (1000));// 剩余多少分钟
					if (second <= 60) {
						throw new LoginException(second + "秒以后，才可登录");
					} else {
						long minute = second / 60;
						throw new LoginException(minute + "分钟以后，才可登录");
					}
				}
				this.setLogincount(username);
				return true;
			}
			onlinecount(sysUser);
			return true;
		}
		throw new LoginException("请检查用户名是否存在");
	}

	private void onlinecount(SysUser sysUser) {
		int onlinecount = sysUser.getOnlinecount();
		if(onlinecount == 1){
			SysUseronlineDao sysUseronlineDao = (SysUseronlineDao) AppUtils.getBeanFromSpringIoc("sysUseronlineDao");
			// 将sys_useronline中的isvalid 设为无效
			String sql = "isdelete = false AND isonline = 'Y' AND userid = " + sysUser.getId()+"";
			List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere(sql);
			for(int i = 0;i<sysUseronlines.size();i++){
				String sessionid  = sysUseronlines.get(i).getSessionid();
				//System.out.println("sessionid delete..." + sessionid);
				HttpSession oldAndOnLineSession = AppSessionLister.sessionMap.get(sessionid);
				AppSessionLister.deleteSession(oldAndOnLineSession);
				if (oldAndOnLineSession != null) oldAndOnLineSession.invalidate();
			}
		}
	}

	private void initApplicationConf(){
		applicationConf.setSaas(false);
		if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_saas"))){
			applicationConf.setSaas(true);
		}
	}


	/**
	 *
	 * 当每次登录成功，设置logincount为0
	 */
	public void setLogincount(String username) {
		String usernames = username.replaceAll("'", "''").trim();
		String usernameUpper = usernames.toUpperCase();
		String sql = "UPDATE sys_user SET logincount = 0 WHERE UPPER(code)='"
				+ usernameUpper
				+ "' AND isinvalid = TRUE AND isdelete = FALSE ";
		sysUserDao.executeSQL(sql);
	}

	/**
	 * 当用户名为Admin,发送邮件
	 *
	 * @throws Exception
	 */
	public void sendMail() throws Exception {
		if (AppUtils.isDebug == false) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String data = sdf.format(date);
			// InetAddress addr = InetAddress.getLocalHost();
			HttpServletRequest request = AppUtils.getHttpServletRequest();
			String ip = request.getRemoteAddr().toString();
			String address = request.getRemoteHost().toString();
			// String ServerName = request.getServerName().toString();//获取服务器名称
			// String ip=addr.getHostAddress().toString();//获得本机IP
			// String address=addr.getHostName().toString();//获得本机名称
			String content = "登录IP:" + ip + ";\n本机名称:" + address + ";\nadmin于"
					+ data + "时间登录系统";
			String subject = "admin登录通知";
			String acceptAddress = "89115698@qq.com";
			String ccAddress = "";
			EMailSendUtil.sendEmailBySystem(content, subject, acceptAddress,
					ccAddress);
		}
	}
	/**
	 * 判断是否为最新客户端版本
	 */
	public boolean getNewVersion() {
		String logversion = AppUtils.getReqParam("ver");// 当前登录版本型号
		if (logversion == null || "".equals(logversion)) {
			return false;
		} else {
			String sysversion = ConfigUtils.findSysCfgVal("Login_Cli_Version");// 系统设置版本型号
			String log = logversion.replace(".", "");
			String sys = sysversion.replace(".", "");// 去点转换
			int sysver = Integer.valueOf(logversion.replace(".", ""));
			int logver = Integer.valueOf(sysversion.replace(".", ""));
			if (sysver == logver) {
				return true;// 最新版本
			}
			return false;// 不是新版本
		}
	}

	/**
	 * 根据uuid查找到sysuser表中的code
	 * @param uuid
	 * @return
	 */
	public String getUserCodeOnUuid(String uuid){
		try {
			SysUseronline sysUseronline = sysUseronlineDao.findOneRowByClauseWhere("loginuuid = '"+uuid+"'");
			SysUser sysuser = sysUserDao.findOneRowByClauseWhere("opneid = '"+sysUseronline.getOpenid()+"'");
			return sysuser.getCode();
		} catch (NoRowException e) {
			throw new LoginException("用户没有绑定微信");
		} catch (MoreThanOneRowException e) {
			throw new LoginException("用户名无效");
		}
	}

	/**
	 * 查用户的openid是否被赋值
	 * @param userid
	 * @return
	 */
	public Boolean isOpenidByUserid(Long userid){
		SysUser user = sysUserDao.findById(userid);
		String opneids = (user!=null?user.getOpneid():"");
		if(!StrUtils.isNull(opneids)){
			return true;
		}
		return false;
	}

    /**
     * 钉钉扫码登录
     */
    public String ddLoginView(String username, String idcode, String mobile, MLType mlType) {
        String errMsg = null;

        UserSession userSession = new UserSession();
        userSession.setSessionid(AppUtils.getHttpSession().getId());

        HttpSession curSession = AppUtils.getHttpSession();
        userSession.setSessionid(curSession.getId());
        curSession.setAttribute("UserSession", userSession);
        username = username.replaceAll("'", "''").trim();
        String usernameUpper = username.toUpperCase();
        //调整钉钉登录，用户名后缀模糊查询+手机号码匹配 OR 工号且工号不为空
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("namec like '" + usernameUpper + "%' " +
                "AND (mobilephone = '" + mobile + "' OR (idcode = '" + idcode + "' AND idcode != ''))  AND isinvalid = TRUE AND isdelete = FALSE");
        if (userList != null && userList.size() > 0) {
            SysUser sysUser = userList.get(0);
            long userid = sysUser.getId();
            if (!sysUser.getIsinvalid()) {
                throw new LoginException("用户登录异常，请联系管理员！(User login exception,please contect admin!)");
            }
            if (sysUser.getIdcode().equals("") && sysUser.getMobilephone().equals("")) {
                throw new LoginException("用户登录异常，请联系管理员！(User login exception,please contect admin!)");
            }

            if (sysUser.getIdcode().equals(idcode) || sysUser.getMobilephone().equals(mobile)) {
                userSession.setLogin(true);
                userSession.setUserid(sysUser.getId());
                userSession.setUsercode(sysUser.getCode());
                userSession.setUsername(sysUser.getNamec());
                userSession.setMlType(mlType);
                userSession.setCorpid(sysUser.getSysCorporation().getId());
                userSession.setActsetid(sysUser.getActsetid());
                userSession.setBaseCurrency(sysUser.getSysCorporation().getBasecurrency());
                HttpServletRequest request = AppUtils.getHttpServletRequest();
                String ip = request.getRemoteAddr();
                if (StrUtils.isNull(ip)) ip = "0.0.0.0";
                SysUseronline sysUseronline = new SysUseronline();
                sysUseronline.setUserid(sysUser.getId());
                sysUseronline.setIsonline("Y");
                sysUseronline.setLogintime(new Date());
                sysUseronline.setIp(ip);
                sysUseronline.setSessionid(userSession.getSessionid());
                sysUseronline.setIsvalid(true);
                sysUseronlineDao.create(sysUseronline);
                initApplicationConf();
                initUserConfig();
                if ("admin".equals(username)) {
                    userSession.setAdmin(true);
                }
            }else {
				username = username.replaceAll("'", "");
				throw new LoginException(username + "扫码登录异常，请联系管理员!(Exception in scanning code login. Please contact the admin!)");
			}
        } else {
            username = username.replaceAll("'", "");
            throw new LoginException("用户名不存在:" + username + "(User name does not exist)");
        }
        return errMsg;
    }
}