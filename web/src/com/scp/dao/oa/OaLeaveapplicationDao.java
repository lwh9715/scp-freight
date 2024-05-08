	package com.scp.dao.oa;

	import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaLeaveapplication;
	/**
	* 
	 * @author 
	 */
	@Component
	public class OaLeaveapplicationDao extends BaseDaoImpl<OaLeaveapplication, Long>{
		
		protected OaLeaveapplicationDao(Class<OaLeaveapplication> persistancesClass) {
			super(persistancesClass);
		}

		public OaLeaveapplicationDao() {
			this(OaLeaveapplication.class);
		}
	}

