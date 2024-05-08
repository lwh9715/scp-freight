package com.scp.view.module.oa.staffmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.OaUserTransfer;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.staffmgr.transferBean", scope = ManagedBeanScope.REQUEST)
public class TransferBean extends MastDtlView {

	@Bind
	@SaveState
	@Accessible
	public OaUserTransfer selectedRowData = new OaUserTransfer();
	
	@Bind
	public UIWindow showTransferWindow;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}

	public void init() {
		selectedRowData = new OaUserTransfer();
		String id = AppUtils.getReqParam("id").trim();
		this.pkVal = Long.parseLong(id);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		addMaster();
	}

	@Override
	public void addMaster() {
		super.addMaster();
		selectedRowData = new OaUserTransfer();
		selectedRowData.setUserinfoid(this.pkVal);
	}

	@Override
	public void grid_ondblclick() {
		this.mPkVal = Long.parseLong(this.grid.getSelectedIds()[0]);
		refreshMasterForm();
		showTransferWindow.show();
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
				serviceContext.oaUserTransferService().removeDate(Long
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
		serviceContext.oaUserTransferService().saveData(this.selectedRowData);
		MessageUtils.alert("OK");
		this.refresh();
		addMaster();
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.oaUserTransferService().oaUserTransferDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "transdate");
		update.markUpdate(true, UpdateLevel.Data, "transpalce");
		update.markUpdate(true, UpdateLevel.Data, "transduty");
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {

	}
	
	@Action
	public void delMaster(){
		serviceContext.oaUserTransferService().oaUserTransferDao.remove(this.selectedRowData);
		MessageUtils.alert("ok");
		this.refresh();
	}
	
	@Action
	public void add(){
		showTransferWindow.show();
		addMaster();
	}
	
	@Action
	public void refresh2(){
		this.refresh();
	}

}
