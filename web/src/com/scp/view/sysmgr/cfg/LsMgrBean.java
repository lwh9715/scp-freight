package com.scp.view.sysmgr.cfg;

import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.springframework.transaction.annotation.Transactional;

import com.scp.dao.sys.SysMlDao;
import com.scp.model.sys.SysMl;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.cfg.lsmgrBean", scope = ManagedBeanScope.REQUEST)
public class LsMgrBean extends GridFormView {
	
	@ManagedProperty("#{sysMlDao}")
	public SysMlDao sysMlDao;
	
	@Accessible
	@SaveState
	public SysMl data;
	
	@Bind
	@SaveState
	public Long pkVal;
	
	@Bind
	@SaveState
	public String ids;
	
	@Bind
	@SaveState
	public String cns;
	
	@Bind
	@SaveState
	public String ens;
	
	@Bind
	@SaveState
	public String filter = "";
	
	@Bind
	public UIPanel editPanel;
	
	@Bind
	public UIPanel alleditPanel2;
	
	@Bind
	public UIWindow setallWindow;
	
	@Action
	public void initBatch(){
		List<SysMl> lists = sysMlDao.findAllByClauseWhere("(en IS NULL OR en = '')");
		StringBuffer idsBuffer = new StringBuffer();
		StringBuffer cnsBuffer = new StringBuffer();
		//int i = 0;
		for (SysMl sysMl : lists) {
			idsBuffer.append(sysMl.getId());
			idsBuffer.append("\n");
			
			cnsBuffer.append(sysMl.getCh());
			cnsBuffer.append("\n");
			//i++;
			//if(i > 100)break;
		}
		ids = idsBuffer.toString();
		cns = cnsBuffer.toString();
		ens = "";
		
		this.update.markUpdate(UpdateLevel.Data,"ids");
		this.update.markUpdate(UpdateLevel.Data,"cns");
		this.update.markUpdate(UpdateLevel.Data,"ens");
	}
	
	@Action
	@Transactional
	public void saveBatch(){
		String[] enarray = ens.split("\n");
		String[] idarray = ids.split("\n");
		for (int i = 0; i < enarray.length; i++) {
			Long id = Long.valueOf(idarray[i]);
			SysMl sysMl = sysMlDao.findById(id);
			sysMl.setEn(enarray[i]);
			sysMlDao.createOrModify(sysMl);
		}
	}
	
	@Action
	public void batchedit(){
		if(setallWindow != null)setallWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "alleditPanel2");
	}
	
	
	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		data = (SysMl)sysMlDao.findById(Long.valueOf(id));
		this.pkVal = getGridSelectId();
		super.grid_ondblclick();
	}
	
	
	@Action
	public void linkEdit(){
		String id = AppUtils.getReqParam("pkid");
		data = (SysMl)sysMlDao.findById(Long.valueOf(id));
		this.pkVal = getGridSelectId();
		super.grid_ondblclick();
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		
		if(!StrUtils.isNull(filter))map.put("filter", filter);
		return map;
	}
	
	
	@Action
	public void filterNoEn(){
		filter = "\nAND (en IS NULL OR en = '')";
		this.grid.reload();
	}
	

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysMlService.sysMlDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.sysMlService.saveData(data);
	}

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();;
		if(ids == null || ids.length < 1){
			alert("Please choose one row!");
			return;
		}
		StringBuilder sqlStringBuilder = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			sqlStringBuilder.append("\nDELETE FROM sys_ml WHERE id = " + ids[i] + ";");
		}
		if(sqlStringBuilder.length()>0){
			sysMlDao.executeSQL(sqlStringBuilder.toString());
		}
		this.grid.reload();
	}
	
	@Override
	public void add() {
		super.add();
		data = new SysMl();
		data.setId(0l);
	}
}
