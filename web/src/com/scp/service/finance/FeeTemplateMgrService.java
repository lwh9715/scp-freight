package com.scp.service.finance;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.finance.FinaArapTempdtlDao;
import com.scp.dao.finance.FinaArapTempletDao;
import com.scp.model.finance.FinaArapTempdtl;
import com.scp.model.finance.FinaArapTemplet;
import com.scp.util.StrUtils;

@Component
public class FeeTemplateMgrService{
	
	
	@Resource
	public FinaArapTempletDao finaArapTempletDao; 
	
	
	@Resource
	public FinaArapTempdtlDao finaArapTempdtlDao;
	

	public void saveData(FinaArapTemplet data) {
		if(0 == data.getId()){
			finaArapTempletDao.create(data);
		}else{
			finaArapTempletDao.modify(data);
		}
	}

	public void removeDate(Long id , String user) {
		String sql = "\nUPDATE fina_arap_templet SET isdelete = TRUE , updater = '"+user+"' , updatetime = NOW() WHERE id = " + id + ";";
		finaArapTempletDao.executeSQL(sql);
	}
	
	public void removeDtlDates(String[] ids , String user) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			stringBuffer.append("\nUPDATE fina_arap_tempdtl SET isdelete = TRUE , updater = '"+user+"' , updatetime = NOW() WHERE id = " + id + ";");
		}
		finaArapTempdtlDao.executeSQL(stringBuffer.toString());
	}
	
	public void addsDates(String[] ids , String user) {
		for (String id : ids) {
			FinaArapTempdtl arapTempdtl = finaArapTempdtlDao.findById(Long.parseLong(id));
			FinaArapTempdtl finaArapTempdtl = new FinaArapTempdtl();
			finaArapTempdtl.setTempletid(arapTempdtl.getTempletid());
			finaArapTempdtl.setAraptype(arapTempdtl.getAraptype());
			finaArapTempdtl.setCustomerid(arapTempdtl.getCustomerid());
			finaArapTempdtl.setCustomercode(arapTempdtl.getCustomercode());
			finaArapTempdtl.setFeeitemid(arapTempdtl.getFeeitemid());
			finaArapTempdtl.setCurrency(arapTempdtl.getCurrency());
			finaArapTempdtl.setAmount(arapTempdtl.getAmount());
			finaArapTempdtl.setPpcc(arapTempdtl.getPpcc());
			finaArapTempdtl.setCalctype(arapTempdtl.getCalctype());
			finaArapTempdtl.setCorpid(arapTempdtl.getCorpid());
			finaArapTempdtl.setInputer(user);
			finaArapTempdtl.setInputtime(new Date());
			finaArapTempdtl.setCustype(arapTempdtl.getCustype());
			finaArapTempdtl.setCntypeid(arapTempdtl.getCntypeid());
			finaArapTempdtl.setTaxrate(arapTempdtl.getTaxrate());
			finaArapTempdtl.setCntnumber(arapTempdtl.getCntnumber());
			finaArapTempdtlDao.create(finaArapTempdtl);
		}
	}

	public void saveDataDtl(FinaArapTempdtl ddata) {
		if(0 == ddata.getId()){
			finaArapTempdtlDao.create(ddata);
		}else{
			finaArapTempdtlDao.modify(ddata);
		}
	}

	public void importFeeFromTemplet(String[] ids, String jobid, String user) {
		importFeeFromTemplet(ids,"jobs",jobid,"-100",user);
	}

	public void importFeeFromTemplet(String[] ids, String type, String jobid,
			String joinid, String user) {
		String joinids[] = joinid.split(",");
		if(StrUtils.isNull(jobid))jobid="-100";
		//以并单为单位，导入多个并单
		if(!StrUtils.isNull(joinid) && joinids.length > 0) {
			for (int i = 0; i < joinids.length; i++) {
				//for (String id : ids) {
				//	String sql = "SELECT f_fina_fee_imptemplate('id="+id+";user="+user+";type="+type+";joinid="+joinids[i]+";jobid="+jobid+"');";
				//	finaArapTempdtlDao.executeQuery(sql);
				//	System.out.println("sql:" + sql + " " + java.util.Calendar.getInstance().getTime().toLocaleString());
				//}
				for(int k = 0; k < ids.length; k++){
					String sql = "SELECT f_fina_fee_imptemplate('id="+ids[k]+";user="+user+";type="+type+";joinid="+joinids[i]+";jobid="+jobid+";counttime="+k+" sec');";
					finaArapTempdtlDao.executeQuery(sql);
					//System.out.println("sql:" + sql + " " + java.util.Calendar.getInstance().getTime().toLocaleString());
				}
			}
		}else {//以工作单为单位导入
			//for (String id : ids) {
			//	String sql = "\nSELECT f_fina_fee_imptemplate('id="+id+";user="+user+";type="+type+";joinid="+joinid+";jobid="+jobid+"');";
			//	finaArapTempdtlDao.executeQuery(sql);
			//}
			for( int i = 0; i < ids.length; i++){
				String sql = "\nSELECT f_fina_fee_imptemplate('id="+ids[i]+";user="+user+";type="+type+";joinid="+joinid+";jobid="+jobid+";counttime="+i+" sec');";
				finaArapTempdtlDao.executeQuery(sql);
			}
		}
	}
	
}