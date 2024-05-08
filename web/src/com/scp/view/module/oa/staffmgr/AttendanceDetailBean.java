package com.scp.view.module.oa.staffmgr;

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

import com.scp.model.oa.OaTimeSheet;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.staffmgr.attendancedetailBean", scope = ManagedBeanScope.REQUEST)
public class AttendanceDetailBean extends MastDtlView {
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
	public OaTimeSheet selectedRowData = new OaTimeSheet();

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
		selectedRowData = new OaTimeSheet();
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
		this.selectedRowData = serviceContext.oaTimeSheetService().oaTimeSheetDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		// this.tabLayout.setActiveTab(0);
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.oaTimeSheetService().saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		refreshMasterForm();
		this.alert("ok");
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	@Bind
	public UIButton addJobsLeaf;


	@Override
	public void addMaster() {
		this.selectedRowData = new OaTimeSheet();
	}

	@Override
	public void delMaster() {
		try {
			this.serviceContext.oaTimeSheetService().removeDate(this.selectedRowData.getId());
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
