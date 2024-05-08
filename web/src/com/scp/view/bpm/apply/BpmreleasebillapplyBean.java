package com.scp.view.bpm.apply;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmProcessinstance;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
@ManagedBean(name = "bpm.apply.bpmreleasebillapplyBean", scope = ManagedBeanScope.REQUEST)
public class BpmreleasebillapplyBean extends FormView{
	

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
	public String remarks;
	
	@Bind
	@SaveState
	public String remarks2;
	
	@Bind
	@SaveState
	public String jobids="" ;
	
	@Bind
	@SaveState
	public String type="" ;
	
	@Bind
	@SaveState
	public Long customerid=0l ;
	
	
	@Bind
	@SaveState
	public Long salesid=0l ;
	
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Bind
	private UIIFrame jobsApplyIFrame;
	
	@Bind
	private UIIFrame jobsNotputerIFrame;
	
	@Bind
	@SaveState
	private String processId = "ReleaseBillProcess";
	
	@Bind
	private UIIFrame attachmentIframe;

	@Resource(name="transactionTemplate")
	private TransactionTemplate transactionTemplate;
	
	@Bind
	@SaveState
	public String taskId;
	
	@Bind
	@SaveState
	public String daysar ;
	
	@Bind
	@SaveState
	public String amtowe ;
	
	@Bind
	@SaveState
	public String currency ;
	
	@Bind
	@SaveState
	public BigDecimal amout;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String jobids = (String)AppUtils.getReqParam("jobid");
			type = (String)AppUtils.getReqParam("type");
			taskId = AppUtils.getReqParam("taskid");
			if(!StrUtils.isNull(jobids)){
				this.jobids = jobids;
				update.markUpdate(true, UpdateLevel.Data, "jobids");
				this.refresh();
			}
			if(!StrUtils.isNull(taskId)){
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
				BpmProcessinstance bpmpross = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
				this.jobids = bpmpross.getRefid();
				actionJsText = "";
				List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmTask.getProcessid()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'js'");
				for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
					actionJsText+=bpmWorkItem.getActions();
				}
				update.markUpdate(true, UpdateLevel.Data, "actionJsText");
				this.refresh();
			}else{
				this.applicants = AppUtils.getUserSession().getUsername();
			}
		}
	}
	
	@Override
	public void refresh() {
		
		String url = AppUtils.getContextPath() + "/pages/ff/apply/jobslink.xhtml?type=apply&id=" + jobids;
		dtlIFrame.load(url);
		url = AppUtils.getContextPath() + "/pages/ff/apply/jobsapply.xhtml?type=apply&id=" + jobids;
		jobsApplyIFrame.load(url);
		url = AppUtils.getContextPath() + "/pages/ff/apply/jobsnotputer.xhtml?type=apply&id=" + jobids;
		jobsNotputerIFrame.load(url);
		String jobid = jobids.split(",")[0];
		selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findByjobId(Long.valueOf(jobid));
		SysCorporation sysCor = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCorpid());
		SysCorporation sysCorop = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCorpidop());
		SysCorporation customer = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		if(customer != null){
			if(customer.getDaysar() != null){
				this.daysar = String.valueOf(customer.getDaysar());
			}
			if(customer.getAmtowe() != null){
				this.amtowe = String.valueOf(customer.getAmtowe());
			}
			if(customer.getCurrency() != null){
				this.currency = String.valueOf(customer.getCurrency());
			}
		}
