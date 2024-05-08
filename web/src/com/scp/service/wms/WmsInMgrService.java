package com.scp.service.wms;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.wms.WmsInDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsIn;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsInMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public WmsInDao wmsInDao; 

	public void saveData(WmsIn data) {
		if(0 == data.getId()){
			wmsInDao.create(data);
		}else{
			wmsInDao.modify(data);
		}
	}


	public void removeDate(Long id) {
		String sql = 
				"UPDATE wms_in SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id +";";
		wmsInDao.executeSQL(sql);
	}

   /**
    * 
    * @param ids
 * @throws Exception 
    */
	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_in SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		wmsInDao.executeSQL(sql);
	}
	
	public void updateReturn(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_in SET isreturn  = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		wmsInDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_in SET ischeck = false  WHERE id in ("+id+")";
		wmsInDao.executeSQL(sql);
	}

	/**
	 * 批量更改储位
	 * @param modifiedData
	 */
	public void saveDtlDatas(Object modifiedData,Long locid) {
		JSONArray jsonArray = (JSONArray) modifiedData;

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String locagent = String.valueOf(jsonObject.get("locagent"));
				String pieceou = String.valueOf(jsonObject.get("pieceout"));
//				int pieceinbox = Integer.parseInt(String.valueOf(jsonObject.get("pieceinbox")));
//				if(Integer.parseInt(pieceou)%pieceinbox!=0){
//					MsgUtil.alert("fail:出库件数%每箱件数=0");
//					return;
//				}
				Long indtlid = new Long(String.valueOf(jsonObject.get("id")));// 返回是long,不能直接将Long转为为integer
				Integer stocknum = new Integer(String.valueOf(jsonObject
						.get("pieces_desc")));// 返回是long,不能直接将Long转为为integer
				
				if (StrUtils.isNull(locagent) || StrUtils.isNull(pieceou)) {

				}else if(stocknum<new Integer(pieceou)){
					throw new CommonRuntimeException("调整数量>库存数量");
				} else {
					Integer pieceout = new Integer(pieceou);// 返回是long,不能直接将Long转为为integer
					String sql = "select f_wms_changelocal(" + indtlid + ","
							+ locid + "," + stocknum + "," + pieceout + ",'"
							+ locagent + "');";
					wmsInDao.executeQuery(sql);
				}
			}
			
		
	}


	public void updateSubmit(String[] ids) {
		String updater = AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_in SET isubmit = TRUE ,submitime = NOW(),submiter = '"+updater+"' WHERE id IN("+id+") AND isubmit = FALSE;";
		wmsInDao.executeSQL(sql);
	}


	public void updateCanceSubmit(String[] ids) {
		String updater = AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_in SET isubmit = FALSE ,submitime = NULL,submiter = NULL WHERE id IN("+id+") AND isubmit = TRUE;";
		wmsInDao.executeSQL(sql);
	}


	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql =  "UPDATE wms_in SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id IN("+id+");";
		wmsInDao.executeSQL(sql);
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		WmsIn data = new WmsIn();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = wmsInDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("cntid")!=null){
				data.setCntid(String.valueOf(jsonObject.get("cntid")));
			}else{
				data.setCntid(null);
			}
			if(jsonObject.get("cbmtotle")!=null){
				String sql = "update wms_indtl set cbmtotle = "+jsonObject.get("cbmtotle")+",updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = '"+new Date()+"' where id = (SELECT id FROM wms_indtl WHERE inid = "+data.getId()+" AND isdelete = FALSE ORDER BY id LIMIT 1);";
				wmsInDao.executeSQL(sql);
			}
			if(jsonObject.get("agentcnt")!=null){
				data.setAgentcnt(String.valueOf(jsonObject.get("agentcnt")));
			}else{
				data.setAgentcnt(null);
			}
			if(jsonObject.get("export")!=null){
				data.setExport(String.valueOf(jsonObject.get("export")));
			}else{
				data.setExport(null);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("ewd")!=null){
				try {
					data.setEwd(sdf.parse((jsonObject.get("ewd")).toString()));
				} catch (ParseException e) {
					data.setEwd(null);
					//e.printStackTrace();
				}
			}else{
				data.setEwd(null);
			}
			if(jsonObject.get("intime")!=null){
				try {
					data.setIntime(sdf.parse((jsonObject.get("intime")).toString()));
				} catch (ParseException e) {
					data.setIntime(null);
					//e.printStackTrace();
				}
			}else{
				data.setIntime(null);
			}
			
			if(jsonObject.get("customtitle")!=null){
				data.setCustomtitle(jsonObject.get("customtitle").toString());
			}
			if(jsonObject.get("customerno")!=null){
				data.setCustomerno(jsonObject.get("customerno").toString());
			}
			if(jsonObject.get("customtype")!=null){
				data.setCustomtype(jsonObject.get("customtype").toString());
			}
			if(jsonObject.get("custominfo")!=null){
				data.setCustominfo(jsonObject.get("custominfo").toString());
			}
			//System.out.println("data:"+data.getIsreturn());
			saveData(data);
		}
	}


	/**
	 * @param jobids
	 * @param ewdedit
	 * @param nationaldateedit
	 * @param polwarehouseid 
	 * @param string 
	 * @param remarkunusual 
	 * @param string 
	 * @param sovoledit 
	 * @param wgttotleedit 
	 * @param pieceedit 
	 * @param userCode
	 */
	public void batchupUpdateEwdAndNationaldate(String[] ids,Date ewdedit, Date nationaldateedit, Long polwarehouseid, String remark, String remarkunusual,  BigDecimal sovoledit, String custominfoedit,  String usercode,Integer customvotes) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("UPDATE wms_in SET ");
		if(ewdedit != null) stringBuilder.append("ewd  ='"+ewdedit+"',");
		if(nationaldateedit != null) stringBuilder.append("nationaldate = '"+nationaldateedit+"',");
		if(!StrUtils.isNull(remark)) stringBuilder.append("remark = '"+remark+"',");
		if(!StrUtils.isNull(remarkunusual)) stringBuilder.append("remarkunusual = '"+remarkunusual+"',");
		if(sovoledit != null) stringBuilder.append("sovol = "+sovoledit+",");
		if(polwarehouseid != null) stringBuilder.append("polwarehouseid = "+polwarehouseid+",");
		if(!StrUtils.isNull(custominfoedit)) stringBuilder.append("custominfo = '"+custominfoedit+"',");
		if(customvotes!=null&&customvotes>=0){
			stringBuilder.append("customvotes = "+customvotes+",");
		}
		stringBuilder.append("updater = '"+usercode+"', updatetime= NOW() where id IN ("+(StrUtils.array2List(ids))+")");
		
		
		//String sql = "update wms_in set ewd = '"+ewd+"',nationaldate = '"+nationaldate+"',updater = '"+usercode+"', updatetime= NOW() where id IN ("+(StrUtils.array2List(ids))+")";
		wmsInDao.executeSQL(stringBuilder.toString());
	}


	public void batchupUpdateForTrain(String[] ids,java.util.Date intime, java.util.Date nationaldate, String remark, String remarkunusual,  BigDecimal sovoledit, String custominfoedit,java.util.Date traindate,  String usercode,Integer customvotes) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("UPDATE wms_in SET ");
		if(intime != null) stringBuilder.append("intime  ='"+intime+"',");
		if(nationaldate != null) stringBuilder.append("nationaldate = '"+nationaldate+"',");
		if(traindate != null) stringBuilder.append("traindate = '"+traindate+"',");
		if(!StrUtils.isNull(remark)) stringBuilder.append("remark = '"+remark+"',");
		if(!StrUtils.isNull(remarkunusual)) stringBuilder.append("remarkunusual = '"+remarkunusual+"',");
		if(sovoledit != null) stringBuilder.append("sovol = "+sovoledit+",");
		if(!StrUtils.isNull(custominfoedit)) stringBuilder.append("custominfo = '"+custominfoedit+"',");
		if(customvotes!=null&&customvotes>=0){
			stringBuilder.append("customvotes = "+customvotes+",");
		}
		stringBuilder.append("updater = '"+usercode+"', updatetime= NOW() where id IN ("+(StrUtils.array2List(ids))+")");
		
		
		//String sql = "update wms_in set ewd = '"+ewd+"',nationaldate = '"+nationaldate+"',updater = '"+usercode+"', updatetime= NOW() where id IN ("+(StrUtils.array2List(ids))+")";
		wmsInDao.executeSQL(stringBuilder.toString());
	}


}
