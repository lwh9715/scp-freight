package com.scp.service.sysmgr;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysMlDao;
import com.scp.model.sys.SysMl;

@Component
public class SysMlService{
	
	@Resource
	public SysMlDao sysMlDao;
	
	public void saveData(SysMl data) {
		if(0 == data.getId()){
			sysMlDao.create(data);
		}else{
			sysMlDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysMl data = sysMlDao.findById(id);
		sysMlDao.remove(data);
	} 
	
	public void saveAutoFill(String key){
		String sql = 
			"\nINSERT INTO sys_ml(id , ch)"+
			"\nSELECT getid(),'%s' "+
			"\nFROM _virtual "+
			"\nWHERE NOT EXISTS (SELECT 1 FROM sys_ml WHERE ch = '%s');";
		sql = String.format(sql , key , key);
		try {
			sysMlDao.executeSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SysMl> findByCh(Object key) {
		return sysMlDao.findAllByClauseWhere("ch = '"+key+"'");
	}
}
