package com.scp.view.module.oa.staffmgr;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.cache.CommonDBCache;
import com.scp.model.oa.OaUserinfo;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.staffmgr.staffaddeditBean", scope = ManagedBeanScope.REQUEST)
public class StaffAddEditBean extends MastDtlView {
	@Bind
	public UIWindow showMessageWindow;

	@Bind
	@Accessible
	public String fnos;

	@Bind
	public Long sale;

	@SaveState
	public Boolean isPostBack = false;

	@Bind
	@SaveState
	@Accessible
	public OaUserinfo selectedRowData = new OaUserinfo();

	@Override
	public void refreshForm() {

	}

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		} else {

		}
	}

	@Override
	public void init() {
		selectedRowData = new OaUserinfo();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else {
			addMaster();
		}
	}

	@Override
	@Action
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.oaUserInfoService().oaUserinfoDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
//	    this.tabLayout.setActiveTab(0);
//		showTransfer();
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.oaUserInfoService().saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		refreshMasterForm();
		this.alert("ok");
//		this.tabLayout.setActiveTab(0);
//		this.addMaster();
	}
	
	@Action
	public void changeuser(){
		String userid = AppUtils.getReqParam("userid");
		 String msg = this.getNames(userid);
		  String[] newmsg = msg.split(",");
		 this.selectedRowData.setNamee(newmsg[0]);
		 this.selectedRowData.setNamec(newmsg[1]);
		 this.update.markUpdate(true, UpdateLevel.Data, "namee");
	}
	
	public String getNames(String id){
		String sql = "SELECT namee,namec FROM sys_user WHERE isdelete = FALSE AND id = "+ id;
		   Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		     String namee =(String)map.get("namee");
		     String namec = (String)map.get("namec");
		     String msg = namee + "," + namec;
		     return msg;
	}

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	// @Bind
	// public UIButton createPod;

	@Bind
	public UIButton addJobsLeaf;

	// private void disableAllButton(Boolean flag) {
	// saveMaster.setDisabled(flag);
	// delMaster.setDisabled(flag);
	// // createPod.setDisabled(flag);
	// addJobsLeaf.setDisabled(flag);
	// }

	@Override
	public void addMaster() {
		this.selectedRowData = new OaUserinfo();
		this.mPkVal = -1l;
		this.selectedRowData.setDatein(new Date());
//		showTransfer();
//		this.tabLayout.setActiveTab(0);
	}

	@Override
	public void delMaster() {
		try {
			// serviceContext.jobsMgrService.removeDate(this.mPkVal, AppUtils
			// .getUserSession().getUsercode());
			this.serviceContext.oaUserInfoService().removeDate(this.mPkVal);
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

	@Bind
	public String popQryKey;
	
	@Bind
	public Long company;
	
	@Action
    private void changeDept() {
		String company = AppUtils.getReqParam("company");
        if(company !=null) {
        	this.company = Long.parseLong(company);
//        	departments = queryDepts(this.company);
//        	this.department = -1l;
          this.update.markUpdate(UpdateLevel.Data, "department");
        }
    }
	
	@Resource
	public CommonDBCache commonDBCache;
	
	@Bind(id="deptids")
	@SelectItems
	private List<SelectItem> getqueryDepartid(){
		try {
			return commonDBCache.getComboxItems("d.id","d.code ||'/'|| COALESCE(d.name,'')","sys_department AS d","WHERE corpid=" + company,"");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

}
