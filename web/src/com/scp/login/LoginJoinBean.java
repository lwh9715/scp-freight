package com.scp.login;

import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;

import sun.misc.BASE64Decoder;

import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.utils.Md5Util;

@ManagedBean(name="login.loginjoinBean", scope=ManagedBeanScope.REQUEST)
/**
 * http://hangxun.vicp.io:8989/scp/login/loginjoin.aspx?userid=1&uid=2
 * http://hangxun.vicp.io:8989/scp/login/loginjoin.aspx?uid=12345 Base64()&pwd=md5(Ufms+uid+DxfY) 从第4位开始，取6个字符
 * 
 * http://192.168.0.188:8888/scp/login/loginjoin.aspx?userid=1&uid=2
 */
public class LoginJoinBean {
	
	
	@ManagedProperty("#{serviceContext}")
	public ServiceContext serviceContext;
	
	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if(!isPostback) {
			try {
				//String userid = AppUtils.getReqParam("userid");
				String remoteAddr = AppUtils.getHttpServletRequest().getRequestURL().toString();
				
				String uid = AppUtils.getReqParam("uid");
				BASE64Decoder decoder = new BASE64Decoder();  
				uid = new String(decoder.decodeBuffer(uid));
				String pwd = AppUtils.getReqParam("pwd");
				String fromurl = AppUtils.getReqParam("fromurl");
				
				//fromurl = "http://baidu.com/";
				
				String md5 = Md5Util.MD5AddSalt(uid);
				//System.out.println(md5);
				//System.out.println(pwd+":::::"+md5.substring(3, 9));
				
				if(StrUtils.isNull(md5) || !md5.substring(3, 9).equalsIgnoreCase(pwd)){
					MessageUtils.showMsg("ERROR!");
					//return;
				}
				
				if(StrUtils.isNull(uid)){
					MessageUtils.alert("invalid user!");
					//return;
				}
				l.m.initLocal(MLType.ch);

				String language = AppUtils.getReqParam("l");
				if(language!=null && "en".equals(language)){
					l.m.setMlType(MLType.en);
				}else{
					l.m.setMlType(MLType.ch);
				}
				
				serviceContext.loginService.loginViewByUFMSUid(uid);
				AppUtils.getUserSession().setMlType(l.m.getMlType());
				AppUtils.getUserSession().setFromUrl(fromurl);
				
//				//System.out.println(remoteAddr);
				String addr = remoteAddr.substring(0, remoteAddr.lastIndexOf("/login"));
//				//System.out.println(addr);
//				if(remoteAddr.indexOf("com")>0){
					AppUtils.getUserSession().setScpUrl(addr+"/../main/index.aspx");
//				}else{
//					AppUtils.getUserSession().setScpUrl("http://120.77.83.0:81/scp");
//				}
				
				//System.out.println(AppUtils.getUserSession().getScpUrl());
				
				if(!StrUtils.isNull(ConfigUtils.findUserCfgVal("corpidCurrent", AppUtils.getUserSession().getUserid()))){
					try {
						AppUtils.getUserSession().setCorpidCurrent(Long.parseLong(ConfigUtils.findUserCfgVal("corpidCurrent", AppUtils.getUserSession().getUserid())));
					} catch (NumberFormatException e) {
						AppUtils.getUserSession().setCorpidCurrent(AppUtils.getUserSession().getCorpid());
					} catch (Exception e) {
						MessageUtils.showException(e);
					}
				}
				
				//FacesContext ctxt = FacesContext.getCurrentInstance();
				//SkinManager.getInstance(ctxt).setDefaultSkin("gray");
				
				//String url = AppUtil.getHttpServletRequest().getHeader("referer");
				StringBuffer url = AppUtils.getHttpServletRequest().getRequestURL();
				AppUtils.getHttpServletResponse().sendRedirect(AppUtils.getUserSession().getScpUrl());
				
			} catch (Exception e) {
				MessageUtils.showException(e);
//				e.printStackTrace();
			}
		}
	}

}

