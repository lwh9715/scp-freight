package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysGridDefDao;
import com.scp.model.sys.SysGridDef;

@Component
public class SysGridDefService {

	@Resource
	public SysGridDefDao sysGridDefDao;

	public void saveData(SysGridDef data) {
		if (0 == data.getId()) {
			sysGridDefDao.create(data);
		} else {
			sysGridDefDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysGridDef data = sysGridDefDao.findById(id);
		sysGridDefDao.remove(data);
	}

}
