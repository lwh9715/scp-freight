package com.scp.view.module.finance;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.busbillBean", scope = ManagedBeanScope.REQUEST)
public class BusBillBean extends GridView {
	
	@Bind
	@SaveState
	private String startDateA;
	
	@Bind
	@SaveState
	private String endDateA;
	
	@Bind
	@SaveState
	private String startDateD;
	
	@Bind
	@SaveState
	private String endDateD;
	
	@Bind
	@SaveState
	private String startDateB;
	
	@Bind
	@SaveState
	private String endDateB;
	
	
	@Bind
	@SaveState
	private String startDateI;
	
	@Bind
	@SaveState
	private String endDateI;
	
	@Bind
	public UIWindow searchWindow;

	@Override
	public void grid_ondblclick() {
		Long billid = Long.parseLong(this.grid.getSelectedIds()[0]);
		AppUtils.openWindow("_edit_bus_bill", "../ship/busbill.xhtml?type=customer&billid=" + billid);
	}
	
	@Action
	public void add() {
		AppUtils.openWindow("_edit_bus_bill", "../ship/busbill.xhtml?type=customer");
	}
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <= 0){
			this.alert("Please choose one row!");
			return;
		}
		
		try {
			this.serviceContext.billMgrService.removeDate(ids,AppUtils.getUserSession().getUsercode());
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
		// ATD区间查询
		qry += "\nAND ((t.atd BETWEEN '" + (StrUtils.isNull(startDateA) ? "0001-01-01" : startDateA)
		+ "' AND '" + (StrUtils.isNull(endDateA) ? "9999-12-31" : endDateA) + "')"
		+ "\n OR t.atd IS NULL)";
		// ETD区间查询
		qry += "\nAND ((t.etd BETWEEN '" + (StrUtils.isNull(startDateD) ? "0001-01-01" : startDateD)
		+ "' AND '" + (StrUtils.isNull(endDateD) ? "9999-12-31" : endDateD) + "')"
		+ "\n OR t.etd IS NULL)";
		// 对账单日期区间查询
		qry += "\nAND t.billdate BETWEEN '" + (StrUtils.isNull(startDateB) ? "0001-01-01" : startDateB)
				+ "' AND '" + (StrUtils.isNull(endDateB) ? "9999-12-31" : endDateB) + "'";
//		// 录入时间区间查询
		qry += "\nAND t.inputtime BETWEEN '" + (StrUtils.isNull(startDateI) ? "0001-01-01" : startDateI) 
		+ "' AND '" + (StrUtils.isNull(endDateI) ? "9999-12-31" : endDateI) + "'";
		map.put("qry", qry);

		return map;
	}
	
	@Override
	public void clearQryKey() {
		this.startDateA = "";
		this.endDateA = "";
		this.startDateD = "";
		this.endDateD = "";
		this.startDateB = "";
		this.endDateB = "";
		this.startDateI = "";
		this.endDateI = "";
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		this.update.markUpdate(UpdateLevel.Data, "startDateD");
		this.update.markUpdate(UpdateLevel.Data, "endDateD");
		this.update.markUpdate(UpdateLevel.Data, "startDateB");
		this.update.markUpdate(UpdateLevel.Data, "endDateB");
		this.update.markUpdate(UpdateLevel.Data, "startDateI");
		this.update.markUpdate(UpdateLevel.Data, "endDateI");
		super.clearQryKey();
	}
	
	@Action
	public void search(){
		this.searchWindow.show();
	}
	
	@Action
	public void clear(){
		this.clearQryKey();
	}
	
	@Action
	public void searchfee(){
		this.qryRefresh();
	}

}
