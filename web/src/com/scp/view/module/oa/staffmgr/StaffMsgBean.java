package com.scp.view.module.oa.staffmgr;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.scp.model.oa.OaUserinfo;
import com.scp.service.oa.SalaryBillMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.oa.staffmgr.staffmsgBean", scope = ManagedBeanScope.REQUEST)
public class StaffMsgBean extends GridFormView {

	@SaveState
	@Accessible
	public OaUserinfo selectedRowData = new OaUserinfo();

	@ManagedProperty("#{salaryBillMgrService}")
	public SalaryBillMgrService salaryBillMgrService;

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.oaUserInfoService().oaUserinfoDao.findById(this.pkVal);

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
	
	@Bind
	public UIWindow ShowDataWindow;
	
	@Bind
	public Date startdate;
	
	@Bind
	public Date enddate;
	
	@Bind
	public String showtype;
	
	@Bind
	public String corpid;
	
	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	@Action
	public void showGroupData(){
		startdate = new Date();
		enddate = new Date();
		showtype = "E";
		this.update.markUpdate(UpdateLevel.Data, "startdate");
		this.update.markUpdate(UpdateLevel.Data, "enddate");
		this.update.markUpdate(UpdateLevel.Data, "showtype");
		ShowDataWindow.show();
	}
	
	@Override
	public void grid_ondblclick(){
		 Long selectid = this.getGridSelectId();
		String winId = "_edit_staffmsg";
		String url = "staffmsgedit.xhtml?id="+selectid;
		AppUtils.openWindow(winId, url);
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_in_staffmsg";

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
	public void dels(){
		 String[] ids = this.grid.getSelectedIds();
		 if(ids == null){
			 MessageUtils.alert("至少选择一行删除!");
			 return;
		 }
		 for(String id:ids){
			 String sql = "UPDATE oa_userinfo SET isdelete = TRUE WHERE id = "+ id;
			 serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			 this.refresh();
		 }
		 MessageUtils.alert("OK");
	}
	
	@Action
	public void importDataOut(){
			String rpturl = AppUtils.getContextPath();
			String openUrl = rpturl
					+ "/reportJsp/showPreview.jsp?raq=/static/staff_msg.raq";
			AppUtils.openWindow("_apAllCustomReport", openUrl);
	}
	
	@Action
	public void showDetail(){
		String rpturl = AppUtils.getContextPath();
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/staff_msg.raq";
		AppUtils.openWindow("_apAllCustomReport", openUrl);
	}
	
	@Action
	public void queryshow(){
		String rpturl = AppUtils.getContextPath();
		String typereport = "";
		if(showtype.equals("E")){
			typereport = "staff_group_area.raq";
		}else{
			typereport = "staff_group_state.raq";
		}
		String openUrl = rpturl
				+ "/reportJsp/showPreview.jsp?raq=/static/"+typereport;
		AppUtils.openWindow("_apAllCustomReport", openUrl + getArgs());
		
	}
	
	private String getArgs() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datas = f1.format(startdate);
		String datae = f1.format(enddate);
		String arg = "&corpid=" + corpid + "&startdate=" + datas
				+ "&enddate=" + datae;
		return arg;
	}

	@Action
	public void addUser(){
		String winId = "_add_staffmsg";
		String url = "staffaddedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
}
