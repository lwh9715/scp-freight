package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaArap;



	/**
	* 
	 * @author Neo
	 */
	@Component
public class FinaCostDao extends BaseDaoImpl<FinaArap, Long>{
		
		protected FinaCostDao(Class<FinaArap> persistancesClass) {
			super(persistancesClass);
		}

		public FinaCostDao() {
			this(FinaArap.class);
		}
	

}
