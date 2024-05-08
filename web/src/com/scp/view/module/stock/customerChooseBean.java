package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.wms.WmsPriceGroup;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.customerchooseBean", scope = ManagedBeanScope.SESSION)
public class customerChooseBean extends GridView {
	
	
	
	@SaveState
	@Accessible
	public WmsPriceGroup  wmsPriceGroup =new WmsPriceGroup();
	
	@SaveState
	@Accessible
	public Long priceid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("priceid");
			 priceid = new Long(code);
		}
	}

	
	@Action
	public void importCustom(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
	   for(int i=0;i<ids.length;i++){
		   wmsPriceGroup=new WmsPriceGroup();
		 Long  customid=new Long((Long.parseLong(ids[i])));
		 wmsPriceGroup.setPriceid(priceid);
		 wmsPriceGroup.setCustomerid(customid);
		 serviceContext.wmsPriceGroupMgrService().saveDtlData(wmsPriceGroup);
	   }
	     MessageUtils.alert("OK");
	     refresh();
		
	}
	 
	@Override
	//相同的费用项目定义的报价组中，不能有重复的客户 sunny 20150906
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND  not exists (" ;
		qry += "SELECT 1 " +
				"from wms_price_group g , wms_price p , wms_price x  " +
				"where p.id=g.priceid " +
				"AND g.customerid = c.id " +
				"AND p.feeitemid = x.feeitemid " +
				"AND x.id = " + priceid +
				"AND NOW()::DATE BETWEEN p.timestart AND p.timeend)";
		//qry+="SELECT 1 from wms_price_group gg , wms_price pp where pp.id=gg.priceid AND pp.id<>p.id AND gg.customerid =c.id "+
//"AND ((pp.timestart between p.timestart AND p.timeend) OR (pp.timeend between p.timestart AND p.timeend)))";
		//qry+="SELECT 1 from wms_price_group g , wms_price p ,wms_price_group gg , wms_price pp where p.id=g.priceid AND  g.priceid ="+priceid+" AND  p.id<>pp.id AND (g.customerid = c.id OR (gg.customerid =c.id AND ( (pp.timestart between p.timestart AND p.timeend) OR (pp.timeend between p.timestart AND p.timeend))) ))";
		
		//qry += "\nAND  not exists (" ;
		//qry+="SELECT 1 from wms_price_group g , wms_price p  where p.id=g.priceid AND  g.priceid ="+priceid+" AND g.customerid = c.id)";
		//qry +="SELECT 1 from wms_price_group g where g.priceid ="+priceid+"AND g.customerid = c.id )";
		//OR  ( w.customerid =c.id AND  ((ww.timestart between gg.timestart AND gg.timeend) OR (ww.timeend between gg.timestart AND gg.timeend)))
		//qry +="SELECT 1 from wms_price_group g ,wms_price gg  where g.priceid = gg.id  AND  g.priceid ="+priceid+" AND g.customerid = c.id AND NOT EXISTS(" +
		//		"SELECT 1 FROM wms_price_group w ,wms_price ww WHERE ww.id<>gg.id AND w.priceid = ww.id AND w.customerid = c.id AND ((ww.timestart between gg.timestart AND gg.timeend) OR (ww.timeend between gg.timestart AND gg.timeend))))";
		m.put("qry", qry);
		return m;
	}
}
