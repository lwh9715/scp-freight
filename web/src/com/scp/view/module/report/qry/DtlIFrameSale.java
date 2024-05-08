package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameSaleBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameSale extends GridView {
	
	@Bind
	@SaveState
	public boolean isOperations = false;
	
	@Bind
	@SaveState
	public String code;
	
	@Bind
	@SaveState
	public String qryValues="";//用于存所以的查询值
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String isOperations = AppUtils.getReqParam("isOperation");
 			if(isOperations.equals("true")){
 				this.isOperations = true;
			}
		}
	}
	
	private void refershs() {
		String args = "";
		if (this.grid.getSelectedIds().length > 0
				&& this.grid.getSelectedIds() != null) {
			args = StrUtils.array2List(this.grid.getSelectedIds());
		}
		if(isOperations){
			Browser.execClientScript("parent.window","setOperations('"+args+"');");
		}else{
			Browser.execClientScript("parent.window","setSaleIds('"+args+"');");
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		this.qryValues = "";
		this.code = "";
//		if(isOperations){
//			Browser.execClientScript("parent.window","argsOperationVar.setValue('');");
//			Browser.execClientScript("parent.window","setOperations('');");
//		}else{
//			Browser.execClientScript("parent.window","argsSaleJsVar.setValue('');");
//			Browser.execClientScript("parent.window","setSaleIds('');");
//		}
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String sql = m.get("qry").toString();
		if(!this.isOperations){
			sql += "\n AND issales = TRUE";
		}
		m.put("qry", sql);
		
		String filter = "AND (EXISTS (SELECT 1 FROM sys_user_corplink x WHERE x.corpid = u.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")) ";
		if(!"2204".equals(ConfigUtils.findSysCfgVal("CSNO"))){//开元客户不按分公司过滤业务员
			m.put("filter", filter);
		}
		
		String filter2 = "\nAND 1=1";
		if(81433600 != AppUtils.getUserSession().getUserid()){
			filter2 += "\nAND id<>81433600";
		}
		if(!qryValues.equals("")){
			String qrysql = "";
			String[] split = qryValues.split(",");
			for(String s:split){
				if(qrysql.equals("")){
					qrysql+="\nAND ((code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR corper ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR depter2 ILIKE '%"+StrUtils.getSqlFormat(s)+"%') ";
				}else{
					qrysql+=" OR(code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR corper ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR depter2 ILIKE '%"+StrUtils.getSqlFormat(s)+"%') ";
				}
			}
			qrysql+=")";
			filter2+=qrysql;
		}
		m.put("filter2",filter2);
		
		
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = u.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){//非saas模式不控制
			m.put("corpfilter", qry);
		}
		return m;
		
	}
	
	@Action
	public void qrysAction(){
		if(code!=null&&!code.equals("")){
			qryValues += code+",";
		}
		update.markUpdate(UpdateLevel.Data, "qryValues");
		this.grid.reload();
	}
}
