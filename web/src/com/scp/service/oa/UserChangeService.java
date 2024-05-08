package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.UserChangeDao;
import com.scp.model.oa.UserChange;

@Component
@Lazy(true)
public class UserChangeService {

	@Resource
	public UserChangeDao userChangeDao;

	public void saveData(UserChange data) {
		if (0 == data.getId()) {
			userChangeDao.create(data);
		} else {
			userChangeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		UserChange data = userChangeDao.findById(id);
		userChangeDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_log_change SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			userChangeDao.executeSQL(sql);
		}
	}

}
