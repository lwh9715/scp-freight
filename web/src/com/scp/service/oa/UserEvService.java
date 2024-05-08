package com.scp.service.oa;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.UserLogEvDao;
import com.scp.model.oa.UserEval;

@Component
@Lazy(true)
public class UserEvService {

	@Resource
	public UserLogEvDao userLogEvDao;

	public void saveData(UserEval data) {
		if (0 == data.getId()) {
			userLogEvDao.create(data);
		} else {
			userLogEvDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		UserEval data = userLogEvDao.findById(id);
		userLogEvDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_log_evaluate SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			userLogEvDao.executeSQL(sql);
		}
	}

}
