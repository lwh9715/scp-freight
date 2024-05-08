package com.scp.view.sysmgr.cfg;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.Sysformcfg;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.sysmgr.cfg.formcfgBean", scope = ManagedBeanScope.REQUEST)
public class FormcfgBean extends GridFormView{
	
	@SaveState
	public Sysformcfg data = new Sysformcfg();

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysformcfgService.sysformcfgDao.findById(this.pkVal);
		Browser.execClientScript("setHuedemo('"+data.getColor()+"')");
	}

	@Override
	protected void doServiceSave() {
		serviceContext.sysformcfgService.saveData(data);
	}
	
	@Override
	public void add() {
		super.add();
		data = new Sysformcfg();
		data.setColor("#FFFFC7");
		data.setId(0l);
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
				serviceContext.sysformcfgService.removeDate(Long.parseLong(id));
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
				serviceContext.sysformcfgService.setRequir(Long.parseLong(id));
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
				serviceContext.sysformcfgService.setnotRequir(Long.parseLong(id));
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
				serviceContext.sysformcfgService.setColors(Long.parseLong(id),setcolor);
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
				serviceContext.sysformcfgService.setColorsNull(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	
	@Action
	public void setishide(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysformcfgService.setIshide(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void setnotishide(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysformcfgService.setnotIshide(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
}
