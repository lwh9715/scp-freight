package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.wmscarryoverBean", scope = ManagedBeanScope.REQUEST)
public class WmsCarryOverBean extends GridView {
	
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	
	@Action
	public void add() {
		String winId = "_edit_wms_carryover";
		String url = "./wmscarryoveredit.xhtml";
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_wms_carryover";
		String url = "./wmscarryoveredit.xhtml?id="+this.getGridSelectId();
		ApplicationUtilBase.openWindow(winId, url);
	}
	

	@Action
	public void customChoose() {
//		String id = AppUtils.getReqParam("priceid");
//		if (StrUtils.isNull(id)) {
//			return;
//		}
//		Long priceid=new Long((Integer.parseInt(id)));
		String url = AppUtils.getContextPath() + "/pages/module/stock/warehousecarryoverchoose.xhtml";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
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
