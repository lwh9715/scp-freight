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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.crm.clientcontacterBean", scope = ManagedBeanScope.REQUEST)
public class ClientcontacterBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorpcontacts selectedRowData = new SysCorpcontacts();
	
//	@SaveState
//	@Accessible
//	public String customerid;
//	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
//			this.customerid =AppUtils.getReqParam("customerid").trim();
//			if(!StrUtils.isNull(this.customerid)) {
//				qryMap.put("customerid$", Long.parseLong(this.customerid));
//			}
		}
		selectedRowData = new SysCorpcontacts();
		this.update.markUpdate(UpdateLevel.Data, "gridPanel");
	}
	
	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		if (editWindow != null) {
			editWindow.show();
			contactedit.load("../customer/contactedit.xhtml?id=" + this.pkVal);
		}

	}

	@Override
	public void save() {
		serviceContext.customerContactsMgrService.saveData(selectedRowData);
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
//			if(StrUtils.isNull(this.customerid)) {
//				contactedit.load("./contactedit.xhtml");
//			} else {
//				contactedit.load("./contactedit.xhtml?customerid=" + this.customerid);
//			}
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
			serviceContext.customerContactsMgrService.delBatch(ids);
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
//		qry += "\nAND customerid = " + this.customerid;
		m.put("qry", qry);
		//过滤 权限客户
		m.put("filter", AppUtils.custCtrlClauseWhere());
		
		String sql = "\nAND ( a.salesid = "+AppUtils.getUserSession().getUserid()
		+ "\n	OR (a.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
		+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign x, sys_corporation y WHERE x.linkid = y.id AND y.id = a.id AND x.linktype = 'C' AND x.userid ="
		+ AppUtils.getUserSession().getUserid() + ")"
		+ "\n)";
		m.put("filter2", sql);
		
		return m;
	}
	
	@Action
	public void addHblconsignee(){
//		String sql = "SELECT f_fix_sys_corpcontacts('id="+this.customerid+";type=hblconsignee'"+")AS m";
//		try {
//			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//			if(map.get("m").toString().equals("0")){
//				alert("已存在");
//				return;
//			}
//			alert("OK");
//			this.qryRefresh();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
	}
	
	@Action
	public void addHblagent(){
//		String sql = "SELECT f_fix_sys_corpcontacts('id="+this.customerid+";type=hblagent'"+") AS m";
//		try {
//			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//			if(map.get("m").toString().equals("0")){
//				alert("已存在");
//				return;
//			}
//			alert("OK");
//			this.qryRefresh();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
	}
}
