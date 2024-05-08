package com.scp.view.module.finance;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.RpException;
import com.scp.exception.VchAutoGenerateException;
import com.scp.model.finance.FinaActpayrec;
import com.scp.model.finance.FinaJobs;
import com.scp.model.finance.FinaRpreq;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.actpayreceditBean", scope = ManagedBeanScope.SESSION)
public class ActPayRecEditBean extends GridView {
	
	@Accessible
	@SaveState
	public FinaActpayrec data;
	
	@Bind
	@SaveState
	public String agentcorpid;
	
	@Bind(id = "agentcorpid")
	public UICombo agentcorpidCombo;
	
	@Bind
	@SaveState
    private Boolean islock;
	
	@Bind
	@SaveState
	private Boolean isagent;
	
	@Bind
	@SaveState
	private Boolean ishideallarap = true;
	
	@Bind
	@SaveState
	private Boolean isAutoGenVch = true;
	
	@Bind
	@SaveState
	private Boolean isRpDateCheck = false;
	
	@Bind
	@SaveState
	private Long actpayrecid = -1l;
	
	@Bind
	@SaveState
	private String customerid = "-1";
	
//	@Bind
//	public UIButton del;
	
	@Bind
	public UIButton save;
	@Bind
	public UIButton delBatch;
	
	@Bind
	@SaveState
	private String customercode;
	
	@Bind
	@SaveState
	private String customername;
	
	@Bind
	@SaveState
	private String paymorecus;
	
	@Bind
	@SaveState
	private String userid;
	
	@Bind
    @SaveState
    @Accessible
	public String processInstanceId;
	
	@Bind
    @SaveState
    @Accessible
	public String workItemIdStr;
	
	@Bind
	@SaveState
	public boolean ispublic = false;
	
	@Bind
    @SaveState
    @Accessible
	public String rp2vchFixArapCorpid = "N";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String type = (String) AppUtils.getReqParam("type");;
			if(!StrUtils.isNull(type))super.applyGridUserDef("pages.module.finance.actpayreceditBean.arapGrid", "arapGridJsvar");
			String id = AppUtils.getReqParam("id");
			processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
			workItemIdStr = (String) AppUtils.getReqParam("workItemId");
			//判断是否走流程
			if(!StrUtils.isNull(workItemIdStr)&&!StrUtils.isNull(id)){
				FinaRpreq finaRpreq = serviceContext.reqMgrService.finaRpreqDao.findById(Long.parseLong(id));
				if(finaRpreq!=null&&finaRpreq.getActpayrecid()!=null){
					id = finaRpreq.getActpayrecid().toString();
				}
				//ProcessInstance processInstance = (ProcessInstance)AppUtils.getWorkflowSession().findProcessInstanceById(processInstanceId);
				//Map<String, Object> m = processInstance.getProcessInstanceVariables();
				//ispublic = (m.get("ispublic")!=null&&((String) m.get("ispublic")).equals("0")?false:true);
			}
			if(!StrUtils.isNull(id)&&!id.equals("null")){
				actpayrecid = Long.valueOf(id);
			}
			if(!StrUtils.isNull(AppUtils.getReqParam("customerid"))){
				customerid = (String) AppUtils.getReqParam("customerid");
			}
			qrydates = "jobdate";
			//ishideallarap = true;
			jobids = "";
			
			userid = AppUtils.getUserSession().getUserid().toString();
			if("edit".equals(type)){
				this.data = serviceContext.actPayRecService.finaActpayrecDao.findById(actpayrecid);
				agentcorpid = (this.data.getAgentcorpid()==null?"-100":this.data.getAgentcorpid().toString());
				this.islock = data.getIslock();
				if(data.getIslock() != null && data.getIslock()==true){
//					del.setDisabled(true);
					save.setDisabled(true);
					//delBatch.setDisabled(true);
				}
				if(data.getIslock() != null && data.getIslock()){
//					del.setDisabled(true);
					save.setDisabled(true);
					//delBatch.setDisabled(true);
				}
				if(this.data == null)add();
				if(this.data.getIsagent()) {
					this.isagent = true;
					this.agentcorpidCombo.show();
				}else {
					this.isagent = false;
					this.agentcorpidCombo.hide();
				}
				this.customerid = String.valueOf(data.getClientid());
				this.isRpDateCheck = this.data.getRpdate() == null ? false:true;
			}else{
				add();
			}
			
			rp2vchFixArapCorpid = ConfigUtils.findSysCfgVal("rp2vch_fix_arap_corpid");
			
