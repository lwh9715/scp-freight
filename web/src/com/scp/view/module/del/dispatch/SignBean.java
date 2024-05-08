package com.scp.view.module.del.dispatch;

import java.util.Calendar;
import java.util.Date;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.del.DelDeliverydtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.del.dispatch.signBean", scope = ManagedBeanScope.REQUEST)
public class SignBean extends GridFormView {

	
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
	
	@Action
	public void issignAjaxSubmit() {
		Boolean isSign = selectedRowData.getIssign();
		String updater = AppUtils.getUserSession().getUsername();
		Date date = new Date();
		String sql = "";
		try {
			if (selectedRowData.getIsreturn())
				throw new CommonRuntimeException(
						"Current is return,plese unreturn first!");
			if (isSign) {
				sql = "UPDATE del_deliverydtl SET issign = TRUE ,signtime = '"
						+ date + "' WHERE id ="
						+ this.pkVal + ";";
			} else {
				sql = "UPDATE del_deliverydtl SET issign = FALSE ,signtime = NULL,signer = NULL WHERE id ="
						+ this.pkVal + ";";
			}
			serviceContext.deliverydtlMgrService.delDeliverydtlDao
					.executeSQL(sql);
			doServiceFindData();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			refresh();
			this.disableAllButton(isSign);
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIssign(!isSign);
			selectedRowData.setSigner(isSign ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setSigntime(isSign ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			this.disableAllButton(!isSign);
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIssign(!isSign);
			selectedRowData.setSigner(isSign ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setSigntime(isSign ? null : Calendar
					.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			this.disableAllButton(!isSign);
		}
	}
	
	@Action
	public void isreturnAjaxSubmit() {
		Boolean isReturn = selectedRowData.getIsreturn();
		String sql = "";
		try {
			if (selectedRowData.getIssign())
				throw new CommonRuntimeException(
						"Current is sign,plese unsign first!");
			if (isReturn) {
				sql = "UPDATE del_deliverydtl SET isreturn = TRUE  WHERE id ="
						+ this.pkVal + ";";
				
			} else {
				sql = "UPDATE del_deliverydtl SET isreturn = FALSE  WHERE id ="
						+ this.pkVal + ";";
			}
			serviceContext.deliverydtlMgrService.delDeliverydtlDao
					.executeSQL(sql);
			refresh();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			this.disableAllButton(isReturn);
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIsreturn(!isReturn);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			this.disableAllButton(!isReturn);
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsreturn(!isReturn);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			this.disableAllButton(!isReturn);
		}
	}
	
	@Bind
	@SaveState
	@Accessible
	public String signEr;
	
	@Bind
	@SaveState
	@Accessible
	public String signRemarks;
	
	@Bind
	@SaveState
	@Accessible
	public String returnRemarks;
	
	
	

	@Bind
	public UIWindow signWindow;
	
	
	@Bind
	public UIWindow returnWindow;
	
	@SaveState
	@Accessible
	public String[] ids;

	
	
	@Action
	public void doSign(){
		try {
			signWindow.close();
			serviceContext.deliverydtlMgrService.updateSignBatch(ids,signEr,signRemarks);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void sign() {
		ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}else{
		signEr = "";
		signRemarks="";
		signWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "signEr");
		this.update.markUpdate(UpdateLevel.Data, "signRemarks");
		}
	}
	
	@Action
	public void unsign() {
		 ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.deliverydtlMgrService.updateUnSignBatch(ids,signEr,signRemarks);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

	}
	
	@Action
	public void back() {
		ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}else{
			returnRemarks="";
			returnWindow.show();
			this.update.markUpdate(UpdateLevel.Data, "returnRemarks");
		}
	}
	
	@Action
	public void doReturn(){
		try {
			returnWindow.close();
			serviceContext.deliverydtlMgrService.updateBackBatch(ids,returnRemarks);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	
	@Action
	public void unback() {
		ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.deliverydtlMgrService.updateUnBackBatch(ids,returnRemarks);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

	}
	
	
}