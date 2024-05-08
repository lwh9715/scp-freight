package com.scp.view.module.oa.paymgr;

import java.util.Calendar;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.oa.OaSalaryTable;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.paymgr.salaryallBean", scope = ManagedBeanScope.REQUEST)
public class SalaryAllBean extends GridFormView {

	@Bind
	public UIIFrame leaveframe;

	@Bind
	public UIWindow detailsWindow;
	
	@Bind
	@SaveState
	public UIWindow outDataWindow;
	
	@Bind
	@SaveState
	public String year;
	
	@Bind
	@SaveState
	public String month;

	@SaveState
	@Accessible
	public OaSalaryTable selectedRowData = new OaSalaryTable();

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.salaryTableService().salaryTableDao
				.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

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

	@Override
	public void grid_ondblclick() {
		Long selectid = this.getGridSelectId();
		detailsWindow.show();
		leaveframe.load("salaryalldetail.xhtml?id=" + selectid);
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_oa_create_salary_table";

				// String args = this.jobid + "";
				String args = "'" + AppUtils.getUserSession().getUsercode()
						+ "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
				importDataWindow.close();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Action
	public void delattendance() {
		String[] ids = this.grid.getSelectedIds();
		if (ids.length < 0 || ids == null) {
			MessageUtils.alert("至少选择一行删除!");
			return;
		}
		this.serviceContext.salaryTableService().removeModle(ids);
		MessageUtils.alert("OK");
		this.refresh();
	}
	
	@Action
	public void queryOut(){
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/salary_all.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
		outDataWindow.close();
	}

	@Action
	public void importDataOut() {
		outDataWindow.show();
		Calendar cal = Calendar.getInstance();
		 month = String.valueOf(cal.get(Calendar.MONTH) + 1);//月
         year = String.valueOf(cal.get(Calendar.YEAR));      //年
         this.update.markUpdate(UpdateLevel.Data, "month");
         this.update.markUpdate(UpdateLevel.Data, "year");
	}

	private String getArgs() {
		String arg = "&year=" + year + "&month=" + month;
		return arg;
	}

}
