package com.scp.service.ship;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipjoinDao;
import com.scp.model.ship.BusShipjoin;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusShipjoinMgrService{
	

	
	
	@Resource
	public BusShipjoinDao busShipjoinDao; 
	

	public void saveData(BusShipjoin data) {
		if(0 == data.getId()){
			busShipjoinDao.create(data);
		}else{
			busShipjoinDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipjoin data = busShipjoinDao.findById(id);
		busShipjoinDao.remove(data);
	}

	public void removeDate(Long id , String user) {
		String sql = "\nUPDATE bus_shipjoin SET isdelete = TRUE , updater = '"+user+"' , updatetime = NOW() WHERE id = " + id + ";";
		busShipjoinDao.executeSQL(sql);
	}

	public String[] generateMailInfo(Long pkVal, Long userid) {
		String sql = "SELECT f_sys_mail_generate('type=shipunion;id="+pkVal+";userid="+userid+"') AS info";
		List list = busShipjoinDao.executeQuery(sql);
		if(list == null || list.size() < 0) {
			return null;
		}else{
		String txt = (String) list.get(0);
		String[] mailInfos = txt.split("-.-.-");
		return mailInfos;
		}
	}

	/**
	 * Neo 2014/5/9
	 * 审核
	 * @param ids
	 */
	public void updateCheck(String[] ids) {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_shipjoin SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		this.busShipjoinDao.executeSQL(sql);
		
	}

	/**
	 * Neo 2014/5/9
	 * 取消审核
	 * @param ids
	 */
	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_shipjoin SET ischeck = false  WHERE id in ("+id+")";
		this.busShipjoinDao.executeSQL(sql);
		
	}
	
}