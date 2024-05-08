package com.scp.service.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpaccountDao;
import com.scp.model.sys.SysCorpaccount;
import com.scp.util.StrUtils;

@Component
public class CustomerAccountMgrService {

	@Resource
	public SysCorpaccountDao sysCorpaccountDao;

	public void saveData(SysCorpaccount data) {
		if (0 == data.getId()) {
			sysCorpaccountDao.create(data);
		} else {
			sysCorpaccountDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCorpaccount data = sysCorpaccountDao.findById(id);
		sysCorpaccountDao.remove(data);
	}

	public void removeDatedel(Long pkid) {

		String sql = "update sys_corpaccount  set isdelete=TRUE WHERE id = " + pkid
				+ ";";
		sysCorpaccountDao.executeSQL(sql);

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE sys_corpaccount  SET isdelete = true  WHERE id in ("+id+")";
		sysCorpaccountDao.executeSQL(sql);
		
	}

}