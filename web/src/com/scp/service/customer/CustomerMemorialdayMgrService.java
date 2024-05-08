package com.scp.service.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpmemoryDao;
import com.scp.model.sys.SysCorpmemory;
import com.scp.util.StrUtils;

@Component
public class CustomerMemorialdayMgrService {

	@Resource
	public SysCorpmemoryDao sysCorpmemoryDao;

	public void saveData(SysCorpmemory data) {
		if (0 == data.getId()) {
			sysCorpmemoryDao.create(data);
		} else {
			sysCorpmemoryDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCorpmemory data = sysCorpmemoryDao.findById(id);
		sysCorpmemoryDao.remove(data);
	}

	public void removeDatedel(Long pkid) {

		String sql = "update sys_corpmemory set isdelete=TRUE WHERE id = " + pkid
				+ ";";
		sysCorpmemoryDao.executeSQL(sql);

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE sys_corpmemory SET isdelete = true  WHERE id in ("+id+")";
		sysCorpmemoryDao.executeSQL(sql);
		
	}
	
}