package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.Datdoor2doorday;



/**
* 
 * @author neo
 */
@Component
public class Datdoor2doordayDao extends BaseDaoImpl<Datdoor2doorday, Long>{
	
	protected Datdoor2doordayDao(Class<Datdoor2doorday> persistancesClass) {
		super(persistancesClass);
	}

	public Datdoor2doordayDao() {
		this(Datdoor2doorday.class);
	}
}