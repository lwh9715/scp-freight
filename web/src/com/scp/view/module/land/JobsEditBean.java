package com.scp.view.module.land;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.ship.JobsnosChooseService;

@ManagedBean(name = "pages.module.land.jobseditBean", scope = ManagedBeanScope.REQUEST)
public class JobsEditBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	@SaveState
	@Accessible
	public Long shipid;

	@Bind
	public UIIFrame shippingIframe;

	@Bind
	public UIIFrame truckIframe;

	@Bind
	public UIIFrame customerIframe;
	
	@Bind
	public UIIFrame invoIframe;

	@Bind
	public UIIFrame billIframe;

	@Bind
	public UIIFrame arapIframe;

	@Bind
	public UIIFrame receiptIframe;

	@Bind
	public UIIFrame attachmentIframe;

	@Bind
	public UIIFrame docdefIframe;

	@Bind
	public UIIFrame assignIframe;

	@Bind
	public UIIFrame constructIframe;

	@Bind
	public UIIFrame msgIframe;

	@Bind
	public UIIFrame traceIframe;
	
	@Bind
	public UIIFrame statusIframe;
	
	@Bind
	public UITabLayout tabLayout;
	
	@Bind
	public Long department;
	
	@Bind
	@Accessible
	public String fnos;
	
	@Bind
	public Long sale;
	
	@SaveState
	public Boolean isPostBack = false;

	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();

	@SaveState
	@Accessible
	public BusTruck truckInit = new BusTruck();
	
	@SaveState
	@Accessible
	public SysCorpcontacts corpc = new SysCorpcontacts();
	
	@Bind
	public UIIFrame foreigncustomerIframe;
	
	@Bind
	public String customerNamec;
	
	@Bind
	public UIButton addJobsLeaf;

	@Override
	public void refreshForm() {

	}
	
	public Long userid;
	
	@Bind
	@SaveState
	private String impexp = "E";

	@Bind
	@SaveState
	public String deptname;
	
	@Bind
	public long deptop = 0L;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			if(!StrUtils.isNull(AppUtils.getReqParam("impexp"))){
				impexp = AppUtils.getReqParam("impexp");
			}
			init();
			if("E".equals(impexp)){
				initCtrlLand();
			}else if("D".equals(impexp)){
				initCtrlLand_D();
			}
			initCtrl();
			this.commonCheck();
