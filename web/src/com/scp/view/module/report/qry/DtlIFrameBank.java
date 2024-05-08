package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameBankBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameBank extends GridView {
	
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

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		Browser.execClientScript("parent.window","argBanks.setValue('');");
		Browser.execClientScript("parent.window","setBankIds('');");
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
		
		
//		String filter = ""
//		+ "\n	AND EXISTS"
//		+ "\n				(SELECT "
//		+ "\n					1 "
//		+ "\n				FROM sys_custlib x , sys_custlib_user y  "
//		+ "\n				WHERE y.custlibid = x.id  "
//		+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
//		+ "\n					AND x.libtype = 'S'  "
//		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = u.id AND z.isdelete = false) " //关联的业务员的单，都能看到
//		+ ")";
//	
		
		//分公司过滤
		//if(AppUtils.getUserSession().getCorpidCurrent() > 0){
			//m.put("corpfilter", filter);
		//}
		
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = u.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){//非saas模式不控制
			m.put("corpfilter", qry);
		}
		
		return m;
		
	}
	
	
}
