package com.scp.view.module.ship;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmProcessinstance;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.finance.FinaJobs;
import com.scp.model.order.BusOrder;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.ship.jobseditBean", scope = ManagedBeanScope.REQUEST)
public class JobsEditBean extends MastDtlView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	@SaveState
	@Accessible
	public Long shipid;
	
	@Bind
	@SaveState
	public String orderno;
	
	@Bind
	public UIWindow showMessageWindow;
	
	@Bind
	public UIWindow setnosWindow;
	
	@Bind
	public UIIFrame shippingIframe;
	
	@Bind
	public UIIFrame bookingIframe;

	@Bind
	public UIIFrame truckIframe;

	@Bind
	public UIIFrame customerIframe;

	@Bind
	public UIIFrame billIframe;

	@Bind
	public UIIFrame arapIframe;

	@Bind
	public UIIFrame receiptIframe;
	
	@Bind
	public UIIFrame invoIframe;

	@Bind
	public UIIFrame attachmentIframe;

	@Bind
	public UIIFrame docdefIframe;

	@Bind
	public UIIFrame assignIframe;
	
	@Bind
	public UIIFrame knowledgeBaseIframe;

	@Bind
	public UIIFrame constructIframe;

	@Bind
	public UIIFrame msgIframe;

	@Bind
	public UIIFrame traceIframe;
	
//	@Bind
//	private UIIFrame dtlIFrame;

	@Bind
	public UITabLayout tabLayout;
	
	@Bind
	public UIIFrame agenbillIframe;
	
	@Bind
	public UIIFrame queryIframe;
	
	@Bind
	public UIIFrame foreigntruckIframe;

	@Bind
	public UIIFrame foreigncustomerIframe;
	
	@Bind
	public UIIFrame foreigndeliveryIframe;
	
	@Bind
	public Long department;
	
	@Bind
	@SaveState
	private Boolean cusalesconfirm;
	
	@Bind
	@SaveState
	private String cusales = "";

	@Bind
	@Accessible
	public String fnos;

	@Bind
	public Long sale;

	@SaveState
	public Boolean isPostBack = false;

	@SaveState
	public Long userid;

	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();

	@SaveState
	@Accessible
	public BusShipping shippinginit = new BusShipping();

	@SaveState
	public SysCorpcontacts corpc;
	
	@SaveState
	@Accessible
	public String processInstanceId;
	
	@Bind
	@SaveState
	public String customtext;
	
	@SaveState
	@Accessible
	public String workItemId;
	
	@Bind
	public String customerNamec;
	
	@Bind
	public String customerType;
	
	@Bind
	public UIIFrame statusIframe;
	
	@Bind
	public UIButton addMaster;
	
	@Bind
	public UIButton createDesktopLink;
	
	@Bind
	@SaveState
	public boolean discopy = false;
//	@Bind(id="deptids")
//	@SelectItems
//	@SaveState
//	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
	@Bind
	private UIWindow showCopyDataWindow;
	
	@Bind
	@SaveState
	public boolean isentrust = false;
	
	@Bind
	@SaveState
	public boolean isbookings = false;
	
	@Bind
	@SaveState
	public boolean ismbls = false;
	
	@Bind
	@SaveState
	public boolean transceiver = false;
	
	@Bind
	@SaveState
	public boolean iscabinet = false;
	
	@Bind
	@SaveState
	public boolean shippingterms = false;
	
	@Bind
	@SaveState
	public boolean portinfo = false;
	
	@Bind
	@SaveState
	public boolean bookingagent = false;
	
	@Bind
	@SaveState
	public boolean boxnum = false;
	
	@Bind
	@SaveState
	public boolean markno = false;
	
	@Bind
	@SaveState
	public boolean vesvoy = false;
	
	@Bind
	@SaveState
	public boolean refeno = false;
	
	@Bind
	@SaveState
	public boolean ordernum = false;
	
	@Bind
	@SaveState
	public boolean bookingnum = false;
	
	@Bind
	@SaveState
	public boolean hblnum = false;
	
	@Bind
	@SaveState
	public boolean mblnum = false;
	
	@Bind
	@SaveState
	public boolean cutday = false;
	
	@Bind
	@SaveState
	public boolean etacopy = false;
	
	@Bind
	@SaveState
	public boolean etdcopy = false;
	
	@Bind
	@SaveState
	public boolean atdcopy = false;
	
	@Bind
	@SaveState
	public boolean atacopy = false;
	
	@Bind
	@SaveState
	public boolean poacopy = false;
	
	@Bind
	@SaveState
	public boolean bltypecopy = false;
	
	@Bind
	@SaveState
	public boolean packinglist = false;
	
	@SaveState
	public int activeTab = 0;//页面显示页
	
	@Bind
	@SaveState
	public String soptext;
	
	@Bind
	public String isuserdzz;
	
	@Bind
	public String ismodifynos;
	
	@Bind
	public String issaas;
	
	@SaveState
	public String openIformType;//记录当前打开的iframe
	
	@Override
	public void refreshForm() {

	}
	
	@Bind
	@SaveState
	public String actionJsText;
	
	@Bind
	@SaveState
	public String deptname;
	
	@Bind
	public UIWindow knowledgeBaseWindow;
	
	@Bind
	@SaveState
	public String arapselected = "";

	@Bind
	@SaveState
	public String bltypeJsisshow = "true";

	@Bind
	public long deptop = 0L;

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			initialization();
			init();
			initCtrl();
			this.commonCheck();
			String type = AppUtils.getReqParam("type");
			arapselected = AppUtils.getReqParam("arapid");
			actionJsText = "";
			if("bpm".equals(type)){
				String taskid = AppUtils.getReqParam("taskid");
				if(!StrUtils.isNull(taskid)){
					BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
					List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmTask.getProcessid()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'js'");
					for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
						actionJsText+=bpmWorkItem.getActions();
					}
					String src = AppUtils.getReqParam("src");
					if("orderbpm".equals(src)){
						BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
						String refid = bpmProcessinstance.getRefid();
						if(!StrUtils.isNull(refid)){
							try {
								//neo 20201216 处理订单流程生成工作单后，refid变成订单id+工作单id情况查询
								this.mPkVal = serviceContext.jobsMgrService.finaJobsDao.findOneRowByClauseWhere("isdelete = false AND orderid IN ("+refid+")").getId();
							} catch (Exception e) {
								e.printStackTrace();
								this.mPkVal = Long.parseLong(refid);
							}
						}
					}
				}else{
					String processid = AppUtils.getReqParam("processid");
					List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+processid+" AND taskname = 'Start' AND actiontype = 'js'");
					for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
						actionJsText+=bpmWorkItem.getActions();
					}
				}
				if(actionJsText.indexOf("saveMasterJsVar.setDisabled(false)")>=0){
					saveMaster.setDisabled(false);
				}
				if(actionJsText.indexOf("saveMasterJsVar.setDisabled(true)")>=0){
					saveMaster.setDisabled(true);
				}
				
				//System.out.println("actionJsText:"+actionJsText);
				update.markUpdate(true, UpdateLevel.Data, "actionJsText");
				
				
			}
			
			List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao.findAllByClauseWhere(" formid = '"+this.getMBeanName()+"' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
			for (Sysformcfg sysformcfg : sysformcfgs) {
				actionJsText+=sysformcfg.getJsaction();
			}
			//System.out.println("actionJsText:"+actionJsText);
			update.markUpdate(true, UpdateLevel.Data, "actionJsText");
			
			if ((this.getMsgcontent() != null && !"".equals(this.getMsgcontent()))) {
				showMessageWindow.show();
				this.getMsgcontent();
				this.update.markUpdate("content");
			}
			
			if (selectedRowData.getCusalesid() != null && this.selectedRowData.getCusalesid() > 0) {
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getCusalesid());
				String username = us.getNamec();
				this.cusales = (username != null && !username.equals("") ? username:us.getCode());
			}
//			this.deptids = getqueryDepartid();
			// disableAllButton(selectedRowData.getIscheck());
