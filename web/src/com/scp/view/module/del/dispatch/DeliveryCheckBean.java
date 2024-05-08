package com.scp.view.module.del.dispatch;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.del.dispatch.deliverycheckBean", scope = ManagedBeanScope.REQUEST)
public class DeliveryCheckBean extends GridView {
	
	@Action
	public void add() {
		String winId = "deliverydtlcheck";
		String url = "./deliverydtlcheck.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "deliverydtlcheck";
		String url = "./deliverydtlcheck.faces?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void checkBatch() {
		
		try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}
			serviceContext.deliveryMgrService.updateCheck(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void cancelCheckBatch() {
		
		try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}
			serviceContext.deliveryMgrService.updateCancelCheck(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void sendBatch() {
		
		try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}
			serviceContext.deliveryMgrService.updateSend(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void cancelSendBatch() {
		
		try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}
			serviceContext.deliveryMgrService.updateCancelSend(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
