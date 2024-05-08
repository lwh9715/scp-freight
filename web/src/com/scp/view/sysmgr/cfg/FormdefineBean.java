package com.scp.view.sysmgr.cfg;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysFormdef;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.sysmgr.cfg.formdefineBean", scope = ManagedBeanScope.REQUEST)
public class FormdefineBean extends GridFormView{
	
	@SaveState
	public SysFormdef data = new SysFormdef();
	
	
	@SaveState
	@Bind
	public String formtype = "F";
	
	@SaveState
	@Bind
	public String modelCode = "";
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String formtype1 = AppUtils.getReqParam("formtype");
			String modelCode1 = AppUtils.getReqParam("modelCode");
			if(!StrUtils.isNull(formtype1))this.formtype = formtype1;
			if(!StrUtils.isNull(modelCode1))this.modelCode = modelCode1;
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = "\nAND 1=1";
		filter += "\nAND formtype = '" + formtype + "'";
		if(!StrUtils.isNull(modelCode)){
			filter += "\nAND beaname = '" + modelCode + "'";
		}
		map.put("filter", filter);
		return map;
	}
	

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysFormdefService.sysFormdefDao.findById(this.pkVal);
		String js = data.getColor()==null?"":data.getColor();
		Browser.execClientScript("setHuedemo('"+js+"')");
	}

	@Override
	protected void doServiceSave() {
		serviceContext.sysFormdefService.saveData(data);
		
	}
	
	@Override
	public void add() {
		super.add();
		data = new SysFormdef();
		data.setId(0l);
		data.setBeaname(modelCode);
		data.setFormtype(formtype);
		data.setInputype("UITextField");
	}
	
	
	@Action
	public void addCopy() {
		SysFormdef old = data;
		add();
		data.setBeaname(old.getBeaname());
		data.setInputype(old.getInputype());
	}
	
	@Override
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysFormdefService.removeDate(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void setRequir(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysFormdefService.setRequir(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void setnotRequir(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysFormdefService.setnotRequir(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Bind
	public String setcolor="#FFFFC7";
	
	@Action
	public void setColors(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysFormdefService.setColors(Long.parseLong(id),setcolor);
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void setColorsNull(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysFormdefService.setColorsNull(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}

	@Bind
	public UIIFrame formdefineIframe;
	
	@Bind
	public UIWindow formdefineWindow;
	
	@Action
	public void formPreview(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0 || ids.length > 1) {
			MessageUtils.alert("Please choose only one!");
			return;
		}
		SysFormdef formdef = serviceContext.sysFormdefService.sysFormdefDao.findById(Long.parseLong(ids[0]));
		String beaname = formdef.getBeaname();
		formdefineIframe.load("/scp/pages/sysmgr/cfg/dynamicformPreview.xhtml?m="+beaname+"&p=");
		formdefineWindow.show();
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		Browser.execClientScript("setSrcdataLable()");
	}

	
}
