package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysAttachment;



/**
* 
 * @author neo
 */
@Component
public class SysAttachmentDao extends BaseDaoImpl<SysAttachment, Long>{
	
	protected SysAttachmentDao(Class<SysAttachment> persistancesClass) {
		super(persistancesClass);
	}

	public SysAttachmentDao() {
		this(SysAttachment.class);
	}
}