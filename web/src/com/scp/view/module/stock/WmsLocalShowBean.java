package com.scp.view.module.stock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.map.ListOrderedMap;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.Align;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.grid.GridColumn;
import org.operamasks.faces.component.grid.GridColumnModel;
import org.operamasks.faces.component.grid.GridHeader;
import org.operamasks.faces.component.grid.GridHeaderCell;
import org.operamasks.faces.component.grid.GridHeaderModel;
import org.operamasks.faces.component.grid.provider.GridRowDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.DynamicGridView;

@ManagedBean(name = "pages.module.stock.wmslocalshowBean", scope = ManagedBeanScope.REQUEST)
public class WmsLocalShowBean extends DynamicGridView{


	@Bind
	private UICombo wareHouseCombo;
	
	@Bind
	private UICombo wareHouseAreaCombo;

	@Bind(id = "wareHouseAreaComboItems")
	@SelectItems
	@SaveState
	private SelectItem[] wareHouseAreaComboItems;
	
	public SelectItem[] getWareHouseAreaComboItems() throws Exception {
		return CommonComBoxBean.getComboxItems("d.id",
				"d.code ",
				"dat_warehouse_area AS d", " WHERE d.warehouseid = " + warehouseid,
				" ORDER BY d.code").toArray(new SelectItem[0]);
	}

	@Action
	private void changeWareHouse() {
		
		warehouseid = AppUtils.getReqParam("warehouseid");
		if (warehouseid != null && warehouseid.length() > 0) {
			try {
				wareHouseAreaComboItems = getWareHouseAreaComboItems();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
				return;
			}
			// 联动：处理选择一级时自动选择二级的第一列(二级没有时为空)
			if (wareHouseAreaComboItems != null
					&& wareHouseAreaComboItems.length >= 1) {
				wareHouseAreaCombo
						.setValue(wareHouseAreaComboItems[0].getValue());
			} else {
				String script = "wareHouseAreaComboJsVar.setValue('');";
				Browser.execClientScript(script);
				return;
			}
			update.markUpdate(UpdateLevel.Data, wareHouseAreaCombo);
			areaId = null;
			executeQuery();
		}
	}
	@SaveState
	private String areaId;
	
	
	@Bind
	public String warehouseid = "-1";
	
