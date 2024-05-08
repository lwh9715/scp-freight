package com.scp.view.sysmgr.addresslist;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysTemplet;
import com.scp.service.sysmgr.TemplateMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.addresslist.templateBean", scope = ManagedBeanScope.REQUEST)
public class TemplateBean extends GridFormView {
	
	@ManagedProperty("#{templateMgrService}")
	public TemplateMgrService templateMgrService;
	
	@SaveState
	@Accessible
	public SysTemplet data;
	
	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		data = templateMgrService.sysTempletDao.findById(Long.valueOf(id));
		super.grid_ondblclick();
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		qryMap.put("userid$", AppUtils.getUserSession().getUserid());
		Map map = super.getQryClauseWhere(queryMap);
		map.get("qry");
		return map;
	}
	
	
	@Override
	public void add() {
		super.add();
		data = new SysTemplet();
	}



	@Override
	public void save() {
		data.setUserid( AppUtils.getUserSession().getUserid());
		templateMgrService.saveData(data);
		this.alert("OK");
		this.refresh();
	}
	
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		templateMgrService.delDatas(id);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
}
