package com.scp.view.sysmgr.mail;


import java.util.Map;
import java.util.regex.Pattern;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysEmailFastext;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.mail.emailfastextBean", scope = ManagedBeanScope.REQUEST)
public class EmailFastextBean extends GridFormView{
	
	@Action
	public void del() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if (p.matcher(s).matches()) {
				try {
					this.serviceContext.sysEmailService.removeSysEmailFastextByid(Long.parseLong(s));
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
	}
	
	@Action
	public void chooseEmaileFastext(){
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		String setvalue = "";
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if (p.matcher(s).matches()) {
				try {
					SysEmailFastext sysEmailFastext = this.serviceContext.sysEmailService.sysEmailFastextDao.findById(Long.parseLong(s));
					setvalue += sysEmailFastext.getContent();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		String js = "parentSetCkeditorAdd('"+setvalue.replace("\"", "\\\"")+"')";
		//System.out.println(js);
		Browser.execClientScript(js);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		qry += "\n AND inputer = '"+AppUtils.getUserSession().getUsercode()+"'";
		m.put("qry", qry);
		return m;
	}
	
	@SaveState
	@Accessible
	public SysEmailFastext dtlData;

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.sysEmailService.sysEmailFastextDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try{
			if(this.pkVal>0){
				serviceContext.sysEmailService.sysEmailFastextDao.modify(this.dtlData);
			}else{
				serviceContext.sysEmailService.sysEmailFastextDao.create(this.dtlData);
			}
			this.pkVal = this.dtlData.getId();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void add() {
		this.pkVal = -1L;
		this.dtlData = new SysEmailFastext();
		super.add();
		Browser.execClientScript("editor1.setValue('');");
	}
	
	
	@Bind
	public String dtlStr;

	@Override
	public void grid_ondblclick() {
		try {
			this.pkVal = getGridSelectId();
			doServiceFindData();
			this.refreshForm();
			if (editWindow != null)editWindow.show();
			dtlStr = this.dtlData.getContent();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			Browser.execClientScript("refreshEdit()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
