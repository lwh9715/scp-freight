package com.scp.service.sysmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysBusnodescDao;
import com.scp.dao.sys.SysBusnoruleDao;
import com.scp.model.sys.SysBusnodesc;
import com.scp.model.sys.SysBusnorule;

@Component
public class BusNoRuleMgrService{
	
	@Resource
	public SysBusnodescDao sysBusnodescDao; 
	
	@Resource
	public SysBusnoruleDao sysBusnoruleDao; 

	public void saveData(SysBusnorule data) {
		if(0 == data.getId()){
			sysBusnoruleDao.create(data);
		}else{
			sysBusnoruleDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysBusnorule data = sysBusnoruleDao.findById(id);
		sysBusnoruleDao.remove(data);
	}

	public void saveDataBusNoType(SysBusnodesc data) {
		if(0 == data.getId()){
			sysBusnodescDao.create(data);
		}else{
			sysBusnodescDao.modify(data);
		}
	} 
	
}