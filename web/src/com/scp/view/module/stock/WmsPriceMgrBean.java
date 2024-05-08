package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.wms.WmsPrice;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.stock.wmspricemgrBean", scope = ManagedBeanScope.REQUEST)
public class WmsPriceMgrBean extends GridFormView {

	@SaveState
	@Accessible
	public WmsPrice selectedRowData = new WmsPrice();
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.wmsPriceMgrService().wmsPriceDao.findById(this.pkVal);
	}

	
	

	@Override
	public void del() {
		super.del();
		Long id = this.getGridSelectId();
		if(id == -1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.wmsPriceMgrService().removeDate(id);
	}




	@Override
	protected void doServiceSave() {
		serviceContext.wmsPriceMgrService().saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_pricegroupmgr";
		String url = "./wmspricegroupmgr.xhtml?id="+this.getGridSelectId();
		int width = 1024;
		int height = 768;
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	
	@Override
	@Action
	public void add() {
		super.grid_ondblclick();
		String winId = "_pricegroupmgr";
		String url = "./wmspricegroupmgr.xhtml";
		int width = 1024;
		int height = 768;
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//qry += "\nAND warehouseid = " + AppUtils.getUserSession().getWarehouseid();
		
		//qry += "\nAND  warehouseid IN ( SELECT linkid FROM sys_user_assign WHERE userid = " + AppUtils.getUserSession().getUserid() + " AND linktype='W' )";
		m.put("qry", qry);
		return m;
	}
	
}
