package com.scp.servlet;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;

public class ExtJsonHandler {
	@Resource
	public ServiceContext serviceContext;
	
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request){
		String reqStr = "";//条件
		try {
			reqStr = URLDecoder.decode(request.getParameter("q"), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			if("pol".equals(action)) {
				result = this.queryPol(request,reqStr);
			}else if("pod".equals(action)) {
				result = this.queryPod(request,reqStr);
			}else if("pdd".equals(action)) {
				result = this.queryPdd(request,reqStr);
			}else if("pot".equals(action)) {
				result = this.queryPot(request,reqStr);
			}else if("destination".equals(action)) {
				result = this.queryDestination(request,reqStr);
			}
		} catch (Exception e) {
			result = "{\"results\":" + "error" + "}";
			e.printStackTrace();
		}
		return result;
	}
	
	private String queryPol(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,namec,namee,line");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispol = true");
		sb.append("\n	AND (namee = '"+reqStr+"' OR code = '"+reqStr+"')" );
		sb.append(filterType);
		sb.append("\n	) T");
		////System.out.println(sb.toString());
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		return "{\"results\":"+result.get("json") + "}";
	}
	
	private String queryPod(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,namec,namee,line");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispod = true");
		sb.append("\n	AND (namee = '"+reqStr+"' OR code = '"+reqStr+"')" );
		sb.append(filterType);
		sb.append("\n	) T");
		////System.out.println(sb.toString());
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		return "{\"results\":"+result.get("json")+"}";
	}
	
	private String queryPdd(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,namec,namee,line");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispdd = true");
		sb.append("\n	AND (namee = '"+reqStr+"' OR code = '"+reqStr+"')" );
		sb.append(filterType);
		sb.append("\n	) T");
		////System.out.println(sb.toString());
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		return "{\"results\":"+result.get("json")+"}";
	}
	
	private String queryPot(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,namec,namee,line");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispod = true");
		sb.append("\n	AND (namee = '"+reqStr+"' OR code = '"+reqStr+"')" );
		sb.append(filterType);
		sb.append("\n	) T");
		////System.out.println(sb.toString());
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		return "{\"results\":"+result.get("json")+"}";
	}
	
	private String queryDestination(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,namec,namee,line");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isdestination = true");
		sb.append("\n	AND (namee = '"+reqStr+"' OR code = '"+reqStr+"')" );
		sb.append(filterType);
		sb.append("\n	) T");
		////System.out.println(sb.toString());
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		return "{\"results\":"+result.get("json")+"}";
	}
}
