package com.scp.view.module.oa.staffmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.dao.IBaseDao;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;



@ManagedBean(name = "pages.module.oa.staffmgr.departmentBean", scope = ManagedBeanScope.REQUEST)
public class DepartmentBean extends GridFormView {
	
	@ManagedProperty("#{sysDepartmentDao}")
	public IBaseDao sysDepartmentDao;
	
	@ManagedProperty("#{sysCorporationDao}")
	public IBaseDao sysCorporationDao; 
	
	@SaveState
	@Accessible
	public Long corpid;
	
	@Bind
	public Long company;
	
	@SaveState
	public SysDepartment data;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)) {
				this.corpid = Long.parseLong(id);
				this.qryMap.put("corpid$", this.corpid);
			}
		}
	}

	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		data = (SysDepartment)sysDepartmentDao.findById(Long.valueOf(id));
		this.company = data.getSysCorporation().getId();
		this.pkVal = data.getId();
		super.grid_ondblclick();
	}
	
	
	@Override
	public void add() {
		data = new SysDepartment();
//		SysCorporation corporation = (SysCorporation)sysCorporationDao.findAllByClauseWhere("iscustomer = 'N'").get(0);
		if(this.corpid != null) {
			SysCorporation corporation = (SysCorporation)this.sysCorporationDao.findById(this.corpid);
			this.company = corporation.getId();
		}
		super.add();
	}
	
	@Override
	protected void doServiceFindData() {
		data = this.serviceContext.sysDeptService.sysDepartmentDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		SysCorporation sc = (SysCorporation) this.sysCorporationDao.findById(this.company);
		data.setSysCorporation(sc);
		this.serviceContext.sysDeptService.saveData(data);
		MessageUtils.alert("OK");
	}

	@Override
	public void del(){
		data = (SysDepartment)sysDepartmentDao.findById(this.pkVal);
		sysDepartmentDao.remove(data);
		this.refresh();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		if(this.corpid == null) {
			return super.getQryClauseWhere(queryMap);
		}
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND corpid = " + this.corpid;
		m.put("qry", qry);
		return m;
	}
	
}
