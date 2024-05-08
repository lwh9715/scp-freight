package com.scp.view.module.finance.edi;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.order.ExecutorUtils;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.finance.edi.sapBean", scope = ManagedBeanScope.REQUEST)
public class SapBean extends EdiGridView {

    @Bind
    @SaveState
    private String startDate;

    @Bind
    @SaveState
    private String endDate;

    @Bind
    @SaveState
    public String qryYear;

    @SaveState
    @Accessible
    public String qryPeriodS;

    @SaveState
    @Accessible
    public String qryPeriodE;

    @SaveState
    @Accessible
    public Date vchDateS;

    @SaveState
    @Accessible
    public Date vchDateE;

    @SaveState
    @Accessible
    public Integer vchnoS;

    @SaveState
    @Accessible
    public Integer vchnoE;

    @SaveState
    @Accessible
    public String isexport;

    @SaveState
    @Accessible
    public String inputername;

    @Override
    public void beforeRender(boolean isPostBack) {
        super.beforeRender(isPostBack);
        try {
            if (!isPostBack) {
                if (AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
                    MessageUtils.alert("未选择帐套，请重新选择帐套！");
                    return;
                }
                String sql = "select a.year,a.period FROM sys_user u LEFT JOIN fs_actset a ON (a.id = u.actsetid AND a.isdelete = FALSE)  WHERE u.id = "+AppUtils.getUserSession().getUserid()+";";
            	Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            	if(null != m && m.size() > 0){
            		qryYear = String.valueOf(m.get("year"));
            		qryPeriodS = String.valueOf(m.get("period"));
            		qryPeriodE = String.valueOf(m.get("period"));
            	}
                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initData() throws Exception {
        String cfgDataMap[] = {"sap_url"};
        this.cfgDataMap = ConfigUtils.findUserCfgVals(cfgDataMap, AppUtils.getUserSession().getUserid());
        if (!StrUtils.isNull(this.cfgDataMap.get("sap_url")) && this.cfgDataMap.get("sap_url") != null) {
            url = this.cfgDataMap.get("sap_url");
        }
    }


    @Bind
    public UIDataGrid gridaccount;

    @SaveState
    @Accessible
    public Map<String, Object> qryMapShip = new HashMap<String, Object>();

    @Bind(id = "gridaccount", attribute = "dataProvider")
    protected GridDataProvider getGridScheduleDataProvider() {
        return new GridDataProvider() {
            @SuppressWarnings("deprecation")
			@Override
            public Object[] getElements() {
                String sqlId = "pages.module.finance.edi.sapBean.gridaccount.page";
                Object[] aa = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere2(qryMapShip), start, limit).toArray();
                return aa;
            }

            @Override
            public int getTotalCount() {
                return 10000;
            }
        };
    }

    public Map getQryClauseWhere2(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        qry += "\nAND id = " + AppUtils.getUserSession().getActsetid();
        m.put("qry", qry);
        return m;
    }

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        gridaccountids = String.valueOf(AppUtils.getUserSession().getActsetid());
        if (!StrUtils.isNull(gridaccountids)) {
            qry += "\nAND actsetid = ANY(array[" + gridaccountids + "])";
        }
        if (!StrUtils.isNull(qryYear)) {
            qry += "\nAND year = " + qryYear; // 年查询
        }
        if (!StrUtils.isNull(qryPeriodS) || !StrUtils.isNull(qryPeriodE)) {
            // 期间查询
            qry += "\nAND period BETWEEN " + (StrUtils.isNull(qryPeriodS) ? 0 : qryPeriodS)
                    + " AND " + (StrUtils.isNull(qryPeriodE) ? 12 : qryPeriodE);
        }
        // 凭证日期查询
        if (vchDateS != null || vchDateE != null) {
            qry += "\nAND singtime BETWEEN '" + (vchDateS == null ? "0001-01-01" : new SimpleDateFormat("yyyy-MM-dd").format(vchDateS))
                    + "' AND '" + (vchDateE == null ? "9999-12-31" : new SimpleDateFormat("yyyy-MM-dd").format(vchDateE)) + "'";
        }
        //凭证号查询
        qry += vchnoS != null && vchnoS >= 0 ? " \n AND (nos::INTEGER >= "+vchnoS+")" : "";
        qry += vchnoE != null && vchnoE >= 0 ? " \n AND (nos::INTEGER <= "+vchnoE+")" : "";

        if (!StrUtils.isNull(isexport)) {
        	String isexportSql = "未导出".equals(isexport)?"=0":">0";
            qry += "\nAND COALESCE(exportcount,0)"+isexportSql; //
        }
        if (!StrUtils.isNull(inputername)) {
            inputername = StrUtils.getSqlFormat(inputername);
            qry += "\nAND v.inputer = (SELECT code FROM sys_user where isdelete = FALSE AND namec = '"+inputername+"' limit 1)"; //
        }



        m.put("qry", qry);
        return m;
    }


    @Action
    public void qryRefreshs() {
        String[] ids = this.gridaccount.getSelectedIds();
        gridaccountids = ids != null && ids.length > 0 ? StrUtils.array2List(ids) : "";
        qryRefresh();
    }

    @SaveState
    public String gridaccountids;

    @Bind
    public UIIFrame iframe;

    @Override
    public void grid_ondblclick() {
        super.grid_ondblclick();
        String winId = "_fsin";
        String url = "";
        //不能编辑
        url = "/pages/module/finance/doc/indtl_readonly.xhtml?id=" + this.getGridSelectId();

        iframe.load(url);
        if (editWindow != null) editWindow.show();
    }

    @SaveState
	public String temFileUrl;
    
    @SaveState
	public String temFileUrl2;
    
    @SaveState
	public String temFileUrl3;

    @Bind(id = "downloadexcle", attribute = "src")
	private File getDownload() {
		return new File(temFileUrl);
	}

    @Bind(id = "downloadexcle2", attribute = "src")
	private File getDownload2() {
		return new File(temFileUrl2);
	}
    
    @Bind(id = "downloadexcle3", attribute = "src")
	private File getDownload3() {
		return new File(temFileUrl3);
	}

    @Action
	public void showExport() {
		Browser.execClientScript("exc1()");
	}
    
    @Action
	public void synexport() {
		Browser.execClientScript("exc2()");
	}

    @Action
    public void exporting() {
    	//将数组ID转换成可以被直接模糊查询的SQL字符串
    	String[] ids = this.grid.getSelectedIds();
    	String id = "";
    	if(null != ids && ids.length > 0){
    		for (int i = 0; i < ids.length; i++) {
    			if(i == 0){
    				id = ids[i];
    			}else{
    				id += "," + ids[i];
    			}
			}
    	}
		StringBuffer fileUrl = new StringBuffer();
		fileUrl.append(System.getProperty("java.io.tmpdir"));

		try {
			if("".equals(id)){
				Map idmap = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT STRING_AGG(v.id||'',',') AS id " +
						"FROM _fs_vch_main v WHERE v.isdelete = FALSE AND v.srctype != 'I' AND v.actsetid = (SELECT actsetid FROM sys_user WHERE isdelete =FALSE AND id ="+AppUtils.getUserSession().getUserid()+") AND v.year = "+qryYear+" " +
								"AND v.period BETWEEN "+qryPeriodS+" AND "+qryPeriodE);
				id = String.valueOf(idmap.get("id"));
			}
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT f_fina_vch_export_sap('"+id+"') AS sap");
			Map map = this.serviceContext.userMgrService.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sbsql.toString());

			if(map==null || map.size() == 0 || map.containsKey("sap") == false){
				return;
			}
			JSONObject json = JSONObject.fromObject(map.get("sap"));
			JSONArray array = StrUtils.isNull(json.getString("data1"))?new JSONArray():JSONArray.fromObject(json.getString("data1"));
			JSONArray array2 = StrUtils.isNull(json.getString("data2"))?new JSONArray():JSONArray.fromObject(json.getString("data2"));

			fileUrl.append("fs_vch_sap.xlsx");
			this.temFileUrl = fileUrl.toString();
			File file = new File(fileUrl.toString());
			// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
			String exportFileName = "fs_vch_sap.xlsx";

			// 模版所在路径
			String fromFileUrl = AppUtils.getHttpServletRequest()
					.getSession().getServletContext().getRealPath("")
					+ File.separator
					+ "upload"
					+ File.separator
					+ "finace"
					+ File.separator + exportFileName;
			if (!SapExprot.exportSapExcle(new File(fromFileUrl), file,
					array, array2)) {
				return;
			}

            for (String s : ids) {
                String dmlSql = "UPDATE fs_vch SET exportcount = COALESCE(exportcount,0) + 1  WHERE id = "+s+";";
                daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
            }
            // this.grid.reload();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Action
    public void exporting2() {
    	//导出数据范围：当前账套下（user信息中的actsetid），凭证字为转，类别为H或J,年度期间内；分组条件：往来单位，部门，币种
		StringBuffer fileUrl = new StringBuffer();
		fileUrl.append(System.getProperty("java.io.tmpdir"));
		StringBuffer sbsql = new StringBuffer();

		try {
			//根据一个往来单位、收付款、币种分组统计数据
			sbsql.append("SELECT f_fina_vch_sap_accounts('qryYear="+qryYear+";qryPeriodS="+qryPeriodS+";qryPeriodE="+qryPeriodE+";userid="+AppUtils.getUserSession().getUserid().toString()+";') AS sap");
			Map map = this.serviceContext.userMgrService.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sbsql.toString());

			if(map==null || map.size() == 0 || map.containsKey("sap") == false){
				return;
			}
			JSONObject json = JSONObject.fromObject(map.get("sap"));
			JSONArray array = StrUtils.isNull(json.getString("data1"))?new JSONArray():JSONArray.fromObject(json.getString("data1"));
			JSONArray array2 = StrUtils.isNull(json.getString("data2"))?new JSONArray():JSONArray.fromObject(json.getString("data2"));

			fileUrl.append("fs_vch_sap.xlsx");
			this.temFileUrl2 = fileUrl.toString();
			File file = new File(fileUrl.toString());
			// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
			String exportFileName = "fs_vch_sap.xlsx";

			// 模版所在路径
			String fromFileUrl = AppUtils.getHttpServletRequest()
					.getSession().getServletContext().getRealPath("")
					+ File.separator
					+ "upload"
					+ File.separator
					+ "finace"
					+ File.separator + exportFileName;
			if (!SapExprot.exportSapExcle(new File(fromFileUrl), file,
					array, array2)) {
				return;
			}


            Set<String> idSet = (Set<String>) new HashSet<String>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject thisJson = array.getJSONObject(i);
                String idstr = thisJson.getString("vid");
                String[] idArray = idstr.split(",");
                for (String thisId : idArray) {
                    idSet.add(thisId);
                }
            }
            for (int i = 0; i < array2.size(); i++) {
                JSONObject thisJson = array2.getJSONObject(i);
                String idstr = thisJson.getString("vid");
                String[] idArray = idstr.split(",");
                for (String thisId : idArray) {
                    idSet.add(thisId);
                }
            }
            for (String s : idSet) {
                String dmlSql = "UPDATE fs_vch SET exportcount = COALESCE(exportcount,0) + 1  WHERE id = " + s + ";";
                daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
            }
            // this.grid.reload();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Action
    public void synexporting(){
    	System.out.println((new Date()).getTime());
    	HttpServletResponse response = AppUtils.getHttpServletResponse();
    	//导出数据范围：当前账套下（user信息中的actsetid），凭证字为转，类别为H或J,年度期间内；分组条件：往来单位，部门，币种
		StringBuffer fileUrl = new StringBuffer();
		fileUrl.append(System.getProperty("java.io.tmpdir"));
		StringBuffer sbsql = new StringBuffer();

		try {
			//根据一个往来单位、收付款、币种分组统计数据
			sbsql.append("SELECT f_fina_syn_export('year="+qryYear+";periods="+qryPeriodS+";periode="+qryPeriodE+";userid="+AppUtils.getUserSession().getUserid()+"') AS sap");
			Map<String,String> map = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());

			if(map==null || map.size() == 0 || map.containsKey("sap") == false){
				return;
			}
			JSONObject json = JSONObject.fromObject(map.get("sap"));
			JSONArray array = StrUtils.isNull(json.getString("data"))?new JSONArray():JSONArray.fromObject(json.getString("data"));

			fileUrl.append("dyn_report.xlsx");
			this.temFileUrl3 = fileUrl.toString();
			File file = new File(fileUrl.toString());
			// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
			String exportFileName = "dyn_report.xlsx";

			// 模版所在路径
			String fromFileUrl = AppUtils.getHttpServletRequest()
					.getSession().getServletContext().getRealPath("")
					+ File.separator
					+ "upload"
					+ File.separator
					+ "finace"
					+ File.separator + exportFileName;
			if (!SapExprot.synExport(new File(fromFileUrl), file, array, response)) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Action
    public void insertJobsToCIMC(){
    	
    	Browser.execClientScript("$(\"button[id$='insertJobsToCIMC']\").attr('disabled','true')");
    	long lo = (new Date()).getTime();
    	System.out.println(lo);
		StringBuffer sbsql = new StringBuffer();
		Connection con = null;
    	Statement ps = null;
		try {
			//根据一个往来单位、收付款、币种分组统计数据
			sbsql.append("SELECT f_fina_jobs_mysqlinfo('year="+qryYear+";periods="+qryPeriodS+";periode="+qryPeriodE+";userid="+AppUtils.getUserSession().getUserid()+"') AS jobs");
			List<Map> list = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbsql.toString());
			if(list==null || list.size() == 0){
				System.out.println("无数据："+(new Date()).getTime());
				return;
			}
			Map<String,Long> m = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT actsetid FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()+" AND isdelete =FALSE");
	    	try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	// mysql驱动
			try {
				con = (Connection) DriverManager.getConnection("jdbc:mysql://120.25.254.109:3306/sc?useUnicode=true&characterEncoding=utf8",
				        "scuser", "K%%3q7tj91");
				ps =  (Statement) con.createStatement();
				System.out.print(AppUtils.getUserSession().getCorpidCurrent());
				int r = ps.executeUpdate("delete from ufms_business where actsetid = "+m.get("actsetid")+";");
				System.out.println("已删除"+r+"条");
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ps.close();
				con.close();
			}
			ExecutorUtils.executeThreadPool(list);
		} catch (Exception e) {
			System.out.println("错误："+(new Date()).getTime());
			e.printStackTrace();
		}finally{
			Browser.execClientScript("$(\"button[id$='insertJobsToCIMC']\").attr('disabled','false')");
		}
		System.out.println("共计耗时（ms）："+((new Date()).getTime()-lo));
    }
    
