package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.BusShipjoinlink;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipjobschooseBean", scope = ManagedBeanScope.REQUEST)
public class ShipJobsChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public BusShipjoinlink  busShipLink =new BusShipjoinlink();
	
	@SaveState
	@Accessible
	public Long parentid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("jobid").trim();
			parentid=Long.valueOf(code);
		}
	}

	
	
	/*
	 * 加入的子单按照顺序重新编号
	 * 
	 */
	@Action
	public void choosejobs(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要加入的行");
			return;
		}
		 try {
			serviceContext.jobsMgrService.choosejobs(ids,this.parentid,AppUtils.getUserSession().getUsercode(),"t");
			 MessageUtils.alert("OK");
			 refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void choosejobsnos(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要加入的行");
			return;
		}
		 try {
			serviceContext.jobsMgrService.choosejobs(ids,this.parentid,AppUtils.getUserSession().getUsercode(),"f");
			 MessageUtils.alert("OK");
			 refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	 
	
	//
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//单独的子节点,为空,不为自己,
		qry += "\nAND t.parentid IS NULL AND t.id <> "+this.parentid;
		//没有子节点;
		qry +="\nAND NOT EXISTS (SELECT 1 from _fina_jobs j where  j.parentid =t.id ) ";
		m.put("qry", qry);
		return m;
	}
	
	
}
