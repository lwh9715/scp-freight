package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.ship.BusShipjoinlink;
import com.scp.model.ship.BusShipping;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shipbookingchooseBean", scope = ManagedBeanScope.REQUEST)
public class ShipBookingChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public BusShipjoinlink  busShipLink =new BusShipjoinlink();
	
	@SaveState
	@Accessible
	public BusShipping  busShipping =new BusShipping();
	
	@SaveState
	@Accessible
	public Long shipid;
	
	@SaveState
	@Accessible
	public Long containid;
	
	

	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			//String containerid = AppUtil.getReqParam("containerid").trim();
			String shipid = AppUtils.getReqParam("shipid").trim();
			this.shipid=Long.valueOf(shipid);
			String contaerids = AppUtils.getReqParam("containerid").trim();
			this.containid=Long.valueOf(contaerids);
			//this.containerid=Long.valueOf(containerid);
			this.busShipping = serviceContext.busShippingMgrService.busShippingDao.findById(this.shipid);
			this.qryMap.put("vessel", this.busShipping.getVessel());
			this.qryMap.put("voyage", this.busShipping.getVoyage());
			this.qryMap.put("carrierdesc",getCarrierdesc(this.busShipping.getCarrierid()));
			this.qryMap.put("scheduleyear$",this.busShipping.getScheduleYear());
			this.qryMap.put("schedulemonth$", this.busShipping.getScheduleMonth());
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");		
				
		}
	}

	
	@Action
	public void importBook(){
		String[] ids = this.grid.getSelectedIds();
		
		//没有勾选
		if(this.containid==0L){
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选一行");
				return;
			}
		}else{
			if(ids == null || ids.length == 0|| ids.length >1) {
				MessageUtils.alert("请先勾选一行");
				return;
			}
		}
		
		 try {
			//serviceContext.busBookingMgrService.chooseBook(Long.valueOf(ids[0]),this.containerid, AppUtil.getUserSession().getUsercode(), AppUtil.getUserSession().getUserid());
			serviceContext.busBookingMgrService.chooseBook(ids, this.shipid,
					this.containid, AppUtils.getUserSession().getUsercode(),
					AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	    
	}
	 
	 
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
//		String qry = StrTools.getMapVal(m, "qry");
//		qry += "\nAND userid_assign ="+AppUtil.getUserSession().getUserid();
//		m.put("qry", qry);
		String qry2="\n userid_assign ="+AppUtils.getUserSession().getUserid();
		String qry3="\n userid_assign <> "+AppUtils.getUserSession().getUserid();
		m.put("qry2", qry2);
		m.put("qry3", qry3);
		
		return m;
	}
	
	public String getCarrierdesc(Long id){
		String sql ="SELECT c.code AS carrierdesc FROM sys_corporation c  WHERE  c.id="+id;
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			return ((String)m.get("carrierdesc")).trim();
		} catch (NoRowException e) {
			return "";
		} catch (MoreThanOneRowException e) {
			return "";
		}
		
	}
	
	
}
