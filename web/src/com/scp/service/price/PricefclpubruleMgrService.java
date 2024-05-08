package com.scp.service.price;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.PricefclpubruleDao;
import com.scp.model.price.Pricefclpubrule;
import com.scp.util.StrUtils;

@Component
public class PricefclpubruleMgrService {
	
	@Resource
	public PricefclpubruleDao pricefclpubruleDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	public void saveData(Pricefclpubrule data) {
		if (0 > data.getId()) {
			pricefclpubruleDao.create(data);
		} else {
			pricefclpubruleDao.modify(data);
		}
	}
	
	public Pricefclpubrule findByruletype(){
		String sql = "ruletype='B'" ;
		List<Pricefclpubrule> pricefclpubrules = pricefclpubruleDao.findAllByClauseWhere(sql);
		if(pricefclpubrules.size()>0){
			return pricefclpubrules.get(0);
		}
		return null;
	} 
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		Pricefclpubrule ddata = new Pricefclpubrule();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String cost20=String.valueOf(jsonObject.get("cost20"));
			String cost40gp=String.valueOf(jsonObject.get("cost40gp"));
			String cost40hq=String.valueOf(jsonObject.get("cost40hq"));
			String cost45hq=String.valueOf(jsonObject.get("cost45hq"));
			
			ddata = pricefclpubruleDao.findById(Long.valueOf(id));
			ddata.setCost20(new BigDecimal(cost20));
			ddata.setCost40gp(new BigDecimal(cost40gp));
			ddata.setCost40hq(new BigDecimal(cost40hq));
			ddata.setCost45hq(new BigDecimal(cost45hq));
			saveData(ddata);
		}		
	}
	
	public void removeDate(String[] ids) {
		String sql = "DELETE FROM price_fcl_pubrule WHERE id IN ("+StrUtils.array2List(ids)+");";
		pricefclpubruleDao.executeSQL(sql);
	}
}
