package com.scp.view.module.finance.initconfig;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.finance.fs.FsAcctrp;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.arapsubjectBean", scope = ManagedBeanScope.REQUEST)
public class ArapSubjectBean extends GridFormView {

	
	@SaveState
	@Accessible
	public FsAcctrp selectedRowData = new FsAcctrp();
	
	@Override
	public void add() {
		super.add();
	}
	@Override
	public void del() {
		super.del();
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0){
			MessageUtils.alert("请至少选择一条记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.acctRpMgrService.removeDate(Long.parseLong(id));
			}
			MessageUtils.alert("OK!");
			this.refresh();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.acctRpMgrService.fsAcctrpDao.findById(this.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.acctRpMgrService.saveData(selectedRowData);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid() + "";
		m.put("qry", qry);
		return m;
	}
	
}