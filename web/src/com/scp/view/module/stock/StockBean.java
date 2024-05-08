package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.wms.WmsIn;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.stockBean", scope = ManagedBeanScope.REQUEST)
public class StockBean extends GridView {
	
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}

	@Bind
	@SaveState
	public Boolean isIncludeZero = false;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//qry += "\nAND warehouseid = " + AppUtils.getUserSession().getWarehouseid();
		
		//qry += "\nAND  warehouseid IN ( SELECT linkid FROM sys_user_assign WHERE userid = " + AppUtils.getUserSession().getUserid() + " AND linktype='W' )";
		m.put("qry", qry);
		if (isIncludeZero) {
			m.put("flag", 1);
			m.put("tbl", "_wms_stock_history");
		} else {
			m.put("flag", 0);
			m.put("tbl", "_wms_stock");
		}
		return m;
	}
	

	@Bind
	public UIWindow showsplitStock;
	
	@Action
	public void splitStock(){
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length != 1) { 
		MessageUtils.alert("请选择一条记录"); 
		}else{
			showsplitStock.show();
		}
	}
	
	@Bind
	private Long splitnum;
	
	@Action
	public void splitAction() { 
		try {
			String[] ids = this.grid.getSelectedIds();
			if(ids == null || ids.length != 1){
				MessageUtils.alert("Please choose one!");
				return;
			}
			WmsIn wmsIn = serviceContext.wmsIndtlMgrService().wmsInDao.findById(Long.parseLong(ids[0]));
			Long warehouseid = wmsIn.getWarehouseid();
			Long customerid = wmsIn.getCustomerid();
			String  sql ="SELECT f_wms_out_split('warehouseid="+warehouseid+";customerid="+customerid+";splitnum="+splitnum+"');";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			showsplitStock.close();
			MessageUtils.alert("OK"); 
			super.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

}
