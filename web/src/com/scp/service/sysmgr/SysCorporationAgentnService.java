package com.scp.service.sysmgr;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysCorporationAgentDao;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporationAgent;
import com.scp.util.StrUtils;

@Component
public class SysCorporationAgentnService{
	
	
	@Resource
	public SysCorporationAgentDao sysCorporationAgentDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(SysCorporationAgent data) {
		if(0 == data.getId()){
			sysCorporationAgentDao.create(data);
		}else{
			sysCorporationAgentDao.modify(data);
		}
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param usercode
	 */
	public void removeDate(String[] ids,String usercode) {
		String sql = "UPDATE sys_corporation_agent SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		sysCorporationAgentDao.executeSQL(sql);
	}
	
	/*
	 * 检查是否重复
	 * */
	public SysCorporationAgent repeat(Long pkVal , String agentname ,String country){
		try {
			String sql = " UPPER(agentname) LIKE UPPER('"+StrUtils.getSqlFormat(agentname)+"%') and UPPER(country) = UPPER('"+StrUtils.getSqlFormat(country)+"') AND isdelete = false AND id <> "+pkVal;
			List<SysCorporationAgent> list = sysCorporationAgentDao.findAllByClauseWhere(sql);
			if(list != null && list.size() > 0){
				return list.get(0);
			}
		} catch (NoRowException e) {
			return null;
		} 
		return null;
	}
	
}