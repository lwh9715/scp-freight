package com.scp.service.sysmgr;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysFormdefDao;
import com.scp.dao.sys.SysFormdefValDao;
import com.scp.model.sys.SysFormdef;
import com.scp.model.sys.SysFormdefVal;

@Component
public class SysFormdefService{
	
	@Resource
	public SysFormdefDao sysFormdefDao;
	
	@Resource
	public SysFormdefValDao sysFormdefValDao;
	
	
	public void saveData(SysFormdef data) {
		if(0 == data.getId()){
			sysFormdefDao.create(data);
		}else{
			sysFormdefDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysFormdef data = sysFormdefDao.findById(id);
		sysFormdefDao.remove(data);
	}
	
	
	public void saveDataDtl(SysFormdefVal data) {
		if(0 == data.getId()){
			sysFormdefValDao.create(data);
		}else{
			sysFormdefValDao.modify(data);
		}
	}

	public void removeDateDtl(Long id) {
		SysFormdefVal data = sysFormdefValDao.findById(id);
		sysFormdefValDao.remove(data);
	}
	
	public void setRequir(Long id){
		SysFormdef data = sysFormdefDao.findById(id);
		data.setIsrequired(true);
		sysFormdefDao.modify(data);
	}
	
	public void setnotRequir(Long id){
		SysFormdef data = sysFormdefDao.findById(id);
		data.setIsrequired(false);
		sysFormdefDao.modify(data);
	}
	
	public void setColors(Long id,String color){
		SysFormdef data = sysFormdefDao.findById(id);
		data.setColor(color);
		sysFormdefDao.modify(data);
	}
	
	public void setColorsNull(Long id){
		SysFormdef data = sysFormdefDao.findById(id);
		data.setColor("");
		sysFormdefDao.modify(data);
	}
	
	/** 
	 * 把不包含ids中的删掉
	 * @param ids 
	 * @param bean
	 * @param araptype
	 */
	public void delbefor(ArrayList<Long> ids,String bean,String araptype){
		List<SysFormdef> formdefs = sysFormdefDao.findAllByClauseWhere(" defvalue = '"+araptype+"' AND beaname = '"+bean+"'");
		for(SysFormdef formdef:formdefs){
			boolean in = ids.contains(formdef.getId());
			if(!in){
				sysFormdefDao.remove(formdef);
			}
		}
	}
}
