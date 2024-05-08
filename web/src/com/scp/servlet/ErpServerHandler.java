package com.scp.servlet;

import javax.servlet.http.HttpServletRequest;



public class ErpServerHandler {

	// 获取日志信息
	public String handle(String action, HttpServletRequest request) {
		//FacesContext facesContext = FacesContext.getCurrentInstance();
//		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
//		//id是sys_msg的id  通过这个id 找到linkid
//		String key = request.getParameter("id");
//		Long msgid = Long.valueOf(key);
//		String result = "Unuse";
//		if ("workflow".equals(action)) {
//			try{
//				
//				String sql = "SELECT linkid FROM sys_message WHERE id ="+msgid;
//				Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//				String workItemId = (String)m.get("linkid");
//				IWorkflowSession wflsession = AppUtils.getWorkflowSession();
//				IWorkItem wi = wflsession.findWorkItemById(workItemId);
//				Task task = wi.getTaskInstance().getTask();
//				
//				// 这样找活动的流程实例偶尔报错
//				// IProcessInstance processInstance = ((TaskInstance) wi
//				// .getTaskInstance()).getAliveProcessInstance();
//				String processInstanceId = wi.getTaskInstance()
//						.getProcessInstanceId();
//				IProcessInstance processInstance = wflsession
//						.findProcessInstanceById(processInstanceId);
//		
//				String sn = "";
//				Long id = 0l;
//				if (processInstance != null) {
//					// 用request传递当前sn
//					sn = (String) processInstance.getProcessInstanceVariable("sn");
//					id = ((Long) processInstance.getProcessInstanceVariable("id"));
//				}
//		
//				if (task instanceof FormTask) {
//					//facesContext.getExternalContext().getRequestMap().put("CURRENT_WORKITEM", wi);
//					if (wi != null && wi.getState() == IWorkItem.RUNNING) {
//						// 如果是运行状态，返回编辑界面
//						String formUri = ((FormTask) task).getEditForm().getUri();
//						formUri = formUri.replace("faces", "xhtml");
//						if(StrUtils.isMobileNO(formUri)){
//						
//						//这种一般是指 那中国批量完成,不需要人工手动参与的活动 ,不设置打开页面
//						}else if(formUri.equals("none")){
//							MessageUtils.alert("该任务为批量操作,不可单独操作!请完成对应批量任务即可");
//						}
//						else{
//						formUri += "?id=" + id + "&sn=" + sn
//								+ "&processInstanceId=" + processInstance.getId()
//								+ "&workItemId=" + workItemId;
//		
//						return formUri;
//						}
//					} else {
//						if (wi.getState() == 0) {
//							MessageUtils.alert("Please claim first!");
//							
//						}
//						// 否则返回只读界面（example未实现view Form）
//						// String formUri = ((FormTask)
//						// task).getViewForm().getUri();
//						// return formUri;
//						// 返回待办任务
//					}
//				} else {
//					// 非FormTask没有界面，直接返回当前的操作界面本身
//					return "/scp/pages/sysmgr/message/msgshow.xhtml?id="+msgid;
//				}
//				} catch (Exception e) {
//					return "/scp/pages/sysmgr/message/msgshow.xhtml?id="+msgid;
//					//AppUtil.openWindow("__"+msgid, "../pages/sysmgr/message/msgshow.xhtml?id="+msgid);
//			}
//		}
		 return "/scp/pages/sysmgr/message/msgshow.xhtml?id=";
	
	}
	
}