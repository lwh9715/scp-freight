package com.ufms.web.view.ship;

import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;

@WebServlet("/ship")
@ManagedBean(name = "pages.module.ship.shippingBean")
public class ShippingBean extends BaseServlet {
	
	
	@Action(method="lockBill")
	public void rpsum(HttpServletRequest request){
		String id = request.getParameter("id");
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String dmlSql = "UPDATE bus_shipping SET isprint = true , dateprint = NOW(),userprint = 'system' WHERE id = "+id+";";
		daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
	}
	
	@Action(method="getWebSite")
	public BaseView getWebSite(HttpServletRequest request){
		BaseView view = new BaseView();
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String querySql = "SELECT w.url FROM bus_shipping s , sys_corporation c, sys_webfavorites w WHERE s.jobid = "+id+" AND s.carrierid = c.id AND w.linkkeys ILIKE '%'||(CASE WHEN position('(' IN c.code) > 0 THEN split_part(c.code ,'(', 1) ELSE c.code END)||'%' AND libtype = '"+type+"' LIMIT 1;";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		String webSite = StrUtils.getMapVal(map, "url");
		view.setMessage("OK");
		view.setData(webSite);
		view.setSuccess(true);
		return view;
	}
	
	@Action(method="checkMbl")
	public BaseView checkMbl(HttpServletRequest request){
		BaseView view = new BaseView();
		String jobid = request.getParameter("jobid");
		String mblno = request.getParameter("mblno");
		if(!StrUtils.isNull(mblno)){
			String sql = "SELECT COUNT(1) AS count FROM bus_shipping s , fina_jobs j WHERE s.mblno = '"+mblno+"' AND s.jobid = j.id AND j.id <> "+jobid+" AND s.isdelete = FALSE AND j.isdelete = FALSE AND j.parentid IS NULL ;";
			try {
				DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
				Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				Long count = Long.valueOf(StrUtils.getMapVal(map, "count"));
				if(count > 0){
					view.setMessage("");
					view.setData(StrUtils.getMapVal(map, "count"));
					view.setSuccess(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return view;
	}
}

