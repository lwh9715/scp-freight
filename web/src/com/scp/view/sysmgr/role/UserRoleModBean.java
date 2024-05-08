package com.scp.view.sysmgr.role;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.role.userrolemodBean", scope = ManagedBeanScope.REQUEST)
public class UserRoleModBean extends GridFormView {

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Bind
	@SaveState
	public Long fromuserid;
	
	@Bind
	@SaveState
	public Long touserid;
	
	@Bind
	public UIWindow detailsWindow;
	
	
	@Action
	public void modcopy(){
		
		showProcess();
	}
	
	public void showProcess() {
		this.detailsWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "fromuserid");
	}
	
	//权限复制
	@Action
	public void savemod(){
		if(this.fromuserid ==null||this.fromuserid==0){
			MessageUtils.alert("请选择需要from-user");
			return;
		}
		if(this.touserid ==null||this.touserid==0){
			MessageUtils.alert("请选择需要to-user");
			return;
		}
		try {
			String sql = "SELECT f_sys_sys_userinrole_copy("+fromuserid+","+touserid+",'F');";
			this.serviceContext.userMgrService.sysUserDao.executeQuery(sql);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	//权限相同
	@Action
	public void savemod2(){
		if(this.fromuserid ==null||this.fromuserid==0){
			MessageUtils.alert("请选择需要from-user");
			return;
		}
		if(this.touserid ==null||this.touserid==0){
			MessageUtils.alert("请选择需要to-user");
			return;
		}
		try {
			String sql = "SELECT f_sys_sys_userinrole_copy("+fromuserid+","+touserid+",'E');";
			this.serviceContext.userMgrService.sysUserDao.executeQuery(sql);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Bind
	@SaveState
	public String name;
	
	@Bind
	@SaveState
	public String roles;
	
	@Bind
	@SaveState
	public String users;
	
	
	@Bind
	public UIWindow detailsWindow2;
	
	@Override
	public void grid_ondblclick(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length >1) {
			MessageUtils.alert("Please choose one at most!");
			return;
		}
		showProcess(Long.valueOf(ids[0]));
	}
	
	public void showProcess(Long id) {
		String sql = "SELECT * FROM _sys_user_rolemod WHERE id = "+id+"";
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		this.name = (String)m.get("code");
		this.roles = (String)m.get("roles");
		this.users = (String)m.get("modules");
		this.detailsWindow2.show();
		this.update.markUpdate(UpdateLevel.Data, "name");
		this.update.markUpdate(UpdateLevel.Data, "roles");
		this.update.markUpdate(UpdateLevel.Data, "users");
	}

}
