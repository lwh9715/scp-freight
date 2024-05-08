package com.scp.view.module.finance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
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
import com.scp.dao.sys.SysLogDao;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmAssign;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmProcessinstance;
import com.scp.model.data.DatAccount;
import com.scp.model.finance.FinaRpreq;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.sysmgr.filedownload.FiledownloadBean;

@ManagedBean(name = "pages.module.finance.payapplyeditBean", scope = ManagedBeanScope.REQUEST)
public class PayApplyEditBean extends GridView {

	@SaveState
	@Accessible
	public FinaRpreq selectedRowData = new FinaRpreq();

	@SaveState
	@Accessible
	public DatAccount selectedRowDataA = new DatAccount();

	@Bind
	public UIWindow showsearchWindow;

	@Bind
	public UIWindow serachWindows;

	@Bind
	@SaveState
	public String startTime;

	@Bind
	@SaveState
	public String endTime;

	@Bind
	@SaveState
	public String clsstartTime;

	@Bind
	@SaveState
	public String clsendTime;

	@Bind
	@SaveState
	public String etdstart;

	@Bind
	@SaveState
	public String etdend;

//	@SaveState
//	@Accessible
//	public FinaArap selectedRowDataArap = new FinaArap();

	@SaveState
	@Bind
	public String customerid;

	@SaveState
	public String finacustomerid;

	@Bind
	@SaveState
	public Long pkVal;

	@Bind
	@SaveState
	public boolean ispublic = false;

	@Bind
	public UIIFrame attachmentIframe;

	@Bind
	public UIWindow attachmentWindows;

	@Bind
	@SaveState
	public String araptype;

	@Bind
	@SaveState
	public String submtcontrol;

	@Bind
	@SaveState
	public String submtcontrol2;

	@Bind
	@SaveState
	public String Procname = "payment";

	@Bind
	@SaveState
	private Boolean ishideallarap = true;

	@Bind
	@SaveState
	private String jobnostr;

	@Bind
	@SaveState
	private String sonostr;

	@Bind
	@SaveState
	private String billsstr;
	
	@Bind
	@SaveState
	private String mblstr;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			applyGridUserDef();
			String type = (String) AppUtils.getReqParam("type");
			String idalies = (String) AppUtils.getReqParam("id");
			String id = idalies.split("\\.")[0];
			pkVal = Long.valueOf(id);
			customerid = (String) AppUtils.getReqParam("customerid");
			araptype = AppUtils.getReqParam("araptype");
			if("edit".equals(type)){
				this.selectedRowData = (FinaRpreq) serviceContext.reqMgrService.finaRpreqDao.findById(pkVal);
				this.customerid = selectedRowData.getCustomerid()==null?null:selectedRowData.getCustomerid().toString();
				showSumDtl();
				this.disableAllButton(this.selectedRowData.getIscheck());
				if(selectedRowData.getAccountid()!=null){
					String sql = "SELECT bankname,accountno,accountcont FROM sys_corpaccount WHERE id= "+selectedRowData.getAccountid();
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					if(m.size()>0){
						Browser.execClientScript("$('#account_input').val('"+ m.get("bankname").toString()+"')");
						selectedRowData.setAccountcont(String.valueOf(m.get("bankname"))+'\n'+String.valueOf(m.get("accountno"))+'\n'+String.valueOf(m.get("accountcont")));
					}
				}

				String sql = "SELECT count(*) FROM _fina_rpreqdtl f, fina_jobs j WHERE f.rpreqid = "+pkVal+" and j.id = f.jobid and j.isconfirm_bus = false ";
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(Integer.parseInt(map.get("count").toString()) > 0){
					submtcontrol2 = "N";
				}
			}else{
				add();
				if(StrUtils.isNull(customerid)){
					customerid = "-100";
				}
			}

			//途曦可以查看分单利润
			if (AppUtils.getUserSession().getCorpid() != 11540072274L) {
				Browser.execClientScript("showHMProfitJsvar.hide();");
			}

