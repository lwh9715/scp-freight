package com.scp.service.wms;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.wms.WmsPriceDao;
import com.scp.model.wms.WmsPrice;

@Component
@Lazy(true)
public class WmsPriceMgrService2{
	
	public WmsPriceMgrService2(){
		//System.out.println("WmsPriceMgrService22222 constuct...................");
	}
	
	@Resource
	public WmsPriceDao wmsPriceDao; 

	public void saveData(WmsPrice data) {
		if(0 == data.getId()){
			wmsPriceDao.create(data);
		}else{
			wmsPriceDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		WmsPrice data = wmsPriceDao.findById(id);
		wmsPriceDao.remove(data);
	} 
}