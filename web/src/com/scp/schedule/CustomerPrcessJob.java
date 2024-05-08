package com.scp.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * 自动处理客商同步
 *
 * @author Neo
 */
public class CustomerPrcessJob {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        System.out.println("CustomerPrcessJob Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ CustomerPrcessJob wraning:another process is running!reTryCount:" + reTryCount);
            reTryCount++;
            if (reTryCount < 5) { //按2分钟一次，5次10分钟如果之前的还没响应，重新启动执行
                return;
            } else {
                reTryCount = 0;
            }
        }
        isRun = true;
        try {
            execute1();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }

    // public static void main(String[] args) {
    //     try {
    //         execute1();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    private static void execute1() throws Exception {
        StringBuffer sysLogstr = new StringBuffer();
        sysLogstr.append("CustomerPrcessJob类execute1开始执行");
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");

            String dmlSql = "select * from api_ufms_imp_customer where isprocess is null order by id limit 1";
            List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(dmlSql);
            if (!list.isEmpty()) {
                for (Map map : list) {
                    String id = StrUtils.getMapVal(map, "id");
                    String jsontxt = StrUtils.getMapVal(map, "jsontxt");

                    StringBuffer sqlBuffer0 = new StringBuffer();
                    StringBuffer sqlBuffer1 = new StringBuffer();
                    try {
                        sqlBuffer0.append("SELECT f_api_ufms_imp_customer_process('" + jsontxt + "') AS json;");
                        sqlBuffer0.append("\n update api_ufms_imp_customer set isprocess = true where id =" + id + ";");
                        serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sqlBuffer0.toString());
                    } catch (Exception e) {
                        sqlBuffer1.append("\n update api_ufms_imp_customer set isprocess = false where id =" + id + ";");
                        serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sqlBuffer1.toString());
                        sysLogstr.append("\n\n\n执行当前条信息异常,当前行信息为\n" + sqlBuffer0 + sqlBuffer1);
                        sysLogstr.append("\n\n异常原因为" + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sysLogstr.append("execute1异常,异常原因为" + e.getMessage());
        }
        sysLogstr.append("\n ,execute1结束");
        LogBean.insertLog(sysLogstr);
    }

}