			if(!StrUtils.isNull(customerid) && !"-100".equals(customerid)) {
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(customerid));
				if(sysCorporation != null){
					this.selectedRowData.setCustomerid(sysCorporation.getId());
					this.selectedRowData.setCustomerabbr(sysCorporation.getNamec());
				}
			}
			//this.gridLazyLoad = true;
			findIshideAuto();
			showContract();
			submtcontrol = ConfigUtils.findSysCfgVal("CSNO");
			update.markUpdate(true, UpdateLevel.Data, "submtcontrol");
			update.markUpdate(true, UpdateLevel.Data, "submtcontrol2");
		}
	}

	@Bind
	public UIWindow addAccountWindows;

	@Bind
	public UIIFrame accountIframe;

	@Action
	public void saveRemark(){
		this.serviceContext.reqMgrService.saveRemark(selectedRowData.getRemark(),this.pkVal,AppUtils.getUserSession().getUsercode());
		update.markUpdate(true, UpdateLevel.Data, "remarks");
		Browser.execClientScript("remarkWindow.hide();");
	}

	@Action
	public void addAccount() {
		addAccountWindows.show();
		String targetUrl = "../customer/account.xhtml?customerid="+this.selectedRowData.getCustomerid();
		accountIframe.load(targetUrl);
	}




	@Bind
	public UIEditDataGrid arapGrid;


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
//			this.editGrid.reload();
//			//记录选择的行数到个人设置
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId+".grid.pagesize";
			try {
//				CfgUtil.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				 applyGridUserDef();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}


	@Action
	public void del(){
		try {
			this.serviceContext.reqMgrService.removeDate(this.pkVal);
			this.alert("OK!");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}



    @Bind(id = "arapGrid", attribute = "modifiedData")
    public List<Map> modifiedData;

    @SuppressWarnings("deprecation")
	@Bind(id = "arapGrid", attribute = "dataProvider")
	public GridDataProvider getArapDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.payapplyeditBean.grid.page";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(), start , limit );
				return list.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.payapplyeditBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere());
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	public Map getQryClauseWhere(){
		Map map = super.getQryClauseWhere(qryMap);


		if("-100".equals(customerid)){
			map.put("customerid",(StrUtils.isNull(this.finacustomerid))?"-100":this.finacustomerid);
		}else{
			map.put("customerid", customerid);
		}
		map.put("rpreqid", this.pkVal==null?-1:this.pkVal);

		String jobdate = "";
		if (!StrUtils.isNull(startTime) && !StrUtils.isNull(endTime)) {
			jobdate += "\nAND jobdate BETWEEN '"+startTime+"' AND '"+endTime+"'";
		}
		if(!StrUtils.isNull(clsstartTime) && !StrUtils.isNull(clsendTime)){
			jobdate += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND cls BETWEEN '"+clsstartTime+"'  AND '"+clsendTime+"'AND jobid = t.jobid)";
		}
		if(!StrUtils.isNull(etdstart) && !StrUtils.isNull(etdend)){
			jobdate += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND etd BETWEEN '"+etdstart+"'  AND '"+etdend+"'AND jobid = t.jobid)";
		}
		map.put("jobdate", jobdate);

		String corpfilter = "";
		if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
			corpfilter = "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = "+AppUtils.getUserSession().getCorpid()+") " +
					"\n			THEN " +
					"\n				(t.corpid = ANY(SELECT id FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())" +
						"								UNION ALL" +
						"								SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() +
						"								)" +
						" 			OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = t.arapid :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + AppUtils.getUserSession().getUserid() + " and ischoose = true))))"+

					"\n			ELSE " +
					"\n				(t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")" +
					" OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = t.arapid :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + AppUtils.getUserSession().getUserid() + " and ischoose = true))))"+
					"\n			END)";
		}else{
			corpfilter = "\n AND (t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")" +
			" OR (EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND corpid = 157970752274 AND id = t.arapid :: bigint) AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE AND corpid = ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = " + AppUtils.getUserSession().getUserid() + " and ischoose = true))))";
		}

		String filter = corpfilter +

		"AND ( EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = t.customerid AND xx.userid = "
		+ AppUtils.getUserSession().getUserid()
		+ ") OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.jobid AND x.linktype = 'J' AND x.userid ="
		+ AppUtils.getUserSession().getUserid() + ")" +
				"OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_user y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = t.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //关联的业务员的单，都能看到
		+ ")"
		+ "\n	OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_role y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = t.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //组关联业务员的单，都能看到
		+ ")"

		//能看所有外办订到本公司的单权限的人能看到对应单
		+ "\n	OR (EXISTS(SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = 3006994888) "
		+ "\n	AND EXISTS(SELECT 1 FROM fina_jobs c WHERE id = t.jobid AND c.isdelete =FALSE AND corpid <> "+AppUtils.getUserSession().getCorpidCurrent()+") AND "+AppUtils.getUserSession().getCorpidCurrent()+" = ANY((SELECT corpidop FROM fina_jobs c WHERE id = t.jobid AND c.isdelete =FALSE) UNION (SELECT corpidop2 FROM fina_jobs c WHERE id = t.jobid AND c.isdelete =FALSE) UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = t.jobid AND c.isdelete =FALSE)))"

		//过滤工作单指派
		+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
		+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_air y WHERE x.linkid = y.id AND y.jobid = t.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
		+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_truck y WHERE x.linkid = y.id AND y.jobid = t.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
		+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = t.jobid AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"

		+ "\n)"

		+ "";
		map.put("filter", filter);

		if(!StrUtils.isNull(araptype)&&araptype.equals("AR")){
			map.put("araptype", "\n AND a.araptype = 'AR'");
			map.put("rptype", "\n AND r.rptype = 'R'");
		}else if(!StrUtils.isNull(araptype)&&araptype.equals("AP")){
			String fina_payapply_showar = ConfigUtils.findSysCfgVal("fina_payapply_showar");
			if("Y".equals(fina_payapply_showar)){//付款申请包含应收
			}else{
				map.put("araptype", "\n AND a.araptype = 'AP'");
			}
			map.put("rptype", "\n AND r.rptype = 'P'");
		}

		if(ishideallarap){
			map.put("hideallarap", "AND false");
		}else{
			map.put("hideallarap", "");
		}
		//权限判断
		String sql="SELECT count(*) as isexit FROM sys_role sr , sys_modinrole am , sys_userinrole ur WHERE roletype = 'M' AND am.roleid = sr.id AND am.moduleid = 299185 AND ur.roleid = sr.id AND userid = '"+AppUtils.getUserSession().getUserid()+"'";
		Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);

		if(Integer.parseInt(map2.get("isexit").toString()) == 1){
			map.put("hidearapdat", "");
			map.put("hidefinal", "");
		}else{
			map.put("hidearapdat", "AND EXISTS(select 1 from dat_feeitem where a.feeitemid=id and ishidden=FALSE)");
			map.put("hidefinal", "AND EXISTS(select 1 from dat_feeitem where id = ANY(select a.feeitemid from fina_arap a where d.arapid=a.id) and ishidden=FALSE)");
		}

		if (!StrUtils.isNull(jobnostr)) {
			String[] jobnosplit = null;
			if (jobnostr.contains(" ")) {
				jobnosplit = jobnostr.split(" ");
			} else {
				jobnosplit = jobnostr.split(",");
			}
			map.put("jobnostr", FiledownloadBean.getInConditionilike(jobnosplit, "jobno"));
		}
		if(!StrUtils.isNull(sonostr)){
			map.put("sonostr", FiledownloadBean.getInConditionilike(sonostr.split(","), "sono"));
		}
		if(!StrUtils.isNull(billsstr)){
			map.put("billsstr", FiledownloadBean.getInConditionilike(billsstr.split(","), "bills"));
		}
		if(!StrUtils.isNull(mblstr)){
			map.put("mblstr", FiledownloadBean.getInConditionilike(mblstr.split(","), "mblno"));
		}
		return map;
	}



	@Action
	public void grid_ondblclick() {
//		Long id = this.getGridSelectId();
//		this.selectedRowDataArap = this.serviceContext.arapMgrService.finaArapDao.findById(id);
//		showEditBillDtlWin.show();
	}



	@Action
	public void add() {
		//this.pkVal = -100l;
		selectedRowData = new FinaRpreq();
		selectedRowData.setSingtime(new Date());
		selectedRowData.setReqtype("Q");
		selectedRowData.setRptype(araptype!=null&&araptype.equals("AR")?"R":"P");
		//this.disableAllButton(false);
		qryRefresh();
	}


	@Action
	public void save() {
		try{
			if(!StrUtils.isNull(this.finacustomerid)){
				selectedRowData.setCustomerid(Long.parseLong(this.finacustomerid));
			}

			if(selectedRowData.getCustomerid() <=0){
				MessageUtils.alert("客户名称不能为空!");
				return;
			}
			if(selectedRowData.getSingtime() == null){
				MessageUtils.alert("日期不能为空!");
				return;
			}

			//付款申请-生成单号根据费用第一条的结算地判断
			if (modifiedData.size() > 0) {
				selectedRowData.setCorpid(Long.parseLong(modifiedData.get(0).get("corpid").toString()));
			}

			this.serviceContext.reqMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			selectedRowData = this.serviceContext.reqMgrService.finaRpreqDao.findById(pkVal);
			this.serviceContext.reqMgrService.saveDataDtl(selectedRowData , modifiedData);
			MessageUtils.alert("OK");
			//this.arapGrid.reload();
			this.qryRefresh();
			Browser.execClientScript("setTimeout(function(){sumamount();}, 1500);");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
	}



	@Action
	public void changeAccountAction(){
		String accountid = AppUtils.getReqParam("accountid");
		DatAccount datAccount = this.serviceContext.accountMgrService.datAccountDao.findById(Long.parseLong(accountid));
		this.selectedRowData.setAccountcont(datAccount.getAccountcont());
		update.markUpdate(UpdateLevel.Data, "accountcont");
	}



	@Override
	public void qryRefresh() {
		if(pkVal > 0){
			this.selectedRowData = serviceContext.reqMgrService.finaRpreqDao.findById(pkVal);
		}
		super.qryRefresh();
		this.arapGrid.reload();
		update.markUpdate(UpdateLevel.Data, "editPanel");
		update.markUpdate(UpdateLevel.Data, "printer");
		update.markUpdate(UpdateLevel.Data, "printtime");
		Browser.execClientScript("autoCalcInit()");
		showSumDtl();
	}

	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "PA_AP.raq";

	@Action
	public void preview() {
		String rpturl = AppUtils.getRptUrl();
		try{
			if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
				MessageUtils.alert("请选择格式！");
				return;
			};
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/static/payapply/"+showwmsinfilename+"&id="+this.pkVal+ "&userid="
			+ AppUtils.getUserSession().getUserid();
			AppUtils.openWindow("_print", openUrl);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'PayApply' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

//	@Override
//	protected void doServiceFindData() {
//		selectedRowData = this.serviceContext.statementMgrService.finaStatementDao.findById(pkVal);
//	}
//
//	@Override
//	protected void doServiceSave() {
//		this.serviceContext.statementMgrService.saveData(selectedRowData);
//		this.pkVal = selectedRowData.getId();
//		
//		MsgUtil.alert("OK");
//		doServiceFindData();
//	}


	@Bind
	private UIIFrame dtlIFrame;

	@Action
	public void showSumDtl() {
		String url = "./rpreqsum.xhtml?id="+this.pkVal;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
	}

	@Bind
	@SaveState
	private String contractLinkId;

	@Action
	public void showContract() {
		//检查客商是否有合同附件
		String sql = "SELECT id from sys_attachment where linkid = " + this.customerid + " AND roleid = 6594782199 limit 1";
		List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		if (null != list && list.size()>0) {
			this.contractLinkId = list.get(0).get("id").toString();
		} else {
			Browser.execClientScript("contractLinkIdJs.hide();");
		}
	}

	@Bind
	public UIButton add;
	@Bind
	public UIButton save;
	@Bind
	public UIButton del;
	@Bind
	public UIButton setAllDefaultAction;
	@Bind
	public UIButton setALLNullAction;

	@Bind
	public UIButton delBatch;

	private void disableAllButton(Boolean ischeck) {
//		add.setDisabled(ischeck);
		save.setDisabled(ischeck);
		setAllDefaultAction.setDisabled(ischeck);
		setALLNullAction.setDisabled(ischeck);
		//chooseFee.setDisabled(ischeck);
		delBatch.setDisabled(ischeck);
	}

	@Bind
	private UIWindow chooseFeeWindows;

	@Bind
	private UIIFrame chooseFeeIFrame;

	@Bind
	public String gridPanel2;
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();

	@Action
	public void chooseFee() {
		update.markUpdate(true,UpdateLevel.Data,gridPanel2);
		showsearchWindow.show();
		Map<String, String> map = DateTimeUtil.getFirstday_Lastday_3Month();
		startDateJ = map.get("first");
		endDateJ = map.get("last");
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");

	}

	@Bind
	public UIDataGrid grid;
	@Bind
	@SaveState
	private String startDateJ;

	@Bind
	@SaveState
	private String endDateJ;

	@Bind(id = "grid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.unlinkfeechooserBean.grid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere3(qryMapShip), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				//String sqlId = "pages.module.finance.unlinkfeechooserBean.grid.count";
//				List<Map> list = serviceContext.daoIbatisTemplate
//						.getSqlMapClientTemplate().queryForList(sqlId,
//								getQryClauseWhere3(qryMapShip));
//				Long count = (Long) list.get(0).get("counts");
				Long count = 10000l;
				return count.intValue();
			}
		};
	}

	@Action
	public void qrygird() {
		this.grid.reload();
	}
	@Bind
	@SaveState
	private String qrydates;

	@Bind
	@SaveState
	private String qryves;

	@Bind
	@SaveState
	private String qryvoy;

	@Bind
	@SaveState
	private String qrysales;

	@Bind
	@SaveState
	private String qryNos;

	public Map getQryClauseWhere3(Map<String, Object> queryMap) {

		Map map = super.getQryClauseWhere(queryMap);
		map.put("customerid", customerid);
		map.put("reqtype", "'Q'");
		String qry = StrUtils.getMapVal(map, "qry");
		//qry += "\nAND NOT EXISTS(SELECT 1 FROM fina_rpreqdtl x WHERE x.arapid = t.arapid AND x.isdelete = FALSE  AND x.rpreqid = "+pkVal+")";
		//if(this.reqtype.equals("Q")){
		//	qry+=" AND t.araptype = 'AP'";
		//}
		map.put("qry", qry);

		String filter = "\n AND EXISTS (SELECT 1 FROM sys_user_corplink x , fina_arap y WHERE x.corpid = y.corpid AND y.jobid = t.jobid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+") " +
		"\n AND (EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.jobid AND x.linktype = 'J' AND x.userid ="
		+ AppUtils.getUserSession().getUserid() + ")" +
				"OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_user y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = t.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //关联的业务员的单，都能看到
		+ ")"
		+ "\n	OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_role y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND EXISTS(SELECT 1 FROM fina_jobs job WHERE job.id = t.jobid AND job.saleid = z.id AND job.isdelete = FALSE)) " //组关联业务员的单，都能看到
		+ "))";
		if(!StrUtils.isNull(qryves)){ //船名
			filter+=filter+"\nAND vessel ILIKE '%"+qryves+"%'";
		}
		if(!StrUtils.isNull(qryvoy)){//船次
			filter+=filter+"\nAND voyage="+qryvoy;
		}
		if(!StrUtils.isNull(qrysales)){//业务
			filter+=filter+"\nAND sales ILIKE '%"+qrysales+"%'";
		}
		if(!StrUtils.isNull(qryNos)){//单号
			filter+=filter+"\nAND (jobno ILIKE '%"+qryNos+"%' OR mblno ILIKE '%"+qryNos+"%'OR sono ILIKE '%"+qryNos+"%')";
		}
		if(!StrUtils.isNull(startDateJ) || !StrUtils.isNull(endDateJ)) {
			if(qrydates.equals("jobdate")){
				filter+=filter+ "\nAND jobdate::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'";
			}else if(qrydates.equals("etd")){
				filter+=filter+ "\n AND (CASE " +
								"		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'S' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.etd::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'L' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.loadtime::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'A' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.flightdate1::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								" 		 WHEN EXISTS(SELECT 1 FROM fina_jobs xx WHERE xx.jobtype = 'C' AND xx.id = T.jobid) THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = T.jobid AND x.singtime::DATE BETWEEN '" + startDateJ + "' AND '" + endDateJ + "'))" +
								"  END)";
			}
		}
		map.put("filter", filter);
		return map;
	}

