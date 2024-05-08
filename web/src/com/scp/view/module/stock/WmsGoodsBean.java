package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.ufms.base.db.SqlObject;
@ManagedBean(name = "pages.module.stock.wmsgoodsBean", scope = ManagedBeanScope.REQUEST)
public class WmsGoodsBean extends GridView {
	
	@Bind
	@SaveState
	public String fkid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String fkid = AppUtils.getReqParam("fkid");
			this.fkid = fkid;
			editDataInit();
		}
	}
	
	@Bind
	@SaveState
	private String jsonData;
	
	@Action
	public void btnRefresh(){
		editDataInit();
	}
	
	public void editDataInit() {
		String ret = "''";
		ret = getJsonByJobid();
		jsonData = StrUtils.isNull(ret) ? "''" : ret;
		this.update.markUpdate(UpdateLevel.Data, "jsonData");
		Browser.execClientScript("initData();");
	}
	
	public String getJsonByJobid() {
		String ret = "{}";
		try {
			String sql = 
				"\nSELECT "+
				"\n		array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json "+
				"\nFROM "+
				"\n	( "+
				"\n		SELECT  "+
				"\n			 id" +
				"\n			,inid AS fkid" +
				"\n			,markno" +
				"\n			,COALESCE(box,0) AS box" +
				"\n			,COALESCE(goodsl,0) AS goodsl" +
				"\n			,COALESCE(goodsw,0) AS goodsw" +
				"\n			,COALESCE(goodsh,0) AS goodsh" +
				"\n			,COALESCE(gdscbm,0) AS gdscbm" +
				"\n			,COALESCE(gdswgt,0) AS gdswgt" +
				"\n			,COALESCE(wgttotle,0) AS wgttotle " +
				"\n			,COALESCE(cbmtotle,0) AS cbmtotle " +
				"\n			,COALESCE(volwgt,0) AS volwgt " +
				"\n			,COALESCE(chargeweight,0) AS chargeweight	  "+
				"\n			,(SELECT x.isubmit FROM wms_in x WHERE x.id = i.inid LIMIT 1) AS isubmit "+
				"\n		FROM wms_indtl i"+
				"\n		WHERE i.inid = " + this.fkid +
				"\n		AND isdelete = FALSE"+
				"\n		ORDER BY id"+
				"\n) AS T "+
				"\n";
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			//System.out.println(sql);
			Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			ret = StrUtils.getMapVal(map, "json");
			if(StrUtils.isNull(ret)){
				ret = "{}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	@Action
	private void saveAjaxSubmit() {
		try{
			StringBuilder sb = new StringBuilder();
    		String dataDetail = AppUtils.getReqParam("dataDetail");
    		if(StrUtils.isNull(dataDetail) || "[]".equals(dataDetail))return;
    		SqlObject sqlObject = new SqlObject("wms_indtl" , dataDetail , AppUtils.getUserSession().getUsercode());
    		sb.append("\n"+sqlObject.toSqls());
    		//System.out.println("\n"+sb.toString());
			daoIbatisTemplate.updateWithUserDefineSql(sb.toString());
			editDataInit();
			MessageUtils.alert("OK");
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
}
