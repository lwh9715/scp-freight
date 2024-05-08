package com.scp.service.bus;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusCustomsDao;
import com.scp.model.ship.BusCustoms;
import com.scp.util.StrUtils;

@Component
public class BusCustomsMgrService {

	@Resource
	public BusCustomsDao busCustomsDao;
	
	public void saveData(BusCustoms data) {
		if(0 == data.getId()){
			busCustomsDao.create(data);
		}else{
			busCustomsDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
//		DatFiledata data = datFiledataDao.findById(id);
//		datFiledataDao.remove(data);
		String sql="UPDATE bus_customs SET isdelete=TRUE where id=" + id;
		busCustomsDao.executeSQL(sql);
	}

	/*
	 * 关联报关和柜子
	 * @param customsid
	 * @param ids
	 */
	public void busCustomsLink(Long customsid, String[] ids) throws Exception {
		String sql1 = "SELECT f_bus_customs_relation_cnt('customsid="+customsid+";ids="+StrUtils.array2List(ids)+"');";
		this.busCustomsDao.executeQuery(sql1);
	}

	/*
	 * 根据报关id获得关联的所有货柜id
	 * 所有货柜id按"xxx,xxx,xxx..."方式拼接
	 * Neo 2014/4/30
	 * @param truckid
	 * @return 拼接的货柜id字符串
	 */
	public String getLinkContainersid(Long customsid) {
		String sql = "SELECT containerid FROM bus_customs_link WHERE customsid=" + customsid;
		List<Long> containerids = this.busCustomsDao.executeQuery(sql);
		StringBuffer sb = new StringBuffer();
		if(containerids == null) {
			return null;
		}
		for(int i=0;i<containerids.size();i++) {
			if(i == (containerids.size()-1)) {
				sb.append(containerids.get(i));
			} else {
				sb.append(containerids.get(i) + ",");
			}
		}
		return sb.toString();
	}

	/*
	 * 根据jobid过滤得到所有的报关信息
	 * @param jobid
	 * @return
	 */
	public List<BusCustoms> getBusCustomsListByJobid(Long jobid) {
		return getBusCustomsListByJobid(jobid,"O");
	} 
	
	
	public List<BusCustoms> getBusCustomsListByJobid(Long jobid , String clstype) {
		String whereSql = "isdelete = false AND jobid = " + jobid + " AND clstype = '" + clstype + "'"+" AND nos IS NOT NULL AND nos <> ''";
		return this.busCustomsDao.findAllByClauseWhere(whereSql);
	}
	
	public List<BusCustoms> getBusCustomsListByJobidClearancenos(Long jobid , String clstype) {
		String whereSql = "isdelete = false AND jobid = " + jobid + 
			" AND clstype = '" + clstype + "'"+" AND clearancenos <> '' AND clearancenos IS NOT NULL";
		return this.busCustomsDao.findAllByClauseWhere(whereSql);
	}
}
