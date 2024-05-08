package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameEmployeeBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameEmployee extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argsEmployeeJsVar.setValue('');");
		Browser.execClientScript("parent.window","setEmployeeIds('');");
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
		String filter = "AND isadmin = 'N' ";
		
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			filter += ""
//				+ "\n	AND corpid ="+AppUtils.getUserSession().getCorpidCurrent();
//		}
//		m.put("corpfilter", filter);
		
		String filter2 = "\nAND 1=1";
		if(81433600 != AppUtils.getUserSession().getUserid()){
			filter2 += "\nAND id<>81433600";
		}
		m.put("filter2", filter2);
		
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE u.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		m.put("corpfilter", corpfilter);
		return m;
	}
}
