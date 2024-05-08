package com.scp.view.module.airtransport.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.view.comp.GridView;
import com.ufms.base.db.DaoUtil;
@ManagedBean(name = "pages.module.airtransport.report.bookingoperatenumBean", scope = ManagedBeanScope.REQUEST)
public class BookingoperatenumBean extends GridView{
	
	@SaveState
	@Bind
	public String isContain = "0";
	
	@SaveState
	@Bind
	public Long saleid;
	
	@SaveState
	@Bind
	public Long agentid;
	
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
	private String jsonData1;
	
	@Bind
	@SaveState
	private String jsonData2;
	
	@Action
	public void qryData(){
		String ret = "";
		String ret1 = "";
		try{
			//if("0".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT c.code, count(1) AS sumPiece, SUM(COALESCE(b.piece3,0)) AS sumPiece3, SUM(COALESCE(b.weight3,0)) AS sumWeight3, SUM(COALESCE(b.volume3,0)) AS sumVolume3 FROM bus_air b, sys_corporation c, fina_jobs fj WHERE fj.jobtype = 'A' AND fj.id = b.jobid AND b.carrierid = c.id AND b.isdelete = FALSE AND fj.isdelete = FALSE AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"'";
					if(agentid != null && agentid != 0){
						sql += "\nAND  agentid = "+agentid+" ";
					}
					sql +="\nGROUP BY c.code ";
					
//					List<Map> maplistList =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//					ret = JSONArray.fromObject(maplistList).toString();
					 ret = DaoUtil.queryForJsonArray(sql);
					jsonData = ret;
					String sql1 = "SELECT fj.sales, c.code, count(1) AS sumPiece, SUM(COALESCE(b.piece3,0)) AS sumPiece3, SUM(COALESCE(b.weight3,0)) AS sumWeight3, SUM(COALESCE(b.volume3,0)) AS sumVolume3 FROM bus_air b, sys_corporation c, fina_jobs fj WHERE fj.jobtype = 'A' AND fj.id = b.jobid AND b.carrierid = c.id AND b.isdelete = FALSE AND fj.isdelete = FALSE AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"'";
					
					if(saleid != null && saleid != 0){
						sql1 += "\nAND  fj.saleid = "+saleid+" ";
					}
					if(agentid != null && agentid != 0){
						sql1 += "\nAND  agentid = "+agentid+" ";
					}
					sql1 +="\nGROUP BY fj.sales,c.code ";
					//System.out.println("sql1--->"+sql1);
//					List<Map> maplist =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql1);
//					ret1 = JSONArray.fromObject(maplist).toString();
					 ret1 = DaoUtil.queryForJsonArray(sql);
					jsonData1 = ret1;
					
					if("1".equals(isContain)){
						String sql2 = "SELECT fj.nos,  COALESCE(b.mawbno,'') AS hawbno, COALESCE(to_char(b.inputtime,'yyyy-mm-dd'),'') AS inputtime, fj.sales, c.code,  COALESCE(b.piece3,0) AS piece3, COALESCE(b.weight3,0) AS weight3, COALESCE(b.volume3,0) AS volume3 FROM bus_air b, sys_corporation c, fina_jobs fj WHERE fj.jobtype = 'A' AND fj.id = b.jobid AND b.carrierid = c.id AND b.isdelete = FALSE AND fj.isdelete = FALSE AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"'";
						
						if(agentid != null && agentid != 0){
							sql2 += "\nAND  agentid = "+agentid+" ";
						}
						List<Map> maplists =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql2);
						jsonData2 = JSONArray.fromObject(maplists).toString();
						
					}
					
				}
				
			
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
			update.markUpdate(true, UpdateLevel.Data, "jsonData1");
			update.markUpdate(true, UpdateLevel.Data, "jsonData2");
			Browser.execClientScript("getData();");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
