package com.scp.view.module.finance.report;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.report.fs_fee_profitBean", scope = ManagedBeanScope.REQUEST)
public class FsFeeProfitBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();

	@Action
	public void grid_ondblclick() {
		Browser.execClientScript("reportJsVar.fireEvent('click')");
	}
	
	@Action
	public void report() {
		String[] ids = this.grid.getSelectedIds();
		String id = "";
		if(ids != null && ids.length == 1){
			id = ids[0];
		}
		
		String arg = "&" + "userid=" + AppUtils.getUserSession().getUserid()+"&" + "customerid=" + id;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp"+"?raq=/static/profit/fs_fee_profit.raq";
		AppUtils.openWindow("", openUrl + arg);
	}
	
	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

}
