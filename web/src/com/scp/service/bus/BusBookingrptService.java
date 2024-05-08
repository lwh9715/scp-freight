package com.scp.service.bus;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusBookingrptDao;
import com.scp.model.bus.BusBookingrpt;
import com.scp.util.StrUtils;

@Component
public class BusBookingrptService {
	
	@Resource
	public BusBookingrptDao bookingrptDao;

	public void saveData(BusBookingrpt data) {
		if(0 == data.getId()){
			bookingrptDao.create(data);
		}else{
			bookingrptDao.modify(data);
		}
		
	}

	public void removeDate(String[] ids, String usercode) {
		String sql = "UPDATE bus_bookingrpt SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		bookingrptDao.executeSQL(sql);
	}

	public void submitDate(String[] ids, String usercode) {
		String sql = "UPDATE bus_bookingrpt SET issubmit = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		bookingrptDao.executeSQL(sql);
	}

}
