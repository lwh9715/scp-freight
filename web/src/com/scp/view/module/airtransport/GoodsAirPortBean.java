package com.scp.view.module.airtransport;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
import com.ufms.base.db.SqlObject;

@ManagedBean(name = "pages.module.airtransport.goodsairportBean", scope = ManagedBeanScope.REQUEST)
public class GoodsAirPortBean extends FormView {

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
		String sql = "SELECT f_bus_air_goods_airport('jobid="+jobid+";userid="+AppUtils.getUserSession().getUserid()+"') AS json";
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
    		
    		String refresh = "\nUPDATE bus_air SET piece4 = piece2,weight4 = weight2,volwgt4 = volwgt2,volume4 = volume2,chargeweight4 = chargeweight2 WHERE jobid = " + jobid + ";";
    		
			//System.out.println(sb.toString() + refresh);
			daoIbatisTemplate.updateWithUserDefineSql(sb.toString() + refresh);
			MessageUtils.alert("OK");
    		Browser.execClientScript("refreshJsVar.fireEvent('click');");
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.airtransport.goodsairportBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.airtransport.goodsairportBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(qryuserdesc) ){
			qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
			qryuserdesc = qryuserdesc.toUpperCase();
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";

				if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='"
								+ dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index)
									+ "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							buffer.append("\nAND UPPER(" + key
									+ ") LIKE UPPER('%'||" +"TRIM('"+ val+"')" + "||'%')");
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		map.put("limit", limits+"");
		map.put("start", starts+"");
		map.put("qry", qry);
		return map;
	}
	
	
	@Action
	public void clearQryKeysc() {
		if (qryMapUser != null) {
			qryMapUser.clear();
			update.markUpdate(true, UpdateLevel.Data, "userPanel");
			this.gridUser.reload();
		}
	}

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Bind
	@SaveState
	public String salesid = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.salesid.contains(id)){
				this.salesid = salesid + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		Browser.execClientScript("$('#arranger').val('"+this.user+"')");
		//update.markUpdate(true, UpdateLevel.Data, "usercfgpanel");
		//Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Action
	public void empty() {
		this.salesid = "";
		this.user = "";
		Browser.execClientScript("$('#arranger').val('"+this.user+"')");
		//update.markUpdate(true, UpdateLevel.Data, "usercfgpanel");
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirm();
	}
	
}
