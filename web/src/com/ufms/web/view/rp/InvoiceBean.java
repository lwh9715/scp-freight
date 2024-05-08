package com.ufms.web.view.rp;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.web.BaseServlet;

@WebServlet("/invoice")
public class InvoiceBean extends BaseServlet {
	
	
	@Action(method="preview")
	public String preview(HttpServletRequest request , HttpServletResponse response){
		String productname = request.getParameter("productname");
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		return "";
	}
	
	
	
	
}

