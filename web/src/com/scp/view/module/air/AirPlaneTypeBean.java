package com.scp.view.module.air;

import java.util.Map;
import java.util.regex.Pattern;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.scp.model.bus.AirPlaneType;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;
@ManagedBean(name = "pages.module.air.airplanetypeBean", scope = ManagedBeanScope.REQUEST)
public class AirPlaneTypeBean extends EditGridView {
	
	@Bind
	private UIButton add;
	
	@Bind
	@SaveState
	public String qryPlanetype;
	
	@Override
	public void beforeRender(boolean isPostBack) {
	if (!isPostBack) {
			super.applyGridUserDef();

		}
	}
	
	@Override
	public void clearQryKey() {
		if(!StrUtils.isNull(qryPlanetype)) {
			qryPlanetype = "";
		}
		super.clearQryKey();
	}
	
	@Action
	public void btnAdd() {
		AirPlaneType airPlaneType = new AirPlaneType();
		try{
			if (addedData != null) {
				JSONArray jsonArray = (JSONArray) addedData;
				if(jsonArray != null && jsonArray.size() > 0){
					JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.size()-1);
					if(jsonObject.get("planetype")!=null && !String.valueOf(jsonObject.get("planetype")).isEmpty()){
						airPlaneType.setPlanetype(String.valueOf(jsonObject.get("planetype")));
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		editGrid.appendRow(airPlaneType);
	}
	
	@Action
	public void btnDel(){
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if(p.matcher(s).matches()){
				try {
					serviceContext.airPlaneTypeService.removeDate(Long.parseLong(s));
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		editGrid.remove();
	}
	
	@Action
	public void btnSave(){
		try {
			editGrid.commit();
			boolean isChangeFlag = false;
			//如果列表里面修改的柜子，数据没有保存，先保存再处理后面数据  20190507 
			if (modifiedData != null) {
			    update(modifiedData);
			    JSONArray jsonArray = (JSONArray) modifiedData;
			    if(jsonArray.size()>0)isChangeFlag=true;
			}
			if (addedData != null) {
			    add(addedData);
			    JSONArray jsonArray = (JSONArray) addedData;
			    if(jsonArray.size()>0)isChangeFlag=true;
			}
			if(removedData != null){
				remove(removedData);
				JSONArray jsonArray = (JSONArray) removedData;
			    if(jsonArray.size()>0)isChangeFlag=true;				
			}
			if(isChangeFlag){
				editGrid.reload();
			}
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.airPlaneTypeService.addBatchEditGrid(addedData);
	}
	
	@Override
	protected void remove(Object removedData) {
		serviceContext.airPlaneTypeService.removedBatchEditGrid(removedData);
	}
	
	
	@Override
	protected void update(Object modifiedData) {
		try {
			serviceContext.airPlaneTypeService.updateBatchEditGrid(modifiedData);
		} catch (Exception e) {
			//MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void btnRefresh() {
		if (editGrid != null) {
			gridLazyLoad = false;
			this.editGrid.reload();
		}
	}
	
	/*@Override
	public void refresh(){
		System.out.println("qryPlanetype--->"+qryPlanetype);
	}*/
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
 		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(qryPlanetype)){
			qry += "\n AND planetype ILIKE '%"+qryPlanetype+"%'";
		}
		m.put("qry", qry);
		return m;
	}
	
}