			SysCorporation sco = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(customerid));
			customername = StrUtils.isNull(sco.getNamec())?sco.getNamee():sco.getNamec();;
			customercode = sco.getCode() + "/" + sco.getAbbr();
			filter = "";
			filter2 = "";
			qryMap.clear();
			clearQryKey();
			xrate_cyidfm = "";
			xrate_cyidto = "";
			setPaymorecus();
			isGridFilterLazyLoad = true;
			findIshideAuto();
			this.update.markUpdate(UpdateLevel.Data,"actpayrecid");
			this.update.markUpdate(UpdateLevel.Data,"ishideallarap");
			this.update.markUpdate(UpdateLevel.Data,"isAutoGenVch");
			this.update.markUpdate(UpdateLevel.Data,"customerid");
			this.update.markUpdate(UpdateLevel.Data,"customercode");
			this.update.markUpdate(UpdateLevel.Data,"customername");
			this.update.markUpdate(UpdateLevel.Data,"editPanel");
			this.update.markUpdate(UpdateLevel.Data, agentcorpid);
		}
	}
	
	@Bind
	public UIEditDataGrid arapGrid;
	
	@Action
	public void isagent_oncheck() {
		//System.out.println(agentcorpidCombo.getValue().toString());
		if(isagent) {
			this.agentcorpid = "100";
			this.data.setAgentcorpid(Long.parseLong(agentcorpid));
			this.update.markUpdate(UpdateLevel.Data, agentcorpid);
			this.agentcorpidCombo.show();
		}else {
			this.agentcorpid = "";
			this.data.setAgentcorpid(null);
			this.update.markUpdate(UpdateLevel.Data, agentcorpid);
			this.agentcorpidCombo.hide();
		}
	}
	
	@Action
	public void islock_oncheck(){
		if(this.actpayrecid <= 0){
			MessageUtils.alert("保存后才能锁定!");
			islock = false;
			this.update.markUpdate(UpdateLevel.Data,"islock");
			return;
		}
		try {
			if(!islock){
				//已经生成凭证的收付款不允许解除锁定
				Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COUNT(1) AS c FROM fs_vch WHERE srcid = "+actpayrecid+";");
				if(Integer.valueOf(String.valueOf(m.get("c")))>0){
					MessageUtils.alert("已经生成凭证,解锁失败!");
					islock = true;
					return;
				}
			}
			this.serviceContext.actPayRecService.lock(this.data.getId(),islock,AppUtils.getUserSession().getUsercode());
//			del.setDisabled(islock);
			save.setDisabled(islock);
			//delBatch.setDisabled(islock);
			this.data.setIslock(islock);
			this.data.setLocker(AppUtils.getUserSession().getUsercode());
			this.data.setLocktime(new Date());
//			this.data.setAgentcorpid(Long.parseLong(agentcorpid));
//			this.data.setIsagent(isagent);
		} catch (Exception e) {
			MessageUtils.showException(e);
//			del.setDisabled(!islock);
			save.setDisabled(!islock);
			//delBatch.setDisabled(!islock);
			this.data.setIslock(!islock);
			this.data.setLocker(AppUtils.getUserSession().getUsercode());
			this.data.setLocktime(new Date());
			this.update.markUpdate(UpdateLevel.Data,"islock");
			Browser.execClientScript("islock.setValue("+!islock+")");
//			this.data.setAgentcorpid(Long.parseLong(agentcorpid));
//			this.data.setIsagent(isagent);
		}
	}
	
	@Action
	public void grid_ondblclick() {
//		chooseArapWin.show();
	}
	
	@Bind 
	public UICombo bankidEidt;
	
	@Bind(id="bank")
    public List<SelectItem> getBank() {
		String corpid = data.getCorpid().toString();
    	try {
    		boolean filterByCorp = ("Y".equals(ConfigUtils.findSysCfgVal("fina_rp_banknotfilterbycorp")))?false:true;
    		if(filterByCorp){
    			return CommonComBoxBean.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.abbr,'')","_dat_bank AS d","WHERE (d.corpid IS NULL OR d.corpid="+corpid+")","" );
    		}else{
    			return CommonComBoxBean.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.abbr,'')","_dat_bank AS d","WHERE 1=1","" );
    		}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Action
	public void accountCorp_onselect() {
		this.arapGrid.reload();
//		bankidEidt.repaint();
//		this.rpSumGrid.repaint();
		//bankidEidt.repaint();
		this.rpSumGrid.reload();
//		this.rpSumGrid.rebind();
		
//		
	}
	
	/**
	 * 刷新方法
	 */
	@Override
	public void refresh() {
		if(actpayrecid>0){
			this.data = (FinaActpayrec) serviceContext.actPayRecService.finaActpayrecDao.findById(actpayrecid);
		}
		this.arapGrid.reload();
		this.rpSumGrid.reload();
		this.vchGrid.reload();
		clearFilterQryKey();
		this.update.markUpdate(UpdateLevel.Data,"f1");
	}
	
	
	
	@Action
	public void refreshArapAction(){
		this.arapGrid.reload();
		this.rateGrid.reload();
		this.rpSumGrid.reload();
		//refreshArapAction.repaint();
	}
	
	@Action
	public void add(){
		this.data = new FinaActpayrec();
		data.setClientid(Long.valueOf(customerid));
		agentcorpid = null;
		isagent = false;
		islock = false;
		this.agentcorpidCombo.hide();
		this.isAutoGenVch = true;
		
		this.data.setIsconfirmed("N");
		this.data.setIsprinted("N");
		
		this.data.setActpayrecdate(Calendar.getInstance().getTime());
		//this.data.setRpdate(Calendar.getInstance().getTime());
		this.data.setRptype("B");
		this.data.setCurrency(AppUtils.getUserSession().getBaseCurrency());//要取本位币
		// 公司默认为当前用户选择帐套的对应公司
		try {
			this.data.setCorpid(this.serviceContext.accountsetMgrService.fsActsetDao.findById(AppUtils.getUserSession().getActsetid()).getCorpid());
		} catch (Exception e) {
			this.data.setCorpid(AppUtils.getUserSession().getCorpid());
		}
		this.currency = this.data.getCurrency();
		actpayrecid = -1l;
		this.refreshArapAction();
		this.rpSumGrid.reload();
		Browser.execClientScript("addInit();");
		//this.update.markUpdate(true,UpdateLevel.Data,"editPanel");
		this.update.markUpdate(true,UpdateLevel.Data,"actpayrecid");
		
//		del.setDisabled(false);
		save.setDisabled(false);
		//delBatch.setDisabled(false);
	}
	
	@Action
	public void save(){
		if(actpayrecid != -1l){
			data.setId(actpayrecid);
		}
		try {
			if(isagent) {
				if(StrUtils.isNull(agentcorpid)){
					this.alert("代收代付公司为空!");
					return;
				}
				this.data.setAgentcorpid(Long.parseLong(agentcorpid));
				this.data.setIsagent(true);
			}else {
				this.data.setIsagent(false);
			}
			//System.out.println("modifiedData save before:"+modifiedData.size());
			String vchid = this.serviceContext.actPayRecService.save(data , modifiedData ,modifiedDataRpSum , this.isAutoGenVch);
			actpayrecid = data.getId();
			this.refresh();
			if (!StrUtils.isNull(vchid)) {
				if(Long.parseLong(vchid)> 0){
					showVchEdit(vchid);
					//生成凭证后锁定收付款
					if(!islock){
						islock = true;
						islock_oncheck();
					}
				}else{
					this.alert("无凭证数据!");
				}
			}
			//System.out.println("modifiedData save after:"+modifiedData.size());
			//modifiedData.clear();
			//modifiedDataRpSum.clear();
		} catch (RpException e) {
			actpayrecid = data.getId();
			this.refresh();
			MessageUtils.showException(e);
		} catch (VchAutoGenerateException vche) {
			if(data != null && data.getId() > 0){
				actpayrecid = data.getId();
			}
			this.refresh();
			MessageUtils.showException(vche);
		} catch (Exception e) {
			if(data != null && data.getId() > 0){
				actpayrecid = data.getId();
			}
			MessageUtils.showException(e);
			this.refresh();
		}
	}
	
	@Action
	public void del(){
		if(actpayrecid == -1l){
			MessageUtils.alert("Data is not save,please save before!");
			return;
		}
		try {
			this.serviceContext.actPayRecService.delRecPay(Long
					.valueOf(actpayrecid));
			MessageUtils.alert("Current is deleted!");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Bind
	private UIEditDataGrid rpSumGrid;
	
	
	@Bind(id = "rpSumGrid", attribute = "modifiedData")
	@SaveState
    public List<Map> modifiedDataRpSum;
    
	
	@Bind(id = "rpSumGrid", attribute = "dataProvider")
	public GridDataProvider getRpSumDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".grid.rpsum";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereRpSum(), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				return 1000;
			}
		};
	}
	
	public Map getQryClauseWhereRpSum(){
		Map map = super.getQryClauseWhere(qryMap);
		map.put("actpayrecid", actpayrecid);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		
		String filter = SaasUtil.filterByCorpid("c");
		map.put("filter", filter);
		
		return map;
	}

	
	
    @Bind(id = "arapGrid", attribute = "modifiedData")
    @SaveState
    public List<Map> modifiedData;
    
	
	@Bind(id = "arapGrid", attribute = "dataProvider")
	public GridDataProvider getArapDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
