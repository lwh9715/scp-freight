package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaUserTransferDao;
import com.scp.model.oa.OaUserTransfer;

@Component
@Lazy(true)
public class OaUserTransferService {

	@Resource
	public OaUserTransferDao oaUserTransferDao;

	public void saveData(OaUserTransfer data) {
		if (0 == data.getId()) {
			oaUserTransferDao.create(data);
		} else {
			oaUserTransferDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaUserTransfer data = oaUserTransferDao.findById(id);
		oaUserTransferDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_user_contract  SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaUserTransferDao.executeSQL(sql);
		}
	}

}
