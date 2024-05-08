package com.scp.view.module.salesmgr;


import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysCorpvisit;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.salesmgr.accesscustomerBean", scope = ManagedBeanScope.REQUEST)
public class AccessCustomerBean extends GridView {
	
	@SaveState
	@Accessible
	public SysCorpvisit selectedRowData = new SysCorpvisit();
	
	@SaveState
	@Accessible
	public String customerid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.customerid =AppUtils.getReqParam("customerid").trim();
			if(!StrUtils.isNull(this.customerid)) {
				qryMap.put("customerid$", Long.parseLong(this.customerid));
			}
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_accesscustomer";
		String url = "./accesscustomeredit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void add() {
		String winId = "_edit_accesscustomer";
		String url = "./accesscustomeredit.xhtml?customerid=" + this.customerid;
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if(ids ==null || ids.length <0){
			alert("请选择一条数据!");
			return;
		}
		try {
			serviceContext.corpvisitService.removeDates(ids);
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	

//	@Bind
//	public UIWindow showamountWindow;
//
//	@Action
//	public void editamount() {
//		String[] ids = this.grid.getSelectedIds();
//		if (ids == null || ids.length == 0) {
//			MsgUtil.alert("请选择一条记录");
//		} else {
//			showamountWindow.show();
//		}
//	}
//
//	@Action
//	public void changamount() {
//		String[] ids = this.grid.getSelectedIds();
//		if (ids == null || ids.length == 0) {
//			MsgUtil.alert("请选择记录!");
//		} else {
//			String amount = AppUtil.getReqParam("amount");
//			String xrate = AppUtil.getReqParam("xrate");
//			String currency = AppUtil.getReqParam("currency");
//			// BigDecimal amount = new BigDecimal(sAmount);
//			// BigDecimal xrate = new BigDecimal(sXrate);
//			try {
//				serviceContext.costMgrService.editAmount(ids, amount, xrate,
//						currency, AppUtil.getUserSession().getUsercode());
//				showamountWindow.close();
//				refresh();
//				MsgUtil.alert("OK");
//			} catch (Exception e) {
//				MsgUtil.showException(e);
//				return;
//			}
//		}
//	}

}
