package com.scp.view.module.oa.staffmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.oa.OaUserinfo;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.staffmgr.staffmsgeditBean", scope = ManagedBeanScope.REQUEST)
public class StaffMsgDetailBean extends MastDtlView {
	@Bind
	public UIWindow showMessageWindow;

	@Bind
	public UIIFrame transferIframe;

	@Bind
	public UIIFrame contractIframe;

	@Bind
	public UIIFrame protocolIframe;

	@Bind
	public UIIFrame visaIframe;
	
	@Bind
	public UIIFrame userlogIframe;
	
	@Bind
	public UITabLayout tabLayout;
	
	@Bind
	public UIIFrame userevalIframe;
	
	@Bind
	public UIIFrame userchangeIframe;
	
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

	@Action
	public void showTransfer() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			transferIframe.load(blankUrl);
		} else {
			// if(!showAttachmentIframe)
			transferIframe.load("transfer.xhtml?id="
					+ this.mPkVal);
			// showAttachmentIframe = true;
		}
	}

	@Action
	public void showContract() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			contractIframe.load(blankUrl);
		} else {
			contractIframe.load("contract.xhtml?id=" + this.mPkVal);
			// showArapEdit = true;
		}
	}

	@Action
	public void showProtocol() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			protocolIframe.load(blankUrl);
		} else {
			// if(!showReceiptEdit)
			protocolIframe.load("protocol.xhtml?id=" + this.mPkVal);
			// showReceiptEdit = true;
		}
	}

	@Action
	public void shwoVisa() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			visaIframe.load(blankUrl);
		} else {
			visaIframe.load("visa.xhtml?id=" + this.mPkVal);
			// showReceiptEdit = true;
		}
	}
	
	@Action
	public void shwoUserlog() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			userlogIframe.load(blankUrl);
		} else {
			userlogIframe.load("userlog.xhtml?id=" + this.mPkVal);
			// showReceiptEdit = true;
		}
	}
	
	@Action
	public void shwoUserEval() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			userevalIframe.load(blankUrl);
		} else {
			userevalIframe.load("userlogev.xhtml?id=" + this.mPkVal);
			// showReceiptEdit = true;
		}
	}
	
	@Action
	public void shwoLogChange() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			userchangeIframe.load(blankUrl);
		} else {
			userchangeIframe.load("userlogchange.xhtml?id=" + this.mPkVal);
			// showReceiptEdit = true;
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
		//this.tabLayout.setActiveTab(0);
		showTransfer();
	}

	@Override
	public void doServiceSaveMaster() {
		serviceContext.oaUserInfoService().saveData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		refreshMasterForm();
		this.alert("ok");
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

	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;

	// @Bind
	// public UIButton createPod;

	@Bind
	public UIButton addJobsLeaf;

//	private void disableAllButton(Boolean flag) {
//		saveMaster.setDisabled(flag);
//		delMaster.setDisabled(flag);
//		// createPod.setDisabled(flag);
//		addJobsLeaf.setDisabled(flag);
//	}
	
	public String getNames(String id){
		String sql = "SELECT namee,namec FROM sys_user WHERE isdelete = FALSE AND id = "+ id;
		   Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		     String namee =(String)map.get("namee");
		     String namec = (String)map.get("namec");
		     String msg = namee + "," + namec;
		     return msg;
	}

	@Override
	public void addMaster() {
		this.selectedRowData = new OaUserinfo();
	}

	@Override
	public void delMaster() {
		try {
			// serviceContext.jobsMgrService.removeDate(this.mPkVal, AppUtils
			// .getUserSession().getUsercode());
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

}
