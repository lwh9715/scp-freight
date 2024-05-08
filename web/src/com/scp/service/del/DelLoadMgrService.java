package com.scp.service.del;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.del.DelLoadDao;
import com.scp.model.del.DelLoad;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class DelLoadMgrService{
	
	
	@Resource
	public DelLoadDao delLoadDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	

	public void saveData(DelLoad data) {
		if(0 == data.getId()){
			delLoadDao.create(data);
		}else{
			delLoadDao.modify(data);
		}
	}


	public void removeDate(Long id) {
		String sql = 
				"UPDATE del_load SET isdelete = TRUE WHERE id = " + id +";";
		delLoadDao.executeSQL(sql);
	}

   /**
    * 
    * @param ids
 * @throws Exception 
    */
	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_load SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		delLoadDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_load SET ischeck = false  WHERE id in ("+id+")";
		//System.out.println(sql);
		delLoadDao.executeSQL(sql);
	}


	public String getCarType(Long carid) {
		String sql="SELECT f_del_getcartype("+carid+") as cardesc";
		List list= delLoadDao.executeQuery(sql);
		if (list!= null && list.size() > 0) {
			return (String)list.get(0);
		}
		
		return "";
	}


	public void chooseGoods(Long mPkVal) {
		String sql ="SELECT f_bus_ship_loadchoose('loadid="+mPkVal+"');";
		delLoadDao.executeQuery(sql);
		
	}


	public BigDecimal[] getLoaddesc(Long mPkVal) {
		BigDecimal[] re = new BigDecimal[2];
		String sql ="SELECT SUM(COALESCE(d.gdscbm,0))AS loadcbm ,SUM(COALESCE(d.gdswgt,0))AS loadgwt FROM del_loadtl l ,wms_outdtl d WHERE l.loadid ="+mPkVal+" AND l.outdtlid = d.id AND d.isdelete = false AND l.isdelete = false ";
		Map m=daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		re[0]=(BigDecimal)m.get("loadcbm");
		re[1]=(BigDecimal)m.get("loadgwt");
		return re;
		
	}
	
	public void updateSubmit(String[] ids) {
		String updater = AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_load SET isubmit = TRUE ,submitime = NOW(),submiter = '"+updater+"' WHERE id IN("+id+") AND isubmit = FALSE;";
		delLoadDao.executeSQL(sql);
	}
	
	public void updateCanceSubmit(String[] ids) {
		String updater = AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_load SET isubmit = FALSE ,submitime = NULL,submiter = NULL WHERE id IN("+id+") AND isubmit = TRUE;";
		delLoadDao.executeSQL(sql);
	}

	
}
