package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmCommentsTemp;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmCommentsTempDao extends BaseDaoImpl<BpmCommentsTemp, Long>{
	
	protected BpmCommentsTempDao(Class<BpmCommentsTemp> persistancesClass) {
		super(persistancesClass);
	}

	public BpmCommentsTempDao() {
		this(BpmCommentsTemp.class);
	}
	
	
}