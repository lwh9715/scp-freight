package com.scp.dao.elogistics.data;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.elogistics.data.Elogistics;

@Component
@Lazy(true)
public class ElogisticsDao extends BaseDaoImpl<Elogistics, Long>{
	
	protected ElogisticsDao(Class<Elogistics> persistancesClass) {
		super(persistancesClass);
	}

	public ElogisticsDao() {
		this(Elogistics.class);
	}
	
	
}
