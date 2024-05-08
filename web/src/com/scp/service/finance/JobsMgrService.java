package com.scp.service.finance;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaJobsDao;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class JobsMgrService {

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	@Resource
	public FinaJobsDao finaJobsDao;

	public void saveData(FinaJobs data) {
		if (0 == data.getId()) {
			finaJobsDao.create(data);
		} else {
			finaJobsDao.modify(data);
		}
	}

	public void removeDate(Long id, String user) {
		String sql = "\nUPDATE fina_jobs SET isdelete = TRUE , updater = '"
				+ user + "' , updatetime = NOW() WHERE id = " + id + ";";
		finaJobsDao.executeSQL(sql);
	}

	public void choosejobs(String[] ids, Long parentid,String user,String createnewnos) {
		String id = StrUtils.array2List(ids);
//		String sql = "UPDATE fina_jobs SET parentid = " + parentid
//				+ " WHERE id in (" + id + ");";
		String sql="SELECT f_bus_jobs_join('jobsids="+id+";parentid="+parentid+";inputer="+user+";createnewnos="+createnewnos+"');";
		finaJobsDao.executeQuery(sql);
	}
	
	

	public void cancel(String[] ids) {

		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_jobs SET parentid = NULL WHERE id in (" + id
				+ ");";
		finaJobsDao.executeSQL(sql);

	}


	public void createPod(Long mPkVal, Long userid, String usercode) {
		String sql = "SELECT f_fina_jobs_createpod('oldid="+mPkVal+";userid="+userid+";inputer="+usercode +"');";
		finaJobsDao.executeQuery(sql);
		
	}


	public String addJobsLeaf(Long pkVal ,String user) {
		String sql="SELECT f_bus_jobs_addjobsleaf('jobsid="+pkVal+";inputer="+user+"') AS jobno;";
		List list = finaJobsDao.executeQuery(sql);
		return list.get(0).toString();
	}

	//空运工作单，新增委托单
	public String addJobsLeafair(Long pkVal ,String user) {
		String sql="SELECT f_bus_jobs_addjobsleafair('jobsid="+pkVal+";inputer="+user+"') AS jobno;";
		List list = finaJobsDao.executeQuery(sql);
		return list.get(0).toString();
	}
	
	public void createwmsin(Long mPkVal, String usercode) {
		String sql = "SELECT f_fina_jobs_createwmsin('oldid="+mPkVal+";inputer="+usercode +"');";
		finaJobsDao.executeQuery(sql);
		
	}

	public void createsplitwmsin(Long mPkVal, String usercode, Long qryWarehouseCom) {
		String sql = "SELECT f_fina_jobs_splitwmsin('oldid="+mPkVal+";inputer="+usercode +";warehouseid="+qryWarehouseCom+"');";
		finaJobsDao.executeQuery(sql);
		
	}

	public String addcopy(String id, Long userid, String usercode,String corpid,String corpidop,String jobdate,String includfee,String serialno,String entrust,String bookinginfo,String portinfo,String packing,String bookingagent,
			String productinfo,String shippingterms,String boxnum,String vesvoy,String refeno,String ordernum,String bookingnum,String hblnum,String mblnum,String cutday,
			String etacopy,String etdcopy,String atdcopy,String atacopy,String packinglist,String dispcopy) {
		Long jobsid = Long.valueOf(id);
		String sql = "SELECT f_fina_jobs_addcopy('id="+jobsid+";userid="+userid+";inputer="+usercode +";corpid="+corpid+";corpidop="+corpidop+";jobdate="+jobdate+";includfee="+includfee+";serialno="+serialno+";" +
				"entrust="+entrust+";bookinginfo="+bookinginfo+";portinfo="+portinfo+";packing="+packing+";bookingagent="+bookingagent+";productinfo="+productinfo+";shippingterms="+shippingterms+";" +
				"boxnum="+boxnum+";vesvoy="+vesvoy+";refeno="+refeno+";ordernum="+ordernum+";bookingnum="+bookingnum+";hblnum="+hblnum+";mblnum="+mblnum+";" +
				"cutday="+cutday+";etacopy="+etacopy+";etdcopy="+etdcopy+";atdcopy="+atdcopy+";atacopy="+atacopy+";packinglist="+packinglist+";dispcopy="+dispcopy+";') AS nos;";
		System.out.print(sql);
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("nos");
		
	}
	
	public String addCopyTrain(String id, Long userid, String usercode,String corpid,String corpidop,String jobdate,String includfee,String serialno,String entrust,String bookinginfo,String portinfo,String packing,String bookingagent,
			String productinfo,String shippingterms,String boxnum,String vesvoy,String refeno,String ordernum,String bookingnum,String hblnum,String mblnum,String cutday,
			String etacopy,String etdcopy,String atdcopy,String atacopy) {
		Long jobsid = Long.valueOf(id);
		String sql = "SELECT f_fina_jobs_addcopy_train('id="+jobsid+";userid="+userid+";inputer="+usercode +";corpid="+corpid+";corpidop="+corpidop+";jobdate="+jobdate+";includfee="+includfee+";serialno="+serialno+";" +
				"entrust="+entrust+";bookinginfo="+bookinginfo+";portinfo="+portinfo+";packing="+packing+";bookingagent="+bookingagent+";productinfo="+productinfo+";shippingterms="+shippingterms+";" +
				"boxnum="+boxnum+";vesvoy="+vesvoy+";refeno="+refeno+";ordernum="+ordernum+";bookingnum="+bookingnum+";hblnum="+hblnum+";mblnum="+mblnum+";" +
				"cutday="+cutday+";etacopy="+etacopy+";etdcopy="+etdcopy+";atdcopy="+atdcopy+";atacopy="+atacopy+";') AS nos;";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("nos");
	}
	
	public String addcopyair(String id, Long userid, String usercode,String corpid,String corpidop,String jobdate,String includfee,String serialno) {
		Long jobsid = Long.valueOf(id);
		String sql = "SELECT f_fina_jobs_addcopy_air('id="+jobsid+";userid="+userid+";inputer="+usercode +";corpid="+corpid+";corpidop="+corpidop+";jobdate="+jobdate+";includfee="+includfee+";serialno="+serialno+";') AS nos;";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("nos");
		
	}
	
	public String addcopyland(String id, Long userid, String usercode,String corpid,String corpidop,String jobdate,String includfee,String serialno) {
		Long jobsid = Long.valueOf(id);
		String sql = "SELECT f_fina_jobs_addcopy_land('id="+jobsid+";userid="+userid+";inputer="+usercode +";corpid="+corpid+";corpidop="+corpidop+";jobdate="+jobdate+";includfee="+includfee+";serialno="+serialno+";') AS nos;";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("nos");
		
	}

	/**
	 * @param ids
	 * @param userid
	 * @param usercode
	 */
	public void saveJoin(String[] ids, Long userid, String usercode) {
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_jobs_joinchild('ids="+id+";userid="+userid+";usercode="+usercode +"');";
		finaJobsDao.executeQuery(sql);
	}

	public void createJobs(Long mPkVal, String usercode) {
		String sql = "UPDATE fina_jobs SET jobtype = 'S' WHERE id in (" + mPkVal
		+ ");";
		finaJobsDao.executeSQL(sql);
	}

	/**
	 * 开账
	 * @param ids
	 */
	public void opeanIslock(String[] ids , String user) {
//		String user = AppUtils.getUserSession().getUsername();
		for(String id : ids) {
			Long jobid = Long.parseLong(id);
			String sql = "UPDATE fina_jobs SET islock = FALSE,updater = '" + user + "',updatetime = NOW() WHERE id = " + jobid;
			this.finaJobsDao.executeSQL(sql);
		}
	}
	/**
	 * 关账
	 * @param ids
	 */
	public void closeIslock(String[] ids, String user) {
//		String user = AppUtils.getUserSession().getUsername();
		for(String id : ids) {
			Long jobid = Long.parseLong(id);
			String sql = "UPDATE fina_jobs SET islock = TRUE,updater = '" + user + "',updatetime = NOW() WHERE id = " + jobid;
			this.finaJobsDao.executeSQL(sql);
		}
	}

	/**
	 * 批量关账
	 * @param endDate 
	 * @param startDate 
	 */
	public void closesIslock(String startDate, String endDate, String user) {
//		String user = AppUtils.getUserSession().getUsername();
		String sql = "UPDATE fina_jobs SET islock = TRUE,updater = '" 
					+ user 
					+ "',updatetime = NOW() WHERE jobdate BETWEEN '"
					+ startDate + "' AND '" + endDate + "' AND isdelete = FALSE;";
		sql += "\nUPDATE sys_config SET val = '" + endDate + "' WHERE key = 'Bus_Job_Cls_Date'";
		this.finaJobsDao.executeSQL(sql);
	}

	public void createsplitwmsin(Long parentid, String[] ids, String usercode,
			Long qryWarehouseCom,Boolean free) {
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_jobs_splitwmsin('oldid="+parentid+";ids="+id+";inputer="+usercode +";warehouseid="+qryWarehouseCom+";freewms="+free+"');";
		finaJobsDao.executeQuery(sql);
		
	}
	/**
	 * hbl申请提单为PP 自动完成
	 * @param id
	 */
	public void checkpp(Long id) {
		String sql = "UPDATE bus_ship_bill SET freightitem = 'PP' WHERE jobid = "+id+" AND isdelete = FALSE AND bltype = 'H'";
		this.finaJobsDao.executeSQL(sql);
		
	}

	/**
	 * 生成付款申请单
	 * @param ids
	 * @param usercode
	 */
	public Long creareReqBill(String[] ids, String usercode) {
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_rpreq_createreq('ids="+id+";user="+usercode+"')AS reqid;";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (Long)m.get("reqid");
	}
	
	/**
	 * 清除海外单号
	 * Neo
	 * 2014/2/3 17:00
	 * NEO 不需要先查,存在生成多个海外单号,第三层好像目前写法没有处理
	 */
	public void removeJobsLink(Long jobid)throws Exception {
		//String sql = "SELECT b.jobidto FROM fina_jobs_link b WHERE  b.jobidfm = "
		//		+ jobid;
		//Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		//System.out.print("成功!");
		//Long jobidto = (Long) map.get("jobidto");
		
		String sql2= "\nUPDATE fina_jobs SET isdelete = TRUE WHERE id IN ( SELECT b2.jobidto FROM fina_jobs_link b ,fina_jobs_link b2 WHERE b2.jobidfm = b.jobidto AND  b.jobidfm = "+ jobid+");";	
		 sql2 += "UPDATE fina_jobs SET isdelete = TRUE WHERE id IN ( SELECT b.jobidto FROM fina_jobs_link b WHERE  b.jobidfm = "+ jobid+");";
		sql2 += "\nDELETE FROM fina_jobs_link WHERE jobidfm = " + jobid + ";";
		sql2 += "\nDELETE FROM fina_jobs_link WHERE jobidfm IN ( SELECT jobidto FROM fina_jobs_link b WHERE b.jobidfm = "+ jobid+");";
		
		//清除第二次的
		
			finaJobsDao.executeSQL(sql2);
		
//		ApplicationUtils.debug("删除成功");
	}
	
	/**
	 * 检查勾选多个工作单的客户是否一致
	 * @param ids
	 * @return
	 */
	public boolean custemerEqually(String[] ids){
		String sql = "SELECT DISTINCT customerid FROM fina_jobs WHERE FALSE ";
		for(String id : ids) {
			sql += "\n OR id = " + id;
		}	
		List<Map> m = daoIbatisTemplate.queryWithUserDefineSql(sql);
		if(m.size()>1){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 批量修改
	 * 
	 * @param ids
	 * @param etd
	 * @param eta
	 * @param clstime
	 * @param sidate
	 * @param UserCode
	 */
	public void editJobs(String[] ids, Date eta,Date etd,Date clstime,Date sidate,String vessel1,String voyage1,
			Date atd,Date ata,Date loadtime,Date storehousedate,String destination1,Long agentdesid,Long clearancecusid,Long docuserid,Long opruserid,
			Long agentid,Long puserid,String mblno,String bltype,String mbltype,String hbltype,String routecodev,Long carrieridv,String userCode, String editbusstatus, boolean releasecnt, Date releasecnttime, boolean pickcnt, Date pickcnttime,
			BigInteger opcomp,String potcode,Date sotime,String pono,String remark) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));    
		BigInteger big = BigInteger.ZERO;
		String corpidop = "";//操作公司
		String remarks = "";//备注
		if(null != opcomp && opcomp.compareTo(big) > 0)corpidop = ",corpidop  ="+opcomp;
		if(!StrUtils.isNull(remark)) remarks = remark;//備注

		String sql2 = "UPDATE fina_jobs SET updater = '" + userCode + "',updatetime = NOW()"+corpidop+",remarks = '"+remarks+"' WHERE id IN("+StrUtils.array2List(ids)+");";

		//bus_shipping
		StringBuilder stringBuilder = new StringBuilder();
		String sql = "UPDATE bus_shipping SET ";
		if(eta != null)stringBuilder.append("eta  ='"+sdf1.format(eta)+"',");
		if(etd != null)stringBuilder.append("etd  ='"+sdf1.format(etd)+"',");
		if(clstime != null)stringBuilder.append("clstime  ='"+clstime.toGMTString()+"',");
		if(sidate != null)stringBuilder.append("sidate  ='"+sidate.toGMTString()+"',");
		if(!StrUtils.isNull(vessel1))stringBuilder.append("vessel  ='"+vessel1+"',");
		if(!StrUtils.isNull(voyage1))stringBuilder.append("voyage  ='"+voyage1+"',");
		if(atd != null)stringBuilder.append("atd  ='"+sdf1.format(atd)+"',");
		if(ata != null)stringBuilder.append("ata  ='"+sdf1.format(ata)+"',");
		if(destination1 != null && destination1 != "")stringBuilder.append("destination  ='"+destination1+"',");
		if(agentdesid != null)stringBuilder.append("agentdesid  ='"+agentdesid+"',");
		if(agentid != null)stringBuilder.append("agentid  ='"+agentid+"',");
		if(puserid != null)stringBuilder.append("priceuserid  ='"+puserid+"',");
		if(!StrUtils.isNull(mblno))stringBuilder.append("mblno  ='"+mblno+"',");
		if(!StrUtils.isNull(bltype))stringBuilder.append("bltype  ='"+bltype+"',");
		if(!StrUtils.isNull(mbltype))stringBuilder.append("mbltype  ='"+mbltype+"',");
		if(!StrUtils.isNull(hbltype))stringBuilder.append("hbltype  ='"+hbltype+"',");
		if(!StrUtils.isNull(routecodev))stringBuilder.append("routecode  ='"+routecodev+"',");
		if(carrieridv != null)stringBuilder.append("carrierid  ='"+carrieridv+"',");
		
		if(!StrUtils.isNull(editbusstatus))stringBuilder.append("busstatus  ='"+editbusstatus+"',");//业务状态
		if(releasecnt)stringBuilder.append("isreleasecnt  ="+releasecnt+",");//是否放箱
		if(releasecnttime != null)stringBuilder.append("releasecnttime  ='"+releasecnttime.toGMTString()+"',");//放箱日期
		if(pickcnt)stringBuilder.append("ispickcnt="+pickcnt+",");//是否提箱
		if(pickcnttime != null)stringBuilder.append("pickcnttime='"+pickcnttime.toGMTString()+"',");//提箱日期
		
		if(!StrUtils.isNull(potcode))stringBuilder.append("potcode='"+potcode+"',");//中转港
		if(sotime != null)stringBuilder.append("sodate='"+sotime.toGMTString()+"',");//SO日期
		if(!StrUtils.isNull(pono))stringBuilder.append("pono='"+pono.trim()+"',");
		if(stringBuilder.length() >0 ){
			String sqlBody = stringBuilder.toString();
			sqlBody = sqlBody.substring(0, sqlBody.length()-1);
			sql += sqlBody +",updater = '" + userCode + "',updatetime = NOW() WHERE jobid IN("+StrUtils.array2List(ids)+");";
			this.finaJobsDao.executeSQL(sql);
			this.finaJobsDao.executeSQL(sql2);
		}
		
		//bus_customs
		StringBuilder stringBuilder1 = new StringBuilder();
		String sql3 = "UPDATE bus_customs SET ";
		if(storehousedate != null)stringBuilder1.append("storehousedate  ='"+sdf.format(storehousedate)+"',");
		if(clearancecusid != null)stringBuilder1.append("clearancecusid  ='"+clearancecusid+"',");
		if(stringBuilder1.length()>0){
			String sqlBody1 = stringBuilder1.toString();
			sqlBody1 = sqlBody1.substring(0, sqlBody1.length()-1);
			sql3 += sqlBody1 +",updater = '" + userCode + "',updatetime = NOW() WHERE jobid IN("+StrUtils.array2List(ids)+");";
			this.finaJobsDao.executeSQL(sql3);
			this.finaJobsDao.executeSQL(sql2);
		}
		//bus_truck
		StringBuilder stringBuilder2 = new StringBuilder();
		String sql4 = "UPDATE bus_truck SET ";
		if(loadtime != null)stringBuilder2.append("loadtime  ='"+loadtime.toGMTString()+"',");
		if(stringBuilder2.length()>0){
			String sqlBody2 = stringBuilder2.toString();
			sqlBody2 = sqlBody2.substring(0, sqlBody2.length()-1);
			sql4 += sqlBody2 +",updater = '" + userCode + "',updatetime = NOW() WHERE jobid IN("+StrUtils.array2List(ids)+");";
			this.finaJobsDao.executeSQL(sql4);
			this.finaJobsDao.executeSQL(sql2);
		}else if("".equals(corpidop) == false || "".equals(remarks) == false){
			this.finaJobsDao.executeSQL(sql2);
		}
		//sys_user_assign
		String jobids = StrUtils.array2List(ids);
		if(docuserid == null){
			docuserid = 1L;
		}if(opruserid == null){
			opruserid = 1L;
		}
		String sq5="SELECT f_sys_user_assign('jobids="+jobids+";docuserid="+docuserid+";opruserid="+opruserid+";userCode="+userCode+"');";
		finaJobsDao.executeQuery(sq5);
		
	}
	
	/*
	 * 查找工作单中模块的权限，返回授权了的code
	 * */
	public List<Map> findModinroleMy(){
		String sql = "SELECT sm.code FROM sys_modinrole am,sys_module sm WHERE am.moduleid = sm.id AND sm.pid = 295000300 AND EXISTS(SELECT 1 FROM sys_role sr WHERE am.roleid = sr.id AND sr.code = '"+AppUtils.getUserSession().getUsercode()+"' AND sr.roletype = 'C') --用户里面的授权"+
				     "\nUNION"+
					 "\nSELECT sm.code FROM sys_modinrole am,sys_module sm WHERE am.moduleid = sm.id AND sm.pid = 295000300 AND EXISTS(SELECT 1 FROM sys_role sr WHERE am.roleid = sr.id AND roletype = 'M' AND EXISTS(SELECT 1 FROM sys_userinrole WHERE roleid = sr.id AND userid = "+AppUtils.getUserSession().getUserid()+")) --组里面的授权";
		List<Map> m = daoIbatisTemplate.queryWithUserDefineSql(sql);
		return m ;
	}
	
	/*
	 * 查找工作单中模块的权限，返回授权了的code
	 * */
	public List<Map> findAirModinroleMy(){
		String sql = "SELECT sm.code FROM sys_modinrole am,sys_module sm WHERE am.moduleid = sm.id AND sm.pid = 299000400 AND EXISTS(SELECT 1 FROM sys_role sr WHERE am.roleid = sr.id AND sr.code = '"+AppUtils.getUserSession().getUsercode()+"' AND sr.roletype = 'C') --用户里面的授权"+
				     "\nUNION"+
					 "\nSELECT sm.code FROM sys_modinrole am,sys_module sm WHERE am.moduleid = sm.id AND sm.pid = 299000400 AND EXISTS(SELECT 1 FROM sys_role sr WHERE am.roleid = sr.id AND roletype = 'M' AND EXISTS(SELECT 1 FROM sys_userinrole WHERE roleid = sr.id AND userid = "+AppUtils.getUserSession().getUserid()+")) --组里面的授权";
		List<Map> m = daoIbatisTemplate.queryWithUserDefineSql(sql);
		return m ;
	}
	
	public void chooseAirjobs(String[] ids, Long parentid,String user,String createnewnos) {
		String id = StrUtils.array2List(ids);
		String sql="SELECT f_bus_jobs_join('jobsids="+id+";parentid="+parentid+";inputer="+user+";type=AIR;createnewnos="+createnewnos+";');";
		finaJobsDao.executeQuery(sql);
	}

	public String findTipsCount(Long jobid , String tblname) {
		String size;
		try {
			String sql = "SELECT COUNT(*) AS cnts FROM "+tblname+" x WHERE x.linkid = " + jobid + " AND isdelete = FALSE";
			if("sys_knowledgelib".equals(tblname)){
				sql = "SELECT COUNT(*) AS cnts FROM "+tblname+" x WHERE isdelete = FALSE" +
						"\n AND " +
						"\n	(EXISTS(SELECT 1 FROM bus_shipping b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.carrierid = x.linkid AND x.linkid2 < 0)" +
						"\n 		OR EXISTS(SELECT 1 FROM fina_jobs b WHERE b.id = "+ jobid +" AND b.isdelete = FALSE AND b.customerid = x.linkid AND x.linkid2 < 0)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.podid = x.linkid AND x.linkid2 < 0)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.polid = x.linkid AND x.linkid2 < 0)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.pddid = x.linkid AND x.linkid2 < 0)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.agentid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_truck b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.truckid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_customs b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.customid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.agentdesid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_air b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.carrierid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_air b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.agentdesid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_air b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.podid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_air b WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND b.polid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b,dat_line d  WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND d.isdelete = FALSE AND b.routecode = COALESCE(d.code,d.namec) AND d.id = x.linkid AND x.linkid2 < 0)" +
						"\n 		OR EXISTS(SELECT 1 FROM bus_shipping b,dat_line d  WHERE b.jobid = "+ jobid +" AND b.isdelete = FALSE AND d.isdelete = FALSE AND b.routecode = COALESCE(d.code,d.namec) AND d.id = x.linkid2 AND b.carrierid = x.linkid)" +
						"\n 		OR EXISTS(SELECT 1 FROM fina_jobs a, bus_shipping b WHERE a.id = b.jobid AND b.jobid = "+ jobid +" AND b.isdelete = FALSE AND a.isdelete = false AND a.customerid = x.linkid AND b.podid = x.linkid2)" +
						"\n 		OR EXISTS(SELECT 1 FROM fina_jobs a, bus_shipping b WHERE a.id = b.jobid AND b.jobid = "+ jobid +" AND b.isdelete = FALSE AND a.isdelete = false AND a.customerid = x.linkid AND b.polid = x.linkid2)" +
						"\n 		OR EXISTS(SELECT 1 FROM fina_jobs a, bus_shipping b WHERE a.id = b.jobid AND b.jobid = "+ jobid +" AND b.isdelete = FALSE AND a.isdelete = false AND a.customerid = x.linkid AND b.pddid = x.linkid2)" +
						"\n	)";
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			size = StrUtils.getMapVal(map, "cnts");
			if("0".equals(size))size="";
			return size;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//批量更新费用
	public String batchupUpdateMoneyDate(String[] jobids, String araptype,String feeitemid,String price,String currency, Long userid, String ppcctype, String customersid, String arapdate, String piece, String payplace, String corp, String type){
		String sql = "\nSELECT f_fina_arap_modifybatch('jobids=" + StrUtils.array2List(jobids) + ";araptype="+araptype+";feeitemid=" +feeitemid+";price=" +price+ ";currency=" +currency+";userid="+userid+";ppcctype="+ppcctype+";customersid=" +customersid+";arapdate=" +arapdate+ ";piece=" +piece+";payplace="+payplace+";corp=" +corp+";type="+type+";') AS feeinfo;";
		//System.out.println(sql);
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("feeinfo");
	}
	
	
	
	public void updateBatchEditGrid(Object modifiedData, String username) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		String sql = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String jobid=String.valueOf(jsonObject.get("jobid"));
			String customerid=String.valueOf(jsonObject.get("customeridap"));
			String ispaycheck=String.valueOf(jsonObject.get("ispaycheck")).trim();
			String ispayagree=String.valueOf(jsonObject.get("ispayagree")).trim();
			
			String ishold=String.valueOf(jsonObject.get("ishold"));
			String holdtime=String.valueOf(jsonObject.get("holdtime"));
			String holddesc=String.valueOf(jsonObject.get("holddesc")).trim();
			String isput=String.valueOf(jsonObject.get("isput"));
			String puttime=String.valueOf(jsonObject.get("puttime"));
			String putdesc=String.valueOf(jsonObject.get("putdesc")).trim();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));  
			
			if(StrUtils.isNull(putdesc) || "null".equals(putdesc))putdesc="";
			if(StrUtils.isNull(holddesc) || "null".equals(holddesc))holddesc="";
			
			sql += "\nINSERT INTO fina_jobs_paycheck(id , jobid , customerid) SELECT getid(),"+jobid+","+customerid+" FROM _virtual WHERE NOT EXISTS (SELECT 1 FROM fina_jobs_paycheck WHERE jobid = "+jobid+" AND customerid = "+customerid+");";
			if(ispaycheck.equals("true")){
				sql += "\nUPDATE fina_jobs_paycheck SET ispaycheck = true , paycheckdate = now(),paychecker = '"+AppUtils.getUserSession().getUsername()+"' WHERE COALESCE(ispaycheck,false) = false AND jobid = "+jobid+" AND customerid = "+customerid+";";
			}else if(ispaycheck.equals("false")){
				sql += "\nUPDATE fina_jobs_paycheck SET ispaycheck = false , paycheckdate = now(),paychecker = ''  WHERE COALESCE(ispaycheck,false) = true AND jobid = "+jobid+" AND customerid = "+customerid+";";
			}
			if(ispayagree.equals("true")){
				sql += "\nUPDATE fina_jobs_paycheck SET ispayagree = true , payagreedate = now(),payagreer = '"+AppUtils.getUserSession().getUsername()+"' WHERE COALESCE(ispayagree,false) = false AND jobid = "+jobid+" AND customerid = "+customerid+";";
			}else if(ispayagree.equals("false")){
				sql += "\nUPDATE fina_jobs_paycheck SET ispayagree = false , payagreedate = now(),payagreer = '' WHERE COALESCE(ispayagree,false) = true AND jobid = "+jobid+" AND customerid = "+customerid+";";
			}
			
			if(ishold.toString().equals("true")){
				sql += "\nUPDATE bus_shipping SET ishold ="+ishold+",holdtime = now(), holder = '"+username+"' WHERE COALESCE(ishold,false) = false AND jobid = "+jobid+";" +
					   "\nUPDATE bus_ship_bill SET ishold ="+ishold+",holdtime = now(), holder = '"+username+"' WHERE COALESCE(ishold,false) = false AND id = "+jobid+";";
			}else{
				sql += "\nUPDATE bus_shipping SET ishold =false,holdtime = null, holder = null WHERE jobid = "+jobid+";" +
						"\nUPDATE bus_ship_bill SET ishold =false,holdtime = null, holder = null WHERE id = "+jobid+";";
			}
			if(!StrUtils.isNull(holdtime) && ishold.toString().equals("true")){
				try {
					Date d = sdf.parse(holdtime.toString());
					sql += "\nUPDATE bus_shipping SET holdtime ='"+d.toGMTString()+"' WHERE  ishold = false AND jobid = "+jobid+";" +
							"\nUPDATE bus_ship_bill SET holdtime ='"+d.toGMTString()+"' WHERE  ishold = false AND id = "+jobid+";";
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(!StrUtils.isNull(holddesc)){
				sql += "\nUPDATE bus_shipping SET holddesc ='"+holddesc+"' WHERE jobid = "+jobid+";" +
						"\nUPDATE bus_ship_bill SET holddesc ='"+holddesc+"' WHERE id = "+jobid+";";
			}
			
			if(isput.toString().equals("true")){
				sql += "\nUPDATE bus_shipping SET isput ="+isput+",puttime = now(), puter = '"+username+"' WHERE COALESCE(isput,false) = false AND jobid = "+jobid+";" +
						"\nUPDATE bus_ship_bill SET isput ="+isput+",puttime = now(), puter = '"+username+"' WHERE COALESCE(isput,false) = false AND id = "+jobid+";";
			}else{
				sql += "\nUPDATE bus_shipping SET isput =false,puttime = null, puter = null WHERE jobid = "+jobid+";" +
						"\nUPDATE bus_ship_bill SET isput =false,puttime = null, puter = null WHERE id = "+jobid+";";
			}
			if(!StrUtils.isNull(puttime) && isput.toString().equals("true")){
				try {
					Date d = sdf.parse(puttime.toString());
					sql += "\nUPDATE bus_shipping SET puttime ='"+d.toGMTString()+"' WHERE jobid = "+jobid+";" +
							"\nUPDATE bus_ship_bill SET puttime ='"+d.toGMTString()+"' WHERE id = "+jobid+";";
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(!StrUtils.isNull(putdesc)){
				sql += "\nUPDATE bus_shipping SET putdesc ='"+putdesc+"' WHERE jobid = "+jobid+";" +
						"\nUPDATE bus_ship_bill SET putdesc ='"+putdesc+"' WHERE id = "+jobid+";";
			}
			
		}		
		if(!StrUtils.isNull(sql)){
			//System.out.println(sql);	
			this.daoIbatisTemplate.updateWithUserDefineSql(sql);
		}
	}
	
	
	/**
	 * @param jobid
	 * 
	 * 查询子单数量
	 */
	public String findChildjobCount(Long jobid) {
		String size;
		try {
			String sql = "SELECT COUNT(*) AS cnts FROM fina_jobs x WHERE x.parentid = " + jobid + " AND isdelete = FALSE";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			size = StrUtils.getMapVal(map, "cnts");
			if("0".equals(size))size="";
			return size;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * @param ids
	 * @param userid
	 * @param usercode
	 * 铁运子单合并委托单
	 */
	public void saveJointrain(String[] ids, Long userid, String usercode) {
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_jobstrain_joinchild('ids="+id+";userid="+userid+";usercode="+usercode +"');";
		finaJobsDao.executeQuery(sql);
	}

	public String batchupUpdateMoneyDate(String[] jobids, String araptype,String feeitemid, String price, String currency, Long userid,String type) {
		String sql = "\nSELECT f_fina_arap_modifybatch('jobids=" + StrUtils.array2List(jobids) + ";araptype="+araptype+";feeitemid=" +feeitemid+";price=" +price+ ";currency=" +currency+";userid="+userid+";type="+type+";') AS feeinfo;";
		//System.out.println(sql);
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("feeinfo");
	}
	
}
