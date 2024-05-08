package com.scp.view.module.del.dispatch;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.model.del.DelDeliverydtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.del.dispatch.billbackBean", scope = ManagedBeanScope.REQUEST)
public class BillBackBean extends GridFormView {

	
	@SaveState
	@Accessible
	public DelDeliverydtl selectedRowData = new DelDeliverydtl();
	
	@Override
	public void add() {
		selectedRowData = new DelDeliverydtl();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.deliverydtlMgrService.delDeliverydtlDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.deliverydtlMgrService.saveDtlData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	private void disableAllButton(Boolean isCheck) {

	}
	
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		if(editWindow != null){
			editWindow.show();
			showAttachmentIframe();
		}
		
	}
	
	@Bind
	public UIIFrame attachmentIframe;

	
	public void showAttachmentIframe() {
		try{
				attachmentIframe.load(AppUtils.getContextPath()
						+ "/pages/module/common/attachment.faces?linkid="
						+ this.pkVal);
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}