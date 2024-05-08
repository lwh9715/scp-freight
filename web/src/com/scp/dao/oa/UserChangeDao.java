package com.scp.dao.oa;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.UserChange;
/**
 * 
 * @author
 */
@Component
public class UserChangeDao extends BaseDaoImpl<UserChange, Long> {
	protected UserChangeDao(Class<UserChange> persistancesClass) {
		super(persistancesClass);
	}

	public UserChangeDao() {
		this(UserChange.class);
	}
}
