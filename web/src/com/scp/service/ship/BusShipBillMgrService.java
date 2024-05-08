package com.scp.service.ship;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatCntypeDao;
import com.scp.dao.ship.BusShipBillDao;
import com.scp.dao.ship.BusShipContainerDao;
import com.scp.model.ship.BusShipBill;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusShipBillMgrService {
	
	@Resource
	public BusShipBillDao busShipBillDao;

	@Resource
	public BusShipContainerDao busShipContainerDao;

	@Resource
	public DatCntypeDao datCntypeDao;

	public void saveData(BusShipBill data) {
		if (0 == data.getId()) {
			busShipBillDao.create(data);
		} else {
			busShipBillDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		String sql = "UPDATE bus_ship_bill SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id =" + id;
		this.busShipBillDao.executeSQL(sql);
		// BusShipBill data = busShipBillDao.findById(id);
		// busShipBillDao.remove(data);
	}

	public void cancelship(String[] ids, Long mPkVal) {
		String sql = "UPDATE bus_ship_container SET billid = null WHERE id = any(array[" + StrUtils.array2List(ids) + "])";
		busShipBillDao.executeSQL(sql);
	}

	public void chooseship(String[] ids, Long mPkVal) {

		String id = StrUtils.array2List(ids);
		String sql = "UPDATE bus_ship_container SET billid = " + mPkVal+ " WHERE id = ANY(ARRAY["+StrUtils.array2List(ids)+"])";
		busShipBillDao.executeSQL(sql);
	}

	public void copybill(String type,String[] ids, Long jobid, Long shipid) {
		String sql ="";
		String text = "'id=" + Long.valueOf(ids[0]) + ";jobid=" + jobid
		+ ";shipid=" + shipid + ";usercode="
		+ AppUtils.getUserSession().getUsercode() + ";corpid="
		+ AppUtils.getUserSession().getCorpid() + "'";
		if(type.equals("H")){
				sql = "SELECT f_bus_shipbill_copy(" + text + ");";
		}else{
			 	sql = "SELECT f_bus_shipbill_copymbl(" + text + ");";
		}
		busShipBillDao.executeQuery(sql);
	}
	
	/**
	 * Neo 2013/06/18
	 */
	public void chooseShip(String[] ids, Long billid, String bltype,long jobid,boolean isDetail) {
		String dmlSql = "";
		if("H".equals(bltype)) {//HBL
			dmlSql = "\nUPDATE bus_ship_container SET billid = NULL WHERE   jobid  = "
				+ jobid + " AND billid="+billid+";";
			for (int i = 0; i < ids.length; i++) {
				dmlSql += "\nUPDATE bus_ship_container SET billid = " + billid
						+ " WHERE id = " + ids[i] + " AND jobid = "+jobid+";";
			}
		} else if ("M".equals(bltype)) {//MBL
			dmlSql = "\nUPDATE bus_ship_container SET billmblid = NULL WHERE jobid  = "
				+ jobid + " AND billmblid="+billid+";";
			for (int i = 0; i < ids.length; i++) {
				dmlSql += "\nUPDATE bus_ship_container SET billmblid = " + billid
						+ " WHERE id = " + ids[i] + " AND jobid = "+jobid+";";
			}
		}
		if (!StrUtils.isNull(dmlSql)) {
			busShipBillDao.executeSQL(dmlSql);
			// 更新提单信息
			busShipBillDao.executeQuery("SELECT f_bus_ship_container_createbillinfo('billid=" + billid + ";bltype=" + bltype + ";isdetail="+isDetail+"');");
		}
	}

	public void updateDate(String[] ids, Long jobid) {
		String sql = "UPDATE bus_shipping b SET mblno = (SELECT (string_agg(DISTINCT p.mblno,',')) FROM bus_ship_bill p WHERE p.id in ("+StrUtils.array2List(ids)+")) WHERE b.jobid = "+jobid;
		busShipBillDao.executeSQL(sql);
	}

//	public Boolean getIsLCL(Long mPkVal) {
//		String sql = "SELECT EXISTS(SELECT 1 FROM bus_ship_container WHERE billid = " + mPkVal + " AND ldtype = 'L' AND isdelete = FALSE) AS islcl;";
//		Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		return Boolean.valueOf(m.get("islcl").toString());
//	}

	// /**
	// * Neo
	// * 2014/04/14
	// * 去除字符串数组中重复的字符串，并将剩余的字符串按照
	// * （重复次数 *字符串，重复次数 *字符串...）拼接
	// * @param args
	// * @return
	// */
	// public static String checkDuplicate(String[] args) {
	// for(int i=0;i<args.length;i++) {
	// if(args[i] == null) args[i] = "UNKNOWN";
	// int count = 1;
	// if(args[i].equals("0")) continue;
	// if(i < (args.length - 1)) {
	// for(int j=i+1;j<args.length;j++) {
	// if(args[i].equals(args[j])) {
	// count++;
	// args[j] = "0";
	// }
	// }
	// }
	// args[i] = count + "*" + args[i];
	// }
	//		
	// StringBuffer sb = new StringBuffer();
	// sb.append("(");
	// int lastArg = args.length-1;
	// for(int l=args.length-1;l>0;l--) {
	// if(args[l].equals("0")) {
	// if(!args[l-1].equals("0")) {
	// lastArg = l - 1;
	// break;
	// }
	// } else {
	// lastArg = l;
	// break;
	// }
	// }
	//		
	// for(int k=0;k<=lastArg;k++) {
	// if(args[k].equals("0"))
	// continue;
	// if(k == lastArg) {
	// sb.append(args[k]);
	// } else {
	// sb.append(args[k] + ",");
	// }
	// }
	// sb.append(")");
	// return sb.toString();
	// }

}