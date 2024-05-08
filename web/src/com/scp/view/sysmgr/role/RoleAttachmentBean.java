package com.scp.view.sysmgr.role;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysRole;
import com.scp.service.sysmgr.RoleMgrService;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.role.roleattachmentBean", scope = ManagedBeanScope.REQUEST)
public class RoleAttachmentBean extends GridFormView {
	
	@SaveState
	public SysRole data = new SysRole();
	
	@Bind
	public UIWindow linkSalesWindow;
	
	@Bind
	private UIIFrame linkSalesIFrame;
	
	
	@ManagedProperty("#{roleMgrService}")
	public RoleMgrService roleMgrService;
	
	@Override
	public void add(){
		data = new SysRole();
		data.setRoletype("F");
		super.add();
//		refreshDtlFrame(true);
	}

	@Action
	public void doDel() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one！");
			return;
		}
		this.roleMgrService.removeRole(ids[0]);
		MessageUtils.alert("OK");
		this.refresh();
	}

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		this.data = roleMgrService.sysRoleDao.findById(this.pkVal);
		super.grid_ondblclick();
		//refreshDtlFrame(false);
	}



	@Override
	public void save() {
		super.save();
		try{
			roleMgrService.saveRole(data);
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		this.alert("OK");
		//refreshDtlFrame(false);
	}
	
	
	@Action
	public void linkUser() {
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String winId = "_userdtl";
		String url = "./userdtl.xhtml?id=" + this.getGridSelectId();
		linkSalesIFrame.setSrc(url);
		update.markAttributeUpdate(linkSalesIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkSalesWindow);
		linkSalesWindow.setTitle("关联组内用户");
		linkSalesWindow.show();
	}
	
	
	
	
	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}

	
//	private void refreshDtlFrame(boolean isNew){
//		if(!isNew){
//			userIframe.load("./roledtl.xhtml?id="+this.pkVal);
//			modIframe.load("./modrole.xhtml?id="+this.pkVal);
//		}else{
//			userIframe.load("/common/blank.html");
//			modIframe.load("/common/blank.html");
//		}
//		update.markAttributeUpdate(userIframe, "src");	
//		update.markAttributeUpdate(modIframe, "src");
//	}

	
//	@Bind
//	public UIIFrame userIframe;
//	@Bind
//	public UIIFrame modIframe;
	
}
