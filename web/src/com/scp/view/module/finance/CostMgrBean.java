package com.scp.view.module.finance;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.costmgrBean", scope = ManagedBeanScope.REQUEST)
public class CostMgrBean extends GridFormView {
	
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.qryMap.put("feeitemdec","海运费");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
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
	public UIWindow showamountWindow;

	@Action
	public void editamount() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择一条记录");
		} else {
			showamountWindow.show();
		}
	}

	@Action
	public void changamount() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录!");
		} else {
			String amount = AppUtils.getReqParam("amount");
			String xrate = AppUtils.getReqParam("xrate");
			String currency = AppUtils.getReqParam("currency");
//			BigDecimal amount = new BigDecimal(sAmount);
//			BigDecimal xrate = new BigDecimal(sXrate);
			try {
				serviceContext.costMgrService.editAmount(ids, amount, xrate, currency, AppUtils
						.getUserSession().getUsercode());
				showamountWindow.close();
				refresh();
				MessageUtils.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	}
	
	
}