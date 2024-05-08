package com.scp.view.module.finance.cashier;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.fs.FsBank;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.cashier.daybankmgrBean", scope = ManagedBeanScope.REQUEST)
public class DayBankMgrBean extends GridFormView {
	
	@Bind
	@SaveState
	public Long actsetid = -1L;
	
	@Bind
	@SaveState
	public String actsetcode = "";
	
	@SaveState
	@Accessible
	public FsBank selectedRowData;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			this.add();
			this.setActsetid();
		}
	}
	
	/**
	 * 设置帐套
	 */
	public void setActsetid(){
		String sql = "SELECT code||'/'||substring(COALESCE(name,'') , 1 , 5) AS actsetcode  FROM fs_actset WHERE isdelete = FALSE AND id = "
				+ AppUtils.getUserSession().getActsetid();
		Map m;
		try {
			m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			this.actsetid = AppUtils.getUserSession().getActsetid();
			this.actsetcode= "账套:" + (String)m.get("actsetcode");
			selectedRowData.setActsetid(this.actsetid);
			//保存进来
		} catch (Exception e) {
			MessageUtils.alert("当前用户没有绑定帐套,不能新增凭证");
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "actsetid");
		update.markUpdate(true, UpdateLevel.Data, "actsetcode");
	}

	@Override
	public void add() {
		selectedRowData = new FsBank();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setBankdate(Calendar.getInstance().getTime());
		selectedRowData.setActsetid(this.actsetid);
		selectedRowData.setBanktype("1");
		selectedRowData.setPrepared(AppUtils.getUserSession().getUsername());
		// 根据当前登录用户，找对应的帐套，过滤兑换率列表
		String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
				+ AppUtils.getUserSession().getActsetid();
		Map m;
		String year = "";
		String period = "";
		try {
			m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			year = m.get("year").toString();
			period = m.get("period").toString();
			selectedRowData.setYear(Short.decode(year));
			selectedRowData.setPeriod(Short.decode(period));
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.add();
	}


	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.fsBankMgrService.fsBankDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		this.serviceContext.fsBankMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Action
	public void deleter(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0 || ids.length > 1) {
				MessageUtils.alert("请选择单行记录");
				return;
			}else{
				this.serviceContext.fsBankMgrService.removeDate(getGridSelectId());
				MessageUtils.alert("OK!");
				refresh();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		filter += "\nAND banktype = '1'";
		map.put("filter", filter);
		return map;
	}
	
	
	@Override
	public void del() {
		try {
			if(selectedRowData.getId()==0){
				this.add();	
			}else{
				this.serviceContext.fsBankMgrService.removeDate(selectedRowData.getId());
				refresh();
				this.add();
				this.alert("OK");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}

	// 凭证字
	@Bind(id = "vchtype")
	public List<SelectItem> getVchtype() {
		try {
			return CommonComBoxBean.getComboxItems("d.id", "d.name",
					"fs_vchtype AS d", "WHERE d.actsetid=" + this.actsetid,
					"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	// 科目(code)
	@Bind(id = "actcode")
	public List<SelectItem> getActcode() {
		try {
			return CommonComBoxBean.getComboxItems("d.id",
					"d.code||'/'||d.name ", "fs_act d", "WHERE d.isdelete = FALSE AND code LIKE '1002%' AND d.actsetid="
							+ AppUtils.getUserSession().getActsetid(),
					"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
}
