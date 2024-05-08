package com.scp.service.bus;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusTruckDao;
import com.scp.exception.NoRowException;
import com.scp.model.ship.BusTruck;
import com.scp.util.StrUtils;

@Component
public class BusTruckMgrService {

	@Resource
	public BusTruckDao busTruckDao;
	
	public void saveData(BusTruck data) {
		if(0 == data.getId()){
			busTruckDao.create(data);
		}else{
			busTruckDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
//		DatFiledata data = datFiledataDao.findById(id);
//		datFiledataDao.remove(data);
		String sql="UPDATE bus_truck SET isdelete=TRUE where id=" + id ;
		busTruckDao.executeSQL(sql);
	}

	/**
	 * 关联拖车和箱/柜
	 * 每次关联会先删除旧关联，然后建立新关联
	 * 如果一个箱/柜都没有选，直接删除旧关联，不建立任何新关联·
	 * Neo 2014/4/30
	 * @param truckid
	 * @param ids
	 */
	public void busTruckLink(Long truckid, String[] ids) {
		//删除关联
		String sqlDel = "DELETE FROM bus_truck_link WHERE truckid=" + truckid;
		this.busTruckDao.executeSQL(sqlDel);
		if(ids != null) {
			//建立关联
			for(String id : ids) {
				String sqlAdd = "INSERT INTO bus_truck_link (id,truckid,containerid) VALUES(getid()," + truckid + "," + id + ")";
				this.busTruckDao.executeSQL(sqlAdd);
			}
		}
	}

	/**
	 * 根据拖车id获得关联的所有货柜id
	 * 所有货柜id按"xxx,xxx,xxx..."方式拼接
	 * Neo 2014/4/30
	 * @param truckid
	 * @return 拼接的货柜id字符串
	 */
	public String getLinkContainersid(Long truckid) {
		String sql = "SELECT containerid FROM bus_truck_link WHERE truckid=" + truckid;
		List<Long> containerids = this.busTruckDao.executeQuery(sql);
		StringBuffer sb = new StringBuffer();
		sb.append("'");
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
		sb.append("'");
		return sb.toString();
	}

	/**
	 * Neo 2014/5/8
	 * 根据当前登陆用户查询历史录入的拖车纪录中所有司机
	 * @param user
	 * @return 
	 */
	public List<SelectItem> queryDriversByInputer(String user, Long truckid) {
		if(user != null) {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT driver FROM bus_truck WHERE driver IS NOT NULL AND inputer = '" 
						+ user + "'" + "  AND truckid=" + truckid +"  AND areatype='C' ";
			List<String> drivers = this.busTruckDao.executeQuery(sql);
			for(String di : drivers) {
				items.add(new SelectItem(di, di));
			}
			items.add(new SelectItem(null, ""));
			return items;
		}
		return null;
	}
	
	public List<SelectItem> foreignqueryDriversByInputer(String user, Long truckid) {
		if(user != null) {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT driver FROM bus_truck WHERE driver IS NOT NULL AND inputer = '" 
						+ user + "'" + "  AND truckid=" + truckid +"  AND areatype='D' ";
			List<String> drivers = this.busTruckDao.executeQuery(sql);
			for(String di : drivers) {
				items.add(new SelectItem(di, di));
			}
			items.add(new SelectItem(null, ""));
			return items;
		}
		return null;
	}

	/**
	 * Neo 2014/5/8
	 * 根据司机查询拖车纪录中对应的司机信息
	 * @param driver
	 * @return 车牌,电话,手机
	 */
	public String[] queryDriverInfo(String driver) {
		String sql = "SELECT driverno,drivertel,drivermobile FROM bus_truck WHERE driver = '" + driver + "'"+"  AND areatype='C' ";
		List<Object[]> info = this.busTruckDao.executeQuery(sql);
		if(info != null && info.size() > 0) {
			Object[] obj = info.get(0);
			return new String[]{obj[0].toString(), obj[1].toString(), obj[2].toString()};
		}
		return null;
	}
	
	/**
	 * 根据司机查询拖车纪录中对应的司机信息
	 * @param driver
	 * @return 车牌,电话,手机
	 */
	public String[] foreignqueryDriverInfo(String driver) {
		String sql = "SELECT driverno,drivertel,drivermobile FROM bus_truck WHERE driver = '" + driver + "'" +"   AND areatype='D'";
		List<Object[]> info = this.busTruckDao.executeQuery(sql);
		if(info != null && info.size() > 0) {
			Object[] obj = info.get(0);
			return new String[]{obj[0].toString(), obj[1].toString(), obj[2].toString()};
		}
		return null;
	}
	

	/**
	 * Neo 2014/5/8
	 * 根据当前登陆用户查询历史录入的拖车纪录中所有装货人
	 * @param user
	 * @return 
	 */
	public List<SelectItem> queryLoadContactsByInputer(String user, String customerAbbr) {
		if(user != null) {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT loadcontact FROM _bus_truck WHERE loadcontact IS NOT NULL AND  inputer = '" 
						+ user + "'" + " AND customerabbr = '" + StrUtils.getSqlFormat(customerAbbr) + "'" +"  AND areatype='C' ";
			List<String> loadcontacts = this.busTruckDao.executeQuery(sql);
			for(String lc : loadcontacts) {
				items.add(new SelectItem(lc, lc));
			}
			items.add(new SelectItem(null, ""));
			return items;
		}
		return null;
	}
	
	public List<SelectItem> foreignqueryLoadContactsByInputer(String user, String customerAbbr) {
		if(user != null) {
			List<SelectItem> items = new ArrayList<SelectItem>();
			String sql = "SELECT DISTINCT loadcontact FROM _bus_truck WHERE loadcontact IS NOT NULL AND  inputer = '" 
						+ user + "'" + " AND customerabbr = '" + customerAbbr + "'" +"  AND areatype='D'";
			List<String> loadcontacts = this.busTruckDao.executeQuery(sql);
			for(String lc : loadcontacts) {
				items.add(new SelectItem(lc, lc));
			}
			items.add(new SelectItem(null, ""));
			return items;
		}
		return null;
	}

	/**
	 * Neo 2014/5/8
	 * 根据司机查询拖车纪录中对应的装货人信息
	 * @param driver
	 * @return 电话,手机,地址,备注
	 */
	public String[] queryLoadContactInfo(String loadcontact) {
		String sql = "SELECT contacttel,contactmobile,loadaddress,loadremarks FROM bus_truck WHERE loadcontact = '" + loadcontact + "'" +"   AND areatype='C' ";
		List<Object[]> info = this.busTruckDao.executeQuery(sql);
		if(info != null && info.size() > 0) {
			Object[] obj = info.get(0);
			return new String[]{obj[0].toString(), obj[1].toString(), obj[2].toString(), obj[3].toString()};
		}
		return null;
	}

	/**
	 * 根据jobid过滤得到所有的拖车信息
	 * @param jobid
	 * @return
	 */
	public List<BusTruck> getBusTruckListByJobid(Long jobid) {
		String whereSql = "isdelete = false AND jobid = " + jobid +"   AND areatype='C' ";
		return this.busTruckDao.findAllByClauseWhere(whereSql);
	} 
	
	/**
	 * 根据jobid过滤得到国外的拖车信息
	 * @param jobid
	 * @return
	 */
	public List<BusTruck> getForeignBusTruckListByJobid(Long jobid) {
		String whereSql = "areatype='D' AND isdelete = false AND jobid = " + jobid ;
		return this.busTruckDao.findAllByClauseWhere(whereSql);
	} 
	
	public BusTruck findjobsByJobid(Long jobid){
		BusTruck busTruck = null;
		try {
			busTruck = this.busTruckDao.findOneRowByClauseWhere("isdelete = FALSE AND jobid = "+jobid);
		}catch (NoRowException e) {
			busTruck = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busTruck;
	}
	
	public BusTruck findByJobId(Long jobid) {
		String sql = "isdelete = false AND jobid ="+jobid;
		return this.busTruckDao.findOneRowByClauseWhere(sql);
	}
}
