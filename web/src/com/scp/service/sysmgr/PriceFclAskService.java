package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.PriceFclASKDao;
import com.scp.model.sys.PriceFclAsk;

@Component
public class PriceFclAskService{
	
	@Resource
	public PriceFclASKDao priceFclDao;
	
	public void saveData(PriceFclAsk data) {
		if(0 == data.getId()){
			priceFclDao.create(data);
		}else{
			priceFclDao.modify(data);
		}
	} 
	

	public void removeDate(Long id) {
		PriceFclAsk data = priceFclDao.findById(id);
		priceFclDao.remove(data);
	} 
}
