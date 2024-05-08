package com.scp.service.wms;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.wms.WmsOutDao;
import com.scp.model.wms.WmsOut;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsOutMgrService {

	@Resource
	public WmsOutDao wmsOutDao;

	public void saveData(WmsOut data) {
		if (0 == data.getId()) {
			wmsOutDao.create(data);
		} else {
			wmsOutDao.modify(data);
		}
	}

	public void removeDate(Long id) {

		String sql = "UPDATE wms_out SET isdelete=TRUE WHERE id = " + id + ";";
		sql += "\nUPDATE wms_outdtl SET isdelete=TRUE WHERE outid = " + id
				+ ";";
		wmsOutDao.executeSQL(sql);

	}

	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_out SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		//System.out.println(sql); 
		wmsOutDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_out SET ischeck = false  WHERE id in ("+id+")";
		//System.out.println(sql);
		wmsOutDao.executeSQL(sql);
		
	}

}
