package com.scp.view.module.finance.clspro;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsConfigShare;
import com.scp.model.finance.fs.FsXrate;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.clspro.shareprofitBean", scope = ManagedBeanScope.REQUEST)
public class ShareProfitBean extends GridFormView {

	@SaveState
	@Accessible
	public String periodid;

	@Bind
	@SaveState
	public FsXrate ddata = new FsXrate();

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String period;
	
	@SaveState
	@Accessible
	public FsConfigShare selectedRowData = new FsConfigShare();
	

	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");

			
		}
	}

	

	
	public void save() {
//		String goodscode = "";
//		try {
//			serviceContext.actrateMgrService.saveDtlDatas(modifiedData);
//			MsgUtil.alert("OK");
//			refresh();
//		} catch (CommonRuntimeException e) {
//			MsgUtil.alert(e.getLocalizedMessage());
//			return;
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//		}
	}

	
	// @Override
	// public Map getQryClauseWhere(Map<String, Object> queryMap) {
	// Map m = super.getQryClauseWhere(queryMap);
	// String qry = StrTools.getMapVal(m, "qry");
	// qry += "\nAND periodid = " + periodid;
	// m.put("qry", qry);
	// return m;
	// }

	
	
	@Override
	public void grid_ondblclick() {
		String url = "./shareprofitdtl.xhtml?id="+this.getGridSelectId();
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	
	@Action
	public void add() {
		String winId = "_shareprofitdtl";
		String url = "./shareprofitdtl.xhtml";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
		
	}
	
	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}


	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.fsConfigShareMgrService.fsConfigShareDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.fsConfigShareMgrService.saveData(selectedRowData);
		
		this.alert("OK");
	}

}
