package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaLeaveapplicationDao;
import com.scp.model.oa.OaLeaveapplication;

@Component
@Lazy(true)
public class OaLeaveapplicationService {

	@Resource
	public OaLeaveapplicationDao oaLeaveapplicationDao;

	public void saveData(OaLeaveapplication data) {
		if (0 == data.getId()) {
			oaLeaveapplicationDao.create(data);
		} else {
			oaLeaveapplicationDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaLeaveapplication data = oaLeaveapplicationDao.findById(id);
		oaLeaveapplicationDao.remove(data);
	}
	
	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_leaveapplication SET isdelete = TRUE  WHERE id ='"+ids[i]+"';";
			oaLeaveapplicationDao.executeSQL(sql);
		}
	}

}
