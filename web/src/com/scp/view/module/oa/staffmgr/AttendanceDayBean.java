package com.scp.view.module.oa.staffmgr;

import java.util.Map;

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

import com.scp.model.oa.OaTimeSheet;
import com.scp.service.oa.SalaryBillMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.oa.staffmgr.attendance_dayBean", scope = ManagedBeanScope.REQUEST)
public class AttendanceDayBean extends GridFormView{
	
	@SaveState
	@Accessible
	public OaTimeSheet selectedRowData = new OaTimeSheet();
	
	@ManagedProperty("#{salaryBillMgrService}")
	public SalaryBillMgrService salaryBillMgrService;
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.oaTimeSheetService().oaTimeSheetDao
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
	
	
	//批量导入
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_in_attendance_day";

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
	
	
	//删除
	@Action
	public void delattendance() {
		String[] ids = this.grid.getSelectedIds();
		if (ids.length < 0 || ids == null) {
			MessageUtils.alert("至少选择一行删除!");
			return;
		}
		for (String id : ids) {
			String sql = "UPDATE oa_timesheet_day SET isdelete = TRUE WHERE id = "
					+ id;
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
		}
		MessageUtils.alert("OK");
	}
	
	
	//导出
	@Action
	public void importDataOut() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("至少选择一行导出!");
			return;
		}
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/attendance.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs(ids[0]));
	}
	
	private String getArgs(String id) {
		String sql = "SELECT logdate FROM oa_timesheet_day WHERE isdelete = FALSE AND id = "+id;
		 Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		 int logdate = (Integer)map.get("logdate");
		String arg = "&logdate=" + logdate;
		return arg;
	}
	
	
	@Action
	public void dayReport() {
		Long userid =  AppUtils.getUserSession().getUserid();
		String winId = "dayreport";
		String url =  "/scp/pages/module/oa/timesheet/dayreport.jsp?userid=" + userid;
		AppUtils.openWindow(true,winId, url,false);
	}
	
}
