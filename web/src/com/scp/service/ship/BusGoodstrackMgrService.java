package com.scp.service.ship;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusGoodstrackDao;
import com.scp.model.ship.BusGoodstrack;
@Component
public class BusGoodstrackMgrService {

	@Resource
	public BusGoodstrackDao busGoodstrackDao;

	public void saveData(BusGoodstrack data) {
		if (0 == data.getId()) {
			busGoodstrackDao.create(data);
		} else {
			busGoodstrackDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusGoodstrack data = busGoodstrackDao.findById(id);
		busGoodstrackDao.remove(data);
	}
	
	
	public void finishDate(String id , boolean flag, Boolean isAutoRefreshTime , String usercode) {
		String sql ="UPDATE bus_goodstrack SET updater = '"+usercode+"',isfinish = "+flag+" WHERE id='"+id+"';";
		if(isAutoRefreshTime){
			if(flag){
				Date currentTime = new Date();
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    String dateString = formatter.format(currentTime);
				sql ="UPDATE bus_goodstrack SET updater = '"+usercode+"',isfinish = "+flag+" , dealdate = '"+dateString+"' WHERE id='"+id+"';";
			}
		}
		busGoodstrackDao.executeSQL(sql);
	}

	public void removeDates(String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			stringBuilder.append("\nDELETE FROM bus_goodstrack WHERE id='"+id+"';");
		}
		busGoodstrackDao.executeSQL(stringBuilder.toString());
	}
}
