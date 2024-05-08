package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;

import com.scp.service.wms.WmsOutMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.warehouseoutcheckBean", scope = ManagedBeanScope.REQUEST)
public class WarehouseOutCheckBean extends GridView {
	
	
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	
	@ManagedProperty("#{wmsOutMgrService}")
	public WmsOutMgrService wmsOutMgrService;
	

	@Action
	public void add() {
		String winId = "_edit_wms_out_check";
		String url = "./wmsoutcheckedit.xhtml";
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_wms_out_check";
		String url = "./wmsoutcheckedit.xhtml?id="+this.getGridSelectId();
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	@Action
	public void checkBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			wmsOutMgrService.updateCheck(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	
	@Action
	public void cancelCheckBatch() {
		
		try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}
			wmsOutMgrService.updateCancelCheck(ids);
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
