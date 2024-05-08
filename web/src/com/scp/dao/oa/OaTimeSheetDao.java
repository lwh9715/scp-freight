package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaTimeSheet;

/**
 * 
 * @author
 */
@Component
public class OaTimeSheetDao extends BaseDaoImpl<OaTimeSheet, Long> {

	protected OaTimeSheetDao(Class<OaTimeSheet> persistancesClass) {
		super(persistancesClass);
	}

	public OaTimeSheetDao() {
		this(OaTimeSheet.class);
	}
}
