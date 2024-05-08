package com.scp.service.oa;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.UserLogDao;
import com.scp.model.oa.OaUserLog;

@Component
@Lazy(true)
public class UserLogService {

	@Resource
	public UserLogDao userLogDao;

	public void saveData(OaUserLog data) {
		if (0 == data.getId()) {
			userLogDao.create(data);
		} else {
			userLogDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaUserLog data = userLogDao.findById(id);
		userLogDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_log_rp SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			userLogDao.executeSQL(sql);
		}
	}

}
