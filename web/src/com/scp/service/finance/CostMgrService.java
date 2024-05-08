package com.scp.service.finance;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaCostDao;
import com.scp.model.finance.FinaArap;
import com.scp.util.StrUtils;

@Component
public class CostMgrService{
		
		@Resource
		public DaoIbatisTemplate daoIbatisTemplate;
		
		@Resource
		public FinaCostDao finaCostDao; 
		

		public void saveData(FinaArap data) {
			if(0 == data.getId()){
				finaCostDao.create(data);
			}else{
				finaCostDao.modify(data);
			}
		}




		public void removeDate(String[] ids , String user) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String id : ids) {
				String sql = "\nUPDATE fina_arap SET isdelete = TRUE , updater = '"+user+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuilder.append(sql);
			}
			String execSql = stringBuilder.toString();
			if(!StrUtils.isNull(execSql)) {
				finaCostDao.executeSQL(execSql);
			}
		}


		public String shareFee(String[] ids, String user) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < ids.length; i++) {
				String sql = "\nSELECT f_fina_fee_share('id="+ids[i]+";user="+user+"');";
				List list = finaCostDao.executeQuery(sql);
				stringBuilder.append(list.toString()+"\n");
			}
			
			return stringBuilder.toString();
		} 
		
		public void editAmount(String[]ids,String amounts,String xrate, String currency,String UserCode){
			String idss = StrUtils.array2List(ids);
			String sql = "select f_fina_amountedit('idss="+idss+";amounts="+amounts+";xrate="+xrate+";currency="+currency+";usercode="+UserCode+"');";
			finaCostDao.executeQuery(sql);
		}
//		String bookids = StrTools.array2List(ids);
//		//bookids = bookids.replaceAll(",", "-");
//		String sql="SELECT f_bus_book_choose('bookingids="+bookids+";shipid="+shipid+";inputer="+user+";userid="+userid+"');";
//		busShipBookingDao.executeQuery(sql);
		
	}

	


