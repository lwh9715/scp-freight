package com.scp.view.module.oa.paymgr;

import java.util.Calendar;
import java.util.Map;

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

import com.scp.model.oa.OaSalaryBaseinfo;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.paymgr.baseinfoBean", scope = ManagedBeanScope.REQUEST)
public class baseInfoBean extends GridFormView {
	
	@Bind
	public UIIFrame leaveframe;

	@Bind
	public UIWindow detailsWindow;
	
	@Bind
	@SaveState
	public UIWindow outDataWindow;
	
	@Bind
	@SaveState
	public UIWindow outDataWindow2;
	
	@Bind
	@SaveState
	public String year;
	
	@Bind
	@SaveState
	public String month;
	
	@Bind
	@SaveState
	public String yearno;
	
	@Bind
	@SaveState
	public String monthno;


	@SaveState
	@Accessible
	public OaSalaryBaseinfo selectedRowData = new OaSalaryBaseinfo();

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.baseInfoService().baseInfoDao
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
		leaveframe.load("baseinfodetail.xhtml?id=" + selectid);
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_in_baseinfo";

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
		for (String id : ids) {
			String sql = "UPDATE oa_salary_baseinfo SET isdelete = TRUE WHERE id = "
					+ id;
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
		}
		MessageUtils.alert("OK");
	}

	@Action
	public void importDataOut() {
		outDataWindow2.show();
		Calendar cal = Calendar.getInstance();
		monthno = String.valueOf(cal.get(Calendar.MONTH) + 1);//月
		yearno = String.valueOf(cal.get(Calendar.YEAR));      //年
         this.update.markUpdate(UpdateLevel.Data, "monthno");
         this.update.markUpdate(UpdateLevel.Data, "yearno");
	}

	private String getArgs(String id) {
		String sql = "SELECT yearno FROM oa_salary_baseinfo WHERE isdelete = FALSE AND id = "
				+ id;
		Map map = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		int year = (Integer) map.get("yearno");
		String arg = "&year=" + year;
		return arg;
	}
	
	@Action
	public void downloadEx(){
		outDataWindow.show();
		Calendar cal = Calendar.getInstance();
		month = String.valueOf(cal.get(Calendar.MONTH) + 1);//月
		year = String.valueOf(cal.get(Calendar.YEAR));      //年
         this.update.markUpdate(UpdateLevel.Data, "month");
         this.update.markUpdate(UpdateLevel.Data, "year");
	}
	
	@Action
	public void queryOut(){
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/salary_ex.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
		outDataWindow.close();
	}
	
	private String getArgs() {
		String arg = "&year=" + year + "&month=" + month;
		return arg;
	}
	
	@Action
	public void queryOut2(){
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/salary.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs2());
		outDataWindow2.close();
	}
	
	private String getArgs2() {
		String arg = "&year=" + yearno + "&month=" + monthno;
		return arg;
	}
	
}
