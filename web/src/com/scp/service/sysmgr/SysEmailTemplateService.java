package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysEmailTemplateDao;
import com.scp.model.sys.SysEmailTemplate;

@Component
public class SysEmailTemplateService{
	
	@Resource
	public SysEmailTemplateDao sysEmailTemplateDao;

	public void saveData(SysEmailTemplate data) {
		if(0 == data.getId()){
			sysEmailTemplateDao.create(data);
		}else{
			sysEmailTemplateDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysEmailTemplate data = sysEmailTemplateDao.findById(id);
		sysEmailTemplateDao.remove(data);
	} 
	
}
