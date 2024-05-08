package com.scp.schedule;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.FtUtils;
import com.scp.util.StrUtils;
import com.ufms.web.view.sysmgr.LogBean;

import java.util.List;
import java.util.Map;

/**
 * 自动提交飞驼跟踪信息
 */
public class AuotoPostApiFTWR2 {

    public static void main(String[] args) throws Exception {
        AuotoPostApiFTWR2 AuotoPostApiFTWR2 = new AuotoPostApiFTWR2();
        AuotoPostApiFTWR2.running();
    }

    private static boolean isRun = false;

    public void execute() throws Exception {
        if (isRun) {
            System.out.print("AuotoPostApiFTWR2 wraning:another process is running!");
            return;
        }
        isRun = true;
        try {
            running();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
        }
    }


    private void running() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("AuotoPostApiFTWR2开始");

            String sql =
                    "select *\n" +
                            "from (\n" +
                            "         SELECT coalesce(sono, '')                         as billno\n" +
                            "              , coalesce((SELECT (SELECT ad.namee FROM api_data ad WHERE isdelete = false and ad.srctype = 'FTWR' AND maptype = 'CARRIER' and code = sc.code limit 1)\n" +
                            "                          FROM sys_corporation sc\n" +
                            "                          WHERE id = _bsbc.carrierid), '') AS carriercode\n" +
                            "         FROM _bus_ship_booking_cimc _bsbc\n" +
                            "         where 1 = 1\n" +
                            "           AND NOT EXISTS(SELECT 1 FROM edi_inttra_report eir where editype = 'FTWR' and eir.sono = _bsbc.sono)\n" +
                            "     ) aaa\n" +
                            "where 1 = 1\n" +
                            "  and billno <> ''\n" +
                            "  and carriercode <> ''\n" +
                            "limit 500";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> lists = daoIbatisTemplate.queryWithUserDefineSql(sql);
            stringBuffer.append(",sql").append(sql);
            if (lists != null && lists.size() > 0) {
                stringBuffer.append(",lists长度为").append(lists.size());
                for (int i = 0; i < lists.size(); i++) {
                    try {
                        Map map = lists.get(i);
                        String billno = StrUtils.getMapVal(map, "billno");
                        String carriercode = StrUtils.getMapVal(map, "carriercode");

                        FtUtils.handleFt(stringBuffer, billno, "", carriercode);
                    } catch (Exception e) {
                        stringBuffer.append(",e为").append(e.getMessage());
                    }
                }
            }
        } catch (Exception e1) {
            stringBuffer.append("nbsp,e1为").append(e1.getMessage());
            isRun = false;
        }
        LogBean.insertLastingLog3(stringBuffer, "AuotoPostApiFTWR2", "DEBUG");
    }
}