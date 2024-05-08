package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaTimeSheetDao;
import com.scp.model.oa.OaTimeSheet;

@Component
@Lazy(true)
public class OaTimeSheetService {

	@Resource
	public OaTimeSheetDao oaTimeSheetDao;

	public void saveData(OaTimeSheet data) {
		if (0 == data.getId()) {
			oaTimeSheetDao.create(data);
		} else {
			oaTimeSheetDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaTimeSheet data = oaTimeSheetDao.findById(id);
		oaTimeSheetDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_timesheet SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			oaTimeSheetDao.executeSQL(sql);
		}
	}

}
