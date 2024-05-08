package com.scp.service.del;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.del.DelDeliveryDao;
import com.scp.dao.del.DelDeliverydtlDao;
import com.scp.model.del.DelDelivery;
import com.scp.model.del.DelDeliverydtl;
import com.scp.util.StrUtils;

@Component
public class DeliverydtlMgrService {

	@Resource
	public DelDeliverydtlDao delDeliverydtlDao;

	@Resource
	public DelDeliveryDao delDeliveryDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(DelDelivery data) {
		if (0 == data.getId()) {
			delDeliveryDao.create(data);
		} else {
			delDeliveryDao.modify(data);
		}
	}

	public void saveDtlData(DelDeliverydtl data) {

		if (0 == data.getId()) {
			delDeliverydtlDao.create(data);
		} else {
			delDeliverydtlDao.modify(data);
		}
	}

	/**
	 * 删除一条明细则会删除所有对应的一个出库单明细
	 * @param ids
	 * @throws Exception
	 */
	public void delBatch(String[] ids) throws Exception{
		
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_deliverydtl SET isdelete=TRUE  WHERE id in (" + id
				+ ")";
		delDeliverydtlDao.executeSQL(sql);
			
	}



	public void saveDtl(String[] ids, Long loadid)throws Exception {
		for(int i=0;i<ids.length;i++){
		String sql="SELECT f_del_insertloaddtl('"+ids[i]+"',"+loadid+");";
		delDeliverydtlDao.executeQuery(sql);
		}
	}

	
	public void removeDate(Long id)throws Exception {
		String sql = 
				"UPDATE del_deliverydtl SET isdelete = TRUE WHERE id = " + id +";";
		delDeliverydtlDao.executeSQL(sql);
	}

	public void updateSignBatch(String[] ids,String signer,String remarks) throws Exception{
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_deliverydtl SET issign = TRUE ,signtime = NOW()"
			+ ",signer = '" + signer + "' ,remark = '"+remarks+"' WHERE id in ("+id+")";
		delDeliverydtlDao.executeSQL(sql);
	}

	
	public void updateUnSignBatch(String[] ids,String signer,String remarks)throws Exception {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_deliverydtl SET issign = FALSE ,signtime = NULL,signer = NULL WHERE id in ("+id+")";
		delDeliverydtlDao.executeSQL(sql);
		
	}
	
	public void updateBackBatch(String[] ids,String remarks) throws Exception{
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_deliverydtl SET isreturn = TRUE  ,remark = '"+remarks+"' WHERE id in ("+id+")";
		delDeliverydtlDao.executeSQL(sql);
	}


	public void updateUnBackBatch(String[] ids,String remarks)throws Exception {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE del_deliverydtl SET isreturn = FALSE  WHERE id in ("+id+")";
		delDeliverydtlDao.executeSQL(sql);
		
	}

	public String getLoadnos(Long refid)throws Exception {
		String sql="SELECT nos FROM del_load WHERE id="+refid+"";
		List list= delDeliverydtlDao.executeQuery(sql);
		if (list!= null && list.size() > 0) {
			return (String)list.get(0);
		}
		
		return "";
	}
}