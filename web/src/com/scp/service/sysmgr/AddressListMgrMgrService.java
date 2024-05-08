package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysAddresslistDao;
import com.scp.model.sys.SysAddresslist;

@Component
public class AddressListMgrMgrService{
	
	@Resource
	public SysAddresslistDao sysAddresslistDao;
	
	public void saveData(SysAddresslist data) {
		if(0 == data.getId()){
			sysAddresslistDao.create(data);
		}else{
			sysAddresslistDao.modify(data);
		}
	} 
	
	public void delDatas(String ids) {
		String dmlSqlBefore = 
			"\nUPDATE sys_addresslist SET isdelete = true WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysAddresslistDao.executeSQL(dmlSqlBefore);
	}
}
