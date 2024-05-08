package com.scp.view.module.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.scp.view.sysmgr.filedownload.FiledownloadBean;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.data.DatAccount;
import com.scp.model.data.DatBank;
import com.scp.model.finance.FinaRpreq;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import org.springframework.util.StringUtils;

@ManagedBean(name = "pages.module.finance.checkbilleditBean", scope = ManagedBeanScope.REQUEST)
public class CheckBillEditBean extends GridView {

	@SaveState
	@Accessible
	public FinaRpreq selectedRowData = new FinaRpreq();

	@Bind
	@SaveState
	public Long userid = AppUtils.getUserSession().getUserid();


	@Bind
	public UIButton confirmArAp;

	@Bind
	public UIButton cancelArAp;

	@Bind
	public UIWindow serachWindows;
//	@SaveState
//	@Accessible
//	public FinaArap selectedRowDataArap = new FinaArap();

	@SaveState
	@Bind
	public String customerid;


	@Bind
	@SaveState
	private Boolean ishideallarap = false;

	@Bind
	@SaveState
	public Long pkVal;

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
	public String atdstart;

	@Bind
	@SaveState
	public String atdend;

	@SaveState
	@Bind
	public String corpidtitile = "100";

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

			String is_hideallarap = ConfigUtils.findUserCfgVal("checkbill_ishideallarap", AppUtils.getUserSession().getUserid());
			//1891 工作单费用确认取消按钮按权限控制灰掉
			if (!getCheckright("ConfirmArAp",AppUtils.getUserSession().getUserid())) confirmArAp.setDisabled(true);
			if (!getCheckright("CancelArAp",AppUtils.getUserSession().getUserid())) cancelArAp.setDisabled(true);
			if ("Y".equals(is_hideallarap)) {
				ishideallarap = true;
			}

