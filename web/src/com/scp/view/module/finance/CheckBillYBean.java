package com.scp.view.module.finance;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.checkbillyBean", scope = ManagedBeanScope.REQUEST)
public class CheckBillYBean extends GridView {
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_checkbillydtl";
		String url = "./checkbillydtl.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void checkBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.reqMgrService.updateCheck(ids,AppUtils.getUserSession().getUsername());
			MessageUtils.alert("审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelCheckBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
		try {
			serviceContext.reqMgrService.updateCancelCheck(ids);
			MessageUtils.alert("取消审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
