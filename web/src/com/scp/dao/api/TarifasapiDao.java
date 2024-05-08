package com.scp.dao.api;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.TarifasApi;
import org.springframework.stereotype.Component;

/**
 * @author bruce
 */
@Component
public class TarifasapiDao extends BaseDaoImpl<TarifasApi, Long> {

	protected TarifasapiDao(Class<TarifasApi> persistancesClass) {
		super(persistancesClass);
	}

	public TarifasapiDao() {
		this(TarifasApi.class);
	}

}
