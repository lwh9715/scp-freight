package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiCoscoBookdataDao;
import com.scp.model.api.ApiCoscoBookData;

@Component
@Lazy(true)
public class ApiCoscoBookdataService {

	@Resource
	public ApiCoscoBookdataDao apiCoscoBookdataDao; 

	public void saveData(ApiCoscoBookData data) {
		if(0 == data.getId()){
			apiCoscoBookdataDao.create(data);
		}else{
			apiCoscoBookdataDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		ApiCoscoBookData data = apiCoscoBookdataDao.findById(id);
		if(data == null)return;
		apiCoscoBookdataDao.remove(data);
	} 
	
	public void removeDateIsdelete(Long id) {
		ApiCoscoBookData data = apiCoscoBookdataDao.findById(id);
		if(data == null)return;
		data.setIsdelete(true);
		apiCoscoBookdataDao.modify(data);
	} 
	
	
	public ApiCoscoBookData findByproductId(String productid) {
		String sql = "isdelete = false AND  productid = '"+productid+"'";
		return this.apiCoscoBookdataDao.findOneRowByClauseWhere(sql);
	} 
	
}
