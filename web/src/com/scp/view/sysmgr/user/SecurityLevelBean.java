package com.scp.view.sysmgr.user;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysUser;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.user.securitylevelBean", scope = ManagedBeanScope.REQUEST)
public class SecurityLevelBean extends GridView {
	
	
	
	@SaveState
	public SysUser data;
	
	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		data = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
		super.grid_ondblclick();
	}
	
	
	
	
	
	
	@Action
	public void toLevel1(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		serviceContext.userMgrService.modifyUserSecurityLevel(id , 1);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void toLevel2(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		serviceContext.userMgrService.modifyUserSecurityLevel(id , 2);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void toLevel3(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		serviceContext.userMgrService.modifyUserSecurityLevel(id , 3);;
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
}
