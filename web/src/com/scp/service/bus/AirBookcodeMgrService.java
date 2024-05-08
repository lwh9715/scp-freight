package com.scp.service.bus;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.AirBookcodeDao;
import com.scp.model.bus.AirBookcode;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class AirBookcodeMgrService {

	@Resource
	public AirBookcodeDao airBookcodeDao;

	public void saveData(AirBookcode data) {
		if (0 == data.getId()) {
			airBookcodeDao.create(data);
		} else {
			airBookcodeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		AirBookcode data = airBookcodeDao.findById(id);
		airBookcodeDao.remove(data);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param usercode
	 */
	public void removeDate(String[] ids,String usercode) {
		String sql = "UPDATE air_bookingcode SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		airBookcodeDao.executeSQL(sql);
	}
	
}