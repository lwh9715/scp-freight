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
@ManagedBean(name = "pages.module.airtransport.report.airporttallyBean", scope = ManagedBeanScope.REQUEST)
public class AirporttallyBean extends GridView{
	
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
					String sql = "SELECT COALESCE(arranger,'') AS arranger ,SUM(COALESCE(weight2,0)) AS weightSum,sum(COALESCE(chargeweight2,0)) AS chargeweightSum FROM bus_air ba, fina_jobs fj WHERE fj.isdelete = FALSE AND ba.isdelete = FALSE AND ba.jobid = fj.id AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' GROUP BY ba.arranger";
					//System.out.println("sql--->"+sql);
//					List<Map> maplistList =  this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//					/*for(Map<String, Object> map : maplistList){
//						//System.out.println("理货人："+map.get("arranger")+"毛重："+map.get("weightsum")+"计费重："+map.get("chargeweightsum"));
//						//ret += JSONArray.fromObject(map).toString();
//					}*/
//					ret = JSONArray.fromObject(maplistList).toString();
					 ret = DaoUtil.queryForJsonArray(sql);
				}
			//}
			//System.out.println(ret);
			jsonData = ret;
			
			
			if("1".equals(isContain)){
				if(dateStart != null && dateEnd != null){
					String sql = "SELECT COALESCE(fj.nos,'') AS jobno ,COALESCE(ba.mawbno,'') AS hawbno,COALESCE(to_char(ba.arrivaltime,'yyyy-mm-dd'),'') AS arrivaltime, COALESCE(ba.arranger,'') AS arranger, COALESCE(weight2,0) AS weight, COALESCE(chargeweight2,0) AS chargeweight FROM bus_air ba, fina_jobs fj WHERE fj.isdelete = FALSE AND ba.isdelete = FALSE AND ba.jobid = fj.id AND fj.jobdate BETWEEN '"+dateStart+"' AND '"+dateEnd +"' ";
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
