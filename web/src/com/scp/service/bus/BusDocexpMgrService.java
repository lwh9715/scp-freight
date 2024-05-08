package com.scp.service.bus;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusDocexpDao;
import com.scp.model.bus.BusDocexp;
import com.scp.util.StrUtils;

@Component
public class BusDocexpMgrService{
	

	
	
	@Resource
	public BusDocexpDao busDocexpDao; 
	

	public void saveData(BusDocexp data) {
		if(0 == data.getId()){
			busDocexpDao.create(data);
		}else{
			busDocexpDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusDocexp data = busDocexpDao.findById(id);
		busDocexpDao.remove(data);
	}

	
public void choose(String[] ids) {
		
		
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_docdef SET ischoose = true WHERE id in ("+id+")";
		busDocexpDao.executeSQL(sql);
	} 
	

	public void cancel(String[] ids) {
		
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_docdef SET ischoose = false WHERE id in ("+id+")";
		busDocexpDao.executeSQL(sql);
	}

	
	
	
}