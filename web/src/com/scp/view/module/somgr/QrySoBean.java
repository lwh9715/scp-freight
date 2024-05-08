package com.scp.view.module.somgr;

import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.ship.BusShipBooking;
import com.scp.model.ship.BusShipping;
import com.scp.service.ship.BusShippingMgrService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.somgr.qrysoBean", scope = ManagedBeanScope.REQUEST)
public class QrySoBean extends GridView {

	
	@Bind
	@SaveState
	public String jobid;

	@Resource
	public BusShippingMgrService busShippingMgrService;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			jobid = AppUtils.getReqParam("jobid");

		}
	}	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);

		BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid = " + this.jobid + "");
		String carrierid = String.valueOf(busShipping.getCarrierid());

		String filter = "\nAND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.saleid = b.salesid AND x.id = "+jobid+")";
		filter += "\nAND carrierid = " + carrierid;
		m.put("filter", filter);
		return m;
	}
	

	@Action
	public void clearQryqryMapSo(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ){
		qryMap.clear();
		this.grid.reload();
	}

	@Action
	public void grid_ondblclick() {
		try {
			// long rowId = this.getGridSelectId();
			// //将之前绑定的解绑
			// String withdrawSOSql = "\n UPDATE bus_ship_booking SET jobid = null WHERE jobid = (" + jobid + ");";
			// serviceContext.daoIbatisTemplate.updateWithUserDefineSql(withdrawSOSql);
			//
			// //绑定新so数据
			// String dmlSql = "UPDATE bus_ship_booking SET jobid = " + jobid + " WHERE id = " + rowId + ";";
			// serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
			//
			// String sql = "SELECT f_bus_booking_sync_jobs('bookingid=" + rowId + ";jobid=" + jobid + "')";
			// serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			// Browser.execClientScript("parent.importbusbooking()");
			// MessageUtils.showMsg("覆盖so数据成功");

			long rowId = this.getGridSelectId();
			BusShipBooking busShipBooking = serviceContext.busBookingMgrService.busShipBookingDao.findById(Long.valueOf(rowId));
			Browser.execClientScript("parent.setSo('" + busShipBooking.getSono() + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
