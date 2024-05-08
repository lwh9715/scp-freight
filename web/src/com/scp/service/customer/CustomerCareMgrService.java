package com.scp.service.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpcareDao;
import com.scp.model.sys.SysCorpcare;
import com.scp.util.StrUtils;

@Component
public class CustomerCareMgrService {

	@Resource
	public SysCorpcareDao sysCorpcareDao;

	public void saveData(SysCorpcare data) {
		if (0 == data.getId()) {
			sysCorpcareDao.create(data);
		} else {
			sysCorpcareDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCorpcare data = sysCorpcareDao.findById(id);
		sysCorpcareDao.remove(data);
	}

	public void removeDatedel(Long pkid) {

		String sql = "update sys_corpcare set isdelete=TRUE WHERE id = " + pkid
				+ ";";
		sysCorpcareDao.executeSQL(sql);

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE sys_corpcare SET isdelete = true  WHERE id in ("+id+")";
		sysCorpcareDao.executeSQL(sql);
		
	}

}