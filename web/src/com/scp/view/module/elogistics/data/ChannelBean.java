package com.scp.view.module.elogistics.data;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.elogistics.data.Channel;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.elogistics.data.channelBean", scope = ManagedBeanScope.REQUEST)
public class ChannelBean extends GridFormView {
	
	@Bind
	private UIButton add;
	
	@SaveState
	@Accessible
	public Channel selectedRowData = new Channel();
	
	@Bind
	@SaveState
	public String qryChannel;
	
	@Bind
	@SaveState
	public String qryVolwgtcalc;
	
	@Override
	public void beforeRender(boolean isPostBack) {
	if (!isPostBack) {
			super.applyGridUserDef();

		}
	}
	
	@Override
	public void clearQryKey() {
		if(!StrUtils.isNull(qryChannel)) {
			qryChannel = "";
		}
		if(!StrUtils.isNull(qryVolwgtcalc)) {
			qryVolwgtcalc = "";
		}
		super.clearQryKey();
	}
	
	@Action
	public void btnDel(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		serviceContext.channelService.removeDate(ids, AppUtils.getUserSession().getUsercode());
		this.alert("OK!");
		this.refresh();
	}
	
	@Action
	public void btnAdd(){
		selectedRowData = new Channel();
		selectedRowData.setId(0L);
		this.add();
	}
	
	@Action
	public void btnRefresh() {
		if (grid != null) {
			gridLazyLoad = false;
			this.grid.reload();
		}
	}
	
	/*@Override
	public void refresh(){
		System.out.println("qryChannel--->"+qryChannel);
	}*/
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
 		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(qryChannel)){
			qry += "\n AND channel ILIKE '%"+qryChannel+"%'";
		}
		if(!StrUtils.isNull(qryVolwgtcalc)){
			qry += "\n AND volwgtcalc ILIKE '%"+qryVolwgtcalc+"%'";
		}
		m.put("qry", qry);
		return m;
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = this.serviceContext.channelService.channelDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.channelService.saveData(selectedRowData);
	}
	
}

