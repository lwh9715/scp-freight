package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaUserinfoDao;
import com.scp.model.oa.OaUserinfo;

@Component
@Lazy(true)
public class OaUserInfoService {

	@Resource
	public OaUserinfoDao oaUserinfoDao;

	public void saveData(OaUserinfo data) {
		if (0 == data.getId()) {
			oaUserinfoDao.create(data);
		} else {
			oaUserinfoDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaUserinfo data = oaUserinfoDao.findById(id);
		oaUserinfoDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_userinfo SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaUserinfoDao.executeSQL(sql);
		}
	}

}
