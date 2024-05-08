package com.scp.view.sysmgr.addresslist;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysAddresslist;
import com.scp.service.sysmgr.AddressListMgrMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.addresslist.usraddressBean", scope = ManagedBeanScope.REQUEST)
public class UsrAddressBean extends GridFormView {
	
	@ManagedProperty("#{addressListMgrMgrService}")
	public AddressListMgrMgrService addressListMgrMgrService;
	
	@SaveState
	public SysAddresslist data;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String type = AppUtils.getReqParam("type");
			if("new".equalsIgnoreCase(type)){
				this.add();
				String email = AppUtils.getReqParam("email");
				data.setEmail1(email);
			}
		}   
	}
	
	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		data = addressListMgrMgrService.sysAddresslistDao.findById(Long.valueOf(id));
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
		data = new SysAddresslist();
	}



	@Override
	public void save() {
		data.setUserid( AppUtils.getUserSession().getUserid());
		addressListMgrMgrService.saveData(data);
		this.alert("OK");
		this.refresh();
	}
	
	
	@Bind
	public Long userid;
	

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		addressListMgrMgrService.delDatas(id);
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
