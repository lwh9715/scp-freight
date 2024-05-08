package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;

/**
 * 
 * @author
 */
@Component
public class OaDatFiledataDao extends BaseDaoImpl<OaDatFiledata, Long> {

	protected OaDatFiledataDao(Class<OaDatFiledata> persistancesClass) {
		super(persistancesClass);
	}

	public OaDatFiledataDao() {
		this(OaDatFiledata.class);
	}
}
