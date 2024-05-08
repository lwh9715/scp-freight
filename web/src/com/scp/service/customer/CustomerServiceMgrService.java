package com.scp.service.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpserviceDao;
import com.scp.model.sys.SysCorpservice;
import com.scp.util.StrUtils;

@Component
public class CustomerServiceMgrService {

	@Resource
	public SysCorpserviceDao sysCorpserviceDao;

	public void saveData(SysCorpservice data) {
		if (0 == data.getId()) {
			sysCorpserviceDao.create(data);
		} else {
			sysCorpserviceDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCorpservice data = sysCorpserviceDao.findById(id);
		sysCorpserviceDao.remove(data);
	}

	public void removeDatedel(Long pkid) {

		String sql = "update sys_corpservice set isdelete=TRUE WHERE id = " + pkid
				+ ";";
		sysCorpserviceDao.executeSQL(sql);

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE sys_corpservice SET isdelete = true  WHERE id in ("+id+")";
		sysCorpserviceDao.executeSQL(sql);
		
	}

}