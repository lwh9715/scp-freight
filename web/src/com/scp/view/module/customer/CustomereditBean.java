package com.scp.view.module.customer;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.customer.customereditBean", scope = ManagedBeanScope.REQUEST)
public class CustomereditBean extends MastDtlView {

	@Bind
	@SaveState
	@Accessible
	public Boolean iswarehouse;

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();

	@Bind
	@SaveState
	@Accessible
	public SysCorporation repetitionTips;

	@Bind
	@SaveState
	public String actionJsText;

	@Bind
	public UIButton sop;

	@Bind
	public UIWindow tipsWindow;

	@Override
	public void refreshForm() {

	}

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Bind
	public Long userid;

	@SaveState
	public String username;

	@Bind
	public UIButton addMaster;
	@Bind
	public UIButton addBlacklist;
	@Bind
	public UIButton searchCustomerInfo;

	@Bind
	public UIWindow showSosWindow2;

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			actionJsText = "";
			Browser.execClientScript("payramarksJs.hide();");
			init();
			initCtrl();
			String taskid = AppUtils.getReqParam("taskid");
			if(!StrUtils.isNull(taskid)){
				actionJsText = "";
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
				List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmTask.getProcessid()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'js'");
				for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
					actionJsText+=bpmWorkItem.getActions();
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
			userid = AppUtils.getUserSession().getUserid();
		}

		SysCorporation data = serviceContext.customerMgrService.sysCorporationDao.findById(this.mPkVal);
		if(data!=null&&data.getSalesid() != null){
			SysUser us = serviceContext.userMgrService.sysUserDao.findById(data.getSalesid());
			if(us  != null){
				if(us.getDeptid() != null){
					SysDepartment sd = serviceContext.sysDeptService.sysDepartmentDao.findById(us.getDeptid());
					this.username = us.getNamec()+"/"+us.getNamee()+"/"+(sd!=null?sd.getName():"");
				}else{
					this.username = us.getNamec()+"/"+us.getNamee();
				}
			}
		}

