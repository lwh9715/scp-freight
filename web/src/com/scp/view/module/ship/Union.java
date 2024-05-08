package com.scp.view.module.ship;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.unionBean", scope = ManagedBeanScope.REQUEST)
public class Union extends GridView {
	
	
	

	@Action
	public void add() {
		String winId = "_edit_union";
		String url = "./unionedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_union";
		String url = "./unionedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void impTemplet() {
//		templetFrame.load("../other/feetemplatechooser.xhtml?jobid="+this.jobid);
//		showTempletWindow.show();
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length >1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		//impFee(Long.valueOf(ids[0]));
		String winId = "_arapTemplet";
		String url = "../other/feetemplatechooser.xhtml?type=join&joinids="+StrUtils.array2List(ids); 
//		int width = 980;
//		int height = 600;
		AppUtils.openWindow(winId, url);
	}
	
	/**
	 * 审核
	 */
	@Action
	public void checkUnion() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			this.serviceContext.busShipjoinMgrService.updateCheck(ids);
			MessageUtils.alert("审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelCheckUnion() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
		try {
			this.serviceContext.busShipjoinMgrService.updateCancelCheck(ids);
			MessageUtils.alert("取消审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 批量通过补导入迪拜固定费用
	 */
	
	public void impFee(Long id){
//		String[] ids = WorkFlowUtil.getWorkitemIds(id,WorkFlowEnumerateShip.IMPORTFEE,"id",AppUtils.getUserSession().getUsercode());
//		try {
//			WorkFlowUtil.passProcessBatch(ids);
//			MessageUtils.alert("FF Pass OK");
//		}catch(CommonRuntimeException e){
//			MessageUtils.alert(e.getLocalizedMessage());
//		}catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		}
	}
}
