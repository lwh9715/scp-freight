package com.scp.view.module.oa.paymgr;

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

@ManagedBean(name = "pages.module.oa.paymgr.withholdmgrBean", scope = ManagedBeanScope.REQUEST)
public class WithholdMgrBean extends MastDtlView {

	@Bind
	public UIIFrame leaveframe;

	@Bind
	public UIWindow detailsWindow;

	@Override
	public void grid_ondblclick() {
		Long id = this.getGridSelectId();
		detailsWindow.show();
		leaveframe.load("withholdmgrdetail.xhtml?id=" + id);
	}

	@Action
	public void add() {
		this.mPkVal = -1l;
		detailsWindow.show();
		leaveframe.load("withholdmgrdetail.xhtml");
	}

	@Action
	public void dels() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one at first!");
			return;
		}
		// String id = StrUtils.array2List(ids);
		this.serviceContext.oaFeeService().removeModle(ids);
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
				String callFunction = "f_oa_withhold_table";

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
//		qry += "\nAND changetype IN ('C','D')";
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void importDataOut(){
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/fee.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl);
	}

}
