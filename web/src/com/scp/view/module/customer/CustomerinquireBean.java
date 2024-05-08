package com.scp.view.module.customer;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.customer.customerinquireBean", scope = ManagedBeanScope.REQUEST)
public class CustomerinquireBean extends GridView{
	
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
		String filter = AppUtils.custCtrlClauseWhere();
		String qry = "\nAND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())filter += qry;//非saas模式不控制
		map.put("filter", filter);
		
		if(!StrUtils.isNull(qrycode)){
			map.put("filter2", "\nAND (namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%'" +
					"\nOR (EXISTS(SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign y WHERE y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=x.id)" +
					"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%'))"+
					"))");
			
		}
		
		if(!StrUtils.isNull(datemonth)){
			map.put("filter3", "\nAND COALESCE((select MAX(b.jobdate) from fina_jobs b where a.id = b.customerid AND b.isdelete = false),'1999-01-01') <= (now()- INTERVAL '"+datemonth+" month')::date");
		}
		return map;
	}
}
