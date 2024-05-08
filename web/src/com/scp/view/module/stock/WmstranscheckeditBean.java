package com.scp.view.module.stock;

import java.math.BigDecimal;
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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.wms.WmsTrans;
import com.scp.model.wms.WmsTransdtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.stock.wmstranscheckeditBean", scope = ManagedBeanScope.REQUEST)
public class WmstranscheckeditBean extends MastDtlView{
	
	@Bind
	public UIButton showStocks;

	@Override
	public void doServiceSaveMaster() {
		serviceContext.wmsTransdtlMgrService.saveMasterData(selectedRowData);
		this.mPkVal = selectedRowData.getId();
		this.refreshMasterForm();
		this.alert("ok");
		
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.wmsTransMgrService.wmsTransDao.findById(this.mPkVal);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		this.ddata = serviceContext.wmsTransdtlMgrService.wmsTransdtlDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		if (this.mPkVal == -1l)
			return;
		serviceContext.wmsTransdtlMgrService.saveDtlData(ddata);
		this.alert("OK");
	}

	

	@Bind
	public UIIFrame attachmentIframe;

	@SaveState
	@Accessible
	public WmsTrans selectedRowData = new WmsTrans();
	
	@SaveState
	@Accessible
	public String sql="";
	
	@SaveState
	@Accessible
	public WmsTransdtl ddata = new WmsTransdtl();
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}
	
	@Bind
	@SaveState
	public Integer stocknum;

	public void getStock() {
		Long customerid = this.selectedRowData.getWarehouseid_out();
		String goodscode = this.ddata.getGoodscode();
		String goodsnamec = this.ddata.getGoodsnamec();
		String goodssize = this.ddata.getGoodssize();
		String markno = this.ddata.getMarkno();
		String goodscolor = this.ddata.getGoodscolor();
		String sql = ""
				+ "\nSELECT "
				+ "\nsum(piece) AS piece "
				+ "\nFROM _wms_stock "
				+ "\nWHERE  customerid  = "
				+ customerid
				+ " AND goodscode = '"
				+ goodscode
				+ "'"
				+ " AND goodsnamec = '"
				+ goodsnamec
				+ "'"
				+ " AND goodssize = '"
				+ goodssize
				+ "'"
				+ " AND markno = '"
				+ markno
				+ "'"
				+ " AND goodscolor = '"
				+ goodscolor
				+ "'"
				+ "\nGROUP BY  goodscode , customerid , goodsnamec,markno,goodssize,goodscolor"
				+ ";";
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			stocknum = ((BigDecimal) m.get("piece")).intValue(); //

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void refreshForm() {
		getStock();
		super.refreshForm();
		this.update.markUpdate(true, UpdateLevel.Data, "stocknum");
	}
	
	@Action
	public void showAttachmentIframe() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
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
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND transid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void init() {
		selectedRowData = new WmsTrans();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
		} else {
			addMaster();
		}
	}
	
	
	@Bind
	public UIWindow editWindow;

	public void closeEidtWindow() {
		editWindow.close();
	}

	@Action(id = "editWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();
	}
	

}
