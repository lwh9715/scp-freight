package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiOoclBookdataDao;
import com.scp.model.api.ApiOoclBookData;

@Component
@Lazy(true)
public class ApiOoclBookdataService {

	@Resource
	public ApiOoclBookdataDao apiOoclBookdataDao; 

	public void saveData(ApiOoclBookData data) {
		if(0 == data.getId()){
			apiOoclBookdataDao.create(data);
		}else{
			apiOoclBookdataDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		ApiOoclBookData data = apiOoclBookdataDao.findById(id);
		if(data == null)return;
		apiOoclBookdataDao.remove(data);
	} 
	
	public void removeDateIsdelete(Long id) {
		ApiOoclBookData data = apiOoclBookdataDao.findById(id);
		if(data == null)return;
		data.setIsdelete(true);
		apiOoclBookdataDao.modify(data);
	} 
	
	
	public ApiOoclBookData findByproductId(String productid) {
		String sql = "isdelete = false AND  productid = '"+productid+"'";
		return this.apiOoclBookdataDao.findOneRowByClauseWhere(sql);
	} 
	
}
