package com.scp.service.bus;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusDocexpLinkDao;
import com.scp.model.bus.BusDocexpLink;
import com.scp.util.StrUtils;

@Component
public class BusDocexpLinkMgrService{
	

	
	
	@Resource
	public BusDocexpLinkDao busDocexpLinkDao; 
	

	public void saveData(BusDocexpLink data) {
		if(0 == data.getId()){
			busDocexpLinkDao.create(data);
		}else{
			busDocexpLinkDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusDocexpLink data = busDocexpLinkDao.findById(id);
		busDocexpLinkDao.remove(data);
	}

	
	public void cancel(String[] ids) {
		
		String id = StrUtils.array2List(ids);
		String sql = "delete from bus_docexp_link  WHERE id in ("+id+")";
		busDocexpLinkDao.executeSQL(sql);
	}
	
	
	
}