package com.scp.view.module.finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.costconfirmBean", scope = ManagedBeanScope.REQUEST)
public class CostConfirm extends GridView {

	@Bind
	@SaveState
	public Boolean iscare = false;
	
	@Bind
	@SaveState
	public String customerabbr;

	@Bind
	public UIWindow batchWindow;
	
	@Bind
	@SaveState
	public Date ymdate = new Date();
	
	@Bind
	@SaveState
	public Date ymddate  = new Date();
	
	@Bind
	@SaveState
	public Date ymddateto = new Date();
	
	@Bind
	@SaveState
	public String batchDate;
	
	@Bind
	@SaveState
	public Date queryymdate;
	
	@Bind
	@SaveState
	public Date queryymddate;
	
	@Bind
	@SaveState
	public Date queryymddateto;
	
	@Bind
	@SaveState
	public String querybatchDate;
	
	@Bind
	@SaveState
	public String qrydates;
	
	@Bind
	@SaveState
	public String tradeway;
	
	@Bind
	@SaveState
	public String qryppfinance;
	
	@Bind
	@SaveState
	public String qryccfinance;
	
	@Bind
	@SaveState
	public String arFinished;
	
	@Bind
	@SaveState
	public String apFinished;

	@Bind
	@SaveState
	public String jobStatus;
	
	@SaveState
	private Long jobid;

	@Bind
	@SaveState
	private Long corpid = AppUtils.getUserSession().getCorpid();
	
	@Bind
	@SaveState
	private Long querycorpid = AppUtils.getUserSession().getCorpid();
	
	@Bind
	@SaveState
	private Long querycorpidop;
	
	// @Action
	// public void clearQryKeyArap() {
	// if(qryMapArap != null){
	// qryMapArap.clear();
	//
	// update.markUpdate(true, UpdateLevel.Data, "gridPanel");
	// this.gridArap.reload();
	// }
	// }

	@Bind
	public UIIFrame arapIframe;

	@SaveState
	private boolean showArapEdit;

	@Bind
	@SaveState
	private String dates = "jobdate";
	
	@Bind
	@SaveState
	public Date lockJobsDate;
	
	@Bind
	@SaveState
	public Date lockEtdDate;
	
	@Bind
	@SaveState
	public Date amenddate;
	
	@Bind
	@SaveState
	public Date completedate;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();