    @Action
    public void insertArapToCIMC(){
    	Browser.execClientScript("$(\"button[id$='insertArapToCIMC']\").attr('disabled','true')");
    	long lo = (new Date()).getTime();
    	System.out.println(lo);
		StringBuffer sbsql = new StringBuffer();
		MessageUtils.alert("导入中...");
		Connection con = null;
    	Statement ps = null;
		try {
			//根据一个往来单位、收付款、币种分组统计数据
			sbsql.append("SELECT f_fina_arap_mysqlinfo('year="+qryYear+";periods="+qryPeriodS+";periode="+qryPeriodE+";userid="+AppUtils.getUserSession().getUserid()+"') AS araps");
			List<Map> list = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbsql.toString());
			if(list==null || list.size() == 0){
				System.out.println("无数据："+(new Date()).getTime());
				return;
			}
			Map<String,Long> m = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT actsetid FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()+" AND isdelete =FALSE");
	    	try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	// mysql驱动
			try {
				con = (Connection) DriverManager.getConnection("jdbc:mysql://120.25.254.109:3306/sc?useUnicode=true&characterEncoding=utf8",
				        "scuser", "K%%3q7tj91");
				ps =  (Statement) con.createStatement();
//				int r = ps.executeUpdate("delete from ufms_arapinfo where (period BETWEEN "+qryPeriodS+" AND "+qryPeriodE+") and year = "+qryYear+" and actsetid = "+m.get("actsetid")+";");
//				System.out.println("已删除"+r+"条");
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ps.close();
				con.close();
			}
			ExecutorUtils.executeThreadPool2(list);
		} catch (Exception e) {
			System.out.println("错误："+(new Date()).getTime());
			e.printStackTrace();
		}finally{
			Browser.execClientScript("$(\"button[id$='insertArapToCIMC']\").attr('disabled','false')");
		}
		System.out.println("共计耗时（ms）："+((new Date()).getTime()-lo));
    }

    public boolean running(String jobids) {
        try {
            String urlArgs2 = "";
            String sqlQry = "SELECT * FROM f_edi_vch_sap('" + urlArgs2 + "')";
            Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
            Object object = m.get("f_sdi_inttra");
            String str = "";
            if (object != null) {
                str = object.toString();
            }
            return writer(str);
        } catch (Exception e) {
            MessageUtils.showException(e);
            return false;
        }
    }

    /*
     * 写入文件
     * */
    public boolean writer(String str) {
        Random ra = new Random();
        //获得当前系统桌面路径
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        String absolutePath = desktopDir.getAbsolutePath();
        String randomString = getRandomString(3);
        File file = new File(absolutePath + "/edi/inttra");
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        String path = absolutePath + "/edi/vch/sap" + randomString + ".txt";
        File newFile = newFile(path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
            bw.write(str);
            bw.newLine();
            //关闭资源
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Bind
    @SaveState
    private String url;

    @SaveState
    private Map<String, String> cfgDataMap;

    @Action
    public void savesetExportin() {
        try {
            ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
            MessageUtils.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        this.url = this.getCfgDataMap().get("sap_url");
        update.markUpdate(true, UpdateLevel.Data, "url");
    }

    public Map<String, String> getCfgDataMap() {
        return this.cfgDataMap;
    }
}