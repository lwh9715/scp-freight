package com.scp.view.module.price;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.data.DatLine;
import com.scp.model.price.Pricefclpubrule;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.price.pricefclpubruleBean", scope = ManagedBeanScope.REQUEST)
public class PricefclpubruleBean extends GridFormView{
	
	@Bind
	private UIButton save;
	
	@Bind
	private UIButton qryAdd;
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton edit;
	
	
	@SaveState
	@Accessible
	public Pricefclpubrule selectedRowData = new Pricefclpubrule();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			
		}
	}

	@Action
	public void qryAdd(){
		selectedRowData.setId(-1L);
		selectedRowData.setRuletype("T");
		this.add();
	}
	

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.pricefclpubruleMgrService.pricefclpubruleDao.findById(this.getGridSelectId());
		
	}
	
	@Override
	protected void doServiceSave() {
		try {
			serviceContext.pricefclpubruleMgrService.saveData(selectedRowData);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("保存出错");
		}		
	}
	
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.pricefclpubruleMgrService.removeDate(ids);
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("删除失败");
		}
	}
	
	
	@Action
	public void edit() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		doServiceFindData();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid lineGrid;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapLin = new HashMap<String, Object>();

	@Bind(id = "lineGrid", attribute = "dataProvider")
	protected GridDataProvider getLineGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.data.lineBean.grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere(qryMapLin), start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.data.lineBean.grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere(qryMapLin));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	
	@Action
	public void qryLine() {
		this.lineGrid.reload();
	}
	
	@Action
	public void confirm() {
		try {
			String[] ids = this.lineGrid.getSelectedIds();
			if(ids == null){
				MessageUtils.alert("请勾选一条记录！");
				return;
			}
			String lineCode = "";
			for (String id : ids) {
				DatLine dl = serviceContext.lineMgrService.datLineDao.findById(Long.valueOf(id));
				lineCode = lineCode + dl.getCode() + ",";
			}
			lineCode = lineCode.substring(0, lineCode.length()-1);
			this.selectedRowData.setLines(lineCode);
			
			update.markUpdate(true, UpdateLevel.Data, "lines");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
