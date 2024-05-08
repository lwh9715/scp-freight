package com.scp.service.del;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.del.DelDeliveryDao;
import com.scp.model.del.DelDelivery;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class DeliveryMgrService{

	
	@Resource
	public DelDeliveryDao deliveryDao; 
	

	public void saveData(DelDelivery data) {
		if(0 == data.getId()){
			deliveryDao.create(data);
		}else{
			deliveryDao.modify(data);
		}
	}


	public void removeDate(Long id) {
		String sql = 
				"UPDATE del_delivery SET isdelete = TRUE WHERE id = " + id +";";
		deliveryDao.executeSQL(sql);
	}

   /**
    * 
    * @param ids
 * @throws Exception 
    */
	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_delivery SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		deliveryDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids)throws Exception {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_delivery SET ischeck = false  WHERE id in ("+id+")";
		deliveryDao.executeSQL(sql);
	}
	
	public void updateSend(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_delivery SET issend = true ,sendtime = NOW() WHERE id in ("+id+")";
		deliveryDao.executeSQL(sql);
	}


	public void updateCancelSend(String[] ids)throws Exception {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_delivery set issend = false ,sendtime = null WHERE id in ("+id+")";
		deliveryDao.executeSQL(sql);
	}

	
}
