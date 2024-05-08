package com.scp.view.module.customer;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.sys.SysCorpmemory;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.memorialdayBean", scope = ManagedBeanScope.REQUEST)
public class MemorialDayBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorpmemory selectedRowData = new SysCorpmemory();
	
	@SaveState
	@Accessible
	public String customerid;

	@Bind
	public UIButton qryRefresh;
	@Bind
	public UIButton add;
	@Bind
	public UIButton delBatch;

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		if (editWindow != null) {
			editWindow.show();
			contactedit.load("./memorialdayedit.xhtml?id=" + this.pkVal);
		}


	}

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.customerid =AppUtils.getReqParam("customerid").trim();
			if(!StrUtils.isNull(this.customerid)) {
				qryMap.put("customerid$", Long.parseLong(this.customerid));
			}
		}

		String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
				qryRefresh.setDisabled(true);
				add.setDisabled(true);
				delBatch.setDisabled(true);
			}
		} catch (Exception e) {
		}
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
			contactedit.load("./memorialdayedit.xhtml?customerid=" + this.customerid);
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
			serviceContext.customerMemorialdayMgrService.delBatch(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		if(StrUtils.isNull(this.customerid)) {
			Map m = super.getQryClauseWhere(queryMap);
			//过滤 权限客户
			m.put("filter", AppUtils.custCtrlClauseWhere());
			return m;
		}
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND customerid = " + this.customerid;
		m.put("qry", qry);
		m.put("filter", AppUtils.custCtrlClauseWhere());
		return m;
	}
}
