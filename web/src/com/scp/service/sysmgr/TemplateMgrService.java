package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysTempletDao;
import com.scp.model.sys.SysTemplet;

@Component
public class TemplateMgrService{
	
	@Resource
	public SysTempletDao sysTempletDao;
	
	public void saveData(SysTemplet data) {
		if(0 == data.getId()){
			sysTempletDao.create(data);
		}else{
			sysTempletDao.modify(data);
		}
	} 
	
	public void delDatas(String ids) {
		String dmlSqlBefore = 
			"\nUPDATE sys_templet SET isdelete = true WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysTempletDao.executeSQL(dmlSqlBefore);
	}
}
