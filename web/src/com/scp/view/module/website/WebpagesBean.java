package com.scp.view.module.website;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.website.WebPages;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.website.webpagesBean", scope = ManagedBeanScope.REQUEST)
public class WebpagesBean extends GridFormView{
	
	@Bind(id = "ckeditor")
	@SaveState
	public String ckeditor;
	
	
	@Bind
	@SaveState
	public String ckeditorStr;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		return map;
	}
	
	
	@SaveState
	public WebPages data = new WebPages();
	
	@Override
	public void add() {
		super.add();
		data = new WebPages();
		data.setId(0L);
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.webPagesService.removeDate(Long.parseLong(id));
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.webPagesService.webPagesDao.findById(this.pkVal);
		ckeditor = data.getContext();
		ckeditorStr = ckeditor;
		//update.markUpdate(UpdateLevel.Data, "ckeditor");
		Browser.execClientScript("setValue()");
		
	}

	@Override
	protected void doServiceSave() {
		serviceContext.webPagesService.saveData(data);
	}
	
	@Action
	public void saveAction() {
		this.data.setContext(AppUtils.getReqParam("editor1"));
		serviceContext.webPagesService.saveData(data);
		this.pkVal = data.getId();
		super.save();
		MessageUtils.alert("OK!");
	}
}
