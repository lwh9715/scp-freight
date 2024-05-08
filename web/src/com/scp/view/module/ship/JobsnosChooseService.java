package com.scp.view.module.ship;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
/**
 * NEO
 * 海运工作单 弹窗选择工作单 
 * @author Neo
 *
 */
@ManagedBean(name = "jobsnoschooseserviceBean", scope = ManagedBeanScope.SESSION)
public class JobsnosChooseService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	@SaveState
	@Accessible
	private  String popQryKey;
	
	@SaveState
	@Accessible
	private String qrySql = "\nAND 1=1";
	
	
	public void qryNos(String popQryKey){
		qryNos(popQryKey,null);
	}
	public void qryNos(String popQryKey,String qryType) {
		if(StrUtils.isNull(popQryKey)){
			qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey)) this.qrySql = "\nAND 1=1";
		this.popQryKey =this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("\nAND ");
		sbsql.append("\n	(FALSE ");
		if(qryType==null||"nos".equals(qryType)){
			sbsql.append("\n		 OR	nos ILIKE '%" + this.popQryKey + "%' ");
		}
		if(qryType==null||"cntno".equals(qryType)){
			sbsql.append("\n		 OR cntnos ILIKE '%" + this.popQryKey + "%'");
		}
		if(qryType==null||"refno".equals(qryType)){
			sbsql.append("\n		 OR refno ILIKE '%" + this.popQryKey + "%'");
		}
		if(qryType==null||"sono".equals(qryType)){
			sbsql.append("\n		 OR sonos ILIKE '%" + this.popQryKey + "%'");
		}
		if(qryType==null||"orderno".equals(qryType)){
			sbsql.append("\n		 OR orderno ILIKE '%" + this.popQryKey + "%'");
		}
		if(qryType==null||"hblno".equals(qryType)){
			sbsql.append("\n		 OR hblno ILIKE '%" + this.popQryKey + "%' OR EXISTS(SELECT 1 FROM bus_ship_bill b WHERE b.isdelete = false AND b.jobid = t.id AND b.hblno ILIKE '%" + this.popQryKey + "%')");
		}
		if(qryType==null||"mblno".equals(qryType)){
			sbsql.append("\n		 OR mblno ILIKE '%" + this.popQryKey + "%' OR EXISTS(SELECT 1 FROM bus_ship_bill b WHERE b.isdelete = false AND b.jobid = t.id AND b.mblno ILIKE '%" + this.popQryKey + "%')");
		}
		if(qryType==null||"invoiceno".equals(qryType)){
			sbsql.append("\n		 OR invoceno ILIKE '%" + this.popQryKey + "%'");
		}
		if(qryType==null||"mawbnoair".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND mawbno ILIKE '%" + this.popQryKey + "%')");
		}
		if(qryType==null||"sonoair".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND sono ILIKE '%" + this.popQryKey + "%')");
		}
		if(qryType==null||"refnoair".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND refno ILIKE '%" + this.popQryKey + "%')");
		}
		
		if(qryType==null||"sonotrain".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_train WHERE isdelete = FALSE AND jobid = t.id AND sono ILIKE '%" + this.popQryKey + "%')");
		}
		
		//sbsql.append("\n		 OR mainnos ILIKE '%" + this.popQryKey + "%'");
		//sbsql.append("\n		 OR customerabbr ILIKE '%" + this.popQryKey + "%'");
		//sbsql.append("\n		 OR saledesc ILIKE '%" + this.popQryKey + "%'");
		sbsql.append("\n	 )");
		
		qrySql = sbsql.toString();
		
		//System.out.println(qrySql);
	}

	
	public List<Map> findJobsnos(String qryPortKey, String portsql,String qryType){
		qryPortKey = qryPortKey.toUpperCase();
		StringBuilder sbsql = new StringBuilder();
		
		if(qryType==null||"nos".equals(qryType)){
			sbsql.append("\n		 OR	nos ILIKE '%" + qryPortKey + "%' ");
		}
		if("cntno".equals(qryType)){
			sbsql.append("\n		 OR cntnos ILIKE '%" + qryPortKey + "%'");
		}
		if("refno".equals(qryType)){
			sbsql.append("\n		 OR refno ILIKE '%" + qryPortKey + "%'");
		}
		if("sono".equals(qryType)){
			sbsql.append("\n		 OR sonos ILIKE '%" + qryPortKey + "%'");
		}
		if("orderno".equals(qryType)){
			sbsql.append("\n		 OR orderno ILIKE '%" + qryPortKey + "%'");
		}
		if("mblno".equals(qryType)){
			sbsql.append("\n		 OR mblno ILIKE '%" + qryPortKey + "%'  OR EXISTS(SELECT 1 FROM bus_ship_bill b WHERE b.isdelete = false AND b.jobid = t.id AND b.hblno ILIKE '%" + this.popQryKey + "%')");
		}
		if("hblno".equals(qryType)){
			sbsql.append("\n		 OR hblno ILIKE '%" + qryPortKey + "%'  OR EXISTS(SELECT 1 FROM bus_ship_bill b WHERE b.isdelete = false AND b.jobid = t.id AND b.mblno ILIKE '%" + this.popQryKey + "%')");
		}
		if(qryType==null||"invoiceno".equals(qryType)){
			sbsql.append("\n		 OR invoceno ILIKE '%" + this.popQryKey + "%'");
		}
		
		
		if("sonotrain".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_train WHERE isdelete = FALSE AND jobid = t.id AND sono ILIKE '%" + this.popQryKey + "%')");
		}
		
		if("mawbnoair".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND mawbno ILIKE '%" + this.popQryKey + "%')");
		}
		if("sonoair".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND sono ILIKE '%" + this.popQryKey + "%')");
		}
		if("refnoair".equals(qryType)){
			sbsql.append("\n		 OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND refno ILIKE '%" + this.popQryKey + "%')");
		}
