package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.RegisterDao;
import com.scp.model.sys.Register;
import com.scp.util.StrUtils;

@Component
public class RegisterService {

	@Resource
	public RegisterDao registerDao;

	public void saveData(Register data) {
		if (0 == data.getId()) {
			registerDao.create(data);
		} else {
			registerDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		Register data = registerDao.findById(id);
		registerDao.remove(data);
	}

	public void removeDates(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE cs_register SET isdelete = TRUE WHERE id IN ("+id+")";
		registerDao.executeSQL(sql);
	}
}
