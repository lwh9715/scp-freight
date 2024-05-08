package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipjoin;
import com.scp.model.ship.BusShipjoinlink;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipjoinlinkchooseBean", scope = ManagedBeanScope.REQUEST)
public class ShipJoinLinkChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public BusShipjoinlink  busShipLink =new BusShipjoinlink();
	
	@SaveState
	@Accessible
	public BusShipjoin  busShipjoin =new BusShipjoin();
	
	@SaveState
	@Accessible
	public Long shipjoinid;
	

	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("shipjoinid").trim();
			shipjoinid=Long.valueOf(code);
			this.busShipjoin = serviceContext.busShipjoinMgrService.busShipjoinDao.findById(this.shipjoinid);
			this.qryMap.put("vessel", this.busShipjoin.getVessel());
			this.qryMap.put("voyage", this.busShipjoin.getVoyage());
			this.qryMap.put("pod", this.busShipjoin.getPod());
			this.qryMap.put("pol", this.busShipjoin.getPol());
			this.qryMap.put("carrierdesc",getCarrierdesc(this.shipjoinid));
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");		
				
		}
	}

	
	@Action
	public void importShipjoin(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		 try {
			serviceContext.busShipjoinlinkMgrService.chooseShip(ids,
					this.shipjoinid);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
			
		}
	}
	 
	 
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND  not exists (" ;
		qry +="SELECT 1 from bus_shipjoinlink g ,bus_shipjoin bs where bs.id = g.shipjoin AND g.shipid = t.shipid AND bs.isdelete =FALSE)";
		m.put("qry", qry);
		return m;
	}
	
	public String getCarrierdesc(Long id){
		String sql ="SELECT COALESCE(c.abbr,'')||'/'||c.code AS carrierdesc FROM sys_corporation c ,bus_shipjoin j WHERE j.carrierid = c.id AND j.id="+id;
		
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			return ((String)m.get("carrierdesc")).trim();
		} catch (Exception e) {
			return "";
		}
		
	}
	
	
}
