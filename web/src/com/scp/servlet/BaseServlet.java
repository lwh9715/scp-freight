package com.scp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.util.StrUtils;

public class BaseServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		//resp.setHeader("Access-Control-Allow-Origin", "*"); 
		
		String src = req.getParameter("src");
		String action = req.getParameter("action");
		String result = "";
		if (StrUtils.isNull(src)) {
			return;
		}
		// 根据不同的项目掉用相应的Handler处理
		if (src.equals("android")) {
			result = new AndroidHandler().handle(action, req);
			GZIPOutputStream gzipStream = new GZIPOutputStream(resp
					.getOutputStream());
			// DataOutputStream output=new
			// DataOutputStream(resp.getOutputStream());
			// output.writeUTF(result);
			gzipStream.write(result.getBytes());
			gzipStream.flush();
			gzipStream.close();
			return;
		}else if (src.equals("jqgrid")) {
			result = new JqGridHandler().handle(action, req);
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
//			
			//resp.setHeader("Content-Encoding", "gzip"); 
			//resp.setHeader("Transfer-Encoding", "chunked"); 
			//GZIPOutputStream gzipStream = new GZIPOutputStream(resp
			//		.getOutputStream());
			// DataOutputStream output=new
			// DataOutputStream(resp.getOutputStream());
			// output.writeUTF(result);
			//gzipStream.write(result.getBytes());
			//gzipStream.flush();
			//gzipStream.close();
		}else if (src.equals("erp")) {
			result = new ErpServerHandler().handle(action, req);
			resp.sendRedirect(result);
		}else if (src.equals("af")) {
			result = new AfServerHandler().handle(action, req);
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("track")){//单号跟踪
			result = new TrackServerHandler().handle(action, req);
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("msg")){//二维码扫描参数
			String nos = req.getParameter("nos").trim();
			if(!"".equals(nos)&& nos !=null){
				 result = new MessageServerHandle().handle(nos,req);
				resp.setContentType("text/html;charset=UTF-8");
				resp.setCharacterEncoding("UTF-8");
				PrintWriter out = resp.getWriter();
				out.println(result);
				out.flush();
				out.close();
			}
		}else if(src.equals("dzz")){//
			result = new DzzServerHandler().handle(action, req);
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("flexbox")){
			result = new FlexBoxHandler().handle(action,req);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("extJson")){
			result = new ExtJsonHandler().handle(action,req);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("rogtemplete")){
			result = new RogTempleteServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("lockreport")){
			result = new LockReportServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("reportemail")){
			result = new ReportEmailServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("updateuser")){
			result = new UpdateUserServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("webServer")){
			result = new WebServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("arrivalNotice")){
			result = new ReportServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if(src.equals("weixin")){
			result = new WeiXinServerHandler().handle(action,req,resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}else if (src.equals("crm")) {
			result = new CrmHandler().handle(action, req);
			resp.setContentType("text/html; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.flush();
			out.close();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
