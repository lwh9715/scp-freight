package com.scp.service.ship;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.ship.BusShipjoinlinkDao;
import com.scp.model.ship.BusShipjoinlink;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

@Component
public class BusShipjoinlinkMgrService{
	

	
	
	@Resource
	public BusShipjoinlinkDao busShipjoinlinkDao; 
	

	public void saveData(BusShipjoinlink data) {
		if(0 == data.getId()){
			busShipjoinlinkDao.create(data);
		}else{
			busShipjoinlinkDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipjoinlink data = busShipjoinlinkDao.findById(id);
		busShipjoinlinkDao.remove(data);
	}

	public void cancel(Long unionid, String[] ids, String usercode, DaoIbatisTemplate daoIbatisTemplate) {
		for (int i = 0; i < ids.length; i++) {
			Long containerid = Long.valueOf(ids[i]);
			String sql = "SELECT f_bus_shipping_cancel('unionid= " + unionid + ";containerid=" + containerid + ";usercode=" + usercode + ";') as returntext;";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String returntext = StrUtils.getMapVal(map, "returntext");
			MessageUtils.alert(returntext);

			// LogBean.insertLastingLog(new StringBuffer().append(("并单移除工作单函数cancel()，")).append(sql.toString())
			// 		.append((",修改人为")).append(AppUtils.getUserSession().getUsercode()).append((",修改时间为")).append(new Date()));
		}
	}

	public void chooseShip(String[] ids, Long shipjoinid) {
		for(int i=0;i<ids.length;i++){
			Long containerid =Long.valueOf(ids[i]);
			String sql="SELECT f_bus_shipping_choose('containerid="+containerid+";shipjoinid="+shipjoinid+"');";
			busShipjoinlinkDao.executeQuery(sql);
		}
		
	}


	
}