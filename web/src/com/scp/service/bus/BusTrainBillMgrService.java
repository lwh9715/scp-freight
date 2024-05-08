package com.scp.service.bus;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusTrainBillDao;
import com.scp.model.bus.BusTrainBill;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusTrainBillMgrService{
	
	@Resource
	public BusTrainBillDao busTrainBillDao; 
	

	public void saveData(BusTrainBill data) {
		if(0 == data.getId()){
			busTrainBillDao.create(data);
		}else{
			busTrainBillDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		String sql = "UPDATE bus_train_bill SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id =" + id;
		this.busTrainBillDao.executeSQL(sql);
	}
	
	
	public void choosetrain(String[] ids, Long billid, String bltype,long jobid,boolean isDetail) {
		String dmlSql = "";
		if("H".equals(bltype)) {//HBL
			dmlSql = "\nUPDATE bus_ship_container SET billid = NULL WHERE   jobid  = "
				+ jobid + " AND billid="+billid+";";
			for (int i = 0; i < ids.length; i++) {
				dmlSql += "\nUPDATE bus_ship_container SET billid = " + billid
						+ " WHERE id = " + ids[i] + " AND jobid = "+jobid+";";
			}
		} else if ("M".equals(bltype)) {//MBL
			dmlSql = "\nUPDATE bus_ship_container SET billmblid = NULL WHERE jobid  = "
				+ jobid + " AND billmblid="+billid+";";
			for (int i = 0; i < ids.length; i++) {
				dmlSql += "\nUPDATE bus_ship_container SET billmblid = " + billid
						+ " WHERE id = " + ids[i] + " AND jobid = "+jobid+";";
			}
		}
		if (!StrUtils.isNull(dmlSql)) {
			busTrainBillDao.executeSQL(dmlSql);
			// 更新提单信息
			busTrainBillDao.executeQuery("SELECT f_bus_trainbill_container_createbillinfo('billid=" + billid + ";bltype=" + bltype + ";isdetail="+isDetail+"');");
		}
	}


	
	
	
}