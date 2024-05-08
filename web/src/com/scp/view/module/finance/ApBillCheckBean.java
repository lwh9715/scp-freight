package com.scp.view.module.finance;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.apbillcheckBean", scope = ManagedBeanScope.REQUEST)
public class ApBillCheckBean extends GridView {
	
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if(!isPostBack){
			super.applyGridUserDef();
			this.gridLazyLoad = true;
		}
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String id = grid.getSelectedIds()[0];
		//AppUtils.openWindow("_newcheckbillEdit", "./checkbilledit.xhtml?type=edit&id="+id+"&customerid=-1");
	}

	
	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
		this.grid.reload();
	}
	
	@SaveState
	@Bind
	public String ispaycheck;
	
	@SaveState
	@Bind
	public String ispayagree;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private String dateastart2;
	
	@Bind
	@SaveState
	private String dateend2;
	
	@SaveState
	@Bind
	public String isprint;
	
	@Bind
	@SaveState
	private String dateastart3;
	
	@Bind
	@SaveState
	private String dateend3;
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer sbcorp = new StringBuffer();
//			sbcorp.append("\n AND t.corpid ="+AppUtils.getUserSession().getCorpidCurrent());
//			m.put("corpfilter", sbcorp.toString());
//		}
		//m.put("corpfilter", corpfilter);
		m.put("userid", AppUtils.getUserSession().getUserid());
		
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE p.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		
		String filter = "";
		if(!StrUtils.isNull(ispaycheck)){
			filter += " AND COALESCE(ispaycheck,FALSE) = " + ispaycheck;
		}
		if(!StrUtils.isNull(ispayagree)){
			filter += " AND COALESCE(ispayagree,FALSE) = " + ispayagree;
		}
		if(!StrUtils.isNull(isprint)){
			filter += " AND COALESCE(isprint,FALSE) = " + isprint;
		}
		String qry = StrUtils.getMapVal(m, "qry");
		qry  += "\nAND jobdate::DATE BETWEEN '"
		+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
		+ "' AND '"
		+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
		+ "'";
		
		if(!StrUtils.isNull(dateastart2) && !StrUtils.isNull(dateend2)){
			qry += "\nAND confirmtime::DATE BETWEEN '" + dateastart2 + "' AND '" + dateend2 + "'";
		}
		
		if(!StrUtils.isNull(dateastart3) && !StrUtils.isNull(dateend3)){
			qry += "\nAND printime::DATE BETWEEN '" + dateastart3 + "' AND '" + dateend3 + "'";
		}
		
		m.put("qry", qry);
		m.put("filter", filter);
		
		return m;
	}
	
	@Action
	public void print() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			serviceContext.sysAttachmentService.print(Long.valueOf(id), AppUtils.getUserSession().getUsername());
		}
		qryRefresh();
	}
	
	@Action
	public void delprint() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			serviceContext.sysAttachmentService.detelePrint(Long.valueOf(id));
		}
		qryRefresh();
	}
	
}
