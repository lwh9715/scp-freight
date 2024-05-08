package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatPortDao;
import com.scp.model.data.DatPort;
import com.scp.util.StrUtils;

@Component
public class PortyMgrService{
	
	@Resource
	public DatPortDao datPortDao;

	public void saveData(DatPort data) {
		if(0 == data.getId()){
			datPortDao.create(data);
		}else{
			datPortDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatPort data = datPortDao.findById(id);
		datPortDao.remove(data);
	} 
	
	public void editPort(String[] ids,String country,String province,String city,String line,
			boolean isshipbatch,boolean isairbatch,boolean ispolbatch,boolean ispodbatch,boolean ispddbatch,boolean isdestinationbatch,boolean isbargebatch,String userCode) {
		StringBuilder stringBuilder = new StringBuilder();
		String sql = "UPDATE dat_port SET ";
		if(!StrUtils.isNull(country))stringBuilder.append("country  ='"+country+"',");
		if(!StrUtils.isNull(province))stringBuilder.append("province  ='"+province+"',");
		if(!StrUtils.isNull(city))stringBuilder.append("city  ='"+city+"',");
		if(!StrUtils.isNull(line))stringBuilder.append("line  ='"+line+"',");
		stringBuilder.append("isship  ='"+isshipbatch+"',");
		stringBuilder.append("isair  ='"+isairbatch+"',");
		stringBuilder.append("ispol  ='"+ispolbatch+"',");
		stringBuilder.append("ispod  ='"+ispodbatch+"',");
		stringBuilder.append("ispdd  ='"+ispddbatch+"',");
		stringBuilder.append("isdestination  ='"+isdestinationbatch+"',");
		stringBuilder.append("isbarge  ='"+isbargebatch+"',");
		if(stringBuilder.length() >0 ){
			String sqlBody = stringBuilder.toString();
			sqlBody = sqlBody.substring(0, sqlBody.length()-1);
			sql += sqlBody +",updater = '" + userCode + "',updatetime = NOW() WHERE id IN("+StrUtils.array2List(ids)+");";
			this.datPortDao.executeSQL(sql);
		}
	}
	
}
