package com.scp.view.bpm.apply;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.LMapBase;
import com.scp.base.LMapBase.MLType;
import com.scp.model.bpm.BpmProcessinsVar;
import com.scp.model.bpm.BpmTask;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "bpm.apply.bpmreleasebillcheckBean", scope = ManagedBeanScope.REQUEST)
public class BpmreleasebillcheckBean extends GridFormView{
	
	@SaveState
	public FinaJobs selectedRowData;
	
	@Bind
	@SaveState
	public Date dateapplication = new Date();
	
	@Bind
	@SaveState
	public String applicants ;
	
	@Bind
	@SaveState
	public String hblno ;
	
	@Bind
	@SaveState
	public String customer="" ;
	
	
	@Bind
	@SaveState
	public String remarks;
	
	@Bind
	@SaveState
	public String remarks2;
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Bind
	private UIIFrame jobsApplyIFrame;
	
	@Bind
	private UIIFrame attachmentIframe;
	
	@SaveState
	private Long refid = null;
	
	@SaveState
	private Long taskid = null;

	@Bind
	@SaveState
	public  String user;
	
	@Bind
	@SaveState
	public String amtowe ;
	
	@Bind
	@SaveState
	public String currency ;
	
	@Bind
	@SaveState
	public BigDecimal amout;
	
	
	@Bind
	@SaveState
	public String passTips = "批准：放货";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String jobid = (String)AppUtils.getReqParam("id");
			String taskId = (String)AppUtils.getReqParam("taskid");
				if(!StrUtils.isNull(taskId)){
					this.taskid = Long.parseLong(taskId); 
				}
				if(!StrUtils.isNull(jobid)){
					Long e= Long.parseLong(jobid);
					this.pkVal = e;
					this.refid = e;
					this.user = AppUtils.getUserSession().getUsername();
					update.markUpdate(true, UpdateLevel.Data, "pkVal");
					update.markUpdate(true, UpdateLevel.Data, "user");
					this.refresh();
					Browser.execClientScript("getislast()");
				}
		}
	}
	
	@Override
	public void refresh() {
		try {
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskid));
			List<BpmProcessinsVar> bpmvar = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "isouteroperation");
			this.isouteroperation = bpmvar.get(0).getVal();
			List<BpmProcessinsVar> bpmvar2 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "billtype");
			this.billtype = bpmvar2.get(0).getVal();
			List<BpmProcessinsVar> bpmvar3 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "releasetype");
			this.releasetype = bpmvar3.get(0).getVal();
			List<BpmProcessinsVar> bpmvar4 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "deliverytype");
			this.deliverytype = bpmvar4.get(0).getVal();
			List<BpmProcessinsVar> bpmvar5 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "reason");
			this.reason = bpmvar5.get(0).getVal();
			List<BpmProcessinsVar> bpmvar6 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "applicants");
			this.applicants = bpmvar6.get(0).getVal();
			List<BpmProcessinsVar> bpmvar7 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "remarks2");
			this.remarks2 = bpmvar7.get(0).getVal();
			List<BpmProcessinsVar> bpmvar8 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "islast");
			
			if("false".equals(bpmvar8.get(0).getVal())){
				this.islast = false;
			}else{
				this.islast = true;
			}
			List<BpmProcessinsVar> bpmvar9 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVar(bpmTask.getProcessinstanceid(), "isouter");
			if("false".equals(bpmvar9.get(0).getVal())){
				this.isouter = false;
			}else{
				this.isouter = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findByjobId(this.pkVal);
		SysCorporation customers = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		if(customers != null){
			if(customers.getAmtowe() != null){
				this.amtowe = String.valueOf(customers.getAmtowe());
			}
			if(customers.getCurrency() != null){
				this.currency = String.valueOf(customers.getCurrency());
			}
		}
		
		Long customerid = selectedRowData.getCustomerid();
		
		if(customerid > 0){
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT  (amountcny + amounthkd + amountdhs + amountusd) AS amount");
			sb.append("\nFROM(SELECT");
			sb.append("\nSUM(amountcny1) AS amountcny,SUM(amounthkd1) AS amounthkd,SUM(amountdhs1) AS amountdhs,SUM(amountusd1) AS amountusd");
			sb.append("\nFROM(SELECT");
			sb.append("\nf_amtto(jobdate,'CNY','USD',SUM(T.amountcny)) AS amountcny1");
			sb.append("\n,f_amtto(jobdate,'HKD','USD',SUM(T.amounthkd)) AS amounthkd1");
			sb.append("\n,f_amtto(jobdate,'DHS','USD',SUM(T.amountdhs)) AS amountdhs1");
			sb.append("\n,SUM(T.amountusd) AS amountusd1");
			sb.append("\nFROM ");
			sb.append("\n(SELECT a.nos,a.sales,a.saleid,b.customerid");
			sb.append("\n,(SELECT x.code FROM sys_corporation x where x.id = b.customerid) AS arapcustomer");
			sb.append("\n,(SELECT x.namec FROM sys_corporation x where x.id = b.customerid) AS arapcustomernc");
			sb.append("\n,(SELECT x.daysar FROM sys_corporation x where x.id = b.customerid) AS daysar ,jobdate");
			sb.append("\n,(CASE WHEN b.currency = 'CNY' THEN COALESCE((b.amount-b.amtstl2),0)::NUMERIC(18,2) ELSE 0 END ) AS amountcny");
			sb.append("\n,(CASE WHEN b.currency = 'HKD' THEN COALESCE((b.amount-b.amtstl2),0)::NUMERIC(18,2) ELSE 0 END ) AS amounthkd");
			sb.append("\n,(CASE WHEN b.currency = 'USD' THEN COALESCE((b.amount-b.amtstl2),0)::NUMERIC(18,2) ELSE 0 END ) AS amountusd");
			sb.append("\n,(CASE WHEN b.currency = 'DHS' THEN COALESCE((b.amount-b.amtstl2),0)::NUMERIC(18,2) ELSE 0 END ) AS amountdhs");
			sb.append("\n,(CASE WHEN EXISTS(SELECT 1 FROM sys_user u WHERE isdelete = false AND isadmin = 'N' AND iscsuser = false AND isinvalid = true AND u.code = '"+AppUtils.getUserSession().getUsercode()+"' AND EXISTS");
			sb.append("\n(SELECT 1 FROM sys_userinrole x,sys_role t  WHERE x.isdelete = false AND x.roleid=t.id AND x.userid = u.id AND t.roletype = 'M' AND");
			
			sb.append("\n(t.name = '放货组' OR t.name like '%财务%')  AND t.isdelete = false)) THEN (SELECT x.payremarks FROM sys_corporation x where x.id = b.customerid) ELSE '*******' END) AS payremarks");
			sb.append("\nFROM fina_jobs a , fina_arap b WHERE a.isdelete = FALSE");
			sb.append("\nAND a.isclose = FALSE");
			sb.append("\nAND (b.customerid ="+customerid+" OR (a.customerid = "+customerid+" AND b.customerid != "+customerid+"))");
			sb.append("\nAND b.customerid != a.corpid AND b.customerid != a.corpidop");
			sb.append("\nAND b.isdelete = false and b.araptype='AR'");
			sb.append("\nAND b.jobid = a.id");
			sb.append("\nAND (b.amount-b.amtstl2)>0");
			
			sb.append("\n) AS T");
			sb.append("\nGROUP BY jobdate ,saleid,sales, arapcustomer,customerid,arapcustomernc,daysar,payremarks");
			sb.append("\nORDER BY jobdate ,saleid,sales, arapcustomer");
			sb.append("\n) AS TT");
			sb.append("\n) AS TTT");
			Map<String, BigDecimal> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			//System.out.println(map.get("sumcny"));
			amout = map.get("amount");
			//System.out.println(amout);
		}
		
		String url = AppUtils.getContextPath() + "/pages/ff/apply/jobslink.xhtml?id=" + this.pkVal;
		dtlIFrame.load(url);
		
		url = AppUtils.getContextPath() + "/pages/ff/apply/jobsapply.xhtml?type=apply&id=" + this.pkVal;
		jobsApplyIFrame.load(url);
		
		if (this.refid == null) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			attachmentIframe.load(blankUrl);
		} else {
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.refid);
		}
		
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
    
	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	@Bind
    @SaveState
    public String billtype;
    
    @Bind
    @SaveState
    public String releasetype;
    
    @Bind
    @SaveState
    public String deliverytype;
    
    @Bind
    @SaveState
    public String reason;
    
    @Bind
    @SaveState
    public Boolean islast = false;
    
    @Bind
    @SaveState
    public Boolean isouter;
    
    @Bind
    @SaveState
    public String isouteroperation;
    
    @Bind(id="releasetypeselect")
    public List<SelectItem> getReleasetypeselect() {
    	try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		MLType mlType = AppUtils.getUserSession().getMlType();
    		items.add(new SelectItem("BILL",mlType.equals(LMapBase.MLType.en)?"original":"正本"));
    		items.add(new SelectItem("TEX",mlType.equals(LMapBase.MLType.en)?"Radio amplifier":"电放"));
    		items.add(new SelectItem("AWB","AIRWAYBILL"));
        	if(billtype!=null&&!billtype.equals("HBL")){
        		items.add(new SelectItem("SWB","SWB "));
        	}	
    		return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
    @Action
    public void billtypeselecta(){
    	Object billtyp = AppUtils.getReqParam("billtyp");
    	this.billtype = (String)billtyp;
    	getReleasetypeselect();
    	update.markUpdate(true, UpdateLevel.Data, "releasetype");
    }
	
    
    @Action
	public void recordReport() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/hbllistreport";
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}

	private String getArgs() {
		
		Long customerid = selectedRowData.getCustomerid();
		String args = "";
		args +=  "&customerid=" + customerid;
		return args;
	}
    
}
