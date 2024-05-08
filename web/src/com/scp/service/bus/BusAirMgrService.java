package com.scp.service.bus;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusAirDao;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusAir;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class BusAirMgrService {

	@Resource
	public BusAirDao busAirDao;

	public void saveData(BusAir data) {
		if (0 == data.getId()) {
			busAirDao.create(data);
		} else {
			busAirDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusAir data = busAirDao.findById(id);
		busAirDao.remove(data);
	}
	public BusAir findjobsByJobid(Long jobid){
		BusAir busair = null;
		try {
			busair = this.busAirDao.findOneRowByClauseWhere("isdelete = FALSE AND jobid = "+jobid);
		}catch (NoRowException e) {
			busair = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busair;
	}
	
	public void updateGoodsSelects(String[] ids,Long id){
		if(id == null){
			return;
		}
		String sql = "UPDATE bus_goods SET isselect = FALSE WHERE isdelete = FALSE AND linkid = "+id;
		busAirDao.executeSQL(sql);
		if(ids!=null&&ids.length>0){
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("\n UPDATE bus_goods SET isselect = TRUE WHERE isdelete = FALSE ");
			sbsql.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
			busAirDao.executeSQL(sbsql.toString());
		}
	}

	public BusAir findByJobId(Long jobid) {
		String sql = "isdelete = false AND  jobid ="+jobid;
		return this.busAirDao.findOneRowByClauseWhere(sql);
	}
	
	public void billLink(Long jobid, String usercode) {
		try{
			String sql = "SELECT f_bus_air_mblno_link_jobno('jobid="+jobid+";usercode="+usercode+";')";
			busAirDao.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		
	}
	
	//回收提单号
	public void recallhbl(Long jobid, String usercode) {
		try{
			String sql = "SELECT f_bus_air_mblno_link_jobno('jobid="+jobid+";usercode="+usercode+";type=recall;')";
			busAirDao.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		
	}
	
//	public void updatePiece2AndVolume2(Long jobid, int piece2, float volume2){
//		String sql = "update bus_air set piece2 = "+piece2+", volume2 = "+volume2+" where jobid = "+jobid+" and isdelete = false";
//		busAirDao.executeSQL(sql);
//	}
	
}