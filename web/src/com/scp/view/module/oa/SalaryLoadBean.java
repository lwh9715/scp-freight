package com.scp.view.module.oa;
 
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.oa.SalaryBill;
import com.scp.service.oa.SalaryBillMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;


@ManagedBean(name = "pages.module.oa.salaryloadBean", scope = ManagedBeanScope.REQUEST)
public class SalaryLoadBean extends GridFormView {


	@SaveState
	@Accessible
	public SalaryBill selectedRowData = new SalaryBill();
	

	@ManagedProperty("#{salaryBillMgrService}")
	public SalaryBillMgrService salaryBillMgrService;

	@Override
	protected void doServiceFindData() {
		selectedRowData = salaryBillMgrService.salaryBillDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		
	}


	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;
	
	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_imp_salary";

				// String args = this.jobid + "";
				String args =  "'"+AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

}
