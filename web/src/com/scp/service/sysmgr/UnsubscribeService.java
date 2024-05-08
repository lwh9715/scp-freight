package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.UnsubscribeDao;
import com.scp.model.sys.Unsubscribe;
import com.scp.util.StrUtils;



@Component
public class UnsubscribeService {

	@Resource
	public UnsubscribeDao unsubscribeDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(Unsubscribe data) {		
		if(0 == data.getId()){			
			unsubscribeDao.create(data);			
		}else{			
			unsubscribeDao.modify(data);
		}
	}

	public void removeDate(String[] ids) {
		String sql = "DELETE FROM bus_unsubscribe WHERE id IN ("+StrUtils.array2List(ids)+");";
		unsubscribeDao.executeSQL(sql);
	}

	
}
