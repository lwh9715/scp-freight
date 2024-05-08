package com.scp.service.website;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.website.WebMenuDao;
import com.scp.model.website.WebMenu;

@Component
public class WebMenuService{
	
	@Resource
	public WebMenuDao webMenuDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(WebMenu data) {
		if (0 == data.getId()) {
			webMenuDao.create(data);
		} else {
			webMenuDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		WebMenu data = webMenuDao.findById(id);
		webMenuDao.remove(data);
	}
	
	
}