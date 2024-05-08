package com.scp.view.bpm.apply;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@ManagedBean(name = "bpm.apply.bpmnewreleasebillapplyBean", scope = ManagedBeanScope.REQUEST)
public class BpmNewReleaseBillApplyBean extends FormView{
	

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
	public Date paymentdate;
	
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
	public UIWindow searchWindow;
	
	@Action
	public void qureyguarantee() {
		this.searchWindow.show();
	}
	
	@Bind
	public UIWindow searchWindow2;
	
	@Action
	public void qureyamout3info() {
		this.searchWindow2.show();
	}
	
	
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
	
	@Bind
	@SaveState
	public BigDecimal creditlimit;
	
	@Bind
	@SaveState
	public BigDecimal cntquantity;
	
	@Bind
	@SaveState
	public BigDecimal amout2;
	
	@Bind
	@SaveState
	public Integer amout3;
	
	@Bind
	@SaveState
	public String nos;
	
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
			sb.append("SELECT (amountcny1+amounthkd1+amountusd1+amountaed1+amountomr1)::NUMERIC(18,2) AS amount");
			sb.append("\n,(SELECT (COALESCE(creditlimit,0)-COALESCE((SELECT SUM(f_amtto(a.arapdate, a.currency, 'USD', a.amount))-SUM(f_amtto(a.arapdate, a.currency, 'USD', COALESCE((SELECT SUM(d.amountwf) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = a.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')  GROUP BY d.arapid),0))) FROM fina_arap a WHERE isdelete = FALSE AND id = ANY(SELECT arapid FROM sys_user_guarantee WHERE userid = u.id)),0)) :: NUMERIC(18,2) AS creditlimit FROM sys_user u WHERE id ="+AppUtils.getUserSession().getUserid()+" AND isdelete = FALSE limit 1) AS creditlimit");
			sb.append("\n,(SELECT COUNT(*) ::NUMERIC FROM bus_ship_container b WHERE ldtype = 'F' AND isdelete = FALSE AND parentid is null AND cntno <> '' AND jobid = ANY(SELECT 	x.id	FROM fina_jobs x,bus_shipping y	WHERE x.isdelete = FALSE AND y.isdelete = FALSE 	AND x.id = y.jobid	AND COALESCE(y.isput,false) = FALSE	AND x.jobdate > now() - interval '3 month'	AND x.customerid = ANY(SELECT DISTINCT customerid FROM fina_jobs WHERE id = ANY(regexp_split_to_array('"+jobids+"',',')::BIGINT[])) AND x.saleid = ANY(SELECT DISTINCT saleid FROM fina_jobs WHERE id IN ("+jobids+")) AND x.id not in ("+jobids+")	AND COALESCE(y.isput,FALSE) = FALSE 	AND (COALESCE(y.putstatus,'') = '' OR (EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = x.id AND s.putstatus IS NOT NULL AND s.putstatus <> '' AND s.putstatus NOT ILIKE '%MBL%' ))) 	AND x.parentid IS NULL 	AND COALESCE(x.nos,'') <> ''   AND (( ( y.putstatus IS NULL OR ( y.putstatus NOT ILIKE'%批准:%' AND y.putstatus NOT ILIKE'%同意:%' ) ) AND ( y.putstatus IS NULL OR ( y.putstatus NOT ILIKE'%批准：%' AND y.putstatus NOT ILIKE'%同意：%' ) )) OR	( y.putstatus ILIKE'%HBL%' AND y.putstatus ILIKE'%HBL%' ))	ORDER BY x.jobdate DESC,x.nos)) as c");
			sb.append("\nFROM(SELECT");
			sb.append("\nSUM(f_amtto(T.arapdate, 'CNY', 'USD', T.amountcny-T.amtstl2cny)) AS amountcny1");
			sb.append("\n,SUM(f_amtto(T.arapdate, 'HKD', 'USD', T.amounthkd-T.amtstl2hkd)) AS amounthkd1");
			sb.append("\n,SUM(T.amountusd-T.amtstl2usd) AS amountusd1");
			sb.append("\n,SUM(f_amtto(T.arapdate, 'AED', 'USD', T.amountaed-T.amtstl2aed)) AS amountaed1");
			sb.append("\n,SUM(f_amtto(T.arapdate, 'OMR', 'USD', T.amountomr-T.amtstl2omr)) AS amountomr1");
			sb.append("\nFROM (SELECT ");
			sb.append("\n(CASE WHEN b.currency = 'CNY' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountcny");
			sb.append("\n,(CASE WHEN b.currency = 'HKD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amounthkd");
			sb.append("\n,(CASE WHEN b.currency = 'USD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountusd");
			sb.append("\n,(CASE WHEN b.currency = 'AED' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountaed");
			sb.append("\n,(CASE WHEN b.currency = 'OMR' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountomr");
			sb.append("\n,COALESCE((SELECT (CASE WHEN b.currency = 'CNY' THEN SUM(COALESCE(amountwf,0))::NUMERIC(18,2) ELSE 0 END )::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')  GROUP BY d.arapid),0) as amtstl2cny");
			sb.append("\n,COALESCE((SELECT (CASE WHEN b.currency = 'HKD' THEN SUM(COALESCE(amountwf,0))::NUMERIC(18,2) ELSE 0 END )::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')  GROUP BY d.arapid),0) as amtstl2hkd");
			sb.append("\n,COALESCE((SELECT (CASE WHEN b.currency = 'USD' THEN SUM(COALESCE(amountwf,0))::NUMERIC(18,2) ELSE 0 END )::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')  GROUP BY d.arapid),0) as amtstl2usd");
			sb.append("\n,COALESCE((SELECT (CASE WHEN b.currency = 'AED' THEN SUM(COALESCE(amountwf,0))::NUMERIC(18,2) ELSE 0 END )::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')  GROUP BY d.arapid),0) as amtstl2aed");
			sb.append("\n,COALESCE((SELECT (CASE WHEN b.currency = 'OMR' THEN SUM(COALESCE(amountwf,0))::NUMERIC(18,2) ELSE 0 END )::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')  GROUP BY d.arapid),0) as amtstl2omr");
			sb.append("\n,b.arapdate");
			sb.append("\nFROM");
			sb.append("\n  fina_jobs a , fina_arap b");
			sb.append("\nWHERE");
			sb.append("\n  a.isdelete = FALSE");
			sb.append("\n  AND a.isclose = FALSE");
			sb.append("\n  AND a.customerid = "+customerid);
			sb.append("\n  AND a.saleid = ANY(SELECT DISTINCT saleid FROM fina_jobs WHERE id IN ("+jobids+"))");
			sb.append("\n  AND b.customerid  not in  (SELECT id FROM sys_corporation WHERE isdelete =FALSE AND iscustomer = FALSE)");
			sb.append("\n  AND b.isdelete = false and b.araptype='AR'");
			sb.append("\n  AND b.jobid = a.id ");
			sb.append("\n) AS T");
			sb.append("\n) AS TT");
			try {
				Map<String, BigDecimal> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
				amout = map.get("amount");
				creditlimit	= map.get("creditlimit");
				cntquantity	= map.get("c");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String sq =	"SELECT "+
			"\n COALESCE((SUM(f_amtto(arapdate, 'CNY', 'USD', t.amountcny))+SUM(f_amtto(arapdate, 'HKD', 'USD', t.amounthkd))+sum(t.amountusd)+SUM(f_amtto(arapdate, 'AED', 'USD', t.amountaed))+SUM(f_amtto(arapdate, 'OMR', 'USD', t.amountomr)) - SUM(amtstl2)),0)::NUMERIC(18,2) AS amount"+
			"\n FROM"+
			"\n (SELECT"+ 					 					
			"\n (CASE WHEN b.currency = 'CNY' THEN COALESCE((b.amount),0)::NUMERIC(18,2) ELSE 0 END ) AS amountcny"+
			"\n ,(CASE WHEN b.currency = 'HKD' THEN COALESCE((b.amount),0)::NUMERIC(18,2) ELSE 0 END ) AS amounthkd"+
			"\n ,(CASE WHEN b.currency = 'USD' THEN COALESCE((b.amount),0)::NUMERIC(18,2) ELSE 0 END ) AS amountusd"+
			"\n ,(CASE WHEN b.currency = 'AED' THEN COALESCE((b.amount),0)::NUMERIC(18,2) ELSE 0 END ) AS amountaed"+
			"\n ,(CASE WHEN b.currency = 'OMR' THEN COALESCE((b.amount),0)::NUMERIC(18,2) ELSE 0 END ) AS amountomr"+
			"\n ,f_amtto(b.arapdate, b.currency, 'USD', COALESCE((b.amount),0))::NUMERIC(18,2) :: INT AS amt"+
			"\n ,COALESCE((SELECT f_amtto(b.arapdate,b.currency,'USD',SUM(COALESCE(amountwf,0)))::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01') GROUP BY d.arapid),0) :: INT AS amtstl2"+
			"\n ,b.arapdate"+
			"\n FROM"+
			"\n fina_jobs a , fina_arap b"+
			"\n WHERE"+
			"\n a.isdelete = FALSE"+			
			"\n AND a.isclose = FALSE"+
			"\n AND a.saleid = ANY(SELECT distinct saleid FROM fina_jobs where id IN ("+jobids+"))"+
			"\n AND (b.customerid = "+customerid+"::BIGINT OR (a.customerid = "+customerid+"::BIGINT AND b.customerid != "+customerid+"::BIGINT))"+
			"\n AND  b.customerid not in (SELECT id FROM sys_corporation WHERE isdelete =FALSE AND iscustomer = FALSE) "+
			"\n and b.isdelete = false and b.araptype='AR'"+
			"\n AND b.jobid = a.id "+
			"\n AND EXISTS (SELECT 1 FROM sys_user_guarantee g WHERE g.arapid = b.id)" +//过滤已担保费用
//			"\n AND NOW() > (DATE_TRUNC('MONTH', (DATE_TRUNC('MONTH', a.jobdate ::DATE) + INTERVAL '1 MONTH')) + INTERVAL '"+daysar+" DAY')::DATE" +
			"\n ORDER BY jobdate,arapdate) t where  (amt-amtstl2) <> 0";
			
			try {
				Map<String, BigDecimal> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sq);
				amout2 = map.get("amount");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.daysar = StrUtils.isNull(this.daysar) ? "1" : this.daysar;
			
			String sql = "";
			//计算超期金额
			if(Integer.valueOf(this.daysar) > 1){
				sql = "SELECT   "+
					"\n COALESCE((SUM(f_amtto(arapdate, 'CNY', 'USD', t.amountcny))+SUM(f_amtto(arapdate, 'HKD', 'USD', t.amounthkd))+sum(t.amountusd)+SUM(f_amtto(arapdate, 'AED', 'USD', t.amountaed)) - SUM(amtstl2)),0) :: int AS amount  "+
//					"\n ,COALESCE(array_to_string(ARRAY(SELECT unnest(array_agg(id||''))),','),null)::TEXT as arapids  "+
//					"\n ,COALESCE(array_to_string(ARRAY(SELECT unnest(array_agg(corpid||''))),','),null)::TEXT as corpids  "+
//					"\n ,COALESCE(array_to_string(ARRAY(SELECT unnest(array_agg((amt-amtstl2)::TEXT))),','),null)::TEXT as amts  "+
					"\n ,STRING_AGG(distinct t.nos,',') AS nos "+
					"\n FROM  "+
					"\n (SELECT "+  					 					
					"\n (CASE WHEN b.currency = 'CNY' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountcny  "+
					"\n ,(CASE WHEN b.currency = 'HKD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amounthkd  "+
					"\n ,(CASE WHEN b.currency = 'USD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountusd  "+
					"\n ,(CASE WHEN b.currency = 'AED' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountaed  "+
					"\n ,f_amtto(b.arapdate, b.currency, 'USD', COALESCE((b.amount),0))::NUMERIC(18,2) :: INT AS amt  "+
					"\n ,COALESCE((SELECT f_amtto(b.arapdate,b.currency,'USD',SUM(COALESCE(amountwf,0)))::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01') GROUP BY d.arapid),0) :: INT AS amtstl2  "+
					"\n ,b.arapdate  "+
					"\n ,b.id  "+
					"\n ,b.corpid  "+
					"\n ,a.nos  "+
					"\n FROM  "+
					"\n fina_jobs a , fina_arap b  "+
					"\n WHERE  "+
					"\n a.isdelete = FALSE "+
					"\n AND a.isclose = FALSE  "+
					"\n AND a.saleid = ANY(SELECT distinct saleid FROM fina_jobs where id IN ("+jobids+"))"+
					"\n AND	a.customerid =  "+customerid+
					"\n AND b.customerid not in (SELECT id FROM sys_corporation WHERE isdelete =FALSE AND iscustomer = FALSE)   "+
					"\n and b.isdelete = false and b.araptype='AR'  "+
					"\n AND b.jobid = a.id   "+
					"\n AND NOT EXISTS (SELECT 1 FROM sys_user_guarantee g WHERE g.arapid = b.id) "+
					"\n AND	to_char(NOW()-(DATE_TRUNC('MONTH', (DATE_TRUNC('MONTH',	a.jobdate ::DATE) + INTERVAL '1 MONTH'))), 'DD')::INT >  "+Integer.valueOf(this.daysar)+
					"\n ORDER BY jobdate,arapdate) t where  (amt-amtstl2) <> 0;";
				try {
					Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					amout3 = Integer.valueOf(String.valueOf(map.get("amount")));
					nos = String.valueOf(map.get("nos"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				sql ="\n SELECT   "+
				"\n COALESCE((SUM(f_amtto(arapdate, 'CNY', 'USD', t.amountcny))+SUM(f_amtto(arapdate, 'HKD', 'USD', t.amounthkd))+sum(t.amountusd)+SUM(f_amtto(arapdate, 'AED', 'USD', t.amountaed)) - SUM(amtstl2)),0) :: INT AS amount  "+
				"\n ,STRING_AGG(distinct t.nos,',') AS nos "+
				"\n FROM  "+
				"\n (SELECT  "+
				"\n (CASE WHEN b.currency = 'CNY' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountcny  "+
				"\n ,(CASE WHEN b.currency = 'HKD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amounthkd  "+
				"\n ,(CASE WHEN b.currency = 'USD' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountusd  "+
				"\n ,(CASE WHEN b.currency = 'AED' THEN COALESCE(b.amount,0)::NUMERIC(18,2) ELSE 0 END ) AS amountaed  "+
				"\n ,f_amtto(b.arapdate, b.currency, 'USD', COALESCE((b.amount),0))::NUMERIC(18,2) :: INT AS amt  "+
				"\n ,COALESCE((SELECT f_amtto(b.arapdate,b.currency,'USD',SUM(COALESCE(amountwf,0)))::NUMERIC(18,2) FROM fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01') GROUP BY d.arapid),0) :: INT AS amtstl2  "+
				"\n ,b.arapdate  "+
				"\n ,b.id  "+
				"\n ,a.nos  "+
				"\n ,b.corpid  "+
				"\n ,(SELECT '('||String_agg(id||'',',')||')' from fina_actpayrecdtl d WHERE d.isdelete = FALSE AND d.arapid = b.id  AND EXISTS(SELECT 1 FROM fina_actpayrec WHERE id = d.actpayrecid AND isdelete = FALSE AND rpdate > '2021-01-01')) AS recdtlid "+
				"\n FROM  "+
				"\n fina_jobs a , fina_arap b  "+
				"\n WHERE  "+
				"\n a.isdelete = FALSE 	 "+		
				"\n AND a.isclose = FALSE  "+
				"\n AND a.saleid = ANY(SELECT distinct saleid FROM fina_jobs where id IN ("+jobids+"))"+
				"\n AND	a.customerid =  "+customerid+
				"\n AND  b.customerid not in (SELECT id FROM sys_corporation WHERE isdelete =FALSE AND iscustomer = FALSE)   "+
				"\n and b.isdelete = false and b.araptype='AR'  "+
				"\n AND b.jobid = a.id   "+
				"\n AND NOT EXISTS (SELECT 1 FROM sys_user_guarantee g WHERE g.arapid = b.id) "+
				"\n AND (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.jobid = a.id AND x.isdelete = FALSE AND (x.putstatus ilike '%批准：MBL%' OR x.putstatus ilike '%批准:MBL%'))  "+
				"\n 			OR (NOT EXISTS(SELECT 1 FROM bus_shipping x WHERE x.jobid = a.id AND x.isdelete = FALSE AND (x.putstatus ilike '%批准：MBL%' OR x.putstatus ilike '%批准:MBL%')) AND	(a.jobdate < DATE_TRUNC('MONTH',	NOW()) - INTERVAL '1 MONTH')	AND a.id not in ("+jobids+")) "+
				"\n 	) "+
				"\n ORDER BY jobdate,arapdate) t where  (amt-amtstl2) <> 0;";
			}
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				amout3 = Integer.valueOf(String.valueOf(map.get("amount")));
				nos = String.valueOf(map.get("nos"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
		salesid = selectedRowData.getSaleid();
		//neo 20170922 附件特殊处理，取当前工作单id+“100”，下面开启流程后，再校正为流程表里面的refid
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid=" + jobid+"100");
		if(!StrUtils.isNull(taskId)){
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			billtype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "billtype");
			releasetype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "releasetype");
			reason = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "reason");
			applicants = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "applicants");
//			remarks = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "remarks");
			remarks2 = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "remarks2");
			islast = "true".equals(serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "islast"))?true:false;
//			isouter = "true".equals(serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "isouter"))?true:false;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				paymentdate = sdf.parse(serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "paymentdate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
				String processCode = "BPM-B80EDD34";
				BpmProcess bpmProcess = null;
				String processName = "新版_放货流程";
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
			String processName = "新版_放货流程";
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
				String processName = "新版_放货流程";
				bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
				
				String processinsVar = "deliverytype"+"-"+deliverytype+"-"+"类型";
				//processinsVar += "#isopothre"+"-"+isouteroperation+"-"+"操作地";
				processinsVar += "#islast"+"-"+islast+"-"+"最后一票";
				processinsVar += "#isopothre"+"-"+isopothre+"-"+"外办";
				String noss = "";
				try{
					if(StrUtils.isNull(nextAssignUser)||"0".equals(nextAssignUser)){	//没有指派审核人默认指派人为部门经理
						String userSql = "SELECT COALESCE((SELECT id FROM sys_user WHERE isdelete = false AND " +
								"deptid = ANY(SELECT deptid  FROM sys_user WHERE isdelete = false AND id = (SELECT saleid FROM fina_jobs where isdelete = FALSE AND id = ANY(regexp_split_to_array('"+jobids+"',',')::BIGINT[]) limit 1) limit 1) " +
								"AND jobdesc = '经理' LIMIT 1),0) as userid";			//当前用户查询不到部门经理则默认为空
						Map userMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(userSql);
						nextAssignUser = String.valueOf(userMap.get("userid"));
					}

					//业务员有上级的情况，只允许分派给上级或上上级
					String pUserSql = "SELECT parentid as parentid FROM sys_user WHERE id = (SELECT saleid FROM fina_jobs WHERE id in (" + this.jobids + ") AND isdelete = FALSE LIMIT 1) AND parentid IS NOT NULL LIMIT 1;";
					Map pUserMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(pUserSql);
					String pUserid = String.valueOf(pUserMap.get("parentid"));

					if (!StrUtils.isNull(pUserid) && !nextAssignUser.contains(pUserid)) {
						String p2UserSql = "SELECT parentid as parentid FROM sys_user WHERE id = " + pUserid + ";";//上上级
						Map p2UserMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(p2UserSql);
						String p2Userid = String.valueOf(p2UserMap.get("parentid"));

						if (!StrUtils.isNull(p2Userid) && !"null".equals(p2Userid)) {
							if (!nextAssignUser.contains(p2Userid)) {
								String p1p2UserSql = "SELECT string_agg(namec,',') as parentname FROM sys_user WHERE id in (" + pUserid + "," + p2Userid + ");";//上上级
								Map p1p2UserMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(p1p2UserSql);
								MessageUtils.alert("请指派该业务员的上级审批：" + p1p2UserMap.get("parentname"));
								return;
							}
						} else {
							String p1p2UserSql = "SELECT string_agg(namec,',') as parentname FROM sys_user WHERE id = " + pUserid + ";";//上级
							Map p1p2UserMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(p1p2UserSql);
							MessageUtils.alert("请指派该业务员的上级审批：" + p1p2UserMap.get("parentname"));
							return;
						}
					}

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
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql("DELETE FROM bpm_processins_var WHERE processinstanceid = "+bpmProcessinstance.getId());	//先清除旧数据，防止脏数据
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "billtype", billtype,"提单方式");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "releasetype", releasetype,"放货方式");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "reason", reason,"取单原因");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "applicants", applicants,"申请人");
					serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "remarks2", remarks2,"备注2");
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(null!=paymentdate){
						serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "paymentdate",	sdf.format(paymentdate),"回款日期");
					}
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
//    		items.add(new SelectItem("目的港放单","目的港放单"));
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
		String processName = "新版_放货流程";
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
	
	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		Map map = queryMap;
		String sql = "\n AND EXISTS(SELECT 1 FROM fina_jobs WHERE saleid = ANY(SELECT distinct saleid FROM fina_jobs where id IN ("+jobids+")) AND id = a.jobid and isdelete =FALSE)"+
				"\n AND EXISTS(SELECT 1 FROM fina_jobs where id = a.jobid AND isdelete =FALSE and customerid = "+customerid+"::BIGINT)"+
				"\n AND  a.customerid not in (SELECT id FROM sys_corporation WHERE isdelete =FALSE AND iscustomer = FALSE) "+
				"\n AND a.isdelete = false and a.araptype='AR'";
		map.put("qry", sql);
		return map;
	}
	
	@Bind(id = "guaranteegrid", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider2() {
		return new GridDataProvider() {
			@SuppressWarnings("deprecation")
			@Override
			public Object[] getElements() {
				String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapUser), start, limit).toArray();

			}

			@SuppressWarnings("deprecation")
			@Override
			public int getTotalCount() {
				String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapUser));
//				UserBean u = new UserBean();
//				newcreditlimit = String.valueOf(list.get(0).get("creditlimit"));
//				u.setNewcreditlimit(String.valueOf(list.get(0).get("creditlimit")));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind(id = "amout3info", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider3() {
		if(StrUtils.isNull(nos)){
			return null;
		}
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String[] array= nos.split(",");
				List<Map> list = new ArrayList<Map>();
				for (int i = 0; i < array.length; i++) {
					Map m = new HashMap();
					m.put("nos", array[i]);
					list.add(i, m);
				}
				
				return list.toArray();
			}

			@Override
			public int getTotalCount() {
//				String sqlId = "pages.sysmgr.user.userBean.amout3info.count";
//				List<Map> list = serviceContext.daoIbatisTemplate
//						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapUser));
////				UserBean u = new UserBean();
////				newcreditlimit = String.valueOf(list.get(0).get("creditlimit"));
////				u.setNewcreditlimit(String.valueOf(list.get(0).get("creditlimit")));
//				Long count = (Long) list.get(0).get("counts");
				return 0;
			}
		};
	}
}
