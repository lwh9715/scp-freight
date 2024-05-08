package com.scp.view.module.oa.staffmgr;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.UserEval;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.oa.staffmgr.userlogevBean", scope = ManagedBeanScope.REQUEST)
public class UserLogEvBean extends MastDtlView {

	@Bind
	@SaveState
	@Accessible
	public UserEval selectedRowData = new UserEval();

	@Bind
	public UIWindow showUserLogWindow;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}

	public void init() {
		selectedRowData = new UserEval();
		String id = AppUtils.getReqParam("id").trim();
		this.pkVal = Long.parseLong(id);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		addMaster();
	}

	@Override
	public void addMaster() {
		super.addMaster();
		selectedRowData = new UserEval();
		selectedRowData.setLogdate(new Date());
		selectedRowData.setUserinfoid(this.pkVal);
	}

	@Override
	public void grid_ondblclick() {
		this.mPkVal = Long.parseLong(this.grid.getSelectedIds()[0]);
		refreshMasterForm();
		showUserLogWindow.show();
	}

	@Action
	public void dels() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}

		try {
			for (String id : ids) {
				serviceContext.userEvService().removeDate(Long
						.valueOf(id));
			}
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qry += "\nAND userinfoid	= " + this.pkVal;
		map.put("qry", qry);
		return map;
	}

	@Action
	public void clear() {
		this.clearQryKey();
	}

	@Action
	public void searchfee() {
		this.qryRefresh();
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.userEvService().saveData(this.selectedRowData);
		MessageUtils.alert("OK");
		this.refresh();
		addMaster();
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.userEvService().userLogEvDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "remarks");
		update.markUpdate(true, UpdateLevel.Data, "detail");
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {

	}

	@Action
	public void delMaster() {
		serviceContext.userEvService().userLogEvDao
				.remove(this.selectedRowData);
		MessageUtils.alert("ok");
		this.refresh();
	}

	@Action
	public void add() {
		showUserLogWindow.show();
		addMaster();
	}

	@Action
	public void refresh2() {
		this.refresh();
	}

}
