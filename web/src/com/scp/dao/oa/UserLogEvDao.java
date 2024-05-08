package com.scp.dao.oa;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.UserEval;
/**
 * 
 * @author
 */
@Component
public class UserLogEvDao extends BaseDaoImpl<UserEval, Long> {
	protected UserLogEvDao(Class<UserEval> persistancesClass) {
		super(persistancesClass);
	}
	public UserLogEvDao() {
		this(UserEval.class);
	}
}
