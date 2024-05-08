package com.scp.service.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.api.ApiMaerskPortDao;
import com.scp.dao.api.ApiMaerskPriceSubDtlDao;
import com.scp.dao.api.ApiMaerskPricesubDao;
import com.scp.model.api.ApiMaerskPort;
import com.scp.model.api.ApiMaerskPriceSub;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class ApiMaerskService {

	@Resource
	public ApiMaerskPortDao apiMaerskPortDao;

	public void saveDataApiMaerskPort(ApiMaerskPort data) {
		if (0 == data.getId()) {
			apiMaerskPortDao.create(data);
		} else {
			apiMaerskPortDao.modify(data);
		}
	}
	
	@Resource
	public ApiMaerskPricesubDao apiMaerskPricesubDao;
	
	@Resource
	public ApiMaerskPriceSubDtlDao apiMaerskPriceSubDtlDao;

	public void saveDataApiMaerskPricesub(ApiMaerskPriceSub data) {
		if (0 == data.getId()) {
			apiMaerskPricesubDao.create(data);
		} else {
			apiMaerskPricesubDao.modify(data);
		}
	}
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	/**
	 * 请求马士基List origin ports with valid offers接口，将返回数据插入api_maersk_port表
	 */
	public void imporMaerskPortPol(){
		String returnjson = AppUtils.sendGetToMSJ("https://offers.api.maersk.com/offers/v2/offers/brand/maeu/locations/origins","includeContainerInfo=false");
		daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_import_api_maersk_port('ispol=true;ispod=false','"+StrUtils.getSqlFormat(returnjson)+"')");
	}
	
	/**
	 * 请求马士基List destination ports with valid offers接口，将返回数据插入api_maersk_port表
	 */
	public void imporMaerskPortPod(){
		String returnjson = AppUtils.sendGetToMSJ("https://offers.api.maersk.com//offers/v2/offers/brand/maeu/locations/destinations","includeContainerInfo=false");
		daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_import_api_maersk_port('ispol=false;ispod=true','"+StrUtils.getSqlFormat(returnjson)+"')");
	}
	
	/**
	 * 删除相同请求的数据
	 * @param ids
	 */
	public void deleteApiMaerskPricesubOnLastId(String[] ids){
		for(String id:ids){
			ApiMaerskPriceSub sub = apiMaerskPricesubDao.findById(Long.parseLong(id));
			List<ApiMaerskPriceSub> seletesubs = apiMaerskPricesubDao.findAllByClauseWhere(" pol = '"+sub.getPol()+"' AND pod = '"
					+sub.getPod()+"' AND cntcode = '"+sub.getCntcode()+"' AND vessel = '"+sub.getVessel()+"' AND voyage = '"+sub.getVoyage()+"'");
			for(ApiMaerskPriceSub s:seletesubs){
				apiMaerskPricesubDao.remove(s);
			}
		}
	}

	public void saveData(ApiMaerskPort data) {
		if(0 == data.getId()){
			apiMaerskPortDao.create(data);
		}else{
			apiMaerskPortDao.modify(data);
		}
		
	}

	public void removeDate(long id) {
		ApiMaerskPort data = apiMaerskPortDao.findById(id);
		apiMaerskPortDao.remove(data);
	}
	
}
