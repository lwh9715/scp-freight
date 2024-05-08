package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCustlibDao;
import com.scp.dao.sys.SysCustlibRoleDao;
import com.scp.model.sys.SysCustlib;

@Component
public class SysCustLibMgrService{
	
	@Resource
	public SysCustlibDao sysCustlibDao; 
	
	@Resource
	public SysCustlibRoleDao sysCustlibRoleDao; 
	

	public void saveData(SysCustlib data) {
		if(0 == data.getId()){
			sysCustlibDao.create(data);
		}else{
			sysCustlibDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysCustlib data = sysCustlibDao.findById(id);
		sysCustlibDao.remove(data);
	}

	public void addCust(String libid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO sys_custlib_cust(id,custlibid,corpid) VALUES(getid(),"+libid+","+id+");";
			stringBuilder.append(sql);
		}
		sysCustlibDao.executeSQL(stringBuilder.toString());
	}

	public void addUser(String libid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO sys_custlib_user(id,custlibid,userid) VALUES(getid(),"+libid+","+id+");";
			stringBuilder.append(sql);
		}
		sysCustlibDao.executeSQL(stringBuilder.toString());
	}

	public void removeUser(String libid, String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM sys_custlib_user WHERE custlibid ="+libid+" AND userid="+id+";";
			stringBuilder.append(sql);
		}
		sysCustlibDao.executeSQL(stringBuilder.toString());
	} 
	
	public void removeCust(String libid, String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM sys_custlib_cust WHERE custlibid ="+libid+" AND corpid="+id+";";
			stringBuilder.append(sql);
		}
		sysCustlibDao.executeSQL(stringBuilder.toString());
	} 

}
