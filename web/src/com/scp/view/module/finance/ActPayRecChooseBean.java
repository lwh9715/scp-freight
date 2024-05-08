package com.scp.view.module.finance;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

/**
 * @author Neo
 * 
 */
@ManagedBean(name = "actpayrecChooseBean", scope = ManagedBeanScope.SESSION)
public class ActPayRecChooseBean  {

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	private String popQryKey;

	@SaveState
	private String qrySql = "\n 1=1";

	public void qry(String nos, String actpayrecno, String actpayrecdate,
			String rpdate, Long amount, String chequeno,String actpayrecdateEnd,String rpdateEnd,Long branchComs,String isvch) {
		// if (StrTools.isNull(popQryKey)) {
		// qrySql = "\n 1=1";
		// return;
		// }
		// this.popQryKey = popQryKey;
		// this.popQryKey = this.popQryKey.trim();
		//
		// if (StrTools.isNull(this.popQryKey))
		// this.qrySql = "\nAND 1=1";
		// this.popQryKey = this.popQryKey.replaceAll("'", "''");
		// this.popQryKey = this.popQryKey.toUpperCase();
		String actpayrecdatesql = "";
		if(actpayrecdate==null&&rpdateEnd==null){
			actpayrecdatesql = "";
		}else{
			actpayrecdatesql = "\nAND actpayrecdate::DATE BETWEEN '"
			+ (StrUtils.isNull(actpayrecdate) ? "0001-01-01" : actpayrecdate)
			+ "' AND '"
			+ (StrUtils.isNull(actpayrecdateEnd) ? "9999-12-31" : actpayrecdateEnd)
			+ "'";
		}
		String rpdatesql = "";
		if(rpdate==null&&rpdateEnd==null){
			rpdatesql = "";
		}else{
			rpdatesql = "\nAND rpdate::DATE BETWEEN '"
			+ (StrUtils.isNull(rpdate) ? "0001-01-01" : rpdate)
			+ "' AND '"
			+ (StrUtils.isNull(rpdateEnd) ? "9999-12-31" : rpdateEnd)
			+ "'";
		}
		String isvchsql = "";
		if(isvch==null){
			isvchsql = "";
		}else if(isvch.equals("T")){
			isvchsql = " AND EXISTS(SELECT 1 FROM fs_vch x WHERE x.srcid = actpayrecid)";
		}else if(isvch.equals("F")){
			isvchsql = " AND NOT EXISTS(SELECT 1 FROM fs_vch x WHERE x.srcid = actpayrecid)";
		}
		qrySql = "\n"
				+ (StrUtils.isNull(nos) ? "1=1" : "UPPER(nos) LIKE '%"
						+ nos.trim().toUpperCase() + "%'")
				+ ""
				+ "\nAND "
				+ (StrUtils.isNull(actpayrecno) ? "1=1"
						: "UPPER(actpayrecno) LIKE '%" + actpayrecno.trim().toUpperCase() + "%'")
				+ ""
				
				+actpayrecdatesql
				
				+ "\nAND "
				+ (-1l == amount ? "1=1" : "amount = "
						+ Long.valueOf(amount) + "")
				+ ""
				
				+rpdatesql
				
				+ "\nAND "
				+ (StrUtils.isNull(chequeno) ? "1=1"
						: "UPPER(chequeno) LIKE '%" + chequeno.trim().toUpperCase() + "%'")
				+ "" + "\n"
				+ "\nAND" +(branchComs==null?" 1=1":" corpid ="+branchComs)
				+ "\n "
				+ isvchsql
				+ "\n "
				;
	}

	/**
	 * 查询客户核销单
	 * 
	 * @return
	 */
	public GridDataProvider getActPayRecClientDataProvider() {
		return new GridDataProvider() {
			String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.arapcorpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
			@Override
			public Object[] getElements() {
				if("\n 1=1".equals(qrySql)) return null;
				String querySql = "\nSELECT " + "\ns.* "
						+ "\nFROM _fina_actpayrec_search s " + "\nWHERE  "
						+ qrySql 
						+ (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") 
						+ ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") //分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
						+ "\nAND actpayrecid IS NOT NULL "
						// + "\nLIMIT 50 OFFSET 1 ";
						+ "\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				if (list == null)
					return null;
				return list.toArray();
			}

			@Override
			public int getTotalCount() {
				if("\n 1=1".equals(qrySql)) return 0;
				String countSql = "\nSELECT " + "\nCOUNT(*) AS counts "
						+ "\nFROM _fina_actpayrec_search s " + "\nWHERE  "
						+ qrySql 
						+ (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") 
						+ ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") //分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
						+ "";
				try {
					Map m = daoIbatisTemplate
							.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long) m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
}