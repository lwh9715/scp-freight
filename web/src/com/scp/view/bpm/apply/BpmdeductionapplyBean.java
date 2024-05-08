package com.scp.view.bpm.apply;

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
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
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
@ManagedBean(name = "bpm.apply.bpmdeductionapplyBean", scope = ManagedBeanScope.REQUEST)
public class BpmdeductionapplyBean extends FormView{
	
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
	public String Procname = "deduction";
	
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
		}

		customerid = selectedRowData.getCustomerid();
		salesid = selectedRowData.getSaleid();
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid=" + jobid+"100");
		if(!StrUtils.isNull(taskId)){
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			applicants = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "applicants");
			remarks2 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "remarks2");
		}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Action
	public void applyBPMform() {
		try {
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				BpmProcess bpmProcess = null;
				String processName = null;
				if("deduction".equals(Procname)){
					processName = "扣款申请";
				};
				if("deductionEMC".equals(Procname)){
					processName = "扣款申请(长荣专用)";
				};
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind(id="taskDefine")
	@SelectItems
	public List<SelectItem> taskDefine = new ArrayList<SelectItem>();
	
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			BpmProcess bpmProcess = null;
			String processName = null;
			if("deduction".equals(Procname)){
				processName = "扣款申请";
			};
			if("deductionEMC".equals(Procname)){
				processName = "扣款申请(长荣专用)";
			};
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
		BpmProcess bpmProcess = null;
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processName = null;
				if("deduction".equals(Procname)){
					processName = "扣款申请";
				};
				if("deductionEMC".equals(Procname)){
					processName = "扣款申请(长荣专用)";
				};
				
				bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
				String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+StrUtils.getSqlFormat(bpmremark)+";taskname="+taskname+";refno="+this.selectedRowData.getNos()+";refid="+this.selectedRowData.getId()+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
				String sub =  sm.get("rets").toString();
				MessageUtils.alert("OK");
				Browser.execClientScript("bpmWindowJsVar.hide();");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		try {
			if(bpmProcess!=null){
				BpmProcessinstance bpmProcessinstance =serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findOneRowByClauseWhere("refid = '"+jobids+"' AND state <> 8 AND state <> 9 AND isdelete = FALSE AND processid ="+bpmProcess.getId());
				serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "applicants", applicants,"申请人");
				serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "remarks2", remarks2,"备注2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    
    
    @Bind
	@SaveState
	public String actionJsText;
    
    
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
		String processName = null;
		if("deduction".equals(Procname)){
			processName = "扣款申请";
		};
		if("deductionEMC".equals(Procname)){
			processName = "扣款申请(长荣专用)";
		};
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
	
	@Action(id="Procname",event="onchange")
	public void Procname_onchange(){
		taskDefine = getTaskDefine();
		update.markUpdate(true,UpdateLevel.Data,"taskDefine");
	}

	@Bind
	public UIWindow arapDetailWindow;

	@Bind
	public UIIFrame arapDetailIFrame;

	@Bind
	@SaveState
	public String jobid;

	/**
	 * 查看费用详情
	 */
	@Action
	public void arapDetail() {
		if ("deduction".equals(Procname) || "deductionEMC".equals(Procname)) {
			arapDetailIFrame.setSrc("/scp/pages/module/finance/arapedit.xhtml?customerid=" + "100" + "&jobid=" + jobid);
			update.markAttributeUpdate(arapDetailIFrame, "src");
			if (arapDetailWindow != null) {
				arapDetailWindow.show();
			}
		}
	}
}
