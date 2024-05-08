package com.scp.view.module.ship;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipjoin;
import com.scp.model.ship.BusShipjoinlink;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.shipjoinlinkBean", scope = ManagedBeanScope.REQUEST)
public class ShipJoinlinkBean extends GridFormView {

	@SaveState
	@Accessible
	public BusShipjoinlink selectedRowData = new BusShipjoinlink();
	
	@SaveState
	@Accessible
	public BusShipjoin busShipjoin = new BusShipjoin();
	
	
	@SaveState
	@Accessible
	public Long unionid;
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String contentType;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			unionid=Long.valueOf(id);
			qryMap.put("shipjoin$", this.unionid);
		}
	}
	
	
	
	@Action
	public void cancel() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.busShipjoinlinkMgrService.cancel(unionid,ids,AppUtils.getUserSession().getUsercode(),daoIbatisTemplate);
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	/**
	 * 甩柜对应的动作 1.移除改单子 2.自动完成环节,并单确认,并且把var_isOnBoad==1
	 * 传过来的是containerid 而不是Jobid ,需要自己去找jobid
	 * 现在甩柜是以工作为单位进行,不是以柜子为单位(后续需要的话再改)
	 * 现在一次处理一行甩柜
	 */
	@Action
	public void delcnt(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要甩柜的行");
			return;
		}
		BusShipContainer busShipContainer = this.serviceContext.busShipContainerMgrService.busShipContainerDao.findById(Long.valueOf(ids[0]));
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(busShipContainer.getJobid(),WorkFlowEnumerateShip.UNION_CONFIRM,"id");
		Map<String,Object> m =new HashMap<String,Object>();
		m.put("var_isOnBoad",1);// 设置为未上船
//		try {
//			WorkFlowUtil.passProcessBatch(workids,m);
//			//移除
//			serviceContext.busShipjoinlinkMgrService.cancel(ids);
//			MessageUtils.alert("cancel success ,Pass OK");
//			refresh();
//		}catch(CommonRuntimeException e){
//			MessageUtils.alert(e.getLocalizedMessage());
//		}
//		catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		}
	}
	
	
//	@Action
//	public void shipChoose() {
//		String url = AppUtil.getContextPath() + "/pages/module/ship/shipjoinlinkchoose.xhtml?shipjoinid="+this.unionid;
//		dtlIFrame.setSrc(url);
//		update.markAttributeUpdate(dtlIFrame, "src");
//		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
//		dtlDialog.show();
//	}
	
	

	@Action
	public void shipChoose() {
		String winId = "_choose_shipjoinlink";
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipjoinlinkchoose.xhtml?shipjoinid="+this.unionid;
		AppUtils.openWindow(winId, url);
	}

	@Bind
	private UIWindow dtlDialog;
	

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	/**
	 * 发送邮件
	 * neo 2014-04-11
	 */
	@Action
	public void sendMail() {
		if(this.unionid==-1L||this.unionid==null){
			MessageUtils.alert("请先保存并单");
			return;
		}
		confirmSendJoinEmail();
		String url = AppUtils.getContextPath() + "/pages/sysmgr/mail/emailsendedit.xhtml?type=shipunion&id="+this.unionid;
		AppUtils.openWindow("_sendMail_shipunion", url);
		//自动批量完成流程  已发送并单邮件
		//再同步一下所有子单委托中的etd atd等信息
		busShipjoin = serviceContext.busShipjoinMgrService.busShipjoinDao.findById(unionid);
		//String sql = "UPDATE bus_shipping SET etd = '"+busShipjoin.getEtd()+"' ,eta='"+busShipjoin.getEta()+"' ,atd = '"+busShipjoin.getAtd()+"' ,ata='"+busShipjoin.getAta()+"' WHERE id iN(SELECT l.shipid FROM bus_shipjoinlink l WHERE l.shipjoin ="+unionid+"  );";
		String sql = "SELECT f_bus_shipping_sameasjoin('unionid="+this.unionid+"');";
		serviceContext.busShipjoinMgrService.busShipjoinDao.executeQuery(sql);
	}
	
	
	/**
	 * 确认发送并单邮件  批量通过该流程
	 */
	public void confirmSendJoinEmail(){
//		String[] ids = WorkFlowUtil.getWorkitemIds(unionid,WorkFlowEnumerateShip.UNION_EMAIL,"id",AppUtils.getUserSession().getUsercode());
//		try {
//			WorkFlowUtil.passProcessBatch(ids);
//			MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//		}catch(CommonRuntimeException e){
//			MessageUtils.alert(e.getLocalizedMessage());
//		}
//		catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		}
	}

	/**
	 * 预览到港通知书
	 */
	@Action
	public void showArrivalNotice() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请选择单行记录！");
			return;
		}
		Long id = Long.parseLong(ids[0]);
		BusShipContainer bsc = this.serviceContext.busShipContainerMgrService.busShipContainerDao.findById(id);
		String jobid = "";
		
		if(bsc != null) {
			importPodNote(bsc.getJobid());
			jobid = bsc.getJobid() + "";
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/ship/arrival_notice.raq&jobid=" + jobid;
		AppUtils.openWindow("_ship_bookink", openUrl);
	}
	
	/**
	 * 确认发送到港通知书    批量通过该流程
	 */
	public void importPodNote(Long id){
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobidAndUser(id,WorkFlowEnumerateShip.ARRIVAL_PORT_NOTICE,"id",AppUtils.getUserSession().getUsercode());
//		try {
//			WorkFlowUtil.passProcessBatch(workids);
//			MessageUtils.alert("OK!已完成，自动流转到下一个环节");
//		}catch(CommonRuntimeException e){
//			MessageUtils.alert(e.getLocalizedMessage());
//		}
//		catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		}
	}
	
	@Bind
	@SaveState
	public String manifestmsgContent;
	
	@Bind
	public UIWindow showManifestWindow;
	
	@Bind
	@SaveState
	public Long containerid;
		
	@Action
	public void ediManifest() {
		String[] ids = this.grid.getSelectedIds();
		StringBuilder str = new StringBuilder();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择单行记录！");
			return;
		} else {
			for (int i = 0; i < ids.length; i++) {
				if (i == ids.length - 1) {
					str.append(ids[i]);
				} else {
					str.append(ids[i] + ",");
				}
			}
			Long id = Long.parseLong(ids[0]);
			containerid = id; // 柜号id赋值，用于后面给下载文件命名
			String sql;
			try {
				sql = "SELECT f_edi_manifest('type=busjobid;id=" + str
						+ "') AS edi;";
				Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				this.manifestmsgContent = m.get("edi").toString();
				this.update.markUpdate(UpdateLevel.Data, "manifestmsgContent");
				showManifestWindow.show();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
			
		}
	}
	@Action
	public void outputMANIFEST(){
		this.fileName = "MFJ_" + containerid + ".txt";
		this.contentType = "text/plain";
	}
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() throws Exception {
		InputStream input = new ByteArrayInputStream(this.manifestmsgContent.getBytes());
		return input;
    }
	
}