//		if(!StrUtils.isNull(qryType)){
//			sbsql.append("\n		 OR mainnos ILIKE '%" + qryPortKey + "%'");
//			sbsql.append("\n		 OR customerabbr ILIKE '%" + qryPortKey + "%'");
//			sbsql.append("\n		 OR saledesc ILIKE '%" + qryPortKey + "%'");
//		}

		if ("AND jobtype = 'L'".equals(portsql)) {
			sbsql.append("\n		 or exists(select 1 from bus_truck bt where bt.isdelete=false and bt.jobid=t.id and bt.sono ILIKE  '%" + qryPortKey + "%' )");
		}

		sbsql.delete(0 , sbsql.indexOf("OR") + 2);
		sbsql.insert(0 , "\nAND\n	(");
		sbsql.append("\n	 )");
		
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM _fina_jobs t " +
			"\nWHERE isdelete = false " +portsql+
			sbsql.toString();
		
		
		
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		querySql += sql;
		
		
		String corpfilter = "";
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			//分公司过滤(忽略迪拜)
//			corpfilter = "AND  (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = t.corpid OR cor.id = t.corpidop)) " +
//					"\nOR t.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR t.saleid = "+AppUtils.getUserSession().getUserid() +
//					")";
//			querySql += corpfilter;
//		}
		corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT t.corpid UNION SELECT t.corpidop UNION SELECT t.corpidop2 UNION SELECT corpid FROM fina_corp WHERE isdelete = FALSE AND jobid = t.id) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql+corpfilter);
		return list;
	}
	
	public List<Map> findJobsnos(String qryPortKey, String portsql) {
		return findJobsnos(qryPortKey, portsql, null);
	}

	public GridDataProvider getJobsnosDataProvider(final String jobnossql) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(jobnossql) || "\nAND 1=1".equals(qrySql))return null;
				//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
				String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
						+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
						+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
									"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_user y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
						+ ")"
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_role y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
						+ ")"
						
						//过滤工作单指派
						+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
						+ AppUtils.getUserSession().getUserid() + ")"
						//要加上空运的，因为此处shipid是shipping表的
						+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s,bus_air bs WHERE bs.jobid = t.id AND s.linktype = 'J' AND s.linkid = bs.id AND s.userid ="
						+ AppUtils.getUserSession().getUserid() + ")"
						+ "\n)";
				String corpfilter = "";
//				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//					//分公司过滤(忽略迪拜)
//					corpfilter = "AND  (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = t.corpid OR cor.id = t.corpidop)) " +
//							"\nOR t.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//							"\nOR t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//							"\nOR t.saleid = "+AppUtils.getUserSession().getUserid() +
//							")";
//				}
				corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
				"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\n ,(SELECT string_agg(DISTINCT y.namec,',') FROM sys_user y , sys_user_assign x WHERE y.isdelete = FALSE AND x.linkid = t.shipid AND x.linktype = 'J' AND x.roletype = 'D' AND x.userid = y.id) AS filenamec" +
					"\nFROM _fina_jobs t " +
					"\nWHERE isdelete = false  " +jobnossql+
					  qrySql
					  +sql// 权限控制 neo 2014-05-30
					  + corpfilter
					  +"\nORDER BY nos"
					  +"\nLIMIT " + this.limit + " OFFSET " + start;
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
					"\nFROM _fina_jobs t " +
					"\nWHERE isdelete = false  "+
					"\nAND id= " +id[0];
				//ApplicationUtils.debug(querySql);
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
				if(StrUtils.isNull(jobnossql) || "\nAND 1=1".equals(qrySql))return 0;
				//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
				String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
						+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
						+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
									"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_user y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
						+ ")"
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_role y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
						+ ")"
						//过滤工作单指派
						+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
						+ AppUtils.getUserSession().getUserid() + ")"
						+ "\n)";
				String corpfilter = "";
//				if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//					//分公司过滤(忽略迪拜)
//					corpfilter = "AND  (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = t.corpid OR cor.id = t.corpidop)) " +
//							"\nOR t.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//							"\nOR t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//							"\nOR t.saleid = "+AppUtils.getUserSession().getUserid() +
//							")";
//				}
				corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
				"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
				
				String countSql = 
					"\nSELECT "
					  + "\nCOUNT(*) AS counts  " 
					  + "\nFROM _fina_jobs t " 
					  + "\nWHERE isdelete = false  " +jobnossql+
					  qrySql
					  +sql// 权限控制 neo 2014-05-30
					  + corpfilter + ";";
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
