package com.scp.service.finance;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaBillDao;
import com.scp.model.finance.FinaBill;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BillMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaBillDao finaBillDao; 
	
	
	@Resource
	public ActPayRecService actPayRecService;
	

	public void saveData(FinaBill data) {
		if(0 == data.getId()){
			finaBillDao.create(data);
		}else{
			finaBillDao.modify(data);
		}
	}



	public void removeDate(Long id) {
		String sql = "UPDATE fina_bill SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW() WHERE id = " + id;
		finaBillDao.executeSQL(sql);
	}



	public void createRP(Long pkVal , String userCode) {
		String sql = "SELECT f_fina_generate_rp('srctype=statement;id="+pkVal+";user="+userCode+"');";
		finaBillDao.executeQuery(sql);
	}



//	public String getCustomerids(Long jobid) {
//		String sql="SELECT string_agg(DISTINCT a.customerid , ',') AS customerids FROM fina_arap a where a.isdelete = false and a.jobid ="+jobid;
//		Map m=daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		return (String) m.get("customerids");
//		
//	}



	/**
	 * 根据jobid过滤得到所有的收据信息
	 * @param jobid
	 * @return
	 */
	public List<FinaBill> getFinaBillListByJobid(Long jobid , Long userid) {
//		String whereSql = "isdelete = false AND jobid = " + jobid + "ORDER BY billno";
		String whereSql = "";
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			whereSql = "isdelete = false AND jobid = " + jobid + " AND corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" ORDER BY billno";
//		}
//		String sql = "SELECT (EXISTS(SELECT 1 FROM _sys_corporation_corplink l,fina_jobs a WHERE a.corpid = l.id AND a.id = " + jobid + ") " +
//					 "\n AND EXISTS (SELECT 1 FROM _sys_corporation_corplink l,fina_jobs a WHERE a.corpidop = l.id AND a.id = " + jobid + ")) AS isco";
//		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		whereSql = " isdelete = false AND jobid = " + jobid ;
//		//2321 账单按分公司过滤显示，处理广东省内不过滤情况
//		if(m!=null&&m.get("isco")!=null&&m.get("isco").toString().equals("true")){
//		}else{
//			whereSql += " AND (EXISTS(SELECT 1 FROM SysUserCorplink x WHERE (x.corpid = thql.corpid) AND x.ischoose = TRUE AND userid ="+userid+"))";
//		}
		whereSql +="\nORDER BY billno ASC";
		return this.finaBillDao.findAllByClauseWhere(whereSql);
	}



	public void removeDate(String[] ids, String usercode) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nUPDATE fina_bill SET isdelete = TRUE , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuilder.append(sql);
		}
		String execSql = stringBuilder.toString();
		if(!StrUtils.isNull(execSql)) {
			this.finaBillDao.executeSQL(execSql);
		}
	} 
	public void removeBillAndDtl(Long pkid){
		//删账单主表
		this.finaBillDao.executeSQL("UPDATE fina_bill SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW() WHERE isdelete = FALSE AND id = "+pkid);
	}
	
}
