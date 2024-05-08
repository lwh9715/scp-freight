package com.scp.service.price;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.PriceHomepageDao;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.price.PriceHomepage;

@Component
public class PriceHomepageMgrService {
	
	@Resource
	public PriceHomepageDao priceHomepageDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	public void saveData(PriceHomepage data) {
		if (0 > data.getId()) {
			priceHomepageDao.create(data);
		} else {
			priceHomepageDao.modify(data);
		}
	}
	
	public void saveHomepage(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			try{
				PriceHomepage p = priceHomepageDao.findOneRowByClauseWhere("priceid="+id);
				if(p!=null){
					Browser.alert("已存在，不可重复添加");
					return;
				}
			}catch(NoRowException e){
			}catch(MoreThanOneRowException e){
				Browser.alert("已存在，不可重复添加");
				return;
			}
			String sql = "\n INSERT INTO price_homepage(id, priceid) "
				+"\n VALUES(getid(),"+id+");";
			stringBuffer.append(sql);
		}
		priceHomepageDao.executeSQL(stringBuffer.toString());
	}
	
	public void delHomepage(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\n DELETE FROM price_homepage WHERE id = "+id+";";
			stringBuffer.append(sql);
		}
		priceHomepageDao.executeSQL(stringBuffer.toString());
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceHomepage ddata = new PriceHomepage();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("homepageid"));
			String cost20=String.valueOf(jsonObject.get("cost20p"));
			String cost40gp=String.valueOf(jsonObject.get("cost40gpp"));
			String cost40hq=String.valueOf(jsonObject.get("cost40hqp"));
			
			ddata = priceHomepageDao.findById(Long.valueOf(id));
			ddata.setCost20(new BigDecimal(cost20));
			ddata.setCost40gp(new BigDecimal(cost40gp));
			ddata.setCost40hq(new BigDecimal(cost40hq));
			saveData(ddata);
		}		
	}
}
