package com.scp.view.module.cs;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.cs.csbookingeditBean", scope = ManagedBeanScope.REQUEST)
public class CsbookingEditBean extends MastDtlView {
	
	@SaveState
	@Accessible
	public CsBooking selectedRowData = new CsBooking();
	
	@SaveState
	public String jobnos = "";
	
	@SaveState
	public Long userid;
	
	@SaveState
	public String customeridname = "";
	
	@Bind
	public UIIFrame bookingFeeIframe;
	
	@Bind
	public UIIFrame bookingDtlIframe;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.userid = AppUtils.getUserSession().getUserid();
			init();
		}
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.csBookingMgrService.csBookingDao.modify(selectedRowData);
		MessageUtils.alert("OK");
	}

	@Override
	public void init() {
		selectedRowData = new CsBooking();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.csBookingMgrService.csBookingDao.findById(this.mPkVal);
		if(selectedRowData.getJobid()!=null&&selectedRowData.getJobid()>0){
			FinaJobs jobs = serviceContext.jobsMgrService.finaJobsDao.findById(selectedRowData.getJobid());
			if(jobs.getIsdelete()==false&&jobs!=null){
				this.jobnos = jobs.getNos();
			}
		}
		if(selectedRowData.getCustomerid()!=null&&selectedRowData.getCustomerid()>0){
			SysCorporation corporation = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			if(corporation!=null){
				this.customeridname = corporation.getAbbr();
			}
		}
		showArapEdit();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	@SaveState
	private boolean showArapEdit;
	
	@SaveState
	private boolean showbookingEdit;
	
	@Action
	public void showArapEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			bookingFeeIframe.load(blankUrl);
		} else {
			if(!showArapEdit){
				bookingFeeIframe.load("./csbookingfee.xhtml?bookingid="
						+ this.mPkVal);
			}
			showArapEdit = true;//如果已经显示了，再打开不重新加载
		}
	}
	
	@Action
	public void showbookingDtl() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			bookingDtlIframe.load(blankUrl);
		} else {
			if(!showbookingEdit){
			bookingDtlIframe.load("./csbookingdtl.xhtml?bookingid="
						+ this.mPkVal);
			}
			showbookingEdit = true;//如果已经显示了，再打开不重新加载
		}
	}
	
	
	@Action
	public void delBatch(){
		try {
			String sql= "UPDATE cs_booking SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = "+this.selectedRowData.getId(); 
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.alert("OK");
			refreshMasterForm();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void setReceiv() {
		try {
			String sql = "UPDATE cs_booking SET isreceived = true,receiveddate=now(),receivedder='"
						+AppUtils.getUserSession().getUsercode()+"' WHERE id = "+this.selectedRowData.getId(); 
			//System.out.println(sql);
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.alert("受理成功");
			refreshMasterForm();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void setNotReceived() {
		try {
			String sql= "UPDATE cs_booking SET isreceived = false,receiveddate=now(),receivedder='"
						+AppUtils.getUserSession().getUsercode()+"' WHERE id = "+this.selectedRowData.getId(); 
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			MessageUtils.alert("取消受理成功");
			refreshMasterForm();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	@Override
	public void refresh() {
		refreshMasterForm();
	}
	
}
