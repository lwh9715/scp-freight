package com.scp.service.finance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.dao.finance.FinaActpayrecDao;
import com.scp.dao.finance.FinaActpayrecSumDao;
import com.scp.dao.finance.FinaActpayrecdtlDao;
import com.scp.dao.finance.FinaArapDao;
import com.scp.dao.sys.SysCorporationDao;
import com.scp.exception.NoRowException;
import com.scp.exception.RpException;
import com.scp.exception.VchAutoGenerateException;
import com.scp.model.finance.FinaActpayrec;
import com.scp.model.finance.FinaActpayrecSum;
import com.scp.model.finance.FinaActpayrecdtl;
import com.scp.model.finance.FinaArap;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

@Component
@Transactional()
public class ActPayRecService{

	@Resource
	public FinaActpayrecDao finaActpayrecDao; 
	
	@Resource
	public FinaActpayrecSumDao finaActpayrecSumDao;
	
	@Resource
	public FinaActpayrecdtlDao finaActpayrecdtlDao; 
	
	@Resource
	public FinaArapDao finaArapDao; 
	
	@Resource
	public SysCorporationDao sysCorporationDao;
	
	
	@Resource(name="hibTtrTemplate")  
    public TransactionTemplate transactionTemplate;  
	
	/**
	 * @param data
	 * @param modifiedData
	 * @param modifiedDataRpSum 
	 * @return vchid
	 */
	public String save(final FinaActpayrec data, final List<Map> modifiedData, final List<Map> modifiedDataRpSum , final Boolean isAutoGenVch) throws Exception{
		Object id = transactionTemplate.execute(new TransactionCallback() { 
			@Override
	        public String doInTransaction(TransactionStatus transactionStatus) {  
	        	String user;
	    		SysCorporation sysCorporation = sysCorporationDao.findById(data.getClientid());
	    		data.setClientname(sysCorporation.getCode() + "/" + sysCorporation.getAbbr());
	    		
	    		if(0 == data.getId()){
	    			finaActpayrecDao.create(data);
	    			user = data.getInputer();
	    		}else{
	    			finaActpayrecDao.modify(data);
	    			user = data.getUpdater();
	    		}
	    		AppUtils.debug("ActPayRecService save："+modifiedData.size());
	    		try {
	    			for (Map m : modifiedData) {
	    				String dtlid = StrUtils.getMapVal(m, "dtlid");
	    				String dtlid1 = dtlid.split("-")[0];//费用id或收付款明细id
	    				String dtlid2 = dtlid.split("-")[1];//类型：1明细，2费用，3额外费用
	    				String type = StrUtils.getMapVal(m, "actpayrecid");
	    				String _args_arapid = StrUtils.getMapVal(m, "arapid");
	    				String _args_amountwf = StrUtils.getMapVal(m, "amountwf");
	    				String _args_amountrp = StrUtils.getMapVal(m, "amountrpflag");
	    				String _args_xtype = StrUtils.getMapVal(m, "xtype");
	    				String _args_xrate = StrUtils.getMapVal(m, "xrate");
	    				String _args_currencyto = StrUtils.getMapVal(m, "currencyto");
	    				
	    				FinaActpayrecdtl finaActpayrecdtl = new FinaActpayrecdtl();
	    				if(type.equals("0")){//新增，费用未保存为收付款明细状态
	    					finaActpayrecdtl.setArapid(Long.valueOf(_args_arapid));
	    				}else{
	    					finaActpayrecdtl = (FinaActpayrecdtl) finaActpayrecdtlDao.findById(Long.valueOf(dtlid1));
	    				}
	    				finaActpayrecdtl.setActpayrecid(data.getId());
	    				
	    				if(StrUtils.isNull(_args_xrate))_args_xrate = "1";
	    				if(StrUtils.isNull(_args_amountwf))_args_amountwf = "0";
	    				if(StrUtils.isNull(_args_amountrp))_args_amountrp = "0";
	    				
	    				FinaArap finaArap = finaArapDao.findById(Long.valueOf(_args_arapid));
	    				finaActpayrecdtl.setCurrencyfm(finaArap == null?"":finaArap.getCurrency());
	    				finaActpayrecdtl.setCurrencyto(_args_currencyto);
	    				finaActpayrecdtl.setXrate(new BigDecimal(_args_xrate)); 
	    				finaActpayrecdtl.setXtype(_args_xtype); 
	    				finaActpayrecdtl.setAmountwf(new BigDecimal(_args_amountwf));
	    				finaActpayrecdtl.setAmountrp(new BigDecimal(_args_amountrp));
	    				
	    				if("2".equals(dtlid2)){
	    					finaActpayrecdtl.setFeetype("A");
	    				}else{
	    					finaActpayrecdtl.setFeetype("N");
	    				}
	    				AppUtils.debug("ActPayRecService dtlid："+dtlid);
	    				
//	    				finaActpayrecdtl.setRpdate(value);
	    				if(0 == finaActpayrecdtl.getId()){
	    					if(finaActpayrecdtl.getAmountwf() != null && (!finaActpayrecdtl.getAmountwf().equals(BigDecimal.ZERO))){
	    						finaActpayrecdtlDao.create(finaActpayrecdtl);
	    					}
	    				}else{
	    					finaActpayrecdtlDao.modify(finaActpayrecdtl);
	    				}
	    			}
	    		} catch (Exception e) {
	    			transactionStatus.setRollbackOnly();
	    			//e.printStackTrace();
	    			throw new RpException("收付款明细保存错误",e);
	    		}
	    		
	    		try {
	    			//System.out.println("modifiedDataRpSum:"+modifiedDataRpSum);
	    			for (Map m : modifiedDataRpSum) {
	    				String id = StrUtils.getMapVal(m, "id");
	    				String rp = StrUtils.getMapVal(m, "rp");
	    				String cyid = StrUtils.getMapVal(m, "cyid");
	    				
	    				String amt = StrUtils.getMapVal(m, "amt");
	    				String amtother = StrUtils.getMapVal(m, "amtother");
	    				String amtback = StrUtils.getMapVal(m, "amtback");
	    				String amtrest = StrUtils.getMapVal(m, "amtrest");
	    				
	    				String bankid = StrUtils.getMapVal(m, "bankid");
	    				String chequeno = StrUtils.getMapVal(m, "chequeno");
	    				String chequeenddate = StrUtils.getMapVal(m, "chequeenddate");
	    				
	    				FinaActpayrecSum finaActpayrecSum = new FinaActpayrecSum();
	    				if(!StrUtils.isNull(id)){
	    					finaActpayrecSum = (FinaActpayrecSum) finaActpayrecSumDao.findById(Long.valueOf(id));
	    				}
	    				finaActpayrecSum.setActpayrecid(data.getId());
	    				finaActpayrecSum.setRp(rp);
	    				finaActpayrecSum.setCyid(cyid);
	    				
	    				if(!StrUtils.isNull(amt))finaActpayrecSum.setAmt(new BigDecimal(amt));
	    				if(!StrUtils.isNull(amtother))finaActpayrecSum.setAmtother(new BigDecimal(amtother));
	    				if(!StrUtils.isNull(amtback))finaActpayrecSum.setAmtback(new BigDecimal(amtback));
	    				if(!StrUtils.isNull(amtrest))finaActpayrecSum.setAmtrest(new BigDecimal(amtrest));
	    				finaActpayrecSum.setBankid(StrUtils.isNull(bankid)?null:Long.valueOf(bankid));
	    				finaActpayrecSum.setChequeno(chequeno);
	    				//finaActpayrecSum.setChequeenddate(chequeenddate);
	    				if(0 == finaActpayrecSum.getId()){
	    					finaActpayrecSumDao.create(finaActpayrecSum);
	    				}else{
	    					finaActpayrecSumDao.modify(finaActpayrecSum);
	    				}
	    			}
	    		} catch (Exception e) {
	    			transactionStatus.setRollbackOnly();
	    			//e.printStackTrace();
	    			throw new RpException("收付款SUM保存错误",e);
	    		}
//	    		((BaseDaoImpl)finaActpayrecDao).getSessionFactory().getCurrentSession().flush();
	    		String isAutoGenVchconfig = ConfigUtils.findSysCfgVal("fina_actpayrec_bankslip_novch");
	    		//2777 收付款中票据类别为水单状态设置不能生成凭证号  设置为Y时，收付款这里，即时勾了保存时生成凭证，也不调用生成凭证
	    		if(isAutoGenVch){
	    			if("F".equals(data.getBilltype())&&"Y".equals(isAutoGenVchconfig)){
		    			return "";
		    		}else{
		    			return rp2vch(data.getId(),user);
		    		}
	    		}else{
	    			return "";
	    		}
	        }  
	    });  
		return id.toString();
	}
	
