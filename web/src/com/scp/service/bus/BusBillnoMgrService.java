package com.scp.service.bus;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusBillnoMgrDao;
import com.scp.util.AppUtils;

@Component
public class BusBillnoMgrService {

	@Resource
	public BusBillnoMgrDao busBillnoMgrDao;

	public void removes(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE bus_billnomgr SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		busBillnoMgrDao.executeSQL(stringBuffer.toString());
		
	}
}
