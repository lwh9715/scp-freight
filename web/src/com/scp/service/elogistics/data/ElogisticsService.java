package com.scp.service.elogistics.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.elogistics.data.ElogisticsDao;
import com.scp.model.elogistics.data.Elogistics;

@Component
public class ElogisticsService {
	
	@Resource
	public ElogisticsDao elogisticsDao; 

	public void saveData(Elogistics data) {
		if(0 == data.getId()){
			elogisticsDao.create(data);
		}else{
			elogisticsDao.modify(data);
		}
	}

	public void removeDate(Long id, String user) {
		String sql = "\nUPDATE bus_elogistics SET isdelete = TRUE , updater = '"
			+ user + "' , updatetime = NOW() WHERE id = " + id + ";";
		elogisticsDao.executeSQL(sql);
		
		
	} 
	
	public Elogistics findByJobId(Long jobid) {
		String sql = "jobid ="+jobid;
		return this.elogisticsDao.findOneRowByClauseWhere(sql);
	}
	
}
