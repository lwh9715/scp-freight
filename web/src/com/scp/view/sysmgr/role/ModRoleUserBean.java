package com.scp.view.sysmgr.role;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.role.modroleuserBean", scope = ManagedBeanScope.REQUEST)
public class ModRoleUserBean extends GridFormView {

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
	public String name;
	
	@Bind
	@SaveState
	public String roles;
	
	@Bind
	@SaveState
	public String users;
	
	
	@Bind
	public UIWindow detailsWindow;
	
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
		String sql = "SELECT * FROM _sys_module_roleuser WHERE id = "+id+"";
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		this.name = (String)m.get("name");
		this.roles = (String)m.get("roles");
		this.users = (String)m.get("users");
		this.detailsWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "name");
		this.update.markUpdate(UpdateLevel.Data, "roles");
		this.update.markUpdate(UpdateLevel.Data, "users");
	}
	

}
