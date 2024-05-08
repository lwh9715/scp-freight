package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysDepartmentDao;
import com.scp.model.sys.SysDepartment;

@Component
public class SysDeptService{
	
	@Resource
	public SysDepartmentDao sysDepartmentDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(SysDepartment data) {
		if(0 == data.getId()){
			sysDepartmentDao.create(data);
		}else{
			sysDepartmentDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysDepartment data = sysDepartmentDao.findById(id);
		sysDepartmentDao.remove(data);
	}

	public void removeDates(String[] ids) {
		for (String id : ids) {
			SysDepartment data = sysDepartmentDao.findById(Long.valueOf(id));
			sysDepartmentDao.remove(data);
		}
	}
}
