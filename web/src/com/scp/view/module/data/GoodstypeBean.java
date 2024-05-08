package com.scp.view.module.data;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.data.DatGoodstype;
import com.scp.service.data.GoodstypeMgrService;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.goodstypeBean", scope = ManagedBeanScope.REQUEST)
public class GoodstypeBean extends GridFormView {

	@ManagedProperty("#{goodstypeMgrService}")
	public GoodstypeMgrService goodstypeMgrService;

	@SaveState
	@Accessible
	public DatGoodstype selectedRowData = new DatGoodstype();

	@Override
	public void add() {

		selectedRowData = new DatGoodstype();
		selectedRowData.setIsleaf("N");
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = goodstypeMgrService.datGoodstypeDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		goodstypeMgrService.saveData(selectedRowData);
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
			goodstypeMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}

	@Override
	public void del() {
		goodstypeMgrService.datGoodstypeDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		refresh();
	}

}