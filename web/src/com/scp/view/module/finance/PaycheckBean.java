package com.scp.view.module.finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.service.finance.JobsMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.finance.paycheckBean", scope = ManagedBeanScope.REQUEST)
public class PaycheckBean extends GridView{
	
	@ManagedProperty("#{jobsMgrService}")
	public JobsMgrService jobsMgrService;
	
	@SaveState
	private boolean isShowJoin = true;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			applyGridUserDef1();
			this.userid = AppUtils.getUserSession().getUserid();
			//Browser.execClientScript("getNowFormatDate()");
			customerQry();
			gridLazyLoad = true;
		}
	}
	
	protected void applyGridUserDef1() {
		String gridid  = this.getMBeanName() + ".clientGrid";
		String gridJsvar = "clientGridJsvar";
		applyGridUserDef(gridid , gridJsvar);
	}
	
	@SaveState
	private String jobid;
	
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);		
		if (isShowJoin) {// 显示合计，不显示明细
			m.put("isShowJoin", "(b.parentid IS NULL)");
		} else {// 显示明细，不显示合计
			m.put("isShowJoin", "(b.rptype <> 'O')");
		}
		
		
		String filterJobs = "";
		if(!StrUtils.isNull(jobid)){
			this.qryMap.put("jobid$", jobid);
			// neo 控制费用显示
			filterJobs = "EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"') x WHERE x.id = b.id)";
		}else{
			this.qryMap.put("jobid$", -1L);
			filterJobs = " FALSE";
		}
		
		m.put("filter", filterJobs);
		
		return m;
	}
	
	@Bind
	public UIEditDataGrid clientGrid;
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return getJobsProvider();
	}
	
	@Action
	public void clientGrid_onrowselect() {
		String id = clientGrid.getSelectedIds()[0];

		jobid = id.split("-")[0];
		this.qryMap.put("jobid$", jobid);
		this.grid.reload();
		String url = AppUtils.getContextPath() + "/pages/module/finance/paycheckdtl.xhtml?id=" +jobid;
		dtlIFrame.load(url);
		this.update.markUpdate(UpdateLevel.Data, "dtlIFrame");
		
	}
	
	@Bind(id = "customerap")
	public List<SelectItem> getCustomerap() {
		try {
			return CommonComBoxBean.getComboxItems("d.id", "d.abbr",
					"sys_corporation AS d", " WHERE  isdelete = FALSE AND iscustomer = true",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	@SaveState
	public Long userid;	
	
	@Action
	public void paycheckJobs() {
		String rpturl = AppUtils.getRptUrl();
		String custom = AppUtils.getReqParam("custom");
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/finance/paycheck.raq";
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}
	
	private String getArgs() {
		String args = "";
		String custom = AppUtils.getReqParam("custom");
		String checkdatestart = AppUtils.getReqParam("checkstar");
		String checkdateend = AppUtils.getReqParam("checkend");
		String payplace = AppUtils.getReqParam("payplace");
		args += "&userid=" + AppUtils.getUserSession().getUserid();
		args += "&checkdatestart=" + (StrUtils.isNull(checkdatestart)?"0001-01-01": parseTime(checkdatestart));
		args += "&checkdateend=" + (StrUtils.isNull(checkdateend)?"9999-12-31": parseTime(checkdateend));
		args += "&custom=" + (StrUtils.isNull(custom)?-1l:custom);
		args += "&payplace=" + payplace;
		return args;
		
	}     
	
	
	@SaveState
	private String qrySql = "\nAND 1=1";
	
	public GridDataProvider getJobsProvider() {
		return new GridDataProvider() {
			String args = ";paychecktimestart="+qrychecktime+";paychecktimend="+qrychecktimeto+";hblno="+qryhbl+";idcode="+qryidcode+";jobno="+qrynos;
			@Override
			public Object[] getElements() {
				if(gridLazyLoad){
					return new Object[]{};
				}
				String qry = "\nAND s.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
				//非saas模式不控制
				String querySql = 
					"\nSELECT * from f_rpa_fina_jobs_paycheck('userid="+AppUtils.getUserSession().getUserid()+";srctype=PAYCHECK"+args+"') AS v" +
					"\nwhere 1 = 1" +
					 qrySql +
					 (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					 ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"") +//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
					"\nORDER BY v.nos,v.saleuser " +
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					//System.out.println(querySql);
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				if(gridLazyLoad){
					return 0;
				}
				//return 100000;
				String qry = "\nAND s.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
				//非saas模式不控制
				String countSql = 
					"\nSELECT COUNT(1) AS counts from f_rpa_fina_jobs_paycheck('userid="+AppUtils.getUserSession().getUserid()+";srctype=PAYCHECK"+args+"') AS v" +
					"\nwhere 1 = 1" +
					 qrySql +
					 (((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()?qry:"") +
					 ("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))?qry:"");//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条;
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	
	@Bind
	@SaveState
	private String qryhbl;
	
	@Bind
	@SaveState
	private String qrynos;
	
	@Bind
	@SaveState
	private String qryidcode;
	
	private String popQryKey;
	
	@Bind
	@SaveState
	private String qrychecktime = getdatestar();
	
	@Bind
	@SaveState
	private String qrychecktimeto =getdateto();
	
	@Bind
	@SaveState
	private String qryispayagree;
	
	@Bind
	@SaveState
	private String qryiscomplete;
	
	
	@Action
	public void customerQry(){
		//qry(qryhbl,qrychecktime,qrychecktimeto,qrynos,qryidcode,qryispayagree);
		this.gridLazyLoad = false;
		qrybefore(qryhbl,qrynos);
		this.clientGrid.reload();
	}
	
	
	public void qrybefore(String hblno, String nos){
		if(StrUtils.isNull(hblno) && StrUtils.isNull(nos)){
			qry(qryhbl,qrychecktime,qrychecktimeto,qrynos,qryidcode,qryispayagree,qryiscomplete);
			return;
		}
		if(!StrUtils.isNull(hblno)){
			hblno = hblno.trim().replaceAll("'", "''").toUpperCase();
			this.qrySql = "\nAND v.hbl ILIKE '%"+hblno+"%'";
		}
		if(!StrUtils.isNull(nos)){
			nos = nos.trim().replaceAll("'", "''").toUpperCase();
			this.qrySql = "\nAND  v.nos ILIKE '%"+nos+"%'";
		}
		try {
			String args = ";paychecktimestart="+qrychecktime+";paychecktimend="+qrychecktimeto+";hblno="+qryhbl+";idcode="+qryidcode+";jobno="+qrynos;
			String sql = "SELECT COUNT(*) AS count FROM f_rpa_fina_jobs_paycheck('userid="+AppUtils.getUserSession().getUserid()+";srctype=PAYCHECK"+args+"') AS v where 1=1 "+qrySql+"";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			Long num = (Long) m.get("count");
			if(num > 0){
				qry(qryhbl,qrychecktime,qrychecktimeto,qrynos,qryidcode,qryispayagree,qryiscomplete);
			}else{
				String sql2 = "SELECT * FROM f_fina_jobs_paycheck_qrybefore('nos="+qrynos+";hblno="+qryhbl+"') AS information";
				Map m2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2);
				String informationText = m2.get("information").toString();
				if(!StrUtils.isNull(informationText)){
					MessageUtils.alert(informationText);
					return;
				}
			}
		} catch (Exception e) {
			MessageUtils.alert("找不到该号码信息！");
		}
		
	}
	
	public void qry(String popQryKey,String timestr, String timeend, String nos,String idcode,String ispaygree,String iscomplete) {
		if(StrUtils.isNull(popQryKey) && StrUtils.isNull(timestr) && StrUtils.isNull(timeend) && StrUtils.isNull(nos) && StrUtils.isNull(idcode) && StrUtils.isNull(ispaygree) && StrUtils.isNull(iscomplete)){
			this.qrySql = "\nAND 1=1";
			return;
		}

		this.qrySql = "\nAND 1=1";
		
		if(!StrUtils.isNull(timestr) || !StrUtils.isNull(timeend)){
			this.qrySql += "\nAND v.paychecktime::DATE BETWEEN '"+ qrychecktime + "' AND '"+ qrychecktimeto + "'";
		}
		if(!StrUtils.isNull(popQryKey)){
			this.popQryKey = popQryKey;
			this.popQryKey = this.popQryKey.trim();
			this.popQryKey = this.popQryKey.replaceAll("'", "''");
			this.popQryKey = this.popQryKey.toUpperCase();
			this.qrySql += "\nAND v.hbl ILIKE '%"+this.qryhbl+"%'";
		}
		if(!StrUtils.isNull(nos)){
			this.qrynos = nos;
			this.qrynos = this.qrynos.trim();
			this.qrynos = this.qrynos.replaceAll("'", "''");
			this.qrynos = this.qrynos.toUpperCase();
			this.qrySql += "\nAND  v.nos ILIKE '%"+this.qrynos+"%'";
		}
		
		if(!StrUtils.isNull(idcode)){
			this.qryidcode = idcode;
			this.qryidcode = this.qryidcode.trim();
			this.qryidcode = this.qryidcode.replaceAll("'", "''");
			this.qrySql += "\nAND  v.idcode ILIKE '%"+this.qryidcode+"%'";
		}
		
		if("Y".equals(ispaygree)){
			this.qrySql += "\nAND  v.ispayagree = TRUE";
		}else if("N".equals(ispaygree)){
			this.qrySql += "\nAND (v.ispayagree = FALSE OR v.ispayagree is null)";
		}
		
		if("Y".equals(iscomplete)){
			this.qrySql += "\nAND  v.iscomplete = TRUE";
		}else if("N".equals(iscomplete)){
			this.qrySql += "\nAND v.iscomplete = FALSE";
		}
		
	}
	
	
	@Bind(id = "clientGrid", attribute = "modifiedData")
    @SaveState
	protected Object modifiedData;
	
	@Action(id = "update")
    protected void update(Object obj) {
		String username = AppUtils.getUserSession().getUsername();
		jobsMgrService.updateBatchEditGrid(obj,username);
    }
	
	@Bind
	public int rowsnumber;
	
	@Action(id = "save")
    public void save() {
        try {
            if (modifiedData != null) {
                update(modifiedData);
            }
            clientGrid.commit();
            //clientGrid.reload();
            //Browser.execClientScript("gotorow();");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }
	
	public String getdateto(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String str = sdf.format(date);
        return str;
	}
	
	public String getdatestar(){
		Date date = new Date();
		date.setMonth(date.getMonth()-1); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String str = sdf.format(date);
        return str;
	}
	
	public static String parseTime(String datdString) {
	    datdString = datdString.replace("GMT", "").replaceAll("\\(.*\\)", "");
	    //将字符串转化为date类型，格式2016-10-12
	    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
	    Date dateTrans = null;
	    try {
	        dateTrans = format.parse(datdString);
	        return new SimpleDateFormat("yyyy-MM-dd").format(dateTrans);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return datdString;

	}
	
	
	/**
	 * grid在前台设置每页显示的行数
	 */
	@Action
	public void doChangeClientGridPage() {
		String pageStr = (String)AppUtils.getReqParam("page");
		if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
			//alert("pageStr:"+pageStr);
			Integer page = Integer.parseInt(pageStr);
			this.clientGrid.setRows(page);
			//gridLazyLoad = false;
			this.clientGrid.rebind();
//			//记录选择的行数到个人设置
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId+".clientGrid.pagesize";
			try {
//				CfgUtil.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				applyGridUserDef();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}
	
	
	public Integer gridPageSize = 100;
	
	/**
	 * 由个人设置中提取行数，若找不到则返回默认的100行
	 * 
	 * @return
	 */
	public Integer getGridPageSize() {
		String mbeanId = this.getMBeanName();
		String girdId = mbeanId + ".clientGrid.pagesize";
		String pageSize;
		try {
			pageSize = ConfigUtils.findUserCfgVal(girdId, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
			return gridPageSize;
		}
		if (!StrUtils.isNull(pageSize) && StrUtils.isNumber(pageSize)) {
			Integer page = Integer.parseInt(pageSize);
			gridPageSize = page;
			return page;
		} else {
			return gridPageSize;
		}
	}
}
