package com.scp.service.price;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.price.PricefclfeeaddDao;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.util.AppUtils;

@Component
public class PricefclfeeaddMgrService {
	
	@Resource
	public PricefclfeeaddDao pricefclfeeaddDao;

	public void saveOrModify(List<PriceFclFeeadd> moduleList) {
		for (PriceFclFeeadd instance : moduleList) {
			pricefclfeeaddDao.createOrModify(instance);
		}
	}

	public void removes(List<Long> lists) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Long id : lists) {
			String sql = "\nUPDATE price_fcl_feeadd SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclfeeaddDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void removes(Long id) {
		if(id>0){
			String sql = "\nUPDATE price_fcl_feeadd SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			pricefclfeeaddDao.executeSQL(sql);
		}
	}
	
}
