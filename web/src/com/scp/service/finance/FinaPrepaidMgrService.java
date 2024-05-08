package com.scp.service.finance;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaPrepaidDao;
import com.scp.dao.finance.FinaPrepaidLinkDao;
import com.scp.dao.finance.FinaPrepaidSumDao;
import com.scp.exception.RpException;
import com.scp.model.finance.FinaPrepaid;
import com.scp.model.finance.FinaPrepaidLink;
import com.scp.model.finance.FinaPrepaidSum;
import com.scp.util.StrUtils;


@Component
public class FinaPrepaidMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaPrepaidDao finaPrepaidDao; 
	
	@Resource
	public FinaPrepaidSumDao finaPrepaidSumDao; 
	
	@Resource
	public FinaPrepaidLinkDao finaPrepaidLinkDao; 
	
	@Resource(name="hibTtrTemplate")  
    public TransactionTemplate transactionTemplate;  
	
	public String save(final FinaPrepaid data, final List<Map> modifiedDataRpSum , final Boolean isAutoGenVch) throws Exception{
		Object id = transactionTemplate.execute(new TransactionCallback() { 
			@Override
	        public String doInTransaction(TransactionStatus transactionStatus) {  
	        	String user;
	    		if(0 == data.getId()){
	    			finaPrepaidDao.create(data);
	    			user = data.getInputer();
	    		}else{
	    			finaPrepaidDao.modify(data);
	    			user = data.getUpdater();
	    		}
	    		try {
	    			for (Map m : modifiedDataRpSum) {
	    				String id = StrUtils.getMapVal(m, "id");
	    				String rp = StrUtils.getMapVal(m, "rp");
	    				String cyid = StrUtils.getMapVal(m, "cyid");
	    				
	    				String amt = StrUtils.getMapVal(m, "amt");
	    				
	    				String bankid = StrUtils.getMapVal(m, "bankid");
	    				String chequeno = StrUtils.getMapVal(m, "chequeno");
	    				String chequeenddate = StrUtils.getMapVal(m, "chequeenddate");
	    				
	    				FinaPrepaidSum finaPrepaidSum = new FinaPrepaidSum();
	    				if(!StrUtils.isNull(id)){
	    					finaPrepaidSum = (FinaPrepaidSum) finaPrepaidSumDao.findById(Long.valueOf(id));
	    				}
	    				finaPrepaidSum.setPrepaid(data.getId());
	    				finaPrepaidSum.setRp(rp);
	    				finaPrepaidSum.setCyid(cyid);
	    				
	    				if(!StrUtils.isNull(amt))finaPrepaidSum.setAmt(new BigDecimal(amt));
	    				finaPrepaidSum.setBankid(StrUtils.isNull(bankid)?null:Long.valueOf(bankid));
	    				finaPrepaidSum.setChequeno(chequeno);
	    				//finaActpayrecSum.setChequeenddate(chequeenddate);
	    				if(0 == finaPrepaidSum.getId()){
	    					finaPrepaidSumDao.create(finaPrepaidSum);
	    				}else{
	    					finaPrepaidSumDao.modify(finaPrepaidSum);
	    				}
	    			}
	    		} catch (Exception e) {
	    			transactionStatus.setRollbackOnly();
	    			throw new RpException("SUM保存错误",e);
	    		}
//	    		((BaseDaoImpl)finaActpayrecDao).getSessionFactory().getCurrentSession().flush();
	    		if(isAutoGenVch){
	    			//return rp2vch(data.getId(),user);
	    			return "";
	    		}else{
	    			return "";
	    		}
	        }  
	    });  
		return id.toString();
	}
	

	public void saveData(FinaPrepaid data) {
		if(0 == data.getId()){
			finaPrepaidDao.create(data);
		}else{
			finaPrepaidDao.modify(data);
		}
	}
	
	public void saveData(FinaPrepaidSum data) {
		if(0 == data.getId()){
			finaPrepaidSumDao.create(data);
		}else{
			finaPrepaidSumDao.modify(data);
		}
	}
	
	public void saveData(FinaPrepaidLink data) {
		if(0 == data.getId()){
			finaPrepaidLinkDao.create(data);
		}else{
			finaPrepaidLinkDao.modify(data);
		}
	}
	
	
	public void removeDate(String id , String user) {
		String sql = "\nUPDATE fina_prepaid SET isdelete = TRUE , updater = '"+user+"' , updatetime = NOW() WHERE id = " + id + ";";
		finaPrepaidDao.executeSQL(sql);
	}


	public String createRP(String id, String userCode) {
		String sql = "SELECT f_fina_prepaid_generate_rp('id="+id+";user="+userCode+"') AS a;";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return StrUtils.getMapVal(m, "a");
	}
	
}