//				String sqlId = getMBeanName() + ".grid.count";
//				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere());
//				Long count = (Long)list.get(0).get("counts");
//				return count.intValue();
				return 10000;
			}
		};
	}
	
	public Map getQryClauseWhere(){
		Map map = super.getQryClauseWhere(qryMap);
		map.put("customerid", customerid);
		map.put("actpayrecid", actpayrecid);
		String qry = StrUtils.getMapVal(map, "qry");
		if(!StrUtils.isNull(startDateJ) || !StrUtils.isNull(endDateJ)) {
			String jobdatefilter = "";
			if(qrydates.equals("jobdate")){
				jobdatefilter += "\nAND jobdate::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'";
			}else if(qrydates.equals("etd")){
				jobdatefilter = " AND (CASE " +
								"		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'S' AND xx.id = a.jobid) THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND x.etd::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'L' AND xx.id = a.jobid) THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND x.loadtime::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'A' AND xx.id = a.jobid) THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND x.flightdate1::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'C' AND xx.id = a.jobid) THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = a.jobid AND x.singtime::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								"  END)";
			}
			qry += jobdatefilter;
		}
		if(!StrUtils.isNull(startDateA) || !StrUtils.isNull(endDateA)) {
			qry += "\nAND ((atd BETWEEN '" + (StrUtils.isNull(startDateA) ? "0001-01-01" : startDateA)
				+ "' AND '" + (StrUtils.isNull(endDateA) ? "9999-12-31" : endDateA) + "')"
				+ "\n OR atd IS NULL)";
		}
		
		if(!StrUtils.isNull(startDateATA) || !StrUtils.isNull(endDateATA)) {
			qry += "\nAND ((ata BETWEEN '" + (StrUtils.isNull(startDateATA) ? "0001-01-01" : startDateATA)
				+ "' AND '" + (StrUtils.isNull(endDateATA) ? "9999-12-31" : endDateATA) + "')"
				+ "\n OR atd IS NULL)";
		}
		map.put("qry", qry);
		
		if(ishideallarap){
			if(!StrUtils.isNull(jobids)){
			}else{
				map.put("hideallarap", "AND false");
			}
		}
		
		map.put("filter", filter);
		map.put("filter2", filter2);
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			String corpfilter = "\n AND a.corpid="+AppUtils.getUserSession().getCorpidCurrent();
//			//String corpfilter = "\nAND 1=1";
////			"\n AND EXISTS(SELECT 1 FROM f_fina_rp_arap_filter('jobid='||jobid||';userid="
////			+ AppUtils.getUserSession().getUserid()
////			+ ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"') x WHERE x.id = a.id)";
//			map.put("corpfilter", corpfilter);
//		}
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE a.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
//		if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
//			if(AppUtils.getUserSession().getCorpid() == 66242199 || AppUtils.getUserSession().getCorpid() == 100){
//				corpfilter = "\n AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE (x.corpid = 66242199 OR x.corpid = 100 OR (x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")))";
//			}
//		}
		if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
			corpfilter = "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = "+AppUtils.getUserSession().getCorpid()+") " +
					"			THEN " +
					"				a.corpid = ANY(SELECT DISTINCT id FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())" +
						"								UNION ALL" +
						"								SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + 
						"								)" +
					"			ELSE " +
					"				a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")" +
					"			END)";
		}else{
			corpfilter = "\n AND (a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")"+
				"\n OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = a.id :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = a.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + userid + " and ischoose = true))))";
		}
		
