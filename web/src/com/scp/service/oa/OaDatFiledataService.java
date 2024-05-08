package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.OaDatFiledata;
import com.scp.dao.oa.OaDatFiledataDao;

@Component
@Lazy(true)
public class OaDatFiledataService{
	
	@Resource
	public OaDatFiledataDao oaDatFiledataDao;
	
	public void saveData(OaDatFiledata data) {
		if(0 == data.getId()){
			oaDatFiledataDao.create(data);
		}else{
			oaDatFiledataDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		//OaDatFiledata data = oaDatFiledataDao.findById(id);
		//oaDatFiledataDao.remove(data);
		String sql="UPDATE oa_dat_filedata SET isdelete=TRUE where id=" + id;
		oaDatFiledataDao.executeSQL(sql);
	} 
	
}
