package com.scp.view.sysmgr.account;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.account.accountpermissBean", scope = ManagedBeanScope.REQUEST)
public class AccountPermissBean extends GridView {

	@Bind
	@SaveState
	public String userid = "";

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			refreshDtlFrame();
		}
	}

	@Action
	public void grid_onrowselect() {
		String[] ids = this.grid.getSelectedIds();
		if( ids.length > 0 ||ids !=null ){
			userid = ids[0];
		 refreshDtlFrame();
		}
	}
	
	@Bind
	public UIIFrame permissIframe;
	
	private void refreshDtlFrame() {
		permissIframe.load("./accountpermissdtl.xhtml?userid=" + userid);

		update.markAttributeUpdate(permissIframe, "src");
	}
	

	public void refresh() {
		this.grid.reload();
		refreshDtlFrame();
	}
	
}