//		if(StrUtils.isNull(agentcorpid)){
//			corpfilter += "\n AND a.corpid = "+this.data.getCorpid()+"";
//		}else{
//			corpfilter += "\n AND a.corpid = ("+agentcorpid+")";
//		}
		map.put("corpfilter", corpfilter);
		map.put("orderby", "ORDER BY odno DESC,araptype DESC,jobno,currency,arapid");
		return map;
	}
	
	
	@Bind
	private UIEditDataGrid rateGrid;

	@Bind
	@SaveState
	private String currency;
	
	@Bind(id="rateGrid")
	public List<Map> getRateGrids() throws Exception{
		List<Map> list = null;
//		if(currency!=null){
			Map map = new HashMap();
			String clauseWhere = "\nAND 1=1";
			if(this.actpayrecid>0){
				//clauseWhere = "\nAND exists(SELECT 1 FROM fina_actpayrecdtl y where (y.currencyfm IS NULL OR y.currencyfm = '' OR x.currencyfm = y.currencyfm) AND y.actpayrecid = "+this.actpayrecid+")";
			}
				
			map.put("qry", clauseWhere);
			
			String filter = SaasUtil.filterByCorpid("x");
			map.put("filter", filter);
			
			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.finance.actpayreceditBean.grid.rate", map);
//		}
		return list;
	}
	
	
	@Action
	public void doRefreshRate(){
		String cytemp = (String)AppUtils.getReqParam("cyid");
		if(StrUtils.isNull(cytemp)){
			return;
		}
		currency = cytemp;
		rateGrid.reload();
	}
	
	@Action
	public void resetRate(){
		rateGrid.reload();
	}
	
	/**
	 * 生成凭证
	 */
	@Action
	public void genVch(){
		if(actpayrecid <= 0){
			MessageUtils.alert("请先保存!");
			return;
		}
		
		try {
			String vchid = this.serviceContext.actPayRecService.rp2vch(actpayrecid, AppUtils.getUserSession().getUsercode());
			if (!StrUtils.isNull(vchid)) {
				if(Long.parseLong(vchid)> 0){
					showVchEdit(vchid);
				}else{
					this.alert("无凭证数据!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			this.refresh();
		}
	}
	
	@Action
	public void showVchWin(){
		showVchWindow.show();
		vchGrid.reload();
	}
	
	@Bind
	public UIWindow showVchWindow;
	
	
	@Bind
	public UIWindow showVchEditWindow;
	
	@Bind
	public UIDataGrid vchGrid;

	@Bind(id="vchGrid")
	public List<Map> getVchGrids() throws Exception{
		List<Map> list = null;
		if(this.actpayrecid > 0){
			Map map = new HashMap();
			map.put("srcid", this.actpayrecid);
			map.put("filter", "\nAND actsetid IS NOT NULL AND actsetid > 0");
			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.finance.actpayreceditBean.grid.vch", map);
		}
		return list;
	}
	
	@Bind
	public UIIFrame vchIfFrame;
	
	@Action
	public void showVchWinAction(){
		String id = AppUtils.getReqParam("id");
		showVchEdit(id);
	}
	
	private void showVchEdit(String id) {
		String url ="";
		String vchSap = ConfigUtils.findSysCfgVal("fs_vch_sap");
		if ("Y".equalsIgnoreCase(vchSap)) {			
			url = "./doc/indtl_sap.xhtml";
			Browser.execClientScript("showVchEditWindow.setWidth(1350)");
		} else {
			url = "./doc/indtl.xhtml";
		}

		showVchEditWindow.show();
		vchIfFrame.load(url+"?id="+id+"");
	}
	
	@Bind
	private UIWindow chooseFeeWindows;
	
	@Bind
	private UIIFrame chooseFeeIFrame;
	
	@Action
	public void chooseFee() {
		if(this.actpayrecid==-1L){
			MessageUtils.alert("请先保存主表");
			return;
			
		}else{
			String url = "./unlinkfeechooseractpay.xhtml?id="+this.actpayrecid+"&customerid="+customerid;
			chooseFeeIFrame.setSrc(url);
			update.markAttributeUpdate(chooseFeeIFrame, "src");
			chooseFeeWindows.show();
		}
	}
	
	@Action(id = "chooseFeeWindows", event = "onclose")
	private void dtlEditDialogClose() {
		this.qryRefresh();

	}
	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
		this.arapGrid.reload();
	}
	
	@Action
	public void delBatch() {
		String[] ids = this.arapGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			this.serviceContext.actPayRecService.delBatch(ids);
			MessageUtils.alert("删除成功");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}
	
	@Action
	public void qryRefresh2() {
		this.arapGrid.reload();
	}
	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFramepaymore;
	
	/**
	 * NEO  多客户销账 弹窗  当前页面as 出来 多客户 ,iframe 显示勾选的多客户
	 */
	@Action
	public void paymore(){
		
		if(this.actpayrecid==-1L){
			MessageUtils.alert("请先保存收付款主表,再进行多客户销账");
			return;
		}else{
			String url = AppUtils.getContextPath() + "/pages/module/finance/actpaymore.xhtml?actpayrecid="+this.actpayrecid;
			dtlIFramepaymore.setSrc(url);
			update.markAttributeUpdate(dtlIFramepaymore, "src");
			update.markUpdate(true, UpdateLevel.Data, dtlDialog);
			dtlDialog.show();
			
		}
		
	}
	

		@Action(id = "dtlDialog", event = "onclose")
		private void dtlDialogClose() {
			refresh();
			//save();
			setPaymorecus();
		}
		
		
		/**
		 * 加载多客户
		 * 排除自己本身 neo 20150129
		 */
		public void setPaymorecus() {
			String sql = "SELECT string_agg(COALESCE(code,'')||'/'||COALESCE(abbr,''),';')AS paymorecus FROM _fina_actpayrec_clients WHERE clientid !=  "+this.customerid+" AND isdelete = FALSE AND actpayrecid = "+this.actpayrecid;
			try {
				Map m = serviceContext.jobsMgrService.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				this.paymorecus = (String) m.get("paymorecus");
			} catch (Exception e) {
				this.paymorecus = "";
			}

			this.update.markUpdate(UpdateLevel.Data, "paymorecus");
		}
		
		@Bind
		@SaveState
		private String startDateJ;
		
		@Bind
		@SaveState
		private String endDateJ;
		
		@Bind
		@SaveState
		private String startDateA;
		
		@Bind
		@SaveState
		private String endDateA;
		
		@Bind
		@SaveState
		private String startDateATA;
		
		@Bind
		@SaveState
		private String endDateATA;
		
		@Bind
		public UIWindow searchWindow;
		

	/*	@Override
		public Map getQryClauseWhere(Map<String, Object> queryMap) {
			Map m = super.getQryClauseWhere(queryMap);
			//费用日期
			String qry = StrTls.getMapVal(m, "qry");
			if(!StrTls.isNull(startDate) || !StrTls.isNull(endDate)) {
				qry += "\nAND t.arapdate BETWEEN '" + (StrTls.isNull(startDate) ? "0001-01-01" : startDate)
					+ "' AND '" + (StrTls.isNull(endDate) ? "9999-12-31" : endDate) + "'";
			}
			m.put("qry", qry);		
			return m;
		}*/
		
		@Action
		public void search(){
			jobnoFilter = "";
			isGridFilterLazyLoad = false;
			
			Map<String, String> map = DateTimeUtil.getFirstday_Lastday_3Month();
			startDateJ = map.get("first");
			endDateJ = map.get("last");
			this.update.markUpdate(UpdateLevel.Data, "startDateJ");
			this.update.markUpdate(UpdateLevel.Data, "endDateJ");
			
//			 System.out.println("一个月前第一天：" + map.get("first"));
//			 System.out.println("一个月前最后一天：" + map.get("last"));
			clearFilterQryKey();
			this.searchWindow.show();
			
		}
		
		@Action
		public void clear(){
			this.clearQryKey();
		}
		
		@Action
		public void searchfee(){
			this.qryRefresh2();
			this.gridFilter.reload();
		}
		
		@Override
		public void clearQryKey() {
			this.startDateJ = "";
			this.endDateJ = "";
			this.startDateA = "";
			this.endDateA = "";
			this.startDateATA = "";
			this.endDateATA = "";
			this.update.markUpdate(UpdateLevel.Data, "startDateJ");
			this.update.markUpdate(UpdateLevel.Data, "endDateJ");
			this.update.markUpdate(UpdateLevel.Data, "startDateA");
			this.update.markUpdate(UpdateLevel.Data, "endDateA");
			this.update.markUpdate(UpdateLevel.Data, "startDateATA");
			this.update.markUpdate(UpdateLevel.Data, "endDateATA");
			
			if (qryMap != null) {
				qryMap.clear();
				update.markUpdate(true, UpdateLevel.Data, "gridPanel");
				this.arapGrid.reload();
				this.gridFilter.reload();
			}
		}
		
		/**
		 * 页面对应的queryMap
		 */
		@SaveState
		@Accessible
		public Map<String, Object> gridFilterQryMap = new HashMap<String, Object>();
		
		@Bind
		public UIDataGrid gridFilter;
		
		
		/*@Bind
		@SaveState
		public Boolean autoFilter = false;*/
		
		@Bind
		@SaveState
		public Boolean autoSet = false;
		
		
		@Bind
		@SaveState
		public String jobnoFilter;
		
		@SaveState
		public Vector<Integer> vector = new Vector<Integer>();
		
		@Action
		public void filterQry(){
//			//System.out.println(jobnoFilter);
			if(StrUtils.isNull(jobnoFilter)){
				return;
			}
			jobnoFilter = jobnoFilter.trim();
			if(autoSet){
				//Browser.execClientScript("autoFindAndFillData('"+jobnoFilter+"');");
				return;
			}
			
			String sqlId = "pages.module.finance.unlinkfeechooseractpayBean.gridFilter.page";
			Object[] obj = daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId, getGridFilterQryClauseWhere(qryMap), 0,
							100000).toArray();
			
			boolean isFind = false;//标记是否列表里面有匹配到，没有匹配则不刷新列表
			String lastJobid = "";
			
			for (int i = 0; i < obj.length; i++) {
				HashMap<String,Object> map = (HashMap<String,Object>)obj[i];
//				//System.out.println(map);
				String jobno = (String)map.get("jobno");
				String mblno = (String)map.get("mblno");
				String sono = (String)map.get("sono");
				String invoiceno = (String)map.get("invoiceno");
				
				if(!StrUtils.isNull(jobno) && (jobno.equalsIgnoreCase(jobnoFilter) || jobno.startsWith(jobnoFilter)  || jobno.indexOf(jobnoFilter)>0)){
					vector.add(i);
					filterMap.put(map.get("jobid").toString(), "");
					isFind = true;
					lastJobid = map.get("jobid").toString();
				}
				if(!StrUtils.isNull(mblno) && (mblno.equalsIgnoreCase(jobnoFilter) || mblno.startsWith(jobnoFilter)  || mblno.indexOf(jobnoFilter)>0)){
					vector.add(i);
					filterMap.put(map.get("jobid").toString(), "");
					isFind = true;
					lastJobid = map.get("jobid").toString();
				}
				if(!StrUtils.isNull(sono) && (sono.equalsIgnoreCase(jobnoFilter) || sono.startsWith(jobnoFilter)  || sono.indexOf(jobnoFilter)>0)){
					vector.add(i);
					filterMap.put(map.get("jobid").toString(), "");
					isFind = true;
					lastJobid = map.get("jobid").toString();
				}
				if(!StrUtils.isNull(invoiceno) && (invoiceno.equalsIgnoreCase(jobnoFilter) || invoiceno.startsWith(jobnoFilter) || invoiceno.indexOf(jobnoFilter)>0) ){
					vector.add(i);
					filterMap.put(map.get("jobid").toString(), "");
					isFind = true;
					lastJobid = map.get("jobid").toString();
				}
			}
			
			if(isFind == false){
				return;
			}
			
			int[] selections = new int[vector.size()];
			for (int i = 0; i < vector.size(); i++) {
				selections[i] = vector.get(i);
			}
			String js="";
			if(vector.size()>0){
				js += "gridFilter.view.focusRow("+selections[vector.size()-1]+");";
				//js += "scrollToRow(gridFilter,"+selections[vector.size()-1]+");";
				//
			}
			//System.out.println(js+":"+lastJobid);
			if(!StrUtils.isNull(js))Browser.execClientScript(js);
				
			gridFilter.setSelections(selections);
		    update.markUpdate("gridFilter");
		    
		    
			Set<String> set = filterMap.keySet();
			StringBuffer stringBuffer = new StringBuffer();
			for (String key : set) {
				stringBuffer.append(key+",");
			}
			jobids = stringBuffer.toString();
			this.update.markUpdate(UpdateLevel.Data, "jobids");
		    
		    //gridSelectFilter();
			//gridFilter.reload();
//			gridFilter.repaint();
//			if(autoFilter)filterConfirm();
//			if(autoSet){
//				this.rpSumGrid.reload();
//				Browser.execClientScript("setTimeout('autoSetDataByFilter()','1000');");
//				Browser.execClientScript("setTimeout('scrollToRow("+lastJobid+")','1000');");
//				
//			}
			
		}
		
		
		@Action
		public void clearFilterQryKey(){
			vector.clear();
			gridFilterQryMap.clear();
			filterMap.clear();
			jobnoFilter = "";
			jobids = "";
			filter = "";
			filter2 = "";
			gridFilter.reload();
			
			gridFilter.setSelections(new int[]{});
		    update.markUpdate("gridFilter");
			
			this.update.markUpdate(UpdateLevel.Data, "jobids");
			this.update.markUpdate(UpdateLevel.Data, "filter");
			this.update.markUpdate(UpdateLevel.Data, "filter2");
			filterQry();
		}
		
		@SaveState
		@Bind
		public String jobids;
		
		@SaveState
		@Bind
		public String filter;
		
		@SaveState
		@Bind
		public String filter2;
		
		@Action
		public void filterConfirm(){
			if(!StrUtils.isNull(jobids)){
				filter  = "\nAND 1=1";
				filter2  = "\nAND (actpayrecid != 0 OR jobid in ("+jobids.substring(0, jobids.length()-1)+"))";
				this.update.markUpdate(UpdateLevel.Data, "filter");
				this.update.markUpdate(UpdateLevel.Data, "filter2");
//				//System.out.println(filter2);
				this.arapGrid.reload();
				this.rpSumGrid.reload();
				
				//Browser.execClientScript("");
			}
		}
		
		@SaveState
		private Map<String , String> filterMap = new HashMap<String , String>();
		
		@Action
		public void gridFilter_onrowselect(){
			gridSelectFilter();
			//filterConfirm();
		}
		
		@Action
		public void gridFilter_onrowdeselect(){
			gridFilter.getSelectedIds();
			//filterMap.clear();
//			vector.remove(index)
			//gridSelectFilter();
			//filterConfirm();
		}
		
		private void gridSelectFilter(){
//			//System.out.println(this.gridFilter.getSelectedRowData());
			String ids[] = this.gridFilter.getSelectedIds();
			
			if(ids == null || ids.length < 1)return;
			for (String id : ids) {
				filterMap.put(id, "");
				////System.out.println(id);
			}
			Set<String> set = filterMap.keySet();
			StringBuffer stringBuffer = new StringBuffer();
			for (String key : set) {
				stringBuffer.append(key+",");
			}
			jobids = stringBuffer.toString();
			this.update.markUpdate(UpdateLevel.Data, "jobids");
		}
		
		@SaveState
		public boolean isGridFilterLazyLoad = true;
		
		
		@Action
		public void closeSearchWindow(){
			isGridFilterLazyLoad = true;
			this.gridFilter.reload();
		}

		@Bind(id = "gridFilter", attribute = "dataProvider")
		protected GridDataProvider getGridFilterDataProvider() {
			return new GridDataProvider() {
				@Override
				public Object[] getElements() {
					if(isGridFilterLazyLoad)return new Object[]{};
					String sqlId = "pages.module.finance.unlinkfeechooseractpayBean.gridFilter.page";
					return daoIbatisTemplate.getSqlMapClientTemplate()
							.queryForList(sqlId, getGridFilterQryClauseWhere(qryMap), start,
									limit).toArray();
				}

				@Override
				public int getTotalCount() {
//					String sqlId =  "pages.module.finance.unlinkfeechooseractpayBean.gridFilter.count";
//					List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
//							.queryForList(sqlId, getGridFilterQryClauseWhere(gridFilterQryMap));
//					if (list == null || list.size() < 1)
//						return 0;
//					Long count = Long.parseLong(list.get(0).get("counts")
//							.toString());
//					return count.intValue();
					return 100000;
				}
			};
		}
		
		@Action
		public void doChangeGridFilterPage(){//每页条数设定
			String page = AppUtils.getReqParam("page");
			this.gridFilter.setRows(Integer.parseInt(page));
			this.gridFilter.rebind();
		}
		
		
		@Bind
		@SaveState
		public String qrydates;
		
		
		public Map getGridFilterQryClauseWhere(Map<String, Object> queryMap) {
			
			Map map = super.getQryClauseWhere(queryMap);
			
			map.put("customerid", customerid);
			map.put("actpayrecid", this.actpayrecid);
			
			String jobdatefilter = "";
			String qry = StrUtils.getMapVal(map, "qry");
			if(!StrUtils.isNull(startDateJ) || !StrUtils.isNull(endDateJ)) {
				if(qrydates.equals("jobdate")){
					jobdatefilter += "\nAND jobdate::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'";
				}else if(qrydates.equals("etd")){
					jobdatefilter = " AND (CASE " +
									"		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'S' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.etd::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
									" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'L' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.loadtime::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
									" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'A' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.flightdate1::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
									" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'C' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.singtime::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
									"  END)";
				}
				qry += jobdatefilter;
			}
			
			map.put("qry", qry);
			
			//20150115 过滤当前单下面已经有的
//			String qry = (String) map.get("qry"); 
			//if(actpayrecid != null && actpayrecid != -1l){
			//	qry += "\nAND NOT EXISTS(SELECT 1 FROM fina_actpayrecdtl x WHERE x.arapid = a.id AND x.isdelete = FALSE AND x.actpayrecid = "+actpayrecid+")";
			//}
//			map.put("qry", qry);
			
			//分公司过滤
//			if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//				String corpfilter = "\n AND a.corpid="+AppUtils.getUserSession().getCorpidCurrent();
//				map.put("corpfilter", corpfilter);
//			}
			
			//String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE a.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
			String corpfilter = "\n AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE  x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
//			if(StrUtils.isNull(agentcorpid)){
//				corpfilter += "\n AND a.corpid = "+this.data.getCorpid()+"";
//			}else{
//				corpfilter += "\n AND a.corpid = ("+agentcorpid+")";
//			}
			map.put("corpfilter", corpfilter);
			return map;
		}
		
		@Bind
		@SaveState
		public String xrate_cyidfm = "";
		
		@Bind
		@SaveState
		public String xrate_cyidto = "";
		
		@Action
		public void rateGrid_onrowselect() {
			String[] ids = rateGrid.getSelectedIds();
			if(ids == null || ids.length <=0){
				return;
			}
			String id = ids[0];
			Object[] obj = rateGrid.getSelectedValues();
			//rateGrid.setSelectedRow(5);
			for (Object object : obj) {
				HashMap<String,String> map = (HashMap<String,String>)object;
				xrate_cyidfm = map.get("currencyfm");
				xrate_cyidto = map.get("currencyto");
			}
			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidfm");
			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidto");
		}
		
		@Action
		public void rateGrid_onrowdeselect(){
			String[] ids = rateGrid.getSelectedIds();
			if(ids == null || ids.length <=0){
				xrate_cyidfm = "";
				xrate_cyidto = "";
			}else{
				String id = rateGrid.getSelectedIds()[0];
				Object[] obj = rateGrid.getSelectedValues();
				for (Object object : obj) {
					HashMap<String,String> map = (HashMap<String,String>)object;
					xrate_cyidfm = map.get("currencyfm");
					xrate_cyidto = map.get("currencyto");
				}
			}
			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidfm");
			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidto");
		}
		/**
		 * grid在前台设置每页显示的行数
		 */
		@Override
		public void doChangeGridPageSize() {
			String pageStr = (String)AppUtils.getReqParam("page");
			if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
				//alert("pageStr:"+pageStr);
				Integer page = Integer.parseInt(pageStr);
				this.arapGrid.setRows(page);
				this.arapGrid.rebind();
//				//记录选择的行数到个人设置
				String mbeanId = this.getMBeanName();
				String girdId = mbeanId+".arapGrid.pagesize";
				try {
//					CfgUtil.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
					ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				alert("Wrong pagesize：" + pageStr);
			}	
		}
		
		/**
		 * 由个人设置中提取行数，若找不大则返回默认的1000行
		 * 
		 * @return
		 */
		@Override
		public Integer getGridPageSize() {
			Integer page = 1000;
			gridPageSize = page;
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId + ".arapGrid.pagesize";
			String pageSize;
			try {
				pageSize = ConfigUtils.findUserCfgVal(girdId, AppUtils.getUserSession().getUserid());
				if (!StrUtils.isNull(pageSize) && StrUtils.isNumber(pageSize)) {
					Integer pageDef = Integer.parseInt(pageSize);
					if(pageDef<page){
						gridPageSize = page;
					}else{
						gridPageSize = pageDef;
					}
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return gridPageSize;
		}
		
		@Action
		public void showJobsInfo(){
			String[] ids = arapGrid.getSelectedIds();
			if(ids == null || ids.length <=0){
				MessageUtils.alert("请勾选要打开的工作单!");
				return;
			}
			String id = ids[0].split("-")[0];
			String querySql = "SELECT DISTINCT x.jobid AS jobid FROM fina_arap x where x.id = "+id+" OR EXISTS(SELECT 1 FROM fina_actpayrecdtl y where y.id = "+id+" AND y.arapid = x.id AND y.isdelete = false) AND x.isdelete = false;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String jobid = StrUtils.getMapVal(map, "jobid");
//			String url =AppUtils.getRptUrl()+"/reportJsp/showReport.jsp?raq=/bus/feeprofit-en.raq&jobid="+jobid+"&userid="+AppUtils.getUserSession().getUserid();
//			AppUtils.openWindow("", url);
//			
			String url = AppUtils.getContextPath();
			String openurl = url + "/pages/module/jobs/jobsinfo.xhtml";
			AppUtils.openWindow("_showDynamic", openurl + "?jobid=" + jobid
					+ "&userid=" + AppUtils.getUserSession().getUserid());
		}
		
		@Action
		public void showJobs(){
			String[] ids = arapGrid.getSelectedIds();
			if(ids == null || ids.length <=0){
				MessageUtils.alert("请勾选要打开的工作单!");
				return;
			}
			String id = ids[0].split("-")[0];
			String querySql = "SELECT DISTINCT x.jobid AS jobid FROM fina_arap x where x.id = "+id+" OR EXISTS(SELECT 1 FROM fina_actpayrecdtl y where y.id = "+id+" AND y.arapid = x.id AND y.isdelete = false) AND x.isdelete = false;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String jobid = StrUtils.getMapVal(map, "jobid");
			FinaJobs jobs = serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid==null?"":jobid));
			if(jobs!=null&&jobs.getIsair()==false){
				String url = "/scp/pages/module/ship/jobsedit.xhtml?id="+jobid;
				AppUtils.openWindow("", url);
			}else if(jobs!=null&&jobs.getIsair()==true){
				String url = "/scp/pages/module/air/jobsedit.xhtml?id="+jobid;
				AppUtils.openWindow("", url);
			}else{
				alert("工作单不存在！");
				return;
			}
			
		}
		
		@Bind
		@SaveState
		@Accessible
		public String showwmsinfilename = "rp_detail.raq";

		@Action
		public void preview() {
			if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
				MessageUtils.alert("请选择格式！");
				return;
			}
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl
					+ "/reportJsp/showReport.jsp?raq=/finance/rp/"
					+ showwmsinfilename;
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
					+ "&userid="
					+ AppUtils.getUserSession().getUserid());
		}

		private String getArgs() {
			String args = "";
			args += "&id=" + this.actpayrecid;
			return args;
		}
		
		@Bind(id = "reportinformat")
		public List<SelectItem> getReportinformat() {
			try {
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'rp' AND isdelete = FALSE",
						"order by namec");
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
		}
		
		@Action
		public void delVch(){
			String[] ids = vchGrid.getSelectedIds();
			if(ids == null || ids.length <=0){
				MessageUtils.alert("请勾选要清除的行!");
				return;
			}
			this.serviceContext.vchMgrService.deleteVchs(ids);
			//alert("OK");
			this.vchGrid.reload();
		}
		
		@Bind
		@SaveState
		public String copyYear;
		
		@Bind
		@SaveState
		public String copyPeriod;
		
		@Bind
		@SaveState
		public Long copyActset;
		
		@Action
		public void copy() {
			String[] ids = this.vchGrid.getSelectedIds();
			if(ids == null || ids.length == 0){
				MessageUtils.alert("Please choose one!");
				return;
			}
			
			if(StrUtils.isNull(copyYear)){
				MessageUtils.alert("Copy year is null!");
				return;
			}
			
			if(StrUtils.isNull(copyPeriod)){
				MessageUtils.alert("Copy month is null!");
				return;
			}
			
			if(ids.length>=1){
				try {
					StringBuffer stringBuffer = new StringBuffer();
					for (String id : ids) {
						String args = "srctype=RP;id="+id+";user="+AppUtils.getUserSession().getUsercode()+";actsetid="+copyActset+";year="+copyYear+";period="+copyPeriod;
						String sql = "SELECT f_fs_vch_addcopy('"+args+"') AS vchid";
						List list = this.serviceContext.vchMgrService.fsVchDao.executeQuery(sql);
						String vchNew = (String) list.get(0);
						stringBuffer.append(vchNew);
						stringBuffer.append("\n");
					}
					MessageUtils.showMsg("OK!");
					this.vchGrid.reload();
				} catch (Exception e) {
					e.printStackTrace();
					MessageUtils.showException(e);
				}
			}
		}
		
		
		
		@Resource(name="transactionTemplate")
		private TransactionTemplate transactionTemplate;
		
		@SaveState
		@Accessible
		public String currwork;
		
		
		@Action
		public void saveIshideAuto(){
			try {
				ConfigUtils.refreshUserCfg("ishideallarap",ishideallarap.toString(), AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg("isAutoGenVch",isAutoGenVch.toString(), AppUtils.getUserSession().getUserid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void findIshideAuto(){
			try{
				String findUserCfgVal = ConfigUtils.findUserCfgVal("ishideallarap",  AppUtils.getUserSession().getUserid());
				ishideallarap = Boolean.valueOf(findUserCfgVal);
			}catch(Exception e){
				ishideallarap = true;
			}
			try{
				isAutoGenVch = Boolean.valueOf(ConfigUtils.findUserCfgVal("isAutoGenVch",  AppUtils.getUserSession().getUserid()));
			}catch(Exception e){
				isAutoGenVch = true;
			}
			this.update.markUpdate(UpdateLevel.Data,"isAutoGenVch");
			this.update.markUpdate(UpdateLevel.Data,"isAutoGenVch");
		}
}
