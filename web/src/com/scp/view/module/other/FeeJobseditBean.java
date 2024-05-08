package com.scp.view.module.other;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.other.feejobseditBean", scope = ManagedBeanScope.REQUEST)
public class FeeJobseditBean extends MastDtlView {
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	@Bind
	public UIIFrame attachmentIframe;
	
	@SaveState
	@Accessible
	public String processInstanceId;
	
	@SaveState
	@Accessible
	public String workItemId;

	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();
	
	@Bind
	@SaveState
	public String deptname;
	
	@Bind
	public String customerNamec;
	
	@Bind
	public UIIFrame assignIframe;

	@Override
	public void refreshForm() {

	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			
			//	disableAllButton(selectedRowData.getIscheck());
			
		}
	}
	
	/**
	 * 完成事件处理
	 */
	@Action
	public void iscompleteAjaxSubmit() {
		Boolean isComplete = selectedRowData.getIscomplete();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (!isComplete) {
				affirmLast();
				sql = "UPDATE fina_jobs SET iscomplete = TRUE,updater='"
						+ updater + "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			} else {
				sql = "UPDATE fina_jobs SET iscomplete = FALSE,updater='"
						+ updater + "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			this.commonCheck();
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIscomplete(!isComplete);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscomplete(!isComplete);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		}
	}
	
	/**
	 * CC终审流程
	 */

	public void affirmLast() {
		Map<String, Object> m = new HashMap<String, Object>();
		if (!StrUtils.isNull(processInstanceId) && !StrUtils.isNull(workItemId)) {
//			try {
//				WorkFlowUtil.passProcess(processInstanceId, workItemId, m);
//				MessageUtils.alert("Confirm OK!,流程已通过!");
//			} catch (EngineException e) {
//				e.printStackTrace();
//			} catch (KernelException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	/**
	 * 关闭,审核,完成判断处理
	 */
	@Action
	public void commonCheck() {
		Boolean isComplete = selectedRowData.getIscomplete();
		if (!isComplete) {
			this.disableAllButton(true);
		} else {
			this.disableAllButton(false);
			}
	}

	@Action
	public void showAttachmentIframe() {
		try {
			if (this.mPkVal == -1l) {
				return;
			} else {
				attachmentIframe.load(AppUtils.getContextPath()
						+ "/pages/module/common/attachment.xhtml?linkid="
						+ this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIIFrame arapIframe;

	@Action
	public void showArapEdit() {
		try {
			if (this.mPkVal == -1l) {
				
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				arapIframe.load(blankUrl);
				
			}else{
			
				arapIframe.load("../finance/arapedit.xhtml?customerid=" + this.selectedRowData.getCustomerid()
						+ "&jobid=" + this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIIFrame traceIframe;
	
	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		}else{
			traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
		}
	}


	@Override
	public void init() {
		selectedRowData = new FinaJobs();
		String id = AppUtils.getReqParam("id");
		processInstanceId =(String)AppUtils.getReqParam("processInstanceId");
		 workItemId =(String)AppUtils.getReqParam("workItemId");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();

		} else {
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
		try {
			deptname = serviceContext.userMgrService.getDepartNameByUserId(this.selectedRowData.getSaleid());
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
		if(selectedRowData!= null && selectedRowData.getCustomerid() != null && selectedRowData.getCustomerid() > 0){
			SysCorporation sysCorporation  = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			this.customerNamec = StrUtils.isNull(sysCorporation.getNamec())?sysCorporation.getNamee():sysCorporation.getNamec();
		}
		Browser.execClientScript("changeJobs('"+this.selectedRowData.getCustomerabbr()+"','"+this.selectedRowData.getSales()+"')");
		//2272 工作单编辑界面，在单号这里切换后，原业务员里面的下拉框数据，还是上一个工作单委托人的，这个会串业务员的，工作单里面刷新后，这个业务员下拉框也要对应刷一下
		Browser.execClientScript("customeridval="+selectedRowData.getCustomerid()+";saleselect();initSalesByCustomer('"+selectedRowData.getSales()+"');");
	}

	@Override
	public void doServiceSaveMaster() {

		serviceContext.jobsMgrService.saveData(selectedRowData);

		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");

	}
	
	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	

	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
	}

	
	@Override
	public void addMaster() {
		
		disableAllButton(false);
		this.selectedRowData = new FinaJobs();
		this.mPkVal = -1l;
		selectedRowData.setJobdate(new Date());
		selectedRowData.setJobtype("Z");
		showArapEdit();

	}

	@Override
	public void delMaster() {
		
			serviceContext.jobsMgrService.removeDate(this.mPkVal,AppUtils.getUserSession().getUsercode());
			this.addMaster();
			this.alert("OK");
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}
	
	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomerabbr((String) m.get("abbr"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}
	
	@Bind
	public UIIFrame receiptIframe;
	
	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			receiptIframe.load(blankUrl);
		} else {
			// if(!showReceiptEdit)
			receiptIframe.load("../ship/busbill.xhtml?jobid=" + this.mPkVal);
			// showReceiptEdit = true;
		}
	}
	
	@Bind
	public UIDataGrid setnosGrid;
	
	@Bind(id = "setnosGrid", attribute = "dataProvider")
	protected GridDataProvider getDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(gridLazyLoad){
					return new Object[]{};
				}else{
					starts = start;
					limits = limit;
					String sqlId = "pages.module.other.feejobseditBean.grid.page";
					return daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId, getQryClauseWhere2(qryMap)).toArray();
				}
			}

			@Override
			public int getTotalCount() {
				return 100000;
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qry += "\nAND r.corpid =" + this.selectedRowData.getCorpid();
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public String codes;
	
	@Bind
	public UIWindow setnosWindow;
	
	@Action
	public void choosenos(){
		String[] ids = this.setnosGrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String querySql  = "SELECT code  FROM sys_busnodesc WHERE id = "+ids[0];
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			codes = StrUtils.getMapVal(m, "code");
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		getJobNos();
		setnosWindow.close();
	}
	
	@Action
	public void setnosGrid_ondblclick() {
		choosenos();
	}
	
	@Action
	public void getJobNos() {
		if(this.selectedRowData!=null&&this.selectedRowData.getNos()!=null&&!this.selectedRowData.getNos().isEmpty()){
			MessageUtils.alert("工作号已生成!");
			return;
		}
		////System.out.println(codes);
		String querySql;
		if(this.mPkVal > 0){
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+"') AS nos;";
		}else{
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+";corpid="+selectedRowData.getCorpid()+";jobdate="+selectedRowData.getJobdate()+"') AS nos;";
		}
		
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String nos = StrUtils.getMapVal(m, "nos");
			this.selectedRowData.setNos(nos);
			update.markUpdate(true, UpdateLevel.Data, "noss");
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void setJobNos(){
		this.setnosGrid.reload();
		setnosWindow.show();
	}
	
	@Bind
	@SaveState
	@Accessible
	public Long salescorpid = null;
	
	/**
	 * 选委托人提取出相应业务员
	 */
	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		Long salesid=null;
		Long deptid;
		String salesname;
		String tradeway;
		StringBuilder querySql = new StringBuilder();
		querySql.append("\n SELECT a.id AS salesid,a.namec AS salesname,deptid,corpid FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE ");
		querySql.append("\n  AND CASE WHEN EXISTS(SELECT a.id AS salesid,a.namec AS salesname FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE AND EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C' ");
		querySql.append("\n  AND EXISTS(SELECT * FROM ");
		querySql.append("\n  (SELECT xx.userid ");
		querySql.append("\n  FROM sys_custlib xx , sys_custlib_user y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND y.userid ="+AppUtils.getUserSession().getUserid()+" AND xx.libtype = 'S' AND xx.userid IS NOT NULL");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z where z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  UNION ");
		querySql.append("\n  SELECT xx.userid");
		querySql.append("\n  FROM sys_custlib xx, sys_custlib_role y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) AND xx.libtype = 'S'");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z WHERE z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  ) AS T");
		querySql.append("\n  WHERE T.userid = x.userid ");
		querySql.append("\n  ))) THEN EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C' ");
		querySql.append("\n  AND EXISTS(");
		querySql.append("\n  SELECT * FROM ");
		querySql.append("\n  (SELECT xx.userid ");
		querySql.append("\n  FROM sys_custlib xx , sys_custlib_user y ");
		querySql.append("\n  WHERE y.custlibid = xx.id AND y.userid ="+AppUtils.getUserSession().getUserid()+" AND xx.libtype = 'S' AND xx.userid IS NOT NULL");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z where z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n UNION ");
		querySql.append("\n  SELECT xx.userid");
		querySql.append("\n  FROM sys_custlib xx, sys_custlib_role y ");
		querySql.append("\n  WHERE y.custlibid = xx.id");
		querySql.append("\n  AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) AND xx.libtype = 'S'");
		querySql.append("\n  AND EXISTS(SELECT 1 FROM sys_user z WHERE z.code = xx.code  AND z.isdelete = false)");
		querySql.append("\n  ) AS T");
		querySql.append("\n  WHERE T.userid = x.userid ");
		querySql.append("\n  ))");
		querySql.append("\n ELSE (EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+customerid+" AND x.userid = a.id  AND x.roletype = 'S' AND x.linktype = 'C')");
		querySql.append("\n OR EXISTS(SELECT x.salesid FROM sys_corporation x WHERE x.id = "+customerid+" AND x.salesid = a.id))   END");
		querySql.append("\n LIMIT 1");
		
		String tradewaysql  = "SELECT  c.tradeway  FROM sys_corporation c WHERE c.id = "+customerid+" AND c.isdelete = false";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
			Map m_tradeway = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(tradewaysql);
			String salesidStr = StrUtils.getMapVal(m, "salesid");
			salesid = StrUtils.isNull(salesidStr)?0l:Long.valueOf(salesidStr);
			//2025 委托人选择后，提取业务员后，按业务员对应的分公司，修改上面的接单公司
			try {
				if(!StrUtils.isNull(salesidStr)){
					SysUser sysuer = serviceContext.userMgrService.sysUserDao.findById(salesid);
					if(sysuer!=null&&sysuer.getSysCorporation()!=null){
						this.selectedRowData.setCorpid(sysuer.getSysCorporation().getId());
						//this.selectedRowData.setCorpidop(sysuer.getSysCorporation().getId());
						update.markUpdate(true, UpdateLevel.Data, "corpid");
						//update.markUpdate(true, UpdateLevel.Data, "corpidop");
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
			deptid = StrUtils.isNull(StrUtils.getMapVal(m, "deptid"))?0l:Long.valueOf(StrUtils.getMapVal(m, "deptid"));
			salesname = StrUtils.getMapVal(m, "salesname");
			this.selectedRowData.setSaleid(salesid);
			this.selectedRowData.setSales(salesname);
			this.selectedRowData.setDeptid(deptid);
			tradeway = StrUtils.getMapVal(m_tradeway, "tradeway");
			this.selectedRowData.setTradeway(tradeway);
			String corpid = StrUtils.getMapVal(m, "corpid");
			this.salescorpid =  StrUtils.isNull(corpid)?0l:Long.valueOf(corpid);
//			this.deptids = getqueryDepartid();
			deptname = serviceContext.userMgrService.getDepartNameByUserId(salesid);
			update.markUpdate(true, UpdateLevel.Data, "deptname");
			update.markUpdate(true, UpdateLevel.Data, "tradeway");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("initSalesByCustomer('"+salesname+"');");
			
		} catch (NoRowException e) {
			this.selectedRowData.setSaleid(0l);
			this.selectedRowData.setSales("");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("initSalesByCustomer('');");
		} catch (Exception e) {
			e.printStackTrace();
		}
		update.markUpdate(true, UpdateLevel.Data, "cusales");
		this.selectedRowData.setCustomerid(Long.decode(customerid));
		arrears(customerid);
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(salesid);
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
	}
	
	/*
	 * 如果该客户欠款在欠账额度之内，不提示，返回空,提示：客户信息(简称，中英文名，欠账额度，当前欠款)
	 * 超过时是否允许接单：不允许：增加提示不允许，并将当前单的界面上id和名称清空，如果允许就不处理只提示
	 * */
	public void arrears(String customerid){
		SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(Long.valueOf(customerid));
		if(sysCorporation.getIsalloworder() == null){
			return;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("customerid",customerid);
		String urlArgs2 = AppUtils.map2Url(argsMap, ";");
		try {
			Long loguserid = AppUtils.getUserSession().getUserid();
			//前海国际事业部
			Map deptmap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select deptid FROM SYS_USER WHERE isdelete = FALSE AND id = "+loguserid);
			if(AppUtils.getUserSession().getCorpidCurrent() != 13632274 && !"1464156732274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565620302274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565643442274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565621682274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565614052274".equals(String.valueOf(deptmap.get("deptid")))
					&& !"1565616572274".equals(String.valueOf(deptmap.get("deptid")))){
				return;
			}
			String sqlQry = "SELECT * FROM f_sys_corporation_checkamtowe('"+urlArgs2+"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			String str = m.get("f_sys_corporation_checkamtowe").toString();
			
			if(str.equals("1")){
			}else {
				
				if(sysCorporation.getIsalloworder()){
					MessageUtils.alert("提示(Tips):"+str);
				}
				else{
					MessageUtils.alert(str);
					Browser.execClientScript("customeridJsVar.setValue('')");
					Browser.execClientScript("customernameJsVar.setValue('')");
					Browser.execClientScript("$('#customer_input').val('')");
				}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showAssign() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			assignIframe.load(blankUrl);
		} else {
			assignIframe.load("../customer/assigneduser.xhtml?id="
					+ this.mPkVal + "&linktype=J");
		}
	}
	
	@Bind
	public UIIFrame msgIframe;
	
	@Action
	public void showMsg() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			msgIframe.load(blankUrl);
		} else {
			//if(!showMsg)
				msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
			//showMsg = true;
		}
	}
	
	@Bind
	public UIIFrame knowledgeBaseIframe;
	
	@Action
	public void showKnowledgeBase() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			knowledgeBaseIframe.load(blankUrl);
		} else {
			knowledgeBaseIframe.load("../../sysmgr/knowledge/knowledgeBase.jsp?id="
					+ this.mPkVal+"&language="+AppUtils.getUserSession().getMlType());
		}
	}
	
	@Action
	public void salesChangeAjaxSubmit() {
		String salesid = AppUtils.getReqParam("salesid");
		String sql  = "SELECT deptid,corpid FROM sys_user WHERE id = " + salesid;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String deptid = StrUtils.getMapVal(m, "deptid");
			String cid = StrUtils.getMapVal(m, "corpid");
			if(!StrUtils.isNull(deptid)){
				this.selectedRowData.setDeptid(StrUtils.isNull(deptid)?0l:Long.valueOf(deptid));
			}
			if(!StrUtils.isNull(cid)){
				this.salescorpid = Long.valueOf(cid);
				this.selectedRowData.setCorpid(StrUtils.isNull(cid)?0l:Long.valueOf(cid));
			}
			//update.markUpdate(true, UpdateLevel.Data, "deptcomb");
			update.markUpdate(true, UpdateLevel.Data, "corpid");
//			this.deptids = getqueryDepartid();
		} catch (NoRowException e) {
			this.selectedRowData.setDeptid(0l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(Long.parseLong(salesid));
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
	}
}
