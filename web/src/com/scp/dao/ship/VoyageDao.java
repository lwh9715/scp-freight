package com.scp.dao.ship;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.Voyage;


@Component
@Lazy(true)
public class VoyageDao extends BaseDaoImpl<Voyage, Long>{
	
	protected VoyageDao(Class<Voyage> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public VoyageDao() {
		this(Voyage.class);
	}
	

}
