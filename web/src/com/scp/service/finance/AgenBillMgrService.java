package com.scp.service.finance;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaAgenBillDao;
import com.scp.model.finance.FinaAgenBill;
import com.scp.util.StrUtils;

@Component
public class AgenBillMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaAgenBillDao finaAgenBillDao; 
	
	
	

	public void saveData(FinaAgenBill data) {
		if(0 == data.getId()){
			finaAgenBillDao.create(data);
		}else{
			finaAgenBillDao.modify(data);
		}
	}

	public void removeDate(Long id , String updater) {
		//FinaAgenBill data = finaAgenBillDao.findById(id);
		//finaAgenBillDao.remove(data);
//		String updater=AppUtils.getUserSession().getUsername();
		String sql = "UPDATE fina_agenbill SET  updater ='"+updater+"',updatetime = NOW(),isdelete = TRUE WHERE id = " + id;
		finaAgenBillDao.executeSQL(sql);
	} 
	
	/**
	 * 根据jobid过滤得到所有的代理账单信息
	 * @param jobid
	 * @return
	 */
	public List<FinaAgenBill> getFinaAgenBillListByJobid(Long jobid) {
		String whereSql = "isdelete = false AND jobid = " + jobid + "ORDER BY nos";
		return this.finaAgenBillDao.findAllByClauseWhere(whereSql);
	}

	public Long genAsJobPorfit(Long jobid) {
		String sql = "SELECT f_fs_agenbill_create_profit('jobid="+jobid+"') AS id";
		List list = finaAgenBillDao.executeQuery(sql);
		String ret = (String) list.get(0);
		return Long.parseLong(ret) ;
	}

	public Long genAsJobFee(String jobid , String[] ids) {
		String sql = "SELECT f_fs_agenbill_create_fee('jobid="+jobid+";arapids="+StrUtils.array2List(ids)+"') AS id";
		List list = finaAgenBillDao.executeQuery(sql);
		String ret = (String) list.get(0);
		return Long.parseLong(ret) ;
	}

	public Long genAsJobPorfitLink(Long jobid) {
		String sql = "SELECT f_fs_agenbill_create_profit_link('jobid="+jobid+"') AS id";
		List list = finaAgenBillDao.executeQuery(sql);
		String ret = (String) list.get(0);
		return Long.parseLong(ret) ;
	}
	
}
