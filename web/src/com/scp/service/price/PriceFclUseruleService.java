package com.scp.service.price;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.price.PriceFclUseruleDao;
import com.scp.model.price.PriceFclUserule;

@Component
public class PriceFclUseruleService{
	
	@Resource
	public PriceFclUseruleDao priceFclUseruleDao;
	
	public void saveData(PriceFclUserule data) {
		if (0 == data.getId()) {
			priceFclUseruleDao.create(data);
		} else {
			priceFclUseruleDao.modify(data);
		}
	}
	
	public void delete(String ids[]){
		for(String id:ids){
			String sql = "DELETE FROM price_fcl_userule WHERE id = "+id;
			priceFclUseruleDao.executeSQL(sql);
		}
	}
	
	public void addPrice(String libid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO price_group_refprice(id,groupid,priceid) VALUES(getid(),"+libid+","+id+");";
			stringBuilder.append(sql);
		}
		priceFclUseruleDao.executeSQL(stringBuilder.toString());
	}
	
	public void addUser(String libid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO price_fcl_userule_ref(id,useruleid,customerid) VALUES(getid(),"+libid+","+id+");";
			stringBuilder.append(sql);
		}
		priceFclUseruleDao.executeSQL(stringBuilder.toString());
	}
	
	public void removeCustomer(String libid, String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM price_fcl_userule_ref WHERE useruleid ="+libid+";";
			stringBuilder.append(sql);
		}
		priceFclUseruleDao.executeSQL(stringBuilder.toString());
	}
	
	public void removePrice(String libid, String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM price_group_refprice WHERE groupid ="+libid+" AND priceid="+id+";";
			stringBuilder.append(sql);
		}
		priceFclUseruleDao.executeSQL(stringBuilder.toString());
	} 
}