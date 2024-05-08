package com.scp.dao.data;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.Ediesi;


@Component
@Lazy(true)
public class EdiesiDao extends BaseDaoImpl<Ediesi, Long>{
	
	protected EdiesiDao(Class<Ediesi> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public EdiesiDao() {
		this(Ediesi.class);
	}
	

}
