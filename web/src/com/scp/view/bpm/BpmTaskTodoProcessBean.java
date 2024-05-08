package com.scp.view.bpm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scp.base.LMap;
import com.scp.base.LMapBase.MLType;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmProcessinstance;
import com.scp.model.bpm.BpmTask;
import com.scp.model.finance.FinaArap;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.module.formdefine.DynamicFormBean;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 待办工单处理页面(编辑界面) 
 * @author neo
 */
@ManagedBean(name = "bpm.bpmtasktodoprocessBean", scope = ManagedBeanScope.REQUEST)
public class BpmTaskTodoProcessBean extends DynamicFormBean{
	
	@Bind
	@SaveState
	public String tips;
	
	@Bind
	@SaveState
	public String taskRemarks;
	
	
	@Bind
	@SaveState
	public String taskId;
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Bind
	@SaveState
	public String src;
	
	@SaveState
	public boolean formatLinkBPMNo = false;//1832 待办打开时，区分是链接打开还是双击打开
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			nextAssignUser="";
			taskId = AppUtils.getReqParam("taskId");
			src = AppUtils.getReqParam("src");
			try {
				formatLinkBPMNo = Boolean.parseBoolean(AppUtils.getReqParam("formatLink"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			tips = bpmProcess.getNamec() + "-->" + bpmTask.getTaskname() + "-->" + bpmProcessinstance.getNos();
			try {
				if(AppUtils.getUserSession().getMlType().equals(MLType.en)){
					LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
					tips = bpmProcess.getNamee() + "-->" + l.get(bpmTask.getTaskname()) + "-->" + bpmProcessinstance.getNos();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			this.update.markUpdate(UpdateLevel.Data, "taskId");
		}
	}
	
	/*
	 * 下一步
	 */
	@Action
	public void next(){
		//alert(nextAssignUser);
		//this.pkVal = getGridSelectId();
		try{
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			if(bpmTask.getState()!=2){//2165 处理两个人同时指派一个流程的情况
				MessageUtils.alert("该流程已经被其他人指派，请刷新待办列表重新进入指派页面");
				return;
			}
			
			bpmTask.setComments(taskRemarks);
			bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
			bpmTask.setCommentime(Calendar.getInstance().getTime());
			serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
			
			
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			
			String sqlsub = "";
			String tasknamegoto = "";
			if ("新版_放货流程".equals(bpmProcess.getNamec()) && "结束".equals(bpmTask.getTaskname()) == false) {
				Map<String,String> m = insertGuarantee(bpmTask,bpmProcess);//根据信用额度判断流程走向
				if(m == null){
					return;
				}
				tasknamegoto = m.get("tasknamegoto");
				sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";assignuserid="+nextAssignUser+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+";tasknamegoto="+tasknamegoto+";type="+m.get("type")+"') AS rets;";
			}else{
				if ("增减费用申请".equals(bpmProcess.getNamec()) || "增减费用申请（青岛）".equals(bpmProcess.getNamec())) {
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COUNT(1) as c from sys_userinrole WHERE roleid = (SELECT id FROM sys_role  where  name = '流程增减费用' AND isdelete = FALSE LIMIT 1) AND isdelete = FALSE AND userid = " + AppUtils.getUserSession().getUserid());
					if (Integer.valueOf(String.valueOf(m.get("c"))) == 0) {
						MessageUtils.alert("无增减流程审批权限!");
						return;
					}
					insertFee();
				} else if ("账单审核".equals(bpmProcess.getNamec())) {
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("select remarks from bpm_processinstance where id = " + bpmTask.getProcessinstanceid() + "LIMIT 1");
					String remarks = (String) m.get("remarks");
					String arapselected = "";
					int index = remarks.lastIndexOf("：");
					if (index != -1) {
						arapselected = remarks.substring(index + 1);
					}
					//账单审核，自动费用确认
					String sql3 = "update fina_arap set isconfirm = true,confirmer = '" + bpmTask.getActorid() +
							"',confirmtime = now() where id in (" + arapselected + ") and isdelete = false and isconfirm = false; ";
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql3);
				}

			    sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Next;assignuserid="+nextAssignUser+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			}
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			//this.editWindow.close();
			if(!StrUtils.isNull(src) && "weixin".equals(src)){
			}else{
				if(!formatLinkBPMNo){
					Browser.execClientScript("parent","closeTaskWindows()");
				}
			}
			Browser.execClientScript("alert('OK')");
			
			// if ("新版_放货流程".equals(bpmProcess.getNamec()) && "结束".equals(tasknamegoto)) {
			// 	Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_sys_mail_generate('type=BPM-B80EDD34;id="+bpmTask.getProcessinstanceid()+";userid="+AppUtils.getUserSession().getUserid()+"') AS mail");
			// 	String txt = String.valueOf(m.get("mail"));
			// 	String[] mailInfos = txt.split("-.-.-");
			// 	String sysEmailid = mailInfos[5];
			//
			// 	Map<String,String> attachments = new HashMap<String, String>();
			// 	for (int i = 0; i < sysEmailid.split(",").length; i++) {
			// 		if(null == sysEmailid.split(",")[i]){
			// 			continue;
			// 		}
			// 		this.serviceContext.sysEmailService.sendEmailHtml(Long.valueOf(sysEmailid.split(",")[i]), attachments);
			// 	}
			// }

			if ("新版_放货流程".equals(bpmProcess.getNamec()) && "结束".equals(tasknamegoto)) {
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_sys_mail_generate('type=BPM-B80EDD34_end;id="+bpmTask.getProcessinstanceid()+";userid="+AppUtils.getUserSession().getUserid()+"') AS mail");
			}
			if ("增减费用申请".equals(bpmProcess.getNamec()) && ((bpmTask.getTaskname()).startsWith("经理审核1") || (bpmTask.getTaskname()).startsWith("副总审核")) && "0".equals(nextAssignUser)) {
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_sys_mail_generate('type=BPM-968CACDE_end;id=" + bpmTask.getProcessinstanceid() + ";userid=" + AppUtils.getUserSession().getUserid() + "') AS mail");
			}
			if ("增减费用申请（青岛）".equals(bpmProcess.getNamec()) && ((bpmTask.getTaskname()).startsWith("经理审核1") || (bpmTask.getTaskname()).startsWith("经理审核2") || (bpmTask.getTaskname()).startsWith("经理审核3")) && "0".equals(nextAssignUser)) {
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_sys_mail_generate('type=BPM-CA59ADF7_end;id=" + bpmTask.getProcessinstanceid() + ";userid=" + AppUtils.getUserSession().getUserid() + "') AS mail");
			}
			//this.refresh();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	private String processinsVar = "";
	/**
	 * 担保信息新增
	 * @param bpmTask
	 * @param bpmProcess
	 */
	public Map<String,String> insertGuarantee(BpmTask bpmTask,BpmProcess bpmProcess){
		Map<String,String> m = new HashMap<String, String>();
		m.put("tasknamegoto",bpmTask.getTaskname());//默认向下执行
		m.put("type","Next");
		
		String billtype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmTask.getProcessinstanceid(), "billtype");
		String releasetype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmTask.getProcessinstanceid(), "releasetype");
		Map<String,String> ma = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_fina_guarantee('processinstanceid="+bpmTask.getProcessinstanceid()+";userid="+AppUtils.getUserSession().getUserid()+";billtype="+billtype+";releasetype="+releasetype+";taskid="+bpmTask.getId()+";') AS ret");

		if(ma.get("ret").indexOf("GoTo") > -1){
			m.put("tasknamegoto","结束");
			m.put("type","GoTo");
		}else if(ma.get("ret").indexOf("Next") > -1){
			m.put("type","Next");
		}else{
			Browser.execClientScript("alert('"+ma.get("ret")+"！')");
			return null;
		}
		return m;
	}
	
	public void insertFee(){
		//副总审核和经理审核后需要新增增减费用
		if(this.tips.indexOf("副总审核") >-1 || this.tips.indexOf("经理审核1") >-1 || this.tips.indexOf("经理审核3") >-1 || this.tips.indexOf("经理审核4") >-1){
			String sql = "SELECT arapinfo FROM bpm_task WHERE processid||''||processinstanceid = (SELECT processid||''||processinstanceid FROM bpm_task WHERE id = "+this.taskId+" LIMIT 1) AND taskname='Start'  LIMIT 1";
			try {
				Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if(m.containsKey("arapinfo") && null != m.get("arapinfo") && "".equals(String.valueOf(m.get("arapinfo"))) == false){
					Object obj = m.get("arapinfo");
					DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
					net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(obj);
					List<Map> l = new ArrayList<Map>();
					for (int i = 0; i < array.size(); i++) {
						Map map = (Map) array.get(i);
						Map<String,String> dateMap = (Map)map.get("arapdate");
						Map<String,String> dateMap2 = (Map)map.get("inputtime");
						map.put("arapdate", fmt.format(dateMap.get("time")));
						map.put("inputtime", fmt.format(dateMap2.get("time")));
						map.put("updatetime", fmt.format(dateMap2.get("time")));
						map.put("confirmtime", fmt.format(dateMap2.get("time")));
						l.add(map);
					}
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					Type type = new TypeToken<List<FinaArap>>() {}.getType();
//					String m2 = map.toString();
					List<FinaArap> arap = gson.fromJson(l.toString(),type);

					Integer num = 1;
					List<FinaArap> finaArapList = new ArrayList<FinaArap>();
					for (FinaArap li : arap) {
	    				FinaArap finaArap = new FinaArap();
	    				finaArap.setCorpid(li.getCorpid());
	    				finaArap.setInputer(li.getInputer());
	    				finaArap.setTaxrate((null == li.getTaxrate() || li.getTaxrate().compareTo(BigDecimal.ZERO)==0) ? new BigDecimal(1) : li.getTaxrate());//早期提交的付款申请流程没有记录税率。默认为1
	    				finaArap.setAraptype(li.getAraptype());
	    				finaArap.setArapdate(li.getArapdate());
	    				finaArap.setRptype(li.getRptype());
	    				finaArap.setCustomerid(li.getCustomerid());
	    				finaArap.setFeeitemid(li.getFeeitemid());
	    				finaArap.setAmount(li.getAmount());
	    				finaArap.setCurrency(li.getCurrency());
	    				if(li.getRemarks()!=null){
	    					finaArap.setRemarks(li.getRemarks());   //界面无备注，这里会全部变成空的 neo 20170202
	    				}
	    				finaArap.setPiece(li.getPiece());
	    				finaArap.setPrice(li.getPrice());
//	    				finaArap.setPricenotax(li.getPrice());
	    				finaArap.setUnit(li.getUnit());
	    				finaArap.setPpcc(li.getPpcc());
	    				finaArap.setSharetype("N");
	    				finaArap.setJobid(li.getJobid());
	    				finaArap.setPayplace(li.getPayplace());
//	    				if(li.getId() == -100L){//新增费用,结算地取当前所选分公司,选全部则取帐号所在分公司
//	    					//finaArap.setCorpid(AppUtils.getUserSession().getCorpidCurrent() < 0 ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent());
//	    					finaArap.setCorpid(AppUtils.getUserSession().getCorpid()); //neo 20200513 改为取当前用户所在分公司
//	    				}
	    				finaArap.setIsamend(li.getIsamend());
	    				long etime1=System.currentTimeMillis()+num*1000;//延时函数，单位毫秒，这里是延时了num秒钟
	    				finaArap.setInputtime(new Date(etime1));
	    				num ++;
	    				
	    				finaArap.setPricenotax(new BigDecimal(-1.01));
	    				finaArapList.add(finaArap);
	    			}
					serviceContext.arapMgrService.saveOrModify(finaArapList);
                    //内部往来增减时，增减自动生成往来
                    for (int i = 0; i < finaArapList.size(); i++) {
                        //增减费用处理，报价金额字段
                        if (!StrUtils.isNull(arap.get(i).getDescinfo())) {
                            String bjsql = "UPDATE fina_arap_link_quote Set isdelete = TRUE WHERE arapid = " + finaArapList.get(i).getId() + ";";
                            bjsql += "\nINSERT INTO fina_arap_link_quote(id ,arapid ,quoteamount ,quotecurrency )" +
                                    "\nSELECT getid()," + finaArapList.get(i).getId() + "," + arap.get(i).getInvoiceamount() + ",'" + arap.get(i).getDescinfo() + "'" +
                                    "\nFROM _virtual WHERE NOT EXISTS(SELECT 1 FROM fina_arap_link_quote WHERE isdelete = FALSE AND arapid = " + finaArapList.get(i).getId() + ");";
                            daoIbatisTemplate.updateWithUserDefineSql(bjsql);
                        }
                        String sql2 = "SELECT EXISTS(SELECT 1 FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = FALSE " +
                                "AND id = " + finaArapList.get(i).getCustomerid() + ") AS iscus;";
                        Map mapAr = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2);

                        //有相同金额，收付方向不一致，结算公司和结算地相同的情况，表示为已经存在往来费用 20240307
                        String sql3 = "SELECT EXISTS(SELECT 1 FROM fina_arap WHERE jobid = " + finaArapList.get(i).getJobid() +
                                " AND araptype <> '" + finaArapList.get(i).getAraptype() + "' AND customerid = " + finaArapList.get(i).getCorpid() +
                                " AND corpid = " + finaArapList.get(i).getCustomerid() + " AND amount = " + finaArapList.get(i).getAmount() +
                                " AND isdelete = FALSE) AS iscontact;";
                        Map isContact = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql3);

                        //费用id不相同，金额不相同的情况，收付方向不一致
                        if (isContact.get("iscontact").toString().equals("false") && mapAr.get("iscus").toString().equals("true")) {
                            try {
                                String sql4 = "SELECT f_arap_copy_link('jobid=" + finaArapList.get(i).getJobid() +
                                        ";userid=" + AppUtils.getUserSession().getUserid() + ";ids=" + finaArapList.get(i).getId() + "');";
                                serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql4);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
					//增减费用后清除费用样本信息，防止因为流程反复运作导致反复添加
					serviceContext.daoIbatisTemplate.queryWithUserDefineSql("UPDATE bpm_task SET arapinfo = '' WHERE processid||''||processinstanceid = (SELECT processid||''||processinstanceid FROM bpm_task WHERE id = "+this.taskId+" LIMIT 1) AND taskname='Start';");
				}
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
				return;
			}
		}
	}
	
	/*
	 * 退回上一步
	 */
	@Action
	public void last(){
		try{
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			if(bpmTask.getState()!=2){//2165 处理两个人同时指派一个流程的情况
				MessageUtils.alert("该流程已经被其他人指派，请刷新待办列表重新进入指派页面");
				return;
			}
			LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
			
			bpmTask.setComments(l.get("打回上一步") + ":" + taskRemarks);
			bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
			bpmTask.setCommentime(Calendar.getInstance().getTime());
			
			serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			String sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Last;assignuserid="+nextAssignUser+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			//this.editWindow.close();
			if(!StrUtils.isNull(src) && "weixin".equals(src)){
			}else{
				if(!formatLinkBPMNo){
					Browser.execClientScript("parent","closeTaskWindows()");
				}
			}
			Browser.execClientScript("alert('OK')");
			//this.refresh();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	/**
	 * 跳到指定的环节，任意跳转
	 * @param gotoTaskName
	 * @param direct  next back 区分是打回还是向后跳转
	 */
	public void goToTask(String gotoTaskName , String direct) {
		try{
			if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			if(bpmTask.getState()!=2){//2165 处理两个人同时指派一个流程的情况
				MessageUtils.alert("该流程已经被其他人指派，请刷新待办列表重新进入指派页面");
				return;
			}
			//insertFee();
			LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
			bpmTask.setComments(taskRemarks);
			if("back".equals(direct)){
				//bpmTask.setRemarks("["+bpmTask.getTaskname()+"]"+l.get("打回到") + "[" + gotoTaskName + "]" + taskRemarks);
				//bpmTask.setComments("["+bpmTask.getTaskname()+"]"+l.get("打回到") + "[" + gotoTaskName + "]" + taskRemarks);
				bpmTask.setRemarks(l.get("打回到") + "[" + gotoTaskName + "]" + taskRemarks);
				bpmTask.setComments(l.get("打回到") + "[" + gotoTaskName + "]" + taskRemarks);
			}else{
				//bpmTask.setRemarks("["+bpmTask.getTaskname()+"]"+l.get("跳转到") + "[" + gotoTaskName + "]" + taskRemarks);
				//bpmTask.setComments("["+bpmTask.getTaskname()+"]"+l.get("跳转到") + "[" + gotoTaskName + "]" + taskRemarks);
			}
			bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
			bpmTask.setCommentime(Calendar.getInstance().getTime());
			
			serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			
			if("增减费用申请".equals(bpmProcess.getNamec())){
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COUNT(1) as c from sys_userinrole WHERE roleid = (SELECT id FROM sys_role  where  name = '流程增减费用' AND isdelete = FALSE LIMIT 1) AND isdelete = FALSE AND userid = "+ AppUtils.getUserSession().getUserid());
				
				if(Integer.valueOf(String.valueOf(m.get("c"))) == 0){
					MessageUtils.alert("无增减流程审批权限!");
					return;
				}
			}
			
			String sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=GoTo;gotodirect="+direct+";assignuserid="+nextAssignUser+";tasknamegoto="+gotoTaskName+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			//this.editWindow.close();
			if(!StrUtils.isNull(src) && "weixin".equals(src)){
			}else{
				if(!formatLinkBPMNo){
					Browser.execClientScript("parent","closeTaskWindows()");
				}
			}
			Browser.execClientScript("alert('OK')");
			//this.refresh();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "bpm.todo.bpmtodoapplyapBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "bpm.todo.bpmtodoapplyapBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(qryuserdesc) ){
			qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
			qryuserdesc = qryuserdesc.toUpperCase();
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";

				if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='"
								+ dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index)
									+ "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							buffer.append("\nAND UPPER(" + key
									+ ") ILIKE UPPER('%'||" +"TRIM('"+ val+"')" + "||'%')");
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		map.put("limit", limits+"");
		map.put("start", starts+"");
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void clearQryKeysc() {
		if (qryMapUser != null) {
			qryMapUser.clear();
			update.markUpdate(true, UpdateLevel.Data, "userPanel");
			this.gridUser.reload();
		}
	}

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser.contains(id)){
				this.nextAssignUser = nextAssignUser + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	
	@Action
	public void clear(){
		this.nextAssignUser ="";
		this.user = "";
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}
	
}
