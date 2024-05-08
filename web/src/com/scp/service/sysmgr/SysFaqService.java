package com.scp.service.sysmgr;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysFaqDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.sys.SysFaq;

@Component
public class SysFaqService{
	
	@Resource
	public SysFaqDao sysFaqDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(SysFaq data) {
		if(0 == data.getId()){
			sysFaqDao.create(data);
		}else{
			sysFaqDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysFaq data = sysFaqDao.findById(id);
		sysFaqDao.remove(data);
	}

	public void saveDatas(List<SysFaq> datas) {
		for (SysFaq sysFaq : datas) {
			saveData(sysFaq);
		}
	}

	public void saveDataTest() {
		SysFaq data = new SysFaq();
		data.setContent("test");
		saveData(data);
		throw new CommonRuntimeException("test");
	}
}
