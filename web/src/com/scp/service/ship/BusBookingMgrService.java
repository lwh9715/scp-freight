package com.scp.service.ship;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipBookdtlDao;
import com.scp.dao.ship.BusShipBookingDao;
import com.scp.model.ship.BusShipBookdtl;
import com.scp.model.ship.BusShipBooking;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@Component
public class BusBookingMgrService{
	
	
	@Resource
	public BusShipBookingDao busShipBookingDao; 
	
	@Resource
	public BusShipBookdtlDao busShipBookdtlDao;
	
	public void saveData(BusShipBooking data) {
		if(0 == data.getId()){
			busShipBookingDao.create(data);
		}else{
			busShipBookingDao.modify(data);
		}
	}

	public void removeDate(Long id , String user) {
		String sql = "\nUPDATE bus_ship_booking SET isdelete = TRUE , updater = '"+user+"' , updatetime = NOW() WHERE id = " + id + ";";
		busShipBookingDao.executeSQL(sql);
	}

	public void saveDataDtl(BusShipBookdtl ddata) {
		if(0 == ddata.getId()){
			busShipBookdtlDao.create(ddata);
		}else{
			busShipBookdtlDao.modify(ddata);
		}
	}

//	public void chooseBook(Long bookdtlid, Long containerid,String user,Long userid) {
//		String sql="SELECT f_bus_book_choose('bookingid="+bookdtlid+";containerid="+containerid+";updater="+user+";userid="+userid+"');";
//		busShipBookingDao.executeQuery(sql);
//	}
	
	public void chooseBook(String[] ids, Long shipid,Long containid, String user,Long userid) {
		String bookids = StrUtils.array2List(ids);
		//bookids = bookids.replaceAll(",", "-");
		String sql="SELECT f_bus_book_choose('bookingids="+bookids+";containid="+containid+";shipid="+shipid+";inputer="+user+";userid="+userid+"');";
		busShipBookingDao.executeQuery(sql);
	}

	public void cancelBook(String[] ids,String user) {
		for(int i=0;i<ids.length;i++){
			String sql="SELECT f_bus_book_cancel('id="+ids[i]+";updater="+user+"');";
			busShipBookingDao.executeQuery(sql);
		}
	}

	public void returnCus(String[] ids) {
		for(int i=0;i<ids.length;i++){
			String sql="SELECT f_bus_book_return('id="+ids[i]+";updater="+AppUtils.getUserSession().getUsercode()+"');";
			busShipBookingDao.executeQuery(sql);
		}
		
	}

	public String[] generateMailInfo(Long pkVal, Long userid) {
		String sql = "SELECT f_sys_mail_generate('type=shipping;id="+pkVal+";userid="+userid+"') AS info";
		try {
			List list = busShipBookingDao.executeQuery(sql);
			if(list == null || list.size() < 0) {
				return null;
			}else{
				String txt = (String) list.get(0);
				String[] mailInfos = txt.split("-.-.-");
				return mailInfos;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{};
	}

	/**
	 * @author Neo
	 * 2014/5/5
	 * 分配
	 * @param ids
	 */
	public void assignBooking(String[] ids, String id) {
		StringBuffer newIds = new StringBuffer();
		// 合并id
		newIds.append("(");
		for(int i=0;i<ids.length;i++) {
			if(i == (ids.length - 1)) {
				newIds.append(ids[i]);
			} else {
				newIds.append(ids[i] + ",");
			}
		}
		newIds.append(")");
		
		Long userid = Long.parseLong(id);
		String sql = "UPDATE bus_ship_bookdtl SET bookstate='A',userid_assign=" + userid + " WHERE id IN" + newIds.toString();
		try {
			this.busShipBookdtlDao.executeSQL(sql);
		} catch(Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	/**
	 * @author Neo
	 * 2014/5/5
	 * 收回
	 * @param ids
	 */
	public void withdrawBooking(String[] ids) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE bus_ship_bookdtl SET bookstate='I',userid_assign=null WHERE id IN(");
		for(int i=0;i<ids.length;i++) {
			if(i == (ids.length - 1)) {
				sql.append(ids[i]);
			} else {
				sql.append(ids[i] + ",");
			}
		}
		sql.append(");");
		
		try {
			this.busShipBookdtlDao.executeSQL(sql.toString());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}

	public void delordrop(String[] soids, String shipids, String user, String type) {
		String sos = StrUtils.array2List(soids);
		Long scheduleid = Long.valueOf(shipids);
		String sql = "SELECT f_bus_booking_delordrop('soids="+sos+";bookingid="+scheduleid+";type="+type+";user="+user+"')";
		this.busShipBookdtlDao.executeQuery(sql);
	}

	public void removedtlDate(String[] ids, String usercode) {
		String id = StrUtils.array2List(ids);
		String sql = "\nUPDATE bus_ship_bookdtl SET isdelete = TRUE , updater = '"+usercode+"' , updatetime = NOW() WHERE id  in( " + id + ");";
		this.busShipBookdtlDao.executeSQL(sql);
		
	}
	public BusShipBooking findBookingByOrderId(Long orderId) {
		List<BusShipBooking> list = this.busShipBookingDao.findAllByClauseWhere("orderId="+orderId);
		return list != null && list.size() == 1 ? list.get(0) : null;
	}
}