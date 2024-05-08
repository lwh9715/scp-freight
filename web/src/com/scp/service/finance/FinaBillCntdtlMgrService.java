package com.scp.service.finance;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaBillCntdtlDao;
import com.scp.model.finance.FinaBillCntdtl;

@Component
public class FinaBillCntdtlMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaBillCntdtlDao finabillcntdtl; 
	
	public void saveData(FinaBillCntdtl data) {
		if(0 == data.getId()){
			finabillcntdtl.create(data);
		}else{
			finabillcntdtl.modify(data);
		}
	}

	public void removeDate(Long billid) {
		String sql = "DELETE FROM fina_billcntdtl WHERE billid="+billid;
		finabillcntdtl.executeSQL(sql);
	}
	
}
