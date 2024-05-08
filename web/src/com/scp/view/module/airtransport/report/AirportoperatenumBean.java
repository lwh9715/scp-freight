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
@ManagedBean(name = "pages.module.airtransport.report.airportoperatenumBean", scope = ManagedBeanScope.REQUEST)
public class AirportoperatenumBean extends GridView{
	
	@SaveState
	@Bind
	public String counttype = "A";
	
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
		if("A".equals(counttype)){
			qryData1();
		} else{
			qryData2();
		}
	}
	
	
	public void qryData1(){
		String ret = "";
		update.markUpdate(true, UpdateLevel.Data, "isContain");
		try{
			//if("0".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT  COALESCE(airportop,'') AS airportop, count(1) AS sumPiece FROM bus_air b, fina_jobs fj WHERE fj.id = b.jobid AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' AND b.isdelete = FALSE AND fj.isdelete = FALSE AND COALESCE(airportop,'') <> '' GROUP BY airportop";
					//System.out.println("sql--->"+sql);
//					List<Map> maplistList =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//					
//					ret = JSONArray.fromObject(maplistList).toString();
					 ret = DaoUtil.queryForJsonArray(sql);
				}
			//}
			//System.out.println(ret);
			jsonData = ret;
			
			
			if("1".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT COALESCE(fj.nos,'') AS jobno ,COALESCE(b.mawbno,'') AS hawbno, COALESCE(airportop,'') AS airportop, COALESCE(to_char(b.arrivaltime,'yyyy-mm-dd'),'') AS arrivaltime FROM bus_air b, fina_jobs fj WHERE fj.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id AND COALESCE(airportop,'') <> '' AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' ";
					//System.out.println("sql--->"+sql);
//					List<Map> maps = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//					ret = JSONArray.fromObject(maps).toString();
					 ret = DaoUtil.queryForJsonArray(sql);
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
	
	public void qryData2(){
		String ret = "";
		update.markUpdate(true, UpdateLevel.Data, "isContain");
		try{
			//if("0".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT  COALESCE(inputer2,'') AS airportop, count(1) AS sumPiece FROM bus_air b, fina_jobs fj WHERE fj.id = b.jobid AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' AND b.isdelete = FALSE AND fj.isdelete = FALSE AND COALESCE(inputer2,'') <> '' GROUP BY inputer2";
					//System.out.println("sql--->"+sql);
					List<Map> maplistList =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
					
					ret = JSONArray.fromObject(maplistList).toString();
				}
			//}
			//System.out.println(ret);
			jsonData = ret;
			
			
			if("1".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT COALESCE(fj.nos,'') AS jobno ,COALESCE(b.mawbno,'') AS hawbno, COALESCE(inputer2,'') AS airportop, COALESCE(to_char(b.arrivaltime,'yyyy-mm-dd'),'') AS arrivaltime FROM bus_air b, fina_jobs fj WHERE fj.isdelete = FALSE AND b.isdelete = FALSE AND b.jobid = fj.id AND COALESCE(inputer2,'') <> '' AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' ";
					//System.out.println("sql--->"+sql);
					List<Map> maps = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
					ret = JSONArray.fromObject(maps).toString();
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
