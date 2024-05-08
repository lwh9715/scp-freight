package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.service.wms.WmsInMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.wmsinitBean", scope = ManagedBeanScope.REQUEST)
public class WmsInitBean extends GridView {
	
	
	@ManagedProperty("#{wmsInMgrService}")
	public WmsInMgrService wmsInMgrService;
	

	@Action
	public void add() {
		String winId = "_edit_wms_init";
		String url = "./wmsinitedit.xhtml";
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_wms_in";
		String url = "./wmsinitedit.xhtml?id="+this.getGridSelectId();
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
			serviceContext.wmsInMgrService().updateCheck(ids);
			MessageUtils.alert("审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	@Bind
	public UICombo qryTypeCom;
	
	@Bind
	@SaveState
	@Accessible
	public String importCustomText;

	@Bind
	public UIWindow importCustomWindow;

	@Action
	public void importCustom() {
		importCustomText = "";
		importCustomWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importCustomText");
	}

	@Action
	public void importCustomBatch() {
		String warehouseid= qryTypeCom.getValue().toString();
		if (StrUtils.isNull(importCustomText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		}else if(warehouseid.equals("")){
			Browser.execClientScript("window.alert('" + "Please choosen warehouse " + "');");
			return;
		} else {
			try {
				Long args2 = new Long(Integer.parseInt(warehouseid));
				String callFunction = "f_imp_wmsinit_customer";
				String args1 = AppUtils.getUserSession().getUsercode();
				String arg="'"+args1+"',"+args2;
				this.serviceContext.commonDBService.addBatchFromExcelText(importCustomText , callFunction ,arg);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	

	@Bind
	@SaveState
	@Accessible
	public String importGoodsText;

	@Bind
	public UIWindow importGoodsWindow;

	@Action
	public void importGoods() {
		importGoodsText = "";
		importGoodsWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importGoodsText");
	}

	@Action
	public void importGoodsBatch() {
		if (StrUtils.isNull(importGoodsText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_wmsinit_goods";
				String args1 = AppUtils.getUserSession().getUsercode();
				String arg="'"+args1+"'";
				this.serviceContext.commonDBService.addBatchFromExcelText(importGoodsText , callFunction , arg);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
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
