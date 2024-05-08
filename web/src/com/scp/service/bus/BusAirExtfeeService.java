package com.scp.service.bus;


import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusAirExtfeeDao;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusAirExtfee;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class BusAirExtfeeService {

	@Resource
	public BusAirExtfeeDao busAirExtfeeDao;

	public void saveData(BusAirExtfee data) {
		if (0 == data.getId()) {
			busAirExtfeeDao.create(data);
		} else {
			busAirExtfeeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusAirExtfee data = busAirExtfeeDao.findById(id);
		busAirExtfeeDao.remove(data);
	}
	public BusAirExtfee findjobsByJobid(Long jobid){
		BusAirExtfee busair = null;
		try {
			busair = this.busAirExtfeeDao.findOneRowByClauseWhere("isdelete = FALSE AND jobid = "+jobid);
		}catch (NoRowException e) {
			busair = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busair;
	}
	
	public void updateGoodsSelects(String[] ids,Long id){
		if(id == null){
			return;
		}
		String sql = "UPDATE bus_goods SET isselect = FALSE WHERE isdelete = FALSE AND linkid = "+id;
		busAirExtfeeDao.executeSQL(sql);
		if(ids!=null&&ids.length>0){
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("\n UPDATE bus_goods SET isselect = TRUE WHERE isdelete = FALSE ");
			sbsql.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
			busAirExtfeeDao.executeSQL(sbsql.toString());
		}
	}

	public void removes(String[] ids,String usercode) {
		String sql = "UPDATE bus_air_extfee SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		busAirExtfeeDao.executeSQL(sql);
	}

	
	
	
	
}