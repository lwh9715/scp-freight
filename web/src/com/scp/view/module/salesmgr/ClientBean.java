package com.scp.view.module.salesmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.salesmgr.clientBean", scope = ManagedBeanScope.REQUEST)
public class ClientBean extends GridView{
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			qryMap.put("iscustomer$", true);
		}
	}
	
	@Bind
	@SaveState
	public String qrycode;
	
	@Bind
	@SaveState
	public String datemonth;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		//String filter = AppUtils.custCtrlClauseWhere();
		//map.put("filter", filter);
		
		if(!StrUtils.isNull(qrycode)){
			qrycode = qrycode.trim();
		}
		if(!StrUtils.isNull(qrycode)){
			map.put("filter2", "\nAND (namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' " +
								"OR (EXISTS(SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign y WHERE y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=x.id)" +
								"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%'))"+
								"))");
		}else{
			map.put("filter2", "\nAND 1=2");
		}
		/*if(!StrUtils.isNull(datemonth)){
			map.put("filter3", "\nAND COALESCE((select MAX(b.jobdate) from fina_jobs b where a.id = b.customerid AND b.isdelete = false),'1999-01-01') <= (now()- INTERVAL '"+datemonth+" month')::date");
		}*/
		
		return map;
	}
	
	
	@Override
	public void refresh(){
		if(!StrUtils.isNull(qrycode)){
			String sql = "select count(*) AS count from _sys_corporation a where isdelete = false AND iscarrier = false AND isairline = false AND iscustomer = true AND (namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' " +
						"OR (EXISTS(SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign y WHERE y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=x.id)" +
						"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%'))"+
						"))";
			Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
			Long i = (Long) s.get("count");
			if(i > 10){
				MessageUtils.alert("查到超过10条信息， 请输入精准条件!");
				return;
			}else{
				super.refresh();
			}
		}else{
			super.refresh();
		}
		
	}
	
}
