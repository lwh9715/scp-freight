package com.scp.view.module.airtransport;


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
import com.scp.view.comp.FormView;
import com.ufms.base.db.SqlObject;

@ManagedBean(name = "pages.module.airtransport.goodsbillBean", scope = ManagedBeanScope.REQUEST)
public class GoodsBillBean extends FormView {

	@Bind
	@SaveState
	private String jsonData;
	

	@Bind
	@SaveState
	private String jobid;

	@Bind
	@SaveState
	private String type;
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			try {
				jobid = AppUtils.getReqParam("jobid");
				type = AppUtils.getReqParam("type");
				editDataInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void refresh(){
		editDataInit();
	}

	public void editDataInit() {
		String ret = "''";
		ret = getJsonByJobid(Long.parseLong(jobid));
		jsonData = StrUtils.isNull(ret) ? "''" : ret;
		this.update.markUpdate(UpdateLevel.Data, "jsonData");
		Browser.execClientScript("initData();");
	}
	
	
	public String getJsonByJobid(Long jobid) {
		String sql = "SELECT f_bus_air_goods_bill('jobid="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS json";
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		//System.out.println(sql);
		if (map != null && 1 == map.size()) {
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}

	
	@Action
	private void saveAjaxSubmit() {
		try{
			StringBuilder sb = new StringBuilder();
			String dataMaster = AppUtils.getReqParam("dataMaster");
    		SqlObject sqlObject = new SqlObject("bus_air" , dataMaster , AppUtils.getUserSession().getUsercode());
    		sb.append(sqlObject.toSql());
    		String dataDetail = AppUtils.getReqParam("dataDetail");
    		sqlObject = new SqlObject("bus_goods" , dataDetail , AppUtils.getUserSession().getUsercode());
    		sb.append(sqlObject.toSqls());
    		
    		String dataBillMaster = AppUtils.getReqParam("dataBillMaster");
    		sqlObject = new SqlObject("bus_air_bill" , dataBillMaster , AppUtils.getUserSession().getUsercode());
    		sb.append(sqlObject.toSqls());
    		
			//System.out.println(sb.toString());
			daoIbatisTemplate.updateWithUserDefineSql(sb.toString());
			MessageUtils.alert("OK");
    		Browser.execClientScript("refreshJsVar.fireEvent('click');");
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
}