//		if(sysCor!=null&&(sysCor.getAbbcode().indexOf("SZ"))<0){//工作单的接单公司，非深圳的勾委托公司
//			this.isouter = true;
//		}
//		//操作地：如果操作公司和接单公司一样，选Local操作。如果操作公司不一样，操作公司是深圳的，选外办操作(深圳)。否则选第二个
//		if(selectedRowData.getCorpid().toString().equals(selectedRowData.getCorpidop().toString())){
//			this.isouteroperation="Local操作";
//		}else if(this.selectedRowData.getCorpid()!=this.selectedRowData.getCorpidop()&&(sysCorop.getAbbcode().indexOf("SZ"))>-1){
//			this.isouteroperation="外办操作(深圳)";
//		}else{
//			this.isouteroperation="外办操作(非深圳)";
//		}
		customerid = selectedRowData.getCustomerid();
		
		if(customerid > 0){
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT  (CASE WHEN (amountcny + amounthkd + amountdhs + amountusd-amtstl2) :: NUMERIC(18,2) > 0 THEN (amountcny + amounthkd + amountdhs + amountusd-amtstl2) :: NUMERIC(18,2) ELSE 0 END) AS amount");
			sb.append("\nFROM(SELECT");
			sb.append("\nSUM(amountcny1) AS amountcny,SUM(amounthkd1) AS amounthkd,SUM(amountdhs1) AS amountdhs,SUM(amountusd1) AS amountusd,SUM(amtstl2) as amtstl2");
			sb.append("\nFROM(SELECT");
			sb.append("\nf_amtto(jobdate,'CNY','USD',SUM(T.amountcny)) AS amountcny1");
			sb.append("\n,f_amtto(jobdate,'HKD','USD',SUM(T.amounthkd)) AS amounthkd1");
			sb.append("\n,f_amtto(jobdate,'DHS','USD',SUM(T.amountdhs)) AS amountdhs1");
			sb.append("\n,SUM(T.amountusd) AS amountusd1");
			sb.append("\n,SUM(T.amtstl2) AS amtstl2");
			sb.append("\nFROM ");
			sb.append("\n(SELECT a.nos,a.sales,a.saleid,b.customerid");
			sb.append("\n,(SELECT x.code FROM sys_corporation x where x.id = b.customerid) AS arapcustomer");
			sb.append("\n,(SELECT x.namec FROM sys_corporation x where x.id = b.customerid) AS arapcustomernc");
			sb.append("\n,(SELECT x.daysar FROM sys_corporation x where x.id = b.customerid) AS daysar ,jobdate");
			sb.append("\n,(CASE WHEN b.currency = 'CNY' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountcny");
			sb.append("\n,(CASE WHEN b.currency = 'HKD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amounthkd");
			sb.append("\n,(CASE WHEN b.currency = 'USD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountusd");
			sb.append("\n,(CASE WHEN b.currency = 'DHS' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountdhs");
			sb.append("\n,COALESCE((SELECT f_amtto(jobdate,b.currency,'USD',SUM(COALESCE(amountrp,0)))::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01') GROUP BY d.arapid):: INT,0) AS amtstl2");
			sb.append("\n,(CASE WHEN EXISTS(SELECT 1 FROM sys_user u WHERE isdelete = false AND isadmin = 'N' AND iscsuser = false AND isinvalid = true AND u.code = '"+AppUtils.getUserSession().getUsercode()+"' AND EXISTS");
			sb.append("\n(SELECT 1 FROM sys_userinrole x,sys_role t  WHERE x.isdelete = false AND x.roleid=t.id AND x.userid = u.id AND t.roletype = 'M' AND");
			
			sb.append("\n(t.name = '放货组' OR t.name like '%财务%')  AND t.isdelete = false)) THEN (SELECT x.payremarks FROM sys_corporation x where x.id = b.customerid) ELSE '*******' END) AS payremarks");
			sb.append("\nFROM fina_jobs a , fina_arap b");
			sb.append("\n WHERE a.isdelete = FALSE");
			sb.append("\nAND a.isclose = FALSE");
			sb.append("\nAND (b.customerid ="+customerid+" OR (a.customerid = "+customerid+" AND b.customerid != "+customerid+"))");
			sb.append("\nAND a.saleid = ANY(SELECT distinct saleid FROM fina_jobs WHERE id in ("+jobid+") )");
			sb.append("\nAND b.customerid != a.corpid AND b.customerid != a.corpidop");
			sb.append("\nAND b.isdelete = false and b.araptype='AR'");
			sb.append("\nAND b.jobid = a.id");
			
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
	
		
		salesid = selectedRowData.getSaleid();
		//neo 20170922 附件特殊处理，取当前工作单id+“100”，下面开启流程后，再校正为流程表里面的refid
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid=" + jobid+"100");
		if(!StrUtils.isNull(taskId)){
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			isopothre = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "isopothre");
			billtype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "billtype");
			releasetype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "releasetype");
			deliverytype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "deliverytype");
			reason = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "reason");
			applicants = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "applicants");
//			remarks = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "remarks");
			remarks2 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "remarks2");
			islast = "true".equals(serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "islast"))?true:false;
