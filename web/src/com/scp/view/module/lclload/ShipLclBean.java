package com.scp.view.module.lclload;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.del.DelLoad;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.lclload.shiplclBean", scope = ManagedBeanScope.REQUEST)
public class ShipLclBean extends GridView {
	
	@SaveState
	@Bind
	public String qryCntids;
	@Bind
	@SaveState
	private String linesQry;
	
	@Bind
	@SaveState
	public String linesQryChoose;
	
	@Action
	public void add() {
		String winId = "_shiplcldtl";
		String url = "./shiplcldtl.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			String querySql = "SELECT string_agg(namec,'@') AS lines FROM dat_line where lintype = 'T' AND isdelete = FALSE;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			linesQry = StrUtils.getMapVal(map, "lines");
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(qryCntids)){
			qry += "\nAND EXISTS (SELECT 1 FROM wms_in zz, wms_indtl z, del_loadtl x , wms_outdtlref y WHERE zz.cntid ILIKE '%"+qryCntids+"%' AND x.outdtlid = y.outdtlid AND y.indtlid = z.id AND x.loadid = p.id AND zz.id = z.inid)";
		}
		if(!StrUtils.isNull(linesQryChoose)){
			qry +="\nAND lines = '"+linesQryChoose+"'";	
		}
		
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_shiplcldtl";
		DelLoad delLoad = serviceContext.delLoadMgrService.delLoadDao.findById(this.getGridSelectId());
		String url;
		if("H".equals(delLoad.getLoadtype())){
			url = "./trainlcldtl.xhtml?id="+this.getGridSelectId();
		}else{
			url = "./shiplcldtl.xhtml?id="+this.getGridSelectId();
		}
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void submitBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选行");
			return;
		}
		try {
			serviceContext.delLoadMgrService.updateSubmit(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelSubmitBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选行");
			return;
		}
		
		try {
			serviceContext.delLoadMgrService.updateCanceSubmit(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
