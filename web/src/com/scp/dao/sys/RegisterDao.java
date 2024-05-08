package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.Register;

/**
 * 
 * @author Neo
 */
@Component
public class RegisterDao extends BaseDaoImpl<Register, Long> {

	protected RegisterDao(Class<Register> persistancesClass) {
		super(persistancesClass);
	}

	public RegisterDao() {
		this(Register.class);
	}
}