//			this.deptids = getqueryDepartid();
		} else {
			this.isPostBack = true;
		}
	}
	
	@Bind
	public String ismodifynos;
	
	private void initCtrl() {
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs_land.getValue())) {
			if (s.endsWith("_modify")) {
				ismodifynos = "true";
			}
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
			if (isComplete) {
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
	
	
	@Bind
	public UICheckBox isClose;
	@Bind
	public UICheckBox isCheck;
	@Bind
	public UICheckBox isComplete;
	
	/**
	 * 关闭,审核,完成判断处理
	 */
	@Action
	public void commonCheck() {
		Boolean isClose = selectedRowData.getIsclose();
		Boolean isCheck = selectedRowData.getIscheck();
		Boolean isComplete = selectedRowData.getIscomplete();
		Boolean isLock = selectedRowData.getIslock();
		
		if(isClose==null)isClose=false;
		if(isCheck==null)isCheck=false;
		if(isComplete==null)isComplete=false;
		if(isLock==null)isLock=false;
		
		if (isLock) {
			this.disableAllButton(true);
			this.isClose.setDisabled(true);
			this.isCheck.setDisabled(true);
			this.isComplete.setDisabled(true);
		} else {
			if (isClose || isCheck || isComplete) {
				this.disableAllButton(true);
			} else {
				this.disableAllButton(false);
				this.isClose.setDisabled(false);
				this.isCheck.setDisabled(false);
				this.isComplete.setDisabled(false);
				initCtrl();
			}
			if (isCheck || isComplete) {
				this.isClose.setDisabled(true);
			} else {
				if (isClose) {
					this.isClose.setDisabled(true);
					this.isCheck.setDisabled(true);
					this.isComplete.setDisabled(true);
				}
			}
		}
		//initCtrl();
		this.repaintCheckbox();
	}
	
	private void repaintCheckbox() {
		this.isClose.repaint();
		this.isCheck.repaint();
		this.isComplete.repaint();
	}
	
	@Action
    private void changeDept() {
		String sale = AppUtils.getReqParam("sale");
        if(!StrUtils.isNull(sale)) {
        	this.sale = Long.parseLong(sale);
        	
        	SysDepartment sd = this.serviceContext.userMgrService.sysUserDao.findById(this.sale).getSysDepartment();
        	if(sd != null) {
        		this.department = sd.getId();
        	}
          this.update.markUpdate(UpdateLevel.Data, "department");
        }
    }
	
	@Bind
	@SaveState
	@Accessible
	public Long salescorpid = null;
	
	private List<SelectItem> getqueryDepartid(){
		try {
			List<SelectItem> list = null;
			if(salescorpid == null){
				Long id = this.selectedRowData != null && this.selectedRowData.getCorpid() != null ? this.selectedRowData.getCorpid() : -1L;
				 list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid ="+id, "ORDER BY d.name");
			}else{
				 list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid ="+salescorpid, "ORDER BY d.name");
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	@Bind(id="deptids")
//	@SelectItems
//	@SaveState
//	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
	@Action
	private void changeCorpid(){
		String corptmp = AppUtils.getReqParam("corpid");
		if(corptmp != null && !corptmp.isEmpty()){
			if(!this.selectedRowData.getCorpid().equals(corptmp)){
				this.selectedRowData.setDeptid(null);
				this.selectedRowData.setCorpid(Long.parseLong(corptmp));
//				this.deptids = getqueryDepartid();
				//update.markUpdate(true,UpdateLevel.Data, "deptcomb");
				//Browser.execClientScript("clearDept();");
			}
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
			String isDeptNotChangebySales = ConfigUtils.findSysCfgVal("jobs_dept_notchangeby_sales");//系统设置，接单公司不按业务员自动联动
			if("Y".equals(isDeptNotChangebySales)){
			}else{
				if(!StrUtils.isNull(cid)){
					this.salescorpid = Long.valueOf(cid);
					this.selectedRowData.setCorpid(StrUtils.isNull(cid)?0l:Long.valueOf(cid));
				}
				//update.markUpdate(true, UpdateLevel.Data, "deptcomb");
				update.markUpdate(true, UpdateLevel.Data, "corpid");
			}
			//this.deptids = getqueryDepartid();
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
	
	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		String corpidv = AppUtils.getReqParam("corpidv");
		Long salesid = null;
		Long deptid;
		String salesname;
		Long usercorpid;
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
		querySql.append("\n AND a.corpid = "+corpidv);
		querySql.append("\n LIMIT 1");
		
		String tradewaysql  = "SELECT  c.tradeway ,c.impexp FROM sys_corporation c WHERE c.id = "+customerid+" AND c.isdelete = false";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
			Map m_tradeway = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(tradewaysql);
			String salesidStr = StrUtils.getMapVal(m, "salesid");
			salesid = StrUtils.isNull(salesidStr)?0l:Long.valueOf(salesidStr);
			deptid = StrUtils.isNull(StrUtils.getMapVal(m, "deptid"))?0l:Long.valueOf(StrUtils.getMapVal(m, "deptid"));
			salesname = StrUtils.getMapVal(m, "salesname");
			usercorpid = StrUtils.isNull(StrUtils.getMapVal(m, "corpid"))?0l:Long.valueOf(StrUtils.getMapVal(m, "corpid"));
			this.selectedRowData.setSaleid(salesid);
			this.selectedRowData.setSales(salesname);
			this.selectedRowData.setDeptid(deptid);
			
			String isDeptNotChangebySales = ConfigUtils.findSysCfgVal("jobs_dept_notchangeby_sales");//系统设置，接单公司不按业务员自动联动
			if("Y".equals(isDeptNotChangebySales)){
			}else{
				this.selectedRowData.setCorpid(usercorpid);
				this.selectedRowData.setCorpidop(usercorpid);
			}
			tradeway = StrUtils.getMapVal(m_tradeway, "tradeway");
			this.selectedRowData.setTradeway(tradeway);
			this.selectedRowData.setImpexp(StrUtils.getMapVal(m_tradeway, "impexp"));
			String corpid = StrUtils.getMapVal(m, "corpid");
			this.salescorpid =  StrUtils.isNull(corpid)?0l:Long.valueOf(corpid);
//			this.deptids = getqueryDepartid();
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "saleid");
//			update.markUpdate(true, UpdateLevel.Data, "deptcomb");
			update.markUpdate(true, UpdateLevel.Data, "corpid");
			update.markUpdate(true, UpdateLevel.Data, "corpidop");
			update.markUpdate(true, UpdateLevel.Data, "tradeway");
			update.markUpdate(true, UpdateLevel.Data, "impexp");
			Browser.execClientScript("initSalesByCustomer('"+salesname+"');");
		} catch (NoRowException e) {
			this.selectedRowData.setSaleid(0l);
			this.selectedRowData.setSales("");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("saleidJsVar.setValue()");
			Browser.execClientScript("initSalesByCustomer('');");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	/**
	 * 状态
	 */
	@Action
	public void showStatus() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			statusIframe.load(blankUrl);
		} else {
			String blno=this.selectedRowData.getNos();
			statusIframe.load("../common/goodstrack.xhtml?fkid="+ this.mPkVal+"&blno="+ blno + "&type=S");
		}
	}

	@Action
	public void showAttachmentIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			attachmentIframe.load(blankUrl);
		} else {
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid="
					+ this.mPkVal);
		}
	}

	@Action
	public void showArapEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			arapIframe.load(blankUrl);
		} else {
			arapIframe.load("../finance/arapedit.xhtml?customerid="
					+ this.selectedRowData.getCustomerid() + "&jobid="
					+ this.mPkVal);
		}
	}

	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			receiptIframe.load(blankUrl);
		} else {
			String str = "SELECT id FROM fina_bill WHERE isdelete = FALSE AND jobid = "+this.mPkVal+" ORDER BY CASE WHEN inputtime IS NOT NULL > updatetime  IS NOT NULL THEN inputtime ELSE updatetime END DESC LIMIT 1";
			Map map = null;
			try {
				map = this.serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(str);
			} catch (NoRowException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
			String url = "";
			if(map!=null){
				url = "../ship/busbill.xhtml?jobid=" + this.mPkVal +"&id=" + map.get("id");
			}else{
				url = "../ship/busbill.xhtml?jobid=" + this.mPkVal;
			}
			receiptIframe.load(url);
		}
	}
	
	@Action
	public void showInvoEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			invoIframe.load(blankUrl);
		} else {
			// if(!showReceiptEdit)
			invoIframe.load("../ship/businvoice.xhtml?jobid=" + this.mPkVal);
			// showReceiptEdit = true;
		}
	}

	@Action
	public void showDocDef() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			docdefIframe.load(blankUrl);
		} else {
			docdefIframe.load("../bus/busdocdef.xhtml?linkid=" + this.shipid);
		}
	}

	@SaveState
	public String openIformType;//记录当前打开的iframe

	@Action
	public void showShipping() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			shippingIframe.load(blankUrl);
		} else {
			shippingIframe.load("../land/busland.xhtml?id="+this.mPkVal+"&impexp="+impexp);
		}
		openIformType = "shippingIframe";
	}

	/**
	 * 拖车
	 * Neo 2014 4/30
	 */
	@Action
	public void showTruck() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			truckIframe.load(blankUrl);
		} else {
			 truckIframe.load("../bus/bustruck.xhtml?id=" + this.mPkVal);
		}
	}

	/**
	 * 报关
	 * Neo 2014 4/29
	 */
	@Action
	public void showCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			customerIframe.load(blankUrl);
		} else {
			 customerIframe.load("../bus/buscustoms.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showBill() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			billIframe.load(blankUrl);
		} else {
			billIframe.load("../ship/busshipbill.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showMsg() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			msgIframe.load(blankUrl);
		} else {
			msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
		}
	}

	@Action
	public void showAssign() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			assignIframe.load(blankUrl);
		} else {
			assignIframe.load("../customer/assigneduser.xhtml?type=jobs&id="
					+ this.shipid + "&linktype=J");
		}
	}

	@Action
	public void showConstruct() {
//		if (this.mPkVal == -1l) {
//			String blankUrl = AppUtils.getContextPath()
//					+ "/pages/module/common/blank.xhtml";
//			constructIframe.load(blankUrl);
//		} else {
//			constructIframe.load("../land/jobschildpod.xhtml?id=" + this.mPkVal);
//		}
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			constructIframe.load(blankUrl);
		} else {
			constructIframe.load("../ship/jobschild.xhtml?id=" + this.mPkVal);
		}
	}

	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			traceIframe.load(blankUrl);
		} else {
			traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
		}
	}
	
	/**
	 * 清关
	 */
	@Action
	public void showForeignCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			foreigncustomerIframe.load(blankUrl);
		} else {
			foreigncustomerIframe.load("../bus/foreignbuscustoms.xhtml?id=" + this.mPkVal);
		}
	}

	@Override
	public void init() {
		selectedRowData = new FinaJobs();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		}else{
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {

		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao
				.findById(this.mPkVal);
		if(selectedRowData.getCustomerid() != null && selectedRowData.getCustomerid() > 0){
			SysCorporation sysCorporation  = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			this.customerNamec = StrUtils.isNull(sysCorporation.getNamec())?sysCorporation.getNamee():sysCorporation.getNamec();
		}
		
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		getshipid(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "shipid");
		setFnos();
		showArapEdit();
		showTrace();
		showAttachmentIframe();
		showReceiptEdit();
		showShipping();
		showInvoEdit();
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(this.selectedRowData.getSaleid());
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
		//2272 工作单编辑界面，在单号这里切换后，原业务员里面的下拉框数据，还是上一个工作单委托人的，这个会串业务员的，工作单里面刷新后，这个业务员下拉框也要对应刷一下
		Browser.execClientScript("customeridval="+selectedRowData.getCustomerid()+";saleselect();initSalesByCustomer('"+selectedRowData.getSales()+"');");
		showTipsCount();
	}
	
	@Override
	public void saveMaster(){
		try {
			
			doServiceSaveMaster(); //Master
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	public void doServiceSaveMaster() {
		if(this.mPkVal > 0){
			if(openIformType!=null&&openIformType.equals("shippingIframe")){//当当前打开的iframe是委托时候，调用委托页面里面的保存
				//Browser.execClientScript("this.frames['shippingIframe']","refreshMaster.fireEvent('click');saveMasterJsvar.fireEvent('click');");
				Browser.execClientScript("this.frames['shippingIframe']","saveMasterJsvar.fireEvent('click');");
			}
		}
		
		if(selectedRowData.getSaleid()!=null&&selectedRowData.getSaleid()>0){
			try {
				SysUser sysuer = serviceContext.userMgrService.sysUserDao.findById(selectedRowData.getSaleid());
				selectedRowData.setDeptid(sysuer.getDeptid());//设置部门id为业务员的部门id
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			selectedRowData.setDeptid(null);
		}
		
//		if((100 == selectedRowData.getCorpidop() || (null != selectedRowData.getCorpidop2() && 100 == selectedRowData.getCorpidop2())) && deptop == 0L){
//			if(0L == selectedRowData.getDeptop()){
//				selectedRowData.setDeptop(Long.valueOf("43009103888"));
//			}
//		}else if(100 != selectedRowData.getCorpidop() && (null == selectedRowData.getCorpidop2() || 100 != selectedRowData.getCorpidop2())){
//			selectedRowData.setDeptop(0L);
//		}
		
		serviceContext.jobsMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		String sql = " isdelete = false AND jobid =" + this.mPkVal;
		try {
			this.truckInit = serviceContext.busTruckMgrService.busTruckDao.findOneRowByClauseWhere(sql);
			this.refreshMasterForm();
			this.alert("ok");
		} catch (NoRowException e) {
			truckInit = new BusTruck();
			setCorpc(this.selectedRowData.getCustomerid());
			truckInit.setJobid(this.mPkVal);
			truckInit.setNos(this.selectedRowData.getNos());
			truckInit.setAreatype("");
			truckInit.setTruckstate("I");
			truckInit.setSingtime(this.selectedRowData.getJobdate());
			String issetloadtime = ConfigUtils.findSysCfgVal("sys_cfg_etd_update_jobdate");
			if("Y".equals(issetloadtime)){
				truckInit.setLoadtime(this.selectedRowData.getJobdate());
			}
			SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getCustomerid());
			truckInit.setLoadcontact(sysCorporation.getContact());
			truckInit.setContacttel(sysCorporation.getTel1());
			truckInit.setLoadaddress(sysCorporation.getAddressc());
			truckInit.setRemarks(sysCorporation.getRemarks());
			truckInit.setFactory(sysCorporation.getNamec());
			
			
			serviceContext.busTruckMgrService.saveData(truckInit);
			this.refreshMasterForm();
			this.alert("ok");
		}catch(MoreThanOneRowException e){
			MessageUtils.showException(e);
		}
		catch (Exception e) {
			MessageUtils.showException(e);
		} 
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
		addJobsLeaf.setDisabled(isCheck);
	}

	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new FinaJobs();
		
		truckInit = new BusTruck();
		this.mPkVal = -1l;
		selectedRowData.setJobdate(new Date());
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setIsland(true);
		selectedRowData.setJobtype("L");
		selectedRowData.setIsair(false);
		selectedRowData.setIsshipping(false);
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCustomerabbr("");
		selectedRowData.setSaleid(null);
		selectedRowData.setDeptid(null);
		selectedRowData.setDeptop(0L);
		selectedRowData.setIslock(false);
		selectedRowData.setSales("");
		selectedRowData.setTradeway("F");
		if(StrUtils.isNull(impexp))impexp="E";
		selectedRowData.setImpexp(impexp);
		this.department = null;
//		this.deptids = getqueryDepartid();
		this.update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		Browser.execClientScript("deptnameJsVar.setValue();");
		showShipping();
		showArapEdit();
		showReceiptEdit();
		showInvoEdit();
		showAttachmentIframe();
	}

	@Override
	public void delMaster() {
		try {
			serviceContext.jobsMgrService.removeDate(this.mPkVal, AppUtils
					.getUserSession().getUsercode());
			this.addMaster();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {

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

	public void getshipid(Long id) {
		String sql = "SELECT id FROM bus_truck where isdelete = false AND jobid= " + this.mPkVal;
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.shipid = (Long) m.get("id");
		}catch(NoRowException e){
		} catch (Exception e) {
			//MessageUtils.alert("数据异常,委托单被非法删除,请检查!");
			e.printStackTrace();
			return;
		}
	}

	// 弹窗选择工作单
	@Bind
	@SaveState
	@Accessible
	public String jobnos;

	@ManagedProperty("#{jobsnoschooseserviceBean}")
	private JobsnosChooseService jobsnosChooseService;

	@Bind
	public UIWindow showJobsnosWindow;

	@Bind
	public UIDataGrid jobnosGrid;

	@Bind(id = "jobnosGrid", attribute = "dataProvider")
	public GridDataProvider getJobnosGridDataProvider() {
		return this.jobsnosChooseService.getJobsnosDataProvider("AND jobtype = 'L'");
	}

	@Bind
	private String qryJobsnosKey;

	@Action
	public void jobsnosQry() {
		this.jobsnosChooseService.qryNos(qryJobsnosKey);
		this.jobnosGrid.reload();
	}

	@Action
	public void showJobnosAction() {
		String jobnos = (String) AppUtils.getReqParam("jobnos");
		qryJobsnosKey = jobnos;
		int index = qryJobsnosKey.indexOf("/");
		if (index > 1)
			qryJobsnosKey = qryJobsnosKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryJobsnosKey");
			showJobsnosWindow.show();
			jobsnosQry();
			Browser.execClientScript("qryButton.focus");
			return;
			// type = '0',只返回一个直接回填,多个显示弹窗
		} else {
			try {
				List<Map> cs = jobsnosChooseService.findJobsnos(qryJobsnosKey,
						"AND jobtype = 'L'");
				if (cs.size() == 1) {
					this.jobnos = ((String) cs.get(0).get("nos"));
					this.mPkVal = ((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "jobnos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					
					if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_jobedit_redirect_url"))){
						String redirectUrl = "'./jobsedit.xhtml?id="+this.mPkVal+"'";
						Browser.execClientScript("window.location.href=" + redirectUrl);
					}else{
						refreshMasterForm();
						showJobsnosWindow.close();
					}
				} else {
					this.update.markUpdate(UpdateLevel.Data, "qryJobsnosKey");
					showJobsnosWindow.show();
					jobsnosQry();
					Browser.execClientScript("qryButton.focus");
				}

			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	}

	@Action
	public void jobnosGrid_ondblclick() {
		Object[] objs = jobnosGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.mPkVal = ((Long) m.get("id"));
		
		if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_jobedit_redirect_url"))){
			String redirectUrl = "'./jobsedit.xhtml?id="+this.mPkVal+"'";
			Browser.execClientScript("window.location.href=" + redirectUrl);
		}else{
			this.jobnos = ((String) m.get("nos"));
			this.update.markUpdate(UpdateLevel.Data, "jobnos");
			this.update.markUpdate(UpdateLevel.Data, "mPkVal");
			showJobsnosWindow.close();
			refreshMasterForm();
		}
	}
//	/**
//	 * NEO
//	 * 2014/4/14 11:48
//	 * 搜索工作单完成的时候 下方页面跳转到委托单
//	 */
//	public void refreshMasterForm2() {
//
//		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao
//				.findById(this.mPkVal);
//		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
//		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
//		getshipid(this.mPkVal);
//		update.markUpdate(true, UpdateLevel.Data, "shipid");
//		showShipping();
//	}
	
	public void setCorpc(Long customerid){
		String sql = "isdelete = FALSE AND isdefault = TRUE AND customerid = "+customerid;
		
		try {
			this.corpc = serviceContext.customerContactsMgrService.sysCorpcontactsDao
					.findOneRowByClauseWhere(sql);
		} catch (NoRowException e) {
			this.corpc =  new SysCorpcontacts();
		}catch(Exception e){
			
		}
		
	}

	
	@Action
	public void createPod(){
		if(this.mPkVal ==-1l){
			MessageUtils.alert("工作单未保存");
			return;
		}
		try {
			serviceContext.jobsMgrService.createPod(this.mPkVal,AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK");
			setFnos();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 加载海外单号
	 */
	public void setFnos() {
		String sql = "SELECT string_agg((SELECT nos FROM fina_jobs WHERE id = k.jobidto AND isdelete = FALSE),',') AS fnos " 
				+ "FROM fina_jobs_link k WHERE k.jobidfm = " + this.mPkVal 
				+ " OR k.jobidfm = (SELECT jobidto FROM fina_jobs_link WHERE jobidfm = " + this.mPkVal + ")";

		try {
			Map m = serviceContext.jobsMgrService.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.fnos = (String) m.get("fnos");
		} catch (NoRowException e) {
			this.fnos = "";
		}

		this.update.markUpdate(UpdateLevel.Data, "fnos");
	}
	

	@SuppressWarnings("deprecation")
	@Action
	public void clearnos(){
		String sql = "UPDATE fina_jobs set nos = '', updater ='"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE id ="+selectedRowData.getId() + ";";
		try {
			this.serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
		this.refreshMasterForm();
	}
	
	@Bind
	public UIWindow setnosWindow;
	
	@Action
	public void setJobNos(){
		this.grid.reload();
		setnosWindow.show();
	}
	
	@SaveState
	public String codes;
	
	@Action
	public void choosenos(){
		String[] ids = this.grid.getSelectedIds();
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
	public void getJobNos() {
		if(this.selectedRowData!=null&&this.selectedRowData.getNos()!=null&&!this.selectedRowData.getNos().isEmpty()){
			MessageUtils.alert("工作号已生成!");
			return;
		}
		String querySql ;
		if(this.mPkVal > 0){
			//querySql = "SELECT f_find_jobs_getnos('id="+this.mPkVal+"') AS nos;";
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+"') AS nos;";
		}else{
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+";corpid="+selectedRowData.getCorpid()+";jobdate="+selectedRowData.getJobdate()+"') AS nos;";
		}
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String nos = StrUtils.getMapVal(m, "nos");
			this.selectedRowData.setNos(nos);
			MessageUtils.alert(nos);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void grid_ondblclick() {
		choosenos();
	}
	
	@Bind
	public UIWindow showAssignWindow;
	
	@Bind
	public UIWindow showMessageAllWindow;
	
	@Bind
	public UIWindow showLogsWindow;
	
	
	@Action
	public void showAssignWin() {
		showAssignWindow.show();
	}
	
	@Action
	public void showMsgWin() {
		showMessageAllWindow.show();
	}
	
	@Action
	public void showLogs() {
		showLogsWindow.show();
	}
	
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
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-DE380A4B";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
				lists.add(selectItem);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
	@Action
	public void applyBPMform() {
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				String processCode = "BPM-DE380A4B";
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
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND r.corpid =" + this.selectedRowData.getCorpid();
		m.put("qry", qry);
		return m;
	}

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
	public void applyBPM() {
		if(this.mPkVal==null||!(mPkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-DE380A4B";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
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
	}
	
	@Bind
	public UIButton addMaster;
	
	//陆运工作单按钮权限
	private void initCtrlLand() {
		addMaster.setDisabled(true);
		saveMaster.setDisabled(true);
		delMaster.setDisabled(true);
		addJobsLeaf.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(250000300100L)) {
			if (s.endsWith("_add")) {
				addMaster.setDisabled(false);
				saveMaster.setDisabled(false);
				addJobsLeaf.setDisabled(false);
			} else if (s.endsWith("_update")) {
				saveMaster.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}
		}
		if(this.selectedRowData != null && selectedRowData.getIslock()){
			saveMaster.setDisabled(true);
			delMaster.setDisabled(true);
		}
	}
	
	//内贸拖车单按钮权限
	private void initCtrlLand_D() {
		addMaster.setDisabled(true);
		saveMaster.setDisabled(true);
		delMaster.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(250000300150L)) {
			if (s.endsWith("_add")) {
				addMaster.setDisabled(false);
				saveMaster.setDisabled(false);
			} else if (s.endsWith("_update")) {
				saveMaster.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}
		}
		if(this.selectedRowData != null && selectedRowData.getIslock()){
			saveMaster.setDisabled(true);
			delMaster.setDisabled(true);
		}
	}
	
	
	/**
	 * 审核事件处理
	 */
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (isCheck) {
				sql = "UPDATE fina_jobs SET ischeck = TRUE,checkter = '"
						+ updater + "',checktime = NOW(),updater = '" + updater
						+ "',updatetime=NOW() WHERE id =" + this.mPkVal + ";";
			} else {
				sql = "UPDATE fina_jobs SET ischeck = FALSE,checkter = NULL,checktime = NULL,updater = '"
						+ updater
						+ "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			this.commonCheck();
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		}
	}
	
	
	/**
	 * 关闭事件处理
	 */
	@Action
	public void iscloseAjaxSubmit() {
		Boolean isClose = selectedRowData.getIsclose();
		Boolean isCheck = selectedRowData.getIscheck();
		Boolean isComplete = selectedRowData.getIscomplete();

		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (isCheck || isComplete) {
				MessageUtils.alert("暂时无法关闭!");
				return;
			}
			if (!isClose) {
				MessageUtils.alert("工作单已关闭，不可恢复！");
				return;
			}
			if (isClose) {
				sql = "UPDATE fina_jobs SET isclose = TRUE,updater='" + updater
						+ "',updatetime=NOW() WHERE id =" + this.mPkVal + ";";
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(sql);
			
			this.commonCheck();
//			WorkFlowUtil.closejobs(this.mPkVal);
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			selectedRowData.setIsclose(!isClose);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIsclose(!isClose);
			this.commonCheck();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
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
	
	
	/**
	 * 动态显示按钮上面数据个数
	 */
	@Action
	public void showTipsCount(){
		if(this.mPkVal > 0){
			String messageTips = this.serviceContext.jobsMgrService.findTipsCount(this.mPkVal , "sys_msgboard");
			if(!StrUtils.isNull(messageTips)){
				Browser.execClientScript("showButtonTips('showMsgWin',"+messageTips+")");
			}
			messageTips = this.serviceContext.jobsMgrService.findTipsCount(this.mPkVal , "sys_knowledgelib");
			if(!StrUtils.isNull(messageTips)){
				Browser.execClientScript("showButtonTips('showKnowledgeBase',"+messageTips+")");
			}
		}
	}
	
	@Action
	public void addJobsLeaf() {
		try {
			String jobnoleaf = serviceContext.jobsMgrService.addJobsLeaf(
					this.mPkVal, AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!委托单号：" + jobnoleaf);
			//Browser.execClientScript("tabLayoutJsVar.activeTab=0");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void refreshAjaxPanel(){
		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	}
	
	@Action
	public void pageUp(){
		Map map = getPageNumber(this.mPkVal, 0);
		if(map !=null && map.containsKey("row")){
			int row = Integer.parseInt(map.get("row").toString());
			row-=1;
			if(row>0){
				Map result = getPageNumber(0L, row);
				if(result !=null && result.containsKey("id")){
					Long jobid = Long.parseLong(result.get("id").toString());
					this.jobnos = "";
					this.mPkVal = jobid;
					this.update.markUpdate(UpdateLevel.Data, "jobnos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					refreshMasterForm();
				}
			}else{
				MessageUtils.alert("已经是第一条!");
			}
		}
	}
	
	@Action
	public void pageDown(){
		Map map = getPageNumber(this.mPkVal, 0);
		if(map !=null && map.containsKey("row")){
			int row = Integer.parseInt(map.get("row").toString());
			row+=1;
			Map result = getPageNumber(0L, row);
			if(result !=null && result.containsKey("id")){
				Long jobid = Long.parseLong(result.get("id").toString());
				this.jobnos = "";
				this.mPkVal = jobid;
				this.update.markUpdate(UpdateLevel.Data, "jobnos");
				this.update.markUpdate(UpdateLevel.Data, "mPkVal");
				refreshMasterForm();
			}else{
				MessageUtils.alert("已经是最后一条!");
			}
		}
	}
	
	/**
	 * 获取工作单列表结果集序号(过滤条件同工作单列表)
	 * @param jobid 传入jobid可获取所在结果集序号(不建议同时传入2个变量,为空传入0)
	 * @param pageNumber 传入序号可获取对应jobid
	 * @return 返回null为过界 Map {id,row}
	 */
	public Map getPageNumber(Long jobid,int pageNumber){
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String filter = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("\n SELECT row,id FROM ");
		sbsql.append("\n (SELECT row_number() OVER() AS row,id FROM");
		sbsql.append("\n (SELECT id FROM _fina_jobs t");
		sbsql.append("\n WHERE isdelete = FALSE");
		sbsql.append("\n AND (jobtype = 'L')");
		sbsql.append(filter);
		sbsql.append(corpfilter);
		String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_ship_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			sbsql.append("\n AND t.jobdate >= NOW()::DATE-60");
		}else{
			sbsql.append("\n AND t.jobdate >= NOW()::DATE-"+Integer.parseInt(findUserCfgVal));
		}
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		String seach = ConfigUtils.findUserSysCfgVal("pages.module.land.jobsBean.search", AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(seach)){
			sbsql.append(seach);
		}
		
		String ordersql = AppUtils.getUserColorder("pages.module.land.jobsBean.grid");
		if(!StrUtils.isNull(ordersql)){
			ordersql = " ORDER BY " + ordersql;
			sbsql.append(ordersql);
		}
		
		String gridLimit = ConfigUtils.findUserCfgVal("pages.module.land.jobsBean"+".grid.pagesize", AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(gridLimit)){
			String limitSql = " LIMIT " + gridLimit;
			sbsql.append(limitSql);
		}
		
		sbsql.append("\n ) AS T ) AS T2");
		sbsql.append("\n WHERE ");
		if(jobid > 0){
			sbsql.append("\n T2.id ="+jobid);//查jobid对应的序列号
		}
		if(pageNumber > 0){
			sbsql.append("\n T2.row ="+pageNumber);//查序列号对应的jobid
		}
		
		Map map = null;
		try {
			map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
		} catch (NoRowException e) {
			//返回null为过界
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		return map;
	}
	
	
	@Bind
	public UIIFrame taskCommentsIframe;
	@Bind
	public UIIFrame traceIframe2;
	
	@Action
	public void showBpm(){
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+this.mPkVal+"' LIMIT 1";
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
		}
		taskCommentsIframe.load("../../../bpm/bpmshowcomments.jsp?processinstanceid="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		traceIframe2.load("../../../bpm/model/trace.html?language="+AppUtils.getUserSession().getMlType()
				+"&id="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show();traceWindow.show()");
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
	
	/**
	 * 操作部门
	 * @return
	 */
	@Bind(id="deptop")
    public List<SelectItem> getdeptop() {
		List<SelectItem> lists = null;
		lists = new ArrayList<SelectItem>();
		SelectItem selectItem = new SelectItem(0L,"");
		lists.add(selectItem);

		long l = selectedRowData.getCorpidop()-Long.valueOf("157970752274") == 0 ? selectedRowData.getCorpid() : selectedRowData.getCorpidop();//操作公司是香港默认操作部门来自接单公司--157970752274正式系统ID
		long ll = 0L;
		if(selectedRowData.getCorpidop2() != null){
			ll = selectedRowData.getCorpidop2()-Long.valueOf("157970752274") == 0 ? selectedRowData.getCorpid() : selectedRowData.getCorpidop2();//28813222274测试系统ID
		}
		try{
			// AND isdeptop = TRUE
			String sql = "SELECT id,name FROM sys_department WHERE isdelete = FALSE AND isdeptop = TRUE";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(maps.size()>0){
				for(Map map:maps){
					selectItem = new SelectItem(StrUtils.getMapVal(map, "id"),StrUtils.getMapVal(map, "name"));
					lists.add(selectItem);
				}
			}
		}catch(NoRowException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
}
