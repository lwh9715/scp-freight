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

@ManagedBean(name = "pages.module.report.uniteqry.qry_apBean", scope = ManagedBeanScope.REQUEST)
public class Qryap extends GridView {
	
	@SaveState
	public Long userid;
	
	@Bind
	@SaveState
	private long customerid=0;
	
	@Bind
	@SaveState
	private Long feeItemid =null;
	
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
	private boolean settled = false;
	
	@Bind
	@SaveState
	private String paymenttype = "3";
	
	@Bind
	@SaveState
	private String checkid ="";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
			gridLazyLoad = true;
			super.gridPageSize = 500;
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if(customerid>0){
			m.put("customerid", customerid);
		}
		if(feeItemid!=null&&feeItemid>0){
			m.put("feeItemid", feeItemid);
		}
		if(!datetype.equals("")){
			m.put("datetype", datetype);
			m.put("dateastart", (StrUtils.isNull(dateastart) ? "9999-12-31" : dateastart));
			m.put("dateend", (StrUtils.isNull(dateend) ? "9999-12-31" : dateend));
		}
		if(!jobnos.equals("")){
			m.put("jobnos", jobnos);
		}
		if(!vessel.equals("")){
			m.put("vessel", vessel);
		}
		if(!voyage.equals("")){
			m.put("voyage", voyage);
		}
		if(saleid>0){
			m.put("saleid", saleid);
		}
		if(ifcommission.equals("A")){
			m.remove("ifcommission");
		}else if(ifcommission.equals("N")){
			m.put("ifcommission", "N");
		}else if(ifcommission.equals("Y")){
			m.put("ifcommission", "YYY");
		}
		if(!settled){
			m.put("settled", "N");
		}
		if(!paymenttype.equals("3")){
			m.put("paymenttype",paymenttype);
		}
		if(!checkid.equals("")){
			m.put("checkid", checkid);
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
	public void doChangeGridPageSize() {
		super.doChangeGridPageSize();
		String url = AppUtils.chaosUrlArs("./qry_ap.xhtml");
		Browser.execClientScript("window.open('"+url+"','_self');");
	}
	
	@Action
	public void qryRefreshNew(){
		ord = "";
		this.qryRefresh();
	}
}