	public String rp2vch(long id, String user) throws VchAutoGenerateException {
		String vchid = "";
		try {
			final String sql = "SELECT f_fs_rp2vch('rpid="+id+";usercode="+user+"') AS vchid;";
			SessionFactory sessionFactory = (SessionFactory)AppUtils.getBeanFromSpringIoc("sessionFactory");
			HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
			List list = (List)hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
				throws HibernateException {
					session.flush();
					List list = session.createSQLQuery(sql).list();
					return list;
				}
			});
			vchid = list.get(0).toString();
		} catch (Exception e) {
			throw new VchAutoGenerateException("自动生成凭证错误,请重新保存!",e);
		}
		return vchid;
	}

	public void delRecPay(Long actpayrecid) {
		FinaActpayrec data = (FinaActpayrec) finaActpayrecDao.findById(actpayrecid);
		finaActpayrecDao.remove(data);
	}
	
	public void savedData(FinaActpayrecdtl data) {
		if(0 == data.getId()){
			finaActpayrecdtlDao.create(data);
		}else{
			finaActpayrecdtlDao.modify(data);
		}
	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_actpayrecdtl SET isdelete=TRUE ,amountwf = 0,amountrp = 0  WHERE id in (" + id
				+ ")";
		finaActpayrecdtlDao.executeSQL(sql);
		
	}

	public void choosecus(String[] ids, Long actpayrecid) {
		String sql ="";
		for(int i =0;i<ids.length;i++){
			sql+="INSERT INTO fina_actpayrec_clients VALUES(getid(),"+actpayrecid+","+Long.valueOf(ids[i])+");";
		}
		finaActpayrecdtlDao.executeSQL(sql);
	}

	public void delcus(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_actpayrec_clients SET isdelete=TRUE   WHERE id in (" + id
				+ ")";
		finaActpayrecdtlDao.executeSQL(sql);
	}

	public void lock(long id, boolean islock, String usercode) {
		String sql = "UPDATE fina_actpayrec SET islock="+islock+",locktime = now(),locker = '"+usercode+"'  WHERE id = " + id + ";";
		finaActpayrecdtlDao.executeSQL(sql);
	}
	
	public FinaActpayrec findActPayByRefid(Long refid) throws NoRowException,Exception{
		FinaActpayrec finaActpayrec = null;
		try{
			finaActpayrec = finaActpayrecDao.findOneRowByClauseWhere("refid = "+refid);
			return finaActpayrec;
		}catch(NoRowException e){
			throw e;
		}catch(Exception e){
			throw e;
		}
	}
	
	public FinaActpayrec findActPayByActpayrecid(Long actpayrecid) throws NoRowException,Exception{
		FinaActpayrec finaActpayrec = null;
		try{
			finaActpayrec = finaActpayrecDao.findOneRowByClauseWhere("id = "+actpayrecid);
			return finaActpayrec;
		}catch(NoRowException e){
			throw e;
		}catch(Exception e){
			throw e;
		}
	}
	
}
