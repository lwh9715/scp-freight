package com.scp.view.module.finance;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import com.scp.model.sys.SysAttachment;
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

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.simple.SimpleDynamicSql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.scp.base.CommonComBoxBean;
import com.scp.dao.cache.CommonDBCache;
import com.scp.exception.RpException;
import com.scp.exception.VchAutoGenerateException;
import com.scp.model.finance.FinaActpayrec;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.actpayreceditnewBean", scope = ManagedBeanScope.SESSION)
public class ActPayRecEditNewBean extends GridView {
	
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
	public Boolean ishideallarap = true;
	
	@Bind
	@SaveState
	private Boolean isAutoGenVch = true;
	
	@Bind
	@SaveState
	private Boolean isRpDateCheck = false;
	
	@Bind
	@SaveState
	public Long actpayrecid = -1l;
	
	@Bind
	@SaveState
	public String customerid = "-1";

	@Bind
	@SaveState
	public String invoiceno = "";

	@Bind
	@SaveState
	public String jobnos = "";

	@Bind
	@SaveState
	public String cbillnos = "";

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
	public String userid;
	
	@Bind
	@SaveState
	public String corpid;
	
	
	@Bind
	@SaveState
	public boolean ispublic = false;
	
	@Bind
    @SaveState
    @Accessible
	public String rp2vchFixArapCorpid = "N";
	
	
	@Bind
    @SaveState
    @Accessible
	public String maindbText = "";
	
	
	@Bind
    @SaveState
    @Accessible
	public String dtldbText = "";
	
	@Bind
    @SaveState
    @Accessible
	public String rpsumdbText = "";
	
	@Bind
    @SaveState
    @Accessible
	public String orderColumn = "";
	
	@SaveState
	public String type;
	
	@Bind
    @SaveState
    @Accessible
	public String paid = "";
	
	@Bind
    @SaveState
	public String bankname = "";
	
	@Bind
    @SaveState
	public String accountno = "";
	
	@Bind
    @SaveState
	public String accountcont = "";
	
