package com.scp.view.module.bus;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bus.BusGoods;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.bus.busgoodsBean", scope = ManagedBeanScope.REQUEST)
public class BusGoodsBean extends GridFormView {
	
	@SaveState
	@Accessible
	public BusGoods selectedRowData = new BusGoods();
	
	@Bind
	@SaveState
	@Accessible
	public Long linkid;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("linkid");
			linkid=Long.valueOf(id);
			update.markUpdate(true, UpdateLevel.Data, "linkid");
			
		}
	}
	
	@Override
	public void add(){
		selectedRowData = new BusGoods();
		selectedRowData.setCurrency("USD");
		selectedRowData.setPackid(getPackid("CTNS"));
		selectedRowData.setLinkid(this.linkid);
		super.add();
	}
	
	
	private Long getPackid(String code) {
		try {
			String sql = "SELECT id FROM dat_package WHERE code = '" + code
					+ "' AND isdelete  = FALSE ";
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			Long id = (Long) m.get("id");
			return id;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}

	

	@Override
	public void del() {
		super.del();
		String[] ids = this.grid.getSelectedIds();
		
		if(ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.busGoodsMgrService.delBatch(ids, AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.busGoodsMgrService.busGoodsDao.findById(this.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.busGoodsMgrService.saveData(selectedRowData);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_busgoods";
				String args = -100 + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND linkid = " + this.linkid;
		m.put("qry", qry);
		return m;
	}
	
	
	
	
	/**
	 * 保存新增
	 */
	@Action
	protected void saveandadd() {
		try {
			this.serviceContext.busGoodsMgrService.saveData(selectedRowData);
			refresh();
			this.add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 保存关闭
	 */
	@Action
	protected void saveandclose() {
		try {
			this.serviceContext.busGoodsMgrService.saveData(selectedRowData);
			refresh();
			this.editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 复制新增
	 */
	@Action
	public void copyadd(){
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipbusgoodschoose.xhtml?linkid="+linkid;
		dtlIFramegoods.setSrc(url);
		update.markAttributeUpdate(dtlIFramegoods, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialoggoods);
		dtlDialoggoods.show();
	}
	
	@Bind
	private UIWindow dtlDialoggoods;
	@Bind
	private UIIFrame dtlIFramegoods;

	@Action(id = "dtlDialoggoods", event = "onclose")
	private void dtlDialoggoodsClose() {
		refresh();
	}
	
	@Bind
	public Boolean isgrswgt;
	@Bind
	public Boolean isnetwgt;
	@Bind
	public Boolean isgdsvalue;
	@Bind
	public Boolean iscbm;
	@Bind
	public Boolean isgdsprice;
	
	@Bind
	public Double random;
	
	private String getArgs() {
		String args = "";
		args += "isgrswgt="+(isgrswgt?"Y":"N")+";";
		args += "isnetwgt="+(isnetwgt?"Y":"N")+";";
		args += "isgdsvalue="+(isgdsvalue?"Y":"N")+";";
		args += "iscbm="+(iscbm?"Y":"N")+";";
		args += "isgdsprice="+(isgdsprice?"Y":"N")+";";
		args += "random="+random+";";
		
		return args;
	}
	
	@Action
	public void sharegoods(){
		String sql = "SELECT f_busgoods_share('linkid="+linkid+";"+ getArgs() + "');";
		try {
			this.serviceContext.busGoodsMgrService.busGoodsDao
					.executeQuery(sql);
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
