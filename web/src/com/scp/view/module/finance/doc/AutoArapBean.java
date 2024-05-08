package com.scp.view.module.finance.doc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsActset;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

/**
 * @author Neo
 *
 */
@ManagedBean(name = "pages.module.finance.doc.autoarapBean", scope = ManagedBeanScope.REQUEST)
public class AutoArapBean extends GridView {


	@Bind
	@SaveState
	public String genProcess;
	
	

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	@SaveState
	public String filtertype = "N";
	
	
	@SaveState
	public Long actsetid;
	
//	@SaveState
//	@Bind
//	public String year = "2017";
//	
//	@SaveState
//	@Bind
//	public String period = "1";
//	
	
	@SaveState
	@Bind
	public String yearOther = "2021";
	
	@SaveState
	@Bind
	public String periodOther = "1";
	
	@SaveState
	@Bind
	public String isGenerateOther = "N";
	
	@SaveState
	@Accessible
	public FsActset fsActset = new FsActset();
	
	
	private SqlMapClient sqlMapClient;


	@SaveState
	@Bind
	public Date startdate;
	
	@SaveState
	@Bind
	public Date enddate;
	
	@SaveState
	@Bind
	public Date startdate2;
	
	@SaveState
	@Bind
	public Date enddate2;
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			init();
			this.grid.reload();
		}
	}
	
	
	private void init(){
		//必须和新增一样 根据是否新增 或者 显示 进来 处理.否则会出现问题
		String sql = "SELECT year,period,basecy FROM fs_actset WHERE isdelete = FALSE AND id = "
				+ AppUtils.getUserSession().getActsetid();
		Map m;
		try {
			m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
		}finally{
			startdate=new Date();
			enddate=new Date();
			
			this.update.markUpdate(UpdateLevel.Data, "year");
			this.update.markUpdate(UpdateLevel.Data, "period");
			this.update.markUpdate(UpdateLevel.Data, "yearOther");
			this.update.markUpdate(UpdateLevel.Data, "periodOther");
		}
	}
	
	
	@Bind
	@SaveState
	public String qrydates = "jobdate";
	
	@Bind
	@SaveState
	public String isconfirm2_pp = "true";
	
	@Bind
	@SaveState
	public String isconfirm2_cc;


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
 		Map m = super.getQryClauseWhere(queryMap);
		m.put("actsetid", AppUtils.getUserSession().getActsetid());
		String qry = StrUtils.getMapVal(m, "qry");
		
		String startdatestr=new SimpleDateFormat("yyyy-MM-dd").format(startdate);
		String enddatestr=new SimpleDateFormat("yyyy-MM-dd").format(enddate);
		
		String confirmppcc = "";
		if(!StrUtils.isNull(isconfirm2_pp)){
			if(Boolean.parseBoolean(isconfirm2_pp)){
				confirmppcc += "\n AND isconfirm2_pp = TRUE ";
			}else{
				confirmppcc += "\n AND isconfirm2_pp = FALSE ";
			}
		}
		if(!StrUtils.isNull(isconfirm2_cc)){
			if(Boolean.parseBoolean(isconfirm2_cc)){
				confirmppcc += "\n AND isconfirm2_cc = TRUE ";
			}else{
				confirmppcc += "\n AND isconfirm2_cc = FALSE ";
			}
		}
		
		String returncntdate = "";
		if(null != startdate2 || null != enddate2){
			returncntdate = "\n  AND COALESCE(s.returncnttime,t.vgmdate) > '2017-01-01' ";
			if(null != startdate2){
				returncntdate += "\n  AND COALESCE(s.returncnttime,t.vgmdate)::date >= '"+new SimpleDateFormat("yyyy-MM-dd").format(startdate2)+"'";
			}
			if(null != enddate2){
				returncntdate += "\n  AND COALESCE(s.returncnttime,t.vgmdate)::date <= '"+new SimpleDateFormat("yyyy-MM-dd").format(enddate2)+"'";
			}
		}
		
		m.put("startdatestr", startdatestr);
		m.put("enddatestr", enddatestr);
		m.put("qrydates", qrydates);
		m.put("filtertype", filtertype);
		m.put("qry", qry);
		m.put("confirmppcc", confirmppcc);
		m.put("returncnttime", returncntdate);
		//System.err.println(qry);
		return m;
	}
	
	
	/*
	 * 生成到指定期间
	 */
	@Action
	public void autoGenerateOther() {
		if(StrUtils.isNull(yearOther)){
			this.alert("Year is null!");
			return;
		}
		if(StrUtils.isNull(periodOther)){
			this.alert("Period is null!");
			return;
		}
		isGenerateOther = "Y";
		this.autoGenerate();
	}

	/*
	 */
	@Action
	public void autoGenerate() {
		try {
			actsetid = AppUtils.getUserSession().getActsetid();
			if(actsetid == null) {
				MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
				return;
			}
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("Please choose one!");
				return;
			}
			String tips = generateVch(ids);
			if(StrUtils.isNull(tips)){
				MessageUtils.alert("OK");
			}else{
				MessageUtils.showException(new Exception(tips));
			}
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 按id生成，一条提交一次
	 * @param ids
	 * @throws Exception 
	 */
	private String generateVch(String[] ids){
		String tips = "";
		String usercode = AppUtils.getUserSession().getUsercode();
		sqlMapClient = (SqlMapClient) (this.serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient());  
		for (String id : ids) {
			try {
				generateVchByJobid(id);
			} catch (Exception e) {
				e.printStackTrace();
				tips += e.getLocalizedMessage();
			}
		}
		return tips;
	}
	
	private void generateVchByJobid(String id) throws Exception {
		String usercode = AppUtils.getUserSession().getUsercode();
		sqlMapClient = (SqlMapClient) (this.serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient());  
		try {  
		    sqlMapClient.startTransaction();  
		    String sql = "\nSELECT f_fs_arap2vch('actsetid="+actsetid+";jobid="+id+";usercode="+usercode+";yearother="+yearOther+";periodother="+periodOther+";isgenerateother="+isGenerateOther+"') WHERE NOT exists" +
		    		"\n(SELECT 1 FROM sys_dml_pool x where isperform = FALSE AND dmlsql ILIKE '%f_fs_arap2vch%' AND x.linkid ="+id.split("-")[0]+");";
		    sqlMapClient.getCurrentConnection().createStatement().executeQuery(sql);
		    sqlMapClient.commitTransaction();  
		    sqlMapClient.endTransaction();  
		}catch(Exception e){  
		    throw new Exception (e);  
		}finally{  
	        try {  
	            sqlMapClient.endTransaction();  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	            throw new RuntimeException(e);  
	        }  
		} 
		//MsgUtil.alert("OK");
	}
	
	/**
	 * 刷新方法
	 */
	@Action
	public void refresh() {
		if(grid != null){
			this.grid.reload();
		}
	}
	
	@Action
	public void qryRefresh() {
		if(grid != null){
			this.grid.reload();
		}
	}
	
//	/**
//	 * 一键生成当前期间所有凭证 ,如果已经生成了 就不重新生成(包括增减费用凭证)
//	 */
//	@Action
//	public void autoCreateAllVch(){
//		actsetid = AppUtils.getUserSession().getActsetid();
//		if(actsetid == null) {
//			MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
//			return;
//		}
//		String usercode = AppUtils.getUserSession().getUsercode();
//		String sql = "\nSELECT idindex , jobno FROM f_rpt_fs_getarapvch('actsetid="+actsetid+";year="+year+";period="+period+";filtertype=N;');";
//		try {
//			List<Map> lists = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//			
//			MessageUtils.alert("总计单数：" + lists.size() + " 请耐心等待，请先处理其他事情!");
//			for (Map map : lists) {
//				String id = StrUtils.getMapVal(map, "idindex");
//				String jobno = StrUtils.getMapVal(map, "jobno");
//				//generateVchByJobid(id);
//				String qry = "\nSELECT f_fs_arap2vch(''actsetid="+actsetid+";jobid="+id+";usercode="+usercode+"'');";
//				String dml = "\nSELECT f_sys_dml_pool_into('"+qry+"' , "+actsetid+" , '"+jobno+"' , 'A');";
//				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(dml);
//			}
////			MsgUtil.alert("OK");
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
//		
//	}

	/**
	 * 一键生删除当前期间所有凭证 ,(包括增减费用凭证)
	 */
	@Action
	public void autoCancelAllVch(){
		actsetid = AppUtils.getUserSession().getActsetid();
		if(actsetid == null) {
			MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "\nSELECT f_fs_arap2vch_all('actsetid="+actsetid+";year="+this.yearOther+";period="+this.periodOther+";usercode="+usercode+";type=cancel');";
		try {
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIIFrame iframeConfig;
	
	@Bind
	public UIWindow ConfigWindow;
	
	@Action
	public void config() {
		iframeConfig.load("configarap.xhtml");
		this.ConfigWindow.show();
	}
	
	
	public Date firstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设定当前时间为每月一号
		return calendar.getTime();
	}
	
	public Date lastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设定当前时间为每月一号
		// 当前日历的天数上-1变成最大值 , 此方法不会改变指定字段之外的字段
		calendar.roll(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}
	
	
}
