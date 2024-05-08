package com.scp.view.module.formdefine;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;


public class ComBox {
	
	private String comId;
	
	private String comLabel;
	
	private String comValue;

	private String comJsVar;
	
	public List<SelectItem> selectItem = new ArrayList<SelectItem>();
	
	
	public List<SelectItem> getSelectItem() {
//		List<SelectItem> items = new ArrayList<SelectItem>();
//		items.add(new SelectItem(comId + ":" + "A", "客户代码/简称"));
//		items.add(new SelectItem(comId + ":" + "B", "收付款编号"));
//		items.add(new SelectItem(comId + ":" + "C", "收付款摘要"));
//		items.add(new SelectItem(comId + ":" + "D", "支票号"));
//		items.add(new SelectItem(comId + ":" + "E", "工作号"));
//		items.add(new SelectItem(comId + ":" + "F", "工作号/收据号"));
//		items.add(new SelectItem(comId + ":" + "N", "无"));
		return selectItem;
	}

	public String getComJsVar() {
		return comJsVar;
	}

	public void setComJsVar(String comJsVar) {
		this.comJsVar = comJsVar;
	}

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getComLabel() {
		return comLabel;
	}

	public void setComLabel(String comLabel) {
		this.comLabel = comLabel;
	}

	public void setSelectItem(List<SelectItem> selectItem) {
		this.selectItem = selectItem;
	}

	public String getComValue() {
		return comValue;
	}

	public void setComValue(String comValue) {
		this.comValue = comValue;
	}
	
	

}
