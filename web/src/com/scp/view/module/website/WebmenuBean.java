package com.scp.view.module.website;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.website.WebMenu;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.website.webmenuBean", scope = ManagedBeanScope.REQUEST)
public class WebmenuBean extends GridFormView{
	
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> webmenu;
	
	@ManagedProperty("#{comboxBean}")
	protected CommonComBoxBean commonComBoxBean;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public WebMenu data = new WebMenu();
	
	@Override
	public void add() {
		super.add();
		data = new WebMenu();
		data.setId(0L);
		webmenu = this.queryWebmenu();
	}
	
	
	@Action
	public void copyAdd() {
		WebMenu dataOld = data;
		super.add();
		data = new WebMenu();
		data.setId(0L);
		data.setParentid(dataOld.getParentid());
		try {
			String ordno = dataOld.getOrdno();
			if(!StrUtils.isNull(ordno) && StrUtils.isNumber(ordno)){
				Long ordNoNew = Long.valueOf(ordno);
				ordNoNew += 10;
				data.setOrdno(ordNoNew.toString());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		data.setUrl(dataOld.getUrl());
		webmenu = this.queryWebmenu();
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
				this.serviceContext.webMenuService.removeDate(Long.parseLong(id));
			}
			alert("OK");
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.webMenuService.webMenuDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.webMenuService.saveData(data);
	}
	
	private List<SelectItem> queryWebmenu() {
		List<SelectItem> selectItems = commonComBoxBean.getWebmenu(pkVal);
		return selectItems;
    }
	
	@Override
	public void grid_ondblclick(){
		this.pkVal = getGridSelectId();
		webmenu = this.queryWebmenu();
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
}
