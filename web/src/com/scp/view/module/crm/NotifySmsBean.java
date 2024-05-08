package com.scp.view.module.crm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.BusShipping;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.ufms.base.utils.SmsUtil;

@ManagedBean(name = "pages.module.crm.notifysmsBean", scope = ManagedBeanScope.REQUEST)
public class NotifySmsBean extends GridView {


	@Bind
	private Long userId;
	
	
	@Bind
	@SaveState
	private String jobno;
	
	@Bind
	@SaveState
	private String status;
	
	@Bind
	@SaveState
	private String billno;
	
	@Bind
	@SaveState
	private String pod;
	
	@Bind
	@SaveState
	private String srctype;
	
	@Bind
	@SaveState
	private String jobid;
	
	@Bind
	@SaveState
	private String tel;
	
	
	@SaveState
	private Map<String,String> telsMap = new HashMap<String, String>();
	
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.tel = "";
			telsMap.clear();
			userId = AppUtils.getUserSession().getUserid();
			
			srctype = AppUtils.getReqParam("srctype");
			String tips = AppUtils.getReqParam("tips");
			jobid = AppUtils.getReqParam("jobid");
			
			tips = "状态:" + tips;
			status = tips;
			
			if(!StrUtils.isNull(jobid)){
				try {
					String jobinfo = "";
					BusShipping busShipping = this.serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid = " + jobid + " AND isdelete = FALSE");
					billno = busShipping.getHblno();
					pod = busShipping.getPod();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			tips = tips.replaceAll("\n", " , ");
			update.markUpdate(true,UpdateLevel.Data, "editPanel");
		}
	}
	
	
	@Override
	public void grid_onrowselect() {
		String[] ids = grid.getSelectedIds();
		for (String id : ids) {
			telsMap.put(id, id);
		}
		this.tel = "";
		Set<String> set = telsMap.keySet();
		for (String key : set) {
			this.tel += key + ";";
		}
		
		update.markUpdate(true,UpdateLevel.Data, "tel");
	}
	
	@Action
	public void sendSms() {
		if (StrUtils.isNull(billno) || StrUtils.isNull(status) || StrUtils.isNull(pod)) {
				this.alert("不能为空!");
				return ;
		}
		
		if (StrUtils.isNull(tel)) {
			this.alert("手机号为空!");
			return ;
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		String[] tels = tel.split(";");
		for (String tel : tels) {
	    	try {
	    		if(!StrUtils.isNull(tel)){
	    			String response = SmsUtil.goodsTrack(tel, billno, status, pod);
					stringBuilder.append(response + "\n");
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.alert("OK");
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		String filter = "\nAND (EXISTS"
			+ "\n				(SELECT "
			+ "\n					1 "
			+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
			+ "\n				WHERE y.custlibid = z.custlibid "
			+ "\n				AND y.corpid = c.customerid "
			+ "\n				AND z.userid = "
			+ AppUtils.getUserSession().getUserid()
			+ ")"
			
			//neo 20170928 增加组关联业务员提取条件 begin--------------------------
			+"\n			OR EXISTS(SELECT 1"
			+"\n					FROM sys_custlib x , sys_custlib_role y "
			+"\n					WHERE y.custlibid = x.id "
			+"\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid) "
			+"\n					AND x.libtype = 'S' "
			+"\n					AND EXISTS(SELECT 1 FROM sys_user z , sys_corporation zz where z.id = x.userid AND z.isdelete = false AND z.id = zz.salesid AND zz.id = c.customerid)"
			+"\n				)"
			+"\n		)"
			//neo 20170928 增加组关联业务员提取条件end--------------------------
			;
		
		if("goodstrack".equals(srctype)){
			filter = "\nAND (CASE WHEN c.customerid = 0 THEN " +
							"\n 	c.code = ANY(SELECT DISTINCT z.code FROM sys_user_assign x , bus_shipping y , sys_user z WHERE y.id = x.linkid AND z.id = x.userid AND y.jobid = "+jobid+")" +
							"\nELSE " +
							"\n	c.customerid = ANY(SELECT x.customerid FROM fina_jobs x WHERE x.id = "+jobid+")" +
							"\nEND)";
		}else{
			
		}
		
		m.put("filter", filter);
		return m;
	}

}
