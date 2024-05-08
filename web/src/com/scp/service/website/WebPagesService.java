package com.scp.service.website;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.website.WebPagesDao;
import com.scp.model.website.WebPages;

@Component
public class WebPagesService{
	
	@Resource
	public WebPagesDao webPagesDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(WebPages data) {
		if (0 == data.getId()) {
			webPagesDao.create(data);
		} else {
			webPagesDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		WebPages data = webPagesDao.findById(id);
		webPagesDao.remove(data);
	}
	
	
}