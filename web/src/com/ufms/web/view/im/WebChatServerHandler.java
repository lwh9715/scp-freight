package com.ufms.web.view.im;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;

@WebServlet("/webChat")
@ManagedBean(name = "pages.module.im.webChatBean" ,tableName="")
public class WebChatServerHandler extends BaseServlet{
	
	@Action(method="process")
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String ret = "";
		
		ImService imService = (ImService)AppUtil.getBeanFromSpringIoc("imService");
		String csno = request.getParameter("csno");
		String action = request.getParameter("action");
		
		if(action.equals("send")){
			String sendid = request.getParameter("sendid");
			String msg = request.getParameter("msg");
			try {
				msg = URLDecoder.decode(msg, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String receiveid = request.getParameter("receiveid");
//			QueueSender queueSender = (QueueSender)AppUtil.getBeanFromSpringIocJunit("queueSender");
//			queueSender.sendMessage(new TransMessage(sendid , receiveid , msg , csno));
			TransMessage transMessage = new TransMessage(sendid , receiveid , msg , csno);
			imService.saveHistory(transMessage);
			ret = "{\"ret\": \"OK\"}";
			return ret;
		}
		
		if(action.equals("receive")){
			ret = "";
			String sendid = request.getParameter("sendid");
			String receiveid = request.getParameter("receiveid");
			ret = imService.receiver(sendid , receiveid , csno);
			return ret;
		}
		
		if(action.equals("existQueueMsg")){
			String receiveid = request.getParameter("receiveid");
			ret = imService.existQueueMsg(receiveid , csno);
			return ret;
		}
		
		if(action.equals("getQueueMsgSize")){
			String sendid = request.getParameter("sendid");
			String receiveid = request.getParameter("receiveid");
			ret = imService.getQueueMsgSize(sendid , receiveid , csno);
			return ret;
		}
		
		if(action.equals("getQueueMsgSizeAll")){
			String sendid = request.getParameter("sendid");
			String receiveid = request.getParameter("receiveid");
			ret = imService.getQueueMsgSizeAll(sendid , receiveid , csno);
			return ret;
		}
		
		if(action.equals("getQueueLastMsg")){
			String receiveid = request.getParameter("receiveid");
			ret = imService.getQueueLastMsg(receiveid , csno);
			return ret;
		}
		
		if(action.equals("getHistory")){
			ret = "";
			String sendid = request.getParameter("sendid");
			String receiveid = request.getParameter("receiveid");
			ret = imService.getHistory(sendid , receiveid , csno);
			return ret;
		}
		
		if(action.equals("getHistoryAll")){
			ret = "";
			String sendid = request.getParameter("sendid");
			String receiveid = request.getParameter("receiveid");
			ret = imService.getHistoryAll(sendid , receiveid , csno);
			return ret;
		}

		return ret;
	}
}
