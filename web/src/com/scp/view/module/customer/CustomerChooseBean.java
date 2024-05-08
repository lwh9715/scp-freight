package com.scp.view.module.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.finance.fs.FsPeriod;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.vo.Period;
import com.scp.vo.Xrate;

/**
 * @author Neo
 *
 */
@ManagedBean(name = "customerchooseBean", scope = ManagedBeanScope.SESSION)
public class CustomerChooseBean {
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	private String popQryKey;
	
	@SaveState
	private String qrySql = "\nAND 1=1";
	
	private String popQryFeeitemKey;
	
	@SaveState
	private String qryFeeitemSql = "\nAND 1=1";
	
	@SaveState
	private String sql = "\nAND 1=1";
	
	@SaveState
	private String sql2 = "\nAND 1=1";
	
	
	
	/**
	 * 按界面输入条件，查询过滤客户列表数据，默认过滤掉被合并客户
	 * @param popQryKey
	 */
	public void qry(String popQryKey) {
		qry(popQryKey , false);
	}
	
	/**
	 * 20150215 neo 重载方法，判断是否要过滤是否被合并的客户，列表显示，使用的时候提示到合并的客户里面操作(对账，付款申请，收付款)
	 * @param popQryKey
	 * @param withJoin true 包含，false排除
	 */
	public void qry(String popQryKey , Boolean withJoin) {
		if(StrUtils.isNull(popQryKey)){
			this.qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey))this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		qrySql = 
			"\nAND " +
			"\n	(	" +
			"\n			code ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR abbr ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namec ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namee ILIKE '%" + this.popQryKey + "%'" +
			"\n		 OR id IN (select customerid from sys_corpext where merchantcode ILIKE '%" + this.popQryKey + "%')" +
			"\n	 )"
			;
		if(!withJoin)qrySql+="\nAND isjoin = FALSE";
	}
	
	/**
	 * 2051 发票页面左边增加搜索条件
	 * @param popQryKey
	 * @param withJoin
	 */
	public void qryInvoice(String popQryKey , Boolean withJoin,String qrytyoe,String qryfindv) {
		if(StrUtils.isNull(popQryKey)&&StrUtils.isNull(qryfindv)){
			this.qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey))this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
