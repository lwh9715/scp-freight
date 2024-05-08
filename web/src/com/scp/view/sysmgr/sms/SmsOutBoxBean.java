package com.scp.view.sysmgr.sms;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysSms;
import com.scp.service.sysmgr.SysSmsService;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.sms.smsoutboxBean", scope = ManagedBeanScope.REQUEST)
public class SmsOutBoxBean extends GridFormView {
	
	@ManagedProperty("#{sysSmsService}")
	public SysSmsService sysSmsService;
	
	@Bind
	public String content;
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		queryMap.put("inputer", AppUtils.getUserSession().getUsercode());
		Map map = super.getQryClauseWhere(queryMap);
		map.get("qry");
		return map;
	}
	

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		SysSms sms = sysSmsService.sysSmsDao.findById(this.pkVal);
		this.content = sms.getContent();
		this.update.markUpdate(UpdateLevel.Data, "content");
	}



	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}
}
