package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysformcfgDao;
import com.scp.model.sys.Sysformcfg;

@Component
public class SysformcfgService{
	
	@Resource
	public SysformcfgDao sysformcfgDao;
	
	public void saveData(Sysformcfg data) {
		if(0 == data.getId()){
			sysformcfgDao.create(data);
		}else{
			sysformcfgDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		Sysformcfg data = sysformcfgDao.findById(id);
		sysformcfgDao.remove(data);
	}

	public void setRequir(Long id){
		Sysformcfg data = sysformcfgDao.findById(id);
		data.setIsrequired(true);
		sysformcfgDao.modify(data);
	}
	
	public void setnotRequir(Long id){
		Sysformcfg data = sysformcfgDao.findById(id);
		data.setIsrequired(false);
		sysformcfgDao.modify(data);
	}
	
	public void setIshide(Long id){
		Sysformcfg data = sysformcfgDao.findById(id);
		data.setIshide(true);
		sysformcfgDao.modify(data);
	}
	
	public void setnotIshide(Long id){
		Sysformcfg data = sysformcfgDao.findById(id);
		data.setIshide(false);
		sysformcfgDao.modify(data);
	}
	
	public void setColors(Long id,String color){
		Sysformcfg data = sysformcfgDao.findById(id);
		data.setColor(color);
		sysformcfgDao.modify(data);
	}
	
	public void setColorsNull(Long id){
		Sysformcfg data = sysformcfgDao.findById(id);
		data.setColor("");
		sysformcfgDao.modify(data);
	}
}