			if("edit".equals(type)){
				this.selectedRowData = (FinaRpreq) serviceContext.reqMgrService.finaRpreqDao.findById(pkVal);
				this.customerid = selectedRowData.getCustomerid()==null?null:selectedRowData.getCustomerid().toString();
				showSumDtl();
				this.disableAllButton(this.selectedRowData.getIscheck());
				if (this.selectedRowData.getIslock()) {
					this.islock = true;
					save.setDisabled(this.islock);
				}
			}else{
				add();
			}
			if(!StrUtils.isNull(customerid)) {
				SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(customerid));
				this.selectedRowData.setCustomerid(sysCorporation.getId());
				this.selectedRowData.setCustomerabbr(sysCorporation.getNamec());
			}
			update.markUpdate(UpdateLevel.Data,"pkVal");

			corpidtitile = String.valueOf(AppUtils.getUserSession().getCorpid());

			sumHideDivValue();
		}
	}


	public void sumHideDivValue(){
		try {
			List<Map>  list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql("SELECT SUM(amtreq) AS amtreq,currency FROM (SELECT amtreq*(SELECT (CASE WHEN araptype = 'AR' THEN -1 ELSE 1 END) FROM fina_arap WHERE id = arapid AND isdelete = FALSE) as amtreq,(SELECT currency FROM fina_arap WHERE id = arapid AND isdelete = FALSE) AS currency from  fina_rpreqdtl where  rpreqid  = "+pkVal+" AND isdelete = FALSE AND amtreq <> 0) T GROUP BY currency");
			String str = "";
			for (int i = 0; i < list.size(); i++) {
				if("".equals(str)){
					str = list.get(i).get("currency")+":"+list.get(i).get("amtreq");
				}else{
					str += "&"+list.get(i).get("currency")+":"+list.get(i).get("amtreq");
				}
			}
			if(list.size()>0){
				Browser.execClientScript("sumcurrencyhideJs.setValue('"+str+"');");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void applyGridUserDef() {
		String gridid  = this.getMBeanName() + ".arapGrid";
		String gridJsvar = "arapGridJsvar";
		super.applyGridUserDef(gridid , gridJsvar);
	}

	@Bind
	public UIEditDataGrid arapGrid;



	@Action
	public void del(){
		this.serviceContext.reqMgrService.removeDate(this.pkVal);
		this.alert("OK!");
	}



    @Bind(id = "arapGrid", attribute = "modifiedData")
    public List<Map> modifiedData;


	@Bind(id = "arapGrid", attribute = "dataProvider")
	public GridDataProvider getArapDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.checkbilleditBean.grid.page";
				return serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.checkbilleditBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere());
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

//	public Map getQryClauseWhere(){
//		//qryMap.put("customerid$", customerid);
//		
//		Map map = super.getQryClauseWhere(qryMap);
//		map.put("customerid", customerid);
//		if(pkVal == -1l){
//			map.put("clause", "(rpreqid = 0)");
//		}else {
//			map.put("clause", "(rpreqid = "+pkVal+") ");
//		}
//		
//		return map;
//	}
//	

	public Map getQryClauseWhere(){
		Map map = super.getQryClauseWhere(qryMap);
		map.put("customerid", customerid==null?"-1":customerid);
		map.put("rpreqid", this.pkVal);

		String jobdate = "";
		if (!StrUtils.isNull(startTime) && !StrUtils.isNull(endTime)) {
			jobdate += "\nAND jobdate BETWEEN '"+startTime+"' AND '"+endTime+"'";

		}
		if(!StrUtils.isNull(jobnostr)){
			String[] jobnosplit = null;
			if (jobnostr.contains(" ")) {
				jobnosplit = jobnostr.split(" ");
			} else {
				jobnosplit = jobnostr.split(",");
			}
			map.put("jobnostr", FiledownloadBean.getInConditionilike(jobnosplit, "t.jobno"));
		}
		if(!StrUtils.isNull(sonostr)){
			map.put("sonostr", FiledownloadBean.getInConditionilike(sonostr.split(","), "t.sono"));
		}
		if(ishideallarap){
			map.put("hideallarap", "AND false");
		}else{
			map.put("hideallarap", "");
		}
		if(!StrUtils.isNull(clsstartTime) && !StrUtils.isNull(clsendTime)){
			jobdate += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND cls BETWEEN '"+clsstartTime+"'  AND '"+clsendTime+"'AND jobid = t.jobid)";
		}
		if(!StrUtils.isNull(atdstart) && !StrUtils.isNull(atdend)){
			jobdate += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND cls BETWEEN '"+atdstart+"'  AND '"+atdend+"'AND jobid = t.jobid)";
		}
		map.put("jobdate", jobdate);
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer sbcorp = new StringBuffer();
//			sbcorp.append("\n AND a.corpid="+AppUtils.getUserSession().getCorpidCurrent());
//			map.put("corpfilter", sbcorp.toString());
//		}
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE a.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		map.put("corpfilter", corpfilter);
		String filter = "";
		map.put("filter", filter);
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
		selectedRowData = new FinaRpreq();
		selectedRowData.setSingtime(new Date());
		selectedRowData.setReqtype("S");
		selectedRowData.setCustomerid(Long.valueOf(customerid));
		this.pkVal = -1l;
		this.disableAllButton(false);
//		selectedRowData.setAccountid(0l);
		qryRefresh();
	}



	@Action
	public void save() {

		//对账申请-生成单号根据费用第一条的结算地判断
		if (modifiedData.size() > 0) {
			selectedRowData.setCorpid(Long.parseLong(this.modifiedData.get(0).get("corpid").toString()));
		}

		this.serviceContext.reqMgrService.saveData(selectedRowData);
		this.pkVal = selectedRowData.getId();

		selectedRowData = this.serviceContext.reqMgrService.finaRpreqDao.findById(pkVal);

		this.serviceContext.reqMgrService.saveDataDtl(selectedRowData , modifiedData);

		MessageUtils.alert("OK");

		this.qryRefresh();
		sumHideDivValue();
	}

	@SaveState
	public String type;

	@Bind
	@SaveState
	public String detailsContent;

	@Bind
	@SaveState
	public String detailsTitle;

	@Bind
	public UIWindow detailsWindow;

	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");


		if ("1".equals(type)) { // 账号大框
			this.detailsContent = content;
			this.detailsTitle = "账号";
		}
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");

		this.detailsWindow.show();

	}
	/**
	 * 输入框(大框)保存
	 */
	@Action
	public void saveDetails() {
		setDetails(this.type);
		this.save1();
		this.detailsWindow.close();
	}
	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.type);
		this.detailsWindow.close();
	}
	public void setDetails(String type) {
		if ("1".equals(type)) {
			this.selectedRowData.setAccountcont(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "accountcont");
		}
	}

	public void save1(){
		try{
			String sql = "update fina_rpreq SET accountid = "+selectedRowData.getAccountid()+", accountcont = '"+selectedRowData.getAccountcont()+"',updater = '"+AppUtils.getUserSession().getUsercode()+"',updatetime = NOW() WHERE id = "+selectedRowData.getId()+" AND isdelete = FALSE ";
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.showMsg("OK");
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showMsg("保存失败");
		}
	}



	@Action
	public void changeAccountAction(){

		String accountid = AppUtils.getReqParam("accountid");
		if(!accountid.isEmpty()){
			try {
				DatAccount account = this.serviceContext.accountMgrService.datAccountDao.findById(Long.parseLong(accountid));
				DatBank bank = this.serviceContext.bankMgrService.datBankDao.findById(account.getBankid());
				StringBuffer accountBuffer = new StringBuffer();
				MLType mlType = AppUtils.getUserSession().getMlType();

					accountBuffer.append("开户名:");
					accountBuffer.append(account.getAccountno() == null || account.getAccountno() == "" ? account.getAccountnoe() : account.getAccountno());
					accountBuffer.append("\n");
					accountBuffer.append("开户行:");
					accountBuffer.append(bank.getNamec() == null || bank.getNamec() == "" ? bank.getNamee() : bank.getNamec());
					accountBuffer.append("\n");
					accountBuffer.append("账号:");
					accountBuffer.append(account.getAccountcont());

				String tmp = this.selectedRowData.getAccountcont() == null ? "" : this.selectedRowData.getAccountcont() + "\n";
				this.selectedRowData.setAccountid(Long.parseLong(accountid));
				this.selectedRowData.setAccountcont(accountBuffer.toString());

				this.update.markUpdate(UpdateLevel.Data,"accountcont");
			} catch (NumberFormatException e) {
				MessageUtils.alert("格式转换错误");
				e.printStackTrace();
			} catch (NoRowException e) {
				MessageUtils.alert("所选帐号名下无帐号数据,请联系管理员查验此帐号!");
				e.printStackTrace();
			} catch (NullPointerException e) {
				MessageUtils.alert("所选帐号名下无帐号数据不全,请联系管理员查验此帐号!");
				e.printStackTrace();
			}catch (Exception e) {
				MessageUtils.showException(e);
				e.printStackTrace();
			}
		}
	}




	@Override
	public void qryRefresh() {
		super.qryRefresh();
		this.arapGrid.reload();

		Browser.execClientScript("autoCalcInit()");

		showSumDtl();
	}

	@Action
	public void refresh2(){
		this.qryRefresh();
	}

	@Action
	public void preview() {
		String reportName = this.selectedRowData.getShowwmsinfilename();
		if(StrUtils.isNull(reportName)) {
			MessageUtils.alert("请选择格式!");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/static/stateaccount/"+reportName;
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}

	private String getArgs() {
		String args = "";
		args += "&id=" + this.pkVal;
		args += "&userid=" + AppUtils.getUserSession().getUserid();
		args += "&corpidtitile=" + this.corpidtitile;
		args += "&customerid=" + this.selectedRowData.getCustomerid();
		return args;
	}

	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'StateAccount' AND isdelete = FALSE",
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


	@Action
	public void generateRP() {
//		try {
//			this.serviceContext.reqMgrService.createRP(this.pkVal , AppUtils.getUserSession().getUsercode());
//			MessageUtils.alert("OK");
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//			e.printStackTrace();
//		}

	}


	@Bind
	private UIIFrame dtlIFrame;

	@Action
	public void showSumDtl() {
		String url = "./rpreqsum.xhtml?id="+this.pkVal;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
	}

	@Bind
	private UIWindow chooseFeeWindows;

	@Bind
	private UIIFrame chooseFeeIFrame;




	@Action(id = "chooseFeeWindows", event = "onclose")
	private void dtlEditDialogClose() {
		this.qryRefresh();

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
	public UIButton chooseFee;
	@Bind
	public UIButton delBatch;


	private void disableAllButton(Boolean ischeck) {
		add.setDisabled(ischeck);
		save.setDisabled(ischeck);
		setAllDefaultAction.setDisabled(ischeck);
		setALLNullAction.setDisabled(ischeck);
		chooseFee.setDisabled(ischeck);
		delBatch.setDisabled(ischeck);
	}

	@Action
	public void openSearch() {
		this.serachWindows.show();
	}

	@Override
	public void clearQryKey() {
		this.startTime = null;
		this.endTime = null;
		super.clearQryKey();
	}


	@Action
	public void setAllDefaultAction() {
		Browser.execClientScript("changeModify('A');");
	}



	@Bind
	public UIWindow showsearchWindow;

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
				Object[] objects=serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapShip), start, limit).toArray();
				return objects;
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.unlinkfeechooserBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapShip));
				Long count = Long.valueOf(String.valueOf(list.get(0).get("counts")));
				return count.intValue();
			}
		};
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
	@Bind
	@SaveState
	private String jobnostr;
	@Bind
	@SaveState
	private String sonostr;

	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("customerid", customerid);
		map.put("reqtype", "'Q'");
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);

		String filter = "\n AND EXISTS (SELECT 1 FROM sys_user_corplink x , fina_arap y WHERE (x.corpid = y.corpid OR (y.corpid = 157970752274 AND x.corpid = (SELECT corpid FROM fina_jobs WHERE id = t.jobid AND isdelete = FALSE LIMIT 1))) AND y.jobid = t.jobid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+") " +
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
			filter+=filter+"\nAND voyage ILIKE '%"+qryvoy+"%'";
		}
		if(!StrUtils.isNull(qrysales)){//业务
			filter+=filter+"\nAND Ssales ILIKE '%"+qrysales+"%'";
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

	@Action
	public void qrygird() {
		this.grid.reload();
	}

	@Action
	public void clearQryKeyGird() {
		qryves = "";
		qryvoy = "";
		qrysales = "";
		qryNos = "";
		update.markUpdate(UpdateLevel.Data, "qryves");
		update.markUpdate(UpdateLevel.Data, "qryvoy");
		update.markUpdate(UpdateLevel.Data, "qrysales");
		update.markUpdate(UpdateLevel.Data, "qryNos");
		super.clearQryKey();
	}

	@Action
	public void createinvoicebycurrency() {
		try {
			String sql = "SELECT *  FROM f_fina_invoice_create('rpreqid="+pkVal+";userid="+userid+";flag=createinvoicebycurrency') as returntext;";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String returntext = m.get("returntext").toString();
			MessageUtils.showMsg("发票号为"+returntext);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void createinvoice() {
		try {
			String sql = "SELECT * FROM f_fina_invoice_create('rpreqid="+pkVal+";userid="+userid+";flag=createinvoice')  as returntext;";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String returntext = m.get("returntext").toString();
			MessageUtils.showMsg("发票号为"+returntext);

			returntext = returntext.replaceAll(",", "");
			String sql2 = "select * from fina_invoice where invoiceno='"+returntext+"'";
			Map m2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2);
			if (!m2.isEmpty()) {
				AppUtils.openWindow("发票编辑",  AppUtils.getContextPath()+ "/pages/module/finance/invoicedtl.aspx?&id="+m2.get("id"));
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void createfukunshenqing() {
		String[] ids = this.arapGrid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length <= 0) {
//			this.alert("Please choose one row!");
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT STRING_AGG(arapid||'',',') as arapid,STRING_AGG(id||'',',') as id FROM fina_rpreqdtl WHERE rpreqid = "+selectedRowData.getId()+" AND isdelete = FALSE;");
				id = String.valueOf(map.get("id"));
				ids = id.split(",");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			for (int i = 0; i < ids.length; i++) {
				if(StrUtils.isNull(id)){
					id = ids[i];
				}else{
					id += "," + ids[i];
				}
			}
		}

		String sql = "SELECT EXISTS (SELECT 1 FROM fina_arap a WHERE a.id = any(select arapid from fina_rpreqdtl where isdelete =FALSE and id in ("+id+")) AND a.isdelete = FALSE AND a.isconfirm = false AND EXISTS (SELECT * FROM fina_jobs WHERE id = a.jobid AND isdelete = FALSE AND isconfirm_bus = false))";
		try {
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map.containsKey("exists") == false || "true".equals(String.valueOf(map.get("exists")))){
				MessageUtils.alert("费用确认后才能生成付款申请！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String applysql = "SELECT * FROM f_fina_checkbilltorpreq('rpreqdtlid="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+"') AS rpreqid";
		try{
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(applysql);
			AppUtils.openNewPage("./payapplyedit.xhtml?type=edit&id="+m.get("rpreqid")+"&customerid=-1&araptype=AP");
//			this.arapGrid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCheckBillAction() {
		String newpage = "";
		String str = AppUtils.getReqParam("ishideallarap");
		if ("true".equals(str)) {
			newpage = "Y";
		} else {
			newpage = "N";
		}
		try {
			ConfigUtils.refreshUserCfg("checkbill_ishideallarap", newpage, AppUtils.getUserSession().getUserid());
			qryRefresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 确认费用
	 */
	@Action
	public void confirmArAp() {
		String[] ids = this.arapGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "";
		for (String id : ids) {

            String isexist = "SELECT EXISTS(SELECT 1 FROM fina_rpreqdtl WHERE id = " + id + ") AS isexist";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isexist);
			if (StrUtils.getMapVal(map, "isexist").equals("true")) {
				String arapidsql = "SELECT arapid FROM fina_rpreqdtl WHERE id = " + id + " LIMIT 1";
				Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(arapidsql);
				id = StrUtils.getMapVal(m, "arapid");
			}

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
//				String emailSql = "\nSELECT f_sys_mail_generate('type=isconfirm_bus;id=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + "');";
//				List list = this.serviceContext.arapMgrService.finaArapDao.executeQuery(emailSql);
				Browser.execClientScript("showmsg()");
				this.qryRefresh();
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
		String[] ids = this.arapGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "";
		for (String id : ids) {

            String isexist = "SELECT EXISTS(SELECT 1 FROM fina_rpreqdtl WHERE id = " + id + ") AS isexist";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isexist);
            if (StrUtils.getMapVal(map, "isexist").equals("true")) {
                String arapidsql = "SELECT arapid FROM fina_rpreqdtl WHERE id = " + id + " LIMIT 1";
                Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(arapidsql);
                id = StrUtils.getMapVal(m, "arapid");
            }

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
				this.qryRefresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Bind
	@SaveState
	private Boolean islock = false;

	/**
	 * 锁定
	 */
	@Action
	public void islockCheckAction() {
        String username = AppUtils.getUserSession().getUsername();
        //1,检查是否已经锁定
        String sql = "SELECT islock,locker FROM fina_rpreq WHERE id = " + this.pkVal + "  AND reqtype = 'S' AND isdelete = FALSE LIMIT 1";
        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
        //2,已锁定的情况，需锁定人解锁
        if (StrUtils.getMapVal(map, "islock").equals("true")) {
            if (StrUtils.getMapVal(map, "locker").equals(username) || username.equals("苏君") || username.equals("梁文辉")) {
                String lockSql = "UPDATE fina_rpreq SET islock = FALSE,locker = '" + username + "',locktime = now() WHERE id = " + this.pkVal + " AND reqtype = 'S'";
                daoIbatisTemplate.updateWithUserDefineSql(lockSql);
				save.setDisabled(false);
            } else {
				MessageUtils.showMsg("请联系锁定人 "+StrUtils.getMapVal(map, "locker")+" 或 IT管理员进行解锁");
            }
        } else {
            //3,锁定
            String lockSql = "UPDATE fina_rpreq SET islock = TRUE,locker = '" + username + "',locktime = now() WHERE id = " + this.pkVal + " AND reqtype = 'S'";
            daoIbatisTemplate.updateWithUserDefineSql(lockSql);
			save.setDisabled(true);
			MessageUtils.showMsg("OK");
        }
    }

	/**
	 * grid在前台设置每页显示的行数
	 */
	@Override
	public void doChangeGridPageSize() {
		String pageStr = (String)AppUtils.getReqParam("page");
		if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
			Integer page = Integer.parseInt(pageStr);
			this.arapGrid.setRows(page);
			this.arapGrid.rebind();
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId+".grid.pagesize";
			try {
				ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				applyGridUserDef();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}

}
