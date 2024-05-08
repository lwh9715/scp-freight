package com.scp.view.module.ship;

import java.util.Date;
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

import com.scp.model.bus.BusDocexp;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.ship.senddoceditBean", scope = ManagedBeanScope.REQUEST)
public class SendDocEditBean extends MastDtlView {
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@Bind
	public UIIFrame docsend;
	
	@Bind
	public UIIFrame arapIframe;
	
	
	@Bind
	public UIIFrame msgIframe;
	
	@Bind
	public UIIFrame traceIframe;
	
	@Bind
	public UIIFrame attachmentIframe;
	
	

	@SaveState
	@Accessible
	public BusDocexp selectedRowData = new BusDocexp();

	@Override
	public void refreshForm() {

	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			
		}
	}
	
	@Action
	public void showDocsendEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			docsend.load(blankUrl);
		}else{
			docsend.load("../ship/senddocchoose.xhtml?sendid=" + this.mPkVal);
		}
	}


	@Action
	public void showArapEdit() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			arapIframe.load(blankUrl);
		}else{
			arapIframe.load("../finance/arapedit.xhtml?jobid=" + selectedRowData.getJobid());
		}
	}
	
	
	@Action
	public void showAttachmentIframe() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			attachmentIframe.load(blankUrl);
		} else {
			attachmentIframe.load(AppUtils.getContextPath() + "/pages/module/common/attachment.xhtml?linkid=" + this.mPkVal);
		}
	}
	
	
	
	@Action
	public void showMsg() {
		if (this.mPkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
			msgIframe.load(blankUrl);
		}else{
			msgIframe.load("../ship/shipmsgboard.xhtml?jobid=" + this.mPkVal);
		}
	}
	
	
	
	
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
		selectedRowData = new BusDocexp();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();

		} else {
			selectedRowData.setSendate(new Date());
			SysUser sysuser = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
			if(sysuser != null) {
				selectedRowData.setSender(sysuser.getCode());
			}
			
		}

	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.busDocexpMgrService.busDocexpDao.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showDocsendEdit();
	}

	@Override
	public void doServiceSaveMaster() {
		try {
			serviceContext.busDocexpMgrService.saveData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			this.alert("ok");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
		this.selectedRowData = new BusDocexp();
		this.mPkVal = -1l;
		selectedRowData.setSendate(new Date());
		showDocsendEdit();
		showArapEdit();

	}

	@Override
	public void delMaster() {
		serviceContext.busDocexpMgrService.removeDate(this.mPkVal);
			this.addMaster();
			this.alert("OK");
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

	
	

	
//	/**
//	 * 合肥审核确认寄单
//	 * 如果某个工作单存在CO候补,需要设置参数
//	 */
//	@Action
//	public  void confirmHF() {
//		List<Map> workids = WorkFlowUtil.getWorkitemIdsBySendid(this.mPkVal, WorkFlowEnumerateShip.CHECK_EXPRESS_HF, "id",AppUtils.getUserSession().getUsercode());
//		
//		IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//		if(workids.size()==0){
//			MessageUtils.alert("当前没有可以活动的节点");
//			return;
//		}
//		String[] workitems = new String[1];
//		Map var_is = new HashMap<String, Object>();
//		String workItemid = "";
//		Object co = "";
//		for(int i = 0;i<workids.size();i++){
//			Map workdtil = workids.get(i);
//			workItemid = (String) workdtil.get("workitemid");
//			co = workdtil.get("co");
////CO 候补从合肥都深圳都有,CO是深圳收到客人寄的以后才从深圳发出,所以这个环节不需要参数
////			if(co==null){
////				var_is.put("var_isHFDocAdd",0);
////			}else{
////				var_is.put("var_isHFDocAdd",1);
////			}
//			workitems[0]=workItemid;
//			try {
//				//WorkFlowUtil.passProcessBatch(workitems,var_is);
//				WorkFlowUtil.passProcessBatch(workitems,null);
//				
//			}catch(CommonRuntimeException e){
//				MessageUtils.alert(e.getLocalizedMessage());
//			}
//			catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			}finally{
//				//目前寄单一个单子被选择以后,后边这个工作单就选不到了,因此只能手动标记补寄,下面方法暂时不用
//				//confirmHFBJ();
//			}
//		}
//		MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//
//	}
	
	
	
	
	
	/**
	 * 深圳审核确认寄单
	 * 如果某个工作单存在CO候补,需要设置参数
	 */
//	@Action
//	public  void confirmSZ() {
//		List<Map> workids = WorkFlowUtil.getWorkitemIdsBySendid(this.mPkVal, WorkFlowEnumerateShip.CHECK_EXPRESS_SZ, "id");
//		IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//		if(workids.size()==0){
//			MessageUtils.alert("当前没有可以活动的节点");
//			return;
//		}
//		String[] workitems = new String[1]; //待会方便调用
//		Map var_is = new HashMap<String, Object>();//流程参数列表
//		String workItemid = "";	
//		Object co = "";
//		
//		Map workdtil = new HashMap();  //遍历list获取map
//		for(int i = 0;i<workids.size();i++){
//			workdtil = workids.get(i);
//			workItemid = (String) workdtil.get("workitemid");
//			co = workdtil.get("co");
//			if(co==null){
//				var_is.put("var_isSZDocAdd",0); 
//			}else{
//				var_is.put("var_isSZDocAdd",1);//存在CO候补
//			}
//			workitems[0]=workItemid; //加入到map中
//			try {
//				WorkFlowUtil.passProcessBatch(workitems,var_is);
//			}catch(CommonRuntimeException e){
//				MessageUtils.alert(e.getLocalizedMessage());
//			}
//			catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg()); 
//				e.printStackTrace();
//			}
//		}
//		MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//
//	}
	
	/**
	 * 合肥审核确认寄单  完成补寄的流程
	 */
	
//	public  void confirmHFBJ() {
//		List<Map> workids = WorkFlowUtil.getWorkitemIdsBySendid(this.mPkVal, WorkFlowEnumerateShip.CHECK_EXPRESS_HF, "id");
//		IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//		if(workids.size()==0){
//			return;
//		}
//		String[] workitems = new String[1];
//		String workItemid = "";
//		for(int i = 0;i<workids.size();i++){
//			Map workdtil = workids.get(i);
//			workItemid = (String) workdtil.get("workitemid");
//			workitems[0]=workItemid;
//			try {
//				WorkFlowUtil.passProcessBatch(workitems,null);
//			}catch(CommonRuntimeException e){
//				//AppUtils.debug(e.getLocalizedMessage());
//				e.printStackTrace();
//			}
//			catch (EngineException e) {
//				//AppUtils.debug(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				//AppUtils.debug(e.getErrorMsg());
//				e.printStackTrace();
//			}
//		}
//	}
}
