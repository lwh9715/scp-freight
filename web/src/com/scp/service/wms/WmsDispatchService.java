package com.scp.service.wms;
import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.wms.WmsDispatchDao;
import com.scp.model.wms.WmsDispatch;
import com.scp.util.AppUtils;

@Component
@Lazy(true)
public class WmsDispatchService{
	
	@Resource
	public WmsDispatchDao wmsDispatchDao; 

	public void saveData(WmsDispatch data) {
		if(0 == data.getId()){
			wmsDispatchDao.create(data);
		}else{
			wmsDispatchDao.modify(data);
		}
	}

	public void removeDate(Long id) {
//		WmsDispatch data = wmsDispatchDao.findById(id);
//		wmsDispatchDao.remove(data);
		String sql = "UPDATE wms_dispatch SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id =  "+id;
		wmsDispatchDao.executeSQL(sql);
	} 
}