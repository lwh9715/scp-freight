package com.scp.view.ff.apply;


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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.ff.apply.releasebillapplyBean", scope = ManagedBeanScope.REQUEST)
public class ReleasebillapplyBean extends FormView {
	
	@SaveState
	public FinaJobs selectedRowData;
	
	@Bind
	@SaveState
	public Date dateapplication = new Date();
	
	@Bind
	@SaveState
	private String processId = "ReleaseBillProcess";
	
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
	public Long customerid=0l ;
	
	
	@Bind
	@SaveState
	public Long salesid=0l ;
	
	@Bind
	@SaveState
	public String twogp="" ;
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Bind
	private UIIFrame jobsApplyIFrame;
	
//	@Bind
//	private UIIFrame formatLinkTraceIframe;
	
	@Bind
	private UIIFrame attachmentIframe;

	@Resource(name="transactionTemplate")
	private TransactionTemplate transactionTemplate;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String jobids = (String)AppUtils.getReqParam("jobid");
			if(!StrUtils.isNull(jobids)){
				this.jobids = jobids;
				update.markUpdate(true, UpdateLevel.Data, "jobids");
				this.refresh();
			}
		}
	}
	
	@Override
	public void refresh() {
		//serviceContext.tFfAssignService().copyAssignFromTemp(processId , processInstanceId , taskName);
		
		String qry = "SELECT f_wf_process_assign('processinstance_id="+processInstanceId+";process_id="+processId+";type=AUTO;usercode="+AppUtils.getUserSession().getUsercode()+";refid="+AppUtils.getUserSession().getUserid()+";jobids="+jobids+"');";
		serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(qry);
		
		this.applicants = AppUtils.getUserSession().getUsername();
		
		this.refreshAssign();
		
		String url = AppUtils.getContextPath() + "/pages/ff/apply/jobslink.xhtml?type=apply&id=" + jobids;
		dtlIFrame.load(url);
		url = AppUtils.getContextPath() + "/pages/ff/apply/jobsapply.xhtml?type=apply&id=" + jobids;
		jobsApplyIFrame.load(url);
		String jobid = jobids.split(",")[0];
		selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findByjobId(Long.valueOf(jobid));
		//if(selectedRowData!=null&&selectedRowData.getTradeway().equals("F")){//工作单是FOB的，类型就是海外部 ，工作单不是FOB的，选CIF
		//	this.deliverytype = "H";
		//}else{
		//	this.deliverytype = "G";
		//}
		SysCorporation sysCor = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCorpid());
		SysCorporation sysCorop = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCorpidop());
		if(sysCor!=null&&(sysCor.getAbbcode().indexOf("SZ"))<0){//工作单的接单公司，非深圳的勾委托公司
			this.isouter = true;
		}
		//操作地：如果操作公司和接单公司一样，选Local操作。如果操作公司不一样，操作公司是深圳的，选外办操作(深圳)。否则选第二个
		if(selectedRowData.getCorpid().toString().equals(selectedRowData.getCorpidop().toString())){
			this.isouteroperation="0";
		}else if(this.selectedRowData.getCorpid()!=this.selectedRowData.getCorpidop()&&(sysCorop.getAbbcode().indexOf("SZ"))>-1){
			this.isouteroperation="2";
		}else{
			this.isouteroperation="1";
		}
		customerid = selectedRowData.getCustomerid();
		salesid = selectedRowData.getSaleid();
		//neo 20170922 附件特殊处理，取当前工作单id+“100”，下面开启流程后，再校正为流程表里面的refid
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid=" + jobid+"100");
		
//		String linkTraceUrl = AppUtils.getContextPath() + "/scp/pages/ff/modelshow.jsp?process_id=" + processId;
//		formatLinkTraceIframe.load(linkTraceUrl);
		
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	
	@Action
	public void startWorkFlow() {
		transactionTemplate.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus transactionStatus) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", selectedRowData.getId());
				m.put("sn", selectedRowData.getNos());
				m.put("billtype", billtype);
				m.put("releasetype", releasetype);
				m.put("deliverytype", deliverytype);
				m.put("reason", reason);
				if(islast){
					m.put("islast", "1");
				}else{
					m.put("islast", "0");
				}
				if(isouter){
					m.put("isouter", "1");
				}else{
					m.put("isouter", "0");
				}
//				if(isouteroperation){
					m.put("isouteroperation", isouteroperation);
