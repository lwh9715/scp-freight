package com.scp.view.module.oa.paymgr;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.OaSalaryTable;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.paymgr.salaryalldetailBean", scope = ManagedBeanScope.REQUEST)
public class SalaryAllDetailBean extends MastDtlView {
	@Bind
	@Accessible
	public String fnos;

	@Bind
	public Long sale;

	@SaveState
	public Boolean isPostBack = false;

	@Bind
	@SaveState
	@Accessible
	public OaSalaryTable selectedRowData = new OaSalaryTable();

	@Override
	public void refreshForm() {

	}

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		} else {

		}
	}

	public void init() {
		selectedRowData = new OaSalaryTable();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else {
			addMaster();
		}
	}

	@Override
	@Action
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.salaryTableService().salaryTableDao.
				findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		// this.tabLayout.setActiveTab(0);
	}

	@Override
	public void doServiceSaveMaster() {
		Long userinfoid;
		try {
			 userinfoid = this.selectedRowData.getUserinfoid();
		} catch (Exception e) {
			MessageUtils.alert("姓名不能为空!");
			return;
		}
		String name = this.getNname(userinfoid);
		selectedRowData.setName(name);
		serviceContext.salaryTableService().saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		refreshMasterForm();
		this.alert("ok");
	}
	
	public String getNname(Long id) {
		String sql = "SELECT namec FROM oa_userinfo WHERE isdelete = FALSE AND id = "
				+ id;
		Map map = this.serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		String name = (String) map.get("namec");
		return name;
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	@Bind
	public UIButton addJobsLeaf;

	@Override
	public void addMaster() {
		this.selectedRowData = new OaSalaryTable();
	}

	@Override
	public void delMaster() {
		try {
			this.serviceContext.salaryTableService()
					.removeDate(this.selectedRowData.getId());
			this.addMaster();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {

	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind
	public String popQryKey;

}
