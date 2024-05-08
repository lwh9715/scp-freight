package com.scp.view.sysmgr.memo;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysMemo;
import com.scp.service.sysmgr.SysMemoService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.memo.memoBean", scope = ManagedBeanScope.REQUEST)
public class MemoBean extends GridFormView {
	
	@ManagedProperty("#{sysMemoService}")
	public SysMemoService sysMemoService;
	
	@SaveState
	@Accessible
	public SysMemo selectedRowData = new SysMemo(); 
	

	@Override
	public void add() {
		selectedRowData = new SysMemo();
		selectedRowData.setRemindertimefm(new Date());
		selectedRowData.setRemindertimeend(new Date());
		selectedRowData.setGrade("*");
		selectedRowData.setIsvalid(true);
		selectedRowData.setIsdelete(false);
		selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
		selectedRowData.setInputtime(new Date());
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = sysMemoService.sysMemoDao.findById(this.pkVal);
	}


	@Override
	public void doServiceSave() {
		sysMemoService.saveData(selectedRowData);
	}
	
	@Action
	public void del() {
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		sysMemoService.removeDate(id);
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap){
		Map map = super.getQryClauseWhere(queryMap);
		map.put("user", AppUtils.getUserSession().getUsercode());
		return map;
	}
	
}
