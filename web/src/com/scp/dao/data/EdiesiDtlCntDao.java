package com.scp.dao.data;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.EdiesiDtlCnt;


@Component
@Lazy(true)
public class EdiesiDtlCntDao extends BaseDaoImpl<EdiesiDtlCnt, Long> {

	protected EdiesiDtlCntDao(Class<EdiesiDtlCnt> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public EdiesiDtlCntDao() {
		this(EdiesiDtlCnt.class);
	}
		
}
