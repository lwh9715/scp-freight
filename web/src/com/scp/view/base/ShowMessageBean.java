package com.scp.view.base;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.Register;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "web.showmessageBean", scope = ManagedBeanScope.REQUEST)
public class ShowMessageBean extends GridView {

	@SaveState
	@Accessible
	public Register selectedRowData = new Register();
	
	@Bind
	@SaveState
	@Accessible
	public String billnos;
	
	@Bind
	@SaveState
	@Accessible
	public String cntno;
	
	@Bind
	@SaveState
	@Accessible
	public String time;
	
	@Bind
	@SaveState
	@Accessible
	public String eta;
	
	@Bind
	@SaveState
	@Accessible
	public String phone;

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			billnos = AppUtils.getReqParam("nos").trim();
			if(billnos == null){
				return;
			}
			 Map map = getBillOjbect(billnos);
			 if(map ==null){
				 return;
			 }
			 cntno = this.getCntno(map);
			 phone = this.getPhone(map);
			 if(this.getMap(map) == null){
				 time = "";
				 eta = "";
			 }else{
				  Map map2 = this.getMap(map);
				  time = (String)map2.get("tctime");
				  eta = (String)map2.get("eta");
			 }
			 this.update.markUpdate(UpdateLevel.Data, "billnos");
			 this.update.markUpdate(UpdateLevel.Data, "cntno");
			 this.update.markUpdate(UpdateLevel.Data, "time");
			 this.update.markUpdate(UpdateLevel.Data, "eta");
			 this.update.markUpdate(UpdateLevel.Data, "phone");
		}
	}
	
	public String getCntno(Map map){
		try {
			String sql = "SELECT  (string_agg(cntno,','))AS cntno  FROM bus_ship_container WHERE isdelete = FALSE AND billid = " + (Long)map.get("id");
			 Map map2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			return (String)map2.get("cntno");
		} catch (Exception e) {
			return "";
		}
	}
	
	public Map getMap(Map map){
		try {
			String sql = "SELECT  (to_char(x.eta,'YYYY年MM月DD日'))AS eta  " +
						 ",(SELECT string_agg((CASE  WHEN  t.loadtime IS NULL THEN '' ELSE to_char(t.loadtime,'YYYY年MM月DD日')END),',') FROM bus_truck t WHERE t.jobid = x.jobid AND t.isdelete = FALSE)AS tctime " +
						 " FROM bus_shipping x WHERE x.jobid = "+(Long)map.get("jobid")+" AND x.isdelete = FALSE ";
			Map map2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			return map2;
		} catch (Exception e) {
			Map map2=new HashMap();
			map2.put("eta", " ");
			map2.put("tctime", " ");
			return map2;
		}
	}
	/**
	 *查询迪拜客服电话 
	 */
	public String getPhone(Map map){
		try {
			String sql = "SELECT (string_agg((CASE WHEN tel1 IS NULL OR tel1 = '' THEN tel2 ELSE tel1 END),','))AS tel FROM sys_user s,sys_user_assign t ,bus_shipping b WHERE s.isdelete = FALSE AND b.isdelete = FALSE AND s.id = t.userid AND t.linkid = b.id AND b.jobid =  "+ map.get("jobid");
			Map map2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			 String tel = (String)map2.get("tel");
			return tel;
		} catch (Exception e) {
			return "";
		}
	}
	
	public Map getBillOjbect(String billnos ){
		try {
			String sql = "SELECT * FROM bus_ship_bill WHERE isdelete = FALSE AND hblno = '" + billnos + "'";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			return map;
		} catch (Exception e) {
			return null;
		}
	}
}
