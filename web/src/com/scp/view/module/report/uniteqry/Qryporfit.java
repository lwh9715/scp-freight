package com.scp.view.module.report.uniteqry;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.uniteqry.qry_porfitBean", scope = ManagedBeanScope.REQUEST)
public class Qryporfit extends GridView {
	
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	private long customerid=0;
	
	@Bind
	@SaveState
	private long feeItemid=0;
	
	@Bind
	@SaveState
	private String datetype = "";

	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private String jobnos = "";
	
	@Bind
	@SaveState
	private String vessel = "";
	
	@Bind
	@SaveState
	private String voyage = "";
	
	@Bind
	@SaveState
	private long saleid=0;
	
	@Bind
	@SaveState
	private String ifcommission = "";
	
	@Bind
	@SaveState
	private boolean settled = true;
	
	@Bind
	@SaveState
	private String impexp = "";
	
	@Bind
	@SaveState
	private String jobtype = "";
	
	@Bind
	@SaveState
	private boolean isclc = false;
	
	@Bind
	@SaveState
	private boolean noclc = false;
	
	@Bind
	@SaveState
	private boolean bigticket = false;
	
	@Bind
	@SaveState
	private String currency = "CNY";
	
	@Bind
	@SaveState
	private String checkid ="";
	
	@Bind
	@SaveState
	private String ourcomp ="";
	
	@Bind
	@SaveState
	private String othercomp ="";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			gridLazyLoad = true;
			super.gridPageSize = 500;
			super.applyGridUserDef();
		}
		this.userid = AppUtils.getUserSession().getUserid();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if(!datetype.equals("")){
			m.put("datetype", datetype);
			m.put("dateastart", (StrUtils.isNull(dateastart) ? "9999-12-31" : dateastart));
			m.put("dateend", (StrUtils.isNull(dateend) ? "9999-12-31" : dateend));
		}
		if(!jobnos.equals("")){
			m.put("jobnos", jobnos);
		}
		if(!impexp.equals("")){
			m.put("impexp", impexp);
		}
		if(!jobtype.equals("")){
			m.put("jobtype", jobtype);
		}
		if(isclc){
			m.put("isclc", "TRUE");
		}
		if(noclc){
			m.put("isclc", "FALSE");
		}
		if(!bigticket){
			m.put("bigticket", "FALSE");
		}
		if(!checkid.equals("")){
			m.put("checkid", checkid);
		}
		m.put("currency", currency);
		
		
		if(!ourcomp.equals("")){
			m.put("ourcomp", ourcomp);
		}
		if(saleid>0){
			m.put("saleid", saleid);
		}
		if(!othercomp.equals("")){
			m.put("othercomp", othercomp);
		}
		if(customerid>0){
			m.put("customerid", customerid);
		}
		
		if(!StrUtils.isNull(ord))m.put("ord", "\nORDER BY " + ord);
		
		return m;
	}
	
	@SaveState
	private String ord = "";
	
	
	@Action
	public void sortGridRemote(){
		//String field = AppUtils.getReqParam("field");
		//String direction = AppUtils.getReqParam("direction");
		String argss = AppUtils.getReqParam("argss");
		//ord = field + " " + direction;
		ord = argss;
		this.grid.reload();
		//System.out.println("argss---->" +argss);
	}
	
	@Bind
	public UIButton appearChecked;
	
	@Action
	public void appearChecked() {
		String[] ids = this.grid.getSelectedIds();
		if (ids.length < 0 || ids == null) {
			MessageUtils.alert("至少勾选一行!");
			return;
		}
		String cid = StrUtils.array2List(ids);
		checkid = cid;
		this.qryRefresh();
	}
	
	@Bind
	public UIButton appearAll;
	
	@Action
	public void appearAll(){
		String a = null;
		checkid = a;
		this.qryRefresh();
	}

	@Override
	public void qryRefresh() {
		super.qryRefresh();
		//ord = "";
	}

	@Override
	public void doChangeGridPageSize() {
		super.doChangeGridPageSize();
		String url = AppUtils.chaosUrlArs("./qry_porfit.xhtml");
		Browser.execClientScript("window.open('"+url+"','_self');");
	}
	
	@Action
	public void qryRefreshNew(){
		ord = "";
		this.qryRefresh();
	}
	
//	@Override
//	public void grid_onrowselect() {
//		String[] ids = this.grid.getSelectedIds();
//		for (int i = 0; i < ids.length; i++) {
//			System.out.println("rowselect:"+ids[i]);
//		} 
//	}
//	
//	@Action
//	public void grid_onrowdeselect() {
//		String[] ids = this.grid.getSelectedIds();
//		for (int i = 0; i < ids.length; i++) {
//			System.out.println("rowdeselect:"+ids[i]);
//		} 
//	}
//	
	
}
