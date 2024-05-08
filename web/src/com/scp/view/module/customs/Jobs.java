package com.scp.view.module.customs;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.customs.jobsBean", scope = ManagedBeanScope.REQUEST)
public class Jobs extends GridView {
	
	
	
	@Bind
	@SaveState
	public Boolean iscare = false;
	
	@Bind
	@SaveState
	public String setDays;
	
	@Bind
	@SaveState
	public String customname;
	
	@Bind
	@SaveState
	private String dates;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private boolean isdate=false;
	
	@Bind
	@SaveState
	private boolean isjob=false;
	@Bind
	@SaveState
	private boolean isbook=false;
	@Bind
	@SaveState
	private boolean iscustoms=false;
	@Bind
	@SaveState
	private boolean isreceipt=false;
	@Bind
	@SaveState
	private boolean isinvoice=false;
	@Bind
	@SaveState
	private boolean isno=false;
	
	@Bind
	@SaveState
	private String numbers;
	
	@Bind
	@SaveState
	private String allcustomer="";
	
	@Bind
	@SaveState
	private long customerid=0;
	
	@Bind
	@SaveState
	private long sale=0 ;
	
	@Bind
	@SaveState
	private String  saleid="" ;
	
	@Bind
	@SaveState
	private String corpid = "" ;
	@Bind
	@SaveState
	private String corpidop = "" ;
	
	@Bind
	@SaveState
	private String cusclass="";
	
	@Bind
	@SaveState
	private String status="";
	
