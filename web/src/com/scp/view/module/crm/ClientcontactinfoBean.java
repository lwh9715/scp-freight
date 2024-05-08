package com.scp.view.module.crm;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.sys.SysCorpservice;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.crm.clientcontactinfoBean", scope = ManagedBeanScope.REQUEST)
public class ClientcontactinfoBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorpservice selectedRowData = new SysCorpservice();
	
//	@SaveState
//	@Accessible
//	public String customerid;

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		if (editWindow != null) {
			editWindow.show();
			contactedit.load("../customer/custserviceedit.xhtml?id=" + this.pkVal);
		}

	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
//			this.customerid =AppUtils.getReqParam("customerid").trim();
//			if(!StrUtils.isNull(this.customerid)) {
//				qryMap.put("customerid$", Long.parseLong(this.customerid));
//			}
		}
	}

	@Override
	public void save() {
		serviceContext.customerServiceMgrService.saveData(selectedRowData);
		this.alert("OK");
		this.refresh();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	

	@Bind
	public UIIFrame contactedit;

	@Override
	public void add() {

		if (editWindow != null) {
			editWindow.show();
			//contactedit.load("../customer/custserviceedit.xhtml?customerid=" + this.customerid);
		}

	}

	@Bind
	public UIWindow editWindow;

	public void closeEidtWindow() {
		editWindow.close();
	}

	@Action(id = "editWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}

	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.customerServiceMgrService.delBatch(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
//		if(StrUtils.isNull(this.customerid)) {
//			Map m = super.getQryClauseWhere(queryMap);
//			//过滤 权限客户
//			m.put("filter", AppUtils.custCtrlClauseWhere());
//			return m;
//		}
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//qry += "\nAND customerid = " + this.customerid;
		m.put("qry", qry);
		m.put("filter", AppUtils.custCtrlClauseWhere());
		return m;
	}
}
