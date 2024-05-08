package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaFeeDao;
import com.scp.model.oa.OaFee;

@Component
@Lazy(true)
public class OaFeeService {

	@Resource
	public OaFeeDao oaFeeDao;

	public void saveData(OaFee data) {
		if (0 == data.getId()) {
			oaFeeDao.create(data);
		} else {
			oaFeeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaFee data = oaFeeDao.findById(id);
		oaFeeDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_fee SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaFeeDao.executeSQL(sql);
		}
	}

}
