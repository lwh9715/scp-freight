package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameCustserviceBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameCustservice extends GridView {
	
	@Bind
	@SaveState
	public boolean isOperations = false;
	
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
			Browser.execClientScript("parent.window","setCustserviceIds('"+args+"');");
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		if(isOperations){
			Browser.execClientScript("parent.window","argsOperationVar.setValue('');");
			Browser.execClientScript("parent.window","setOperations('');");
		}else{
			Browser.execClientScript("parent.window","argsCustserviceJsVar.setValue('');");
			Browser.execClientScript("parent.window","setCustserviceIds('');");
		}
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
			sql += "\n AND iscs = TRUE";
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
		m.put("filter2", filter2);
		
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = u.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){//非saas模式不控制
			m.put("corpfilter", qry);
		}
		return m;
		
	}
	
	
}
