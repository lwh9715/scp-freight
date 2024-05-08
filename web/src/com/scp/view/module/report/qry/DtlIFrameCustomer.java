package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameCustomerBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameCustomer extends GridView{
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		Long userid = AppUtils.getUserSession().getUserid();
		String usercode = AppUtils.getUserSession().getUsercode();
		StringBuffer sb = new StringBuffer(); 
		
		
		String filter = ""
			+ "\nAND ((a.iscustomer = false and isdelete = false) OR EXISTS"
			+ "\n				(SELECT "
			+ "\n					1 "
			+ "\n				FROM sys_custlib x , sys_custlib_user y"
			+ "\n				WHERE y.custlibid = x.id  "
			+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
			+ "\n					AND EXISTS(SELECT 1 FROM sys_custlib_cust z where z.custlibid = x.id and z.corpid = a.id)	"
			+ "\n					AND EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.isdelete = FALSE AND xx.customerid = a.id AND xx.saleid = x.userid)" //关联的业务员的单，都能看到
			+ "\n					)" 
			+ "\n	)";
		m.put("filter", filter);
		
		
//		sb.append("\n	AND (");
//		sb.append("\n		EXISTS (SELECT 1 ");
//		sb.append("\n					FROM sys_custlib_cust y,sys_custlib_user z");
//		sb.append("\n					WHERE");
//		sb.append("\n							y.custlibid = z.custlibid");
//		sb.append("\n					AND y.corpid = a.id");
//		sb.append("\n					AND z.userid = "+userid);
//		sb.append("\n							))");
//		m.put("filter", sb.toString());
//		//分公司过滤委托人
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer sbcorp = new StringBuffer();
//			sbcorp.append("\n AND EXISTS (SELECT 1 FROM fina_jobs x WHERE x.isdelete = FALSE AND x.customerid = a.id AND (x.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" OR x.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+"))");
//			m.put("corpfilter", sbcorp.toString());
//		}
		return m;
	}
	
	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argsCustomerJsVar.setValue('');");
		Browser.execClientScript("parent.window","setCustomerIds('');");
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
}
