package com.scp.service.del;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.del.DelLoadDao;
import com.scp.dao.del.DelLoadtlDao;
import com.scp.model.del.DelLoad;
import com.scp.model.del.DelLoadtl;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class DelLoaddtlMgrService {

	@Resource
	public DelLoadtlDao delLoadtlDao;

	@Resource
	public DelLoadDao delLoadDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(DelLoad data) {
		if (0 == data.getId()) {
			delLoadDao.create(data);
		} else {
			delLoadDao.modify(data);
		}
	}

	public void saveDtlData(DelLoadtl data) {

		if (0 == data.getId()) {
			delLoadtlDao.create(data);
		} else {
			delLoadtlDao.modify(data);
		}
	}

	/**
	 * 删除一条明细则会删除所有对应的一个出库单明细
	 * @param ids
	 * @throws Exception
	 */
	public void delBatch(String[] ids) throws Exception{
		String sql="UPDATE  del_loadtl SET isdelete = TRUE WHERE id in("+StrUtils.array2List(ids)+");";
		delLoadtlDao.executeSQL(sql);
	}
	
	public void delBatch(Long loadid, String[] ids) throws Exception{
		//String sql="UPDATE  del_loadtl SET isdelete = TRUE WHERE id in("+StrUtils.array2List(ids)+");";
		String sql = "SELECT f_del_loadtl_delete('user="+AppUtils.getUserSession().getUsercode()+";loadid="+loadid+";wmsid="+StrUtils.array2List(ids)+"')";
		daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
	}



	public void saveDtl(String[] ids, Long loadid)throws Exception {
		for(int i=0;i<ids.length;i++){
		String sql="SELECT f_del_insertloaddtl('"+ids[i]+"',"+loadid+");";
		delLoadtlDao.executeQuery(sql);
		}
	}

	
}