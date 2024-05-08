package com.scp.view.module.finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.ibm.icu.text.DecimalFormat;
import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.ConstantBean.Module;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.cache.CommonDBCache;
import com.scp.dao.sys.SysLogDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.data.DatFeeitem;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysUser;
import com.scp.schedule.AutoImportDocument;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.ufms.web.view.sysmgr.LogBean;

@ManagedBean(name = "pages.module.finance.arapeditBean", scope = ManagedBeanScope.REQUEST)
@SuppressWarnings("deprecation")
public class ArapEditBean extends EditGridView {

	@SaveState
	@Accessible
	public FinaArap selectedRowData = new FinaArap();

	@SaveState
	@Accessible
	public FinaJobs jobs = new FinaJobs();

	@Bind
	@SaveState
	private Long arapfkid;

	@Bind
	@SaveState
	private String cntdesc;

	@Bind
	@SaveState
	private Long jobid;

	@Bind
	@SaveState
	private Long customerid = -100l;

	@SaveState
	@Bind
	private String iswarehouse = "N";

	@Bind
	@SaveState
	private Boolean ishidegpr = false;

	@Bind
	@SaveState
	public String arTip;

	@Bind
	@SaveState
	public String currency="USD";

	@Bind
	@SaveState
	public String feeIds = "";


	@Bind
	@SaveState
	public String apTip;

	@Bind
	@SaveState
	public String profitTip;

	@Bind
	@SaveState
	public String arTipNoTax;

	@Bind
	@SaveState
	public String apTipNoTax;

	@Bind
	@SaveState
	public String profitTipNoTax;

	@Bind
	@SaveState
	public String processInstanceId;

	@Bind
	@SaveState
	public String workItemId;

	@Bind
	@SaveState
	public String exchangeTip;

	@SaveState
	public String retar_ap;

	@SaveState
	public String lixi;

	@Bind
	public UIWindow batchEditWindow;

	@Bind
	public UIButton dividedProfit;

	@Bind
	public UIButton costConsolidation;

	@Bind
	public UIWindow dividedProfitWindow;

	@Bind
	public UIIFrame dividedProfitIFrame;

	@Bind
	public UIIFrame dtlIFrameArap;

	@Bind
	public UIButton addw;

	@Bind
	public UIButton delBat;

	@Bind
	public UIButton showDynamic;

	@Bind
	public UIButton batchedit;

	@Bind
	public UIButton batchmodify;

	@Bind
	public UIButton createrCurrent;

	@Bind
	public UIButton scanReport;

	@Bind
	public UIButton impTemplet;

//	@Bind
//	public UIButton importData;

//	@Bind
//	public UIButton importFile;

	@Bind
	public UIButton impOtherJobs;

	@Bind
	public UIButton addwin;

	@Bind
	public UIButton addedit;

	@Bind
	public UIButton addBatch;

	@Bind
	public UIButton copyadd;

	@Bind
	public UIButton save;

	@Bind
	public UIButton savewin;

	@Bind
	public UIButton saveadd;

	@Bind
	public UIButton saveclose;

	@Bind
	public UIButton del;

	@Resource
	public ApplicationConf applicationConf;

	public Long userid;

	@Bind
	@SaveState
	public String jobtype;

	@Bind
	@SaveState
	public boolean issettlement = false;

	@Bind
	@SaveState
	public boolean isHideAPCustomerShow = false;

	@Bind
	@SaveState
	public boolean isHideAPFeeitemShow = false;

	@SaveState
	protected String CSNO = "";

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			userid = AppUtils.getUserSession().getUserid();
			super.applyGridUserDef();
			initCtrl();
			usrCfg();
			ConfigUtils.findUserCfgVal("fee_profits_cy2", AppUtils.getUserSession().getUserid());
			String id = AppUtils.getReqParam("id");
			String jobid = AppUtils.getReqParam("jobid");
			processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
			workItemId = (String) AppUtils.getReqParam("workItemId");
			String customerid = AppUtils.getReqParam("customerid");
			iswarehouse = AppUtils.getReqParam("iswarehouse");
			arapselected = AppUtils.getReqParam("arapid");
			if (!StrUtils.isNull(id)) {
				arapfkid = Long.parseLong(id);
			}
			if (!StrUtils.isNull(jobid)) {
				this.jobid = Long.parseLong(jobid);
				this.update.markUpdate(UpdateLevel.Data, "jobid");

			} else {
				MessageUtils.alert("参数jobid为空!");
			}

