package com.scp.view.module.air;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.model.bus.BusAir;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.air.busairdefineBean", scope = ManagedBeanScope.REQUEST)
public class BusAirDefineBean extends FormView {
	@SaveState
	@Accessible
	public BusAir selectedRowData = new BusAir();
	
	@SaveState
	@Bind
	public String billid;

	@SaveState
	@Bind
	public Long userid;
	
	@Bind
	@SaveState
	public String control;
	
	@SaveState
	public String language;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("id");
		initControl();
		this.userid = AppUtils.getUserSession().getUserid();
		update.markUpdate(UpdateLevel.Data, "userid");
		language = AppUtils.getUserSession().getMlType().toString();
		if(!StrUtils.isNull(id)){
			billid = id;
			selectedRowData = this.serviceContext.busAirMgrService.busAirDao.findById(Long.parseLong(billid));
			
			this.refresh();
		}
	}	
	
	@Bind
	@SaveState
	public String hblRpt;
	
	public void initControl(){
		this.control = AppUtils.base64Encoder("false");
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_ship_bill.getValue())) {
			if (s.endsWith("_templete")) {
				this.control = AppUtils.base64Encoder("true");
			}
		}
		update.markUpdate(UpdateLevel.Data,"control");	
	}
	
	@Action
	public void savelastbill(){
		String rpturl = AppUtils.getRptUrl();
		String upg = "UPDATE bus_shipping set hblrpt = '" + hblRpt + "' where id = " + billid + ";";
		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
	}
	
	// 临时保存系统生成可供下载文件路径
	@SaveState
	public String temFileUrl;
	
	@Action
	public void showExport() {
		if (this.selectedRowData != null && this.selectedRowData.getId() > 0) {
			// 生成下载临时文件路径
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));
			/*fileUrl.append("hawb11");
			fileUrl.append(new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date()));
			fileUrl.append("_");
			fileUrl.append(AppUtils.getUserSession().getUsercode());*/
			fileUrl.append("HAWB NO_");
			fileUrl.append(selectedRowData.getHawbno());
			fileUrl.append(".xls");
			this.temFileUrl = fileUrl.toString();
			File file = new File(fileUrl.toString());
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append("\n SELECT");
				/*sbsql.append("\n 	(string_to_array(cnortitle, CHR(10)))[1] AS cnorname");
				sbsql.append("\n 	,(string_to_array(cnortitle, CHR(10)))[2] AS cnoradress");
				sbsql.append("\n 	,(string_to_array(cneetitle, CHR(10)))[1] AS cneename");
				sbsql.append("\n 	,(string_to_array(cneetitle, CHR(10)))[2] AS cneeadress");*/
				sbsql.append("\n 	cnortitle");
				sbsql.append("\n 	,cneetitle");
				sbsql.append("\n    ,notifytitle");
				sbsql.append("\n 	,agentdesabbr");
				sbsql.append("\n 	,(SELECT a.code FROM sys_corporation a WHERE a.id =  t.carrierid) AS aircode");
				sbsql.append("\n 	,podcode");
				sbsql.append("\n	,to2");
				sbsql.append("\n 	,to3");
				sbsql.append("\n	,by1");
				sbsql.append("\n 	,by2");
				sbsql.append("\n 	,by3");
				sbsql.append("\n 	,pod");
				sbsql.append("\n 	,flightno1");
				sbsql.append("\n 	,to_char(flightdate1, 'dd/MON') AS flightdate1");
				sbsql.append("\n 	,piece");
				sbsql.append("\n	,weight");
				sbsql.append("\n 	,volume");
				sbsql.append("\n 	,markno");
				sbsql.append("\n 	,chargeweight");
				sbsql.append("\n 	,polcode");
				sbsql.append("\n 	,podcyid");
				sbsql.append("\n 	,podfee");
				sbsql.append("\n 	,transamt");
				sbsql.append("\n 	,customeamt");
				sbsql.append("\n 	,insuranceamt");
				/*sbsql.append("\n 	,ppccothfee");
				sbsql.append("\n 	,feesumpp");
				sbsql.append("\n 	,feesumcc");
				sbsql.append("\n 	,sumto");*/
				sbsql.append("\n 	,goodsdesc");
				/*sbsql.append("\n 	,taxfeecc");
				sbsql.append("\n 	,agentothfeepp");
				sbsql.append("\n 	,agentothfeecc");
				sbsql.append("\n 	,carrothfeepp");
				sbsql.append("\n 	,carrothfeecc");*/
				sbsql.append("\n 	,substr(mawbno ,1,3) AS mawbno1");
				sbsql.append("\n 	,substr(mawbno ,4,length(mawbno)) AS mawbno2");
				sbsql.append("\n 	,hawbno");
				sbsql.append("\n    ,substring(flightno1,E'(\\\\D+)') AS flight");
				/*sbsql.append("\n 	,valueaddpp");
				sbsql.append("\n 	,valueaddcc");
				sbsql.append("\n 	,refno");*/
				sbsql.append("\n 	,pol");
				sbsql.append("\n    ,ppccgoods");
				sbsql.append("\n    ,ppccothfee");
				sbsql.append("\n    ,ppccpaytype");
				sbsql.append("\n 	,polcyid");
				sbsql.append("\n 	,(SELECT jobdate FROM fina_jobs a WHERE a.id = t.jobid) AS jobdate");
				sbsql.append("\n 	,(SELECT x.namec FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = (SELECT id FROM bus_air WHERE id = "+this.selectedRowData.getId()+") AND userid = x.id)) AS assigner");
				sbsql.append("\n 	,(SELECT namee FROM sys_corporation a WHERE a.id = (SELECT corpidop FROM fina_jobs s WHERE s.id = t.jobid and s.isdelete = false)) AS corpnamee");
				
				sbsql.append("\n FROM bus_air t");
				sbsql.append("\n WHERE");
				sbsql.append("\n isdelete = FALSE");
				sbsql.append("\n AND id = " + this.selectedRowData.getId());
				//System.out.println("sql--->"+sbsql.toString());
				Map map = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
				//System.out.println("flight--->"+map.get("flight"));
				// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
				String exportFileName = "hawb11.xls";

				// 模版所在路径
				String fromFileUrl = AppUtils.getHttpServletRequest()
						.getSession().getServletContext().getRealPath("")
						+ File.separator
						+ "upload"
						+ File.separator
						+ "air"
						+ File.separator + exportFileName;
				if (!ReadExcel.exportAirJobsForExcel(new File(fromFileUrl), file,map)) {
					return;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			MessageUtils.alert("请先保存工作单!");
			return;
		}

	}
	
	@Bind(id = "downloadAirTemplete", attribute = "src")
	private File getDownload() {
		return new File(temFileUrl);
	}
	
	@Action
	public void downloadAirTempleteList() {
		String exportFileName = "hawb11.xls";
		this.temFileUrl = AppUtils.getHttpServletRequest().getSession()
				.getServletContext().getRealPath("")
				+ File.separator
				+ "upload"
				+ File.separator
				+ "air"
				+ File.separator + exportFileName;
	}
	
	
	@SaveState
	@Bind
	public String mblRpt;
	
	@SaveState
	@Bind
	public String orderRpt;
	
	@SaveState
	@Bind
	public String bookRpt;
	
	private String getArgs() {
		String args = "";
		args += "&id=" + this.billid;
		return args;
	}
	
	@Bind(id = "hblrpt")
	public List<SelectItem> getHblrpt() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='air' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind(id = "mblrpt")
	public List<SelectItem> getMblrpt() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='airmbl' AND isdelete = FALSE",
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Override
	public void save() {
		super.save();
		this.serviceContext.busAirMgrService.saveData(this.selectedRowData);
	}
	
	@Bind(id = "orderrpt")
	public List<SelectItem> getOrderrpt() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", "WHERE modcode='airorder' AND isdelete = FALSE",
			"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	@Action
	public void printOrder() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ orderRpt;
		String url = openUrl + getArgsusr();
		AppUtils.openWindow("_airOrderReport",url);
	}
	private String getArgsusr() {
		BusAir bs = serviceContext.busAirMgrService.busAirDao.findById(Long.parseLong(billid));
		String args = "&para=jobid="+bs.getJobid()+":userid="+AppUtils.getUserSession().getUserid()+":corpidcurrent="+AppUtils.getUserSession().getCorpid()+":";
		args = args+"&id=" + this.billid;
		return args;
	}
	
	
}

