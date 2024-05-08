package com.scp.service.finance;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaStatementDao;
import com.scp.model.finance.FinaStatement;
import com.scp.util.AppUtils;

@Component
public class StatementMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaStatementDao finaStatementDao; 
	

	public void saveData(FinaStatement data) {
		if(0 == data.getId()){
			finaStatementDao.create(data);
		}else{
			finaStatementDao.modify(data);
		}
	}



	public void removeDate(Long id) {
		String sql = "UPDATE fina_statement SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id;
		finaStatementDao.executeSQL(sql);
	} 
	
	//对账单生成收付款
	public void createRP(Long pkVal , String userCode) {
		String sql = "SELECT f_fina_generate_rp('srctype=statement;id="+pkVal+";user="+userCode+"');";
		finaStatementDao.executeQuery(sql);
	}
	
	
	
}
