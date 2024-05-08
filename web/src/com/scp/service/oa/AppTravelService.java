package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.AppTravelDao;
import com.scp.model.oa.AppTravel;

@Component
@Lazy(true)
public class AppTravelService {
	
	

	public AppTravelService() {
		super();
		//System.out.println("AppTravelService constru....................");
	}

	@Resource
	public AppTravelDao appTravelDao;
	public void saveData(AppTravel data) {
		if (0 == data.getId()) {
			appTravelDao.create(data);
		} else {
			appTravelDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		AppTravel data = appTravelDao.findById(id);
		appTravelDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_apply_travel SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			appTravelDao.executeSQL(sql);
		}
	}

}
