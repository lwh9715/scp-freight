package com.scp.service.price;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.price.PriceTrainDao;
import com.scp.model.price.PriceTrain;
import com.scp.util.AppUtils;

@Component
public class PriceTrainService{
	
	@Resource
	public PriceTrainDao priceTrainDao;
	
	public void saveData(PriceTrain data) {
		if (0 > data.getId()) {
			priceTrainDao.create(data);
		} else {
			priceTrainDao.modify(data);
		}
	}
	
	public void removes(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_train SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceTrainDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceTrain ddata = new PriceTrain();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String pol=String.valueOf(jsonObject.get("pol")).trim();
			String pod=String.valueOf(jsonObject.get("pod")).trim();
			String line=String.valueOf(jsonObject.get("line"));
			ddata = priceTrainDao.findById(Long.valueOf(id));
			ddata.setPol(pol);
			ddata.setPod(pod);
			ddata.setLine(line);
			saveData(ddata);
		}		
	}

	public void release(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_train SET isrelease = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceTrainDao.executeSQL(stringBuffer.toString());
	}
}