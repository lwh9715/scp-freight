package com.scp.schedule;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.FtUtils;
import com.scp.util.StrUtils;
import com.ufms.web.view.sysmgr.LogBean;

import java.util.List;
import java.util.Map;

/**
 * 自动获取飞驼跟踪信息
 */
public class AutoProcessLenovoData {

    public static void main(String[] args) throws Exception {
        AutoProcessLenovoData autoProcessLenovoData = new AutoProcessLenovoData();
        autoProcessLenovoData.running();
    }

    private static boolean isRun = false;

    public void execute() throws Exception {
        if (isRun) {
            System.out.print("autoProcessLenovoData wraning:another process is running!");
            return;
        }
        isRun = true;
        try {
            //暂停
            // running();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
        }
    }


    private void running() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("AutoProcessLenovoData开始");
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");

            String sql =
                    "select fj.id                                                                                                                                         as jobid,\n" +
                            "       bsc.id                                                                                                                                        as containerid,\n" +
                            "       bsc.sono                                                                                                                                      as bscsono,\n" +
                            "       bsc.cntno                                                                                                                                     as bsccntno,\n" +
                            "       (SELECT code FROM sys_corporation AS y WHERE y.id = (SELECT x.carrierid FROM bus_shipping AS x WHERE x.isdelete = FALSE AND x.jobid = fj.id)) as carriercode,\n" +
                            "       *\n" +
                            "from fina_jobs fj\n" +
                            "         inner join bus_ship_container bsc\n" +
                            "                    on bsc.isdelete = false and bsc.parentid is null and fj.id = bsc.jobid\n" +
                            "                        AND (not EXISTS(SELECT 1\n" +
                            "                                        FROM bus_ship_book_cnt x,\n" +
                            "                                             bus_ship_booking y\n" +
                            "                                        WHERE x.linkid = y.id\n" +
                            "                                          AND y.isdelete = FALSE\n" +
                            "                                          AND (bsc.cntno) = (x.cntno)\n" +
                            "                                          AND (bsc.sono) = (y.sono))\n" +
                            "                            or\n" +
                            "                             (\n" +
                            "                                 EXISTS(SELECT 1\n" +
                            "                                        FROM bus_ship_book_cnt x,\n" +
                            "                                             bus_ship_booking y\n" +
                            "                                        WHERE x.linkid = y.id\n" +
                            "                                          AND y.isdelete = FALSE\n" +
                            "                                          AND (bsc.cntno) = (x.cntno)\n" +
                            "                                          AND (bsc.sono) != (y.sono))\n" +
                            "                                 )\n" +
                            "                           )\n" +
                            "where fj.isdelete = false\n" +
                            "  and fj.customerid in (select id\n" +
                            "                        from sys_corporation\n" +
                            "                        where namec ilike '%联想%'\n" +
                            "                           or namee ilike '%lenove%'\n" +
                            "                           or namee ilike '%LENOVO%')";
            List<Map> lists = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
            if (lists != null && lists.size() > 0) {
                stringBuffer.append("，已经获取到联想箱子数据，list0长度为").append(lists.size());
                for (int i = 0; i < lists.size(); i++) {
                    Map map = lists.get(i);
                    String bscsono = StrUtils.getMapVal(map, "bscsono");
                    String bsccntno = StrUtils.getMapVal(map, "bsccntno");
                    String carriercode = StrUtils.getMapVal(map, "carriercode");
                    stringBuffer.append("，bscsonow为").append(bscsono);
                    stringBuffer.append("，bsccntno为").append(bsccntno);
                    stringBuffer.append("，carriercode为").append(carriercode);

                    FtUtils.handleFt(stringBuffer, bscsono, bsccntno, carriercode);
                    // OkktUtils.postLenovoTraceData(stringBuffer, serviceContext, resp, map,"AutoProcessLenovoData");
                }
            }
        } catch (Exception e) {
            stringBuffer.append(",running异常为").append(e.getMessage());
            isRun = false;
            return;
        }
        // LogBean.insertLastingLog2(stringBuffer, "AutoProcessLenovoData");
    }


}