package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.bus.BusDocexpLink;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipchooseBean", scope = ManagedBeanScope.REQUEST)
public class ShipChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public BusDocexpLink  busDocexpLink =new BusDocexpLink();
	
	@SaveState
	@Accessible
	public Long docexpid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("sendid").trim();
			docexpid=Long.valueOf(code);
		}
	}

	
	@Action
	public void importShip(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
	   for(int i=0;i<ids.length;i++){
		   busDocexpLink=new BusDocexpLink();
		 Long  shipid=Long.valueOf(ids[i]);
		 busDocexpLink.setDocexpid(this.docexpid);
		 busDocexpLink.setLinkid(shipid);
		 serviceContext.busDocexpLinkMgrService.saveData(busDocexpLink);
	   }
	     MessageUtils.alert("OK");
	     refresh();
		
	}
	 
	@Override
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND  not exists (" ;
		qry +="SELECT 1 from bus_docexp_link g where  g.linkid = t.id)";
		m.put("qry", qry);
		return m;
	}
	
	
}
