package com.scp.view.sysmgr.user;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.sysmgr.user.sainfomgrBean", scope = ManagedBeanScope.REQUEST)
public class SaInfoMgrBean extends FormView {
	
	
	
	@SaveState
	@Accessible
	public SysUser selectedRowData = new SysUser();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			selectedRowData = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
			this.refresh();
		}
	}
	
	
	
	
	@Bind
	public UIWindow uiWindow;
	@Bind
	public String oldPWD;
	@Bind
	public String newPWD1;
	@Bind
	public String newPWD2;
	
	@Action
	public void modifyPWD(){
		this.oldPWD = "";
		this.newPWD1 = "";
		this.newPWD2 = "";
		this.uiWindow.show();
	}
	
	@Action
	public void savePWD(){
		if(!newPWD1.equals(newPWD2)){
			MessageUtils.alert("两次输入密码不一致，请重新输入！");
			this.newPWD1 = "";
			this.newPWD2 = "";
			return;
		}
		
		try {
			if(!serviceContext.userMgrService.checkPwd(selectedRowData , oldPWD)) {
				MessageUtils.alert("当前密码不正确，请重新输入！");
				this.oldPWD = "";
				this.newPWD1 = "";
				this.newPWD2 = "";
				return;
			}
			
			serviceContext.userMgrService.modifyPwd(newPWD1, selectedRowData);
			MessageUtils.alert("密码修改成功！");
			this.uiWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

}
