package com.scp.service.ship;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.ship.BusShippingDao;
import com.scp.model.ship.BusShipping;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusShippingMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public BusShippingDao busShippingDao; 
	

	public void saveData(BusShipping data) {
		if(0 == data.getId()){
			busShippingDao.create(data);
		}else{
			busShippingDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipping data = busShippingDao.findById(id);
		busShippingDao.remove(data);
	}

	/**
	 * Neo 2014/5/7
	 * 删除事件轨迹
	 * @param ids
	 */
	public void delOptrack(String[] ids) {
		String sql="DELETE FROM bus_optrack WHERE id IN("+StrUtils.array2List(ids)+");";
		this.busShippingDao.executeSQL(sql);
		
	}
	
	/**
	 * Neo 2014/5/30
	 * 根据联系人查找对应的信息
	 * @param custcontact
	 * @return 手机,电话,传真
	 */
	public List<Object[]> queryCustcontactInfo(String custcontact) {
		String sql = "SELECT phone,tel,fax FROM sys_corpcontacts WHERE name='"
				+ custcontact + "'";
		List<Object[]> info = this.busShippingDao.executeQuery(sql);
		if (info != null && info.size() > 0) {
			return info;
		}
		return null;
	}

	public BusShipping findByjobId(Long jobid) {
		String sql = "isdelete = false AND  jobid ="+jobid;
		
		return this.busShippingDao.findOneRowByClauseWhere(sql);
	} 
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		
		String sql = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			
			String jobid=String.valueOf(jsonObject.get("jobid"));
			String istelrel=String.valueOf(jsonObject.get("istelrel"));
			String telreler=String.valueOf(jsonObject.get("telreler"));
			String telreltime=String.valueOf(jsonObject.get("telreltime"));
			
			if(StrUtils.isNull(telreler) || "null".equals(telreler))telreler=AppUtils.getUserSession().getUsername();

			if(istelrel.toString().equals("true")){
				sql += "\nUPDATE bus_shipping SET istelrel ="+istelrel+",telreltime = now(), telreler = '"+telreler+"' WHERE jobid = "+jobid+";" +
					   "\nUPDATE bus_ship_bill SET istelrel ="+istelrel+",telreltime = now(), telreler = '"+telreler+"' WHERE id = "+jobid+";";
			}else{
				sql += "\nUPDATE bus_shipping SET istelrel =false,telreltime = null, telreler = null WHERE jobid = "+jobid+";" +
						"\nUPDATE bus_ship_bill SET istelrel =false,telreltime = null, telreler = null WHERE id = "+jobid+";";
			}
			
//			if(jsonObject.get("istelrelback").toString().equals("true")){
//				data.setIstelrelback(true);
//				if(jsonObject.get("telrelbacktime")!=null && !jsonObject.get("telrelbacktime").equals("")){
//					try {
//						data.setTelrelbacktime(sdf.parse((jsonObject.get("telrelbacktime")).toString()));
//					} catch (ParseException e) {
//						data.setTelrelbacktime(null);
//					}
//				}else{
//					data.setTelrelbacktime(Calendar.getInstance().getTime());
//				}
//			}else{
//				data.setIstelrelback(false);
//				data.setTelrelbacktime(null);
//			}
			
			String telrelnos=String.valueOf(jsonObject.get("telrelnos"));
			String telreltype=String.valueOf(jsonObject.get("telreltype"));
			String telrelrptid=String.valueOf(jsonObject.get("telrelrptidcode"));
			
			if(StrUtils.isNull(telrelnos) || "null".equals(telrelnos))telrelnos="";
			if(StrUtils.isNull(telreltype) || "null".equals(telreltype))telreltype="";
			
			sql += "\nUPDATE bus_shipping SET telrelnos ='"+telrelnos+"' WHERE jobid = "+jobid+";" +
				   "\nUPDATE bus_ship_bill SET telrelnos ='"+telrelnos+"' WHERE id = "+jobid+";";
			sql += "\nUPDATE bus_shipping SET telreltype ='"+telreltype+"' WHERE jobid = "+jobid+";" +
			   		"\nUPDATE bus_ship_bill SET telreltype ='"+telreltype+"' WHERE id = "+jobid+";";
			
			if(StrUtils.isNumber(telrelrptid)&&!StrUtils.isNull(telrelrptid)){
				sql += "\nUPDATE bus_shipping SET telrelrptid ="+telrelrptid+" WHERE jobid = "+jobid+";" +
   				"\nUPDATE bus_ship_bill SET telrelrptid ="+telrelrptid+" WHERE id = "+jobid+";";
			}
		}
		if(!StrUtils.isNull(sql)){
			busShippingDao.executeSQL(sql);
		}
	}
	
	public String findTipsCount(Long jobid) {
		String size;
		try {
			String sql = "SELECT precount+truckcount+billcount+clearcount+factoryinfocount"
						+"\n+remark1count+remark2count+remark3count+remark4count+remark5count AS cnts"
						+"\nFROM(SELECT "
						+"\n			 CASE WHEN COALESCE(claim_Pre,'') = '' THEN 0 ELSE 1 END AS precount"
						+"\n			,CASE WHEN COALESCE(claim_Truck,'') = '' THEN 0 ELSE 1 END AS truckcount"
						+"\n			,CASE WHEN COALESCE(claim_Bill,'') = '' THEN 0 ELSE 1 END AS billcount"
						+"\n			,CASE WHEN COALESCE(claim_Clear,'') = '' THEN 0 ELSE 1 END AS clearcount"
						+"\n			,CASE WHEN COALESCE(factoryinfo,'') = '' THEN 0 ELSE 1 END AS factoryinfocount"
						+"\n			,CASE WHEN COALESCE(remark1,'') = '' THEN 0 ELSE 1 END AS remark1count"
						+"\n			,CASE WHEN COALESCE(remark2,'') = '' THEN 0 ELSE 1 END AS remark2count"
						+"\n			,CASE WHEN COALESCE(remark3,'') = '' THEN 0 ELSE 1 END AS remark3count"
						+"\n			,CASE WHEN COALESCE(remark4,'') = '' THEN 0 ELSE 1 END AS remark4count"
						+"\n			,CASE WHEN COALESCE(remark5,'') = '' THEN 0 ELSE 1 END AS remark5count"
						+"\nFROM bus_shipping"
						+"\nWHERE isdelete = FALSE AND jobid = "+jobid+")t";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			size = StrUtils.getMapVal(map, "cnts");
			if("0".equals(size))size="";
			return size;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
}