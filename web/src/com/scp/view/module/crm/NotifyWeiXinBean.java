package com.scp.view.module.crm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.JSONUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.ufms.base.wx.WeiXinUtil;

@ManagedBean(name = "pages.module.crm.notifyweixinBean", scope = ManagedBeanScope.REQUEST)
public class NotifyWeiXinBean extends GridView {


	@Bind
	private Long userId;
	
	
	@Bind
	@SaveState
	private Date tipTime;
	
	@Bind
	@SaveState
	private String tipContent;
	
	@Bind
	@SaveState
	private String jobno;
	
	@Bind
	@SaveState
	private String status;
	
	@Bind
	@SaveState
	private Date times;
	
	@Bind
	@SaveState
	private String processer;
	
	@Bind
	@SaveState
	private String contact;
	
	@Bind
	@SaveState
	private String qryKey;
	
	@Bind
	@SaveState
	private String template = "goodstrack";
	
	@SaveState
	@Accessible
	public FinaJobs job = new FinaJobs();
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			userId = AppUtils.getUserSession().getUserid();
			tipTime = Calendar.getInstance().getTime();
			times = tipTime;
			processer = AppUtils.getUserSession().getUsername();
			contact = this.serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid()).getTel1();
			String srctype = AppUtils.getReqParam("srctype");
			String tips = AppUtils.getReqParam("tips");
			String jobid = AppUtils.getReqParam("jobid");
			
			if("goodstrack".equals(srctype)){
				//tips = "状态:" + tips;
				status = tips;
			}
			
			String jobinfo = "";
			if(!StrUtils.isNull(jobid)){
				try {
					String sql = "SELECT * FROM f_bus_goodstrack_weixin('jobid=" + jobid + ";tips=" + tips + "') AS information";
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					jobinfo = m.get("information").toString();
					//tips += "\n" + jobinfo;
					jobno = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid)).getNos();
					this.job = serviceContext.jobsMgrService.finaJobsDao.findById(Long.valueOf(jobid));
				} catch (Exception e) {
				}
			}
			
			tips = tips.replaceAll("\n", " , ");
			
			tipContent = jobinfo;
			update.markUpdate(true,UpdateLevel.Data, "editPanel");
		}
	}
	
	@Action
	public void sendWeiXin() {
		if(template.equals("goodstrack")){
			if (StrUtils.isNull(tipContent)) {
				this.alert("内容不能为空!");
				return ;
			}
		}else{
			if (StrUtils.isNull(jobno) || StrUtils.isNull(status)) {
				this.alert("不能为空!");
				return ;
			}
		}
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("请选择发送方!");
			return ;
		}
		String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
		String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
		
		String cs_url_base = ConfigUtils.findSysCfgVal("cs_url_base");
		
		StringBuilder stringBuilder = new StringBuilder();
		for (String openid : ids) {
	    	try {
	    		String json1 = "";
				if(template.equals("goodstrack")){
					String weixinTemplatejobs = ConfigUtils.findSysCfgVal("weixin_template_goodstrack");
					json1 = JSONUtil.sendWeixinTemplate(cs_url_base,openid, weixinTemplatejobs, "货物消息提醒"
							, DateTimeUtil.getFormatedDateString(null, tipTime)
							, tipContent
							, null
							, null
							, null
							, null);
				}else{
					String weixinTemplatejobs = ConfigUtils.findSysCfgVal("weixin_template_jobs");
					json1 = JSONUtil.sendWeixinTemplate(cs_url_base,openid, weixinTemplatejobs, "工作单进度"
							, jobno
							, status
							, DateTimeUtil.getFormatedDateString(null, times)
							, processer
							, contact
							, null);
				}
				//System.out.println(json1);
				String response = WeiXinUtil.messageSendByTemplate(appID, appsecret, json1);
				stringBuilder.append(response + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.alert("OK");
	}
	
	@Bind
	@SaveState
	private String filterKey;
	
	@Action
	public void filterJobsAssign() {
		qryKey = "";
		filterKey = "\nAND c.types = '系统用户' AND c.userid = ANY(SELECT x.userid FROM sys_user_assign x , bus_shipping y WHERE x.linkid = y.id AND y.jobid = "+this.job.getId()+")";
		this.grid.reload();
	}
	
	
	@Action
	public void filterCustomer() {
		qryKey = "";
		filterKey = "\nAND c.types = '客户账号' AND c.customerid = ANY(SELECT x.customerid FROM fina_jobs x WHERE x.id = "+this.job.getId()+")";
		this.grid.reload();
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if (this.job != null&& (job.getParentid()==null)){
			qry += "\nAND  (CASE WHEN EXISTS(SELECT 1 FROM fina_jobs WHERE isdelete = false and parentid = "+this.job.getId()+") THEN (c.customerid = ANY(select customerid from fina_jobs where isdelete = false and parentid = "+this.job.getId()+") OR c.types ='系统用户')  ELSE 1=1  END) ";
		}else{
			qry += "\nAND 1=1 ";
		}
		
		if(!StrUtils.isNull(qryKey)){
			String filter = "\nAND (code ILIKE '%"+qryKey+"%' OR namec ILIKE '%"+qryKey+"%' OR types ILIKE '%"+qryKey+"%' OR customername ILIKE '%"+qryKey+"%')";
			m.put("filter", filter);
		}
		if(!StrUtils.isNull(filterKey)){
			m.put("filter", filterKey);
		}
		
		m.put("qry", qry);
		return m;
	}

}
