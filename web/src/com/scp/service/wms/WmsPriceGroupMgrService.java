package com.scp.service.wms;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.wms.WmsPriceDao;
import com.scp.dao.wms.WmsPriceGroupDao;
import com.scp.model.wms.WmsPrice;
import com.scp.model.wms.WmsPriceGroup;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsPriceGroupMgrService{
	
	@Resource
	public WmsPriceGroupDao wmsPriceGroupDao; 
	
	@Resource
	public WmsPriceDao wmsPriceDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(WmsPrice data) {
		if(0 == data.getId()){
			wmsPriceDao.create(data);
			
		}else{
			wmsPriceDao.modify(data);
		}
	}
	
	public void saveDtlData(WmsPriceGroup data) {
		
		if(0 == data.getId()){
			
			wmsPriceGroupDao.create(data);
			
		}else{
			wmsPriceGroupDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		String sql = "DELETE FROM wms_price WHERE id = " + id +";";
		sql += "\nDELETE FROM wms_price_group WHERE priceid = " + id +";";
		wmsPriceDao.executeSQL(sql);
	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "Delete from wms_price_group  WHERE id in ("+id+")";
		wmsPriceGroupDao.executeSQL(sql);
		
	}
	
	
}