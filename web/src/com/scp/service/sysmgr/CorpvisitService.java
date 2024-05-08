package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.CorpvisitDao;
import com.scp.model.sys.SysCorpvisit;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class CorpvisitService {

	@Resource
	public CorpvisitDao corpvisitDao;

	public void saveData(SysCorpvisit data) {
		if (0 == data.getId()) {
			corpvisitDao.create(data);
		} else {
			corpvisitDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCorpvisit data = corpvisitDao.findById(id);
		corpvisitDao.remove(data);
	}

	public void removeDates(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE sys_corpvisit SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id IN ("+id+")";
		corpvisitDao.executeSQL(sql);
	}
}
