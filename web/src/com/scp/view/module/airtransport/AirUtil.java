package com.scp.view.module.airtransport;


public class AirUtil {

	/**
	 * neo 20201230 排除之前单及无流程的单
	 *
	 */
	public static String getCommonFilter(){
		String filter = "";
		filter += "\nAND t.jobdate >= '2021-01-01'";
		filter += "\nAND EXISTS(select 1 from _bpm_task where refid = t.id::text)";
		
		return filter;
	}
}
