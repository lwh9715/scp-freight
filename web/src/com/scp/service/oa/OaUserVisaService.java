package com.scp.service.oa;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaUserVisaDao;
import com.scp.model.oa.OaUserVisa;

@Component
@Lazy(true)
public class OaUserVisaService {

	@Resource
	public OaUserVisaDao oaUserVisaDao;

	public void saveData(OaUserVisa data) {
		if (0 == data.getId()) {
			oaUserVisaDao.create(data);
		} else {
			oaUserVisaDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaUserVisa data = oaUserVisaDao.findById(id);
		oaUserVisaDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_visa  SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaUserVisaDao.executeSQL(sql);
		}
	}

}