	@Bind
    @SaveState
	public long accountid = 0L;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			type = (String) AppUtils.getReqParam("type");
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)&&!id.equals("null")){
				actpayrecid = Long.valueOf(id);
			}
			if(!StrUtils.isNull(AppUtils.getReqParam("customerid"))){
				customerid = (String) AppUtils.getReqParam("customerid");
			}
			qrydates = "jobdate";
			//ishideallarap = true;
			jobids = "";
			maindbText = "";
			dtldbText = "";
			rpsumdbText = "";
			userid = AppUtils.getUserSession().getUserid().toString();
			corpid = AppUtils.getUserSession().getCorpid().toString();
			try {
				FinaActpayrec payrec = serviceContext.actPayRecService.finaActpayrecDao.findById(actpayrecid);
				if(null != payrec && null != payrec.getAccountid()){
					accountid = payrec.getAccountid();
					if(accountid > 0) {
						Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT bankname,accountno,accountcont FROM sys_corpaccount WHERE isdelete =FALSE and id = "+accountid);
						if(null != m){
							bankname	=	String.valueOf(m.get("bankname"));
							accountno	=	String.valueOf(m.get("accountno"));
							accountcont	=	String.valueOf(m.get("accountcont"));
							
							Browser.execClientScript("accountJs.setValue('"+bankname+"/"+accountno+"/"+accountcont+"')");
							Browser.execClientScript("accountcontJs.setValue('"+accountcont+"')");
							Browser.execClientScript("banknameJs.setValue('"+bankname+"')");
							Browser.execClientScript("accountnoJs.setValue('"+accountno+"')");
						}
					}else{
						Browser.execClientScript("accountJs.setValue('')");
						Browser.execClientScript("accountcontJs.setValue('')");
						Browser.execClientScript("banknameJs.setValue('')");
						Browser.execClientScript("accountnoJs.setValue('')");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			//查看是否有合同
			showContract();
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
	
	@Action
	public void applyGridUserDefajax(){
		if(!StrUtils.isNull(type))super.applyGridUserDef("pages.module.finance.actpayreceditBean.arapGrid", "'#arapGrid'");
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
		//this.arapGrid.reload();
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
		//this.arapGrid.reload();
		Browser.execClientScript("setTimeout('refreshGridData()',200)");
		this.rpSumGrid.reload();
		this.vchGrid.reload();
		clearFilterQryKey();
		this.update.markUpdate(UpdateLevel.Data,"f1");
	}
	
	
	
	@Action
	public void refreshArapAction(){
		
		Browser.execClientScript("refreshGridData();");
		//this.arapGrid.reload();
		//this.rateGrid.reload();  //华展：销账窗口，汇率调好，搜索单查询，汇率会跟着变回原来的，请设置调好后没改动汇率不要随查询单号刷新汇率
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
			data.setAccountid(accountid);
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
			
//			System.out.println("localdbText:"+maindbText);
//			System.out.println("localdbText:"+dtldbText);
//			System.out.println("localdbText:"+rpsumdbText);
			List<FinaActpayrec> magas= new ArrayList<FinaActpayrec>();
			magas.add(data);
			List<String> filterField=new ArrayList<String>();
			maindbText = AppUtils.modelBeanToJSON(filterField, magas);
			//System.out.println("localdbText:"+maindbText);
			
			maindbText = StrUtils.getSqlFormat(maindbText);
			
			String sql = 
					"SELECT f_fina_actpayrec('actpayrecid="+actpayrecid+";'"+
					",'"+maindbText+"'"+
					",'"+dtldbText+"'"+
					",'"+rpsumdbText+"'"+
					",'"+AppUtils.getUserSession().getUserid()+"') AS ret";
			//System.out.println("sql:"+sql);
			
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String ret = StrUtils.getMapVal(m, "ret");
			String actpayrecidStr = ret.split(":")[0];
			String vchid = ret.split(":")[1];
			actpayrecid = Long.valueOf(actpayrecidStr);
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
					if(isAutoGenVch){
						this.alert("无凭证数据!");	
					}
				}
			}
			//System.out.println("modifiedData save after:"+modifiedData.size());
			//modifiedData.clear();
			//modifiedDataRpSum.clear();
		} catch (RpException e) {
			actpayrecid = data.getId();
			//this.refresh();
			MessageUtils.showException(e);
		} catch (VchAutoGenerateException vche) {
			if(data != null && data.getId() > 0){
				actpayrecid = data.getId();
			}
			//this.refresh();
			MessageUtils.showException(vche);
		} catch (Exception e) {
			if(data != null && data.getId() > 0){
				actpayrecid = data.getId();
			}
			MessageUtils.showException(e);
			//this.refresh();
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
				String sqlId = "pages.module.finance.actpayreceditBean.grid.rpsum";
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
		return map;
	}

	
	@Action
	public void filterConfirm(){
		if(!StrUtils.isNull(jobids)){
			filter  = "\nAND 1=1";
			filter2  = "\nAND (actpayrecid != 0 OR jobid in ("+jobids.substring(0, jobids.length()-1)+"))";
			this.update.markUpdate(UpdateLevel.Data, "filter");
			this.update.markUpdate(UpdateLevel.Data, "filter2");
			this.refresh();
//			//System.out.println(filter2);
//			this.arapGrid.reload();
//			this.rpSumGrid.reload();
			//Browser.execClientScript("");
		}
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
				
				SqlMapClientImpl sqlmap = (SqlMapClientImpl) daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
				MappedStatement stmt = sqlmap.getMappedStatement(sqlId);
				SimpleDynamicSql staticSql = (SimpleDynamicSql) stmt.getSql();
				String sql = staticSql.getSql(null, getQryClauseWhere());
				//System.out.println(sql);
				
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

		if (!StrUtils.isNull(invoiceno)) {
			qry += "AND invoiceno ILIKE '%'||TRIM('"+invoiceno+"')||'%'";
		}
		if (!StrUtils.isNull(cbillnos)) {
			qry += "AND rpreqnos ILIKE '%'||TRIM('"+cbillnos+"')||'%'";
		}
		//工作單支持逗号和模糊查询
		if (!StrUtils.isNull(jobnos)) {
			if (jobnos.contains(",")) {
				String tempStr = jobnos.replaceAll(",", "','");
				qry += "AND jobno IN ('" + tempStr + "')";
			}else{
				qry += "AND jobno ILIKE '%" + jobnos + "%'";
			}
		}

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
		if("T".equals(paid)){
			filter2 +="\n and (CASE WHEN actpayrecid > 0 THEN TRUE ELSE ispayagree = TRUE END)";
		}else if("F".equals(paid)){
			filter2 +="\n and (CASE WHEN actpayrecid > 0 THEN TRUE ELSE ispayagree = FALSE END)";
		}
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
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE a.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+userid+")";
//		if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
//			if(AppUtils.getUserSession().getCorpid() == 66242199 || AppUtils.getUserSession().getCorpid() == 100){
//				corpfilter = "\n AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE (x.corpid = 66242199 OR x.corpid = 100 OR (x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")))";
//			}
//		}
		if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
			corpfilter = "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = "+corpid+") " +
					"\n			THEN " +
					"\n				(a.corpid = ANY(SELECT DISTINCT id FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())" +
						"\n								UNION ALL" +
						"\n								SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + userid + 
						"\n								)" +
						"\n	 		OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = t.arapid :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + userid + " and ischoose = true))))"+

					"\n			ELSE " +
					"\n				(a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + userid + ")" +
					"\n				 OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = t.id :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + userid + " and ischoose = true))))"+

					"\n			END)";
		}else{
			corpfilter = "\n AND (a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + userid + ")"+
						"\n OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = a.id :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = a.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + userid + " and ischoose = true))))";

		}
		
