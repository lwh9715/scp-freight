package com.scp.view.module.data;

import com.scp.model.data.DatCntype;
import com.scp.service.data.CntypeMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.html.impl.UIIFrame;

import java.util.Map;

@ManagedBean(name = "pages.module.data.cntypeBean", scope = ManagedBeanScope.REQUEST)
public class CntypeBean extends GridFormView {
	
	

	@ManagedProperty("#{cntypeMgrService}")
	public CntypeMgrService cntypeMgrService;
	
	
	@SaveState
	@Accessible
	public DatCntype selectedRowData = new DatCntype();

	@Bind
	public UIIFrame edi_mapping;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String ediUrl = "../edi/edi_map.aspx?edittype=cnttype";
			this.edi_mapping.load(ediUrl);
		}
	}
	
	@Override
	public void add() {
		selectedRowData = new DatCntype();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = cntypeMgrService.datcntypeDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		cntypeMgrService.saveData(selectedRowData);
		this.alert("OK");
		refresh();
		
		
	}
	
	@Action
	public void deleter(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0 || ids.length > 1) {
				MessageUtils.alert("请选择单行记录");
				return;
			}else{
				String sql = "update dat_cntype set isdelete = true  where id =" + ids[0];
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
				refresh();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void del() {
		cntypeMgrService.datcntypeDao.remove(selectedRowData);
		this.add();
		this.alert("OK");
		refresh();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		map.put("filter", filter);
		return map;
	}

}
