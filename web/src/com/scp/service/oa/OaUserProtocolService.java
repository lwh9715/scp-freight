package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaUserProtocolDao;
import com.scp.model.oa.OaUserProtocol;

@Component
@Lazy(true)
public class OaUserProtocolService {

	@Resource
	public OaUserProtocolDao oaUserProtocolDao;

	public void saveData(OaUserProtocol data) {
		if (0 == data.getId()) {
			oaUserProtocolDao.create(data);
		} else {
			oaUserProtocolDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaUserProtocol data = oaUserProtocolDao.findById(id);
		oaUserProtocolDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_contract SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaUserProtocolDao.executeSQL(sql);
		}
	}

}
