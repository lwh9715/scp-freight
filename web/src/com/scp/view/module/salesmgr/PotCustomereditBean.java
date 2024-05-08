package com.scp.view.module.salesmgr;

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
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.salesmgr.potcustomereditBean", scope = ManagedBeanScope.REQUEST)
public class PotCustomereditBean extends MastDtlView {

	@Bind
	@SaveState
	@Accessible
	public Boolean iswarehouse;

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Bind
	@SaveState
	public String actionJsText;
	
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
						+ this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void refreshForm() {

	}
	
	public Long userid;
	
	@SaveState
	public String username;

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();

		//如果当前新增的录入人，不是业务员(判断一下 sys_user.issales = false),不赋值界面的业务员
		String sql  = "SELECT issales FROM sys_user WHERE id = " + AppUtils.getUserSession().getUserid();
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String issales = StrUtils.getMapVal(m, "issales");
		if(!StrUtils.isNull(issales) && "true".equals(issales)){
			this.username = AppUtils.getUserSession().getUsername();
		}

		if (!isPostBack) {
			actionJsText = "";
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			init();
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
			
			if(!"BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				Browser.execClientScript("showcheck();");
			}
			

			List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao.findAllByClauseWhere(" formid = '"+this.getMBeanName()+"' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
			for (Sysformcfg sysformcfg : sysformcfgs) {
				actionJsText+=sysformcfg.getJsaction();
			}
			//System.out.println("actionJsText:"+actionJsText);
			update.markUpdate(true, UpdateLevel.Data, "actionJsText");
		}
		
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao.findById(this.mPkVal);
		
		if(this.selectedRowData != null && this.selectedRowData.getSalesid() != null){
			SysUser us = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getSalesid());
			if(us.getDeptid() != null){
				SysDepartment sd = serviceContext.sysDeptService.sysDepartmentDao.findById(us.getDeptid());
				this.username = us.getNamec()+"/"+us.getNamee()+"/"+sd.getName();
			}else{
				this.username = us.getNamec()+"/"+us.getNamee();
			}
		}else{
			init();
		}
		
	}

	/**
	 * 钩选全包散货时,自动钩选仓储客户
	 */
	@Action
	public void isddpAjaxSubmit() {
		Boolean isddp = this.selectedRowData.getIsddp();
		if (isddp) {
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
	public UIIFrame memorialdayIframe;

	@Bind
	public UIIFrame custServiceIframe;

	@Bind
	public UIIFrame custCareIframe;

	@Bind
	public UIIFrame csUserIframe;
	
	@Bind
	@SaveState
	@Accessible
	public SysCorporation repetitionTips;
	
	@Bind
	public UIWindow tipsWindow;
	
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

				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				arapIframe.load(blankUrl);

			} else {

				// String blankUrl = AppUtil.getContextPath() +
				// "/pages/module/common/blank.xhtml";
				// arapIframe.load(blankUrl);
				arapIframe.load("../customer/assigneduser.xhtml?linktype=C&id="
						+ this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showContact() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				contactIframe.load(blankUrl);

			} else {
				contactIframe.load("../customer/contact.xhtml?customerid="
						+ this.mPkVal);
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
				visitIframe.load("./accesscustomer.xhtml?customerid="
						+ this.mPkVal);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showMemorialday() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				memorialdayIframe.load(blankUrl);

			} else {
				memorialdayIframe
						.load("../customer/memorialday.xhtml?customerid="
								+ this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCustService() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				custServiceIframe.load(blankUrl);

			} else {
				custServiceIframe
						.load("../customer/custservice.xhtml?customerid="
								+ this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCustCare() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				custCareIframe.load(blankUrl);

			} else {
				custCareIframe.load("../customer/custcare.xhtml?customerid="
						+ this.mPkVal);
			}

		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void showCsUser() {
		try {
			if (this.mPkVal == -1l) {

				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				csUserIframe.load(blankUrl);

			} else {
				csUserIframe.load("../customer/csuser.xhtml?customerid="
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

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showArapEdit();
		showContact();
		showVisit();
		showMemorialday();
		showCustService();
		showCustCare();
		showCsUser();
		showAttachmentIframe();
	}

	@Override
	public void doServiceSaveMaster() {

		try {
			//if(this.selectedRowData.getLicno().trim().isEmpty()&&this.selectedRowData.getTaxid().trim().isEmpty()){
				//this.alert("营业执照号码和税务登记证号码请至少填其中一项!");
				//return;
			//}
			selectedRowData.setIsofficial(false);
			serviceContext.customerMgrService.saveData(selectedRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

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
		this.selectedRowData = new SysCorporation();
		this.mPkVal = -1l;
		selectedRowData.setIsar(true);
		selectedRowData.setIsap(true);
		selectedRowData.setIsofficial(false);
		selectedRowData.setIscustomer(true);
		selectedRowData.setIsfcl(true);
		selectedRowData.setIsair(true);
		selectedRowData.setLevel("*");
		selectedRowData.setImpexp("E");
		//System.out.println(selectedRowData.getLevel());
		selectedRowData.setSalesid(AppUtils.getUserSession().getUserid());
		showArapEdit();
		showContact();
		showVisit();
		showMemorialday();
		showCustService();
		showCustCare();
		showCsUser();
		showAttachmentIframe();
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
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Action
	public void saveCheck() {
		if(this.mPkVal == null || this.mPkVal < 0){
			this.alert("请先保存！");
			return;
		}
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processName = "客户审核";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("namec = '"+processName+"'");
				String refno = StrUtils.isNull(this.selectedRowData.getAbbr())? this.selectedRowData.getCode():this.selectedRowData.getAbbr();
				String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";refno="+refno+";refid="+this.selectedRowData.getId()+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
				String sub =  sm.get("rets").toString();
				MessageUtils.alert("OK，已提交申请");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
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
				String sqlId = "pages.module.salesmgr.potcustomereditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.salesmgr.potcustomereditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		return getQryClauseWhere(queryMap);
	}
	
	
	@Action
	public void clearQryKeysc() {
		if (qryMapUser != null) {
			qryMapUser.clear();
			update.markUpdate(true, UpdateLevel.Data, "userPanel");
			this.gridUser.reload();
		}
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
		try {
			String[] ids = this.gridUser.getSelectedIds();
			if(ids == null || ids.length  < 1){
				this.alert("Please choose one!");
				return;
			}
			for (String id : ids) {
				this.nextAssignUser = nextAssignUser + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
			//nextAssignUser = nextAssignUser.substring(0, nextAssignUser.length()-1);
			//user = user.substring(0, user.length()-1);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void gridUser_ondblclick() {
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Bind(id="qryCountry")
    public List<SelectItem> getqryCountry() {
    	try {
    		return CommonComBoxBean.getComboxItems("country","country","sys_corporation_agent AS d"
    				,"WHERE isdelete = false and status <> '' and country is not null group by country","ORDER BY convert_to(country,'GBK')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
}
