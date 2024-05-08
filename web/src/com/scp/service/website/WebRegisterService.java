package com.scp.service.website;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.website.WebRegisterDao;
import com.scp.model.website.WebRegister;

@Component
public class WebRegisterService{
	
	@Resource
	public WebRegisterDao webRegisterDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(WebRegister data) {
		if (0 > data.getId()) {
			webRegisterDao.create(data);
		} else {
			webRegisterDao.modify(data);
		}
	}
	
	/**
	 * 审核
	 */
	public void check(Long id, String user) {
		String sql = "\nUPDATE web_register SET ischeck = TRUE , checker = '"
				+ user + "' , checktime = NOW() WHERE id = " + id + ";";
		webRegisterDao.executeSQL(sql);
	}
	
	/**
	 * 取消审核
	 */
	public void uncheck(Long id, String user) {
		String sql = "\nUPDATE web_register SET ischeck = false , checker = '', checktime = null WHERE id = " + id + ";";
		webRegisterDao.executeSQL(sql);
	}
	
	
}