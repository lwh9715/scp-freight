package com.scp.service.price;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.price.PriceTrainLclDao;
import com.scp.model.price.PriceTrainLcl;
import com.scp.util.AppUtils;

@Component
public class PriceTrainLclService{
	
	@Resource
	public PriceTrainLclDao priceTrainLclDao;
	
	public void saveData(PriceTrainLcl data) {
		if (0 > data.getId()) {
			priceTrainLclDao.create(data);
		} else {
			priceTrainLclDao.modify(data);
		}
	}
	
	public void removes(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_train_lcl SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceTrainLclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceTrainLcl ddata = new PriceTrainLcl();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String pol=String.valueOf(jsonObject.get("pol")).trim();
			String pod=String.valueOf(jsonObject.get("pod")).trim();
			String line=String.valueOf(jsonObject.get("line"));
			ddata = priceTrainLclDao.findById(Long.valueOf(id));
			ddata.setPol(pol);
			ddata.setPod(pod);
			saveData(ddata);
		}		
	}

	public void release(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_train_lcl SET isrelease = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceTrainLclDao.executeSQL(stringBuffer.toString());
	}
}