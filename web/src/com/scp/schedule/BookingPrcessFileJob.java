package com.scp.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.scp.model.ship.BusShipBooking;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * 自动处理附件
 *
 * @author Neo
 */
public class BookingPrcessFileJob {

    private static boolean isRun = false;
    private static int reTryCount = 0;

    public void execute() throws Exception {
        System.out.println("BookingPrcessFileJob Start:" + new Date());
        if (isRun) {
            System.out.println("@@@ BookingPrcessFileJob wraning:another process is running!reTryCount:" + reTryCount);
            reTryCount++;
            if (reTryCount < 5) { //按2分钟一次，5次10分钟如果之前的还没响应，重新启动执行
                return;
            } else {
                reTryCount = 0;
            }
        }
        isRun = true;
        try {
            bookingPrcessFile();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
            reTryCount = 0;
        }
    }

    //    public static void main(String[] args) {
    //        try {
    //            receive();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }

    private static void bookingPrcessFile() throws Exception {
        StringBuffer sysLogstr = new StringBuffer();
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");

            String dmlSql = "select * from bus_ship_booking_prcessfile where isdelete = false";
            List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(dmlSql);
            if (!list.isEmpty()) {
                for (Map map : list) {
                    String sono = StrUtils.getMapVal(map, "sono");
                    String currenttimestr = StrUtils.getMapVal(map, "currenttimestr");

                    List<BusShipBooking> BusShipBookingList = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere
                            ("sono = '" + sono + "' AND inputer = 'inttra' AND isdelete = false");
                    if (BusShipBookingList != null && BusShipBookingList.size() > 0) {
                        StringBuffer sqlBuffer = new StringBuffer();
                        for (BusShipBooking bsb : BusShipBookingList) {
                            String linkid = String.valueOf(bsb.getId());
                            sqlBuffer.append("\n update sys_attachment set linkid = " + linkid + " where linkid = " + currenttimestr + ";");
                        }
                        sqlBuffer.append("\n update bus_ship_booking_prcessfile set isdelete = true , updater = 'BookingPrcessFileJob', updatetime = now() where currenttimestr ='" + currenttimestr + "';");
                        serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sqlBuffer.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sysLogstr.append("bookingPrcessFile异常,异常原因为" + e.getMessage());
        }
        LogBean.insertLog(sysLogstr);
    }

}