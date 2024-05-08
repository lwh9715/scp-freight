package com.scp.view.module.airtransport.report;

import java.util.Calendar;
import java.util.Date;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.view.comp.GridView;
import com.ufms.base.db.DaoUtil;
@ManagedBean(name = "pages.module.airtransport.report.headoperatenumBean", scope = ManagedBeanScope.REQUEST)
public class HeadoperatenumBean extends GridView{
	
	@SaveState
	@Bind
	public String isContain = "0";
	
	@SaveState
	@Bind
	public Date dateStart;
	
	@SaveState
	@Bind
	public Date dateEnd;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			initDate();
		}
	}
	
	public void initDate(){
		dateStart = new Date();
		dateEnd = new Date();
		Calendar calendar = Calendar.getInstance();    
		calendar.setTime(dateStart);
		calendar.add(Calendar.MONTH, -1);
		dateStart = calendar.getTime();
		
		calendar.setTime(dateEnd);
		calendar.add(Calendar.DATE, -1);
		dateEnd = calendar.getTime();
	}
	
	@Bind
	@SaveState
	private String jsonData;
	
	@Bind
	@SaveState
	private String jsonDetailData;
	
	@Action
	public void qryData(){
		String ret = "";
		update.markUpdate(true, UpdateLevel.Data, "isContain");
		try{
			//if("0".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT  COALESCE((SELECT namec FROM sys_user x WHERE code = f.actorid AND x.isdelete = false limit 1),'') AS assigndesc, count(1) AS sumPiece FROM _bpm_task f ,fina_jobs fj,bpm_process c WHERE f.refno=fj.nos AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' AND f.taskname='客服跟单' AND c.id=f.processid AND c.namec='审单流程_空运工作单' AND fj.isdelete = FALSE " +
							"AND EXISTS(SELECT 1 FROM sys_user x, sys_department d WHERE x.code = f.actorid AND x.isdelete = false and x.deptid = d.id and d.name = '深圳空运客服部') GROUP BY assigndesc";
					//System.out.println("sql--->"+sql);
//					List<Map> maplistList =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//					
//					ret = JSONArray.fromObject(maplistList).toString();
					ret=DaoUtil.queryForJsonArray(sql);
				}
			//}
			//System.out.println(ret);
			jsonData = ret;
			
			
			if("1".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT fj.nos AS jobno ,b.mawbno AS hawbno,(SELECT namec FROM sys_user x WHERE code = f.actorid AND x.isdelete = false  limit 1) AS assigndesc, to_char(fj.inputtime,'yyyy-mm-dd') AS inputtime FROM _bpm_task f,bpm_process c,bus_air b, fina_jobs fj WHERE f.refno=fj.nos AND b.jobid = fj.id AND fj.isdelete = FALSE AND b.isdelete = FALSE AND f.taskname='客服跟单' AND c.id=f.processid AND c.namec='审单流程_空运工作单'" +
							"AND EXISTS(SELECT 1 FROM sys_user x, sys_department d WHERE x.code = f.actorid AND x.isdelete = false and x.deptid = d.id and d.name = '深圳空运客服部') AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"'";
					//System.out.println("sql--->"+sql);
//					List<Map> maps = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//					ret = JSONArray.fromObject(maps).toString();
					ret=DaoUtil.queryForJsonArray(sql);
				}
				//System.out.println(ret);
				jsonDetailData = ret;
			}
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
			update.markUpdate(true, UpdateLevel.Data, "jsonDetailData");
			Browser.execClientScript("getData();");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