//		if(StrUtils.isNull(agentcorpid)){
//			corpfilter += "\n AND a.corpid = "+this.data.getCorpid()+"";
//		}else{
//			corpfilter += "\n AND a.corpid = ("+agentcorpid+")";
//		}
		map.put("corpfilter", corpfilter);
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
			String clauseWhere = "\nAND EXISTS(SELECT 1 FROM dat_currency z WHERE z.code = x.currencyto AND z.isdelete = FALSE AND z.isuseinact = TRUE)";
			if(this.actpayrecid>0){
				//clauseWhere = "\nAND exists(SELECT 1 FROM fina_actpayrecdtl y where (y.currencyfm IS NULL OR y.currencyfm = '' OR x.currencyfm = y.currencyfm) AND y.actpayrecid = "+this.actpayrecid+")";
			}
				
			map.put("qry", clauseWhere);
			
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
		Browser.execClientScript("setTimeout('refreshRateGridDate()','1000');");
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
	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
		//this.arapGrid.reload();
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
		//this.arapGrid.reload();
		Browser.execClientScript("refreshGridData();");
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
			this.jobnos = "";
			this.update.markUpdate(UpdateLevel.Data, "jobnos");
			this.update.markUpdate(UpdateLevel.Data, "startDateJ");
			this.update.markUpdate(UpdateLevel.Data, "endDateJ");
			this.update.markUpdate(UpdateLevel.Data, "startDateA");
			this.update.markUpdate(UpdateLevel.Data, "endDateA");
			this.update.markUpdate(UpdateLevel.Data, "startDateATA");
			this.update.markUpdate(UpdateLevel.Data, "endDateATA");
			
			if (qryMap != null) {
				qryMap.clear();
				update.markUpdate(true, UpdateLevel.Data, "gridPanel");
				//this.arapGrid.reload();
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
		
		
		@SaveState
		private Map<String , String> filterMap = new HashMap<String , String>();
		
		
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
					return 100000;
				}
			};
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

			if (!StrUtils.isNull(invoiceno)) {
				map.put("invoiceno", "\n AND EXISTS(SELECT 1 FROM fina_invoice fi WHERE isdelete = FALSE AND fi.jobid=j.id AND fi.invoiceno ILIKE '%"+invoiceno+"%')");
			}
			if (!StrUtils.isNull(cbillnos)) {
				map.put("cbillnos", "\n AND EXISTS(SELECT 1 FROM  fina_rpreq  r , _fina_rpreqdtl d where r.id = d.rpreqid AND r.reqtype = 'S' AND r.isdelete = FALSE AND d.isdelete = FALSE  " +
						"AND j.id = d.jobid AND r.nos ILIKE '%"+cbillnos+"%' )");
			}

			return map;
		}
		
