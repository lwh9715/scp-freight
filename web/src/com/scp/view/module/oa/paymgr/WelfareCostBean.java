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

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.paymgr.welfarecostBean", scope = ManagedBeanScope.REQUEST)
public class WelfareCostBean extends MastDtlView {

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

	

	@Override
	public void grid_ondblclick() {
		Long id = this.getGridSelectId();
		detailsWindow.show();
		leaveframe.load("welfarecostdetail.xhtml?id=" + id);
	}

	@Action
	public void add() {
		this.mPkVal = -1l;
		detailsWindow.show();
		leaveframe.load("welfarecostdetail.xhtml");
	}

	@Action
	public void dels() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one at first!");
			return;
		}
		// String id = StrUtils.array2List(ids);
		this.serviceContext.salaryWelfareService().removeModle(ids);
		MessageUtils.alert("OK!");
		this.grid.reload();
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
				String callFunction = "f_oa_welfare_cost_table";

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
	
	@Action
	public void queryOut(){
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/well_cost.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
		outDataWindow.close();
	}


	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		this.mPkVal = this.getGridSelectId();
	}

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		// qry += "\nAND changetype IN ('C','D')";
		map.put("qry", qry);
		return map;
	}

}
