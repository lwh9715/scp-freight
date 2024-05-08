package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameCarrierBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameCarrierBean extends GridView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	 
	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argsCarrierJsVar.setValue('');");
		Browser.execClientScript("parent.window","setCarrierIds('');");
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
//			StringBuffer sbcorp = new StringBuffer();
//			sbcorp.append("\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.carrierid = a.id ");
//			sbcorp.append("\n AND EXISTS (SELECT 1 FROM fina_jobs y WHERE y.isdelete = FALSE AND y.id = x.jobid AND (y.corpid="+AppUtils.getUserSession().getCorpidCurrent()+" OR y.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")))");
//			m.put("corpfilter", sbcorp.toString());
//		}
		
		StringBuffer sbcorp = new StringBuffer();
		sbcorp.append("\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.carrierid = a.id ");
		sbcorp.append("\n AND EXISTS (SELECT 1 FROM fina_jobs y WHERE y.isdelete = FALSE AND y.id = x.jobid AND EXISTS (SELECT 1 FROM sys_user_corplink xx WHERE (y.corpid = xx.corpid OR y.corpidop = xx.corpid) AND xx.ischoose = TRUE AND xx.userid="+AppUtils.getUserSession().getUserid()+")))");
		m.put("corpfilter", sbcorp.toString());
		
		return m;
	}
	
	
	
}
