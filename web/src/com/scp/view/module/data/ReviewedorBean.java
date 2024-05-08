package com.scp.view.module.data;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.model.data.DatReviewedor;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.reviewedorBean", scope = ManagedBeanScope.REQUEST)
public class ReviewedorBean extends GridFormView {

	public DatReviewedor selectedRowData = new DatReviewedor();
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		map.put("filter", filter);
		return map;
	}

	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			try {
				for (String id : ids) {
					serviceContext.daoIbatisTemplate.queryWithUserDefineSql("UPDATE dat_reviewedor SET isdelete = TRUE WHERE id = "+id+" AND isdelete = FALSE");
				}
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			refresh();
		}
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}
	
	public void addinfo() {
		editWindow.show();
	}
	
	@Override
	protected void doServiceSave() {
		try {
			String sql = "INSERT INTO dat_reviewedor (id,corpid,userid,processid,assignname,isdelete) VALUES (getid(),"+selectedRowData.getCorpid()+","+selectedRowData.getUserid()+","+selectedRowData.getProcessid()+",'"+selectedRowData.getAssignname().trim()+"',false);";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		refresh();
	}
	
	
	@Override
	public void add() {
		selectedRowData = new DatReviewedor();
		super.add();
	}
	
}
