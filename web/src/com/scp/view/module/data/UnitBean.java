package com.scp.view.module.data;

import com.scp.model.data.DatUnit;
import com.scp.service.data.UnitMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;

import java.util.Map;

@ManagedBean(name = "pages.module.data.unitBean", scope = ManagedBeanScope.REQUEST)
public class UnitBean extends GridFormView {
	

	@ManagedProperty("#{unitMgrService}")
	public UnitMgrService unitMgrService;
	
	@SaveState
	@Accessible
	public DatUnit selectedRowData = new DatUnit();

	@Override
	public void add() {
		selectedRowData = new DatUnit();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = unitMgrService.datUnitDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		unitMgrService.saveData(selectedRowData);
		this.alert("OK");
		refresh();
		
		
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			unitMgrService.datUnitDao.remove(unitMgrService.datUnitDao.findById(getGridSelectId()));
			refresh();
		}
	}
	
	@Override
	public void del() {
		unitMgrService.datUnitDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		refresh();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		map.put("filter", filter);
		return map;
	}
}
