package com.scp.view.module.salesmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.salesmgr.BlackList;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.salesmgr.blacklistBean", scope = ManagedBeanScope.REQUEST)
public class BlackListBean  extends GridFormView{
	
	@SaveState
	@Accessible
	public BlackList selectedRowData = new BlackList();
	
	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
	@Bind
	public UIWindow importDataWindow;
	
	@Bind
	@SaveState
	public String start;
	
	@Bind
	@SaveState
	public Long mPkVal = -1L;
	
	@Bind
	public UIIFrame blacklistIframe;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	
	
	@Action
	protected void startImport() {
		importDataBatch();
	}
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			String args = "'"+AppUtils.getUserSession().getUsercode()+"'";
			try {
				String callFunction = "f_imp_sys_corporation_black";
				
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				
				
				MessageUtils.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	@Action
	public void add() {
		blacklistIframe.load("./blacklistedit.xhtml");
		editWindow.show();
	}
	
	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		try {
			for(String id : ids){
				serviceContext.blackListService.delDate(Long.parseLong(id));
			}
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void startTrue(){
		String[] ids = this.grid.getSelectedIds();
		try {
			for(String id : ids){
				serviceContext.blackListService.startTrue(Long.parseLong(id));
			}
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void startFalse(){
		String[] ids = this.grid.getSelectedIds();
		try {
			for(String id : ids){
				serviceContext.blackListService.startFalse(Long.parseLong(id));
			}
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/*@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_blacklist1";
		String url = "./blacklistedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
		
	}*/
	
	@Action
	public void grid_ondblclick() {
		try {
				blacklistIframe.load("./blacklistedit.xhtml?id="+this.getGridSelectId());
				editWindow.show();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, Object> map = super.getQryClauseWhere(queryMap);
		String qry = (String)map.get("qry");
		map.remove("qry");
		qry = qry.replace("AND isstart ILIKE '%'||TRIM('true')||'%'","AND isstart = true");
		qry = qry.replace("AND isstart ILIKE '%'||TRIM('false')||'%'","AND isstart = false");
		//System.out.println("qry--->"+qry);
		map.put("qry",qry);
		return map;
	}



	@Override
	protected void doServiceFindData() {
		
	}



	@Override
	protected void doServiceSave() {
		
	}
	
	
}
