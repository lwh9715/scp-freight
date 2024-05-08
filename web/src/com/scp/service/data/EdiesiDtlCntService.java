package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.model.data.EdiesiDtlCnt;


@Component
@Lazy(true)
public class EdiesiDtlCntService {
	
	@Resource
	public com.scp.dao.data.EdiesiDtlCntDao EdiesiDtlCntDao;
	
	public void saveData(EdiesiDtlCnt data) {
		if(0 == data.getId()){
			EdiesiDtlCntDao.create(data);
		}else{
			EdiesiDtlCntDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		EdiesiDtlCnt data = EdiesiDtlCntDao.findById(ids);
		if(data == null)return;
		EdiesiDtlCntDao.remove(data);
	} 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void removeDate(String[] ids) {
		for(int i=0;i<ids.length;i++){
			long date = Long.parseLong(ids[i]);
			removeDate(date);
		}
		
	}
}
