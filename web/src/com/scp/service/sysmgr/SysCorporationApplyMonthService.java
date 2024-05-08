package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysCorporationApplyMonthDao;
import com.scp.model.sys.SysCorporationApplyMonth;

@Component
public class SysCorporationApplyMonthService {
	
	@Resource
	public SysCorporationApplyMonthDao sysCorporationApplyMonthDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveData(SysCorporationApplyMonth data) {
		if(0 == data.getId()){
			sysCorporationApplyMonthDao.create(data);
		}else{
			sysCorporationApplyMonthDao.modify(data);
		}
	}

	public SysCorporationApplyMonth findbyCustomerid(Long customerid) {
		String sql = " customerid = "+customerid+" AND isdelete = FALSE";
		return this.sysCorporationApplyMonthDao.findOneRowByClauseWhere(sql);
	}

}
