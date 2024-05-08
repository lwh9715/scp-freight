package com.scp.service.website;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.website.WebNewsDao;
import com.scp.model.website.WebNews;

@Component
public class WebNewsService{
	
	@Resource
	public WebNewsDao webNewsDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(WebNews data) {
		if (0 == data.getId()) {
			webNewsDao.create(data);
		} else {
			webNewsDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		WebNews data = webNewsDao.findById(id);
		webNewsDao.remove(data);
	}
	
	
}