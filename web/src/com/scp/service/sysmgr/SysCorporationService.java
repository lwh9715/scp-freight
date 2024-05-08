package com.scp.service.sysmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysCorporationDao;
import com.scp.model.sys.SysCorporation;

@Component
public class SysCorporationService{
	
	
	@Resource
	public SysCorporationDao sysCorporationDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(SysCorporation data) {
		if(0 == data.getId()){
			sysCorporationDao.create(data);
		}else{
			sysCorporationDao.modify(data);
		}
	}
	
}