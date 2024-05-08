package com.scp.view.module.price;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.price.PriceFclFeeadd;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.localchargeconditionBean", scope = ManagedBeanScope.REQUEST)
public class LocalChargeConditionBean extends GridView {
	
	@SaveState
	@Bind
	public String linkid = "-123";
	
	@SaveState
	@Bind
	public String bargeid = "-123";
	
	@SaveState
	@Bind
	public String src = "";
	
	@Bind
	public UIWindow join2Window;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("linkid");
			String bargeidStr = AppUtils.getReqParam("bargeid");
			src = AppUtils.getReqParam("src");
			
			if(!StrUtils.isNull(id)){
				linkid = id;
			}
			
			if(!StrUtils.isNull(bargeidStr)){
				bargeid = bargeidStr;
			}
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = "\nAND (fclid = " + linkid + " OR fclid =  " + bargeid + ")  AND isdelete = FALSE";
		if(!StrUtils.isNull(src) && "query".equals(src)){// neo 20181105 运价查询列表中多行显示中已包含了燃油附加费和中转费，查询弹窗中不再显示重复的这两项费用明细
			filter += "\nAND feeitemname NOT like '%燃油附加费%' AND feeitemname NOT like '%中转费%'";
		}
		map.put("filter", filter);
		return map;
	}
	
	@Bind
	@SaveState
	public String conditiontype;
	
	@Bind
	@SaveState
	public String condition;
	
	@Bind
	@SaveState
	public String conditionvalue;
	
	@Action
	public void setCondition(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请至少选择一条记录");
			return;
		}
		if(ids.length==1){
			PriceFclFeeadd priceFclFeeadd = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findById(Long.parseLong(ids[0]));
			conditiontype = priceFclFeeadd.getConditiontype();
			condition = priceFclFeeadd.getCondition();
			conditionvalue = priceFclFeeadd.getConditionvalue();
		}else{
			conditiontype = "";
			condition = "";
			conditionvalue = "";
		}
		join2Window.show();
		update.markUpdate("conditiontype");
		update.markUpdate("condition");
		update.markUpdate("conditionvalue");
	}
	
	@Action
	public void joinConfirm(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请至少选择一条记录");
			return;
		}
		for(String id:ids){
			PriceFclFeeadd priceFclFeeadd = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findById(Long.parseLong(id));
			priceFclFeeadd.setConditiontype(conditiontype);
			priceFclFeeadd.setCondition(condition);
			priceFclFeeadd.setConditionvalue(conditionvalue);
			serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.modify(priceFclFeeadd);
		}
		this.grid.reload();
		join2Window.close();
	}
	
}