			gridLazyLoad = true;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String lockJobsDateStr = ConfigUtils.findSysCfgVal("lock_jobsdate");
				if(!StrUtils.isNull(lockJobsDateStr)){
					lockJobsDate = sdf.parse(lockJobsDateStr);
				}
				String lockEtdDateStr = ConfigUtils.findSysCfgVal("lock_etddate");
				if(!StrUtils.isNull(lockEtdDateStr)){
					lockEtdDate = sdf.parse(lockEtdDateStr);
				}
				String lockAmendDateStr = ConfigUtils.findSysCfgVal("lock_amenddate");
				if(!StrUtils.isNull(lockAmendDateStr)){
					amenddate = sdf.parse(lockAmendDateStr);
				}
				String lockCompletedateStr = ConfigUtils.findSysCfgVal("lock_completedate");
				if(!StrUtils.isNull(lockCompletedateStr)){
					completedate = sdf.parse(lockCompletedateStr);
				}
				this.update.markUpdate(UpdateLevel.Data, "lockJobsDate");
				this.update.markUpdate(UpdateLevel.Data, "lockEtdDate");
				this.update.markUpdate(UpdateLevel.Data, "amenddate");
				this.update.markUpdate(UpdateLevel.Data, "completedate");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Action
	private void saveDate() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(lockJobsDate!=null){
				String lockJobsDateStr = sdf.format(lockJobsDate);
				ConfigUtils.refreshSysCfg("lock_jobsdate", lockJobsDateStr, AppUtils.getUserSession().getUserid());
			}else{
				ConfigUtils.refreshSysCfg("lock_jobsdate", "", AppUtils.getUserSession().getUserid());
			}
			if(lockEtdDate!=null){
				String lockEtdDateStr = sdf.format(lockEtdDate);
				ConfigUtils.refreshSysCfg("lock_etddate", lockEtdDateStr, AppUtils.getUserSession().getUserid());
			}else{
				ConfigUtils.refreshSysCfg("lock_etddate", "", AppUtils.getUserSession().getUserid());
			}
			if(amenddate!=null){
				String lockEtdDateStr = sdf.format(amenddate);
				ConfigUtils.refreshSysCfg("lock_amenddate", lockEtdDateStr, AppUtils.getUserSession().getUserid());
			}else{
				ConfigUtils.refreshSysCfg("lock_amenddate", "", AppUtils.getUserSession().getUserid());
			}
			if(completedate!=null){
				String lockEtdDateStr = sdf.format(completedate);
				ConfigUtils.refreshSysCfg("lock_completedate", lockEtdDateStr, AppUtils.getUserSession().getUserid());
			}else{
				ConfigUtils.refreshSysCfg("lock_completedate", "", AppUtils.getUserSession().getUserid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Action
	public void showArapEdit() {
		if (this.jobid == null) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			arapIframe.load(blankUrl);
		} else {
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao
					.findById(this.jobid);
			arapIframe.load("../finance/arapedit.xhtml?customerid="
					+ finaJobs.getCustomerid() + "&jobid=" + this.jobid);
			// showArapEdit = true;
		}
	}


	/**
	 * 工作单完成
	 */
	@Action
	public void singlejobsComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();

			//工作单完成，检查往来是否一致，不一致跳过该票
			String wlsql = " AND (SELECT (SELECT COUNT(1) AS c\n" +
					"        FROM (SELECT t.corpid,t.customerid,t.currency,COALESCE(t.amount, 0) + COALESCE(t2.amount, 0) AS amount\n" +
					"              FROM (SELECT a.corpid,customerid,currency,sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
					"                    FROM fina_arap a\n" +
					"                    WHERE a.jobid = t.id\n" +
					"                      AND a.isdelete = FALSE\n" +
					"                      AND a.rptype != 'O'\n" +
					"                      AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
					"                    GROUP BY a.corpid, customerid, currency) t\n" +
					"                       LEFT JOIN (SELECT a.corpid,customerid,currency,sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
					"                                  FROM fina_arap a\n" +
					"                                  WHERE a.jobid = t.id\n" +
					"                                    AND a.isdelete = FALSE\n" +
					"                                    AND a.rptype != 'O'\n" +
					"                                    AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
					"                                  GROUP BY a.corpid, customerid, currency) t2 ON (t2.customerid = t.corpid AND t2.corpid = t.customerid AND t2.currency = t.currency)\n" +
					"             ) TT\n" +
					"        WHERE TT.amount != 0) = 0)";

			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs t SET iscomplete = TRUE ,updatetime = NOW(),updater = '" + updater + "' WHERE t.id =" + jobid + wlsql + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 工作单完成--强制完成
	 */
	@Action
	public void jobsComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET iscomplete = TRUE ,updatetime = NOW(),updater = '" + updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 工作单取消完成
	 */
	@Action
	public void jobsUnComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET iscomplete = FALSE ,updatetime = NOW(),updater = '" + updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * pp客服确认
	 */
	@Action
	public void ppConfirm() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET isconfirm_pp = TRUE ,confirm_pp_time = NOW(),confirm_pp_user = '"
						+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * pp客服取消
	 */
	@Action
	public void ppCancel() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET isconfirm_pp = FALSE ,confirm_pp_time = NOW(),confirm_pp_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * pp财务确认
	 */
	@Action
	public void ppConfirm2() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET isconfirm2_pp = TRUE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
						+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * pp财务取消
	 */
	@Action
	public void ppCancel2() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET isconfirm2_pp = FALSE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * cc客服确认
	 */
	@Action
	public void ccConfirm() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET isconfirm_cc = TRUE ,confirm_cc_time = NOW(),confirm_cc_user = '"
						+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * cc客服取消
	 */
	@Action
	public void ccCancel() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET isconfirm_cc = FALSE ,confirm_cc_time = NOW(),confirm_cc_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * cc财务确认
	 */
	@Action
	public void ccConfirm2() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET isconfirm2_cc = TRUE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
						+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * cc财务取消
	 */
	@Action
	public void ccCancel2() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET isconfirm2_cc = FALSE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if (!iscare) {
			m.put("icare", "1=1");
		} else {
			String sql = "(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
					+ AppUtils.getUserSession().getUserid() + ")";
			sql += "OR t.inputer ='" + AppUtils.getUserSession().getUsercode()
					+ "' OR t.updater = '"
					+ AppUtils.getUserSession().getUsercode() + "')";
			m.put("icare", sql);

		}

		String sql = "\nAND t.nos IS NOT NULL AND t.nos != '' AND (EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
				+ "\n				WHERE y.custlibid = z.custlibid "
				+ "\n				AND y.corpid = customerid"
				+ "\n				AND z.userid = "
				+ AppUtils.getUserSession().getUserid()
				+ ")"
				
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				+
				// xx.linktype = 'C' AND
				"\n	OR EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = customerid AND xx.userid = "
				+ AppUtils.getUserSession().getUserid()
				+ ")"
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n	OR (t.inputer ='"
				+ AppUtils.getUserSession().getUsercode() + "' OR t.updater = '"
				+ AppUtils.getUserSession().getUsercode() + "')" + "\n)";

		if(!StrUtils.isNull(jobStatus)){
			if(Boolean.parseBoolean(jobStatus)){
				sql += "\n AND parentid is null ";
			}else{
				sql += "\n AND parentid is not null ";
			}
		}

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		
		if(StrUtils.isNull(customerabbr)) {
			m.put("customer", "1=1");
		} else {
			String sql1 = "t.id IN (SELECT DISTINCT(jobid) FROM fina_arap a,sys_corporation s WHERE a.customerid = s.id AND s.iscustomer='Y' AND s.isdelete = FALSE AND a.isdelete = FALSE AND s.abbr LIKE '%"+customerabbr+"%')";
			m.put("customer", sql1);
		}
		String jobdatefilter = "";
		if(!StrUtils.isNull(querybatchDate)&&querybatchDate.equals("YM")){
			if(queryymdate!=null){
				if(qrydates.equals("jobdate")){
					jobdatefilter = " AND to_char(t.jobdate, 'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"' ";
				}else if(qrydates.equals("submitime")){
					jobdatefilter = " AND to_char(t.submitime, 'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"' ";
				}else if(qrydates.equals("completedate")){
					jobdatefilter = " AND to_char(t.completedate, 'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"' ";
				}else if(qrydates.equals("etd")){
					/*jobdatefilter = " AND (" +
										" EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND to_char(flightdate1,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"')" +
										" OR "+
										" EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"')" +
										")";*/
					
					jobdatefilter = " AND (CASE " +
									"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'L' THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.loadtime,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.flightdate1,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.singtime,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
									"  END)";
				}else if(qrydates.equals("eta")){
					jobdatefilter = " AND (CASE " +
							"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.eta,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.eta,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.eta,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.eta,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"'))" +
							"  END)";
				}else if("returncnttime".equals(qrydates)){
					jobdatefilter = " AND to_char(t.returncnttime, 'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(queryymdate)+"' ";
				}
			}
			
		}else{//YMD
			if(queryymddate!=null){
				if(qrydates.equals("jobdate")){
					jobdatefilter += " AND t.jobdate::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'";
				}else if(qrydates.equals("submitime")){
					jobdatefilter += " AND t.submitime::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'";
				}else if(qrydates.equals("completedate")){
					jobdatefilter += " AND t.completedate::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'";
				}else if(qrydates.equals("etd")){
//					jobdatefilter = " AND( EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"')"
//								  + " OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND flightdate1::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))";	
//					
					jobdatefilter = " AND (CASE " +
									"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.etd::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'L' THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.loadtime::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.flightdate1::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.etd::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.singtime::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
									"  END)";
				}else if(qrydates.equals("eta")){
					jobdatefilter = " AND (CASE " +
							"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.eta::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.eta::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.eta::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.eta::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
							"  END)";
				}else if("returncnttime".equals(qrydates)){
					jobdatefilter += " AND (CASE " +
					"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.returncnttime::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
					" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.vgmdate::DATE >= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddate)+"'))" +
					"  END)";
				}
			}
			if(queryymddateto != null){
				if(qrydates.equals("jobdate")){
					jobdatefilter += " AND t.jobdate::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'";
				}else if(qrydates.equals("submitime")){
					jobdatefilter += " AND t.submitime::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'";
				}else if(qrydates.equals("completedate")){
					jobdatefilter += " AND t.completedate::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'";
				}else if(qrydates.equals("etd")){
					/*jobdatefilter += "	AND (EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"')"
								  +	 " OR EXISTS(SELECT 1 FROM bus_air WHERE isdelete = FALSE AND jobid = t.id AND flightdate1::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))";		
					*/
					jobdatefilter += " AND (CASE " +
									"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.etd::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'L' THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.loadtime::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.flightdate1::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.etd::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
									" 		 WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.singtime::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
									"  END)";
				}else if(qrydates.equals("eta")){
					jobdatefilter += " AND (CASE " +
							"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.eta::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'L' THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.loadtime::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.flightdate1::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.eta::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
							" 		 WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.singtime::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
							"  END)";
				}else if("returncnttime".equals(qrydates)){
					jobdatefilter += " AND (CASE " +
					"		 WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.returncnttime::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
					" 		 WHEN COALESCE(jobtype,'S') = 'H' THEN (EXISTS(SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.vgmdate::DATE <= '"+new SimpleDateFormat("yyyy-MM-dd").format(queryymddateto)+"'))" +
					"  END)";
				}
			}
		}
		m.put("jobdatefilter", jobdatefilter);
		String corpfilter = " AND EXISTS((SELECT 1 FROM sys_user_corplink x WHERE x.ischoose = TRUE AND (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.userid = "+AppUtils.getUserSession().getUserid()+"))";
		if(querycorpid == null && querycorpidop == null){
			MessageUtils.alert("操作公司接单公司必选其一!");
		}else{
			if(querycorpid != null){
				corpfilter += " AND t.corpid ="+querycorpid;
			}
			if(querycorpidop != null){
				corpfilter += " AND t.corpidop ="+querycorpidop;
			}
		}
		m.put("corpfilter", corpfilter);
		
		String qryTradeway = "";
		if(!StrUtils.isNull(tradeway)){
			qryTradeway = "\n AND t.tradeway ='"+tradeway+"'";
		}
		m.put("qrytradeway", qryTradeway);
		
		String qryppcc = "";
		if(!StrUtils.isNull(qryppfinance)){
			if(Boolean.parseBoolean(qryppfinance)){
				qryppcc += "\n AND isconfirm2_pp=TRUE ";
			}else{
				qryppcc += "\n AND isconfirm2_pp=FALSE ";
			}
		}
		if(!StrUtils.isNull(qryccfinance)){
			if(Boolean.parseBoolean(qryccfinance)){
				qryppcc += "\n AND isconfirm2_cc=TRUE ";
			}else{
				qryppcc += "\n AND isconfirm2_cc=FALSE ";
			}
		}
		//应收应付费用完全SQL条件
		String qryFeeiFinished = "";
		if(!StrUtils.isNull(arFinished)){
			if(Boolean.parseBoolean(arFinished)){
				qryppcc += "\n AND iscomplete_ar = TRUE ";
			}else{
				qryppcc += "\n AND iscomplete_ar = FALSE ";
			}
		}
		if(!StrUtils.isNull(apFinished)){
			if(Boolean.parseBoolean(apFinished)){
				qryppcc += "\n AND iscomplete_ap = TRUE ";
			}else{
				qryppcc += "\n AND iscomplete_ap = FALSE ";
			}
		}
		m.put("qryppcc", qryppcc);
		return m;
	}

	@Override
	public void clearQryKey() {
		customerabbr = "";
		qryccfinance = "";
		qryppfinance = "";
		update.markUpdate(UpdateLevel.Data, "qryccfinance");
		update.markUpdate(UpdateLevel.Data, "qryppfinance");
		update.markUpdate(true, UpdateLevel.Data, "customerabbr");
		super.clearQryKey();
	}
	
	@Action
	public void showBatch(){
		batchWindow.show();
	}
	
	@Action
    public void batchDate_onselect() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        if("YM".equals(batchDate)){
	        	ymdate = new Date();
			}else if("YMD".equals(batchDate)){
				ymddate  = df.parse(DateTimeUtil.getFirstDay());
				ymddateto = df.parse(DateTimeUtil.getLastDay());
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 工作单权限过滤
	 * @return
	 */
	private String joblistFlter(){
		String sql = "\nAND t.nos IS NOT NULL AND t.nos != '' AND (EXISTS"
			+ "\n				(SELECT "
			+ "\n					1 "
			+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
			+ "\n				WHERE y.custlibid = z.custlibid "
			+ "\n				AND y.corpid = customerid"
			+ "\n				AND z.userid = "
			+ AppUtils.getUserSession().getUserid()
			+ ")"

			+ "\n	OR EXISTS"
			+ "\n				(SELECT "
			+ "\n					1 "
			+ "\n				FROM sys_custlib x , sys_custlib_role y  "
			+ "\n				WHERE y.custlibid = x.id  "
			+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
			+ "\n					AND x.libtype = 'S'  "
			//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
			+ "\n					AND x.userid = t.saleid "
			+ ")"
			
			+
			// xx.linktype = 'C' AND
			"\n	OR EXISTS(SELECT 1 FROM sys_user_assign xx WHERE xx.linkid = customerid AND xx.userid = "
			+ AppUtils.getUserSession().getUserid()
			+ ")"
			+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' " +
					"AND EXISTS ( SELECT 1 FROM bus_shipping y WHERE y.isdelete = FALSE AND y.jobid = t.id AND s.linkid = y.id)" +
					"AND s.userid ="
			+ AppUtils.getUserSession().getUserid() + ")"
			+ "\n	OR (t.inputer ='"
			+ AppUtils.getUserSession().getUsercode() + "' OR t.updater = '"
			+ AppUtils.getUserSession().getUsercode() + "')" + "\n)";

			// 权限控制 neo 2014-05-30
		//String sql2 = " AND (jobtype IN ('S','A','D','L','C') OR (jobtype = 'B' AND parentid IS NOT NULL))";
		//return sql+sql2;
		return sql;
	}
	/**
	 * pp财务确认
	 */
	@Action
	public void batchppConfirm(){
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		String sql = "UPDATE fina_jobs AS t SET isconfirm2_pp = TRUE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
			+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE" + corpidsql + datesql+joblistFlter() +
					"AND isconfirm2_pp = FALSE;";
		try {
			serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
			//MessageUtils.alert("OK!");
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * cc财务确认
	 */
	@Action
	public void batchccConfirm(){
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期范围类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期范围类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		String sql = "UPDATE fina_jobs AS t SET isconfirm2_cc = TRUE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
			+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" " +
					"AND isconfirm2_cc = FALSE;";
		try {
			serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * pp财务取消
	 */
	@Action
	public void batchppCancel(){
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
					//datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
					/*datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";*/
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		String sql = "UPDATE fina_jobs AS t SET isconfirm2_pp = FALSE ,confirm2_pp_time = NOW(),confirm2_pp_user = '"
			+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" " +
					"AND isconfirm2_pp = TRUE;";
		try {
			serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * cc财务取消
	 */
	@Action
	public void batchccCancel(){
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
					//datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		String sql = "UPDATE fina_jobs AS t SET isconfirm2_cc = FALSE ,confirm2_cc_time = NOW(),confirm2_cc_user = '"
			+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" " +
					"AND isconfirm2_cc = TRUE;";
		try {
			serviceContext.jobsMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void grid_ondblclick() {
		this.jobid = Long.parseLong(this.grid.getSelectedIds()[0]);
		this.showArapEdit();
	}
	
	
	private String getEtdDateSqlFilter(String type){
		String datesql = "";
		if(type.equals("YM")){
			datesql = "\n AND (CASE " +
							"\n WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'))"+
							"\n WHEN COALESCE(jobtype,'S') = 'L' THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.loadtime,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'))"+
							"\n WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.flightdate1,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'))"+
							"\n WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND to_char(x.singtime,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'))"+
							"\n END)";
		}else if(type.equals("YMD")){
			datesql = "\n AND (CASE " +
						"\n WHEN COALESCE(jobtype,'S') = 'S' THEN (EXISTS(SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+ new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'))"+
						"\n WHEN COALESCE(jobtype,'S') = 'L' THEN (EXISTS(SELECT 1 FROM bus_truck x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.loadtime::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+ new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'))"+
						"\n WHEN COALESCE(jobtype,'S') = 'A' THEN (EXISTS(SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.flightdate1::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+ new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'))"+
						"\n WHEN COALESCE(jobtype,'S') = 'C' THEN (EXISTS(SELECT 1 FROM bus_customs x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.singtime::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+ new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'))"+
						"\n END)";
		}
		return datesql;
	}
	
	/**
	 * 应收完成
	 */
	@Action
	public void isCompletearc() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
					//datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
					//datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+ new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String sql = "UPDATE fina_jobs t SET iscomplete_ar = TRUE ,complete_ar_time = NOW(),complete_ar_user = '"
				+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" AND iscomplete_ar = FALSE";
			//System.out.println(sql);
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 应收完成取消
	 */
	@Action
	public void isCompleteard() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String sql = "UPDATE fina_jobs t SET iscomplete_ar = FALSE ,complete_ar_time = NOW(),complete_ar_user = '"
				+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" AND iscomplete_ar = TRUE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 应付完成
	 */
	@Action
	public void isCompleteapc() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String sql = "UPDATE fina_jobs t SET iscomplete_ap = TRUE ,complete_ap_time = NOW(),complete_ap_user = '"
				+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" AND iscomplete_ap = FALSE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 应付完成取消
	 */
	@Action
	public void isCompleteapa() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String sql = "UPDATE fina_jobs t SET iscomplete_ap = FALSE ,complete_ap_time = NOW(),complete_ap_user = '"
				+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" AND iscomplete_ap = TRUE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 账单审核
	 */
	@Action
	public void checkerd() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND EXISTS(SELECT 1 FROM fina_jobs t WHERE isdelete = false AND to_char(jobdate,'yyyy-MM') = '"
						+new SimpleDateFormat("yyyy-MM").format(ymdate)+"' AND t.id = x.jobid "+joblistFlter()+")";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping a,fina_jobs t WHERE a.isdelete = FALSE AND a.jobid = t.id AND to_char(etd,'yyyy-MM') = '"
//					+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'"+joblistFlter()+" AND t.isdelete = false AND t.id = x.jobid)";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND EXISTS(SELECT 1 FROM fina_jobs t WHERE isdelete = false AND jobdate::DATE BETWEEN '"
							+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"' AND id = x.jobid "+joblistFlter()+")";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping a,fina_jobs t WHERE a.isdelete = FALSE AND a.jobid = t.id AND a.etd::DATE BETWEEN '"
//					+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"' AND t.isdelete = FALSE AND x.jobid = t.id"+joblistFlter()+")";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND EXISTS(SELECT 1 FROM fina_jobs t WHERE t.id = x.jobid AND t.isdelete = FALSE "+joblistFlter()+" AND t.corpid = "+corpid+")";
		try {
			String 	sql = "UPDATE fina_bill x SET ischeck = TRUE , checkdate = now() ,updater = '"
				+updater+"' ,updatetime = NOW(), checker = '"
				+updater+ "' WHERE isdelete = FALSE "+corpidsql+datesql+" AND COALESCE(ischeck,FALSE) = FALSE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 账单审核取消
	 */
	@Action
	public void checkerc() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND EXISTS(SELECT 1 FROM fina_jobs t WHERE isdelete = false AND to_char(jobdate,'yyyy-MM') = '"
						+new SimpleDateFormat("yyyy-MM").format(ymdate)+"' AND t.id = x.jobid "+joblistFlter()+")";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping a,fina_jobs t WHERE a.isdelete = FALSE AND a.jobid = t.id AND to_char(etd,'yyyy-MM') = '"
//					+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'"+joblistFlter()+" AND t.isdelete = false AND t.id = x.jobid)";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND EXISTS(SELECT 1 FROM fina_jobs t WHERE isdelete = false AND jobdate::DATE BETWEEN '"
							+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"' AND id = x.jobid "+joblistFlter()+")";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping a,fina_jobs t WHERE a.isdelete = FALSE AND a.jobid = t.id AND a.etd::DATE BETWEEN '"
//					+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"' AND t.isdelete = FALSE AND x.jobid = t.id"+joblistFlter()+")";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND EXISTS(SELECT 1 FROM fina_jobs t WHERE t.id = x.jobid AND t.isdelete = FALSE "+joblistFlter()+" AND t.corpid = "+corpid+")";
		try {
			String 	sql = "UPDATE fina_bill x SET ischeck = FALSE , checkdate = now() ,updater = '"
				+updater+"' ,updatetime = NOW(), checker = '"
				+updater+ "' WHERE isdelete = FALSE "+corpidsql+datesql+" AND COALESCE(ischeck,FALSE) = TRUE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 工作单确认
	 */
	@Action
	public void isCompletec() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
							new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {

			//批量工作单完成，检查往来是否一致，不一致跳过该票
			String wlsql = " AND (SELECT (SELECT COUNT(1) AS c\n" +
					"        FROM (SELECT t.corpid,t.customerid,t.currency,COALESCE(t.amount, 0) + COALESCE(t2.amount, 0) AS amount\n" +
					"              FROM (SELECT a.corpid,customerid,currency,sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
					"                    FROM fina_arap a\n" +
					"                    WHERE a.jobid = t.id\n" +
					"                      AND a.isdelete = FALSE\n" +
					"                      AND a.rptype != 'O'\n" +
					"                      AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
					"                    GROUP BY a.corpid, customerid, currency) t\n" +
					"                       LEFT JOIN (SELECT a.corpid,customerid,currency,sum(CASE WHEN araptype = 'AR' THEN amount ELSE -1 * amount END) AS amount\n" +
					"                                  FROM fina_arap a\n" +
					"                                  WHERE a.jobid = t.id\n" +
					"                                    AND a.isdelete = FALSE\n" +
					"                                    AND a.rptype != 'O'\n" +
					"                                    AND EXISTS(SELECT 1 FROM sys_corporation WHERE id = a.customerid AND isdelete = FALSE AND iscustomer = FALSE)\n" +
					"                                  GROUP BY a.corpid, customerid, currency) t2 ON (t2.customerid = t.corpid AND t2.corpid = t.customerid AND t2.currency = t.currency)\n" +
					"             ) TT\n" +
					"        WHERE TT.amount != 0) = 0)";

			String 	sql = "UPDATE fina_jobs t SET iscomplete = TRUE,updater='"
				+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+wlsql+corpidsql+datesql+joblistFlter()+" AND iscomplete = FALSE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 工作单确认取消
	 */
	@Action
	public void isCompleted() {
		String datesql = " AND FALSE";
		if(!StrUtils.isNull(batchDate)){
			if("YM".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND to_char(etd,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else if("YMD".equals(batchDate)){
				if(dates.equals("jobdate")){
					datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
				}else if(dates.equals("etd")){
//					datesql = " AND EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND jobid = t.id AND etd::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '"+
//					new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"')";
					datesql = getEtdDateSqlFilter(batchDate);
				}else{
					MessageUtils.alert("必须选择日期类型!");
					return;
				}
			}else{
				MessageUtils.alert("必须选择日期类型!");
				return;
			}
		}else{
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String 	sql = "UPDATE fina_jobs t SET iscomplete = FALSE,updater='"
					+ updater + "' , updatetime = NOW() WHERE isdelete = FALSE "+corpidsql+datesql+joblistFlter()+" AND iscomplete = TRUE";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 增减费用确认
	 */
	@Action
	public void isAmendFeeConfirm() {
		if(StrUtils.isNull(batchDate) || StrUtils.isNull(dates)){
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String datesql = " AND FALSE";
		if("YM".equals(batchDate)){
			datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
		}else if("YMD".equals(batchDate)){
			datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String 	sql = "UPDATE fina_jobs t SET iscomplete_amend = TRUE,complete_amend_time=NOW(),complete_amend_user='" + updater + "' WHERE isdelete = FALSE " + corpidsql + datesql + " ";
			//System.out.println(sql);
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			MessageUtils.showMsg("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 增减费用取消确认
	 */
	@Action
	public void isAmendFeeCancel() {
		if(StrUtils.isNull(batchDate) || StrUtils.isNull(dates)){
			MessageUtils.alert("必须选择日期类型!");
			return;
		}
		String datesql = " AND FALSE";
		if("YM".equals(batchDate)){
			datesql = " AND to_char(jobdate,'yyyy-MM') = '"+new SimpleDateFormat("yyyy-MM").format(ymdate)+"'";
		}else if("YMD".equals(batchDate)){
			datesql = " AND jobdate::DATE BETWEEN '"+new SimpleDateFormat("yyyy-MM-dd").format(ymddate)+"' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(ymddateto)+"'";
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String corpidsql = corpid == null || corpid <= 0 ? "" : " AND corpid = "+corpid;
		try {
			String 	sql = "UPDATE fina_jobs t SET iscomplete_amend = FALSE,complete_amend_time=NOW(),complete_amend_user='" + updater + "' WHERE isdelete = FALSE " + corpidsql + datesql + " ";
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			MessageUtils.showMsg("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 列表勾选增减费用确认
	 */
	@Action
	public void amendFeeConfirm() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET iscomplete_amend = TRUE,complete_amend_time=NOW(),complete_amend_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			MessageUtils.showMsg("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	/**
	 * 列表勾选增减费用确认取消
	 */
	@Action
	public void amendFeeCancel() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "UPDATE fina_jobs SET iscomplete_amend = FALSE,complete_amend_time=NOW(),complete_amend_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			MessageUtils.showMsg("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	
	/**
	 * 勾选应收完成
	 */
	@Action
	public void arComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET iscomplete_ar = TRUE ,complete_ar_time = NOW(),complete_ar_user = '"
						+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 勾选取消应收完成
	 */
	@Action
	public void cancalarComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET iscomplete_ar = FALSE ,complete_ar_time = NOW(),complete_ar_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	
	/**
	 * 勾选应付完成
	 */
	@Action
	public void apComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET iscomplete_ap = TRUE ,complete_ap_time = NOW(),complete_ap_user = '"
						+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/**
	 * 勾选取消应付完成
	 */
	@Action
	public void cancalapComplete() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (String id : ids) {
				Long jobid = Long.parseLong(id);
				String sql = "\nUPDATE fina_jobs SET iscomplete_ap = FALSE ,complete_ap_time = NOW(),complete_ap_user = '"
					+ updater + "' WHERE id =" + jobid + ";";
				stringBuffer.append(sql);
			}
			serviceContext.jobsMgrService.finaJobsDao.executeSQL(stringBuffer.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
}
