package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameDepartmentBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameDepartment extends GridView {

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	
	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argsDepartmentJsVar.setValue('');");
		Browser.execClientScript("parent.window","setDepartmentIds('');");
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
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			m.put("corpfilter", "AND corpid = "+AppUtils.getUserSession().getCorpidCurrent());
//		}
		String qry = m.get("qry").toString();
		qry += "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE d.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		m.put("qry", qry);
		return m;
	}
	
	
}
