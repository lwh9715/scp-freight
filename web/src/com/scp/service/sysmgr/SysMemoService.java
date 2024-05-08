package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysMemoDao;
import com.scp.model.sys.SysMemo;

@Component("sysMemoService")
public class SysMemoService{
	
	@Resource(name="sysMemoDao")
	public SysMemoDao sysMemoDao;
	
	public void saveData(SysMemo data) {
		if(0 == data.getId()){
			sysMemoDao.create(data);
		}else{
			sysMemoDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysMemo data = sysMemoDao.findById(id);
		sysMemoDao.remove(data);
	} 
}
