package com.scp.login;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;

public class ViewCtrl implements PhaseListener {

//	@Override
	public void afterPhase(PhaseEvent event) {
	}

//	@Override
	public void beforePhase(PhaseEvent event) {
		ctrl();
	}

//	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	private void ctrl(){
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = AppUtils.getHttpServletRequest();
		HttpServletResponse response = AppUtils.getHttpServletResponse();
		UIViewRoot uiv = new UIViewRoot();
		String viewid = fc.getViewRoot().getViewId();
		//检查页面：/main/   /pages/
		if(viewid.contains("/pages/") || viewid.contains("/main/")){
			if(viewid.contains("/crm/") || viewid.contains("src=crm")){
				return;
			}
			Application app = fc.getApplication();
			if(AppUtils.getHttpSession() == null){
				try {
					ApplicationConf applicationConf = (ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf");
//					boolean isUseDzz = applicationConf.getIsUseDzz();
//					if(!isUseDzz){
//						AppUtils.getHttpServletResponse().sendRedirect("/scp/login_new/relogin2.html");
//					}else{
						AppUtils.getHttpServletResponse().sendRedirect("/scp/login/relogin.html");
//					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			Object session = AppUtils.getHttpSession().getAttribute("UserSession");
	        if(session == null) {
	        	ViewHandler vh = app.getViewHandler();
	        	
	        	//如果是网上客户直接返回到登陆界面
	        	UIViewRoot newRoot = null;
	        	if(viewid.contains("/cs/")){
	        		newRoot =vh.createView(fc, "/cs/login/relogin.html");
	        	}else {
	        		//System.out.println("ViewCtrl:"+viewid);
	        		newRoot =vh.createView(fc, "/login/relogin.html");
	        		try {
	        			//AppUtils.getHttpServletRequest().getSession().invalidate();
	        			////System.out.println("sessionid:"+AppUtils.getHttpServletRequest().getSession().getId());
						AppUtils.getHttpServletResponse().sendRedirect("/scp/login/relogin.html");
//	        			RequestDispatcher rd = AppUtils.getHttpServletRequest().getRequestDispatcher("/scp/login/relogin.html");
//	        			rd.forward(request,response);
						return;
					} catch (IOException e) {
						e.printStackTrace();
//					} catch (ServletException e) {
//						e.printStackTrace();
					}
	        	}
	    		fc.setViewRoot(newRoot);
	        	
	        	fc.renderResponse();
	        }
		}
	}
	
	private void ctrlDemo(){
		//FacesContext fc = event.getFacesContext().getCurrentInstance();
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		// 通过表达式获取后台Bean的userType属性，该属性用于判断用户是否已经登陆
		// SingSession是一个具有会话生存期的后台Bean，当用户登陆后，其userType属性将被设置为1，标记用户已经登陆
//		int userType = (Integer) app.evaluateExpressionGet(fc,"#{SingSession.userType}", Integer.class);
		// 下面3行用户取得当前呈现页面的视图ID，也就是呈现页面的文件名，并对文件名进行适当处理
		UIViewRoot uiv = new UIViewRoot();
		String viewid = fc.getViewRoot().getViewId();
		viewid = viewid.substring(1, viewid.length() - 4);
		// 判断权限来控制对相关页面视图的访问
//		if (viewid.equals("default") && userType < 1) {
//			uiv.setViewId("/login.jsp");
//			fc.setViewRoot(uiv);
//		}
//		if (viewid.equals("listAccount") && userType < 1) {
//			uiv.setViewId("/login.jsp");
//			fc.setViewRoot(uiv);
//		}
	}
}