//				}else{
//					m.put("isouteroperation", "0");
//				}
				//System.out.println(m);
				//判断职务是否含有经理是经理则跳过操作部审批（操作部审核已删除）
//				SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
//				if(sysUser.getJobdesc().indexOf("经理")>=0){
//					if(deliverytype.equals("H")){
//						m.put("checkOP",2);
//					}else if(deliverytype.equals("G")){
//						m.put("checkOP",1);
//					}
////					m.put("ismanager", 1);
//				}
				if(deliverytype.equals("H")){
					m.put("checkOP",2);
				}else if(deliverytype.equals("G")){
					m.put("checkOP",1);
				}else if(deliverytype.equals("J")){
					m.put("checkOP",1);
				}
//				try {
//					Long refid = (Long) AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT getid() AS pkid").get("pkid");
//					ProcessInstance procInst = WorkFlowUtil.startFF(processId , m, AppUtils.getUserSession().getUsercode(),refid,remarks,remarks2);
//					serviceContext.tFfProcessRefService().saveData(procInst.getId() , jobids);
//					MessageUtils.alert("流程已开启!流水号:["+procInst.getNos()+"]");
//					
//					//begin neo 20170922 附件特殊处理，取当前工作单id+“100”，下面开启流程后，再校正为流程表里面的refid
//					String jobid = jobids.split(",")[0]+"100";
//					String dmlSql = "UPDATE sys_attachment set linkid = "+procInst.getRefId()+" WHERE linkid = "+jobid;
//					AppUtils.getServiceContext().daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
//					//end neo 20170922 附件特殊处理，取当前工作单id+“100”，下面开启流程后，再校正为流程表里面的refid
//					
//					Browser.execClientScript("closeThis()");
//				} catch (EngineException e) {
//					MessageUtils.alert(e.getErrorMsg());
//					e.printStackTrace();
//					transactionStatus.setRollbackOnly();
//				} catch (KernelException e) {
//					MessageUtils.alert(e.getErrorMsg());
//					e.printStackTrace();
//					transactionStatus.setRollbackOnly();
//				} catch (Exception e) {
//					MessageUtils.showException(e);
//					transactionStatus.setRollbackOnly();
//				}
//				sendweixin();
				return null;
			}  
		});
	}
	

	
	
	@Bind
    @SaveState
    @Accessible
	public String processInstanceId="";
	
	@Bind
    @SaveState
    @Accessible
	public String workItemId;
	
	@Bind
    @SaveState
    @Accessible
	public String taskId = "Start";
	
	@Bind
    @SaveState
    @Accessible
	public String taskName = "Start";
	
	@Bind
	@SaveState
	public String taskAssign;
	
	@Bind
	private UIIFrame assignsIframe;

    @Bind
    public UIWindow showTaskAssignWindow;
    
    @Action
    public void closeTaskAssignWindowAction(){
    	this.refreshAssign();
    }
    
    public void refreshAssign() {
//		taskAssign = WorkFlowUtil.findCurrentTaskAssignDesc(processId , processInstanceId , taskName);
		update.markUpdate(true, UpdateLevel.Data, "taskAssign");
	}
    
	/**
	 * 分派联系人
	 */
    @Action
	public void showAssigns(){
    	String jobid = jobids.split(",")[0];
		String url = AppUtils.getContextPath() + "/pages/ff/workitem/taskassign.xhtml?id=" 
		+ jobid +"&processInstanceId="+processInstanceId + "&taskId="+taskId + "&step=0" + "&processId=ReleaseBillProcess&taskName="+taskName;
		assignsIframe.load(url);
		showTaskAssignWindow.show();
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
    
    @Bind
    @SaveState
    public Boolean isouter;
    
    @Bind
    @SaveState
    public String isouteroperation;
    
    @Bind
    @SaveState
    public String deliverytype;
    
    @Bind
    @SaveState
    public String reason;
    
    @Bind(id="releasetypeselect")
    public List<SelectItem> getReleasetypeselect() {
    	try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		items.add(new SelectItem("BILL","正本"));
    		items.add(new SelectItem("TEX","电放"));
    		items.add(new SelectItem("AWB","AIRWAYBILL"));
        	if(billtype!=null&&!billtype.equals("HBL")){
        		items.add(new SelectItem("SWB","SWB "));
        	}
        	items.add(new SelectItem("SEAWAYBILL","SEAWAY BILL"));
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
