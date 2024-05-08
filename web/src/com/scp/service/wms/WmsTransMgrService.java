package com.scp.service.wms;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.wms.WmsTransDao;
import com.scp.model.wms.WmsTrans;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsTransMgrService {

	@Resource
	public WmsTransDao wmsTransDao;

	public void saveData(WmsTrans data) {
		if (0 == data.getId()) {
			wmsTransDao.create(data);
		} else {
			wmsTransDao.modify(data);
		}
	}

	public void removeDate(Long id) {

		String sql = "UPDATE wms_trans SET isdelete=TRUE WHERE id = " + id + ";";
		sql += "\nUPDATE wms_transdtl SET isdelete=TRUE WHERE transid = " + id
				+ ";";
		wmsTransDao.executeSQL(sql);

	}

	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_trans SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		wmsTransDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_trans SET ischeck = false  WHERE id in ("+id+")";
		wmsTransDao.executeSQL(sql);
		
	}

}
