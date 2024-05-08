package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.bus.BusDocdef;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.senddocchooseBean", scope = ManagedBeanScope.REQUEST)
public class SendDocChooseBean extends GridFormView {

	@SaveState
	@Accessible
	public BusDocdef selectedRowData = new BusDocdef();
	
	
	@SaveState
	@Accessible
	public Long sendid;
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("sendid").trim();
			sendid=Long.valueOf(id);
			qryMap.put("docexpid$", this.sendid);
		}
	}
	
	
	
	@Action
	public void cancel() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.busDocexpLinkMgrService.cancel(ids);
			MessageUtils.alert("cancel success");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	
	
	@Action
	public void docDefine() {
		String url = "./senddocdefine.xhtml";
		AppUtils.openWindow("_docDefine", url );
	}

	
	@Action
	public void shipChoose() {
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipchoose.xhtml?sendid="+this.sendid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}



	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 发送邮件
	 * neo 2014-04-11
	 */
	@Action
	public void sendMail() {
		String url = AppUtils.getContextPath() + "/pages/sysmgr/mail/emailsendedit.xhtml?type=shipdocsend&id="+this.sendid;
		AppUtils.openWindow("_sendMail_shipdocsend", url);
	}


	
}
