package com.scp.view.module.data;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.data.DatGoods;
import com.scp.model.sys.SysCorporation;
import com.scp.service.data.GoodsMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.goodsBean", scope = ManagedBeanScope.REQUEST)
public class GoodsBean extends GridFormView {

	@ManagedProperty("#{goodsMgrService}")
	public GoodsMgrService goodsMgrService;
	
	@SaveState
	@Accessible
	public DatGoods selectedRowData = new DatGoods();
	
	@Override
	public void add() {
		selectedRowData = new DatGoods();
		super.add();
	}
	
	@Bind
	public UIIFrame attachmentIframe;
	
	@Bind
	@SaveState
	public String customerabbr;

	@Override
	protected void doServiceFindData() {
		selectedRowData = goodsMgrService.datGoodsDao.findById(this.pkVal);
		if (this.pkVal > 0) {
			attachmentIframe.load(AppUtils.getContextPath()
					+ "/pages/module/common/attachment.xhtml?linkid=" + this.pkVal);
		}
		SysCorporation custom = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomerid());
		if(custom!=null){
			customerabbr = custom.getAbbr();
			Browser.execClientScript("$('#customer_input').val('"+customerabbr+"');;");
		}
	}


	@Override
	protected void doServiceSave() {
		goodsMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			goodsMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Override
	public void del() {
		goodsMgrService.datGoodsDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		refresh();
	}
	
}