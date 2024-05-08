package com.scp.view.module.report.qry;

import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameCorpidBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameCorpidBean extends GridView {
	
	@Bind
	@SaveState
	public String type;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			type = AppUtils.getReqParam("type");
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		if(type!=null&&type.equals("corpid")){
			Browser.execClientScript("parent.window","corpidJsvar.setValue('');");
			Browser.execClientScript("parent.window","setcorpids('');");
		}else if(type!=null&&type.equals("corpidop")){
			Browser.execClientScript("parent.window","corpidopJsavr.setValue('');");
			Browser.execClientScript("parent.window","setcorpidops('');");
		}
	}
	
	@Override
	public void refresh() {
		if (grid != null) {
			this.grid.repaint();
			Browser.execClientScript("resize();");
		}
	}
	
	@Resource
	public ApplicationConf applicationConf;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		String qry1 = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.id AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(!applicationConf.isSaas())qry1 = "";//非saas模式不控制
		qry = qry + qry1;
		m.put("qry", qry);
		
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
			m.put("filter", "\nAND id = " + AppUtils.getUserSession().getCorpid());
		}
		
		return m;
	}
	
	
	
}