			//取出增减流程增减费用信息
			String type = AppUtils.getReqParam("type");
			if("amend".equals(type) && "".equals(amendfeeiinfo)){
				String taskid = AppUtils.getReqParam("taskid");
				String sql = "SELECT arapinfo FROM bpm_task WHERE processid||''||processinstanceid = (SELECT processid||''||processinstanceid FROM bpm_task WHERE id = "+taskid+" LIMIT 1) AND taskname='Start'  ORDER BY CREATEDTIME DESC LIMIT 1";
				try {
					Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					if(map.containsKey("arapinfo")){
						amendfeeiinfo = String.valueOf(map.get("arapinfo"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


			if (!StrUtils.isNull(customerid) && !"null".equals(customerid)) {
				this.customerid = Long.parseLong(customerid);
			}

			if (StrUtils.isNull(iswarehouse)) {
				this.iswarehouse = "N";
			}

			//2024110途曦仓库不允许查看费用明细
			if (AppUtils.getUserSession().getCorpid() == 11540072274L) {
				boolean ishidearap = getCheckright("HideArapProfit", AppUtils.getUserSession().getUserid());
				if (ishidearap) {
					MessageUtils.alert("无查看费用权限！");
					return;
				}
			}

			if (this.jobid != null) {
				jobs = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
				initArapInfo();
				initCntDesc();
				// qryMap.put("jobid$", this.jobid);
				initAdd();
				// this.grid.repaint();

				String querySql = "select count(*) from fina_jobs where parentid = "+jobs.getId()+" AND isdelete = false;";
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

				boolean isdivided = getCheckright("DividedProfit", AppUtils.getUserSession().getUserid());

				//仅接单公司电商途曦、青岛可见
				if (Long.valueOf(map.get("count").toString()) == 0 || (AppUtils.getUserSession().getCorpid() != 11540072274L
						&& AppUtils.getUserSession().getCorpid() != 1122274L && !isdivided)) {
					Browser.execClientScript("costConsolidationJsvar.hide();");
					Browser.execClientScript("dividedProfitJsvar.hide();");
                } else if (jobs.getJobtype().equals("D")) {
					Browser.execClientScript("costConsolidationJsvar.hide();");
					Browser.execClientScript("dividedProfitJsvar.hide();");
				}

				jobtype = jobs.getJobtype();

				isHideAPCustomerShow = getCheckright("HideAPCustomerShow" , AppUtils.getUserSession().getUserid());
				isHideAPFeeitemShow = getCheckright("HideAPFeeitemShow" , AppUtils.getUserSession().getUserid());
			}
			jobs_lock();
			CSNO = ConfigUtils.findSysCfgVal("CSNO");

			//查询往来费用是否一致
			checkFeei();
		}
	}

	public void checkFeei() {
		Map m = null;
		String sql = " SELECT COUNT(1) AS c FROM	"+
			"\n (SELECT	"+
			"\n t.corpid,t.customerid,t.currency,COALESCE(t.amount,0)+COALESCE(t2.amount,0) AS amount	"+
			"\n FROM	"+
			"\n (SELECT 	"+
			"\n a.corpid,customerid,currency,sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1*amount END) AS amount	"+
			"\n FROM fina_arap a 	"+
			"\n WHERE a.jobid = "+jobid+
			"\n AND a.isdelete = FALSE 	"+
			"\n AND a.rptype != 'O'	"+
			"\n AND EXISTS (SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)	"+
			"\n GROUP BY a.corpid,customerid,currency) t	"+
			"\n LEFT JOIN (SELECT 	"+
			"\n a.corpid,customerid,currency,sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1*amount END) AS amount	"+
			"\n FROM fina_arap a 	"+
			"\n WHERE a.jobid = "+jobid+
			"\n AND a.isdelete = FALSE 	"+
			"\n AND a.rptype != 'O'	"+
			"\n AND EXISTS (SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)	"+
			"\n GROUP BY a.corpid,customerid,currency) t2 ON (t2.customerid = t.corpid AND t2.corpid = t.customerid AND t2.currency = t.currency)	"+
			"\n ) TT WHERE TT.amount != 0";
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(Integer.valueOf(String.valueOf(m.get("c")))>0){
				Browser.execClientScript("checkfeeistatus.show()");
			}else{
				Browser.execClientScript("$('#feeiCheck').hide()");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	public void reloadSelect() {
		if(StrUtils.isNull(arapselected)==false){
			List<Integer> rowList = new ArrayList<Integer>();
			int[] row = null;
			int rownum = 0;
			String orderby = ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime");
			String orderSql = "";
			if(StrUtils.isNull(orderby) || "N".equals(orderby)){
				orderSql = "";
			}else{
				orderSql = ",a.inputtime";
			}
			String sql = "SELECT f.code,A.* " +
					"\n from _fina_arap a " +
					"\n LEFT JOIN dat_feeitem f ON (f.id = a.feeitemid AND f.isdelete = FALSE)" +
					" LEFT JOIN fina_arap_link_quote falq ON a.ID = falq.arapid " +
					"\n WHERE a.jobid = "+jobid+" AND a.isdelete = FALSE AND a.parentid IS null" +
					"\n AND a.corpid = ANY(SELECT (CASE WHEN EXISTS (SELECT 1 FROM sys_user_corplink WHERE userid = "+AppUtils.getUserSession().getUserid()+" AND ischoose = TRUE AND corpid = (SELECT corpid FROM fina_jobs j WHERE j.id = a.jobid AND j.isdelete = FALSE LIMIT 1)) THEN 157970752274 ELSE -1 END) UNION SELECT corpid FROM sys_user_corplink WHERE userid = "+AppUtils.getUserSession().getUserid()+" AND ischoose = TRUE) " +
					"\n AND NOT EXISTS (SELECT 1 from dat_feeitem f WHERE f.id = feeitemid AND f.isdelete = FALSE AND ishidden = TRUE AND f_checkright('CheckTheCostOfTheShadow'," + AppUtils.getUserSession().getUserid() + ")=0)"+
					"\n order by a.araptype desc, a.ppcc,a.customerid , a.currency DESC , a.feeitemdec "+orderSql;
			try {
				List<Map> list = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql(sql);
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					if (map.get("id") != null && arapselected.indexOf(String.valueOf(map.get("id")))>-1){
						rowList.add(rownum);
					}
					rownum++;
				}

				row = new int[rowList.size()];
				for (int i = 0; i < rowList.size(); i++) {
					row[i] = rowList.get(i);
				}
				this.editGrid.setSelections(row);
				this.editGrid.reload();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SaveState
	public String currencycyadd = "CNY";


	@Resource
	public CommonDBCache commonDBCache;

	@SaveState
	@Bind
	public String comboQryKey = "";

	@Bind(id = "feeitemCombox")
    private UICombo feeitemCombox;

	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> feeitems;

	@Action
	private void changeFeeItemCombox() {
        comboQryKey = AppUtils.getReqParam("filter");
        feeitems = getFeeItem();
        if(feeitems == null)return;
        for (SelectItem item : feeitems) {
        	//System.out.println(item.getLabel());
		}
		this.update.markUpdate(true , UpdateLevel.Data, feeitemCombox );
	}

	/**
	 * 费用名称
	 * @return
	 */
    public List<SelectItem> getFeeItem() {
    	try {
    		String whereSql = "WHERE 1=1";
    		if(!StrUtils.isNull(comboQryKey)){
    			if(comboQryKey.indexOf("/") > 0)comboQryKey=comboQryKey.split("/")[0];
    			whereSql += " AND( code ILIKE '%"+comboQryKey+"%'";
    			whereSql += " OR namee ILIKE '%"+comboQryKey+"%'";
    			whereSql += " OR name ILIKE '%"+comboQryKey+"%'";
    			whereSql += " )";
    		}
    		//System.out.println(whereSql);
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| d.namee","d.code ||'/'|| d.name","_dat_feeitem AS d",whereSql," ORDER BY code LIMIT 20");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	/*
	 * 查个人设置
	 * */
	public void usrCfg(){
		Map map = ConfigUtils.findUserCfgVals(new String[]{"fee_profits_cy2","fee_add_cyid"}, AppUtils.getUserSession().getUserid());
		if(map != null && map.size() > 0){
			if(map.get("fee_profits_cy2")!=null&&!map.get("fee_profits_cy2").equals("")){
				this.currency = map.get("fee_profits_cy2").toString();
			}
			if(map.get("fee_add_cyid")!=null){
				this.currencycyadd = map.get("fee_add_cyid").toString();
			}
		}
	}

	private void initCntDesc() {
		String sql = "SELECT f_fina_jobs_cntdesc('jobid="+this.jobid+"') AS cntdesc";
		try {
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map != null && map.size() > 0){
				this.cntdesc = StrUtils.getMapVal(map, "cntdesc");
			}else{
				this.cntdesc = "";
			}
		} catch (NoRowException e) {
			this.cntdesc = "";
		}catch (Exception e) {
			this.cntdesc = "ERROR";
		}
		this.cntdesc = retar_ap +"\n"+ lixi +"\n"+ this.cntdesc;
		//update.markUpdate(UpdateLevel.Data, "cntdesc");
	}


	@Bind
	public UIButton confirmArAp;

	@Bind
	public UIButton cancelArAp;

	@Bind
	public UIButton audit;

	@Bind
	public UIButton auditcancel;

	@Bind
	public UIButton orderSyn;

	@Bind
	public String isadditionalcost;

	private void initCtrl() {
		addw.setDisabled(true);
		delBat.setDisabled(true);
		showDynamic.setDisabled(true);
		batchedit.setDisabled(true);
		dividedProfit.setDisabled(true);
		batchmodify.setDisabled(true);
		createrCurrent.setDisabled(true);
		//scanReport.setDisabled(true);
		impTemplet.setDisabled(true);
//		importData.setDisabled(true);
//		importFile.setDisabled(true);
		impOtherJobs.setDisabled(true);
		addwin.setDisabled(true);
		addedit.setDisabled(true);
		addBatch.setDisabled(true);
		copyadd.setDisabled(true);
		save.setDisabled(true);
		savewin.setDisabled(true);
		saveadd.setDisabled(true);
		saveclose.setDisabled(true);
		del.setDisabled(true);

		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_arap.getValue())) {
			if (s.endsWith("_add")) {
				addw.setDisabled(false);
				impTemplet.setDisabled(false);
//				importData.setDisabled(false);
				//importFile.setDisabled(false);
				impOtherJobs.setDisabled(false);
				addwin.setDisabled(false);
				addedit.setDisabled(false);
				addBatch.setDisabled(false);
				copyadd.setDisabled(false);
				createrCurrent.setDisabled(false);
				batchedit.setDisabled(false);
				dividedProfit.setDisabled(false);
				batchmodify.setDisabled(false);
				save.setDisabled(false);
				savewin.setDisabled(false);
				saveclose.setDisabled(false);
				saveadd.setDisabled(false);
			}else if (s.endsWith("_update")) {
				save.setDisabled(false);
				savewin.setDisabled(false);
				saveclose.setDisabled(false);
				batchedit.setDisabled(false);
				dividedProfit.setDisabled(false);
				batchmodify.setDisabled(false);
			} else if (s.endsWith("_report")) {
				showDynamic.setDisabled(false);
				//scanReport.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delBat.setDisabled(false);
				del.setDisabled(false);
			} else if (s.equals("HideAPAmtstlShow")) {
				String isHideAPAmtstlShowJsVar = "Y";
				if(StrUtils.isNull(isHideAPAmtstlShowJsVar))isHideAPAmtstlShowJsVar="N";
				Browser.execClientScript("isHideAPAmtstlShowJsVar.setValue('"+isHideAPAmtstlShowJsVar+"')");
			}
		}
		//1891 工作单费用确认取消按钮按权限控制灰掉
		if (!getCheckright("ConfirmArAp",AppUtils.getUserSession().getUserid())) confirmArAp.setDisabled(true);
		if (!getCheckright("CancelArAp",AppUtils.getUserSession().getUserid())) cancelArAp.setDisabled(true);
		//2151  此列只有财务和商务才可以看到这个金额，其他人是不授权就不能看到的
		Map m = null;
		String sql = "SELECT EXISTS (SELECT * FROM sys_user u WHERE u.id = "+AppUtils.getUserSession().getUserid()
				   + "\nAND EXISTS(SELECT * FROM sys_userinrole x WHERE x.isdelete = FALSE AND x.userid = u.id "
				 + "\nAND EXISTS(SELECT * FROM sys_role y WHERE y.id = x.roleid AND (y.code ILIKE 'financial%' OR y.code ILIKE 'Business')))) AS Additionalcost";
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		Boolean isAdditionalcost = m!=null&&m.get("additionalcost")!=null&&m.get("additionalcost").toString().equals("true");
		if (!getCheckright("Additionalcost",AppUtils.getUserSession().getUserid())&&!isAdditionalcost){
//			Browser.execClientScript("$('#j_id81\\:amtcost').hide();$('#j_id81\\:amtcostlable').hide();");
			isadditionalcost = "0";
		}

		try {
			String sys_settlement_method = ConfigUtils.findSysCfgVal("sys_settlement_method");
			if("Y".equalsIgnoreCase(sys_settlement_method)){
				issettlement = true;
			}else{
				issettlement = false;
			}
		} catch (Exception e) {
			issettlement = false;
		}

	}

	private void jobs_lock(){
		try {
			if(jobs != null && jobs.getIslock() != null && jobs.getIslock()){
				addw.setDisabled(true);
				delBat.setDisabled(true);
				showDynamic.setDisabled(true);
				batchedit.setDisabled(true);
				dividedProfit.setDisabled(true);
				batchmodify.setDisabled(true);
				createrCurrent.setDisabled(true);
				//scanReport.setDisabled(true);
				impTemplet.setDisabled(true);
//				importData.setDisabled(true);
				impOtherJobs.setDisabled(true);
				addwin.setDisabled(true);
				addedit.setDisabled(true);
				addBatch.setDisabled(true);
				copyadd.setDisabled(true);
				save.setDisabled(true);
				savewin.setDisabled(true);
				saveadd.setDisabled(true);
				saveclose.setDisabled(true);
				del.setDisabled(true);
			}
		}catch(NoRowException e){
			//System.out.println("NoRowException:FinaJobs");
		} catch(IllegalArgumentException e){
			//System.out.println("IllegalArgumentException:FinaJobs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SaveState
	private boolean isShowJoin = true;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);

		// neo 控制费用显示
		String filterJobs = "(EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"') x WHERE x.id = t.id)" +
				" OR (t.corpid = 157970752274 AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = " + this.jobid + " AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + AppUtils.getUserSession().getUserid() + " and ischoose = true))" +
				"AND (CASE WHEN COALESCE(f_sys_config('fina_arap_feehide'),'N') = 'Y' " +
				"	THEN	" +
				"		(CASE WHEN EXISTS(SELECT 1 FROM dat_feeitem WHERE id = t.feeitemid AND ishidden = TRUE) THEN f_checkright('CheckTheCostOfTheShadow'," + AppUtils.getUserSession().getUserid() + ") > 0 ELSE TRUE END)	" +
				"	ELSE " +
				"		TRUE	" +
				"	END)))";
		//filterJobs += "\nAND (t.jobid = " + this.jobid + " OR EXISTS(SELECT 1 FROM fina_jobs x where x.parentid = " + this.jobid + " AND x.id = t.jobid AND x.isdelete = false))";
		// neo 优化写法 20200331 -- add 途曦主单不需要显示子单费用
		filterJobs += "\nAND t.jobid = ANY(SELECT " + this.jobid + " UNION (SELECT x.id FROM fina_jobs x where x.parentid = " + this.jobid + " AND (x.corpid <> 11540072274 AND x.corpidop <> 11540072274) AND x.isdelete = false))";

		//2024110途曦仓库不允许查看费用明细
		if (AppUtils.getUserSession().getCorpid() == 11540072274L) {
			boolean ishidearap = getCheckright("HideArapProfit", AppUtils.getUserSession().getUserid());
			if (ishidearap) {
				filterJobs += "\nAND FALSE";
			}
		}

		map.put("jobs", filterJobs);

		if (isShowJoin) {// 显示合计，不显示明细
			map.put("isShowJoin", "(t.parentid IS NULL)");
		} else {// 显示明细，不显示合计
			map.put("isShowJoin", "(rptype <> 'O')");
		}
		String orderby = ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime");
		if(StrUtils.isNull(orderby) || "N".equals(orderby)){
			map.put("orderSql", "");
		}else{
			map.put("orderSql", ",t.inputtime");
		}

//		String arapfilter = " EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"') x WHERE x.id = t.id)";
//		map.put("arapfilter", arapfilter);
		return map;
	}

	public String getCustomercode() {
		String sql = "SELECT code ,abbr FROM sys_corporation  WHERE id ="
				+ customerid;
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
//		return (String) list.get(0).get("code") + "/"
//				+ (String) list.get(0).get("abbr");
		return (String) list.get(0).get("code");
	}

	@Action
	public void initArapInfo() {
		String	profitDisclosure = null == ConfigUtils.findSysCfgVal("fina_arap_profit_disclosure") ? "N" : ConfigUtils.findSysCfgVal("fina_arap_profit_disclosure");
		String sql = "\nSELECT f_findarapinfo('jobid=" + this.jobid
				+ ";tax=Y;userid=" + AppUtils.getUserSession().getUserid()
				+ ";currency="+this.currency
				+ ";profitDisclosure=" + profitDisclosure
				+ ";showGpr=" + (this.ishidegpr ? "Y":"N")
				+ "') AS arapinfo";
		try {
			Map m = this.serviceContext.arapMgrService.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			String arapinfo = StrUtils.getMapVal(m, "arapinfo");
			String[] tips = arapinfo.split(",");
			if (tips != null && tips.length == 5) {
				arTip = tips[0];
				apTip = tips[1];
				profitTip = tips[2];
				exchangeTip = tips[3];
				retar_ap = tips[4];
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.update.markUpdate(UpdateLevel.Data, "arTip");
		this.update.markUpdate(UpdateLevel.Data, "apTip");
		this.update.markUpdate(UpdateLevel.Data, "profitTip");
		this.update.markUpdate(UpdateLevel.Data, "exchangeTip");

		String sqlTax = "\nSELECT f_findarapinfo('jobid=" + this.jobid + ";tax=N;userid=" + AppUtils.getUserSession().getUserid() + "') AS arapinfo";
		try {
			Map m = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlTax);
			String arapinfo = StrUtils.getMapVal(m, "arapinfo");
			String[] tips = arapinfo.split(",");
			if (tips != null && tips.length == 4) {
				arTipNoTax = tips[0];
				apTipNoTax = tips[1];
				profitTipNoTax = tips[2];
//				exchangeTip = tips[3];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String lixiSql = "SELECT f_fina_arap_interest("+this.jobid+",'"+this.currency+"') AS lixi";
		try {
			Map liximap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(lixiSql);
			if(liximap != null && liximap.size() > 0){
				lixi = StrUtils.getMapVal(liximap, "lixi");
				lixi = "资金成本\n"+currency + lixi+"\n";
				initCntDesc();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			reloadSelect();
		}

		this.update.markUpdate(UpdateLevel.Data, "arTipNoTax");
		this.update.markUpdate(UpdateLevel.Data, "apTipNoTax");
		this.update.markUpdate(UpdateLevel.Data, "profitTipNoTax");
	}

	protected void initAdd() {
		selectedRowData = new FinaArap();
		this.selectedRowData.setSharetype("N");
//		this.selectedRowData.setFkid(arapfkid);
		this.selectedRowData.setPpcc("PP");
		this.selectedRowData.setJobid(jobid);
		if(currencycyadd == null || currencycyadd.equals("")){
			this.selectedRowData.setCurrency(AppUtils.getUserSession()
					.getBaseCurrency());
		}else{
			this.selectedRowData.setCurrency(currencycyadd);
		}
		this.selectedRowData.setAraptype("AR");
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));
		this.selectedRowData.setPiece(new BigDecimal(1));
		this.selectedRowData.setSharetype("N");
		cost = new BigDecimal(0);

		String payplace = ConfigUtils.findUserCfgVal("fee_add_payplace", AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(payplace)){
			this.selectedRowData.setPayplace(payplace);
		}
//		this.selectedRowData.setPayplace("CN");
		//this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpidCurrent() < 0 ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent());
		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());//neo 20200513 改为取当前用户所在分公司
		setCustomer();
		Browser.execClientScript("$('#feei_input').val('')");
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	/**
	 * 设置费用的结算单位 1581 应付默认结算单位
	 */
	protected void setCustomer(){
		String araptype = this.selectedRowData.getAraptype();
		if(araptype!=null&&araptype.equals("AR")){
			if(null == serviceContext.customerMgrService.findJoinCorporationById(customerid)){
				if (-100l == this.customerid) {
				} else {
					this.selectedRowData.setCustomercode(this.getCustomercode());
					this.selectedRowData.setCustomerid(customerid);
				}
			}else{
				this.selectedRowData.setCustomercode("");
				this.selectedRowData.setCustomerid(-1L);
			}
		}else if(araptype!=null&&araptype.equals("AP")){
			List<Map> coperatin = serviceContext.customerMgrService.findAgentorcarrById(jobid);
			if(coperatin != null&&coperatin.get(0)!=null&&coperatin.get(0).get("id")!=null&&coperatin.get(0).get("abbr")!=null){
				try{
					this.selectedRowData.setCustomercode(coperatin.get(0).get("code").toString()+"/"+coperatin.get(0).get("abbr").toString());
					this.selectedRowData.setCustomerid(Long.parseLong(coperatin.get(0).get("id").toString()));
				}catch(Exception e){
					return;
				}
			}
		}
		this.update.markUpdate(UpdateLevel.Data, "customercode");
	}

	@Action
	public void setCustomerAjax(){
		String araptype = AppUtils.getReqParam("araptype");
		if(araptype!=null){
			this.selectedRowData.setAraptype(araptype);
			setCustomer();
		}
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Action
	public void reloadSelected(){
		reloadSelect();
	}

	@Action
	public void addwin() {
		SysCorporation custmer = serviceContext.customerMgrService.sysCorporationDao.findById(customerid);
		selectedRowData.setCustomercode(custmer.getCode());
		Browser.execClientScript("$('#customer_input').val('"+custmer.getCode()+"')");
		addw();
	}


	@Action
	public void addBatch() {
		String[] ids = null;
		if (ids == null) {
			dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid=" + this.jobid+"&editType=add");
		} else {
			StringBuffer sb = new StringBuffer();
			for (String s : ids) {
				sb.append(s);
				sb.append(",");
			}
			dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid=" + this.jobid
					+ "&arapid=" + sb.substring(0, sb.lastIndexOf(","))+"&editType=add");
		}
		update.markAttributeUpdate(dtlIFrameArap, "src");
		if (batchEditWindow != null)
            batchEditWindow.setTitle("");
			batchEditWindow.show();
	}

	@Bind
	@SaveState
	public Long pkVal;

	@Action
	public void addw() {
		this.pkVal = -1L;
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		initAdd();
	}

	@Action
	public void qryAdd() {
		initAdd();
	}

	@Action
	public void delBat() {
		String[] ids = this.editGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if(StrUtils.isNull(id)){
					id = ids[i];
				}else{
					id += "," + ids[i];
				}
			}
		}
		try {

		    //检查代垫费用是否可以删除
			String sql1 = "SELECT rptype,amount,corpid,currency from fina_arap WHERE isdelete = false AND id in ("+id+")";
			List<Map> list1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql1);
			if(null != list1 && list1.size()>0){
				for (int i = 0; i < list1.size(); i++) {
					for (int j = 0; j < list1.size(); j++) {
						//是否包含代垫费用
						if (list1.get(i).get("rptype").equals("D")) {
								if (list1.size() % 2 == 0) {
									//比较两条代垫费用是否相同
									if (list1.get(i).get("rptype").equals(list1.get(j).get("rptype"))) {
										if (!list1.get(i).get("amount").equals(list1.get(j).get("amount"))
												|| !list1.get(i).get("corpid").equals(list1.get(j).get("corpid"))
												|| !list1.get(i).get("currency").equals(list1.get(j).get("currency"))) {
											MessageUtils.alert("请勾选金额，币种，结算地相同的代垫费用!");
											return;
										}
									} else {
										MessageUtils.alert("请勾选金额，币种，结算地相同的代垫费用!");
										return;
									}
								} else {
									MessageUtils.alert("代垫费用不可单条删除!");
									return;
								}
						}
					}
				}
			}

			String sql = "SELECT id from fina_arap WHERE isdelete = false AND id in ("+id+")";
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(null != list && list.size()>0){
				String[] arapid = new String[list.size()];
				for (int j = 0; j < list.size(); j++) {
					if(null == list.get(j) || null == list.get(j).get("id")){
						continue;
					}
					arapid[j] = String.valueOf(list.get(j).get("id"));
				}
				this.editGrid.setSelectedIds(arapid);
				// auditcancel();	//先注释
			}
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			this.editGrid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
			this.editGrid.rebind();
		}
	}

	@Action
	public void del() {
		try {
			String[] ids = new String[] { String.valueOf(this.pkVal) };
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			this.addw();
			this.editGrid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void copyadd() {
		this.pkVal = -100L;

		FinaArap temp = selectedRowData;

		FinaArap newObj = new FinaArap();
		newObj.setJobid(temp.getJobid());
		newObj.setAraptype(temp.getAraptype());
		newObj.setCustomerid(temp.getCustomerid());
		newObj.setCustomercode(temp.getCustomercode());
//		newObj.setFkid(temp.getId());
		newObj.setFeeitemid(temp.getFeeitemid());
		newObj.setCurrency(temp.getCurrency());
		newObj.setPiece(temp.getPiece());
		newObj.setPpcc(temp.getPpcc());
		newObj.setUnit(temp.getUnit());
		newObj.setCorpid(temp.getCorpid());

		this.selectedRowData = newObj;
		this.update.markUpdate(true, UpdateLevel.Data, "pkId");
		// super.initAdd();
		this.selectedRowData.setAmount(new BigDecimal(0));
		this.selectedRowData.setBillid(null);
		this.selectedRowData.setBillxrate(new BigDecimal(0));
		this.selectedRowData.setBillamount(new BigDecimal(0));

		this.selectedRowData.setPrice(new BigDecimal(0));
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));

		this.update.markUpdate(UpdateLevel.Data, "amount");
		this.update.markUpdate(UpdateLevel.Data, "piece");
		this.update.markUpdate(UpdateLevel.Data, "price");
	}

	@Action
	public void copyaddAdd() {
		initAdd();
	}

	@Action
	public void share() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			this.alert("Please one row!");
			return;
		}
		try {
			String ret = this.serviceContext.arapMgrService.shareFee(ids,
					AppUtils.getUserSession().getUsercode());
			// this.alert("OK!\n"+ret);
			MessageUtils.showMsg("OK!\n" + ret);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}


	@Action
	public void deleteShare() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			this.alert("Please one row!");
			return;
		}
		try {
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder2 = new StringBuilder();

			for (String id : ids) {
				stringBuilder.append("\nUPDATE fina_arap SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE fkid = "+id+" AND isdelete = FALSE AND rptype = 'S';");
				stringBuilder2.append("\nUPDATE fina_arap SET sharetype = 'N' WHERE id = "+id+" AND isdelete = FALSE;");
			}
			this.serviceContext.arapMgrService.finaArapDao.executeSQL(stringBuilder.toString());
			this.serviceContext.arapMgrService.finaArapDao.executeSQL(stringBuilder2.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Bind
	private String qryCustomerKey;

	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		String filter = "";
		String value = ConfigUtils.findSysCfgVal("sys_factoryneedcheck");
		if(value!=null&&value.equals("Y")){
			filter = "\nAND (CASE WHEN isfactory = TRUE THEN ischeck = TRUE ELSE 1=1 END)";//neo 20161118 if factory filter uncheck data
		}
		return this.customerService.getAllCustomerDataProvider(filter);
	}

	@Action
	public void showCustomer() {
		String customercode = (String) AppUtils.getReqParam("customercode");
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			//this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			//customerQry();
			Browser.execClientScript("sc.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
				this.selectedRowData.setCustomerid((Long) cs.get(0).get("id"));
				this.selectedRowData.setCustomercode(cs.get(0).get("code")
						+ "/" + cs.get(0).get("abbr"));
				this.update.markUpdate(UpdateLevel.Data, "customerid");
				this.update.markUpdate(UpdateLevel.Data, "customercode");

				showCustomerWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
				showCustomerWindow.show();
				customerQry();
				Browser.execClientScript("sc.focus");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.selectedRowData.setCustomerid((Long) m.get("id"));
//		String customercode = (String) m.get("code") + "/"
//				+ (String) m.get("abbr");
		String namec = (String) m.get("namec");
		String namee = (String) m.get("namee");
		String customercode = StrUtils.isNull(namec)?namee:namec;
		this.selectedRowData.setCustomercode(customercode);
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customercode");
		showCustomerWindow.close();
	}

	@Action
	public void refreshAjaxSubmit() {
		this.refresh();
	}

	protected void doServiceFindData() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT");
		sql.append(" (CASE WHEN (SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND parentid = t.id LIMIT 1) = 1 THEN (t.amount+a.amount2)/t.piece ELSE t.price END) AS pricecount");
		sql.append(",(CASE WHEN (SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND parentid = t.id LIMIT 1) = 1 THEN (t.amount+a.amount2) ELSE t.amount END) AS amountcount");
		sql.append(",(CASE WHEN (SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND parentid = t.id LIMIT 1) = 1 THEN t.amount ELSE 0 END) AS costcount");
		sql.append(",(CASE WHEN (SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND parentid = t.id LIMIT 1) = 1 THEN a.amount2 ELSE 0 END) AS amtcostcount");
		sql.append(",(CASE WHEN (SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND parentid = t.id LIMIT 1) = 1 THEN ((t.amount+a.amount2)/t.piece)/t.taxrate ELSE t.price/t.taxrate END) AS pricenotax");
		sql.append(",t.*");
		sql.append(" FROM _fina_arap t ");
		sql.append(" LEFT JOIN (SELECT parentid AS parentid2,amount AS amount2 FROM fina_arap WHERE isdelete = false AND parentid > 0) a ON a.parentid2 = t.id");
		sql.append(" WHERE isdelete = false AND id ="+this.getGridSelectId()+" limit 1;");
		List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql.toString());
		Map map = list.get(0);
		SimpleDateFormat sp = new SimpleDateFormat("YYYY-MM-DD");
		if(null != map && map.size()> 0){
			FinaArap newRowData = new FinaArap();
			newRowData.setId(Long.valueOf(String.valueOf(map.get("id"))));
			newRowData.setAraptype(String.valueOf(map.get("araptype")));
			newRowData.setPpcc(String.valueOf(map.get("ppcc")));
			newRowData.setRptype(String.valueOf(map.get("rptype")));
			newRowData.setCustomerid(Long.valueOf(String.valueOf(map.get("customerid"))));
			newRowData.setCustomercode(String.valueOf(map.get("customercode")));
			try {
				newRowData.setArapdate(new Date());
				newRowData.setInputtime(new Date());
				newRowData.setUpdatetime("null".equals(String.valueOf(map.get("updatetime")))?new Date():sp.parse(String.valueOf(map.get("updatetime"))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			newRowData.setFeeitemid(Long.valueOf(String.valueOf(map.get("feeitemid"))));
			newRowData.setPrice(BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("pricecount")))));
			newRowData.setPiece(BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("piece")))));
			newRowData.setAmount(BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("amountcount")))));
			newRowData.setCurrency(String.valueOf(map.get("currency")));
			cost = BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("costcount"))));
			newRowData.setAmtcost(BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("amtcostcount")))));
			newRowData.setUnit(String.valueOf(map.get("unit")));
			newRowData.setCorpid(Long.valueOf(String.valueOf(map.get("corpid"))));
			newRowData.setSharetype(String.valueOf(map.get("sharetype")));
			newRowData.setPricenotax(BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("pricenotax")))));
			newRowData.setTaxrate(BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("taxrate")))));
			newRowData.setJobid(Long.valueOf(String.valueOf(map.get("jobid"))));
			newRowData.setIsamend(Boolean.valueOf(String.valueOf(map.get("isamend"))));
			newRowData.setInputer("null".equals(String.valueOf(map.get("inputer")))?AppUtils.getUserSession().getUsercode():String.valueOf(map.get("inputer")));
			newRowData.setUpdater("null".equals(String.valueOf(map.get("updater")))?AppUtils.getUserSession().getUsercode():String.valueOf(map.get("updater")));

			this.selectedRowData = newRowData;
		}
	}

	@Action
	public void savewin(){
		try {
			if(!splitArap()){
				return;
			}
//			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
			this.addw();
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
	}

	protected void doServiceSave() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		SysCorporation custmer = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		selectedRowData.setCustomercode(StrUtils.isNull(custmer.getAbbr())?custmer.getCode():custmer.getAbbr());
		//系统设置中结算方式必填
		String sql = "SELECT EXISTS(SELECT * FROM sys_config WHERE key = 'sys_settlement_method' AND val = 'Y') AS issettlement;";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		boolean issettlement = Boolean.parseBoolean(m.get("issettlement").toString());
		if(issettlement&&StrUtils.isNull(selectedRowData.getPayplace())){
			alert("结算方式必填");
			return;
		}
		Long feeId = serviceContext.arapMgrService.saveDataReturnId(selectedRowData);

        //弹窗新增检查是否内部关联，是的话提示自动生成往来
        String feeSql = "SELECT (EXISTS(SELECT 1 FROM fina_corp WHERE jobid = " + this.jobid + " AND corpid = (SELECT customerid FROM fina_arap WHERE id = " + feeId + " LIMIT 1)) OR\n" +
                " EXISTS(SELECT 1 FROM fina_jobs WHERE id = " + this.jobid + " AND corpid = (SELECT customerid FROM fina_arap WHERE id = " + feeId + " LIMIT 1))) AS isrelated;";
        Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(feeSql);

        if (map.get("isrelated").toString().equals("true")) {

            String sql1 = "SELECT string_agg(id::VARCHAR, ',') as feeids FROM fina_arap WHERE jobid = " + this.jobid + "" +
                    " AND (customerid in (SELECT corpid FROM fina_corp where jobid = " + this.jobid + ") OR customerid = " + this.jobs.getCorpid() + ")" +
                    " AND fkid ISNULL AND rptype <> 'O' AND isdelete = false AND inputtime >= CURRENT_TIMESTAMP - interval '1 minutes';";
            Map feeMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);

            feeIds = feeMap.get("feeids").toString();

            Browser.execClientScript("setTimeout('expenseFee()','1000')");
        }

		// initArapInfo();
	}

	/**
	 * 费用名称
	 *
	 * @return
	 */
	@Bind(id = "feeitemtype")
	public List<SelectItem> getFeeitemtype() {
		try {
			if (null == iswarehouse) {
				iswarehouse = "N";
			}
			MLType mlType = AppUtils.getUserSession().getMlType();
			return CommonComBoxBean
					.getComboxItems(
							"d.id",
							mlType.equals(LMapBase.MLType.en)?"COALESCE(d.code,'')||'/'||COALESCE(d.namee,'')":"COALESCE(d.code,'')||'/'||COALESCE(d.name,'')",
							"dat_feeitem AS d",
							"WHERE isdelete = false AND ( iswarehouse = '"
									+ iswarehouse
									+ "' OR ispublic = TRUE ) AND NOT EXISTS(SELECT 1 FROM dat_feeitem_join x WHERE x.idfm = d.id AND x.jointype = 'J')",
							"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	public void ppAjaxSubmit() {
		Boolean isSubmit = jobs.getIsconfirm_pp();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm_pp = TRUE ,confirm_pp_time = NOW(),confirm_pp_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm_pp = FALSE ,confirm_pp_time = NOW(),confirm_pp_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void isconAjax() {
		Boolean isSubmit = jobs.getIsconfirm_bus();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm_bus = TRUE ,confirm_bus_time = NOW(),confirm_bus_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm_bus = FALSE ,confirm_bus_time = NOW(),confirm_bus_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void ccAjaxSubmit() {
		Boolean isSubmit = jobs.getIsconfirm_cc();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm_cc = TRUE ,confirm_cc_time = NOW(),confirm_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm_cc = FALSE ,confirm_cc_time = NOW(),confirm_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			if (!StrUtils.isNull(processInstanceId)
					&& !StrUtils.isNull(workItemId)) {
				//this.affirmNoCC();
			} else {
				MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
			}
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void arAjaxSubmit() {
		Boolean isSubmit = jobs.getIscomplete_ar();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		String sqlchild = "";
		String sqlchildap = "";
		try {

			//途曦子单检查是否完成应收
			if ((jobs.getCorpid() == 11540072274L || jobs.getCorpidop() == 11540072274L) && jobs.getParentid() == null && !jobs.getNos().contains("TAE") && isSubmit) {
				String querySql = "SELECT EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = "+jobs.getId()+" AND iscomplete_ar = FALSE) AS isexit;";
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				if (StrUtils.getMapVal(map, "isexit").equals("true")) {
					MessageUtils.alert("H单存在应收费用未完成，请检查！");
					return;
				}
			}

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET iscomplete_ar = TRUE ,complete_ar_time = NOW(),complete_ar_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";

				//途曦M单勾选应收确认同步H单
				sqlchild = "UPDATE fina_jobs SET iscomplete_ar = TRUE ,complete_ar_time = NOW(),complete_ar_user = '"
						+ updater + "' WHERE nos ILIKE 'TAE%' AND iscomplete_ar = FALSE AND parentid =" + this.jobid + ";";

				serviceContext.jobsMgrService.finaJobsDao.executeSQL(sqlchild);
			} else {
				sql = "UPDATE fina_jobs SET iscomplete_ar = FALSE ,complete_ar_time = NOW(),complete_ar_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}

			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void apAjaxSubmit() {
		Boolean isSubmit = jobs.getIscomplete_ap();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		String sqlchild = "";
		try {

			//途曦子单检查是否完成应付
			if ((jobs.getCorpid() == 11540072274L || jobs.getCorpidop() == 11540072274L) && jobs.getParentid() == null && !jobs.getNos().contains("TAE") && isSubmit) {
				String querySql = "SELECT EXISTS(SELECT 1 FROM fina_jobs WHERE parentid = "+jobs.getId()+" AND iscomplete_ap = FALSE) AS isexit;";
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				if (StrUtils.getMapVal(map, "isexit").equals("true")) {
					MessageUtils.alert("H单存在应付费用未完成，请检查！");
					return;
				}
			}

			if (isSubmit) {
				sql = "UPDATE fina_jobs SET iscomplete_ap = TRUE ,complete_ap_time = NOW(),complete_ap_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
				//途曦M单勾选同步H单
				sqlchild = "UPDATE fina_jobs SET iscomplete_ap = TRUE ,complete_ap_time = NOW(),complete_ap_user = '"
						+ updater + "' WHERE nos ILIKE 'TAE%' AND iscomplete_ap = FALSE AND parentid =" + this.jobid + ";";

				serviceContext.jobsMgrService.finaJobsDao.executeSQL(sqlchild);

			} else {
				sql = "UPDATE fina_jobs SET iscomplete_ap = FALSE ,complete_ap_time = NOW(),complete_ap_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}

			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void pp2AjaxSubmit() {
		Boolean isSubmit = jobs.getIsconfirm2_pp();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm2_pp = TRUE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm2_pp = FALSE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void amendAjaxSubmit() {
		Boolean isSubmit = jobs.getIscomplete_amend();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET iscomplete_amend = TRUE ,complete_amend_time = NOW(),complete_amend_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET iscomplete_amend = FALSE ,complete_amend_time = NOW(),complete_amend_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void lossAjaxSubmit() {
		Boolean isSubmit = jobs.getIscomplete_loss();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET iscomplete_loss = TRUE ,updater = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET iscomplete_loss = FALSE ,updater = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void cc2AjaxSubmit() {
//		if (!StrUtils.isNull(processInstanceId) && !StrUtils.isNull(workItemId)
//				&& this.jobs.getIsconfirm_cc() == false) {
//			MessageUtils.alert("客服CC尚未确认，不能完成此操作!");
//			return;
//		}
		Boolean isSubmit = jobs.getIsconfirm2_cc();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isSubmit) {
				sql = "UPDATE fina_jobs SET isconfirm2_cc = TRUE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirm2_cc = FALSE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			if (!StrUtils.isNull(processInstanceId)
					&& !StrUtils.isNull(workItemId)) {
				//this.affirmNoCC2(this.jobid, WorkFlowEnumerateShip.CC_QUERY);
			} else {
				MessageUtils.alert(isSubmit ? "Confirm OK!" : " Cancel OK!");
			}
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Bind
	@SaveState
	private String qryFeeitemKey;

	@Action
	public void feeitemQry() {
		this.customerService.qryFeeitem(qryFeeitemKey);
		this.feeitemGrid.reload();
	}

	@Bind
	public UIWindow showFeeitemWindow;

	@Bind
	public UIDataGrid feeitemGrid;

	@Bind(id = "feeitemGrid", attribute = "dataProvider")
	public GridDataProvider getFeeitemGridDataProvider() {
		//"\nAND COALESCE(iswarehouse,'N')='" + iswarehouse+ "'";
		return this.customerService.getFeeitemDataProvider("");
	}

	@Action
	public void showFeeitem() {
		String feeitemcode = (String) AppUtils.getReqParam("feeitemcode");
		qryFeeitemKey = feeitemcode;
		int index = qryFeeitemKey.indexOf("/");
		if (index > 1)
			qryFeeitemKey = qryFeeitemKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryFeeitemKey");
			showFeeitemWindow.show();
			feeitemQry();
			Browser.execClientScript("sf.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findFeeitem(qryFeeitemKey,
					iswarehouse);
			if (cs.size() == 1) {
				Map map = cs.get(0);
				this.selectedRowData.setFeeitemid((Long) map.get("id"));
				this.update.markUpdate(UpdateLevel.Data, "feeitemid");
				showFeeitemWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryFeeitemKey");
				showFeeitemWindow.show();
				feeitemQry();
				Browser.execClientScript("sf.focus");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void feeitemGrid_ondblclick() {
		Object[] objs = feeitemGrid.getSelectedValues();
		Map map = (Map) objs[0];
		this.selectedRowData.setFeeitemid(((Long) map.get("id")));
		autoUnit();
		this.update.markUpdate(UpdateLevel.ValueOnly, "feeitemid");
		showFeeitemWindow.close();
	}

	@Bind
	public UIWindow showOtherJobFeeWindow;

	@Bind
	public UIIFrame otherJobFeeFrame;

	@Action
	public void impTemplet() {
		// templetFrame.load("../other/feetemplatechooser.xhtml?jobid="+this.jobid);
		// showTempletWindow.show();
		String winId = "_arapTemplet";
		String url = "../other/feetemplatechooser.xhtml?jobid=" + this.jobid;
		// int width = 980;
		// int height = 600;
		AppUtils.openWindow(winId, url);
	}

	@Action
	public void impOtherJobs() {
		otherJobFeeFrame.load("../other/arapchooser.xhtml?jobid=" + this.jobid);
		showOtherJobFeeWindow.show();
	}

	/*
	 * 保存新增
	 */
	@Action
	protected void saveadd() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		try {
			if(!splitArap()){
				return;
			}
//			this.serviceContext.arapMgrService.saveData(selectedRowData);
			refresh();
			addw();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/*
	 * 保存并关闭
	 */
	@Action
	protected void saveclose() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		try {
			if(!splitArap()){
				return;
			}
//			this.serviceContext.arapMgrService.saveData(selectedRowData);
			refresh();
			this.editWindow.close();
			checkFeei();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	private BigDecimal cost;//成本
	/**
	 * 用于有额外成本的费用拆分成主费用和额外费用
	 */
	public boolean splitArap(){

		if (selectedRowData.getRptype()!=null && selectedRowData.getRptype().equals("D")) {
			MessageUtils.alert("代垫费用请在 其他-代垫费用申请 进行修改!");
			return false;
		}

		if(selectedRowData.getAmount().compareTo(BigDecimal.valueOf(0)) == 1){
			if(selectedRowData.getAmount().compareTo(cost)!=1 || cost.compareTo(BigDecimal.valueOf(0)) == -1){
				MessageUtils.alert("成本填写错误!");
				return false;
			}
		}
		if(selectedRowData.getAmount().compareTo(BigDecimal.valueOf(0)) == -1){
			if(selectedRowData.getAmount().compareTo(cost)==1){
				MessageUtils.alert("成本填写错误!");
				return false;
			}
		}

		//先检查是否存在额外费用，有就清除，方便后面新增
		String sql = "UPDATE fina_arap SET isdelete = TRUE,updater = '"+AppUtils.getUserSession().getUsercode()+"',updatetime=NOW() WHERE isdelete = FALSE AND parentid =" +selectedRowData.getId();
		try {
			this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return false;
		}
		if(cost.compareTo(BigDecimal.valueOf(0)) == 0){//如果成本为0，则不拆分费用直接保存
			selectedRowData.setIsdelete(false);
			doServiceSave();
			return true;
		}

		//取出需要还原的原始数据
		BigDecimal amount 	= selectedRowData.getAmount();		//原费用金额
		BigDecimal amtcost 	= amount.subtract(cost);			//原费用额外成本
		BigDecimal price 	= selectedRowData.getPrice();		//原费用单价
		BigDecimal piece 	= selectedRowData.getPiece();		//原费用数量
		Long customerid 	= selectedRowData.getCustomerid();	//原费用客户ID
		BigDecimal pricenotax 	= selectedRowData.getPricenotax();//税前单价
		BigDecimal taxrate = selectedRowData.getTaxrate();//税率
		BigDecimal billamount = selectedRowData.getBillamount();

		//拆分额外成本后生成新的费用
		BigDecimal price2 = cost.divide(piece, 3,BigDecimal.ROUND_HALF_UP);
		selectedRowData.setAmount(cost);//金额
		selectedRowData.setPrice(price2);//价格
		selectedRowData.setAmtcost(BigDecimal.valueOf(0d));
		selectedRowData.setIsdelete(false);
		selectedRowData.setPricenotax(price2.multiply(taxrate));//税前单价=单价*税率
		selectedRowData.setBillamount(cost);
		try {
			//新的费用新增
			System.out.print(selectedRowData.toString());
			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return false;
		}

		Long id = selectedRowData.getId();//费用行ID

		//新增额外成本费用数据填充
		selectedRowData.setParentid(selectedRowData.getId());//父ID
		selectedRowData.setAmount(amtcost);//金额
		selectedRowData.setPrice(amtcost);//价格
		selectedRowData.setPiece(BigDecimal.valueOf(1));//数量
		selectedRowData.setCustomerid(Long.valueOf("25322452274"));//GLOBESKY
		selectedRowData.setId(Long.valueOf(0));
		selectedRowData.setPricenotax(amtcost);
		selectedRowData.setTaxrate(BigDecimal.valueOf(1));
		selectedRowData.setBillamount(amtcost);
		try {
			//新增额外成本费用
			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return false;
		}

		//还原数据行，用于刷新页面不影响回显
		selectedRowData.setParentid(null);//父ID
		selectedRowData.setAmount(amount);//金额
		selectedRowData.setPrice(price);//价格
		selectedRowData.setPiece(piece);//数量
		selectedRowData.setCustomerid(customerid);//客户
		selectedRowData.setId(id);
		selectedRowData.setAmtcost(amtcost);
		selectedRowData.setPricenotax(pricenotax);
		selectedRowData.setTaxrate(taxrate);
		selectedRowData.setBillamount(billamount);
		return true;
	}

	@Override
	public void refresh() {
		this.editGrid.reload();
		initArapInfo();
		initCntDesc();
		checkFeei();
	}

	@Bind
	public UIWindow showbatchupdateWindow;

	@Bind
	public UIIFrame batchupdateFrame;



	@Action
	public void batchmodify() {
		 String[] ids = this.editGrid.getSelectedIds();
		 if (ids == null ||ids.length == 0) {
			 MessageUtils.alert("请选择一条记录");
		 } else {
			 StringBuilder sb = new StringBuilder();
			 for (int i = 0; i <ids.length; i++) {
				 if (i == (ids.length - 1)) {
					 sb.append(ids[i]);
				 }else {
					 sb.append(ids[i] + ",");
				 }
				 if(ids[i].indexOf("ext")>-1){//说明列表还没有保存
					 MessageUtils.alert("请先保存列表再批量修改！");
					 return;
				 }
			 }

			 String sql = "SELECT EXISTS(SELECT 1 FROM fina_arap WHERE rptype = 'D' AND isdelete = FALSE AND id in ( " + sb.toString() + ")) as isret;";
			 Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
			 if (s.get("isret").toString().equals("true")) {
				 MessageUtils.alert("代垫费用请在 其他-代垫费用申请 进行修改!");
				 editGrid.reload();
				 return;
			 }

			 showbatchupdateWindow.show();
			 batchupdateFrame.load("./batchupdate.xhtml?iswarehouse=" +this.iswarehouse + "&ids=" + sb.toString()+"&jobid="+this.jobid+"");
		 }
	}

	@Action
	public void batchedit() {
//		 grid.selectedIds = null;
		 String[] ids = this.editGrid.getSelectedIds();
//		String[] ids = null;
		// String[] ids =
		// (java.lang.String[])GenComponentUtils.getValueFromExpression(this.grid,
		// "selectedIds");
		if (ids == null || ids.length == 0) {
			dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid=" + this.jobid);
		} else {
			StringBuffer sb = new StringBuffer();
			for (String s : ids) {
				sb.append(s);
				sb.append(",");
			}
			dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid=" + this.jobid
					+ "&arapid=" + sb.substring(0, sb.lastIndexOf(",")));
		}
		update.markAttributeUpdate(dtlIFrameArap, "src");
		if (batchEditWindow != null)
            batchEditWindow.setTitle("");
			batchEditWindow.show();
	}

	public void closeShowbatchupdateWindow() {
		showbatchupdateWindow.close();
	}

	@Action(id = "showbatchupdateWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}

	@Action
	public void hideShare() {
		this.qryMap.put("rptype", "G");
		this.editGrid.reload();
	}

	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "arap_jobs.raq";

	@Action
	public void scanReport() {
		if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
			MessageUtils.alert("请选择格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		for (String s : AppUtils.getUserRoleModuleCtrl(900000L)){
			if (s.endsWith("eportdesign")) {
				String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?design=true&raq=/static/arap/"
				+ showwmsinfilename;
				AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
						+ "&userid="
						+ AppUtils.getUserSession().getUserid()
						+"&language="+AppUtils.getUserSession().getMlType().toString());
				return;
			}
		}

		String openUrl = rpturl
		+ "/reportJsp/showReport.jsp?design=false&raq=/static/arap/"
		+ showwmsinfilename;
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
				+ "&userid="
				+ AppUtils.getUserSession().getUserid()
				+"&language="+AppUtils.getUserSession().getMlType().toString());
	}

	private String getArgs() {
		String args = "";
		args += "&id=" + this.jobid;
		return args;
	}

	@Inject(value = "l")
	private MultiLanguageBean l;


	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			if(l.m.getMlType()==MLType.en){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namee",
						"sys_report AS d", " WHERE modcode = 'jobarap' AND isdelete = FALSE",
						"order by filename");
			}else if(l.m.getMlType()==MLType.ch){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'jobarap' AND isdelete = FALSE",
						"order by filename");
			}else{
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'jobarap' AND isdelete = FALSE",
						"order by filename");
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Bind
	@SaveState
	public String taskname;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();

	@Bind
	public UIDataGrid gridUser;

	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				Object[] obj = serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere2(qryMapUser), start, limit)
				.toArray();
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
	public void applyBPMform() {
		try {
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
			String processCode = "BPM-C4C22BEC";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+this.selectedRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8";
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

	@Bind
	@SaveState
	public String qryuserdesc = "";

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		qrycustomerdesc = StrUtils.getSqlFormat(qrycustomerdesc);
		qrycustomerdesc = qrycustomerdesc.toUpperCase();
		if(!StrUtils.isNull(qrycustomerdesc) ){
			qry += "AND (code ILIKE '%"+qrycustomerdesc+"%' OR namec ILIKE '%"+qrycustomerdesc+"%' OR namee ILIKE '%"+qrycustomerdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}

	@Bind
	@SaveState
	public String user = "";

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

	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-C4C22BEC";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				if("提交".equals(map.get("taskname"))){
					continue;
				}
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
	public String nextAssignUser = "";

	@Bind
	@SaveState
	public String arapselected = "";

	@Bind
	@SaveState
	public String amendfeeiinfo = "";
	@Bind
	@SaveState
	public String checkfeeistatus = "";			//检查往来费用是否正确

	@Bind
	@SaveState
	public String bpmremark = "";
	/**
	 * 账单审核
	 */
	@Action
	public void bpmBillReview(){
		String[] ids = this.editGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if("".equals(id)){
					id = ids[i];
				}else{
					id += ","+ids[i];
				}
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("	(CASE WHEN	EXISTS	(SELECT 1 FROM bus_shipping WHERE jobid = a.jobid AND isdelete = FALSE) THEN	(SELECT mblno FROM bus_shipping WHERE jobid = a.jobid AND isdelete = FALSE	LIMIT 1) ");
		sql.append("																	WHEN EXISTS	(SELECT 1 FROM bus_air WHERE jobid = a.jobid AND isdelete = FALSE) THEN	(SELECT mawbno FROM bus_air WHERE jobid = a.jobid AND isdelete = FALSE	LIMIT 1) ");
		sql.append("																			ELSE	(SELECT mblno FROM bus_train WHERE jobid = a.jobid AND isdelete = FALSE	LIMIT 1)	END)	AS mblno ");
		sql.append(",a.isconfirm,(SELECT jobtype FROM fina_jobs WHERE isdelete = FALSE AND id = "+jobid+") AS jobtype FROM fina_arap a WHERE a.id in ("+id+") AND a.isdelete = FALSE;");
		String jobtype = "";
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql.toString());

			for (int i = 0; i < list.size(); i++) {
				if("".equals(list.get(i).get("mblno"))){
					this.alert("MBL提单号码不能为空！");
					return;
				}
				if("true".equals(list.get(i).get("mblno"))){
					this.alert("提交费用含有已确认费用！");
					return;
				}
				jobtype = String.valueOf(list.get(i).get("jobtype"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
			String processCode = "BPM-C4C22BEC";
			if("A".equals(jobtype)){
				processCode = "BPM-7A8B7ED3";
			}else if("H".equals(jobtype)){
				processCode = "BPM-989EA88C";
			}
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks=待审核费用ID："+id+";taskname="+taskname+";refno="+this.jobs.getNos()+";refid="+this.jobs.getId()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
			String sub =  sm.get("rets").toString();
			MessageUtils.alert("OK");
			Browser.execClientScript("bpmWindowJsVar.hide();");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}


	/**
	 * 确认费用
	 */
	@Action
	public void confirmArAp() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "";
		for (String id : ids) {
			sql += "\nUPDATE fina_arap SET isconfirm = TRUE,confirmer = '"
					+ usercode
					+ "',confirmtime = NOW() "
					+",updater='"+usercode+"',updatetime = NOW()"
					+"WHERE isdelete = FALSE AND COALESCE(isconfirm,FALSE) = FALSE AND id = "
					+ id + ";";
		}
		if (!StrUtils.isNull(sql)) {
			try {
				this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);

				String emailSql = "\nSELECT f_sys_mail_generate('type=isconfirm_bus;id=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + "');";
				List list = this.serviceContext.arapMgrService.finaArapDao.executeQuery(emailSql);

				Browser.execClientScript("showmsg()");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/**
	 * 取消确认
	 */
	@Action
	public void cancelArAp() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "";
		for (String id : ids) {
			sql += "\nUPDATE fina_arap SET isconfirm = FALSE,confirmer = '"
					+ usercode
					+ "',confirmtime = NOW()"
					+ ",updater='"+usercode+"',updatetime = NOW()"
					+ " WHERE isdelete = FALSE AND id = "
					+ id + ";";
		}
		if (!StrUtils.isNull(sql)) {
			try {
				this.serviceContext.arapMgrService.finaArapDao.executeSQL(sql);
				Browser.execClientScript("showmsg()");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/**
	 * BMS付款申请
	 */
	@Action
	public void bmsarappayapply(){
		String[] ids = this.editGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if("".equals(id)){
					id = ids[i];
				}else{
					id += ";"+ids[i];
				}
			}
		}

		//世倡系统必须商务审核后才能发起付款申请
		if("2274".equals(CSNO) || "8888".equals(CSNO)){
			String sql = "SELECT EXISTS (SELECT 1 FROM fina_arap a WHERE a.id in ("+id+") AND a.isdelete = FALSE AND a.isconfirm = false AND EXISTS (SELECT * FROM fina_jobs WHERE id = a.jobid AND isdelete = FALSE AND isconfirm_bus = false))";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map.containsKey("exists") == false || "true".equals(String.valueOf(map.get("exists")))){
					MessageUtils.alert("必须商务审核或费用确认才能生成付款申请！");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String url = ConfigUtils.findSysCfgVal("sys_bms_expense_url");//获取系统配置UMS URL
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try{
			audit();//付款申请前先提交费用审核
			synUfmsAndBms.expense(url+id);//跳转BMS请款 各种跳转URL大体一致，param代表各种类型，比如请款、开票，类型不同systemid参数名与值不同
		}catch(Exception e){
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
		}
	}

	/**
	 * BMS费用审核
	 */
	@Action
	public void audit() {
		String[] ids = null;
		if(null != idByBill && idByBill.length > 0){
			ids = idByBill;
		}else{
			ids = this.editGrid.getSelectedIds();
		}
		String id = "";
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if(StrUtils.isNull(id)){
					id = ids[i];
				}else{
					id += "," + ids[i];
				}
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.jobid,");
		sql.append("a.id AS bizSystemFreightId,a.araptype AS rpFlag,(SELECT cc.custcode FROM sys_corpext cc WHERE cc.customerid = c.id) AS settleCustCode,c.namec AS settleCustName");
		sql.append(",f.freightType,f.freightCode,f.name AS freightNameCn,a.piece AS quantity,a.price AS unitPrice");
		sql.append(",a.currency AS settleCurrencyCode,a.amount,a.bcfreightid,a.isamend,a.inputer AS creator,a.inputtime AS creator");
		sql.append(",TO_CHAR(a.inputtime, 'yyyyMMddHHmmss') AS createTime");
		sql.append(",(select (SELECT xxx.mzccode FROM sys_corpext xxx WHERE xxx.customerid = xx.id) from sys_corporation xx WHERE xx.id = a.corpid AND xx.isdelete = false) AS settleOffice");
		sql.append(",(select xx.namec from sys_corporation xx WHERE xx.id = a.corpid AND xx.isdelete = false) AS settleOfficeName");
		sql.append(",(CASE WHEN a.currency='CNY' THEN 1 ELSE (SELECT e.rate FROM dat_exchangerate e WHERE e.currencyfm = a.currency AND e.currencyto = 'CNY' AND e.isdelete= FALSE AND e.datato > a.arapdate ORDER BY e.updatetime DESC LIMIT 1) END) AS exchangeRate ");
		sql.append(",(a.amount*(CASE WHEN a.currency='CNY' THEN 1 ELSE (SELECT e.rate FROM dat_exchangerate e WHERE e.currencyfm = a.currency AND e.currencyto = 'CNY' AND e.isdelete= FALSE AND e.datato > a.arapdate ORDER BY e.updatetime DESC LIMIT 1) END))::numeric(18,2) AS baseCurrencyValue");
		sql.append(" FROM fina_arap a  left join dat_feeitem f ON (a.feeitemid = f.id AND f.isdelete = false),sys_corporation c ");
		sql.append(" WHERE a.isdelete = FALSE AND c.isdelete = FALSE AND c.id = a.customerid AND (a.id in ("+id+") OR a.parentid in("+id+"));" );

		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT ");
		sql2.append("j.id AS publicBusinessOrderId,j.jobtype AS publicBusinessType,j.nos AS publicJobNo,(SELECT xxx.mzccode FROM sys_corpext xxx WHERE xxx.customerid = c.id) AS publicSettleOffice,c.namec AS publicSettleOfficeName,j.inputer AS creator");
		sql2.append(",TO_CHAR(j.jobdate,'yyyyMMddHHmmss') AS createTime,TO_CHAR(j.inputtime,'yyyyMMddHHmmss') AS publicBusinessDate,j.jobtype AS publicBusinessTypeName");
		sql2.append(",'null' AS cimcID,'null' AS cimcNo,j.impexp AS businessType,s.mblno AS mblNo,TO_CHAR(NOW(),'yyyyMMddHHmmss') AS auditdate");
//		sql2.append(",j.bcpublicorderId AS cimcID,'null' AS cimcNo,j.impexp AS businessType,s.mblno AS mblNo,TO_CHAR(NOW(),'yyyyMMddHHmmss') AS auditdate");
		sql2.append(",TO_CHAR(j.jobdate,'yyyyMMddHHmmss') AS jobdates,d.mzccode AS settleOfficeDeptCode,d.name AS settleOfficeDeptName");
		sql2.append(" FROM fina_jobs j LEFT JOIN sys_department d ON (d.isdelete = FALSE AND d.id = (SELECT deptid FROM sys_user WHERE isdelete = FALSE AND id = j.saleid))");
		sql2.append(",sys_corporation c,bus_shipping s");
		sql2.append(" WHERE j.isdelete = FALSE AND c.isdelete = FALSE AND s.isdelete = FALSE AND s.jobid = j.id AND c.id = j.corpid AND j.id in (SELECT jobid FROM fina_arap where id in ("+id+"))");
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try {
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql.toString());
			List<Map> list2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql2.toString());
			synUfmsAndBms.synArapInfoToBms(list, list2, serviceContext);
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
		}
		if(null == idByBill){
			this.refresh();
		}
	}

	/**
	 * BMS费用取消审核（待测试，代码暂时上传）
	 */
	@Action
	public void auditcancel() {
		String[] ids = this.editGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if(StrUtils.isNull(id)){
					id = ids[i];
				}else{
					id += "," + ids[i];
				}
			}
		}
		String sql = "SELECT a.id,a.bcfreightid FROM fina_arap a WHERE a.isdelete = FALSE AND (a.id in ("+id+") OR a.parentid in("+id+"));";
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try {
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);

			synUfmsAndBms.cancelAudit(list, serviceContext);
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
		}
		this.refresh();
	}

	/**
	 * 订单同步BMS（待测试，代码暂时上传）
	 */
	@Action
	public void orderSyn() {
		if (jobid == null || jobid<0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT a.id FROM fina_arap a WHERE a.isdelete = FALSE AND a.bcfreightid is not null AND a.jobid =" + jobid + "LIMIT 1;");
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2.toString());
			String[] value = {""};
			if(null != map && map.size() > 0){
				value[0] = String.valueOf(map.get("id"));
			}
			this.editGrid.setSelectedIds(value);
			audit();
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
		}
		this.refresh();
	}

	/**
	 * 根据URL地址调用BMS接口
	 */
	public String requestUrl(String urlstr){
		String result = "";
        try {
            URL url = new URL(urlstr);
            //得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //防止中文乱码
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            //设置请求方式
            connection.setRequestMethod("POST");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            //得到响应流
            InputStream inputStream = connection.getInputStream();
            //获取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            if(responseCode == HttpURLConnection.HTTP_OK){
                while ((line = reader.readLine()) != null){
                    result +=line + "\n";
                }

                reader.close();
                //断连接
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * http post 请求raw
     */
    public String httpPost(String url, String rawBody){

        HttpURLConnection conn = null;
        PrintWriter pw = null ;
        BufferedReader rd = null ;
        StringBuilder sb = new StringBuilder ();
        String line = null ;
        String response = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.print(rawBody);
            pw.flush();
            rd  = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null ) {
                sb.append(line);
            }
            response = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            MessageUtils.alert(e.getMessage());
        }finally{
            try {
                if(pw != null){
                    pw.close();
                }
                if(rd != null){
                    rd.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public void checkNull(Object obj, Object paramName){
    	if(obj == null || StrUtils.isNull(String.valueOf(obj))){
    		MessageUtils.alert(paramName +" cannot be empty!");
			return;
    	}
    }

	/*
	 * 确认/取消业务报告
	 */
	@Action
	public void rptAjaxSubmit() {
		Boolean isconfirmrpt = jobs.getIsconfirmrpt();
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		try {
			if (isconfirmrpt) {
				sql = "UPDATE fina_jobs SET isconfirmrpt = TRUE ,isconfirmrpt_time = NOW(),isconfirmrpt_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			} else {
				sql = "UPDATE fina_jobs SET isconfirmrpt = FALSE ,isconfirmrpt_time = NOW(),isconfirmrpt_user = '"
						+ updater + "' WHERE id =" + this.jobid + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			MessageUtils.alert(isconfirmrpt ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally {
			jobs = serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void createreqbill() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		try {
			Long reqid = serviceContext.jobsMgrService.creareReqBill(ids,
					usercode);
			AppUtils.openWindow("_newpayapplyedit",
					"./payapplyedit.xhtml?type=edit&id=" + reqid
							+ "&customerid=-1");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void chooseBusUnit() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		AppUtils.openWindow("_newChooseBusUnit", AppUtils.getContextPath()
				+ "/pages/sysmgr/unit/busunitchooser.xhtml?id="
				+ StrUtils.array2List(ids) + "&customerid=-1", 500, 380);
	}

	@Action
	public void showDynamic() {
//		String rpturl = AppUtils.getRptUrl();
//		String openUrl = rpturl
//				+ "/reportJsp/showReport.jsp?raq=/bus/feeprofit-en.raq";
//		AppUtils.openWindow("_showDynamic", openUrl + "&jobid=" + this.jobid
//				+ "&userid=" + AppUtils.getUserSession().getUserid());


		String url = AppUtils.getContextPath();
		String openurl = url + "/pages/module/jobs/jobsinfo.xhtml";
		AppUtils.openWindow("_showDynamic", openurl + "?jobid=" + this.jobid
				+ "&userid=" + AppUtils.getUserSession().getUserid());
	}

	@Action
	public void showProfit() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/feeprofit.raq";
		AppUtils.openWindow("_showProfit", openUrl + "&id=" + this.jobid
				+ "&userid=" + AppUtils.getUserSession().getUserid());
	}

	/**
	 * 子单单号
	 *
	 * @return
	 */
	@Bind(id = "jobsChildNo")
	public List<SelectItem> getJobsChildNo() {
		try {
			return CommonComBoxBean.getComboxItems("d.id",
					"COALESCE(d.nos,'')", "fina_jobs AS d", "WHERE id ="
							+ this.jobid + " OR parentid=" + this.jobid
							+ " ANd isdelete = false", "ORDER BY nos");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;




	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if (this.jobid == -1l) {
			MessageUtils.alert("请先保存主表");
			return;
		}
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_imp_arap";

				// String args = this.jobid + "";
				String args = "'" + this.jobid + "'" + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				Browser.execClientScript("showmsg()");
				this.editGrid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}


	@Action
	public void importDataQuote() {
		try {
			String sql = "SELECT f_fina_fee_imp_quote('jobid="+this.jobid+";userid="+AppUtils.getUserSession().getUserid()+";type=create') AS returntext;";
			if("A".equals(jobtype)){//空运
				sql = "SELECT f_fina_fee_imp_quoteair('jobid="+this.jobid+";userid="+AppUtils.getUserSession().getUserid()+";type=create') AS returntext;";
			}
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String alerttext = m.get("returntext").toString();
			if(!StrUtils.isNull(alerttext)){
				alert(alerttext);
				//System.out.println("importDataQuote sql:"+sql);
			}else{
				String sql2 = "SELECT f_auto_optrace('jobid="+this.jobid+";linkid=0;usr="+AppUtils.getUserSession().getUsername()+";remarks=运价导入');";
				serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql2);
				this.alert("OK");
			}
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	@Action
	public void clearQuote() {
		try {
			String sql = "SELECT f_fina_fee_imp_quote('jobid="+this.jobid+";userid="+AppUtils.getUserSession().getUserid()+";type=clear');";
			serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql);
			String sql2 = "SELECT f_auto_optrace('jobid="+this.jobid+";linkid=0;usr="+AppUtils.getUserSession().getUsername()+";remarks=清除运价导入');";
			serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql2);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}


	@Action
	public void generateAR() {
		try {
			String ids[] = this.editGrid.getSelectedIds();
			String arapids = StrUtils.array2List(ids);
			String sql = "SELECT f_fina_arap_generatearap('jobid="+this.jobid+";type=AR;arapids="+arapids+";userid="+AppUtils.getUserSession().getUserid()+"');";
			serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void generateAP() {
		try {
			String ids[] = this.editGrid.getSelectedIds();
			String arapids = StrUtils.array2List(ids);
			String sql = "SELECT f_fina_arap_generatearap('jobid="+this.jobid+";type=AP;arapids="+arapids+";userid="+AppUtils.getUserSession().getUserid()+"');";
			//System.out.println(sql);
			serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}



	@Action
	public void importOrderAR() {
		try {
			if(jobs.getOrderid() == null || jobs.getOrderid() <=0){
				return;
			}
			jobs = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			String sql = "SELECT f_fina_arap_copyfromorder('jobid="+this.jobid+";araptype=AR;orderid="+jobs.getOrderid()+";userid="+AppUtils.getUserSession().getUserid()+"');";
			//serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql);
			//serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
			daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void importOrderAP() {
		try {
			if(jobs.getOrderid() == null || jobs.getOrderid() <=0){
				return;
			}
			jobs = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			String sql = "SELECT f_fina_arap_copyfromorder('jobid="+this.jobid+";araptype=AP;orderid="+jobs.getOrderid()+";userid="+AppUtils.getUserSession().getUserid()+"');";
			//serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
			daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void importLogisticsCostsJsVar() {
		StringBuffer logsb = new StringBuffer();
		boolean issuccess = false;
		try {
			jobs = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			BusShipping busShipping = this.serviceContext.busShippingMgrService.findByjobId(jobid);
			if (StrUtils.isNull(busShipping.getMblno())) {
				this.alert("请填写mbl提单号");
				return;
			}

			String sql = "select sc.namec,substring(scp.merchantcode,7,4) as merchantcode from sys_corporation sc left join sys_corpext scp on sc.id=scp.customerid " +
					"where sc.isdelete =false and sc.iscustomer = false and scp.merchantcode is not null and sc.namec not in ('世倡嘉美国际物流（上海）有限公司','CIMC G S LOGISTICS LLC')";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for (Map map : maps) {
				String merchantcode = String.valueOf(map.get("merchantcode"));

				String url0 = "http://120.76.119.53:9031/cost/queryOrderInCharge";
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("cache-control", "no-cache");
				headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
				headers.put("client_id", "823700-80179");
				headers.put("client_secret", "973e867994dea4c03c212f11ebe7193d5e88c2eb");
				headers.put("grant_type", "client_credentials");
				headers.put("Content-Type", "application/json;charset=utf-8");
				String ss = "{\n" +
						"\t\"mblNo\": \"" + busShipping.getMblno() + "\",\n" +
						"\t\"settleOffice\": \"" + merchantcode + "\",\n" +
						"\t\"settleOfficeName\": \"\"\n" +
						"}";
				String result0 = AutoImportDocument.httpsRequest(headers, url0, "POST", ss);
				logsb.append("importLogisticsCostsJsVar开始，result0为").append(result0);
				net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(result0);
				if (jsonobject.containsKey("data") && jsonobject.getJSONArray("data").size() > 0) {
					String sql1 = "SELECT f_fina_arap_logisticscosts('jobid=" + this.jobid + ";result0=" + result0 + ";userid=" + AppUtils.getUserSession().getUserid() + "');";
					daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
					issuccess = true;
				}
			}

			if (issuccess) {
				this.alert("OK");
				this.refresh();
			} else {
				this.alert("未获取到有效费用信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		LogBean.insertLog(logsb);
	}

	@Action
	public void importOwnerbill() {
		StringBuffer logsb = new StringBuffer();
		boolean issuccess = false;
		try {
			jobs = serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			BusShipping busShipping = this.serviceContext.busShippingMgrService.findByjobId(jobid);
			if (StrUtils.isNull(busShipping.getMblno())) {
				this.alert("请填写mbl提单号");
				return;
			}

			String url0 = "http://dmc.cimcwetrans.com/dmc/auth/api/getToken?tenantId=CCS-UFMS&authCode=f7238be0-f244-3b12-2d6b-25577002f45a&securityKey=57Gy14EECkrjaR69";
			String result000 = AutoImportDocument.httpsRequest(new HashMap<String, String>(), url0, "GET", "");
			String token = net.sf.json.JSONObject.fromObject(result000).getJSONObject("data").getString("accessToken");

			SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(busShipping.getCarrierid());
			String url = "http://dmc.cimcwetrans.com/dmc/bill_charge/get_detail?shipping=" + sysCorporation.getCode() + "&blNo=" + busShipping.getMblno();
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("AccessToken", token);
			String result111 = AutoImportDocument.httpsRequest(headers, url, "GET", "");
			logsb.append("importOwnerbill开始，result111为").append(result111);
			net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(result111);
			if (jsonobject.containsKey("data") && jsonobject.getJSONArray("data").size() > 0) {
				String sql1 = "SELECT f_fina_arap_ownerbill('jobid=" + this.jobid + ";result0=" + result111 + ";userid=" + AppUtils.getUserSession().getUserid() + "');";
				daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
				issuccess = true;
			}

			if (issuccess) {
				this.alert("OK");
				this.refresh();
			} else {
				this.alert("未获取到有效费用信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		LogBean.insertLog(logsb);
	}

	@Action
	public void showJoin() {
		isShowJoin = !isShowJoin;
		this.editGrid.reload();
	}

	@Action
	public void showJoinRpt() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/bus/feejoin.raq";
		AppUtils.openWindow("_showJoin", openUrl + "&id=" + this.jobid);
	}

	@Action
	public void joinConfirm() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("至少选择两个!");
			return;
		}
		String idss = StrUtils.array2List(ids);
		String sql = "\nSELECT f_fina_fee_join('feeid=" + idss + ";feeitemid="
				+ join2Feeitemid + ";arap=" + join2ARAP + ";ppcc=" + join2PPCC
				+ ";currency=" + join2Currency + ";userid="
				+ AppUtils.getUserSession().getUserid() + "') AS join";

		try {
			//AppUtils.debug(sql);
			this.serviceContext.arapMgrService.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
			this.join2Window.close();
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void joinClear() {
		String sql = "SELECT f_fina_fee_join_clear('jobid=" + this.jobid
				+ "');";
		try {
			//AppUtils.debug(sql);
			this.serviceContext.arapMgrService.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIWindow join2Window;

	@Bind
	@SaveState
	private Long join2Feeitemid;

	@Bind
	@SaveState
	public String join2PPCC = "PP";

	@Bind
	@SaveState
	public String join2Currency = "CNY";

	@Bind
	@SaveState
	public String join2ARAP = "AR";

	/**
	 * 合并费用项目
	 */
	@Action
	public void join() {
		String ids[] = this.editGrid.getSelectedIds();

		if (ids == null || ids.length < 1) {
			this.alert("请至少一行将被合并的费用!");
			return;
		}
		join2Window.show();
		this.update.markUpdate(UpdateLevel.Data, "join2Feeitemid");
		// ishow = false;
	}

	@Bind(id = "join2Feeitem")
	public List<SelectItem> getJoin2Feeitem() {
		String ids[] = this.editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			return null;
		}
		String id = StrUtils.array2List(ids);
		try {
			return CommonComBoxBean
					.getComboxItems(
							"d.id",
							"d.code||'/'||COALESCE(name,'')",
							"dat_feeitem AS d",
							"WHERE d.code = 'ALLINCH' OR d.code = 'INLAND' OR d.code = 'YUNFEI' OR d.code = 'DISB' OR d.code = 'CLEARANCE CHARGE' OR d.code = 'OCB' OR d.code = 'ALLIN' OR d.code = 'LAND'",
							"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

//	/**
//	 * 开始CC费用确认流程
//	 */
//	@Action
//	public void startCC() {
//
//		if (this.jobs.getNos().contains("DJCD")) {
//			if (this.queryPricer() == false) {
//				MessageUtils.alert("此单为单独清关,如果要启动CC费用确认流程,请指派报价客服!");
//				return;
//			}
//
//		}
//
//		if (queryStartCC2() == true) {// 如果是海运单子,判断是否整柜，否则不去判断.
//			if (queryStartCC()) {// 不是整柜
//				if (!queryStartLclCC()) {// 不是全包散货和正规散货，包柜散货类型
//					MessageUtils.alert("此单不是全包,正规,包柜散货或整柜类型,无法启动CC流程!");
//					return;
//				}
//			}
//		}
//
//		if (queryCCDivider() == false && queryPricer() == false) {
//			MessageUtils.alert("没有寻找到该单指派流程的客服(报价客服或国内客服),请指派客服,再启动!");
//			return;
//		}
//		if (this.queryStart() == true) {
//			pass("CCFeeConfirmProcess");
//			MessageUtils.alert("CC费用确认流程已启动!");
//		} else {
//			MessageUtils.alert("已启动该流程,不能重复启动!");
//		}
//	}

//	public void pass(String processid) {
//
//		Map<String, Object> m = new HashMap<String, Object>();
//		m.put("id", jobid);
//		m.put("sn", jobs.getNos());
//		try {
//			// WorkFlowUtil.startFF("ShipFCLProcess0712", m, AppUtil
//			// .getUserSession().getUsercode());
//			WorkFlowUtil.startFF(processid, m, AppUtils.getUserSession()
//					.getUsercode(),jobid);
//
//		} catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
//	}

//	/**
//	 * CC客服确认
//	 */
//
//	public void affirmNoCC() {
//		Map<String, Object> m = new HashMap<String, Object>();
//		if (!StrUtils.isNull(processInstanceId) && !StrUtils.isNull(workItemId)) {
//			try {
//				WorkFlowUtil.passProcess(processInstanceId, workItemId, m);
//				MessageUtils.alert("Confirm OK!,流程已通过!");
//			} catch (EngineException e) {
//				e.printStackTrace();
//			} catch (KernelException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * CC财务确认
//	 */
//	public void affirmNoCC2(Long id, String Actitivy) {
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(id,
//				"CCFeeConfirmProcess", Actitivy, "id");
//		try {
//			WorkFlowUtil.passProcessBatch(workids);
//			MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//		} catch (CommonRuntimeException e) {
//			// MsgUtil.alert(e.getLocalizedMessage());
//		} catch (EngineException e) {
//			// MsgUtil.alert(e.getErrorMsg());
//			// e.printStackTrace();
//		} catch (KernelException e) {
//			// MsgUtil.alert(e.getErrorMsg());
//			// e.printStackTrace();
//		}
//
//	}

//	/**
//	 * 确认判断是否重复启动流程
//	 */
//	public boolean queryStart() {
//		String sql = "SELECT v.value FROM t_ff_rt_taskinstance t,t_ff_rt_procinst_var v WHERE v.processinstance_id = t.processinstance_id AND t.process_id = 'CCFeeConfirmProcess' AND v.name = 'id' AND v.value LIKE 'java.lang.Long#%' AND  CAST(replace(v.value,'java.lang.Long#','') AS BIGINT) = "
//				+ jobid + ";";
//		List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
//		if (list.size() <= 0 || "".equals(list)) {
//			return true;
//		} else {
//			return false;
//		}
//	}

//	/**
//	 * 判断当前单号是否有客服分配
//	 */
//	public boolean queryCCDivider() {
//		List<Map> usersForRole = WorkFlowUtil.findAllUsersForRoleByJobsAssign(
//				"C", jobid);
//		if (usersForRole.size() == 0) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	/**
//	 *判断为正规海运才可启动该流程
//	 */
//	public boolean queryStartCC() {
//		String sql = "SELECT id FROM fina_jobs t WHERE t.ldtype = 'F' AND t.ldtype2 = 'F' AND t.isdelete = FALSE AND t.id = "
//				+ this.jobid;
//		List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
//		if (list.size() <= 0 || "".equals(list)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 添加全包散货和正规散货，包柜散货类型可以开启CC费用审核流程
//	 */
//	public boolean queryStartLclCC() {
//		String sql = " SELECT ( CASE WHEN EXISTS(SELECT 1 FROM fina_jobs WHERE id = "
//				+ this.jobid
//				+ " AND isdelete = FALSE AND ldtype = 'L' AND ldtype2 IN ('D','B','C')) THEN 'T' ELSE 'F' END )"
//				+ " AS type";
//		Map map = serviceContext.daoIbatisTemplate
//				.queryWithUserDefineSql4OnwRow(sql);
//		String type = (String) map.get("type");
//		if ("F".equals(type)) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	/**
//	 *判断是否为海运单子
//	 */
//	public boolean queryStartCC2() {
//		String sql = "SELECT id FROM fina_jobs t WHERE t.jobtype = 'S' AND t.isdelete = FALSE AND t.id = "
//				+ this.jobid;
//		List list = serviceContext.arapMgrService.finaArapDao.executeQuery(sql);
//		if (list.size() <= 0 || "".equals(list)) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	/**
//	 * 判断是否有报价客服
//	 */
//	public boolean queryPricer() {
//		List<Map> usersForRole = WorkFlowUtil.findPricer("C", jobid);
//		if (usersForRole.size() == 0) {
//			return false;
//		} else {
//			return true;
//		}
//	}

	@Action
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}

	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 2);
	}*/

	/**
	 * 生成往来
	 */
	@Action
	protected void createrCurrent() {
		String[] ids = this.editGrid.getSelectedIds();
		StringBuilder sb = new StringBuilder();
		if (ids == null ||ids.length == 0) {
			MessageUtils.alert("请选择一条记录");
			return;
		}else{
			 for (int i = 0; i <ids.length; i++) {
				 if (i == (ids.length - 1)) {
					 sb.append(ids[i]);
				 }else {
					 sb.append(ids[i] + ",");
				 }
			 }
		}
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			String username = AppUtils.getUserSession().getUsername();
			String basepath = AppUtils.getBasePath();
			Long fmsid = serviceContext.userMgrService.sysUserDao.findById(userid).getFmsid();

			String sql = "SELECT f_arap_copy_link('jobid=" + jobid + ";userid=" + userid + ";ids="+sb.toString()+"');";
			serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);

			Long linkid = this.serviceContext.busShippingMgrService.findByjobId(jobid).getId();

			String sqlname = "SELECT a.saleid as saleid," +
					"(SELECT COALESCE(code,'') code FROM sys_user x WHERE isdelete = FALSE AND id = a.saleid),"+
					"(SELECT COALESCE(namec,'') namec FROM sys_user x WHERE isdelete = FALSE AND id = a.saleid)," +
					"(SELECT COALESCE(namee,'') namee FROM sys_user x WHERE isdelete = FALSE AND id = a.saleid)," +
					"COALESCE(nos,'') nos FROM fina_jobs a WHERE isdelete = FALSE AND id = "+jobid;

			Map map = this.serviceContext.sysUserAssignMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlname);

			String codessql = "SELECT string_agg(COALESCE(usercode::TEXT,'-100'),',') AS codes,string_agg(COALESCE(userid::TEXT,'-100'),',') AS ids FROM _sys_user_assign t  WHERE linktype = 'J' AND linkid = "+linkid+" AND userid IS NOT NULL";

			try {
				Map codesmap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(codessql);
				if(map != null && map.size() > 0){
					if(codesmap.size() > 0 && codesmap.containsKey("codes") && codesmap.get("codes") != null){
						new StartThread(userid,fmsid,username,basepath,map,codesmap.get("codes").toString(),this.jobid,codesmap.get("ids").toString()).start();
						MessageUtils.alert("OK!消息已推送至工作单分派!");
					}else{
						//没有分派,推送给业务员自己
						new StartThread(userid,fmsid,username,basepath,map,map.get("code").toString(),this.jobid,null).start();
						MessageUtils.alert("OK!消息已推送至业务员:"+map.get("code").toString());
					}
				}
			} catch (NoRowException e) {
				new StartThread(userid,fmsid,username,basepath,map,map.get("code").toString(),this.jobid,null).start();
				MessageUtils.alert("OK!消息已推送至业务员:"+map.get("code").toString());
			}catch (Exception e) {
				e.printStackTrace();
			}
		} catch (NoRowException e) {
			Browser.execClientScript("showmsg()");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			this.refresh();
		}
	}

	class StartThread extends Thread{
		Map map;
		String codes;
		String senderUserName;
		String basePath;
		Long uid;
		Long linkid;
		Long sendid;
		String ids;
	    public StartThread(Long sendid,Long uid, String senderUserName,String basePath , Map map, String codes,Long linkid,String ids) {
			this.map = map;
			this.codes = codes;
			this.senderUserName = senderUserName;
			this.basePath = basePath;
			this.uid = uid;
			this.linkid = linkid;
			this.sendid = sendid;
			this.ids = ids;
		}

		@Override
	    public void run(){
			String saleid = map.get("saleid").toString();
	    	String namec = map.get("namec").toString();
			String namee = map.get("namee").toString();
			String nos = map.get("nos").toString();
			for (int i = 0; i < codes.split(",").length; i++) {
				if(null == ids || ids.isEmpty()){
					serviceContext.arapMgrService.sendMessageToArapService(sendid,uid,codes.split(",")[i],nos,senderUserName,basePath,namec,namee, linkid,null,saleid);
				}else{
					serviceContext.arapMgrService.sendMessageToArapService(sendid,uid,codes.split(",")[i],nos,senderUserName,basePath,namec,namee, linkid,ids.split(",")[i],saleid);
				}
				try {
					sleep(1200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	    }
	};

//	@Action
//	private void saveAjaxSubmit() {
//		List<Long> idsOldArray = new ArrayList<Long>();
//		idsOldArray.clear();
//		List<Long> idsNewArray = new ArrayList<Long>();
//		idsNewArray.clear();
//
//		String data = AppUtils.getReqParam("data");
//		if (!StrUtils.isNull(data) && !"null".equals(data)) {
//			Type listType = new TypeToken<ArrayList<com.scp.vo.finance.FinaArap>>() {
//			}.getType();// TypeToken内的泛型就是Json数据中的类型
//			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//			ArrayList<com.scp.vo.finance.FinaArap> list = gson.fromJson(data,
//					listType);// 使用该class属性，获取的对象均是list类型的
//
//			ArrayList<com.scp.vo.finance.FinaArap> listOld = gson.fromJson(
//					this.serviceContext.arapMgrService.getArapsJsonByJobid(
//							this.jobid, null), listType);
//
//			List<FinaArap> finaArapList = new ArrayList<FinaArap>();
//
//			for (com.scp.vo.finance.FinaArap li : list) {
//				if ("".equals(li.getCurrency()) || -100 == li.getFeeitemid()) {
//					continue;
//				}
//				FinaArap finaArap = new FinaArap();
//				if (-100 != li.getId()) {
//					finaArap = serviceContext.arapMgrService.finaArapDao
//							.findById(li.getId());
//					idsNewArray.add(li.getId());
//				}
//				finaArap.setAraptype(li.getAraptype());
//				finaArap.setArapdate(li.getArapdate());
//				finaArap.setCustomerid(li.getCustomerid());
//				finaArap.setFeeitemid(li.getFeeitemid());
//				finaArap.setAmount(li.getAmount());
//				finaArap.setCurrency(li.getCurrency());
////				finaArap.setRemarks(li.getRemarks());
//				finaArap.setPiece(li.getPiece());
//				finaArap.setPrice(li.getPrice());
//				finaArap.setUnit(li.getUnit());
//				finaArap.setCustomercode(li.getCustomercode());
//				finaArap.setPpcc(li.getPpcc());
//				finaArap.setSharetype("N");
//				finaArap.setJobid(this.jobid);
//				finaArapList.add(finaArap);
//			}
//			try {
//				serviceContext.arapMgrService.saveOrModify(finaArapList);
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//				return;
//			}
//
//			if (listOld != null && listOld.size() > 0) {
//				for (com.scp.vo.finance.FinaArap li : listOld) {
//					idsOldArray.add(li.getId());
//				}
//				List<Long> lists = getDiffrent(idsOldArray, idsNewArray);
//
//				if (!lists.isEmpty()) {
//					try {
//						serviceContext.arapMgrService.removes(lists);
//					} catch (Exception e) {
//						MessageUtils.showException(e);
//						return;
//					}
//				}
//			}
//			MessageUtils.alert("OK");
//		}
//	}

	/**
	 * 获取两个List的不同元素
	 *
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
		long st = System.nanoTime();
		List<Long> diff = new ArrayList<Long>();
		for (Long str : list1) {
			if (!list2.contains(str)) {
				diff.add(str);
			}
		}
		////System.out.println("getDiffrent total times "+ (System.nanoTime() - st));
		return diff;
	}

	@Action(id="feeitemid",event="onselect")
	public void feeitemSelect(){
		autoUnit();
	}

	public void autoUnit(){
		if(this.selectedRowData.getUnit()!=null&&this.selectedRowData.getUnit().length()>0){

		}else{
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("\n SELECT DISTINCT unit FROM fina_arap a ");
			sbsql.append("\n WHERE a.isdelete = FALSE");
			sbsql.append("\n AND a.jobid =");
			sbsql.append(this.jobid);

			try {
				List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbsql.toString());
				if(list != null && list.size() == 1){
					Map map = list.get(0);
					if(map != null && map.size() == 1 && map.containsKey("unit")){
						String unit = map.get("unit").toString();
						this.selectedRowData.setUnit(unit);
						update.markUpdate(true,UpdateLevel.Data,"editPanel");
					}
				}
			} catch (NoRowException e) {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Action
	public void changeArapInfo(){
		if(StrUtils.isNull(currency)){
			initArapInfo();
		}
	}

	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}

	@Override
	public void editGrid_ondblclick() {
		String[] ids = this.editGrid.getSelectedIds();
		try{
			this.pkVal =  Long.valueOf(ids[0]);
		}catch(Exception e){
			MessageUtils.alert("请先保存！");
			return;
		}
		doServiceFindData();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		SysCorporation custom = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		if(custom!=null&&custom.getCode()!=null){
			Browser.execClientScript("$('#customer_input').val('"+custom.getCode()+"');");
		}
		DatFeeitem feeit = serviceContext.feeItemMgrService.datFeeitemDao.findById(selectedRowData.getFeeitemid());
		if(feeit!=null){
			Browser.execClientScript("$('#feei_input').val('"+feeit.getCode()+"/"+feeit.getName()+"');");
		}
		if(isHideAPCustomerShow && "AP".equals(selectedRowData.getAraptype())){
			Browser.execClientScript("$('#customer_input').val('*************');");
		}
	}


	/**
	 * 提单分单号码
	 *
	 * @return
	 */
	@Bind(id = "busshipbillno")
	public List<SelectItem> getBusshipbillno() {
		try {
			List<SelectItem> lista = CommonComBoxBean.getComboxItems("DISTINCT d.mblno ", "d.mblno", "bus_ship_bill AS d", "WHERE jobid ="
							+ this.jobid + " AND isdelete = false AND COALESCE(d.mblno,'') <> ''", "");
			List<SelectItem> listb = CommonComBoxBean.getComboxItems("DISTINCT d.hblno ", "d.hblno", "bus_ship_bill AS d", "WHERE jobid ="
					+ this.jobid + " AND isdelete = false AND COALESCE(d.hblno,'') <> ''", "");
			lista.addAll(listb);
			return lista;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}


	/**
	 * 箱号
	 *
	 * @return
	 */
	@Bind(id = "cntid")
	public List<SelectItem> getCntid() {
		try {
			List<SelectItem> liste = CommonComBoxBean.getComboxItems("DISTINCT f.cntno ", "f.cntno", "bus_ship_container AS f", "WHERE jobid ="
							+ this.jobid + "AND isdelete = false AND COALESCE(f.cntno,'') <> '' AND parentId is null  and cntno is not null", "");
			return liste;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@SaveState
	private Vector<String> addRowUuids = new Vector<String>();

	@Override
	protected void add(Object obj) {
		if (addedData != null) {
			JSONArray jsonArray = (JSONArray) addedData;
			JSONArray jsonArrayNew = new JSONArray();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String uuid = String.valueOf(jsonObject.get("uuid"));
				//System.out.println(uuid);
				if(addRowUuids.contains(uuid)){
					System.out.println("重复提交id" + uuid + " jobno:[" + this.jobs.getNos() + "] : " + Calendar.getInstance().getTime().toGMTString());
				}else{
					addRowUuids.add(uuid);
					jsonArrayNew.add(jsonObject);
				}
			}
			List<Long> list = serviceContext.arapMgrService.addArapEditGrid(jsonArrayNew, this.jobid);

			//新增检查是否内部关联，是的话提示自动生成往来
			boolean isRelated = false;
            for (int i = 0; i < list.size(); i++) {
                String feeSql = "SELECT (EXISTS(SELECT 1 FROM fina_corp WHERE jobid = " + this.jobid + " AND corpid = (SELECT customerid FROM fina_arap WHERE id = " + list.get(0) + " LIMIT 1)) OR\n" +
                        " EXISTS(SELECT 1 FROM fina_jobs WHERE id = " + this.jobid + " AND corpid = (SELECT customerid FROM fina_arap WHERE id = " + list.get(0) + " LIMIT 1))) AS isrelated;";
                Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(feeSql);
                if (map.get("isrelated").toString().equals("true")) {
					isRelated = true;
                    continue;
                }
            }
            if (isRelated) {
                String sql1 = "SELECT string_agg(id::VARCHAR, ',') as feeids FROM fina_arap WHERE jobid = " + this.jobid + "" +
                        " AND (customerid in (SELECT corpid FROM fina_corp where jobid = " + this.jobid + ") OR customerid = " + this.jobs.getCorpid() + ")" +
                        " AND fkid ISNULL AND rptype <> 'O' AND isdelete = false AND inputtime >= CURRENT_TIMESTAMP - interval '1 minutes';";
                Map feeMap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
                feeIds = feeMap.get("feeids").toString();
                Browser.execClientScript("setTimeout('expenseFee()','1000')");
            }

//			this.editGrid.reload();
		}
	}

	@Override
	protected void update(Object obj) {
		if (modifiedData != null) {
				serviceContext.arapMgrService.updateArapEditGrid(modifiedData);
//				this.editGrid.reload();
		}
	}

	@Action
	public void addedit(){

		if("Y".equals(ConfigUtils.findSysCfgVal("fina_arap_addbywindow"))){
			addBatch();
			return;
		}

		this.pkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		String[] ids = this.editGrid.getSelectedIds();
		if(ids!=null&&ids.length>0){//如果勾选了一行就复制这行新增
			if(!StrUtils.isNumber(ids[0])){
				MessageUtils.alert("请选择已经保存的费用再复制新增");
				return;
			}
			FinaArap arapadd = serviceContext.arapMgrService.finaArapDao.findById(Long.parseLong(ids[0]));
			if(arapadd!=null){
				com.scp.vo.finance.FinaArap vo = new com.scp.vo.finance.FinaArap();
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(arapadd.getCustomerid());
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao.findById(arapadd.getFeeitemid());
				vo.setId(0l);
				vo.setAraptype(arapadd.getAraptype());
				vo.setCurrency(arapadd.getCurrency());
				vo.setCorpid(arapadd.getCorpid());
				vo.setArapdate(arapadd.getArapdate());
				vo.setPpcc(arapadd.getPpcc());
				vo.setCustomerid(arapadd.getCustomerid());
				vo.setCustomeabbr(sysCorporation.getAbbr());
				vo.setCustomecode(sysCorporation.getCode());
				vo.setCustomenamec(sysCorporation.getNamec());
				vo.setCustomenamee(sysCorporation.getNamee());
				//vo.setFeeitemid(arapadd.getFeeitemid());
				//vo.setFeeitemdec(datFeeitem.getCode()+"/"+datFeeitem.getName());
				vo.setPiece(arapadd.getPiece());
				vo.setPrice(arapadd.getPrice());
				vo.setAmount(arapadd.getAmount());
				vo.setPricenotax(arapadd.getPricenotax());
				vo.setTaxrate(arapadd.getTaxrate());
				vo.setBlno(arapadd.getBlno());
				vo.setAmtcost(arapadd.getAmtcost());
				vo.setPayplace(arapadd.getPayplace());

				vo.setUuid(UUID.randomUUID().toString());

				editGrid.appendRow(vo);
				//Browser.execClientScript("setTimeout('addRowAfter()','3000')");

			}
		}else{
			initAdd();
			com.scp.vo.finance.FinaArap vo = new com.scp.vo.finance.FinaArap();
			vo.setId(0l);
			vo.setAraptype("AR");
			vo.setCurrency("CNY");
			vo.setCorpid(selectedRowData.getCorpid());
			vo.setArapdate(selectedRowData.getArapdate());
			vo.setPpcc(selectedRowData.getPpcc());
			vo.setCustomerid(selectedRowData.getCustomerid());
			vo.setPiece(selectedRowData.getPiece());
			vo.setPrice(selectedRowData.getPrice());
			vo.setAmount(selectedRowData.getAmount());
			vo.setPricenotax(selectedRowData.getPricenotax());
			vo.setTaxrate(selectedRowData.getTaxrate());
			vo.setBlno(selectedRowData.getBlno());
			vo.setAmtcost(selectedRowData.getAmtcost());
			vo.setPayplace(selectedRowData.getPayplace());
			vo.setUuid(UUID.randomUUID().toString());

			//vo.setFeeitemid(arapadd.getFeeitemid());
			//vo.setFeeitemdec(datFeeitem.getCode()+"/"+datFeeitem.getName());

			if(this.jobs.getCustomerid() != null){
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(this.jobs.getCustomerid());
				vo.setCustomeabbr(sysCorporation.getAbbr());
				vo.setCustomecode(sysCorporation.getCode());
				vo.setCustomenamec(sysCorporation.getNamec());
				vo.setCustomenamee(sysCorporation.getNamee());
			}
			editGrid.appendRow(vo);
		}

		Browser.execClientScript("setTimeout('initFlexBoxAll()','500')");
		//Browser.execClientScript("initFlexBox('custflex')");
	}


	/**
	 * 费用结算地，只提取工作单里面的接单公司或操作公司，2选1
	 * @return
	 */
	@Bind(id="corpAbbr")
    public List<SelectItem> getCorpAbbr() {
    	try {
    		String qry = SaasUtil.filterById("d");
    		return CommonComBoxBean.getComboxItems("d.id","COALESCE(d.abbcode,'')","sys_corporation d ,fina_jobs j ","WHERE d.iscustomer = false " +
//					"\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = j.corpid)" +
//					"\n 	          AND EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = j.corpidop) "+
//					"\n 		THEN  "+
//					"\n 			d.id = ANY(SELECT id FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())) "+
//					"\n 		ELSE  "+
//					"\n			(d.id = j.corpid OR d.id = j.corpidop OR d.id = j.corpidop2 OR d.id = ANY(SELECT corpid FROM fina_corp c WHERE c.jobid = j.id AND c.isdelete = FALSE)) "+
					"\n AND d.id = ANY(SELECT j.corpid UNION SELECT j.corpidop UNION SELECT j.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = j.id AND c.isdelete = FALSE)" +
//					"\n 		END) " +
					"\nAND j.id = "+this.jobid+""+ qry,"ORDER BY d.id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }




	@Bind
	public UIWindow importDataHandWindow;

	@Bind
	public UIIFrame importDataHandIFrame;

	@Action
	public void importDataHand() {
		importDataHandIFrame.setSrc("./choosefclbyhand.xhtml?jobid=" + this.jobid);
		update.markAttributeUpdate(importDataHandIFrame, "src");
		if (importDataHandWindow != null)
			importDataHandWindow.show();
	}

	@Override
	public void save() {
//		cost = selectedRowData.getAmount().subtract(selectedRowData.getAmtcost());
//		if(!splitArap()){
//			return;
//		}
        try {
            if (modifiedData != null) {
            	JSONArray array = (JSONArray)modifiedData;
            	JSONArray newArray = new JSONArray();
            	for (int i = 0; i < array.size(); i++) {
            		if(null == array.get(i)){
            			continue;
            		}
            		Object object = array.get(i);
            		JSONObject json = new JSONObject();
            		json = (JSONObject)(array.get(i));

					if (json.get("rptype").equals("D")) {
						MessageUtils.alert("代垫费用请在 其他-代垫费用申请 进行修改!");
						editGrid.reload();
						return;
					}

            		if(null != json.get("amtcost") && Double.valueOf("".equals(String.valueOf(json.get("amtcost")))?"0":String.valueOf(json.get("amtcost"))) > 0){//判断是否存在额外成本
            			//拆分费用行为成本费用和额外成本费用
            			double cost = Double.valueOf(String.valueOf(json.get("cost")));
            			double amtcost = Double.valueOf(String.valueOf(json.get("amtcost")));//额外成本

            			//成本费用
            			json.put("amount", cost);//金额
            			json.put("price", cost/Double.valueOf(String.valueOf(json.get("piece"))));//单价
            			json.put("pricenotax", cost/Double.valueOf(String.valueOf(json.get("piece"))));//税前单价
            			json.put("amtcost", "0");//额外成本
            			json.put("billamount", cost);//账单金额
            			newArray.add(json);
            			modifiedData = (Object)newArray;
            			super.save();
            			newArray = new JSONArray();
            			//额外成本费用
            			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT id FROM fina_arap WHERE isdelete = FALSE AND parentid = "+json.get("id")+" LIMIT 1");
            			json.put("amount", amtcost);//金额
            			json.put("piece", 1);//数量
            			json.put("price", amtcost);//单价
            			json.put("pricenotax", amtcost);//税前单价
            			json.put("taxrate", "1");//税率默认1
            			json.put("customerid", "25322452274");//结算公司默认GLOBESKY
            			json.put("id",m.get("id"));//额外成本费用ID
            			json.put("billamount", amtcost);//账单金额
            		}
        			newArray.add(json);
        			modifiedData = (Object)newArray;
        			super.save();


                    //保存报价金额
					JSONObject jsonObject = (JSONObject) array.get(i);
					String quoteamount = String.valueOf(null == jsonObject.get("quoteamount") ? "" : jsonObject.get("quoteamount"));
					String quotecurrency = String.valueOf(null == jsonObject.get("quotecurrency") ? "" : jsonObject.get("quotecurrency"));
					String arapid = String.valueOf(jsonObject.get("id"));
					StringBuffer bfsql = new StringBuffer();
					if (!StrUtils.isNull(quoteamount) && !StrUtils.isNull(quotecurrency)) {
						String sql = "UPDATE fina_arap_link_quote Set isdelete = TRUE WHERE arapid = "+arapid+";";
						sql += "\nINSERT INTO fina_arap_link_quote(id ,arapid ,quoteamount ,quotecurrency )" +
							"\nSELECT getid()," + arapid + "," + new BigDecimal(quoteamount) + ",'"+quotecurrency+"'" +
							"\nFROM _virtual WHERE NOT EXISTS(SELECT 1 FROM fina_arap_link_quote WHERE isdelete = FALSE AND arapid = " + arapid +");";
						daoIbatisTemplate.updateWithUserDefineSql(sql);
					}
				}
            }
            if (addedData != null) {
                add(addedData);
            }
            if (removedData != null) {
                remove(removedData);
            }
            editGrid.commit();
            editGrid.reload();
            checkFeei();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
//		initArapInfo();//neo 20200102 保存后刷新合计部分信息，但是不能刷整个列表，否则后台检查提示的会导致列表输入的没保存被刷没有了
	}

	@Action
	public void arap2payapply(){
		String[] ids = this.editGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if(StrUtils.isNull(id)){
					id = ids[i];
				}else{
					id += "," + ids[i];
				}
			}
		}

		//世倡系统必须商务审核后才能发起付款申请
		if("2274".equals(CSNO) || "8888".equals(CSNO)){
			String sql = "SELECT EXISTS (SELECT 1 FROM fina_arap a WHERE a.id in ("+id+") AND a.isdelete = FALSE AND a.isconfirm = false AND EXISTS (SELECT * FROM fina_jobs WHERE id = a.jobid AND isdelete = FALSE AND isconfirm_bus = false))";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(map.containsKey("exists") == false || "true".equals(String.valueOf(map.get("exists")))){
					MessageUtils.alert("费用确认后才能生成付款申请！");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String applysql = "SELECT * FROM f_fina_arap2payapply('jobid="+this.jobid+";arapid="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+"') AS rpreqid";
		try{
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(applysql);
			AppUtils.openNewPage("./payapplyedit.xhtml?type=edit&id="+m.get("rpreqid")+"&customerid=-1&araptype=AP");
			this.editGrid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	/**
	 * 冲账申请
	 */
	@Action
	public void arapreverse(){
		String[] ids = this.editGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if(StrUtils.isNull(id)){
					id = ids[i];
				}else{
					id += "," + ids[i];
				}
			}
		}

		//冲账申请必须保证费用类型、结算地、客户、币制完全一致，金额相加为0
		String sql = "SELECT (SELECT abbr FROM sys_corporation WHERE id = a.corpid AND isdelete = FALSE limit 1)" +
				",(SELECT abbr AS custabbr FROM sys_corporation WHERE id = customerid AND isdelete = FALSE limit 1)" +
				",araptype" +
				",SUM(amount) AS amount" +
				",currency " +
				",STRING_AGG(id||'',',') " +
				"from  fina_arap a " +
				"WHERE id = ANY(regexp_split_to_array('"+StrUtils.array2List(ids)+"',',')::BIGINT[]) " +
				"AND isdelete = FALSE " +
				"GROUP BY corpid,araptype,customerid,currency";
//		List<String[]> idslist = new ArrayList<String[]>();
//		List<String> currencylist = new ArrayList<String>();
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			String errstr = "";
			for (int i = 0; i < list.size(); i++) {
				if(Double.valueOf(String.valueOf(list.get(i).get("amount"))) != 0){
					errstr += "结算地"+String.valueOf(list.get(i).get("abbr"))
					+":抬头"+String.valueOf(list.get(i).get("custabbr"))
					+String.valueOf(list.get(i).get("araptype"))+" "
					+String.valueOf(list.get(i).get("currency"))
					+"金额有误!\n";
				}

//				String[] idlist = String.valueOf(list.get(i).get("ids")).split(",");
//				idslist.add(idlist);
//
//				String currency = String.valueOf(list.get(i).get("currency"));
//				currencylist.add(currency);
			}
			if(!StrUtils.isNull(errstr)){
				MessageUtils.alert(errstr);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try{
			//冲账申请与付款申请内容一致，付款方式类型为对冲（D）
			String applysql = "SELECT * FROM f_fina_arap3payapply('jobid="+this.jobid+";arapid="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+";paytype=D;') AS rpreqid";
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(applysql);
			AppUtils.openNewPage("./payapplyedit.xhtml?type=edit&id="+m.get("rpreqid")+"&customerid=-1&araptype=AP");
			this.editGrid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	@Action
	public void arap2bill(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		String applysql = "SELECT * FROM f_fina_arap2bill('jobid="+this.jobid+";arapid="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+"') AS billid";
		try{
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(applysql);
			AppUtils.openNewPage("../ship/busbill.xhtml?jobid="+this.jobid+"&id="+m.get("billid"));
			this.editGrid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}


	@Action
	public void arap2payapply2(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}

		String applysql = "SELECT * FROM f_fina_arap2payapply('jobid="+this.jobid+";arapid="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+"') AS rpreqid";
		try{
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(applysql);
			AppUtils.openWindow("_newpayapplyedit", "./payapplyedit.xhtml?type=edit&id="+m.get("rpreqid")+"&customerid=-1&araptype=AP");
			this.editGrid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	private String[] idByBill = null;

	public void auditByIds(String[] ids,long jobid, ServiceContext serviceContext) {
		idByBill=ids;
		this.serviceContext = serviceContext;
		this.jobid = jobid;
		audit();
	}

	@Bind
	public UIEditDataGrid amendGrid;

	@Bind(id = "amendGrid", attribute = "dataProvider")
	protected GridDataProvider getAmendGrid() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(amendfeeiinfo)){
					return null;
				}
				Object[] objs = null;
				try {
					DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
					net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(amendfeeiinfo);
					objs = new Object[array.size()];
					for (int i = 0; i < array.size(); i++) {
						Map map = (Map) array.get(i);
						Map<String,String> dateMap = (Map)map.get("arapdate");
						Map<String,String> dateMap2 = (Map)map.get("inputtime");
						String sql = "SELECT code,namec,namee,abbr,(SELECT name as feeitemdec FROM dat_feeitem WHERE isdelete = FALSE AND id = "+map.get("feeitemid")+" LIMIT 1)" +
								"FROM sys_corporation WHERE id = "+map.get("customerid")+" AND isdelete = FALSE;";
						Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
						map.put("customecode", m.get("code"));
						map.put("customenamec", m.get("namec"));
						map.put("customenamee", m.get("namee"));
						map.put("customeabbr", m.get("abbr"));
						map.put("feeitemdec", m.get("feeitemdec"));

						map.put("arapdesc", "增减费用");
						map.put("arapdate", fmt.format(dateMap.get("time")));
						map.put("inputtime", fmt.format(dateMap2.get("time")));
						objs[i] = map;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return objs;
			}

			@Override
			public int getTotalCount() {
				if(StrUtils.isNull(amendfeeiinfo)){
					return 0;
				}
				net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(amendfeeiinfo);
				return array.size();
			}
		};
	}

	@Bind
	public UIDataGrid profitGrid;

	@Bind(id = "profitGrid", attribute = "dataProvider")
	protected GridDataProvider getProfitGrid() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(amendfeeiinfo)){
					return null;
				}
				net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(amendfeeiinfo);
				double[] d = new double[6];
				if(arTip.indexOf("CNY:")>-1){
					d[0] = Double.valueOf(arTip.split("CNY:")[1].split("（")[0]);
				}
				if(arTip.indexOf("HKD:")>-1){
					d[1] = Double.valueOf(arTip.split("HKD:")[1].split("（")[0]);
				}
				if(arTip.indexOf("USD:")>-1){
					d[2] = Double.valueOf(arTip.split("USD:")[1].split("（")[0]);
				}
				if(apTip.indexOf("CNY:")>-1){
					d[0] = d[0]-Double.valueOf(apTip.split("CNY:")[1].split("（")[0]);
				}
				if(apTip.indexOf("HKD:")>-1){
					d[1] = d[1]-Double.valueOf(apTip.split("HKD:")[1].split("（")[0]);
				}
				if(apTip.indexOf("USD:")>-1){
					d[2] = d[2]-Double.valueOf(apTip.split("USD:")[1].split("（")[0]);
				}

				DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,String> dateMap = (Map)((Map) array.get(0)).get("arapdate");
				String sql = "SELECT x.xtype||x.rate AS hkdtousd,(SELECT xtype||rate AS cnytousd FROM _dat_exchangerate WHERE datafm < '"+fmt.format(dateMap.get("time"))+"'	AND currencyto = 'USD'	AND currencyfm = 'CNY'	ORDER BY datafm DESC LIMIT 1)	FROM _dat_exchangerate x WHERE x.datafm < '"+fmt.format(dateMap.get("time"))+"'	AND x.currencyto = 'USD'	AND x.currencyfm = 'HKD'	ORDER BY x.datafm DESC LIMIT 1";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				Map<String,String> maps = new HashMap<String, String>();
				maps.put("profit_cny", d[0]+"");
				maps.put("profit_hkd", d[1]+"");
				maps.put("profit_usd", d[2]+"");
				double usd = 0d;
				double usd2 = 0d;
				if(d[1] != 0){
					if(String.valueOf(m.get("hkdtousd")).indexOf("/")>-1){
						usd2 = d[1]/Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("/",""));
					}else{
						usd2 = d[1]*Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("*",""));
					}
				}
				if(d[2] != 0){
					if(String.valueOf(m.get("cnytousd")).indexOf("/")>-1){
						usd = d[0]/Double.valueOf(String.valueOf(m.get("cnytousd")).replace("/",""));
					}else{
						usd = d[0]*Double.valueOf(String.valueOf(m.get("cnytousd")).replace("*",""));
					}
				}
				maps.put("profit", String.format("%.2f",(d[2]+usd2+usd)));
				maps.put("amenddesc", "增减前");
				Object[] objs = {maps,maps};
				maps = new HashMap<String, String>();

				for (int i = 0; i < array.size(); i++) {
					Map map = (Map) array.get(i);
					if("CNY".equals(String.valueOf(map.get("currency"))) && "AR".equals(String.valueOf(map.get("araptype")))){
						d[0] = d[0]+Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("HKD".equals(String.valueOf(map.get("currency"))) && "AR".equals(String.valueOf(map.get("araptype")))){
						d[1] = d[1]+Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("USD".equals(String.valueOf(map.get("currency"))) && "AR".equals(String.valueOf(map.get("araptype")))){
						d[2] = d[2]+Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("CNY".equals(String.valueOf(map.get("currency"))) && "AP".equals(String.valueOf(map.get("araptype")))){
						d[0] = d[0]-Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("HKD".equals(String.valueOf(map.get("currency"))) && "AP".equals(String.valueOf(map.get("araptype")))){
						d[1] = d[1]-Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("USD".equals(String.valueOf(map.get("currency"))) && "AP".equals(String.valueOf(map.get("araptype")))){
						d[2] = d[2]-Double.valueOf(String.valueOf(map.get("amount")));
					}
				}
				if(d[1] != 0){
					if(String.valueOf(m.get("hkdtousd")).indexOf("/")>-1){
						usd2 = d[1]/Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("/",""));
					}else{
						usd2 = d[1]*Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("*",""));
					}
				}
				if(d[2] != 0){
					if(String.valueOf(m.get("cnytousd")).indexOf("/")>-1){
						usd = d[0]/Double.valueOf(String.valueOf(m.get("cnytousd")).replace("/",""));
					}else{
						usd = d[0]*Double.valueOf(String.valueOf(m.get("cnytousd")).replace("*",""));
					}
				}
				maps.put("profit_cny", d[0]+"");
				maps.put("profit_hkd", d[1]+"");
				maps.put("profit_usd", d[2]+"");
				maps.put("profit", String.format("%.2f",(d[2]+usd2+usd)));
				maps.put("amenddesc", "增减后");
				objs[1] = maps;
				return objs;
			}

			@Override
			public int getTotalCount() {
				int count = 2;
				return count;
			}
		};
	}

    /**
     * 电商模块-费用合并
     */
	@Action
	public void costConsolidation() {
		String sql = "SELECT f_commerce_arap_merge('" + this.jobid + "') AS json;";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String ret = StrUtils.getMapVal(map, "json");
		if (!ret.contains("合并成功")) {
			MessageUtils.alert(ret);
			return;
		}
		this.refresh();
	}

    /**
     * 电商模块-分单利润
     */
	@Action
	public void dividedProfit() {
		dividedProfitIFrame.setSrc("/scp/pages/module/commerce/profitchild.html?id=" + this.jobid);
		update.markAttributeUpdate(dividedProfitIFrame, "src");
		if (dividedProfitWindow != null) {
			dividedProfitWindow.setTitle("分单利润");
			dividedProfitWindow.show();
		}
	}

	@Action
	public void showprofitGrid() {
		amendfeeiinfo = AppUtils.getReqParam("array");
		profitGrid.reload();
		Browser.execClientScript("profitGridJsvar.show();");
	}

	@Action
	public void clearFeeIds() {
		feeIds = "";
	}

	/**
	 * 自动生成往来
	 */
	@Action
	protected void autoCreaterCurrent() {
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			String username = AppUtils.getUserSession().getUsername();
			String basepath = AppUtils.getBasePath();
			Long fmsid = serviceContext.userMgrService.sysUserDao.findById(userid).getFmsid();

			String sql = "SELECT f_arap_copy_link('jobid=" + jobid + ";userid=" + userid + ";ids="+feeIds+"');";
			serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
			feeIds = "";

			Long linkid = this.serviceContext.busShippingMgrService.findByjobId(jobid).getId();

			String sqlname = "SELECT a.saleid as saleid," +
					"(SELECT COALESCE(code,'') code FROM sys_user x WHERE isdelete = FALSE AND id = a.saleid),"+
					"(SELECT COALESCE(namec,'') namec FROM sys_user x WHERE isdelete = FALSE AND id = a.saleid)," +
					"(SELECT COALESCE(namee,'') namee FROM sys_user x WHERE isdelete = FALSE AND id = a.saleid)," +
					"COALESCE(nos,'') nos FROM fina_jobs a WHERE isdelete = FALSE AND id = "+jobid;

			Map map = this.serviceContext.sysUserAssignMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlname);

			String codessql = "SELECT string_agg(COALESCE(usercode::TEXT,'-100'),',') AS codes,string_agg(COALESCE(userid::TEXT,'-100'),',') AS ids FROM _sys_user_assign t" +
					"  WHERE linktype = 'J' AND linkid = "+linkid+" AND userid IS NOT NULL";

			try {
				Map codesmap = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(codessql);
				if(map != null && map.size() > 0){
					if(codesmap.size() > 0 && codesmap.containsKey("codes") && codesmap.get("codes") != null){
						new StartThread(userid,fmsid,username,basepath,map,codesmap.get("codes").toString(),this.jobid,codesmap.get("ids").toString()).start();
						MessageUtils.alert("OK!消息已推送至工作单分派!");
					}else{
						//没有分派,推送给业务员自己
						new StartThread(userid,fmsid,username,basepath,map,map.get("code").toString(),this.jobid,null).start();
						MessageUtils.alert("OK!消息已推送至业务员:"+map.get("code").toString());
					}
				}
			} catch (NoRowException e) {
				new StartThread(userid,fmsid,username,basepath,map,map.get("code").toString(),this.jobid,null).start();
				MessageUtils.alert("OK!消息已推送至业务员:"+map.get("code").toString());
			}catch (Exception e) {
				e.printStackTrace();
			}
		} catch (NoRowException e) {
			Browser.execClientScript("showmsg()");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			this.refresh();
		}
	}


	@Bind
	public UIDataGrid gridCustomer;

	@Bind
	@SaveState
	public String customer = "";

	@Bind
	@SaveState
	public String hedgarapids = "";

    @Bind
    @SaveState
    public long hedgcustomerid = 0l;

	@Bind
	@SaveState
	public String nextAsCustomer = "";

	@Bind
	@SaveState
	public String qrycustomerdesc = "";

	@Bind
	@SaveState
	public String araphedgsql = "";

	/**
	 * 一键对冲
	 */
	@Action
	public void araphedging(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		try{
			Browser.execClientScript("hedgWindowJsVar.show();");
			araphedgsql = "SELECT * FROM f_fina_araphedging('jobid="+this.jobid+";arapid="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+"') AS billid";
			this.hedgarapids = StrUtils.array2List(ids);
			this.editGrid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	/**
	 * 一键对冲后，清空结算公司费用
	 */
	@Action
	public void delHedg() {
		this.nextAsCustomer ="";
		this.customer = "";
		update.markUpdate(true, UpdateLevel.Data, "hedgpanel");
	}

    /**
     * 一键对冲后，确定新生成的结算公司费用
     */
    @Action
    public void hedgReview() {

		if (this.hedgcustomerid == 0l) {
			this.alert("Please choose one row!");
			return;
		}

        String hedgsql = "SELECT * FROM f_fina_araphedging_generate('jobid=" + this.jobid + ";arapid=" + this.hedgarapids + ";userid="
				+ AppUtils.getUserSession().getUserid() + ";customerid=" + this.hedgcustomerid + "') AS hedgid";

        try{
			Browser.execClientScript("hedgWindowJsVar.hide();");
			daoIbatisTemplate.queryWithUserDefineSql4OnwRow(araphedgsql);
			daoIbatisTemplate.queryWithUserDefineSql4OnwRow(hedgsql);
			this.customer = "";
            this.hedgarapids = "";
            this.hedgcustomerid = 0l;
            this.nextAsCustomer = "";
            MessageUtils.alert("OK! 一键对冲新结算单位费用已生成");
            this.editGrid.reload();
        }catch(Exception e){
            MessageUtils.showException(e);
        }
    }

	/**
	 * 查询客户
	 */
	@Action
	public void qrycustomer() {
		this.gridCustomer.reload();
	}

	/**
	 * 选择客户提交
	 */
	@Action
	public void confirmCus() {
		String[] ids = this.gridCustomer.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAsCustomer.contains(id)){
				SysCorporation us = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
				this.customer = customer + us.getNamec();
				this.hedgcustomerid = us.getId();
				break;
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "hedgpanel");
	}

	/**
	 * 提交并关闭
	 */
	@Action
	public void confirmCusAndClose(){
		this.confirmCus();
		Browser.execClientScript("customerWindowJs.hide();");
	}

	/**
	 * 查询客户列表
	 * @return
	 */
	@Bind(id = "gridCustomer", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProviderCustomer() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridCustomer.page";
				Object[] obj = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMapUser), start, limit).toArray();
				return serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere2(qryMapUser), start, limit).toArray();
			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridCustomer.count";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	/**
	 * 新增代垫费用
	 */
	@Action
	public void substitute() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid=" + this.jobid+"&editType=substitute");
		} else {
			StringBuffer sb = new StringBuffer();
			for (String s : ids) {
				sb.append(s);
				sb.append(",");
			}
			dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid=" + this.jobid + "&arapid=" + sb.substring(0, sb.lastIndexOf(","))+"&editType=substitutechange");
		}
		update.markAttributeUpdate(dtlIFrameArap, "src");
		if (batchEditWindow != null)
            batchEditWindow.setTitle("代垫费用");
			batchEditWindow.show();
	}

	@Action
	public void refreshIFrameArapSubmit() {
		String data = AppUtils.getReqParam("iframArapData");
		dtlIFrameArap.setSrc("./iframarapedit.xhtml?jobid="+this.jobid+"&arapid="+data+"&editType=importArap");
	}

	@Action
	public void refreshIFrameQuoteSubmit() {
		dtlIFrameArap.load("./iframarapedit.xhtml?jobid=" + this.jobid+"&editType=add");
	}

	/**
	 * 毛利率按钮，默认为false，为true时显示毛利率和分币利润
	 */
	@Action
	public void showGprAction() {
		String str = AppUtils.getReqParam("isshowgpr");
		if ("true".equals(str)) {
			this.ishidegpr = true;
		}
		try {
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