	@Action
	private void changeArea() {
		Map<String, String> reqParam = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap();
		areaId = reqParam.get("areaid");
		executeQuery();
		String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
		dtlIFrame.setSrc(blankUrl);
		update.markAttributeUpdate(dtlIFrame, "src");
		
	}
	/**
	 * 执行查询
	 */
	@Action(id = "queryBtn")
	public void executeQuery() {
		String areaid = wareHouseAreaCombo.getValue()==null?null:wareHouseAreaCombo.getValue().toString();

		if (!StrUtils.isNull(areaId) && !areaId.equalsIgnoreCase(areaid)) {
			areaid = areaId;
		}
		
		gridCollection = new ArrayList<Map>();
		
		if (StrUtils.isNull(areaid)) {
			MessageUtils.alert("Please choose area first!");
			return;
		}
		try {
			String sql ="SELECT colcount AS cols,rowcount AS rows FROM dat_warehouse_area st WHERE id  = "
				+ areaid + ";";
			Map queryMap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(queryMap.isEmpty())return;
			int rows = Integer.parseInt(String.valueOf(queryMap.get("rows")));
			int cols = Integer.parseInt(String.valueOf(queryMap.get("cols")));

			for (int i = 0; i < rows; i++) {
				String qrysql = "\nSELECT "
					+ "\n l.id "
					+ "\n,code "
					+ "\n,(EXISTS(SELECT 1 FROM _wms_stock x WHERE x.locid = l.id AND x.piece > 0) OR EXISTS(SELECT 1 FROM wms_in_locdtl x , wms_indtl y , wms_in z , _wms_stock zz WHERE x.locid = l.id AND x.pieces > 0 AND x.iscompleted = false AND x.indtlid = y.id AND y.inid = z.id AND z.ischeck = TRUE AND z.isdelete = FALSE AND y.isdelete = false AND zz.indtlid = y.id AND zz.piece > 0)) AS isempty" 
					+ "\nFROM dat_warehouse_loc l"
					+ "\nWHERE 1=1"
					+ "\nAND l.areaid = " + areaid
					+ "\nAND l.rowindex = " + (i+1)
					+ "\nORDER BY rowindex , colindex , code;";
				List<Map> goodsStoList = daoIbatisTemplate.queryWithUserDefineSql(qrysql);
				
				ListOrderedMap row = new ListOrderedMap();
				int temp = 1;
				for (Map map : goodsStoList) {
					Boolean isempty = (Boolean) map.get("isempty");
					String prefix = !isempty?"E":"H";
					row.put(StrUtils.convertNumToLetter(temp), prefix + "-" + StrUtils.getMapVal(map, "code"));
					temp++;
				}
				row.put("rowIndexCol", String.valueOf(i));
				gridCollection.add(row);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		grid.reload();
		grid.rebind();
		grid.repaint();
	}
	
	@Bind
	private UIIFrame dtlIFrame;

	@Action
	public void showLocalDtlAction() {
		String code = AppUtils.getReqParam("code");
		if (StrUtils.isNull(code)) {
			return;
		}
		String areaid = wareHouseAreaCombo.getValue()==null?null:wareHouseAreaCombo.getValue().toString();

		dtlIFrame.setSrc("../data/warehouselocaldtl.xhtml?code=" + code+"&areaid="+areaid);
		update.markAttributeUpdate(dtlIFrame, "src");
	}

	

	@Action(id = "dtlDialog",event="onclose")
	private void dtlEditDialogClose() {
		String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.html";
		dtlIFrame.setSrc(blankUrl);
		update.markAttributeUpdate(dtlIFrame, "src");
		
	}
	
	
	@Override
	public GridRowDataProvider getDinamicGridRowProvider() {
		return new GridRowDataProvider() {
			public Object getLabel(Object rowData, GridColumn column) {
				String id = column.getId();
				ListOrderedMap listOrderedMap = (ListOrderedMap) rowData;
				return listOrderedMap.get(id);
			}
		};
	}


	@Override
	public GridColumnModel createDynamicGridColumnModel(List<Map> list) {
		GridColumnModel columnModel = new GridColumnModel();
		GridColumn column;
		if (list == null || list.size() <= 0) {
			column = new GridColumn("Result");
			columnModel.addColumn(column);
			return columnModel;
		}
		Map map = list.get(0);
		Set<String> set = map.keySet();
		String key;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			key = (String) iterator.next();
			column = new GridColumn(key);
			column.setEnableSort(false);
			column.setClientFormatter("formatChange");
			column.setWidth(60);

			if (map.get(key) instanceof String) {
				column.setAlign(Align.LEFT);
			} else {
				column.setAlign(Align.RIGHT);
			}
			if ("rowIndexCol".equalsIgnoreCase(column.getId())) {
				column.setHidden(true);
			}
			columnModel.addColumn(column);
		}
		return columnModel;
	}

	@Override
	public GridHeaderModel createDynamicGridHeaderModel(List<Map> list) {
		GridHeaderModel headerModel = new GridHeaderModel();
		GridHeader header = new GridHeader();
		GridHeaderCell cell;

		if (list == null || list.size() <= 0) {
			cell = new GridHeaderCell("Result");
			header.addCell(cell);
			headerModel.addHeader(header);
			return headerModel;
		}
		Map map = list.get(0);
		Set<String> set = map.keySet();
		String key;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			key = (String) iterator.next();
			cell = new GridHeaderCell(key);
			header.addCell(cell);
		}
		headerModel.addHeader(header);
		return headerModel;
	}
}
