package com.scp.view.module.data;

import com.scp.model.data.DatGoodstype;
import com.scp.service.data.GoodstypeMgrService;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;

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