//			boolean isUseDzz = applicationConf.getIsUseDzz();
//			if(!isUseDzz){
//				isuserdzz = "true";
//			}
			issaas = String.valueOf(applicationConf.isSaas());
			this.update.markUpdate("issaas");
		} else {
			this.isPostBack = true;
		}
		if(!getSysformcfg().equals("")){
			String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
			Browser.execClientScript(js);
		}

		keshihua();
		
		if(AppUtils.getUserSession().getUserid().equals(Long.valueOf("56962290888"))){
			Browser.execClientScript("corpidopJsvar.setReadOnly(false)");
		}
	}

	//可视化追踪
	private void keshihua() {
		try {

			String keshihuasql = "SELECT\n" +
					"\t round(COALESCE(cast(count(distinct bsbc.cntno) as numeric ) / NULLIF(cast((select count(1) from bus_ship_container where isdelete = false and parentid is null and coalesce(cntno,'') !='' and jobid= " + mPkVal + ") as numeric ), 0), 0),4)\n" +
					"FROM\n" +
					"\t\tbus_ship_booking bsb\n" +
					"\t\tLEFT JOIN bus_ship_book_cnt bsbc ON bsb.ID = bsbc.linkid\n" +
					"\t\tLEFT JOIN bus_ship_container_track bsct ON bsbc.ID = bsct.linkid\n" +
					"\t\tinner join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and coalesce(bsc.cntno,'') !='' and bsc.jobid= " + mPkVal + " and bsb.sono = bsc.sono and bsbc.cntno = bsc.cntno\n" +
					"WHERE\n" +
					"\t\tbsct.ID IS NOT NULL\n" +
					"\t\tand bsct.sts1code='EE' OR bsct.sts1code='STSP'\n" +
					"\t\t\n" +
					"union all\n" +
					"SELECT\n" +
					"\t round(COALESCE(cast(count(distinct bsbc.cntno) as numeric ) / NULLIF(cast((select count(1) from bus_ship_container where isdelete = false and parentid is null and coalesce(cntno,'') !='' and jobid= " + mPkVal + ") as numeric ), 0), 0),4)\n" +
					"FROM\n" +
					"\t\tbus_ship_booking bsb\n" +
					"\t\tLEFT JOIN bus_ship_book_cnt bsbc ON bsb.ID = bsbc.linkid\n" +
					"\t\tLEFT JOIN bus_ship_container_track bsct ON bsbc.ID = bsct.linkid\n" +
					"\t\tinner join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and coalesce(bsc.cntno,'') !='' and bsc.jobid= " + mPkVal + " and bsb.sono = bsc.sono and bsbc.cntno = bsc.cntno\n" +
					"WHERE\n" +
					"\t\tbsct.ID IS NOT NULL\n" +
					"\t\tand bsct.sts1code='I' OR bsct.sts1code='GITM'\n" +
					"\t\t\n" +
					"union all\n" +
					"SELECT\n" +
					"\t round(COALESCE(cast(count(distinct bsbc.cntno) as numeric ) / NULLIF(cast((select count(1) from bus_ship_container where isdelete = false and parentid is null and coalesce(cntno,'') !='' and jobid= " + mPkVal + ") as numeric ), 0), 0),4)\n" +
					"FROM\n" +
					"\t\tbus_ship_booking bsb\n" +
					"\t\tLEFT JOIN bus_ship_book_cnt bsbc ON bsb.ID = bsbc.linkid\n" +
					"\t\tLEFT JOIN bus_ship_container_track bsct ON bsbc.ID = bsct.linkid\n" +
					"\t\tinner join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and coalesce(bsc.cntno,'') !='' and bsc.jobid= " + mPkVal + " and bsb.sono = bsc.sono and bsbc.cntno = bsc.cntno\n" +
					"WHERE\n" +
					"\t\tbsct.ID IS NOT NULL\n" +
					"\t\tand bsct.sts1code='AE' OR bsct.sts1code='LOBD'\n" +
					"\t\t\n" +
					"union all\n" +
					"SELECT\n" +
					"\t round(COALESCE(cast(count(distinct bsbc.cntno) as numeric ) / NULLIF(cast((select count(1) from bus_ship_container where isdelete = false and parentid is null and coalesce(cntno,'') !='' and jobid= " + mPkVal + ") as numeric ), 0), 0),4)\n" +
					"FROM\n" +
					"\t\tbus_ship_booking bsb\n" +
					"\t\tLEFT JOIN bus_ship_book_cnt bsbc ON bsb.ID = bsbc.linkid\n" +
					"\t\tLEFT JOIN bus_ship_container_track bsct ON bsbc.ID = bsct.linkid\n" +
					"\t\tinner join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and coalesce(bsc.cntno,'') !='' and bsc.jobid= " + mPkVal + " and bsb.sono = bsc.sono and bsbc.cntno = bsc.cntno\n" +
					"WHERE\n" +
					"\t\tbsct.ID IS NOT NULL\n" +
					"\t\tand bsct.sts1code='UV' OR bsct.sts1code='DSCH'\n" +
					"\n" +
					"union all\n" +
					"SELECT\n" +
					"\t round(COALESCE(cast(count(distinct bsbc.cntno) as numeric ) / NULLIF(cast((select count(1) from bus_ship_container where isdelete = false and parentid is null and coalesce(cntno,'') !='' and jobid= " + mPkVal + ") as numeric ), 0), 0),4)\n" +
					"FROM\n" +
					"\t\tbus_ship_booking bsb\n" +
					"\t\tLEFT JOIN bus_ship_book_cnt bsbc ON bsb.ID = bsbc.linkid\n" +
					"\t\tLEFT JOIN bus_ship_container_track bsct ON bsbc.ID = bsct.linkid\n" +
					"\t\tinner join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and coalesce(bsc.cntno,'') !='' and bsc.jobid= " + mPkVal + " and bsb.sono = bsc.sono and bsbc.cntno = bsc.cntno\n" +
					"WHERE\n" +
					"\t\tbsct.ID IS NOT NULL\n" +
					"\t\tand bsct.sts1code='Z8' OR bsct.sts1code='STCS'\n" +
					"\n" +
					"union all\n" +
					"SELECT\n" +
					"\t round(COALESCE(cast(count(distinct bsbc.cntno) as numeric ) / NULLIF(cast((select count(1) from bus_ship_container where isdelete = false and parentid is null and coalesce(cntno,'') !='' and jobid= " + mPkVal + ") as numeric ), 0), 0),4)\n" +
					"FROM\n" +
					"\t\tbus_ship_booking bsb\n" +
					"\t\tLEFT JOIN bus_ship_book_cnt bsbc ON bsb.ID = bsbc.linkid\n" +
					"\t\tLEFT JOIN bus_ship_container_track bsct ON bsbc.ID = bsct.linkid\n" +
					"\t\tinner join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and coalesce(bsc.cntno,'') !='' and bsc.jobid= " + mPkVal + " and bsb.sono = bsc.sono and bsbc.cntno = bsc.cntno\n" +
					"WHERE\n" +
					"\t\tbsct.ID IS NOT NULL\n" +
					"\t\tand bsct.sts1code='RD' OR bsct.sts1code='RCVE'";

			List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(keshihuasql);
			if (lists != null && lists.size() > 0) {
				for (int i = 0; i < lists.size(); i++) {
					Map map = lists.get(i);
					String round = StrUtils.getMapVal(map, "round");
					BigDecimal bgnow = BigDecimal.valueOf(new BigDecimal("98").multiply(new BigDecimal(round)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
					Browser.execClientScript("$('.greenbeijingse').eq(" + i + ").css('width','" + bgnow + "px');");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initCtrl() {
		addMaster.setDisabled(true);
		saveMaster.setDisabled(true);
		delMaster.setDisabled(true);
		addJobsLeaf.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue())) {
			if (s.endsWith("_add")) {
				addMaster.setDisabled(false);
				saveMaster.setDisabled(false);
				addJobsLeaf.setDisabled(false);
			} else if (s.endsWith("_update")) {
				saveMaster.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}else if (s.endsWith("_modify")) {
				ismodifynos = "true";
			}
		}
		if(this.selectedRowData != null && selectedRowData.getIslock()){
			saveMaster.setDisabled(true);
			delMaster.setDisabled(true);
		}
		//系统设置知识库自动弹出提示
		String auto = ConfigUtils.findSysCfgVal("sys_knowledge_auto_tips");
		if("Y".equals(auto)){
			String	messageTips = this.serviceContext.jobsMgrService.findTipsCount(this.mPkVal , "sys_knowledgelib");
			if(!StrUtils.isNull(messageTips)){
				knowledgeBaseWindow.show();
			}
		}
		
		if("Y".equals(ConfigUtils.findSysCfgVal("JobsMessageAutoOpen"))){
			String messageTips = this.serviceContext.jobsMgrService.findTipsCount(this.mPkVal , "sys_msgboard");
			if(!StrUtils.isNull(messageTips)){
				showMessageAllWindow.show();
			}
		}
		alterDiscopy();
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
	
	@Action
	private void changeCorpid(){
		String corptmp = AppUtils.getReqParam("corpid");
		if(corptmp != null && !corptmp.isEmpty()){
			if(!this.selectedRowData.getCorpid().equals(corptmp)){
				this.selectedRowData.setDeptid(null);
				this.selectedRowData.setCorpid(Long.parseLong(corptmp));
//				this.deptids = getqueryDepartid();
//				update.markUpdate(true,UpdateLevel.Data, "deptcomb");
				Browser.execClientScript("clearDept();");
				//update.markUpdate(true,UpdateLevel.Data, "masterEditPanel");
			}
		}
	}
	
	@Action
	private void changeDept() {
		String sale = AppUtils.getReqParam("sale");
		if (!StrUtils.isNull(sale)) {
			this.sale = Long.parseLong(sale);
			SysDepartment sd = this.serviceContext.userMgrService.sysUserDao
					.findById(this.sale).getSysDepartment();
			if (sd != null) {
				this.department = sd.getId();
			}
			this.update.markUpdate(UpdateLevel.Data, "department");
		}
	}
	
	@SuppressWarnings("deprecation")
	@Action
	public void clearnos(){
		String sql = "UPDATE fina_jobs set nos = '',updater='"+AppUtils.getUserSession().getUsercode()+"',updatetime=NOW() WHERE id ="+selectedRowData.getId() + ";";
		try {
			this.serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
			return;
		}
		this.refreshMasterForm();
	}
	
	@Action
	private void createDesktopLink() {
//		if(!applicationConf.getIsUseDzz()){
//			this.alert("DZZ未启用");
//			return;
//		}
		if(this.mPkVal == null || this.mPkVal <= 0){
			this.alert("无效ID");
			return;
		}
		try {
			String httport = AppUtils.getServerHttPort();
			String dzzuid = AppUtils.getUserSession().getDzzuid();
			String sqlQry = "SELECT f_dzz_desktop_add('httport="+httport+";type=Jobs;jobid="+this.mPkVal+";dzzuid="+dzzuid+";') As t;";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			String sql = StrUtils.getMapVal(m, "t");
//			this.serviceContext.dzzService.executeQuery(sql);
			Browser.execClientScript("showmsg()");
			//Browser.execClientScript(script)
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Action
	public void aorderno(){
		String url = AppUtils.chaosUrlArs("../order/busorderfcl.xhtml") + "&type=edit&id=" + this.selectedRowData.getOrderid();
		AppUtils.openWindow("_showJobno_" + this.selectedRowData.getOrderid(),url);
	}
	
	@SaveState
	private boolean showAttachmentIframe;

	@Action
	public void showAttachmentIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			attachmentIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("finaEnclosure")){
					this.activeTab = i;
				}
			}
			if(!showAttachmentIframe)
				attachmentIframe.load(AppUtils.getContextPath()+ "/pages/module/common/attachment.xhtml?linkid=" + this.mPkVal + "&refid="+this.selectedRowData.getOrderid()
						+ "&jobflag=ship");
			showAttachmentIframe = true;
		}
	}

	@SaveState
	private boolean showArapEdit;

	@Action
	public void showArapEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			arapIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(费用)
				if(list.get(i).get("key").equals("finaCost")){
					this.activeTab = i;
				}
			}
			if(!showArapEdit){
				arapIframe.load("../finance/arapedit.xhtml?customerid="
						+ this.selectedRowData.getCustomerid() + "&jobid="
//						+ this.mPkVal);
						+ this.mPkVal +"&processInstanceId=" + processInstanceId + "&workItemId=" + workItemId+"&arapid="+arapselected);//arapid，账单审核用于勾选费用行
			}
			showArapEdit = true;//如果已经显示了，再打开不重新加载
			openIformType = "arapIframe";
		}
	}

	@SaveState
	private boolean showReceiptEdit;

	
	/**
	 * CC终审流程
	 */

	public void affirmLast() {
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
	}
	
	@Action
	public void showReceiptEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			receiptIframe.load(blankUrl);
		} else {
			// if(!showReceiptEdit)
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(账单)
				if(list.get(i).get("key").equals("finabill")){
					this.activeTab = i;
				}
			}
			String str = "SELECT id FROM fina_bill WHERE isdelete = FALSE AND jobid = "+this.mPkVal+" ORDER BY (CASE WHEN (inputtime IS NOT NULL AND updatetime IS NOT NULL) AND inputtime > updatetime  THEN inputtime ELSE updatetime END) DESC LIMIT 1";
			Map map = null;
			try {
				map = this.serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(str);
			} catch (NoRowException e) {
//				//AppUtils.debug("当前工作单无账单,默认进入新增状态!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String url = "";
			if(map!=null){
				url = "../ship/busbill.xhtml?jobid=" + this.mPkVal + "&processInstanceId=" + processInstanceId + "&workItemId=" + workItemId + "&id=" + map.get("id");
			}else{
				url = "../ship/busbill.xhtml?jobid=" + this.mPkVal + "&processInstanceId=" + processInstanceId + "&workItemId=" + workItemId;
			}
			if(!showReceiptEdit)receiptIframe.load(url);
			showReceiptEdit = true;
			openIformType = "receiptIframe";
		}
	}
	
	@SaveState
	private boolean showInvoEdit;
	
	@Action
	public void showInvoEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			invoIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(发票)
				if(list.get(i).get("key").equals("finainvoice")){
					this.activeTab = i;
				}
			}
			if(!showInvoEdit){
				String str = "SELECT id FROM fina_invoice WHERE isdelete = FALSE AND jobid = "+this.mPkVal+" ORDER BY CASE WHEN inputtime IS NOT NULL > updatetime  IS NOT NULL THEN inputtime ELSE updatetime END DESC LIMIT 1";
				Map map = null;
				try {
					map = this.serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(str);
				} catch (NoRowException e) {
//					//AppUtils.debug("当前工作单无发票,默认进入新增状态!");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String url = "";
				if(map!=null){
					url = "../ship/businvoice.xhtml?jobid=" + this.mPkVal + "&id=" + map.get("id");
				}else{
					url = "../ship/businvoice.xhtml?jobid=" + this.mPkVal;
				}
				invoIframe.load(url);
			}
			showInvoEdit = true;
			openIformType = "invoIframe";
		}
	}

	@SaveState
	private boolean showDocDef;

	@Action
	public void showDocDef() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			docdefIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(文件)
				if(list.get(i).get("key").equals("finaFill")){
					this.activeTab = i;
				}
			}
			if(!showDocDef)
				docdefIframe.load("../bus/busdocdef.xhtml?linkid=" + this.shipid);
			showDocDef = true;
			openIformType = "docdefIframe";
		}
	}

	@SaveState
	private boolean showShipping;

	@Action
	public void showShipping() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			shippingIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(委托)
				if(list.get(i).get("key").equals("finaEntrust")){
					this.activeTab = i;
				}
			}
			if(!showShipping){
				shippingIframe.setSrc("../ship/busshipping.xhtml?id=" + this.mPkVal);
				this.update.markAttributeUpdate(shippingIframe,"src");
				//shippingIframe.load("../ship/busshipping.xhtml?id=" + this.mPkVal);
			}
			
				
			showShipping = true;
			openIformType = "shippingIframe";
		}
	}
	
	@SaveState
	private boolean showBooking;

	@Action
	public void showBooking() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			bookingIframe.load(blankUrl);
		} else {
			this.activeTab = 9;
			if(!showBooking)
				bookingIframe.load("../ship/bookingSpace.xhtml?id=" + this.mPkVal);
			showBooking = true;
		}
	}

	@SaveState
	private boolean showTruck;

	@Action
	public void showTruck() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			truckIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(拖车)
				if(list.get(i).get("key").equals("finatrailer")){
					this.activeTab = i;
				}
			}
			if(!showTruck)
				truckIframe.load("../bus/bustruck.xhtml?id=" + this.mPkVal);
			showTruck = true;
			openIformType = "truckIframe";
		}
	}

	@SaveState
	private boolean showCustomerIframe;

	@Action
	public void showCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			customerIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(报关)
				if(list.get(i).get("key").equals("finacustoms")){
					this.activeTab = i;
				}
			}
			if(!showCustomerIframe)
				customerIframe.load("../bus/buscustoms.xhtml?id=" + this.mPkVal);
			showCustomerIframe = true;
			openIformType = "customerIframe";
		}
	}

	@SaveState
	private boolean showBill;

	@Action
	public void showBill() {
//		if (this.mPkVal == -1l) {
//			String blankUrl = AppUtils.getContextPath()
//					+ "/pages/module/common/blank.html";
//			billIframe.load(blankUrl);
//		} else {
//			// if(!showBill)
//			billIframe.load("../ship/busshipbill.xhtml?id=" + this.mPkVal);
//			// showBill = true;
//		}
	}

	@SaveState
	private boolean showMsg;

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

	@SaveState
	private boolean showAssign;

	@Action
	public void showAssign() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			assignIframe.load(blankUrl);
		} else {
			// if(!showAssign)
			assignIframe.load("../customer/assigneduser.xhtml?type=jobs&id="
					+ this.shipid + "&linktype=J");
			// showAssign = true;
		}
	}

	@SaveState
	private boolean showConstruct;

	@Action
	public void showConstruct() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			constructIframe.load(blankUrl);
		} else {
			// if(!showConstruct)
			constructIframe.load("../ship/jobschild.xhtml?id=" + this.mPkVal);
			// showConstruct = true;
		}
	}

	@SaveState
	private boolean showTrace;

	@Action
	public void showTrace() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			traceIframe.load(blankUrl);
		} else {
			// if(!showTrace)
			traceIframe.load("../bus/optrace.xhtml?jobid=" + this.mPkVal);
			// showTrace = true;
		}
	}

	@Bind
	public UIWindow remarkWindow;


	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		String type = AppUtils.getReqParam("type");
		Browser.execClientScript("remarkWindow.setTitle('工作单备注');");
		this.remarkWindow.show();
	}

	@Action
	public void showAgenbill() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			agenbillIframe.load(blankUrl);
		} else {
			agenbillIframe.load("../agenbill/agenbill.xhtml?jobid=" + this.mPkVal);
		}
	}
	
	@SaveState
	private boolean showStatus;
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
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(跟踪)
				if(list.get(i).get("key").equals("finaTrack")){
					this.activeTab = i;
				}
			}
			String blno=this.selectedRowData.getNos();
			//if(!showStatus)
				statusIframe.load("../common/goodstrack.xhtml?fkid="+ this.mPkVal+"&blno="+ blno + "&type=S");
			//showStatus = true;
		}
	}
	
	@Bind
	public UIIFrame formdefineIframe;
	
	@Action
	public void showFormDefine() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			formdefineIframe.load(blankUrl);
		} else {
			// if(!showTruck)
			for(int i = 0;i<list.size();i++){//遍历找出角标位置
				if(list.get(i).get("key").equals("finaformdefine")){
					this.activeTab = i;
				}
			}
			formdefineIframe.load("/scp/pages/module/formdefine/dynamicform.xhtml?m="+this.getMBeanName()+"&p="+this.mPkVal);
			//formdefineIframe.load("../bus/bustruck.xhtml?id=" + this.mPkVal);
			// showTruck = true;
		}
	}

	@Override
	public void init() {
		addMaster.setDisabled(true);
		selectedRowData = new FinaJobs();
		String id = AppUtils.getReqParam("id");
		processInstanceId =(String)AppUtils.getReqParam("processInstanceId");
		workItemId =(String)AppUtils.getReqParam("workItemId");
		
		if (!StrUtils.isNull(id)) {
			if("orderbpm".equals(AppUtils.getReqParam("src"))){
				try {
					//neo 20201216 处理订单流程生成工作单后，refid变成订单id+工作单id情况查询
					this.mPkVal = serviceContext.jobsMgrService.finaJobsDao.findOneRowByClauseWhere("isdelete = false AND orderid IN ("+id+")").getId();
				} catch (Exception e) {
					e.printStackTrace();
					this.mPkVal = Long.parseLong(id);
				}
			}else{
				this.mPkVal = Long.parseLong(id);
			}
			this.refreshMasterForm();
			this.commonCheck();
		} else {
			addMaster();
		}
		
		String qryTypeStr = AppUtils.getReqParam("qryType");
		String jobnosStr = AppUtils.getReqParam("jobnos");
		String currentTab = AppUtils.getReqParam("currentTab");
		
		if(!StrUtils.isNull(qryTypeStr)){
			this.qryType = qryTypeStr;
			this.jobnos = jobnosStr;
		}else{
			this.qryType = "nos";
		}
		if(!StrUtils.isNull(currentTab)){
			try {
				int tabIndex = Integer.parseInt(currentTab);
				activeTab = tabIndex;
				this.tabLayout.setActiveTab(tabIndex);
				refreshMasterForm();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		getassigninfo();
	}

	@Bind
	@SaveState
	public String mblno;
	
	@Override
	@Action
	public void refreshMasterForm() {
		
		if(this.mPkVal < 0){//新增未保存下刷新处理
			return;
		}
		
		initTabStatus();
		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this.mPkVal);
		if(selectedRowData!= null && selectedRowData.getCustomerid() != null && selectedRowData.getCustomerid() > 0){
			SysCorporation sysCorporation  = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			this.customerNamec = StrUtils.isNull(sysCorporation.getNamec())?sysCorporation.getNamee():sysCorporation.getNamec();
			try {
				if(!StrUtils.isNull(sysCorporation.getCustomertype())){
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT namec FROM dat_filedata WHERE code = '"+sysCorporation.getCustomertype()+"' AND isdelete = FALSE  AND fkcode = 95 LIMIT 1");
					this.customerType = String.valueOf(m.get("namec"));
				}else{
					this.customerType = "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(selectedRowData!= null){
			try {
				List<BusOrder> orderlist = this.serviceContext.busOrderMgrService.busOrderDao.findAllByClauseWhere("isdelete = FALSE AND jobid ="+selectedRowData.getId());
				if(orderlist!=null&&orderlist.size()>0){
					this.orderno = orderlist.get(0).getOrderno();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.orderno = null;
		}
		this.commonCheck();
		if(this.selectedRowData!=null ){
			String sql = " isdelete = false AND jobid =" + this.selectedRowData.getId();
			try {
				BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere(sql);
				this.mblno = busShipping.getMblno();
			} catch (NoRowException e) {
				//return;
				e.printStackTrace();
			}
		}
		if(this.selectedRowData!=null && this.selectedRowData.getCorpid()!=null){
//			this.deptids = getqueryDepartid();
		}
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		setFnos();
		getshipid(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "shipid");
		update.markUpdate(true, UpdateLevel.Data, "tipsTextArea");
		this.update.markUpdate(UpdateLevel.Data, "qryType");
//		showArapEdit();
//		showDocDef();
//		showMsg();
//		showTrace();
//		showAttachmentIframe();
//		showAssign();
//		showBill();
//		showConstruct();
//		showReceiptEdit();
//		showTruck();
		for(int i = 0;i<list.size();i++){//遍历找出显示位置
			if(list.get(i).get("key").equals("finaEntrust")&&activeTab==i){
				showShipping();
			}
			if(list.get(i).get("key").equals("finaCost")&&activeTab==i){
				showArapEdit();
			}
			if(list.get(i).get("key").equals("finabill")&&activeTab==i){
				showReceiptEdit();
			}
			if(list.get(i).get("key").equals("finainvoice")&&activeTab==i){
				showInvoEdit();
			}
			if(list.get(i).get("key").equals("finatrailer")&&activeTab==i){
				showTruck();
			}
			if(list.get(i).get("key").equals("finacustoms")&&activeTab==i){
				showCustomerIframe();
			}
			if(list.get(i).get("key").equals("finaclearance")&&activeTab==i){
				showForeignCustomerIframe();
			}
			if(list.get(i).get("key").equals("finaDelivery")&&activeTab==i){
				showforeigndeliveryIframe();
			}
			if(list.get(i).get("key").equals("finaEnclosure")&&activeTab==i){
				showAttachmentIframe();
			}
			if(list.get(i).get("key").equals("finaFill")&&activeTab==i){
				showDocDef();
			}
			if(list.get(i).get("key").equals("finaTrack")&&activeTab==i){
				showStatus();
			}
			if(list.get(i).get("key").equals("finaformdefine")&&activeTab==i){
				showFormDefine();
			}
//			if(list.get(i).get("key").equals("Elogistics")&&activeTab==i){
//				showElogistics();
//			}
		}
		Browser.execClientScript("changeJobs('"+this.selectedRowData.getCustomerabbr()+"','"+this.selectedRowData.getSales()+"')");
		this.tabLayout.setActiveTab(activeTab);
		setFnos();
		try {
			String sql = "SELECT sop FROM sys_corporation WHERE id = "+this.selectedRowData.getCustomerid();
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.size()>0&&m.get("sop")!=null){
				soptext = m.get("sop").toString();
			}else{
				soptext = "" ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		try {
			String sql = "SELECT '客户类型:'||COALESCE((SELECT namec from dat_filedata where code = COALESCE(c.customertype ,'') AND isdelete =false and fkcode = 95),'')||'\n'||'CODE:'||COALESCE(c.code ,'')||'\n'||'name:'||COALESCE(c.namec ,'')||'\n'||'tel:'||COALESCE(c.tel1 ,'')||'\n'||'email:'||COALESCE(c.email1 ,'')||'\n'||'add:'||COALESCE(c.addressc ,'')||'\n'||'remark:'||COALESCE(c.remarks ,'') AS customtext " +
					"FROM sys_corporation c WHERE id = " +this.selectedRowData.getCustomerid();
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			customtext = m.get("customtext").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		showTipsCount();
		
		//重定向页面就没有类似问题，不用再刷一遍页面数据
		if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_jobedit_redirect_url"))){
			
		}else{
			//2272 工作单编辑界面，在单号这里切换后，原业务员里面的下拉框数据，还是上一个工作单委托人的，这个会串业务员的，工作单里面刷新后，这个业务员下拉框也要对应刷一下
			Browser.execClientScript("customeridval="+selectedRowData.getCustomerid()+";initFlexBox();initSalesByCustomer('"+selectedRowData.getSales()+"');");
		}
	}
	/**
	 * 20180810 动态显示按钮上面数据个数，比如留言数量，知识库数量
	 * Neo 
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
			
			messageTips = this.serviceContext.jobsMgrService.findChildjobCount(this.mPkVal);
			if(!StrUtils.isNull(messageTips)){
				Browser.execClientScript("showButtonTips('showChild',"+messageTips+")");
			}
		}
	}
	
	/**
	 * 重置tab的初始化状态为新，页面会重新加载，当前页面也会重新加载
	 * neo 20170217
	 */
	private void initTabStatus(){
		this.showShipping = false;
		this.showArapEdit = false;
		this.showReceiptEdit = false;
		this.showInvoEdit = false;
		this.showTruck = false;
		this.showCustomerIframe = false;
		this.showDocDef = false;
		this.showAttachmentIframe = false;
		this.showTrace = false;
		this.ifshowForeignCustomerIframe = false;
		this.ifshowforeigndeliveryIframe = false;
//		this.ifshowelogisticsIframe = false;
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
		
//		if(selectedRowData.getCorpidop()-Long.valueOf("157970752274") != 0 && (null != selectedRowData.getCorpidop2() && selectedRowData.getCorpidop2()-Long.valueOf("157970752274") != 0)){
//			if((100 == selectedRowData.getCorpidop() || (null != selectedRowData.getCorpidop2() && 100 == selectedRowData.getCorpidop2())) && deptop == 0L){
//				if(0L == selectedRowData.getDeptop()){
//					selectedRowData.setDeptop(Long.valueOf("43009103888"));
//				}
//			}else if(100 != selectedRowData.getCorpidop() && (null == selectedRowData.getCorpidop2() || 100 != selectedRowData.getCorpidop2())){
//				selectedRowData.setDeptop(0L);
//			}
//		}
		serviceContext.jobsMgrService.saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();

		String sql = " isdelete = false AND jobid =" + this.mPkVal;
		try {

			this.shippinginit = serviceContext.busShippingMgrService.busShippingDao
					.findOneRowByClauseWhere(sql);
			//this.refreshMasterForm();
			Browser.execClientScript("showmsg()");

		} catch (NoRowException e) {
			shippinginit = new BusShipping();
			setCorpc(this.selectedRowData.getCustomerid());
			shippinginit.setJobid(this.mPkVal);
			shippinginit.setSignplace(this.companyToSign(this.selectedRowData.getCorpid()));
			shippinginit.setCustomerid(this.selectedRowData.getCustomerid());
			shippinginit
					.setCustomerabbr(this.selectedRowData.getCustomerabbr());
			shippinginit.setNos(this.selectedRowData.getNos());
			shippinginit.setSingtime(this.selectedRowData.getJobdate());
			shippinginit.setRefno(this.selectedRowData.getRefno());
			shippinginit.setLdtype(this.selectedRowData.getLdtype());
			shippinginit.setCustcontact(this.corpc.getName());
			shippinginit.setCustfax(this.corpc.getFax());
			shippinginit.setCustmobile(this.corpc.getPhone());
			shippinginit.setCusttel(this.corpc.getTel());
			serviceContext.busShippingMgrService.saveData(shippinginit);

			BusShipContainer busShipContainer = new BusShipContainer();
			busShipContainer.setId(0l);
			busShipContainer.setJobid(this.mPkVal);
			busShipContainer.setShipid(shippinginit.getId());
			busShipContainer.setLdtype(this.selectedRowData.getLdtype());
			serviceContext.busShipContainerMgrService.saveData(busShipContainer);

			this.refreshMasterForm();
			Browser.execClientScript("showmsg()");
		} catch (MoreThanOneRowException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			MessageUtils.showException(e);
			refresh();
		}
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	@Bind
	public UIButton createPod;

	@Bind
	public UIButton addJobsLeaf;

	private void disableAllButton(Boolean flag) {
		saveMaster.setDisabled(flag);
		delMaster.setDisabled(flag);
		createPod.setDisabled(flag);
		addJobsLeaf.setDisabled(flag);
	}

	private void disableButton(Boolean flag) {
		delMaster.setDisabled(flag);
		createPod.setDisabled(flag);
		addJobsLeaf.setDisabled(flag);
	}

	@Override
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new FinaJobs();
		this.orderno = null;
		this.mblno = null;
		String url = AppUtils.getContextPath() + "/pages/module/jobs/jobslink.xhtml?id=" + -1L;
//		dtlIFrame.load(url);
//		this.update.markUpdate(UpdateLevel.Data, "dtlIFrame");

		shippinginit = new BusShipping();
		this.mPkVal = -1l;
		selectedRowData.setIsshipping(true);
		selectedRowData.setJobdate(new Date());
		selectedRowData.setJobtype("S");
		selectedRowData.setLdtype("F");
		selectedRowData.setLdtype2("F");
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());
		selectedRowData.setDeptop(0L);
		selectedRowData.setCustomerabbr("");
		selectedRowData.setSaleid(null);
		selectedRowData.setDeptid(null);
		selectedRowData.setIslock(false);
		selectedRowData.setTradeway("F");
		selectedRowData.setSales("");
		selectedRowData.setCusales("");
		this.department = null;
		this.isClose.setDisabled(false);
		this.isCheck.setDisabled(false);
		this.isComplete.setDisabled(false);
		this.repaintCheckbox();
		this.showArapEdit = false;
		this.showAssign = false;
		this.showAttachmentIframe = false;
		this.showBill = false;
		this.showConstruct = false;
		// this.showCustomerIframe = false;
		this.showDocDef = false;
		this.showMsg = false;
		this.showReceiptEdit = false;
		this.showShipping = false;
		this.showTrace = false;
		this.showTruck = false;
		// 执行客户端脚本
		// Browser.execClientScript("autoShowLink()");

		showArapEdit();
		// showDocDef();
		// showMsg();
		// showTrace();
		// showAttachmentIframe();
		// showAssign();
		showBill();
		// showConstruct();
		// showReceiptEdit();
		// showTruck();
		showShipping();
//		this.deptids = getqueryDepartid();
		this.update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		Browser.execClientScript("deptnameJsVar.setValue();");
	}

	@Override
	public void delMaster() {
		try {
			serviceContext.jobsMgrService.removeDate(this.mPkVal, AppUtils
					.getUserSession().getUsercode());
			this.addMaster();
			Browser.execClientScript("showmsg()");
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
	public UIWindow showChildWindow;
	
	@Bind
	public UIWindow showLogsWindow;
	
	@Bind
	public UIWindow showAssignWindow;
	
	@Bind
	public UIWindow showMessageAllWindow;
	
	
	@Action
	public void showChild() {
		showChildWindow.show();
	}
	
	
	@Action
	public void showLogs() {
		showLogsWindow.show();
	}
	
	@Action
	public void showAssignWin() {
		showAssignWindow.show();
	}
	
	@Action
	public void showMsgWin() {
		showMessageAllWindow.show();
	}
	

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
		if (m.get("salesid") != null) {
			selectedRowData.setSaleid((Long) m.get("salesid"));
			SysUser su = this.serviceContext.userMgrService.sysUserDao
					.findById((Long) m.get("salesid"));
			if (su != null) {
				if (su.getSysDepartment() != null) {
					selectedRowData.setDeptid(su.getSysDepartment().getId());
				} else {
					selectedRowData.setDeptid(null);
				}
			} else {
				selectedRowData.setDeptid(null);
			}
		} else {
			selectedRowData.setSaleid(null);
			selectedRowData.setDeptid(null);
		}
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		this.update.markUpdate(UpdateLevel.Data, "sale");
		this.update.markUpdate(UpdateLevel.Data, "department");
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
		String sql = "SELECT id FROM bus_shipping where isdelete =false AND jobid= "
				+ this.mPkVal;
		Map m;
		try {
			m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			this.shipid = (Long) m.get("id");
		} catch (Exception e) {
			// MsgUtil.showException(e);
			MessageUtils.alert("数据异常,委托单被非法删除,请检查!");
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
		return this.jobsnosChooseService
				.getJobsnosDataProvider("AND jobtype = 'S'");
	}

	@Bind
	private String qryJobsnosKey;

	@Bind
	@SaveState
	public String qryType;
	
	@Action
	public void jobsnosQry() {
		this.jobsnosChooseService.qryNos(qryJobsnosKey,qryType);
		this.jobnosGrid.reload();
	}
	

	@Action
	public void showJobnosAction() {
		
		String jobnos = (String) AppUtils.getReqParam("jobnos");
		jobnos = jobnos.trim();
		qryType = (String) AppUtils.getReqParam("qryType");
		
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
				List<Map> cs = jobsnosChooseService.findJobsnos(qryJobsnosKey,"AND jobtype = 'S'",qryType);
				if (cs.size() == 1) {
					this.jobnos = jobnos;
					this.mPkVal = ((Long) cs.get(0).get("id"));
					this.update.markUpdate(UpdateLevel.Data, "jobnos");
					this.update.markUpdate(UpdateLevel.Data, "mPkVal");
					
					if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_jobedit_redirect_url"))){
						String redirectUrl = "'./jobsedit.xhtml?id="+this.mPkVal+"&qryType="+qryType+"&jobnos="+jobnos+"&currentTab="+activeTab+"'";
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
			String redirectUrl = "'./jobsedit.xhtml?id="+this.mPkVal+"&qryType="+qryType+"&jobnos="+jobnos+"&currentTab="+activeTab+"'";
			Browser.execClientScript("window.location.href=" + redirectUrl);
		}else{
			this.jobnos = ((String) m.get("nos"));
			this.update.markUpdate(UpdateLevel.Data, "jobnos");
			this.update.markUpdate(UpdateLevel.Data, "mPkVal");
			showJobsnosWindow.close();
			this.showArapEdit = false;
			this.showAssign = false;
			this.showAttachmentIframe = false;
			this.showBill = false;
			this.showConstruct = false;
			this.showCustomerIframe = false;
			this.showDocDef = false;
			this.showMsg = false;
			this.showTrace = false;
			this.showReceiptEdit = false;
			this.showShipping = false;
			this.showTruck = false;
			this.tabLayout.setActiveTab(1);
			this.refreshMasterForm();
		}
	}


	public void setCorpc(Long customerid) {
		String sql = "isdelete = FALSE AND isdefault = TRUE AND customerid = "
				+ customerid;
		try {
			this.corpc = serviceContext.customerContactsMgrService.sysCorpcontactsDao
					.findOneRowByClauseWhere(sql);
		} catch (NoRowException e) {
			this.corpc = new SysCorpcontacts();
		} catch (Exception e) {
			this.corpc = new SysCorpcontacts();
		}
	}

	@Action
	public void createPod() {
		if (this.mPkVal == -1l) {
			MessageUtils.alert("工作单未保存");
			return;
		}
		try {
			serviceContext.jobsMgrService.createPod(this.mPkVal, AppUtils
					.getUserSession().getUserid(), AppUtils.getUserSession()
					.getUsercode());
			Browser.execClientScript("showmsg()");
			setFnos();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 加载海外单号
	 */
	public void setFnos() {
//		String sql = "SELECT string_agg((SELECT nos FROM fina_jobs WHERE id = k.jobidto AND isdelete = FALSE),',') AS fnos " 
//				+ "FROM fina_jobs_link k WHERE k.jobidfm = " + this.mPkVal 
//				+ " OR k.jobidfm IN (SELECT jobidto FROM fina_jobs_link WHERE jobidfm = " + this.mPkVal + ")";
//		try {
//			Map m = serviceContext.jobsMgrService.daoIbatisTemplate
//					.queryWithUserDefineSql4OnwRow(sql);
//			this.fnos = (String) m.get("fnos");
//		} catch (Exception e) {
//			this.fnos = "";
//		}
//		this.update.markUpdate(UpdateLevel.Data, "fnos");
		if(applicationConf.isSaas())return;
		String url = AppUtils.getContextPath() + "/pages/module/jobs/jobslink.xhtml?id=" + this.mPkVal;
//		dtlIFrame.load(url);
//		//this.update.markUpdate(UpdateLevel.Data, "dtlIFrame");
//		dtlIFrame.repaint();
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

	@Bind
	public String content;

	public String getMsgcontent() {
		String sql = "SELECT f_sys_msgboard_getmsg('linkid=" + this.mPkVal
				+ ";user="+AppUtils.getUserSession().getUsercode()+"')";
		List message = new ArrayList();
		String mes = "";
		message = serviceContext.accountMgrService.datAccountDao
				.executeQuery(sql);
		for (int i = 0; i < message.size(); i++) {
			mes = mes + (String) message.get(i);
		}
		this.content = mes;
		return this.content;
	}

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
			if (isClose || isComplete) {
				this.disableAllButton(true);
			}else if(isCheck){
				this.disableButton(true);
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

	@Bind
	public UICheckBox isClose;

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
	public UICheckBox isCheck;

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
				String sql11 = " isdelete = false AND jobid =" + this.selectedRowData.getId();
				BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere(sql11);
				if ("true".equals(bltypeJsisshow) && (busShipping.getBltype() == null || "".equals(busShipping.getBltype()))) {
					Browser.execClientScript("layer.msg('审核前提是所有必填信息录入完整(提单类型)',{offset:['60%','40%'],time:5000,type: 1});");
					return;
				}

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
            Browser.execClientScript("layer.msg('" + MessageUtils.returnException(e) + "',{offset:['60%','40%'],time:5000,type: 1});");
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
	 * 跟踪
	 */
	@Action
	public void showQuery() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			queryIframe.load(blankUrl);
		} else {
			queryIframe.load("/web/blno_query.jsp" );
		}
	}

	@Bind
	public UICheckBox isComplete;

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

	private void repaintCheckbox() {
		this.isClose.repaint();
		this.isCheck.repaint();
		this.isComplete.repaint();
	}

	public String companyToSign(long corpid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("US", "SHENZHEN,CHINA");
		map.put("SZ", "SHENZHEN,CHINA");
		map.put("GZ", "GUANGZHOU,CHINA");
		map.put("NB", "NINGBO,CHINA");
		map.put("SH", "SHANGHAI,CHINA");
		map.put("TJ", "TIANJIN,CHINA");
		map.put("QD", "QINGDAO,CHINA");
		Iterator it = map.keySet().iterator();
		String sql = "SELECT abbcode FROM sys_corporation WHERE id = "
				+ corpid + " ";
		List<Object> list = serviceContext.busShippingMgrService.busShippingDao
				.executeQuery(sql);
		if (list != null && list.size() > 0) {
			String abbr = (String) list.get(0);
			while (it.hasNext()) {
				String key = (String) it.next();
				if (abbr.contains(key)) {
					return map.get(key);
				}
			}
		}
		return null;
	}
	
	@Action
	public void clearAction(){
		try {
			serviceContext.jobsMgrService.removeJobsLink(this.mPkVal);
			
		} catch (Exception e) {
			MessageUtils.alert("操作发生异常");
		}finally{
			this.setFnos();
		}
		//alert("海外单号清除成功!");
	}
	
	/**
	 * 拖柜
	 */
	@Action
	public void showForeignTruck() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			foreigntruckIframe.load(blankUrl);
		} else {
			foreigntruckIframe.load("../bus/foreignbustruck.xhtml?id=" + this.mPkVal);
		}
	}
	
	@SaveState
	private boolean ifshowForeignCustomerIframe;

	/**
	 * 进口清关
	 */
	@Action
	public void showForeignCustomerIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			foreigncustomerIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(清关)
				if(list.get(i).get("key").equals("finaclearance")){
					this.activeTab = i;
				}
			}
			if(!ifshowForeignCustomerIframe)
				foreigncustomerIframe.load("../bus/foreignbuscustoms.xhtml?id=" + this.mPkVal);
			ifshowForeignCustomerIframe = true;
			openIformType = "foreigncustomerIframe";
		}
	}
	
	@SaveState
	private boolean ifshowforeigndeliveryIframe;
	
	/**
	 * 清关派送
	 */
	@Action
	public void showforeigndeliveryIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			foreigndeliveryIframe.load(blankUrl);
		} else {
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(清关派送)
				if(list.get(i).get("key").equals("finaDelivery")){
					this.activeTab = i;
				}
			}
			if(!ifshowforeigndeliveryIframe)	
				foreigndeliveryIframe.load("../bus/foreigndelivery.xhtml?id=" + this.mPkVal);
			ifshowforeigndeliveryIframe = true;
			openIformType = "showAttachmentIframe";
		}
	}
	
	/**
	 * 选委托人提取出相应业务员
	 */
	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		String corpidv = AppUtils.getReqParam("corpidv");
		Long uid = AppUtils.getUserSession().getUserid();
		Long salesid=null;
		Long deptid;
		String salesname;
		String tradeway;
		StringBuilder querySql = new StringBuilder();
		querySql.append("\n SELECT a.id AS salesid,a.namec AS salesname,deptid,corpid FROM sys_user a ");
		querySql.append("\n WHERE a.isdelete = FALSE ");
		querySql.append("\n  AND CASE WHEN EXISTS(SELECT 1 FROM sys_user_assign  WHERE linkid = "+customerid+" and linktype = 'C' AND roletype = 'S' AND userid = "+uid+") THEN a.id = "+uid+" ");
		querySql.append("\n WHEN EXISTS(SELECT a.id AS salesid,a.namec AS salesname FROM sys_user a ");
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
			//2025 委托人选择后，提取业务员后，按业务员对应的分公司，修改上面的接单公司
			try {
				String isDeptNotChangebySales = ConfigUtils.findSysCfgVal("jobs_dept_notchangeby_sales");//系统设置，接单公司不按业务员自动联动
				if("Y".equals(isDeptNotChangebySales)){
				}else{
					if(!StrUtils.isNull(salesidStr)){
						SysUser sysuer = serviceContext.userMgrService.sysUserDao.findById(salesid);
						if(sysuer!=null&&sysuer.getSysCorporation()!=null){
							this.selectedRowData.setCorpid(sysuer.getSysCorporation().getId());
							//this.selectedRowData.setCorpidop(sysuer.getSysCorporation().getId());
							update.markUpdate(true, UpdateLevel.Data, "corpid");
							//update.markUpdate(true, UpdateLevel.Data, "corpidop");
						}
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
			this.selectedRowData.setImpexp(StrUtils.getMapVal(m_tradeway, "impexp"));
			String corpid = StrUtils.getMapVal(m, "corpid");
			this.salescorpid =  StrUtils.isNull(corpid)?0l:Long.valueOf(corpid);
//			this.deptids = getqueryDepartid();
			deptname = serviceContext.userMgrService.getDepartNameByUserId(salesid);
			update.markUpdate(true, UpdateLevel.Data, "deptname");
			update.markUpdate(true, UpdateLevel.Data, "tradeway");
			update.markUpdate(true, UpdateLevel.Data, "impexp");
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
		try {
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
	
	/*
	 * 提示分派业务操作客服未填写
	 * */
	public void alterDiscopy(){
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map = new HashMap<String, String>();
		String tips = "尚未分派!";
		boolean flag = false;
		try {
			map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT String_agg(t.typename,',') AS typename FROM (SELECT (SELECT COALESCE(namec,'') AS typename " +
					"from dat_filedata AS d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 160 AND code = a.roletype) AS typename from  sys_user_assign a WHERE a.isdelete =FALSE AND userid > 0 and a.linkid = "+ this.shipid +") t");
			
			if(null == map || map.size() < 1 || null == map.get("typename") || "".equals(map.get("typename")) ){
				MessageUtils.alert("提示(Tips):操作、文件"+tips);
				return;
			}
			if(map.get("typename").contains("文件") == false){
				tips = "文件"+tips;
				flag = true;
			}
			if(map.get("typename").contains("操作") == false){
				if(flag){
					tips = "操作、"+tips;
				}else{
					tips = "操作"+tips;
					flag = true;
				}
			}
			if(flag){
				MessageUtils.alert("提示(Tips):"+tips);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
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
	
	@Override
	public void grid_ondblclick() {
		choosenos();
	}

	@Action
	public void setJobNos(){
		//if(this.mPkVal == null || this.mPkVal <= 0){
		//	this.alert("请先保存");
		//	return;
		//}
		this.grid.reload();
		setnosWindow.show();
	}
	
	@SaveState
	public String codes;
	
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
	
	@Bind
	@SaveState
	public String copyNo;
	
	@Bind
	@SaveState
	public String selectType = "J";
	
	@Action
	public void copyData() {
		if(!isentrust&&!iscabinet&&!isbookings&&!ismbls&&!transceiver  &&!shippingterms&&!portinfo&&!bookingagent&&!boxnum&&!markno
				&&!vesvoy&&!refeno&&!ordernum&&!bookingnum&&!hblnum&&!mblnum&&!cutday&&!etacopy&&!etdcopy&&!atdcopy&&!atacopy&&!poacopy&&!bltypecopy&&!packinglist&&!discopy){
			MessageUtils.alert("请至少选择一项复制内容");
			return;
		}
		String isentrusts = "";
		String iscabinets = "";
		String isbooking = "";
		String ismbl = "";
		String transceivers = "";
		if(isentrust){
			isentrusts = "t";
		}
		if(iscabinet){
			iscabinets = "t";
		}
		if(isbookings){
			isbooking = "t";
		}
		if(ismbls){
			ismbl = "t";
		}
		if(transceiver){
			transceivers = "t";
		}
		String querySql  = "SELECT f_find_jobs_copy2this('nos="+copyNo+";user="+AppUtils.getUserSession().getUsercode()+";jobid="+this.mPkVal.toString()+";isentrusts="+isentrusts+";iscabinets="+iscabinets+";isbooking="+isbooking+";selectType="+selectType+";ismbl="+ismbl+";" +
				"transceiver="+transceivers+";shippingterms="+shippingterms+";portinfo="+portinfo+";bookingagent="+bookingagent+";boxnum="+boxnum+";markno="+markno+";vesvoy="+vesvoy+";refeno="+refeno+";ordernum="+ordernum+";bookingnum="+bookingnum+";" +
						"hblnum="+hblnum+";mblnum="+mblnum+";cutday="+cutday+";etacopy="+etacopy+";etdcopy="+etdcopy+";atdcopy="+atdcopy+";atacopy="+atacopy+";poacopy="+poacopy+";bltypecopy="+bltypecopy+";packinglist="+packinglist+";discopy="+discopy+"') AS nos;";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			Browser.execClientScript("showmsg()");
			showCopyDataWindow.close();
			this.refreshMasterForm();
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND r.corpid =" + this.selectedRowData.getCorpid();
		m.put("qry", qry);
		return m;
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
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT t.corpid UNION SELECT t.corpidop UNION SELECT t.corpidop2 UNION SELECT corpid FROM fina_corp WHERE isdelete = FALSE AND jobid = t.id) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("\n SELECT row,id FROM ");
		sbsql.append("\n (SELECT row_number() OVER() AS row,id FROM");
		sbsql.append("\n (SELECT id FROM _fina_jobs t");
		sbsql.append("\n WHERE isdelete = FALSE");
		sbsql.append("\n AND jobtype = 'S'");
		sbsql.append(filter);
		sbsql.append(corpfilter);
		String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_ship_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			sbsql.append("\n AND t.jobdate >= NOW()::DATE-365");
		}else{
			sbsql.append("\n AND t.jobdate >= NOW()::DATE-"+Integer.parseInt(findUserCfgVal));
		}
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		String seach = ConfigUtils.findUserSysCfgVal("pages.module.ship.jobsBean.search", AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(seach)){
			sbsql.append("\nAND "+seach);
		}
		
		String ordersql = AppUtils.getUserColorder("pages.module.ship.jobsBean.grid");
		if(!StrUtils.isNull(ordersql)){
			ordersql = " ORDER BY " + ordersql;
			sbsql.append(ordersql);
		}
		
		String gridLimit = ConfigUtils.findUserCfgVal("pages.module.ship.jobsBean"+".grid.pagesize", AppUtils.getUserSession().getUserid());
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
//				this.deptids = getqueryDepartid();
			}
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
	
	@Bind(id="sales")
    public List<SelectItem> getSales() {
    	try {
			return CommonComBoxBean.getComboxItems("d.name","d.name","sys_corpcontacts AS d",
				"WHERE isdelete=FALSE AND iscusales = TRUE AND customerid="+this.selectedRowData.getCustomerid()  ,"ORDER BY name");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
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
		alterDiscopy();
	}
	
	@Action
	public void saveMastersop(){
		if(selectedRowData!=null&&selectedRowData.getCustomerid()>0){
			SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			sysCorporation.setSop(soptext);
			serviceContext.sysCorporationService.saveData(sysCorporation);
		}
		saveMaster();
		Browser.execClientScript("tipsWindow.hide();");
	}
	
	@Action
	public void saveRemark(){
		saveMaster();
		Browser.execClientScript("remarkWindow.hide();");
	}
	
	@SaveState
	LinkedList<Map> list = new LinkedList<Map>();
	
	//初始化模块
	public void initialization(){
		Map<String,String> m = new HashMap<String, String>();
		m.put("key", "finaEntrust");
		list.add(m);
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("key", "finaCost");
		list.add(m1);
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("key", "finabill");
		list.add(m2);
		Map<String,String> m3 = new HashMap<String, String>();
		m3.put("key", "finainvoice");
		list.add(m3);
		Map<String,String> m4 = new HashMap<String, String>();
		m4.put("key", "finatrailer");
		list.add(m4);
		Map<String,String> m5 = new HashMap<String, String>();
		m5.put("key", "finacustoms");
		list.add(m5);
		Map<String,String> m6 = new HashMap<String, String>();
		m6.put("key", "finaclearance");
		list.add(m6);
		Map<String,String> m7 = new HashMap<String, String>();
		m7.put("key", "finaDelivery");
		list.add(m7);
		Map<String,String> m8 = new HashMap<String, String>();
		m8.put("key", "finaEnclosure");
		list.add(m8);
		Map<String,String> m9 = new HashMap<String, String>();
		m9.put("key", "finaFill");
		list.add(m9);
		Map<String,String> m10 = new HashMap<String, String>();
		m10.put("key", "finaTrack");
		list.add(m10);
		Map<String,String> m11 = new HashMap<String, String>();
		m11.put("key", "finaformdefine");
//		Map<String,String> m12 = new HashMap<String, String>();
//		m12.put("key", "Elogistics");
//		list.add(m12);
	}
	
	/*
	 * 控制模块权限
	 * */
	@Action
	public void submodule(){
		String tabaction = AppUtils.getReqParam("tabaction");
		String tabCode = AppUtils.getReqParam("tabcode");
		
		if("remove".equals(tabaction)){
			String[] codes = tabCode.split(",");
			if(codes != null && codes.length > 0){
				for (String code : codes) {
					this.tabLayout.removeTab(code);
				}
			}else{
				this.tabLayout.removeTab(tabCode);
			}
		}
		
		List<Map> li = serviceContext.jobsMgrService.findModinroleMy();
		if(li.size() > 0){
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(文件)
				boolean isremove = true;
				String value = list.get(i).get("key").toString();
				for(int j = 0;j<li.size();j++){
					if(value.equals(li.get(j).get("code"))){
						isremove = false;
					}
				}
				if(isremove){
					this.tabLayout.removeTab(value);
				}
			}
			for(int i = 0;i<list.size();i++){//遍历找出角标位置(文件)
				boolean isremove = true;
				String value = list.get(i).get("key").toString();
				for(int j = 0;j<li.size();j++){
					if(value.equals(li.get(j).get("code"))){
						isremove = false;
					}
				}
				if(isremove){
					list.remove(i);
				}
			}
		}else{
			Browser.execClientScript("tabLayoutJsVar.hide();");
		}
	}
	
	@Action
	public void setChooseNosDefault(){
		
	}
	
	@Action
	public void synchroChild(){
		if (this.mPkVal == -1l) {
			MessageUtils.alert("工作单未保存");
			return;
		}
		try {
			String sql = "SELECT f_fina_jobs_syn_child('jobid="+mPkVal+"');";
			serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
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
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Bind
	@SaveState
	public String bpmremark = "";
	
	@Bind
	@SaveState
	public String nextAssignUser2 = "";
	
	@Bind
	@SaveState
	public String bpmremark2 = "";
	
	
	@Action
	public void applyBPM() {
		if(this.mPkVal==null||!(mPkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}

		if ("账单审核".equals(taskname)) {
			BusShipping busShipping = serviceContext.busShippingMgrService.findByjobId(selectedRowData.getId());
			String thismblno = busShipping.getMblno();
			if (StrUtils.isNull(thismblno)) {
				MessageUtils.alert("MBL提单号码不能为空！");
				return;
			}
		}
		
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-19822F44";
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
	
	@Action
	public void applyBPM2() {
		if(this.mPkVal==null||!(mPkVal>0)){
			MessageUtils.alert("请先保存！");
			return;
		}

		BusShipping busShipping = null;
		try {
			busShipping = serviceContext.busShippingMgrService.findByjobId(selectedRowData.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
 		String processinsVar = "isgetmbl"+"-"+busShipping.getIsgetmbl()+"-"+"取单";
 		
 		if(busShipping.getIsgetmbl()){
			if(taskname2.equals("电放负责人")){
				taskname2 = "正本提单管理";
			}
		}else{
			if(taskname2.equals("正本提单管理") || taskname2.equals("正本电放负责人")){
				taskname2 = "电放负责人";
			}
		}

		try {
 			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
 				if(StrUtils.isNull(nextAssignUser2))nextAssignUser2="0";
				String processCode = "BPM-8F7A83E4";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser2+";bpmremarks="+StrUtils.getSqlFormat(bpmremark2)+";taskname="+taskname2+";refno="+this.selectedRowData.getNos()+";refid="+this.selectedRowData.getId()+";processinsVar="+processinsVar+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
				String sub =  sm.get("rets").toString();
				MessageUtils.alert("OK");
				Browser.execClientScript("bpmWindow2JsVar.hide();");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser2 = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind
	public UIDataGrid gridUser2;
	
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
	
	@Bind(id = "gridUser2", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider2() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				Object[] obj = serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere3(qryMapUser2), start, limit)
				.toArray();
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere3(qryMapUser2), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser2));
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
	
	@Bind
	@SaveState
	public String qryuserdesc2 = "";
	

	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc2 = StrUtils.getSqlFormat(qryuserdesc2);
		qryuserdesc2 = qryuserdesc2.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc2) ){
			qry += "AND (code ILIKE '%"+qryuserdesc2+"%' OR namec ILIKE '%"+qryuserdesc2+"%' OR namee ILIKE '%"+qryuserdesc2+"%' OR depter2 ILIKE '%"+qryuserdesc2+"%' OR company ILIKE '%"+qryuserdesc2+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Action
	public void qryuser2() {
		this.gridUser2.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Bind
	@SaveState
	public String user2 = "";
	
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
		//nextAssignUser = nextAssignUser.substring(0, nextAssignUser.length()-1);
		//user = user.substring(0, user.length()-1);
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Action
	public void confirm2() {
		String[] ids = this.gridUser2.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser2.contains(id)){
				this.nextAssignUser2 = nextAssignUser2 + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user2 = user2 + us.getNamec() + ",";
			}
		}
		//nextAssignUser = nextAssignUser.substring(0, nextAssignUser.length()-1);
		//user = user.substring(0, user.length()-1);
		update.markUpdate(true, UpdateLevel.Data, "bpmpane2");
	}
	
	@Action
	public void confirmAndClose2(){
		this.confirm2();
		Browser.execClientScript("userWindow2Js.hide();");
	}
	
	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void delBPM2() {
		this.nextAssignUser2 ="";
		this.user2 = "";
		this.bpmremark2 = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel2");
	}
	
	@Action
	public void applyBPMform() {
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-19822F44";
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
	
	@Action
	public void applyBPMform2() {
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser2))nextAssignUser2="0";
				String processCode = "BPM-8F7A83E4";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+this.selectedRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str =  s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPM2JsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPM2JsVar.setValue(1);");
				}
				Browser.execClientScript("bpmWindow2JsVar.show();");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	@SaveState
	public String taskname;
	
	@Bind
	@SaveState
	public String taskname2;
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-19822F44";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
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
	
	@Bind(id="taskDefine2")
    public List<SelectItem> getTaskDefine2() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-8F7A83E4";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End','提交') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao.findById(shipid);
			for(Map map:maps){
				if(busShipping.getIsgetmbl()){
					if(StrUtils.getMapVal(map, "taskname").equals("电放负责人")){
						continue;
					}
				}else{
					if(StrUtils.getMapVal(map, "taskname").equals("正本提单管理") ||StrUtils.getMapVal(map, "taskname").equals("正本电放负责人") ){
						continue;
					}
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

	@Action
	public void refreshAjaxSubmit(){
		this.refresh();
		refreshMasterForm();
	}
	
	@Action
	public void refreshAjaxPanel(){
		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	@Bind
	public UIIFrame traceIframe2;
	
	@Action
	public void showBpm(){
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+this.mPkVal+"' AND isdelete = FALSE ORDER BY id DESC LIMIT 1";
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
	
	@Bind
	@SaveState
	public String assigninfodesc;
	
	@Action
	public void getassigninfo() {
		String sql  = "SELECT string_agg(rolearea || '/' || (select namec from dat_filedata where isdelete = false and code = a.roletype and fkcode = '160')|| '/' ||(select namec from sys_user where id = a.userid) , '</br>') AS info from  sys_user_assign a where a.linkid = "+this.shipid+" and a.isdelete = false and userid > 0";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			assigninfodesc = StrUtils.getMapVal(m, "info");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "assigninfodesc");
		}
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
	
	@Bind(id="corpop2")
    public List<SelectItem> getCorpop2() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String sql = "SELECT * FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = FALSE order by namec";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "id"),StrUtils.getMapVal(map, "code")+"/"+StrUtils.getMapVal(map, "abbr"));
				lists.add(selectItem);
			}
		}catch(NoRowException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
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
//		long l = selectedRowData.getCorpidop()-Long.valueOf("28813222274") == 0 ? selectedRowData.getCorpid() : selectedRowData.getCorpidop();//28813222274测试系统ID
		long ll = 0L;
		if(selectedRowData.getCorpidop2() != null){
			ll = selectedRowData.getCorpidop2()-Long.valueOf("157970752274") == 0 ? selectedRowData.getCorpid() : selectedRowData.getCorpidop2();//28813222274测试系统ID
		}
		try{
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
	
//	@Bind
//	public UIIFrame elogisticsIframe;
//	
//	@SaveState
//	private boolean ifshowelogisticsIframe;
	
	/**
	 * 电商
	 */
//	@Action
//	public void showElogistics() {
//		if (this.mPkVal == -1l) {
//			String blankUrl = AppUtils.getContextPath()
//					+ "/pages/module/common/blank.html";
//			elogisticsIframe.load(blankUrl);
//		} else {
//			for(int i = 0;i<list.size();i++){//遍历找出角标位置(电商)
//				if(list.get(i).get("key").equals("Elogistics")){
//					this.activeTab = i;
//				}
//			}
//			if(!ifshowelogisticsIframe)
//				elogisticsIframe.load("/pages/module/elogistics/jobs/jobs.xhtml?id=" + this.mPkVal);
//			ifshowelogisticsIframe = true;
//			openIformType = "showAttachmentIframe";
//		}
//	}


	@Bind
	public UIDataGrid gridCntTracksgrid;

	@Bind(id = "gridCntTracksgrid", attribute = "dataProvider")
	protected GridDataProvider getGridCntTracksgridProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridCntTracksgrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWheregridCntTracks(), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridCntTracksgrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWheregridCntTracks());
				Integer count = (Integer) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	public Map getQryClauseWheregridCntTracks() {
		Map map = super.getQryClauseWhere(new HashMap<String, Object>());

		map.put("jobid", mPkVal);
		if ("0".equals(keshihuaflag)) {
			map.put("filter", "\n and bsct.sts1code='EE' OR bsct.sts1code='STSP'");
		} else if ("1".equals(keshihuaflag)) {
			map.put("filter", "\n and bsct.sts1code='I' OR bsct.sts1code='GITM'");
		} else if ("2".equals(keshihuaflag)) {
			map.put("filter", "\n and bsct.sts1code='AE' OR bsct.sts1code='LOBD'");
		} else if ("3".equals(keshihuaflag)) {
			map.put("filter", "\n and and bsct.sts1code='UV' OR bsct.sts1code='DSCH'");
		} else if ("4".equals(keshihuaflag)) {
			map.put("filter", "\n and bsct.sts1code='Z8' OR bsct.sts1code='STCS'");
		} else if ("5".equals(keshihuaflag)) {
			map.put("filter", "\n and bsct.sts1code='RD' OR bsct.sts1code='RCVE'");
		} else {
			map.put("filter", "\n and 1=2");
		}
		return map;
	}

	@Bind
	@SaveState
	public String keshihuaflag;

	@Action
	public void showkeshihuaTipsJsVar() {
		keshihuaflag =  AppUtils.getReqParam("keshihuaflag");
		gridCntTracksgrid.reload();
	}
	
	/**
	 * 客服确认
	 */
	@Action
	public void cusalesSubmit(){
		cusalesconfirm = null == selectedRowData.getCusalesconfirm() ? false : selectedRowData.getCusalesconfirm();
		if(null == selectedRowData || selectedRowData.getId() < 1){
			MessageUtils.alert("请先保存");
			this.refresh();
			return;
		}
		long userid = null == selectedRowData.getCusalesid() ? 0L : selectedRowData.getCusalesid();
		try {
			if (cusalesconfirm == true){
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("Update fina_jobs SET cusalesconfirm = "+cusalesconfirm+",cusalesid = "+AppUtils.getUserSession().getUserid()+",updater = '"+AppUtils.getUserSession().getUsercode()+"' WHERE isdelete = false and id = "+selectedRowData.getId());
				cusales	= AppUtils.getUserSession().getUsername();
				Browser.execClientScript("$('#cusales_input').val('" + AppUtils.getUserSession().getUsername() + "')");
			}else{
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("Update fina_jobs SET cusalesconfirm = "+cusalesconfirm+",cusalesid = NULL, updater = '"+AppUtils.getUserSession().getUsercode()+"' WHERE isdelete = false and id = "+selectedRowData.getId());
				cusales = "";
				Browser.execClientScript("$('#cusales_input').val('')");
			}
			this.refresh();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.alert("没有OBD确认或取消权限!");
			e.printStackTrace();
		}
	}

}
