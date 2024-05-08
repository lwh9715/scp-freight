package com.scp.service.bus;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusInsurancetDao;
import com.scp.model.bus.BusInsurance;

@Component
public class BusInsuranceService{
	
	@Resource
	public BusInsurancetDao datInsurancetDao; 

	public void saveData(BusInsurance data) {
		if(0 == data.getId()){
			datInsurancetDao.create(data);
		}else{
			datInsurancetDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusInsurance data = datInsurancetDao.findById(id);
		if(data == null)return;
		datInsurancetDao.remove(data);
	} 
	
	public void removeDateIsdelete(Long id) {
		BusInsurance data = datInsurancetDao.findById(id);
		if(data == null)return;
		data.setIsdelete(true);
		datInsurancetDao.modify(data);
	} 
	
	
	public BusInsurance findByjobId(Long jobid) {
		String sql = "isdelete = false AND  jobid ="+jobid;
		return this.datInsurancetDao.findOneRowByClauseWhere(sql);
	} 
	
	
}
