package com.scp.servlet;


import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class JqGridHandler {
    @Resource
    public ServiceContext serviceContext;


    @Resource
    public DaoIbatisTemplate daoIbatisTemplate;

    public String handle(String action, HttpServletRequest request) {
        String reqStr = "";//条件
//		try {
//			reqStr = URLDecoder.decode(request.getParameter("q"), "UTF-8");
//		} catch (Exception e1) {
//			//e1.printStackTrace();
//		}
        String result = "";
        try {
            serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
            if ("getRPFee".equals(action)) {
                result = this.queryRPFee(request, reqStr);
            }
            if ("getARAP".equals(action)) {
                result = this.queryARAP(request, reqStr);
            }
        } catch (Exception e) {
            result = "{\"results\":" + "error" + "}";
            e.printStackTrace();
        }
        return result;
    }

    private String queryARAP(HttpServletRequest request, String reqStr) {
        String type = request.getParameter("type");

        String sidx = request.getParameter("sidx");//来获得排序的列名，
        String sord = request.getParameter("sord");//来获得排序方式，

        String result = "";

        return result;
    }

    private String queryRPFee(HttpServletRequest request, String reqStr) {
        String type = request.getParameter("type");

        String sidx = request.getParameter("sidx");//排序条件
        String sord = request.getParameter("sord");//最后个字段排序方式

        String result = "";

        return result;
    }

}
