package com.scp.service.sysmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpinvDao;
import com.scp.model.sys.SysCorpinv;


@Component
public class SysCorpinvMgrService{
	
	
	@Resource
	public SysCorpinvDao sysCorpinvDao;
	
	
	public void saveData(SysCorpinv data) {
		if(0 == data.getId()){
			sysCorpinvDao.create(data);
		}else{
			sysCorpinvDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		SysCorpinv data = sysCorpinvDao.findById(id);
		sysCorpinvDao.remove(data);
	} 

}