	@Bind
	@SaveState
	public String cntnos;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			initData();
			super.applyGridUserDef();
		}
	}
	
	@Action
	public void add() {
		String winId = "_edit_jobsland";
		String url = "./jobsedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_jobsland";
		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if (!iscare) {
			m.put("icare", "1=1");
		} else {
			String sql="(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+AppUtils.getUserSession().getUserid()+")";
			sql+="OR t.inputer ='"+AppUtils.getUserSession().getUsercode()+"' OR t.updater = '"+AppUtils.getUserSession().getUsercode()
				+"' OR COALESCE(t.saleid,0) <= 0)";
			m.put("icare",sql);
			
		}
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="
			+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0)";
		m.put("corpfilter", corpfilter);
		if("0".equals(this.setDays)){
			String setDays = "\n AND 1=1";
			m.put("setDays", setDays);
		}else{
			String setDays = "\n AND t.jobdate::DATE >= NOW()::DATE-"+(StrUtils.isNull(this.setDays)?"365":this.setDays);
			m.put("setDays", setDays);
		}
		
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}
		
		String qry = m.get("qry").toString();
		if(!StrUtils.isNull(customname)){
			qry += "\n AND EXISTS(SELECT 1 FROM sys_corporation x " +
				   "WHERE (code ILIKE '%"+customname+"%' OR namee ILIKE '%"+customname+"%' OR namec ILIKE '%"+customname+"%') " +
				   "AND EXISTS(SELECT 1 FROM bus_customs WHERE customid = x.id AND jobid = t.id))";
		}
		
		if(!StrUtils.isNull(cntnos)){
			qry += "\n AND EXISTS(SELECT 1 FROM bus_ship_container c WHERE c.jobid = t.id AND c.isdelete = FALSE AND c.parentid is null and cntno ILIKE '%"+cntnos+"%')";
		}
		
		//高级检索查询拼接语句
		if(isdate){	
			qry += "\nAND "+dates+"::DATE BETWEEN '"
				+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
				+ "' AND '"
				+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
				+ "'";
		}
		if(isjob||isbook||iscustoms||isreceipt||isinvoice||isno){
			qry += "\nAND (FALSE ";
			if(isjob){
				qry +="\nOR nos ILIKE '%"+numbers+"%'";			
			}
			if(isbook){
				qry +="\nOR EXISTS (SELECT 1 FROM bus_customs bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.sono ILIKE '%"+numbers+"%')";
			}
			if(iscustoms){
				qry +="\nOR EXISTS(SELECT 1 FROM bus_customs bc WHERE isdelete = FALSE AND bc.jobid=t.id AND bc.nos ILIKE '%"+numbers+"%')";	
			}
			if(isreceipt){
				qry +="\nOR EXISTS(SELECT 1 FROM fina_bill fb WHERE isdelete = FALSE AND fb.jobid=t.id AND fb.billno ILIKE '%"+numbers+"%')";	
			}
			if(isinvoice){
				qry +="\n OR EXISTS(SELECT 1 FROM  fina_invoice x , fina_arap y WHERE y.jobid = t.id " +
				"AND x.isdelete = FALSE AND y.isdelete = FALSE AND y.invoiceid = x.id AND x.invoiceno ILIKE '%"+numbers+"%')";
			}
			if(isno){
				String numbersplit = numbers.replaceAll(",","%,%");
				qry +="\nOR " +
						"EXISTS(SELECT 1 FROM bus_ship_container bs WHERE isdelete = FALSE AND bs.jobid=t.id " +
						"AND bs.cntno ILIKE ANY(regexp_split_to_array('%"+numbersplit+"%',',')))";	
			}
			qry += ")";
		}
		//客户
		if(customerid!=0&&allcustomer.equals("customerid")){
			qry +="\nAND "+allcustomer+" ='"+customerid+"'";
		}
		if(customerid!=0&&allcustomer.equals("clientid")){
			qry +="\nAND EXISTS (SELECT 1 FROM fina_arap fa WHERE  isdelete = FALSE AND fa.jobid=t.id AND fa.customerid="+customerid+")";
		}
		if(customerid!=0&&allcustomer.equals("operationid")){
			qry +="\nAND EXISTS(SELECT 1 FROM bus_customs WHERE isdelete = FALSE AND jobid = t.id AND "+allcustomer+" ='"+customerid+"' )";
		}
		//业务员
		if(sale!=0){
			qry +="\nAND t.saleid = '"+sale+"'";
		}
		//录入人
		if(saleid!=""){
			qry +="\nAND t.inputer = '"+saleid+"'";
		}
		//接单公司
		if(corpid!=""){
			qry +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=t.corpid AND sc.id = "+corpid+")";		
		}
		//操作公司
		if(corpidop!=""){
			qry +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=t.corpidop AND sc.id = "+corpidop+")";		
		}
		
		//报关方式
		if(!StrUtils.isNull(cusclass)){
			qry +="\nAND EXISTS (SELECT 1 FROM bus_customs WHERE jobid = t.id AND isdelete = FALSE AND cusclass= '"+cusclass+"')";		
		}
		//报关方式
		if(!StrUtils.isNull(status)){
			qry +="\nAND EXISTS (SELECT 1 FROM bus_customs WHERE jobid = t.id AND isdelete = FALSE AND status= '"+status+"')";		
		}
		
		
		
		m.put("qry",qry);
		
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		try {
			ConfigUtils.refreshUserCfg("pages.module.customs.jobsBean.search", 
					StrUtils.getSqlFormat(qry)
					, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return m;
	}
	
	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			serviceContext.jobsMgrService.removeDate(this.getGridSelectId(),
					AppUtils.getUserSession().getUsercode());
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	protected void initData(){
		String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_land_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="365";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("jobs_land_date",this.setDays, AppUtils.getUserSession().getUserid());
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIWindow unloaDaysWindows;
	
	@Action
	public void cancel(){
		unloaDaysWindows.close();
	}
	
	
	@Bind
	@SaveState
	public String sortlabel_1;
	
	@Bind
	@SaveState
	public String sortvalue_1;
	
	@Bind
	@SaveState
	public String sortlabel_2;
	
	@Bind
	@SaveState
	public String sortvalue_2;
	
	@Bind
	@SaveState
	public String sortlabel_3;
	
	@Bind
	@SaveState
	public String sortvalue_3;
	
	@Bind
	public UIWindow sortWindow;
	
	@Action
	public void showSortWindow(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		sortlabel_2 = null;
		sortvalue_2 = "asc";
		sortlabel_3 = null;
		sortvalue_3 = "asc";
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(!StrUtils.isNull(ordersql)){
			String[] sorts = ordersql.split(",");
			if(sorts != null){
				for (int i = 0; i < sorts.length; i++) {
						switch (i+1) {
						case 1:
							sortlabel_1 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_1 = "desc";
							}else{
								sortvalue_1 = "asc";
							}
							break;
						case 2:
							sortlabel_2 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_2 = "desc";
							}else{
								sortvalue_2 = "asc";
							}
							break;
						case 3:
							sortlabel_3 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_3 = "desc";
							}else{
								sortvalue_3 = "asc";
							}
							break;
						}
				}
			}
		}
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		sortWindow.show();
	}
	
	@Action
	public void resetUserColorder(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		sortlabel_2 = null;
		sortvalue_2 = "asc";
		sortlabel_3 = null;
		sortvalue_3 = "asc";
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", null);
		MessageUtils.alert("OK!");
	}
	
	@Action
	public void saveUserColorder(){
		String colorder = "";
		if(!StrUtils.isNull(sortlabel_1)&&!StrUtils.isNull(sortvalue_1)){
			colorder += sortlabel_1 + " " + sortvalue_1 + ",";
		}
		if(!StrUtils.isNull(sortlabel_2)&&!StrUtils.isNull(sortvalue_2)){
			colorder += sortlabel_2 + " " + sortvalue_2 + ",";
		}
		if(!StrUtils.isNull(sortlabel_3)&&!StrUtils.isNull(sortvalue_3)){
			colorder += sortlabel_3 + " " + sortvalue_3 + ",";
		}
		colorder = colorder.endsWith(",") ? colorder.substring(0, colorder.length()-1) : colorder;
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", colorder);
		MessageUtils.alert("OK!");
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	@Bind
	public UIIFrame traceIframe;
	
	@Action
	public void showTaskCheckInfo(){
		String jobid = AppUtils.getReqParam("jobid");
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+jobid+"' LIMIT 1";
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
		}
		taskCommentsIframe.load("../../../bpm/bpmshowcomments.jsp?processinstanceid="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		traceIframe.load("../../../bpm/model/trace.html?language="+AppUtils.getUserSession().getMlType()
				+"&id="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show();traceWindow.show()");
	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
	}
	
	@Action
	public void clear() {
		this.clearQryKey();
	}
	
	@Action
	public void clearAndClose() {
		this.clearQryKey();
		Browser.execClientScript("searchWindowJsVar.hide()");
	}
	
	@Override
	public void clearQryKey() {
		isdate=false;
		dates="";
		dateastart="";
		dateend="";
		isjob=false;
		isbook=false;
		iscustoms=false;
		isreceipt=false;
		isinvoice=false;
		isno=false;
		numbers="";
		allcustomer="";
		customerid=0;
		sale=0;
		saleid="";
		corpid="";
		corpidop="";
		cusclass="";
		status="";
		Browser.execClientScript("$('#sales_input').val('')");
		Browser.execClientScript("$('#inputers_input').val('')");
		Browser.execClientScript("$('#usergetmblflex_input').val('')");
		Browser.execClientScript("$('#userreleasemblflex_input').val('')");
		Browser.execClientScript("$('#fils_input').val('')");
		Browser.execClientScript("$('#operation_input').val('')");
		Browser.execClientScript("$('#call_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		Browser.execClientScript("$('#port_input').val('')");
		Browser.execClientScript("$('#market_input').val('')");
		Browser.execClientScript("$('#finance_input').val('')");
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		this.update.markUpdate(UpdateLevel.Data, "startDateATA");
		this.update.markUpdate(UpdateLevel.Data, "endDateATA");
		this.update.markUpdate(UpdateLevel.Data, "startDateETD");
		this.update.markUpdate(UpdateLevel.Data, "endDateETD");
		this.update.markUpdate(UpdateLevel.Data, "startDateATD");
		this.update.markUpdate(UpdateLevel.Data, "endDateATD");
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}
}
