package com.scp.service.price;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.PriceFclBargefeeDao;
import com.scp.model.price.PriceFclBargefee;

@Component
public class PriceFclBargeService {
	
	@Resource
	public PriceFclBargefeeDao priceFclBargefeeDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	public void saveData(PriceFclBargefee data) {
		if (0 > data.getId()) {
			priceFclBargefeeDao.create(data);
		} else {
			priceFclBargefeeDao.modify(data);
		}
	}
	
	public void delClient(String id){
		String sql = "delete from price_fcl_bargefee where id ="+id+";delete from price_fcl_bargefeedtl where bargefeeid ="+id;
		priceFclBargefeeDao.executeSQL(sql);
	}

}