//	@Action
//	public void savegrid() {
//		if(this.pkVal < 1){
//			MessageUtils.alert("Please save first!请先保存付款申请单!");
//			return;
//		}
//		String ids[] = this.grid.getSelectedIds();
//		String sql = "";
//		for (String id : ids) {
//			String jobid = id.substring(0, id.indexOf("."));
//			String arapids = "SELECT x.id AS arapid FROM fina_arap x WHERE x.jobid = "+jobid+" AND x.isdelete = false AND x.rptype = 'G' AND x.customerid = "+customerid+" AND x.corpid = ANY (SELECT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
//			List<Map> lists = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(arapids);
//			for (Map map : lists) {
//				String arapid = StrUtils.getMapVal(map, "arapid");
//				sql += "\n INSERT INTO fina_rpreqdtl(id,rpreqid,arapid,corpid,inputer,inputtime) VALUES(getid(),"+this.pkVal+","+arapid+","+AppUtils.getUserSession().getCorpid()+",'"+AppUtils.getUserSession().getUsercode()+"',now());";
//			}
//			//			FinaRpreqdtl finaRpreqdtl = new FinaRpreqdtl();
////			finaRpreqdtl.setRpreqid(pkVal);
////			finaRpreqdtl.setArapid(Long.valueOf(id));
////			this.serviceContext.reqMgrService.saveData(finaRpreqdtl);
//		}
//		try {
//			//System.out.println(sql);
//			this.serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
//			MessageUtils.alert("OK");
//			this.grid.reload();
//			this.qryRefresh();
//			//Browser.execClientScript("parent.chooseFeeWindows.hide()");
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
//	}

	@Action(id = "chooseFeeWindows", event = "onclose")
	private void dtlEditDialogClose() {
		this.qryRefresh();

	}

	@Action
	public void delBatch() {
		String[] ids = this.arapGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			this.serviceContext.reqMgrService.delBatch(ids);
			MessageUtils.alert("删除成功");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}



	@Override
	protected void applyGridUserDef() {
		String gridid  = this.getMBeanName() + ".arapGrid";
		String gridJsvar = "arapGridJsvar";
		super.applyGridUserDef(gridid , gridJsvar);
	}

	@Bind
	@SaveState
	private String processId = "PaymentRequest";//付款申请

	@Bind
	@SaveState
	private String processarId = "PaymentarRequest";//收款申请

	@Resource(name="transactionTemplate")
	private TransactionTemplate transactionTemplate;


	@Bind
	@SaveState
	public String nextAssignUser = "";

	@Bind
	@SaveState
	public String bpmremark = "";

	@Bind
	@SaveState
	public String taskname;

	@Bind(id="taskDefine")
	@SelectItems
	public List<SelectItem> taskDefine = new ArrayList<SelectItem>();

    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{

			//查询是否迪拜结算地
			String corpsql = "SELECT (SELECT fa.corpid FROM fina_arap fa WHERE fa.id = r.arapid AND fa.isdelete = FALSE LIMIT 1) AS corpid FROM fina_rpreqdtl r WHERE r.rpreqid = " + this.pkVal + " LIMIT 1";
			Map corpidStr = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(corpsql.toString());
			//TODO:迪拜结算地 走迪拜付款流程
			if (corpidStr.get("corpid").toString().equals("399480672274")) {
				this.Procname = "payment_gdb";
				Browser.execClientScript("ProcnameJs.setValue('payment_gdb');");
			}

			String processName = null;
			if("payment".equals(Procname)){
				processName = "付款申请";
			}
			if("payment_gdb".equals(Procname)){
				processName = "付款申请（GDB）";
			}
			if("payment_lcl".equals(Procname)){
				processName = "付款申请-LCL";
			}
			if("payment2".equals(Procname)){
				processName = "供应商票结付款申请";
			}
			if("reverse".equals(Procname)){
				processName = "冲账申请";
			}
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
			String sql = "SELECT DISTINCT taskname ,step FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+"  and isauto = false AND isdelete = false AND taskname NOT IN('Start','End') order by step";
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
	private String amtreqamttosum;

	@Action
	public void startFF() {
		if(this.pkVal <=0 ){
			MessageUtils.alert("Please save first!");
			return;
		}
		if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
			try{
				taskDefine = getTaskDefine();
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processName = null;
				if("payment".equals(Procname)){
					processName = "付款申请";
				};
				if("payment_gdb".equals(Procname)){
					processName = "付款申请（GDB）";
				}
				if("payment_lcl".equals(Procname)){
					processName = "付款申请-LCL";
				};
				if("payment2".equals(Procname)){
					processName = "供应商票结付款申请";
				};
				if("reverse".equals(Procname)){
					processName = "冲账申请";
				};
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+this.selectedRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str =  s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
				}
				Browser.execClientScript("bpmWindowJsVar.show();");

				setDefaultApprover(bpmProcess);
				return;
			}catch(Exception e){
				MessageUtils.showException(e);
			}
		}
	}

	private void setDefaultApprover(BpmProcess bpmProcess) {
		try {
			if ("付款申请".equals(bpmProcess.getNamec())) {
				SysUser createrUser = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(String.valueOf(AppUtils.getUserSession().getUserid())));
				String corpid = String.valueOf(createrUser.getSysCorporation().getId());
				List<BpmAssign> bpmAssignList = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere("process_id = '" + bpmProcess.getId() + "' AND taskname = '总经理审核'");
				SysUser touserUser = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere("code = '" + bpmAssignList.get(0).getTousercode()+"'");

				String ss =
						"SELECT\n" +
						"\tuserid\n" +
						"\t,(SELECT x.namec FROM sys_user x where x.id = userid AND x.isdelete = false) AS usernamec\n" +
						"\t,(select corpid from sys_user where id=userid) AS corpid\n" +
						"FROM  bpm_assign_ref a WHERE assignid= " + bpmAssignList.get(0).getId() + "";
				List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(ss);

				if (mapList != null && mapList.size() > 0) {
					for (int i = 0; i < mapList.size(); i++) {
						if (corpid.equals(mapList.get(i).get("corpid").toString())) {
							nextAssignUser = String.valueOf(mapList.get(i).get("userid"));
							user = mapList.get(i).get("usernamec") + ",";
						}
					}
					if ("0".equals(nextAssignUser)) {
						nextAssignUser = String.valueOf(touserUser.getId());
						user = touserUser.getNamec() + ",";
					}
				}

				//青岛默认总经理审核
//				if("1122274".equals(corpid)){
//					nextAssignUser = "405489182274";
//					user = "朱建政,";
//				}
				if("100".equals(corpid)){
					nextAssignUser = "2245939888";
					user = "钱伟荣,";
				}
				update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
			} else if ("付款申请（GDB）".equals(bpmProcess.getNamec())) {
				SysUser createrUser = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(String.valueOf(AppUtils.getUserSession().getUserid())));
				String corpid = String.valueOf(createrUser.getSysCorporation().getId());
				List<BpmAssign> bpmAssignList = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere("process_id = '" + bpmProcess.getId() + "' AND taskname = '总经理审核'");
				SysUser touserUser = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere("code = '" + bpmAssignList.get(0).getTousercode()+"'");

				String ss = "SELECT\n" +
						"\tuserid\n" +
						"\t,(SELECT x.namec FROM sys_user x where x.id = userid AND x.isdelete = false) AS usernamec\n" +
						"\t,(select corpid from sys_user where id=userid) AS corpid\n" +
						"FROM  bpm_assign_ref a WHERE assignid= " + bpmAssignList.get(0).getId() + "";
				List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(ss);

				if (mapList != null && mapList.size() > 0) {
					for (int i = 0; i < mapList.size(); i++) {
						if (corpid.equals(mapList.get(i).get("corpid").toString())) {
							nextAssignUser = String.valueOf(mapList.get(i).get("userid"));
							user = mapList.get(i).get("usernamec") + ",";
						}
					}
					if ("0".equals(nextAssignUser)) {
						nextAssignUser = String.valueOf(touserUser.getId());
						user = touserUser.getNamec() + ",";
					}
				}

				update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 付款申请在BMS进行
	 */
	@Action
	public void startbmsFF(){
		String[] ids = this.arapGrid.getSelectedIds();
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
		String url = ConfigUtils.findSysCfgVal("sys_bms_expense_url");//获取系统配置UMS URL
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try{
			synUfmsAndBms.expense(url+id);//跳转BMS请款 各种跳转URL大体一致，param代表各种类型，比如请款、开票，类型不同systemid参数名内容不同
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

//	@Action
//	public void confirmFee(){
//		String[] ids = this.arapGrid.getSelectedIds();
//		if (ids == null || ids.length == 0) {
//			MessageUtils.alert("请先勾选要确认的行");
//			return;
//		}
//		try {
//			this.serviceContext.reqMgrService.finishDate(ids , AppUtils.getUserSession().getUsercode() ,true);
//			qryRefresh();
//		} catch (Exception e) {
//			//MsgUtil.showException(e);
//		}
//		
//	}

//	@Action
//	public void unConfirmFee(){
//		String[] ids = this.arapGrid.getSelectedIds();
//		if (ids == null || ids.length == 0) {
//			MessageUtils.alert("请先勾选要确认的行");
//			return;
//		}
//		try {
//			this.serviceContext.reqMgrService.finishDate(ids , AppUtils.getUserSession().getUsercode() ,false);
//			qryRefresh();
//		} catch (Exception e) {
//			//MsgUtil.showException(e);
//		}
//		
//	}

	@Action
	public void showAttachment(){
		attachmentIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid="
				+ this.pkVal+"&code=payapply");
		attachmentWindows.show();
	}

	@Action
	public void changelist(){
		this.customerid = (String) AppUtils.getReqParam("costomerid");
		this.finacustomerid = (String) AppUtils.getReqParam("costomerid");
		this.arapGrid.reload();
	}


	@Action
	public void applyBPM() {
		if(this.pkVal==null||!(pkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}
		Double d = 0d;
		try {
			String processName = null;
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("select SUM(amtreq) as amounts,sum(f_amtto((SELECT jobdate FROM fina_jobs WHERE id = (SELECT jobid FROM fina_arap a WHERE a.id = arapid AND a.isdelete = FALSE limit 1) AND isdelete = FALSE limit 1), (SELECT currency FROM fina_arap a WHERE a.id = arapid AND a.isdelete = FALSE limit 1), 'CNY', amtreq)) AS amtreqcny from  fina_rpreqdtl WHERE rpreqid = "+selectedRowData.getId()+" AND isdelete = FALSE");
			d = Double.valueOf(String.valueOf(m.get("amtreqcny")));
			if("reverse".equals(Procname)){//冲账申请不进行费用确认
				if(Double.valueOf(String.valueOf(m.get("amounts"))) != 0d){
					MessageUtils.alert("对冲金额不为0");
					return;
				}
				processName = "冲账申请";
				taskname="财务审核";
			}else{
				//进行费用确认验证 + this.selectedRowData.getNos() + "'
				String sql = "SELECT COALESCE(String_agg((SELECT NOS FROM fina_jobs WHERE id = jobid AND isdelete = false)||'',','),'') AS nos,String_agg(distinct corpid||'','') AS corpid  FROM fina_arap WHERE id = ANY(SELECT arapid FROM _fina_rpreq fr, _fina_rpreqdtl frd where  fr.id = frd.rpreqid and frd.amtreq <> 0  and fr.nos ='" + this.selectedRowData.getNos() + "') AND isdelete = FALSE AND isconfirm = FALSE";
				Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if("399480672274".equals(String.valueOf(map.get("corpid"))) == false){//除了迪拜，发起付款申请都要进行费用确认
					if(map.containsKey("nos") == false || StrUtils.isNull(String.valueOf(map.get("nos"))) == false){
						MessageUtils.alert(String.valueOf(map.get("nos"))+"需要费用确认才能发起付款申请！");
						return;
					}
				}
			}

			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser)){
					nextAssignUser="0";
				}
				if("payment".equals(Procname)){
					processName = "付款申请";
					taskname="总经理审核";
				}
				if("payment_gdb".equals(Procname)){
					processName = "付款申请（GDB）";
					taskname="分公司总经理审核";
				}
				if("payment_lcl".equals(Procname)){
					processName = "付款申请-LCL";
				}
				if("payment2".equals(Procname)){
					processName = "供应商票结付款申请";
				}

				String sql = "SELECT d.arapbranch FROM fina_rpreq fr LEFT JOIN _fina_rpreqdtl d ON (fr.id = d.rpreqid AND d.isdelete = FALSE) where fr.nos ='" + this.selectedRowData.getNos() + "' AND fr.isdelete = FALSE limit 1";
				Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String arapbranch = String.valueOf(map.get("arapbranch"));
				boolean	flag = false;//是分公司 暂时只判断青岛江苏宁波
				if(
//						"CIMCGLJS".equals(arapbranch)			//江苏
//						||	
						"CIMCGLNB".equals(arapbranch)	//宁波
//						||	"CIMCGLQD".equals(arapbranch)	//青岛
					){
					flag = true;
				}

				//2023-11-02 迪拜付款申请流程
				if("payment_gdb".equals(Procname)){
					boolean flag2 = d > 100000 ? true : false;//判断金额是否大于10W
					String processinsVar = "#大于十万CNY"+"-"+flag2+"-"+"大于十万";
					BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
					String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+bpmremark+";taskname="+taskname+";refno="+this.selectedRowData.getNos()+";refid="+this.selectedRowData.getId()+";processinsVar="+processinsVar+"') AS rets;";
					Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
					String sub =  sm.get("rets").toString();
					MessageUtils.alert("OK");
					Browser.execClientScript("bpmWindowJsVar.hide();");
					if(bpmProcess!=null){
						BpmProcessinstance bpmProcessinstance =serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findOneRowByClauseWhere("refid = '"+this.selectedRowData.getId()+"' AND state <> 8 AND state <> 9 AND isdelete = FALSE AND processid ="+bpmProcess.getId());
						serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "amtreqamttosum", amtreqamttosum,"折合CNY");
					}
				} else {
					//2023-11-02 公共付款申请流程
					boolean flag2 = d > 200000 ? true : false;//判断金额是否大于20W
					//processinsVar分支条件，分别对应bpm_processins_var的name,val,lable
					String processinsVar = "分公司"+"-"+flag+"-"+"分公司";
					processinsVar += "#大于二十万CNY"+"-"+flag2+"-"+"大于二十万";
					BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
					String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+bpmremark+";taskname="+taskname+";refno="+this.selectedRowData.getNos()+";refid="+this.selectedRowData.getId()+";processinsVar="+processinsVar+"') AS rets;";
					Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
					String sub =  sm.get("rets").toString();
					MessageUtils.alert("OK");
					Browser.execClientScript("bpmWindowJsVar.hide();");
					if(bpmProcess!=null){
						BpmProcessinstance bpmProcessinstance =serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findOneRowByClauseWhere("refid = '"+this.selectedRowData.getId()+"' AND state <> 8 AND state <> 9 AND isdelete = FALSE AND processid ="+bpmProcess.getId());
						serviceContext.bpmProcessinsVarService.save(bpmProcessinstance.getId(), "amtreqamttosum", amtreqamttosum,"折合CNY");
					}
				}

//TODO 付款申请触发点迁移到收付款，待确认
//				//付款申请生成发票号
//				if("payment".equals(Procname)){
//					processName = "付款申请";
//					String sql = "SELECT * FROM f_fina_invoice_create('rpreqid=" + pkVal + ";userid=" + AppUtils.getUserSession().getUserid() + ";flag=createinvoicebypayment')  as returntext;";
//					Map m1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//					String returntext = m1.get("returntext").toString();
////					MessageUtils.showMsg("发票号为" + returntext);
//
//					returntext = returntext.replaceAll(",", "");
//					String sql2 = "select * from fina_invoice where invoiceno='" + returntext + "'";
//					Map m2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2);
//					if (!m2.isEmpty()) {
//						// AppUtils.openNewPage(AppUtils.getContextPath() + "/pages/module/finance/invoicedtl.aspx?&id=" + m2.get("id"));
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

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
		map.put("qry", qry);
		return map;
	}


	@Action
	public void qryuser() {
		this.gridUser.reload();
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

	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}

	@Action
	public void setAllDefaultAction() {
		Browser.execClientScript("changeModify('A');");
	}


	//@Bind
	//@SaveState
	//public boolean isprint = false;

	@Action
	public void isprintAjaxSubmit() {
		Boolean isPrint = selectedRowData.getIsprint();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (!isPrint) {
			sql = "UPDATE fina_rpreq SET isprint = TRUE ,printtime = NOW(),printer = '"+updater+"',printcount = COALESCE(printcount,0) + 1 WHERE id ="+this.pkVal+";";
		}else {
			sql = "UPDATE fina_rpreq SET isprint = FALSE ,printtime = NULL,printer = '"+updater+"' WHERE id ="+this.pkVal+";";
		}
		try {
			serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			qryRefresh();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsprint(!isPrint);
			selectedRowData.setPrinter(isPrint?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setPrinttime(isPrint?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
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


	@Action
	public void saveIshideAuto(){
		try {
			ConfigUtils.refreshUserCfg("payapply_ishideallarap",ishideallarap.toString(), AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void findIshideAuto(){
		try{
			String findUserCfgVal = ConfigUtils.findUserCfgVal("payapply_ishideallarap",  AppUtils.getUserSession().getUserid());
			ishideallarap = Boolean.valueOf(findUserCfgVal);
		}catch(Exception e){
			ishideallarap = true;
		}
	}

	@Bind
	public UIWindow arapEditWindow;

	@Bind
	public UIIFrame arapEditIFrame;

	@Bind
	@SaveState
	public String jobid;

	/**
	 * 付款申请-费用弹窗
	 */
	@Action
	public void arapEdit() {
		this.jobid = (String) AppUtils.getReqParam("jobid");
		arapEditIFrame.setSrc("/scp/pages/module/finance/arapedit.xhtml?jobid=" + this.jobid);
		update.markAttributeUpdate(arapEditIFrame, "src");
		if (arapEditWindow != null) {
			arapEditWindow.show();
		}
	}

    @Action
    public void showJobssubmit() {
        try {
            String[] ids = this.arapGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                this.alert("Please choose one row!");
                return;
            }

            String id = ids[0].split("-")[0];
            String querySql = "SELECT COALESCE((SELECT y.jobid FROM fina_rpreqdtl x,fina_arap y WHERE x.arapid = y.id AND x.id = " + id + " LIMIT 1)," +
                    "(select jobid from fina_arap where id = " + id + " LIMIT 1)) AS jobid";
            Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String jobid = StrUtils.getMapVal(map, "jobid");
            FinaJobs jobs = serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid == null ? "" : jobid));
            if (jobs.getJobtype().equals("S")) {
                String url = "/scp/pages/module/ship/jobsedit.xhtml?id=" + jobid;
				AppUtils.openNewPage(url);
            } else if (jobs.getJobtype().equals("A")) {
                String url = "/scp/pages/module/air/jobsedit.xhtml?id=" + jobid;
				AppUtils.openNewPage(url);
            } else if (jobs.getJobtype().equals("L")) {
                String url = "/scp/pages/module/land/jobsedit.xhtml?id=" + jobid;
				AppUtils.openNewPage(url);
            } else if (jobs.getJobtype().equals("D")) {
                String url = "/scp/pages/module/commerce/jobsedit.html?id=" + jobid;
				AppUtils.openNewPage(url);
            } else {
                alert("工作单不存在！");
                return;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	@Bind
	public UIWindow showHMProfitWindow;

	@Bind
	public UIIFrame showHMProfitIFrame;

	@Action
	public void showHMProfit() {
		showHMProfitIFrame.setSrc("/scp/pages/module/commerce/payprofit.html?id=" + this.pkVal);
		update.markAttributeUpdate(showHMProfitIFrame, "src");
		if (showHMProfitWindow != null) {
			showHMProfitWindow.setTitle("分单利润");
			showHMProfitWindow.show();
		}
	}

}
