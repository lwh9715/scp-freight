package com.scp.service.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpcontactsDao;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.StrUtils;

@Component
public class CustomerContactsMgrService {

	@Resource
	public SysCorpcontactsDao sysCorpcontactsDao;

	public void saveData(SysCorpcontacts data) {
		if (0 == data.getId()) {
			sysCorpcontactsDao.create(data);
		} else {
			sysCorpcontactsDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCorpcontacts data = sysCorpcontactsDao.findById(id);
		sysCorpcontactsDao.remove(data);
	}

	public void removeDatedel(Long pkid) {

		String sql = "update sys_corpcontacts set isdelete=TRUE WHERE id = " + pkid
				+ ";";
		sysCorpcontactsDao.executeSQL(sql);

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE sys_corpcontacts SET isdelete = true  WHERE id in ("+id+")";
		sysCorpcontactsDao.executeSQL(sql);
		
	}

}