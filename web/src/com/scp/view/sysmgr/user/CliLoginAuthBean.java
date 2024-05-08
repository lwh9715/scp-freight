package com.scp.view.sysmgr.user;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysUser;
import com.scp.service.sysmgr.SysLoginCtrlService;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.user.cliloginauthBean", scope = ManagedBeanScope.REQUEST)
public class CliLoginAuthBean extends GridView {
	
	@ManagedProperty("#{sysLoginCtrlService}")
	public SysLoginCtrlService sysLoginCtrlService;
	
	@SaveState
	public SysUser data;
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}
	
	
	
	
	
	
	@Action
	public void allow(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		sysLoginCtrlService.modifyUserSecurityAllow(id , true);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void forbid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		sysLoginCtrlService.modifyUserSecurityAllow(id , false);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
}