//			isouter = "true".equals(serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "isouter"))?true:false;
		}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Action
	public void applyBPMform() {
		try {
			//if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-89120814";
				BpmProcess bpmProcess = null;
				String processName = "放货流程";
				if(!StrUtils.isNull(type)&&"gsit".equals(type)){
					processName = "放货流程_GSIT";
				}
				bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+jobids+"' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str =  s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
				}
				Browser.execClientScript("bpmWindowJsVar.show();");
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			BpmProcess bpmProcess = null;
			String processName = "放货流程";
			if(!StrUtils.isNull(type)&&"gsit".equals(type)){
				processName = "放货流程_GSIT";
			}
			bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
				lists.add(selectItem);
			}
		}catch(NoRowException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
	@Bind
	@SaveState
	public String taskname;
	
	
	
	@Action
	public void saveRemarks() {
		if(!StrUtils.isNull(taskId)){
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmpross = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			serviceContext.bpmProcessinsVarService.save(bpmpross.getId(), "remarks2", remarks2,"备注2");
		}
	}
	
	@Action
	public void applyBPM() {
		if(StrUtils.isNull(this.jobids)){
			MessageUtils.alert("Please save first!");
			return;
		}
		if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm")) || "8888".equals(ConfigUtils.findSysCfgVal("CSNO")) || AppUtils.isDebug){
			BpmProcess bpmProcess = null;
			try{
				String processName = "放货流程";
				if(!StrUtils.isNull(type)&&"gsit".equals(type)){
					processName = "放货流程_GSIT";
				}
				bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
				
				String processinsVar = "deliverytype"+"-"+deliverytype+"-"+"类型";
				//processinsVar += "#isopothre"+"-"+isouteroperation+"-"+"操作地";
				processinsVar += "#islast"+"-"+islast+"-"+"最后一票";
				processinsVar += "#isopothre"+"-"+isopothre+"-"+"外办";
				String noss = "";
				try{
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT string_agg(COALESCE(nos,''),',') AS noss FROM fina_jobs WHERE isdelete = FALSE AND id = ANY(regexp_split_to_array('"+jobids+"',',')::BIGINT[])");
					if(m!=null&&m.get("noss")!=null){
						noss = m.get("noss").toString();
					}
				}catch (Exception e){
					noss = this.selectedRowData.getNos();
				}
				String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+bpmremark+";taskname="+taskname+";refno="+noss+";refid="+jobids+";processinsVar="+processinsVar+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
				String sub =  sm.get("rets").toString();
				String getpronossql = "SELECT nos FROM bpm_processinstance WHERE refid = '"+jobids+"' AND state = 1 LIMIT 1;";
				Map nosmap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(getpronossql);
				if(nosmap!=null&&nosmap.get("nos")!=null){
					MessageUtils.alert("OK!流水号为：["+nosmap.get("nos").toString()+"]");
				}else{
					MessageUtils.alert("OK");
				}
				Browser.execClientScript("bpmWindowJsVar.hide();");
				Browser.execClientScript("closeWindow();closeWindow2();");
			}catch(Exception e){
				MessageUtils.showException(e);
			}
			try {
				if(bpmProcess!=null){
					BpmProcessinstance bpmProcessinstance =serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findOneRowByClauseWhere("refid = '"+jobids+"' AND state <> 8 AND state <> 9 AND isdelete = FALSE AND processid ="+bpmProcess.getId());
					//serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "isouteroperation", isouteroperation,"操作地");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "billtype", billtype,"提单方式");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "releasetype", releasetype,"放货方式");
					//serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "deliverytype", deliverytype,"类型");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "reason", reason,"取单原因");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "applicants", applicants,"申请人");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "remarks2", remarks2,"备注2");
//					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "remarks", remarks,"备注");
					//serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "islast", ""+islast+"","最后一票");
//					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "isouter", ""+isouter+"","委托公司");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
    @Bind
    @SaveState
    public String billtype;
    
    @Bind
    @SaveState
    public String releasetype;
    
    @Bind
    @SaveState
    public Boolean islast;
    
//    @Bind
//    @SaveState
//    public Boolean isouter;
    
    @Bind
    @SaveState
    public String isopothre;
    
    @Bind
    @SaveState
    public String deliverytype;
    
    @Bind
    @SaveState
    public String reason;
    
    @Bind
	@SaveState
	public String actionJsText;
    
    @Bind(id="releasetypeselect")
    public List<SelectItem> getReleasetypeselect() {
    	try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		items.add(new SelectItem("正本","正本"));
    		items.add(new SelectItem("电放","电放"));
//        	if(billtype!=null&&!billtype.equals("HBL")){
//        		items.add(new SelectItem("SWB","SWB "));
//        	}
        	items.add(new SelectItem("SEAWAYBILL","SEAWAY BILL"));
        	items.add(new SelectItem("AIRWAYBILL","AIRWAYBILL"));
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
	
	@Bind
	public UIDataGrid gridUser;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	@Action
	public void taskname_onselect(){
		this.gridUser.reload();
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder buffer = new StringBuilder();
		buffer.append("\n1=1 ");
		String qryv = StrUtils.isNull(buffer.toString()) ? "" : buffer.toString();
		map.put("qry", qryv);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "\nAND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		BpmProcess bpmProcess = null;
		String processName = "放货流程";
		if(!StrUtils.isNull(type)&&"gsit".equals(type)){
			processName = "放货流程_GSIT";
		}
		bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
		if(bpmProcess!=null){
			qry +=  "\n AND(CASE WHEN EXISTS(SELECT 1 FROM bpm_assign WHERE taskname = '"+taskname+"' AND process_id = "+bpmProcess.getId()+" AND isdelete = FALSE  AND ismatchassign = TRUE) THEN" +
					"\n EXISTS(SELECT 1 FROM bpm_assign_ref x WHERE userid = t.id " +
					"\n AND EXISTS(SELECT 1 FROM bpm_assign WHERE ismatchassign = true AND id = x.assignid AND taskname = '"+taskname+"'))" +
					"\n ELSE TRUE END)";
		}
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser.contains(id)){
				this.nextAssignUser = nextAssignUser + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Bind
	@SaveState
	public String bpmremark = "";
	
	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
}
