package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatFiledataDao;
import com.scp.model.data.DatFiledata;

@Component
public class FiledataMgrService {

	@Resource
	public DatFiledataDao datFiledataDao;
	
	public void saveData(DatFiledata data) {
		if(0 == data.getId()){
			datFiledataDao.create(data);
		}else{
			datFiledataDao.modify(data);
		}
	}

	public void removeDate(Long id,String usercode) {
//		DatFiledata data = datFiledataDao.findById(id);
//		datFiledataDao.remove(data);
		String sql="UPDATE dat_filedata SET isdelete=TRUE,updater = '"+usercode+"' where id=" + id;
		datFiledataDao.executeSQL(sql);
	} 
	
	public String findExCodeByExcorp(String excorp){
		try{
			String sql = "fkcode = 70 AND isleaf = TRUE AND namec='"+excorp+"'";
			DatFiledata data = datFiledataDao.findOneRowByClauseWhere(sql);
			if(data!=null){
				return data.getCode();
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