//		qrySql = 
//			"\nAND " +
//			"\n	(	FALSE" ;
		if(!StrUtils.isNull(popQryKey)){
			qrySql+="\n	AND ( code ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR abbr ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namec ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namee ILIKE '%" + this.popQryKey + "%' " +
			"\n)";
		}
		//qrySql+="\n	 )";
		if(qrytyoe.equals("A")){//工作单号
			qrySql+="\n		 AND EXISTS(SELECT 1 FROM fina_arap a,fina_jobs b WHERE a.isdelete = FALSE AND b.isdelete = FALSE AND a.jobid = b.id AND a.customerid = s.id AND b.nos ILIKE '%" + qryfindv + "%')" ;
		}else if(qrytyoe.equals("D")){	//SO
			qrySql+="\n		 AND EXISTS(SELECT 1 FROM fina_arap a,bus_shipping b WHERE a.isdelete = FALSE AND b.isdelete = FALSE AND a.jobid = b.jobid AND a.customerid = s.id AND b.sono ILIKE '%" + qryfindv + "%')" ;
		}else if(qrytyoe.equals("B")||qrytyoe.equals("C")){	//提单号
			qrySql+="\n		 AND EXISTS(SELECT 1 FROM fina_arap a,bus_shipping b WHERE a.isdelete = FALSE AND b.isdelete = FALSE AND a.jobid = b.jobid AND a.customerid = s.id AND (b.mblno ILIKE '%" + qryfindv + "%' OR b.hblno ILIKE '%" + qryfindv + "%'))" ;
		}
		if(!withJoin)qrySql+="\nAND isjoin = FALSE";
	}
	
	//1514 收付款左侧列表增加条件过滤（因为qry别处有引用，所以重载  ） 
	public void qryType(String popQryKey , Boolean withJoin ,String qryType) {
		if(StrUtils.isNull(popQryKey)){
			this.qrySql = "\nAND 1=1";
//			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey))this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		qrySql = 
			"\nAND " +
			"\n	(	" +
			"\n			code ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR abbr ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namec ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namee ILIKE '%" + this.popQryKey + "%'" +
			"\n		 OR id IN (select customerid from sys_corpext where merchantcode ILIKE '%" + this.popQryKey + "%')" +
			"\n	 )"
			;
		if(qryType!=null&&qryType.equals("all")){
			
		}else if(qryType!=null&&qryType.equals("notnull")){
			qrySql+="\n AND ((CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END))<>''";
		}else if(qryType!=null&&qryType.equals("rrp")){
			qrySql+="\n AND (((CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END)) = 'R' " +
					"\n OR ((CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END)) = 'RP')";
		}else if(qryType!=null&&qryType.equals("prp")){
			qrySql+="\n AND (((CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END)) = 'P' " +
					"\n OR ((CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END)) = 'RP')";
		}else if(qryType!=null&&qryType.equals("rp")){
			qrySql+="\n AND ((CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END))='RP'";
		}
		if(!withJoin)qrySql+="\nAND isjoin = FALSE";
	}
	
	public void qryFeeitem(String popQryKey) {
		if(StrUtils.isNull(popQryKey)){
			this.qryFeeitemSql = "\nAND 1=1";
			return;
		}
		this.popQryFeeitemKey = popQryKey;
		this.popQryFeeitemKey = this.popQryFeeitemKey.trim();
		
		if(StrUtils.isNull(this.popQryFeeitemKey))this.qryFeeitemSql = "\nAND 1=1";
		this.popQryFeeitemKey = this.popQryFeeitemKey.replaceAll("'", "''");
		this.popQryFeeitemKey = this.popQryFeeitemKey.toUpperCase();
		
		qryFeeitemSql = 
			"\nAND " +
			"\n	(	" +
			"\n			CAST(id AS VARCHAR)='" + this.popQryFeeitemKey + "' " +
			"\n		 OR	code ILIKE '%" + this.popQryFeeitemKey + "%' " +
			"\n		 OR name ILIKE '%" + this.popQryFeeitemKey + "%' " +
			"\n		 OR namee ILIKE '%" + this.popQryFeeitemKey + "%'" +
			"\n	 )";
	}
	
	
	
	
	/**
	 * 根据关键字查找客户
	 * @param qryCustomerKey
	 * @return
	 * @throws Exception
	 */
	public List<Map> findCustomer(String qryCustomerKey) throws Exception{
		qryCustomerKey = qryCustomerKey.toUpperCase();
		String value = ConfigUtils.findSysCfgVal("arap_filter_unchek_customer");
		String filter= "";
		if(value!=null&&value.equals("Y")){
			 filter = "\nAND (ischeck = true)";
		}else{
			filter = "\nAND (1=1)";
		}
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM _sys_corporation a " +
			"\nWHERE isofficial= TRUE " +
			"\nAND " +
			"\n	(	" +
			"\n			code ILIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR abbr ILIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR namec ILIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR namee ILIKE '%" + qryCustomerKey + "%'" +
			"\n	 )" +
			"\nAND isjoin = FALSE" +filter;
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
		return list;
	}
	
	
	/**
	 * 根据关键字查找费用
	 * @param qryFeeitemKey
	 * @return
	 * @throws Exception
	 */
	public List<Map> findFeeitem(String qryFeeitemKey,String iswarehouse) throws Exception{
		qryFeeitemKey = qryFeeitemKey.toUpperCase();
		String querySql = "";
		if(iswarehouse.equals("Y")){
			 querySql = 
				"\nSELECT " +
				"\n* " +
				"\nFROM _dat_feeitem a " +
				"\nWHERE (iswarehouse = TRUE OR ispublic = TRUE )AND isdelete = false" +
				"\nAND " +
				"\n	(	" +
				"\n			CAST(id AS VARCHAR)='" + qryFeeitemKey + "' " +
				"\n		 OR	code ILIKE '%" + qryFeeitemKey + "%' " +
				"\n		 OR name ILIKE '%" + qryFeeitemKey + "%' " +
				"\n		 OR namee ILIKE '%" + qryFeeitemKey + "%'" +
				"\n	 )";
		}else{
			querySql = 
				"\nSELECT " +
				"\n* " +
				"\nFROM _dat_feeitem a " +
				"\nWHERE isdelete = false" +
				"\nAND " +
				"\n	(	" +
				"\n			CAST(id AS VARCHAR)='" + qryFeeitemKey + "'" +
				"\n		OR	code ILIKE '%" + qryFeeitemKey + "%' " +
				"\n		 OR name ILIKE '%" + qryFeeitemKey + "%' " +
				"\n		 OR namee ILIKE '%" + qryFeeitemKey + "%'" +
				"\n	 )";
		}
		
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
		return list;
	}


	/**
	 * 通用弹窗查客户
	 * @return
	 */
	public GridDataProvider getCustomerDataProvider(){
		return getCustomerDataProvider(sql);
	}
	
	/**
	 * 
	 * @param extClauseWhere
	 * @return
	 */
	public GridDataProvider getCustomerDataProvider(final String extClauseWhere){
		return new GridDataProvider() {
			
			String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
			//非saas模式不控制
			
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corporation a " +
					"\nWHERE isdelete = FALSE " + 
						extClauseWhere +
						qrySql +
						(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
						//AppUtils.custCtrlClauseWhere()+//按客户组控制过滤客户 neo 2014-05-21
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object[] getElementsById(String[] id) {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corporation a " +
					"\nWHERE id= " +id[0];
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM _sys_corporation a " +
					"\nWHERE isdelete =false "+extClauseWhere+
					  qrySql +
					  (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					  //AppUtils.custCtrlClauseWhere()+//按客户组控制过滤客户neo 2014-05-21
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
	/**
	 * 通用弹窗查费用
	 * @param extClauseWhere 
	 * @param extClauseWhere 
	 * @return
	 */
	public GridDataProvider getFeeitemDataProvider(final String extClauseWhere){
			return new GridDataProvider() {
				@Override
				public Object[] getElements() {
					String querySql = 
						"\nSELECT " +
						"\n* " +
						"\nFROM _dat_feeitem a " +
						"\nWHERE 1=1 " + 
						extClauseWhere  +
						qryFeeitemSql +
						"\nAND NOT EXISTS(SELECT 1 FROM dat_feeitem_join x WHERE x.idfm = a.id AND x.jointype = 'J') " +
						"\nORDER BY code"+
						"\nLIMIT " + this.limit + " OFFSET " + start;
					try {
						List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
						if(list==null) return null;
						return list.toArray(); 
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				
				@Override
				public Object[] getElementsById(String[] id) {
					String querySql = 
						"\nSELECT " +
						"\n* " +
						"\nFROM _dat_feeitem a " +
						"\nWHERE id= " +id[0];
					try {
						List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
						if(list==null) return null;
						return list.toArray(); 
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public int getTotalCount() {
					String countSql = 
						"\nSELECT " +
						"\nCOUNT(*) AS counts  " +
						"\nFROM _dat_feeitem a " +
						"\nWHERE 1=1 " + 
							extClauseWhere +
							qryFeeitemSql +
							"\nAND NOT EXISTS(SELECT 1 FROM dat_feeitem_join x WHERE x.idfm = a.id AND x.jointype = 'J') " +
						";";
					try {
						Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
						Long count = (Long)m.get("counts");
						return count.intValue();
					} catch (Exception e) {
						e.printStackTrace();
						return 0;
					}
				}
			};
		
	}
	
	
	
	
	
	/**
	 * 待开账单客户
	 * @return
	 */
	public GridDataProvider getBillClientDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String corpfilter = "";
				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
					corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = s.id AND x.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+")";
				}
				String querySql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.billid  IS NULL AND a.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.billid  IS NULL AND a.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,s.* " +
					"\nFROM _sys_corporation s " +
					"\nWHERE  iscustomer = TRUE" +
//					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.billid  IS NULL) " +
					 qrySql +
					 //corpfilter+
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String corpfilter = "";
				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
					corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = s.id AND x.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+")";
				}
				String countSql = 
					"SELECT COUNT(*) AS counts " +
					"FROM _sys_corporation s " +
					"\nWHERE iscustomer = TRUE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.billid  IS NULL) " +
					 qrySql +corpfilter+
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	
	/**
	 * 付款申请客户
	 * @return
	 * @param searchAr
	 */
	public GridDataProvider getRpreqClientDataProvider(final boolean isRet) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {

				//默认应付
				String arsql = "";
				if (isRet && !qrySql.contains("ILIKE")) {
					arsql = " AND a.araptype = 'AP' AND a.isfinish2 = FALSE";
				} else if (!isRet && !qrySql.equals("ILIKE")) {
					arsql = " AND a.araptype = 'AR' AND a.isfinish2 = FALSE";
				}

				String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
				//非saas模式不控制
				String querySql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM _fina_arap a WHERE s.id = a.customerid  AND a.isfinish2 = FALSE AND a.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM _fina_arap a WHERE s.id = a.customerid  AND a.isfinish2 = FALSE AND a.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,(select merchantcode from sys_corpext where customerid = s.id limit 1) AS merchantcode" +
					"\n,s.code,s.isjoin ,s.id ,s.abbr ,s.namec " +
					"\nFROM _sys_corporation s " +
					"\nWHERE  s.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid " + arsql + " AND a.isdelete = FALSE AND amount <> 0) " + qrySql +
					 (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					 ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") +//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					"\nORDER BY araptype asc nulls last,code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {

				//默认应付
				String arsql = "";
				if (isRet && !qrySql.contains("ILIKE")) {
					arsql = " AND a.araptype = 'AP' AND a.isfinish2 = FALSE";
				} else if (!isRet && !qrySql.equals("ILIKE")) {
					arsql = " AND a.araptype = 'AR' AND a.isfinish2 = FALSE";
				}
				String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
				//非saas模式不控制
				String countSql = 
					"SELECT COUNT(*) AS counts " +
					"FROM _sys_corporation s " +
					"\nWHERE s.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid " + arsql + " AND a.isdelete = FALSE AND amount <> 0) " + qrySql +
					 (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					 ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") +//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	/**
	 * 收款申请客户
	 * @return
	 */
	public GridDataProvider getRpreqArClientDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
				//非saas模式不控制
				String querySql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM _fina_arap a WHERE s.id = a.customerid  AND a.isfinish2 = FALSE AND a.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM _fina_arap a WHERE s.id = a.customerid  AND a.isfinish2 = FALSE AND a.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,s.* " +
					"\nFROM _sys_corporation s " +
					"\nWHERE  s.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid AND a.araptype = 'AR' AND a.isdelete = FALSE AND amount <> 0) " +
					 qrySql +
					 (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					 ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") +//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
						
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
				//非saas模式不控制
				String countSql = 
					"SELECT COUNT(*) AS counts " +
					"FROM _sys_corporation s " +
					"\nWHERE s.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid AND a.araptype = 'AR' AND a.isdelete = FALSE AND amount <> 0) " +
					 qrySql +
					 (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					 ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") +//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	
	/**
	 * 对账客户
	 * @return
	 */
	public GridDataProvider getStatementClientDataProvider(final boolean isRet) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {

				//默认应付
				String arsql = "";
				if (isRet && !qrySql.contains("ILIKE")) {
					arsql = " AND x.araptype = 'AP'";
				} else if (!isRet && !qrySql.equals("ILIKE")) {
					arsql = " AND x.araptype = 'AR'";
				}

				//分公司过滤
//				String corpfilter = "";
//				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//					corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+")";
//				}
				String corpfilter = "";
				corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND EXISTS (SELECT 1 FROM sys_user_corplink y WHERE y.corpid = x.corpid AND y.ischoose = TRUE AND y.userid ="+AppUtils.getUserSession().getUserid()+"))";
				
				String querySql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM _fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM _fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,(select merchantcode from sys_corpext where customerid = a.id limit 1) AS merchantcode" +
					"\n,a.code,a.isjoin ,a.id ,a.abbr ,a.namec " +
					"\nFROM _sys_corporation a " +
					"\nWHERE  a.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid " + arsql + " AND x.isdelete = FALSE AND x.isfinish2 = FALSE) " +
					 qrySql + 
					 //corpfilter +
					//	AppUtils.custCtrlClauseWhere()+//按客户组控制过滤客户 neo 2014-05-21
					"\nORDER BY araptype asc nulls last,code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {

				//默认应付
				String arsql = "";
				if (isRet && !qrySql.contains("ILIKE")) {
					arsql = " AND x.araptype = 'AP' AND x.isfinish2 = FALSE";
				} else if (!isRet && !qrySql.equals("ILIKE")) {
					arsql = " AND x.araptype = 'AR' AND x.isfinish2 = FALSE";
				}

				//分公司过滤
//				String corpfilter = "";
//				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//					corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+")";
//				}
				String corpfilter = "";
				corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND EXISTS (SELECT 1 FROM sys_user_corplink y WHERE y.corpid = x.corpid AND y.ischoose = TRUE AND y.userid ="+AppUtils.getUserSession().getUserid()+"))";
				String countSql = 
					"SELECT COUNT(*) AS counts " +
					"FROM _sys_corporation a " +
					"\nWHERE a.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid " + arsql + " AND x.isdelete = FALSE) " +
					 qrySql +
					 //corpfilter +
					 //AppUtils.custCtrlClauseWhere()+//按客户组控制过滤客户 neo 2014-05-21
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	
	

	/**
	 * 对账客户  传入客户
	 * @return
	 */
	public GridDataProvider getStatementClientDataProvider(final String sql) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM _fina_arap a WHERE s.id = a.customerid  AND a.isfinish2 = FALSE AND a.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM _fina_arap a WHERE s.id = a.customerid  AND a.isfinish2 = FALSE AND a.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,s.* " +
					"\nFROM _sys_corporation s " +
					"\nWHERE  s.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid AND a.isdelete = FALSE AND a.isfinish2 = FALSE) " +
					 qrySql +sql+
					 
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String countSql = 
					"SELECT COUNT(*) AS counts " +
					"FROM _sys_corporation s " +
					"\nWHERE s.isdelete = FALSE" +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid AND a.isdelete = FALSE AND a.isfinish2 = FALSE) " +
					 qrySql +sql+
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	
	/**
	 * 待核销客户
	 * @return
	 */
	public GridDataProvider getActPayRecClientDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				//分公司过滤
//				String corpfilter = "";
//				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//					corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND EXISTS (SELECT 1 FROM fina_jobs y WHERE y.isdelete = FALSE AND y.id = x.jobid AND (y.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" OR y.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")))";
//				}
				String corpfilter = "";
				corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid = ANY(SELECT DISTINCT y.corpid FROM sys_user_corplink y WHERE y.ischoose = TRUE AND y.userid ="+AppUtils.getUserSession().getUserid()+")";
				String querySql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,(select merchantcode from sys_corpext where customerid = a.id limit 1) AS merchantcode" +
					"\n,a.code,a.isjoin,a.id,a.abbr,a.namec " +
					"\nFROM _sys_corporation a "+
					"\nWHERE isdelete = false " +
					 sql2 +
					 //corpfilter+
					"\nAND EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isdelete = FALSE) " +
					//"\nAND (EXISTS (SELECT 1 FROM _fina_arap x WHERE a.id = x.customerid AND x.isdelete = FALSE) OR EXISTS(SELECT 1 FROM sys_corporation_join s2 WHERE s2.idto = a.id)) \n"+
					qrySql ;
					String qry = "\nAND (a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
					if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())querySql += qry;//非saas模式不控制
					if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))querySql += qry;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					
					//AppUtils.custCtrlClauseWhere()+//按客户组控制过滤客户 neo 2014-05-21
					querySql+="\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e){
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				//分公司过滤
//				String corpfilter = "";
//				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//					corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND EXISTS (SELECT 1 FROM fina_jobs y WHERE y.isdelete = FALSE AND y.id = x.jobid AND (y.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" OR y.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")))";
//				}
				String corpfilter = "";
				corpfilter += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid = ANY(SELECT DISTINCT y.corpid FROM sys_user_corplink y WHERE y.ischoose = TRUE AND y.userid ="+AppUtils.getUserSession().getUserid()+")";
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts " +
					"\nFROM _sys_corporation a "+
					"\nWHERE  isdelete = false  " +
					 sql2 +
					 //corpfilter +
					 "\nAND EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isdelete = FALSE) " +
					 //"\nAND (EXISTS (SELECT 1 FROM _fina_arap x WHERE a.id = x.customerid AND x.isdelete = FALSE AND x.isfinish2 = FALSE) OR EXISTS(SELECT 1 FROM sys_corporation_join s2 WHERE s2.idto = a.id)) \n"+
					 qrySql ;
					String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
					if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())countSql += qry;//非saas模式不控制
					if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))countSql += qry;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					
					//AppUtils.custCtrlClauseWhere()+//按客户组控制过滤客户 neo 2014-05-21
					countSql+="";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	/**
	 * 高级检索传递customerid
	 */
	public void setQry(String customerid){
		this.sql2 = "\nAND id IN " + customerid;
	}
	/**
	 * 清除sql遗留
	 */
	public void setQrysqlforNull(){
		this.qrySql = "\n AND 1=1";
	}
	
	/**
	 * 清除sql遗留
	 */
	public void setQryforNull(){
		this.sql2 = "\n AND 1=1";
	}

	/**
	 * 待开发票客户
	 * @return
	 */
	public GridDataProvider getInvoiceClientDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"SELECT " +
					"\n(CASE WHEN EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.invoiceid IS NULL AND a.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.invoiceid IS NULL AND a.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype"+
					"\n,s.* " +
					"\nFROM _sys_corporation s " +
					"\nWHERE  isdelete = false " +
					//"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.invoiceid IS NULL AND a.isdelete = FALSE) " +
					 qrySql ;
					String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
					if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())querySql += qry;//非saas模式不控制
					if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))querySql += qry;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					
					querySql+="\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				if(querySql==null||"".equals(querySql)) return null;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String countSql = 
					"SELECT " +
					"COUNT(*) AS counts " +
					"FROM _sys_corporation s " +
					"\nWHERE isdelete = false " +
					"\nAND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid  AND a.invoiceid  IS NULL AND a.isdelete = FALSE) " +
					 qrySql ;
					String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
					if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())countSql += qry;//非saas模式不控制
					if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))countSql += qry;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					
					countSql+="";
				if(countSql==null||"".equals(countSql)) return 0;
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	
	/**
	 * 查所有客户
	 * @return
	 */
	public GridDataProvider getCustomerCtrlDataProvider(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\n,(SELECT COUNT(1) FROM sys_user x , sys_corpctrl y WHERE x.valid = TRUE AND x.isadmin = 'N' AND x.id = y.userid AND y.customerid = a.id)||'/'||(SELECT COUNT(1) FROM sys_user x WHERE x.valid = TRUE AND x.isadmin = 'N') AS ctrldesc"+
					"\nFROM _sys_corporation a " +
					"\nWHERE iscustomer = TRUE " +
					  qrySql +
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM _sys_corporation a " +
					"\nWHERE iscustomer = TRUE"+
					  qrySql +
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}



	public void setQrySql(String qrySql) {
		this.qrySql = qrySql;
	}
	
	
	
	public void qryOher(String popQryKey) {
		if(StrUtils.isNull(popQryKey)){
			this.qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey))this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		qrySql = 
			"\nAND " +
			"\n	(	" +
			"\n			code ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR abbr ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namec ILIKE '%" + this.popQryKey + "%' " +
			"\n		 OR namee ILIKE '%" + this.popQryKey + "%'" +
			"\n	 )";
	}
	
	
	
	/**
	 * 查所有公司
	 * @return
	 */
	public GridDataProvider getCompanyDataProvider(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corporation a " +
					"\nWHERE iscustomer = FALSE AND isdelete =false " +
					  qrySql +
					"\nORDER BY id"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM _sys_corporation a " +
					"\nWHERE iscustomer = FALSE AND isdelete =false"+
					  qrySql +
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
	/**
	 * 查科目
	 * @return
	 */
	public GridDataProvider getActDataProvider(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM fs_act a " +
					"\nWHERE  corpid = 0 AND actsetid = 0 AND isdelete =false " +
					  qrySql +
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM fs_act a " +
					"\nWHERE  corpid = 0 AND actsetid = 0 AND isdelete =false"+
					  qrySql +
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}


	/**
	 * 
	 * @return
	 */
	public GridDataProvider getPeriodDataProvider() {
		
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
					Date date =new Date();
					int year =date.getYear();
					//AppUtils.debug(year);
					Date[] dates =new Date[12];
					
					if(year%4==0){
					//	dates[1]=new Date(,);
					}
				try {
					
					List<Map> list =new ArrayList<Map>();
					for(int i=1;i<13;i++){
						
						Period per =new Period();
						per.setId(i);
						per.setYear((short)2014);
						per.setPeriod((short)i);
						per.setDatefm(new Date());
						per.setDateto(new Date());
						Map m=new HashMap<String, FsPeriod>();
						m.put("id", per.getId());
						m.put("year", per.getYear());
						m.put("period", per.getPeriod());
						m.put("datefm", new Date());
						m.put("dateto", new Date());
						list.add(m);
					}
					
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				return 12;
			}
		};
	}
	
	
	
	
	
	
	//edit_date;
	public GridDataProvider getXrateDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
					
				try {
					
					List<Map> list =new ArrayList<Map>();
					int k=0;
					for(int i=1;i<13;i++){
						
						for(int j=1;j<4;j++){
							Map m=new HashMap<String, Xrate>();
							Xrate fsxrate =new Xrate();
							fsxrate.setId(k++);
							fsxrate.setCurrencyfm("USD");
							fsxrate.setXtype("*");
							fsxrate.setRate(new Double("1"));
							fsxrate.setPeriodid(j);
							m.put("id",fsxrate.getId());
							m.put("period",fsxrate.getPeriodid() );
							m.put("currencyfm", fsxrate.getCurrencyfm());
							m.put("xtype", fsxrate.getXtype());
							m.put("rate", fsxrate.getRate());
							list.add(m);
						}
						
					}
					
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				return 48;
			}
		};
	}
	
	/**
	 * 查所有客户，包括自己公司
	 * @param extClauseWhere
	 * @return
	 */
	public GridDataProvider getAllCustomerDataProvider(final String extClauseWhere){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corporation a " +
					"\nWHERE isdelete = FALSE " + 
						extClauseWhere +
						qrySql +
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					System.out.println("CustomerChooseBean.getAllCustomerDataProvider:"+querySql);
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object[] getElementsById(String[] id) {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corporation a " +
					"\nWHERE id= " +id[0];
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM _sys_corporation a " +
					"\nWHERE isdelete =false "+extClauseWhere+
					  qrySql +
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					System.out.println("CustomerChooseBean.getAllCustomerDataProvider:"+countSql);
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
	/**
	 *报表客户查询 
	 */
	public GridDataProvider getCustomersDataProvider(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corporation a " +
					"\nWHERE isdelete = FALSE " + 
					qrySql +
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM _sys_corporation a " +
					"\nWHERE isdelete =false "+qrySql+
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
}
