package com.scp.service.price;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.price.PriceGroupDao;
import com.scp.model.price.PriceGroup;

@Component
public class PriceGroupService{
	
	@Resource
	public PriceGroupDao priceGroupDao;
	
	public void saveData(PriceGroup data) {
		if (0 == data.getId()) {
			priceGroupDao.create(data);
		} else {
			priceGroupDao.modify(data);
		}
	}
	
	public void delete(String ids[]){
		for(String id:ids){
			String sql = "DELETE FROM price_group WHERE id = "+id;
		}
	}
	
	public void addPrice(String libid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO price_group_refprice(id,groupid,priceid) VALUES(getid(),"+libid+","+id+");";
			stringBuilder.append(sql);
		}
		priceGroupDao.executeSQL(stringBuilder.toString());
	}
	
	public void addUser(String libid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO price_group_refuser(id,groupid,userid) VALUES(getid(),"+libid+","+id+");";
			stringBuilder.append(sql);
		}
		priceGroupDao.executeSQL(stringBuilder.toString());
	}
	
	public void removeUser(String libid, String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM price_group_refuser WHERE groupid ="+libid+" AND userid="+id+";";
			stringBuilder.append(sql);
		}
		priceGroupDao.executeSQL(stringBuilder.toString());
	}
	
	public void removePrice(String libid, String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM price_group_refprice WHERE groupid ="+libid+" AND priceid="+id+";";
			stringBuilder.append(sql);
		}
		priceGroupDao.executeSQL(stringBuilder.toString());
	} 
}