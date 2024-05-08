package com.scp.view.comp;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;


/**
 * 编辑
 * @author neo
 *
 */
public abstract class FormView{
	
	@Inject
	protected PartialUpdateManager update;
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;
	
	@Bind
	public UIPanel editPanel;
	
	@Bind
	@SaveState
	public Long pkVal;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	
	@Action
	public void add(){
		this.pkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void qryAdd(){
		this.add();
	}
	
	@Action
	public void save(){
		//this.pkVal = getViewControl().save(this.pkVal , data);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
	}
	
	@Action
	public void del(){
	}
	
	@Action
	public void refresh(){
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	public void disableAllButton(Boolean ischeck) {
		if (ischeck) {
			String script = "disableAllButton(true);";
			Browser.execClientScript(script);
		} else{
			String script = "disableAllButton(false);";
			Browser.execClientScript(script);
		}
	}
	
	protected String getMBeanName() {
		ManagedBean ma = this.getClass().getAnnotation(ManagedBean.class);
		String mbeanName = ma.name();
		return mbeanName;
	}
	
	@ManagedProperty("#{daoIbatisTemplate}")
	public DaoIbatisTemplate daoIbatisTemplate;
	
	/**
	 * @return返回对应bean表单设置
	 */
	public String getSysformcfg(String mBeanName){
		String sql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM ("
					+"		SELECT columnname AS columnid,inputlable AS namec,* from sys_formdef WHERE beaname = '"+mBeanName+"'"
					+") AS T";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(m!=null&&m.get("json")!=null){
			return m.get("json").toString();
		}else{
			return "";
		}
	}

	protected void alert(String message) {
		Browser.execClientScript("window.alert('" + message + "');");
	}
}
