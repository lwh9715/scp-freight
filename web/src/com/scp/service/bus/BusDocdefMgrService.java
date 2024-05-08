package com.scp.service.bus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusDocdefDao;
import com.scp.model.bus.BusDocdef;
import com.scp.util.StrUtils;

@Component
public class BusDocdefMgrService{
	

	
	
	@Resource
	public BusDocdefDao busDocdefDao; 
	

	public void saveData(BusDocdef data) {
		if(0 == data.getId()){
			busDocdefDao.create(data);
		}else{
			busDocdefDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusDocdef data = busDocdefDao.findById(id);
		busDocdefDao.remove(data);
	}

	
	public void choose(String[] ids) {
		
		
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_docdef SET ischoose = true WHERE id in ("+id+")";
		busDocdefDao.executeSQL(sql);
	} 
	

	public void cancel(String[] ids) {
		
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_docdef SET ischoose = false WHERE id in ("+id+")";
		busDocdefDao.executeSQL(sql);
	}

	public String[] generateMailInfo(Long pkVal, Long userid) {
		String sql = "SELECT f_sys_mail_generate('type=shipdocsend;id="+pkVal+";userid="+userid+"') AS info";
		List list = busDocdefDao.executeQuery(sql);
		if(list == null || list.size() < 0) {
			return null;
		}else{
		String txt = (String) list.get(0);
		String[] mailInfos = txt.split("-.-.-");
		return mailInfos;
		}
	}

	public void saveDtlDatas(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusDocdef ddata = new BusDocdef();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			if(!StrUtils.isNull(id)){
				ddata = this.busDocdefDao.findById(Long.valueOf(id));
				String expno=String.valueOf(jsonObject.get("expno") == null ? "" :jsonObject.get("expno"));
				String sender=String.valueOf(jsonObject.get("sender") == null ? "" :jsonObject.get("sender"));
				String sendate=String.valueOf(jsonObject.get("sendate") == null ? "" :jsonObject.get("sendate"));
				ddata.setExpno(expno);
				ddata.setSender(sender);
				////System.out.println(sendate);
				if(!StrUtils.isNull(sendate)){
					SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
					try {
						////System.out.print(dateFormater.parse(sendate));
						ddata.setSendate(dateFormater.parse(sendate));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					
				}
				saveData(ddata);
			}
		}
	}
	
	/**
	 * 获取寄单信息
	 * @param shipid
	 * @return
	 */
	public List<BusDocdef> getDocdefByShipId(Long shipid){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("\n isdelete = FALSE");
		sbsql.append("\n AND linkid = "+shipid);
		sbsql.append("\n ORDER BY code");
		return this.busDocdefDao.findAllByClauseWhere(sbsql.toString());
	}
}