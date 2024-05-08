package com.scp.service.oa;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaUserContractDao;
import com.scp.model.oa.OaUserContract;

@Component
@Lazy(true)
public class OaUserContractService {

	@Resource
	public OaUserContractDao oaUserContractDao;

	public void saveData(OaUserContract data) {
		if (0 == data.getId()) {
			oaUserContractDao.create(data);
		} else {
			oaUserContractDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaUserContract data = oaUserContractDao.findById(id);
		oaUserContractDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_contract SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaUserContractDao.executeSQL(sql);
		}
	}

}
