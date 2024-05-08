package com.scp.service.price;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.BusPriceDao;
import com.scp.model.price.BusPrice;

@Component
public class BusPriceService{
	
	@Resource
	public BusPriceDao busPriceDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(BusPrice data) {
		if (0 > data.getId()) {
			busPriceDao.create(data);
		} else {
			busPriceDao.modify(data);
		}
	}
	
	public void removeDate(Long id, String user) {
		String sql = "\nUPDATE bus_price SET isdelete = TRUE , updater = '"
				+ user + "' , updatetime = NOW() WHERE id = " + id + ";";
		busPriceDao.executeSQL(sql);
	}
	
	
	public String addcopyjobs(String id, Long customerid, Long userid, String usercode) {
		Long jobsid = Long.valueOf(id);
		String sql = "SELECT f_busprice_addcopy('id="+jobsid+";customerid="+customerid+";userid="+userid+";inputer="+usercode +";') AS nos;";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("nos");
		
	}
	
}