//		@Bind
//		@SaveState
//		public String xrate_cyidfm = "";
//		
//		@Bind
//		@SaveState
//		public String xrate_cyidto = "";
//		
//		@Action
//		public void rateGrid_onrowselect() {
//			String[] ids = rateGrid.getSelectedIds();
//			if(ids == null || ids.length <=0){
//				return;
//			}
//			String id = ids[0];
//			Object[] obj = rateGrid.getSelectedValues();
//			//rateGrid.setSelectedRow(5);
//			for (Object object : obj) {
//				HashMap<String,String> map = (HashMap<String,String>)object;
//				xrate_cyidfm = map.get("currencyfm");
//				xrate_cyidto = map.get("currencyto");
//			}
//			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidfm");
//			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidto");
//		}
//		
//		@Action
//		public void rateGrid_onrowdeselect(){
//			String[] ids = rateGrid.getSelectedIds();
//			if(ids == null || ids.length <=0){
//				xrate_cyidfm = "";
//				xrate_cyidto = "";
//			}else{
//				String id = rateGrid.getSelectedIds()[0];
//				Object[] obj = rateGrid.getSelectedValues();
//				for (Object object : obj) {
//					HashMap<String,String> map = (HashMap<String,String>)object;
//					xrate_cyidfm = map.get("currencyfm");
//					xrate_cyidto = map.get("currencyto");
//				}
//			}
//			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidfm");
//			this.update.markUpdate(UpdateLevel.Data, "xrate_cyidto");
//		}
//		
		
		@Action
		public void showJobsInfo(){
			try {
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Action
		public void showJobsInfoajax(){
			try {
				String jobid = AppUtils.getReqParam("jobid");
				if(StrUtils.isNull(jobid)){
					alert("工作单id不能为空");
					return;
				}
				String url = AppUtils.getContextPath();
				String openurl = url + "/pages/module/jobs/jobsinfo.xhtml";
				AppUtils.openWindow("_showDynamic", openurl + "?jobid=" + jobid
						+ "&userid=" + AppUtils.getUserSession().getUserid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Action
		public void showJobs(){
			try {
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Action
		public void showJobsajax(){
			try {
				String jobid = AppUtils.getReqParam("jobid");
				if(StrUtils.isNull(jobid)){
					alert("工作单id不能为空");
					return;
				}
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		@Action
		public void ishideallarapfunajax(){
			String ishideallarapv = AppUtils.getReqParam("ishideallarap");
			String isAutoGenVchv = AppUtils.getReqParam("isAutoGenVch");
			try {
				ConfigUtils.refreshUserCfg("ishideallarap",ishideallarapv, AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg("isAutoGenVch",isAutoGenVchv, AppUtils.getUserSession().getUserid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/* @Action
		public void saveIshideAuto(){
			try {
				ConfigUtils.refreshUserCfg("ishideallarap",ishideallarap.toString(), AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg("isAutoGenVch",isAutoGenVch.toString(), AppUtils.getUserSession().getUserid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		
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
			this.update.markUpdate(UpdateLevel.Data,"ishideallarap");
			this.update.markUpdate(UpdateLevel.Data,"isAutoGenVch");
		}
		
		
		@Override
		public void saveGridUserDefSetDefault() {
			super.saveGridUserDefSetDefault();
			try {
				ConfigUtils.refreshUserCfg("rpgrid.order", "", AppUtils.getUserSession().getUserid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void saveGridUserDef() {
			super.saveGridUserDef();
			if(!StrUtils.isNull(orderColumn)){
				try {
					ConfigUtils.refreshUserCfg("rpgrid.order", orderColumn, AppUtils.getUserSession().getUserid());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		@Bind
		public UIIFrame attachmentIframe;
		
		@Bind
		public UIWindow attachmentWindows;
		
		@Bind
		@SaveState
		public Long pkVal;
		
		@Action
		public void showAttachment(){
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.data.getId()+"&code=payapply");
			attachmentWindows.show();
		}
		
		@Bind
		public UIWindow addAccountWindows;

		@Bind
		public UIIFrame accountIframe;
		
		@Action
		public void addAccount() {
			addAccountWindows.show();
			String targetUrl = "../customer/account.xhtml?customerid="+this.data.getClientid();
			accountIframe.load(targetUrl);
		}
		
		@Resource
		private CommonDBCache commonDBCache;
		
		/**
		 * 客户账户信息
		 */
		@Bind(id="accountconts")
		public List<SelectItem> getAccountconts() {
	    	try {
				return commonDBCache.getComboxItems("id","","bankname||'/'||accountno||'/'||accountcont","sys_corpaccount ","WHERE isdelete = false and  customerid ="+this.data.getClientid(),"order by bankname");
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
	    }
		
		@Action
		public void accountidchange() {
			accountid = Long.valueOf(AppUtils.getReqParam("accountid"));
			try {
				if(accountid > 0L) {
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT bankname,accountno,accountcont FROM sys_corpaccount WHERE isdelete =FALSE and id = "+accountid);
					if(null != m){
						bankname	=	String.valueOf(m.get("bankname"));
						accountno	=	String.valueOf(m.get("accountno"));
						accountcont	=	String.valueOf(m.get("accountcont"));
						
						Browser.execClientScript("accountcontJs.setValue('"+accountcont+"')");
						Browser.execClientScript("banknameJs.setValue('"+bankname+"')");
						Browser.execClientScript("accountnoJs.setValue('"+accountno+"')");
					}
				}else{
					
					Browser.execClientScript("accountcontJs.setValue('')");
					Browser.execClientScript("banknameJs.setValue('')");
					Browser.execClientScript("accountnoJs.setValue('')");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }

	@Bind
	@SaveState
	public String fileName;

	@Bind
	@SaveState
	public String contentType;

	@Bind
	@SaveState
	public Long dPkVal;

	@Action
	public void download(){
		this.dPkVal = Long.valueOf(contractLinkId);
		if(dPkVal==-1l){
			MessageUtils.alert("please choose one");
			return;
		}else{
			SysAttachment sysAttachment = this.serviceContext.sysAttachmentService.sysAttachmentDao.findById(this.dPkVal);
			this.fileName = sysAttachment.getFilename();
			this.contentType = sysAttachment.getContenttype();
			this.update.markUpdate(UpdateLevel.Data,"dPkVal");
		}
	}

	//下载文件
	@Bind(id="fileDownLoad", attribute="src")
	private InputStream getDownload5() {
		try {
			try {
				return this.serviceContext.attachmentService.readFile(this.dPkVal);
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
		} catch (Exception soaExce) {
			try {
				return this.serviceContext.attachmentService.readFile(this.dPkVal);
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
		}
	}

	@Bind
	@SaveState
	private String contractLinkId;

	@Bind
	@SaveState
	private String httpPrefix;

	@Action
	public void showContract() {
		//检查客商是否有合同附件
		String sql = "SELECT id,filename from sys_attachment where linkid = " + this.customerid + " AND roleid = 6594782199 order by id desc limit 1";
		List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		if (null != list && list.size()>0) {
			this.contractLinkId = list.get(0).get("id").toString();
			this.httpPrefix = "/scp/attachment/"+this.contractLinkId+list.get(0).get("filename").toString();
		} else {
			Browser.execClientScript("contractLinkIdJs.hide();");
			Browser.execClientScript("previewContractJsVar.hide();");
		}
	}
}