		String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
				addMaster.setDisabled(true);
				delMaster.setDisabled(true);
				saveMaster.setDisabled(true);
				addBlacklist.setDisabled(true);
				searchCustomerInfo.setDisabled(true);
			}
		} catch (Exception e) {
		}
	}

	@Action
	public void  addBlacklist(){
		//System.out.println("id--->"+this.mPkVal);
		try{
			String querySql = "SELECT f_sys_corporation_black('id="+this.mPkVal+";usercode="+AppUtils.getUserSession().getUsercode()+"');";
			this.serviceContext.attachmentService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			MessageUtils.alert("OK!");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}

	/**
	 * 钩选全包散货时,自动钩选仓储客户
	 */
	@Action
	public void isddpAjaxSubmit() {
		Boolean isddp = this.selectedRowData.getIsddp();
		if(isddp) {
			this.iswarehouse = true;
		} else {
			this.iswarehouse = false;
		}
		this.update.markUpdate(UpdateLevel.Data, "masterEditPanel");
	}

	@Bind
	public UIIFrame arapIframe;

	@Bind
	public UIIFrame contactIframe;

	@Bind
	public UIIFrame accountIframe;

	@Bind
	public UIIFrame corpinvIframe;

	@Bind
	public UIIFrame contractIframe;


	@Bind
	public UIIFrame memorialdayIframe;

	@Bind
	public UIIFrame custServiceIframe;

	@Bind
	public UIIFrame custCareIframe;

	@Bind
	public UIIFrame csUserIframe;

	@Bind
	public UIIFrame knowledgeBaseIframe;

	@Bind
	public UIIFrame apiIframe;

	@Bind
	public UIIFrame sysCorpextIframe;

	@Bind
	public UIIFrame sysArApIframe;

	@Action
	public void showAPI() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				apiIframe.load(blankUrl);
			}else{
				apiIframe.load( "../customer/api.xhtml?customerid="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showArAp() {
		try {
			showSosWindow2.show();
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				sysArApIframe.load(blankUrl);
			}else{
				sysArApIframe.load( "../customer/customerarap.xhtml?customerid="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Action
	private void checkRepeatAjaxSubmit() {
		String name = AppUtils.getReqParam("name");
		String value = AppUtils.getReqParam("value");
		if(StrUtils.isNull(value)){this.alert("不能为空!");return;}
		try {
			repetitionTips = serviceContext.customerMgrService.repeat(this.mPkVal , name , value);
			if(repetitionTips != null){
				tipsWindow.show();
				update.markUpdate(true, UpdateLevel.Data, "tipsPanel");
				if("abbr".equals(name)){
					Browser.execClientScript("abbrjsvar.setValue('');");
				}else if("namec".equals(name)){
					Browser.execClientScript("namecjsvar.setValue('');");
				}else if("namee".equals(name)){
					Browser.execClientScript("nameejsvar.setValue('');");
				}else if("code".equals(name)){
					Browser.execClientScript("codejsvar.setValue('');");
				}
			}
		} catch (CommonRuntimeException e) {
			String exception  = e.getLocalizedMessage();
			this.alert(exception);
		}
	}

	@Action
	public void showArapEdit() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				arapIframe.load(blankUrl);
			}else{
//				String blankUrl = AppUtil.getContextPath() + "/pages/module/common/blank.xhtml";
//				arapIframe.load(blankUrl);
				arapIframe.load( "../customer/assigneduser.xhtml?linktype=C&id="+this.mPkVal+"&flag=CustomereditBean");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showContact() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				contactIframe.load(blankUrl);
			}else{
				contactIframe.load( "../customer/contact.xhtml?customerid="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showAccount() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				accountIframe.load(blankUrl);
			}else{
				accountIframe.load( "../customer/account.xhtml?customerid="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}



	@Action
	public void showMemorialday() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				memorialdayIframe.load(blankUrl);

			}else{
				memorialdayIframe.load( "../customer/memorialday.xhtml?customerid="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCustService() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				custServiceIframe.load(blankUrl);

			}else{
				custServiceIframe.load( "../customer/custservice.xhtml?customerid="+this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCustCare() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				custCareIframe.load(blankUrl);

			}else{
				custCareIframe.load( "../customer/custcare.xhtml?customerid="+this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showAttachmentIframe() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				attachmentIframe.load(blankUrl);
			} else {
				attachmentIframe.load(AppUtils.getContextPath()
						+ "/pages/module/common/attachment.xhtml?linkid="
						+ this.mPkVal+"&flag=CustomereditBean");
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCsUser() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				csUserIframe.load(blankUrl);

			}else{
				csUserIframe.load( "../customer/csuser.xhtml?customerid="+this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIIFrame visitIframe;

	@Action
	public void showVisit() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				visitIframe.load(blankUrl);
			} else {
				visitIframe.load("../salesmgr/accesscustomer.xhtml?customerid="
						+ this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void init() {
		selectedRowData = new SysCorporation();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();

		} else {
			addMaster();
		}
	}

	private void initCtrl() {
		if(this.mPkVal == -1L){
			sop.setDisabled(true);
		}else{
			sop.setDisabled(false);
		}
		for (String s : AppUtils.getUserRoleModuleCtrl(410000L)) {
			if (s.endsWith("marks")){
				Browser.execClientScript("payramarksJs.show();");
			}
		}



	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "soptext");
		initCtrl();
		showArapEdit();
		showContact();
		showAccount();
		showMemorialday();
		showCustService();
		showCustCare();
		showCsUser();
		showAttachmentIframe();
		showKnowledgeBase();
		showAPI();
		showOther();
		showArAp();
	}

    @Override
    public void doServiceSaveMaster() {
        try {
            String namec = selectedRowData.getNamec();
            if (!StrUtils.isNull(namec)) {
                namec = namec.replace("'", "''");
                String dmlSql = "select upper(TRIM(f_str_getfulltohalf('" + namec + "')))  as namecqry;";
                Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(dmlSql);
                selectedRowData.setNamecqry(StrUtils.getMapVal(map, "namecqry"));
            } else {
                MessageUtils.alert("获取不到中文名!");
                return;
            }

            serviceContext.customerMgrService.saveData(selectedRowData);
            MessageUtils.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        this.mPkVal = selectedRowData.getId();
        this.refreshMasterForm();
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
		this.selectedRowData = new SysCorporation();
		this.mPkVal = -1l;
		selectedRowData.setIsar(true);
		selectedRowData.setIsap(true);
		selectedRowData.setIsofficial(true);
		selectedRowData.setIscustomer(true);

		selectedRowData.setIsfcl(true);
		selectedRowData.setIsair(true);

		selectedRowData.setImpexp("E");

		selectedRowData.setLevel("*");
		selectedRowData.setPaytype("B");
		initCtrl();
		showArapEdit();
		showContact();
		showAccount();
		showMemorialday();
		showCustService();
		showCustCare();
		showCsUser();
		showAttachmentIframe();
		showKnowledgeBase();
		showAPI();
		showOther();
		showArAp();
	}

	@Override
	public void delMaster() {

		try {
			serviceContext.customerMgrService.delDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Action
	public void saveMastersop(){
		saveMaster();
	}

	@Action
	public void showKnowledgeBase() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				knowledgeBaseIframe.load(blankUrl);
			}else{
				knowledgeBaseIframe.load( "../../sysmgr/knowledge/knowledgeBase.xhtml?linkid="+this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showOther() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				sysCorpextIframe.load(blankUrl);
			}else{
				sysCorpextIframe.load( "../customer/sysCorpext.xhtml?customerid="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIIFrame defgridIframe;

	@Action
	public void showdefgrid() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				defgridIframe.load(blankUrl);
			}else{
				defgridIframe.load("../../sysmgr/knowledge/ufmsdefgrid.html?linkid="+this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@SaveState
	public String number;

	@Bind
	@SaveState
	public String detailsContent;

	@Bind
	public UIWindow detailsWindow;

	@Bind
	@SaveState
	public String detailsTitle;

	@Action
	public void showDetailsAction() {
		this.number = AppUtils.getReqParam("number");
		String content = AppUtils.getReqParam("content");
		if("1".equals(number)){
			this.detailsContent = content;
			this.detailsTitle = "备注";
		}
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
		this.detailsWindow.show();
	}

	public void setDetails(String number) {
		if("1".equals(number)){
			this.selectedRowData.setRemarks(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "remarks");
		}
	}

	@Action
	public void back() {
		setDetails(this.number);
		this.detailsWindow.close();
	}

	@Action
	public void showCorpinv() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				corpinvIframe.load(blankUrl);
			}else{
				corpinvIframe.load( "./syscorpinv.xhtml?id="+this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showContract() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			contractIframe.load(blankUrl);
		}else{
			contractIframe.load( "./sysContract.xhtml?id="+this.mPkVal);
		}
	}

	@Bind(id="qryCountry")
    public List<SelectItem> getqryCountry() {
    	try {
    		return CommonComBoxBean.getComboxItems("country","country","sys_corporation_agent AS d"
    				,"WHERE isdelete = false and country <> '' and country is not null group by country","ORDER BY country");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind
	public UIIFrame traceIframe;
	@Bind
	public UIWindow showLogsWindow;
	@Action
	public void showLogs() {
		traceIframe.load("../customer/operatelog.xhtml?id="+this.mPkVal);
		showLogsWindow.show();
	}



}
