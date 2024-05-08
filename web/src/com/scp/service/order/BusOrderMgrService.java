package com.scp.service.order;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.springframework.stereotype.Component;

import com.scp.base.LMapBase;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.order.BusOrderDao;
import com.scp.dao.order.BusOrderdtlDao;
import com.scp.dao.sys.SysCorpcontactsDao;
import com.scp.exception.NoRowException;
import com.scp.model.order.BusOrder;
import com.scp.model.order.BusOrderdtl;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusOrderMgrService {
	
	@Resource
	public BusOrderDao busOrderDao;
	
	@Resource
	public BusOrderdtlDao busOrderdtlDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public SysCorpcontactsDao sysCorpcontactsDao;
	
	
	@Resource
	public BusOrderDao busOrder;
	
	public void saveData(BusOrder data) {
		if(0 == data.getId()){
			busOrder.create(data);
		}else{
			busOrder.modify(data);
		}
	}
	
	/**
	 * 根据订单id查询费用条目FCL
	 * @param orderid 订单id
	 * @return json
	 */
	public String getOrderdtlById(Long orderid){
		boolean isen = AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en);
		String sql = 
					"\nSELECT"+
					"\n	array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+
					"\nFROM"+
					"\n	("+
					"\n			SELECT"+
					"\n				id,"+
					"\n				orderid,"+
					"\n				feeitemid,"+
					"\n				feeitemcode,"+
					"\n				customerar,"+
					"\n				customeridar,"+
					"\n				customerap,"+
					"\n				customeridap,"+
					//1582 英文翻译，订单
					(isen?"\n		 (SELECT namee FROM dat_feeitem WHERE id = d.feeitemid) AS feeitemname,":"\n				feeitemname,")+
					"COALESCE((\n 			SELECT "+(isen?"(CASE WHEN COALESCE(namee,'') <> '' THEN namee ELSE namec END)":"(CASE WHEN COALESCE(namec,'') <> '' THEN namec ELSE namee END)")+" FROM sys_corporation WHERE id = customeridar),'') AS customeridarname,"+
					"COALESCE((\n 			SELECT "+(isen?"(CASE WHEN COALESCE(namee,'') <> '' THEN namee ELSE namec END)":"(CASE WHEN COALESCE(namec,'') <> '' THEN namec ELSE namee END)")+" FROM sys_corporation WHERE id = customeridap),'') AS customeridapname,"+
					"\n				ppcc,"+
					"\n				currency," +
					"\n				COALESCE(currencyap,currency) AS currencyap,"+
					(isen?"\n		(CASE WHEN unit='箱型' THEN 'Cnt type' WHEN unit='箱' THEN 'box' ELSE 'Bill' END) AS unit,":"\n				unit,")+
					"\n				amt20,"+
					"\n				amt20_ar,"+
					"\n				amt40gp,"+
					"\n				amt40gp_ar,"+
					"\n				amt40hq,"+
					"\n				amt40hq_ar,"+
					"\n				amtother,"+
					"\n				amtother_ar,"+
					"\n				amt,"+
					"\n				amt_ar,"+
					"\n				piece20,"+
					"\n				piece40gp,"+
					"\n				piece40hq,"+
					"\n				pieceother,"+
					"\n				piece,"+
					"\n				cntypeothercode"+
					"\n			FROM"+
					"\n				bus_orderdtl d"+
					"\n			WHERE"+
					"\n				d.orderid = "+orderid+
					"\n				AND d.isdelete = FALSE"+
					"\n			ORDER BY ID"+
					"\n		) T;";
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		
		if(map != null && 1 == map.size()){
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}
	
	/**
	 * 根据订单id查询费用条目LCL
	 * @param orderid 订单id
	 * @return json
	 */
	public String getOrderdtlLclById(Long orderid){
		boolean isen = AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en);
		String sql = 
					"\nSELECT"+
					"\n	array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+
					"\nFROM"+
					"\n	("+
					"\n			SELECT"+
					"\n				id,"+
					"\n				orderid,"+
					"\n				feeitemid,"+
					"\n				feeitemcode,"+
					"\n				customerar,"+
					"\n				customeridar,"+
					"\n				customerap,"+
					"\n				customeridap,"+
					//1582 英文翻译，订单
					(isen?"\n		 (SELECT namee FROM dat_feeitem WHERE id = d.feeitemid) AS feeitemname,":"\n				feeitemname,")+
					"COALESCE((\n 			SELECT "+(isen?"(CASE WHEN COALESCE(namee,'') <> '' THEN namee ELSE namec END)":"(CASE WHEN COALESCE(namec,'') <> '' THEN namec ELSE namee END)")+" FROM sys_corporation WHERE id = customeridar),'') AS customeridarname,"+
					"COALESCE((\n 			SELECT "+(isen?"(CASE WHEN COALESCE(namee,'') <> '' THEN namee ELSE namec END)":"(CASE WHEN COALESCE(namec,'') <> '' THEN namec ELSE namee END)")+" FROM sys_corporation WHERE id = customeridap),'') AS customeridapname,"+
					"\n				ppcc,"+
					"\n				currency,"+
					"\n				currencyap,"+
					"\n				unit,"+
					"\n				amt,"+
					"\n				amt_ar AS amtar,"+
					"\n				piece,"+
					"\n				cbm"+
					"\n			FROM"+
					"\n				bus_orderdtl d"+
					"\n			WHERE"+
					"\n				d.orderid = "+orderid+
					"\n				AND d.isdelete = FALSE"+
					"\n			ORDER BY ID"+
					"\n		) T;";
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		
		if(map != null && 1 == map.size()){
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}
	/**
	 * 根据报价id查询费用海运费及附加费
	 * @param orderid 订单id
	 * @return json
	 */
	public String getPriceFeeById(Long fclid){
		String sql = "SELECT f_find_pricefee_json('fclid="+fclid+"') AS json";
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		
		if(map != null && 1 == map.size()){
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}

	/**
	 * 新建或更新附加费
	 * @param moduleList
	 */
	public void saveOrModify(List<BusOrderdtl> moduleList) {
		////System.out.println(moduleList.size());
		for (BusOrderdtl instance : moduleList) {
			busOrderdtlDao.createOrModify(instance);
		}
	}
	/**
	 * 根据id删除附加费
	 * @param lists
	 */
	public void removes(List<Long> lists) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Long id : lists) {
			String sql = "\nUPDATE bus_orderdtl SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		busOrderdtlDao.executeSQL(stringBuffer.toString());
	}

	
	/**
	 * 订单生成工作单
	 * @param pkVal
	 * @param userid
	 * @return
	 */
	public String createJobs(Long pkVal , Long userid) {
		String sql = "SELECT f_bus_order2jobs('orderid="+pkVal+";userid="+userid+"') AS jobno";
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		
		if(map != null && 1 == map.size()){
			return StrUtils.getMapVal(map, "jobno");
		}
		return "''";
	}
	public void deleteOrderById(List<Long> orderIds) throws  Exception {
		StringBuffer sb = new StringBuffer();
		for (Long l : orderIds) {
			sb.append(" OR id = ");
			sb.append(l);
		}
		String sql = "UPDATE bus_order SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE 1=2 "+sb.toString()+";";
		busOrderDao.executeSQL(sql);
	}
	
	public void checkBatchById(List<Long> orderIds) throws  Exception {
		StringBuffer sb = new StringBuffer();
		for (Long l : orderIds) {
			sb.append(" OR id = ");
			sb.append(l);
		}
		String sql = "UPDATE bus_order SET ischeck = TRUE , checktime = now()," +
					"checkter='"+AppUtils.getUserSession().getUsercode()+"' WHERE 1=2 "+sb.toString()+";";
		busOrderDao.executeSQL(sql);
	}
	
	public void cancelCheckBatchById(List<Long> orderIds) throws  Exception {
		StringBuffer sb = new StringBuffer();
		for (Long l : orderIds) {
			sb.append(" OR id = ");
			sb.append(l);
		}
		String sql = "UPDATE bus_order SET ischeck = FALSE , checktime = now()," +
					"checkter='"+AppUtils.getUserSession().getUsercode()+"' WHERE 1=2 "+sb.toString()+";";
		busOrderDao.executeSQL(sql);
	}
	
	
	/**
	 * 复制新增订单
	 * @param id
	 * @param userid
	 * @param usercode
	 * @return
	 */
	public String addcopy(String id, Long userid, String usercode,String corpid,String corpidop,String orderdate) {
		Long jobsid = Long.valueOf(id);
		String sql = "SELECT f_bus_order_addcopy('id="+jobsid+";userid="+userid+";inputer="+usercode +";corpid="+corpid+";corpidop="+corpidop+";orderdate="+orderdate+";') AS nos;";
		Map m =daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String)m.get("nos");
		
	}
	/**
	 * 更改订单类型时删除不同类型明细
	 * @param orderid
	 * @param busType
	 */
	public void updateBusType(Long orderid,String busType){
		BusOrder busorder = this.busOrderDao.findById(orderid);
		if(busorder!=null){
			if(busorder.getBustype()!=null&&!busorder.getBustype().toUpperCase().equals(busType.toUpperCase())){
				busorder.setBustype(busType);
				this.busOrderdtlDao.executeSQL("UPDATE bus_orderdtl SET isdelete = TRUE WHERE isdelete = FALSE AND orderid = "+orderid);
				this.busOrderDao.modify(busorder);
			}
		}
		
	}

	public void saveCne(String cneid, String cnename, String cnetitle,
			String type,BusOrder order) {
		
		if(type.equals("1")){
			this.saveShip(cneid, cnename, cnetitle , order);
		}else if(type.equals("2")){
			this.saveCons(cneid, cnename, cnetitle , order);
		}else if(type.equals("3")){
			this.saveNotify(cneid, cnename, cnetitle , order);
		}
		
	}

	//保存通知人
	private void saveNotify(String cneid, String cnename, String cnetitle, BusOrder order) {
		SysCorpcontacts sysCorpcontacts;
		String sql = "contactype2 = 'Z' AND isdelete = FALSE AND  name = '" + cnename+"'";
			List<SysCorpcontacts> lists = sysCorpcontactsDao.findAllByClauseWhere(sql);
			if(lists.size()>0){
				sysCorpcontacts = lists.get(0);
				sysCorpcontacts.setName(cnename);
				sysCorpcontacts.setContactxt(cnetitle);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontactsDao.createOrModify(sysCorpcontacts);
			}else{
				sysCorpcontacts = new SysCorpcontacts();
				sysCorpcontacts.setCustomerid(order.getCustomerid());
				sysCorpcontacts.setContactype("B");
				sysCorpcontacts.setContactype2("Z");
				String code = (StrUtils.isNull(cnename) ? (order.getCustomercode()
						+ "-Z-" + getCusCode("1" , order.getCustomerid())) : cnename);
				sysCorpcontacts.setName(StrUtils.isNull(cnename) ? code
						: cnename);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontacts.setSex("M");
				sysCorpcontacts.setName(cnename);
				sysCorpcontacts.setContactxt(cnetitle);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontactsDao.createOrModify(sysCorpcontacts);
			}
		
	}
	
	//保存收货人
	private void saveCons(String cneid, String cnename, String cnetitle, BusOrder order) {
		SysCorpcontacts sysCorpcontacts;
		String sql = "contactype2 = 'Y' AND isdelete = FALSE AND  name = '" + cnename+"'";
			List<SysCorpcontacts> lists = sysCorpcontactsDao.findAllByClauseWhere(sql);
			if(lists.size()>0){
				sysCorpcontacts = lists.get(0);
				sysCorpcontacts.setName(cnename);
				sysCorpcontacts.setContactxt(cnetitle);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontactsDao.createOrModify(sysCorpcontacts);
			}else{
				sysCorpcontacts = new SysCorpcontacts();
				sysCorpcontacts.setCustomerid(order.getCustomerid());
				sysCorpcontacts.setContactype("B");
				sysCorpcontacts.setContactype2("Y");
				String code = (StrUtils.isNull(cnename) ? (order.getCustomercode()
						+ "-Y-" + getCusCode("1" , order.getCustomerid())) : cnename);
				sysCorpcontacts.setName(StrUtils.isNull(cnename) ? code
						: cnename);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontacts.setSex("M");
				sysCorpcontacts.setName(cnename);
				sysCorpcontacts.setContactxt(cnetitle);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontactsDao.createOrModify(sysCorpcontacts);
			}
	}

	//保存发货人
	private void saveShip(String cneid , String cnename, String cnetitle, BusOrder order) {
		SysCorpcontacts sysCorpcontacts;
		String sql = " contactype2 = 'X' AND isdelete = FALSE AND  name = '" + cnename+"'";
			List<SysCorpcontacts> lists = sysCorpcontactsDao.findAllByClauseWhere(sql);
			if(lists.size()>0){
				sysCorpcontacts = lists.get(0);
				sysCorpcontacts.setName(cnename);
				sysCorpcontacts.setContactxt(cnetitle);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontactsDao.createOrModify(sysCorpcontacts);
			}else{
				sysCorpcontacts = new SysCorpcontacts();
				sysCorpcontacts.setCustomerid(order.getCustomerid());
				sysCorpcontacts.setContactype("B");
				sysCorpcontacts.setContactype2("X");
				String code = (StrUtils.isNull(cnename) ? (order.getCustomercode()
						+ "-X-" + getCusCode("1" , order.getCustomerid())) : cnename);
				sysCorpcontacts.setName(StrUtils.isNull(cnename) ? code
						: cnename);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontacts.setSex("M");
				sysCorpcontacts.setName(cnename);
				sysCorpcontacts.setContactxt(cnetitle);
				sysCorpcontacts.setCustomerabbr(order.getCustomercode());
				sysCorpcontactsDao.createOrModify(sysCorpcontacts);
			}
	}
	
	private Long getCusCode(String type , Long customerid) {
		String sql = "";
		if (type.equals("1")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'X' AND customerid = "
					+ customerid;
		} else if (type.equals("2")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'Y' AND customerid = "
					+ customerid;
		} else if (type.equals("3")) {
			sql = "SELECT (COUNT(*)+1) AS count FROM sys_corpcontacts WHERE contactype = 'B' AND contactype2 = 'Z' AND customerid = "
					+ customerid;
		}

		Map m = daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return (Long) m.get("count");
	}
	
	public String getAssigns(Long orderid){
		String sql = "select string_agg( rolearea ||':'|| roletypedesc||':'||usernamec,'\n') AS assign FROM _sys_user_assign t  WHERE linktype = 'D' AND linkid ="+orderid;
		try{
			Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null && map.containsKey("assign")){
				return map.get("assign").toString();
			}
		}catch (NullPointerException e) {
			//没有分派人员
		}catch (NoRowException e) {
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SaveState
	@Accessible
	private String qrySql = "\nAND 1=1";
	
	
	/**
	 *  弹窗选择订单
	 */
	public GridDataProvider getJobsnosDataProvider(final String jobnossql) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
//				if(StrUtils.isNull(jobnossql) || "\nAND 1=1".equals(qrySql))return null;
				//（录入人，修改人，业务员，客户组，委托人指派，订单指派）
				String sql = "\nAND ( cscode =	'" + AppUtils.getUserSession().getUsercode() + "' "
				+ " OR salesid = " + AppUtils.getUserSession().getUserid()
				+ " OR inputer = '" + AppUtils.getUserSession().getUsercode() + "'"
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
								"AND o.corpid <> o.corpidop AND o.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
				//+ " OR updater = '" + AppUtils.getUserSession().getUsercode() + "'"
				+ " OR EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = o.id AND x.userid ="+AppUtils.getUserSession().getUserid()+")"
				+ " OR EXISTS (SELECT 1 FROM bus_ship_booking x WHERE x.orderid = o.id AND x.isdelete = false AND (x.userprice ='"+AppUtils.getUserSession().getUsername()+"' OR x.userprice ='"+AppUtils.getUserSession().getUsercode()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsername()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsercode()+"'))"
				+ " OR" + " (EXISTS" + "\n (SELECT 1 "
				+ "\n		 FROM sys_custlib x , sys_custlib_user y "
				+ "\n	WHERE y.custlibid = x.id AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND x.libtype = 'S' AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = o.salesid))) "
				
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND z.id = o.salesid) " //组关联业务员的单，都能看到
				+ ")"
				
				+ ")";
				// 权限控制 neo 2016-07-24
				// 分公司过滤
		//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
		//			//分公司过滤(忽略迪拜)
		//			String corpfilter = "AND (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = o.corpid OR cor.id = o.corpidop)) " +
		//					"\nOR o.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
		//					"\nOR o.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
		//					"\nOR o.salesid = "+AppUtils.getUserSession().getUserid() +
		//					")";
		//			m.put("corpfilter", corpfilter);
		//		}
				String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = o.corpid OR x.corpid = o.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
				"\n OR o.salesid ="+AppUtils.getUserSession().getUserid()+") ";
		
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _bus_order o " +
					"\nWHERE isdelete = false  " +jobnossql+
					  qrySql
					  +sql// 权限控制 neo 2014-05-30
					  + corpfilter
					  +"\nORDER BY orderno"
					  +"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object[] getElementsById(String[] id) {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _bus_order o " +
					"\nWHERE isdelete = false  "+
					"\nAND id= " +id[0];
				//ApplicationUtils.debug(querySql);
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
//				if(StrUtils.isNull(jobnossql) || "\nAND 1=1".equals(qrySql))return 0;
				//（录入人，修改人，业务员，客户组，委托人指派，订单指派）
				String sql = "\nAND ( cscode =	'" + AppUtils.getUserSession().getUsercode() + "' "
				+ " OR salesid = " + AppUtils.getUserSession().getUserid()
				+ " OR inputer = '" + AppUtils.getUserSession().getUsercode() + "'"
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
								"AND o.corpid <> o.corpidop AND o.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
				//+ " OR updater = '" + AppUtils.getUserSession().getUsercode() + "'"
				+ " OR EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = o.id AND x.userid ="+AppUtils.getUserSession().getUserid()+")"
				+ " OR EXISTS (SELECT 1 FROM bus_ship_booking x WHERE x.orderid = o.id AND x.isdelete = false AND (x.userprice ='"+AppUtils.getUserSession().getUsername()+"' OR x.userprice ='"+AppUtils.getUserSession().getUsercode()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsername()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsercode()+"'))"
				+ " OR" + " (EXISTS" + "\n (SELECT 1 "
				+ "\n		 FROM sys_custlib x , sys_custlib_user y "
				+ "\n	WHERE y.custlibid = x.id AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND x.libtype = 'S' AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = o.salesid))) "
				
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND z.id = o.salesid) " //组关联业务员的单，都能看到
				+ ")"
				
				+ ")";
				// 权限控制 neo 2016-07-24
				// 分公司过滤
		//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
		//			//分公司过滤(忽略迪拜)
		//			String corpfilter = "AND (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = o.corpid OR cor.id = o.corpidop)) " +
		//					"\nOR o.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
		//					"\nOR o.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
		//					"\nOR o.salesid = "+AppUtils.getUserSession().getUserid() +
		//					")";
		//			m.put("corpfilter", corpfilter);
		//		}
				String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = o.corpid OR x.corpid = o.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
				"\n OR o.salesid ="+AppUtils.getUserSession().getUserid()+") ";
				
				String countSql = 
					"\nSELECT "
					  + "\nCOUNT(*) AS counts  " 
					  + "\nFROM _bus_order o " 
					  + "\nWHERE isdelete = false  " +jobnossql+
					  qrySql
					  +sql// 权限控制 neo 2014-05-30
					  + corpfilter + ";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
	@SaveState
	@Accessible
	private  String popQryKey;
	
	public void qryOrders(String popQryKey) {
		if(StrUtils.isNull(popQryKey)){
			qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey)) this.qrySql = "\nAND 1=1";
		this.popQryKey =this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("\nAND ");
		sbsql.append("\n	(FALSE ");
		sbsql.append("\n		 OR	UPPER(orderno) LIKE '%" + this.popQryKey + "%' ");
		sbsql.append("\n	 )");
		qrySql = sbsql.toString();
	}
	
	public List<Map> findorders(String qryPortKey){
		qryPortKey = qryPortKey.toUpperCase();
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("\nAND ");
		sbsql.append("\n	(FALSE ");
		sbsql.append("\n	OR	UPPER(orderno) LIKE '%" + qryPortKey + "%' ");
		sbsql.append("\n	 )");
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM _bus_order o " +
			"\nWHERE isdelete = false " +
			sbsql.toString();
		
		//（录入人，修改人，业务员，客户组，委托人指派，订单指派）
		String sql = "\nAND ( cscode =	'" + AppUtils.getUserSession().getUsercode() + "' "
		+ " OR salesid = " + AppUtils.getUserSession().getUserid()
		+ " OR inputer = '" + AppUtils.getUserSession().getUsercode() + "'"
		+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
						"AND o.corpid <> o.corpidop AND o.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
		//+ " OR updater = '" + AppUtils.getUserSession().getUsercode() + "'"
		+ " OR EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = o.id AND x.userid ="+AppUtils.getUserSession().getUserid()+")"
		+ " OR EXISTS (SELECT 1 FROM bus_ship_booking x WHERE x.orderid = o.id AND x.isdelete = false AND (x.userprice ='"+AppUtils.getUserSession().getUsername()+"' OR x.userprice ='"+AppUtils.getUserSession().getUsercode()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsername()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsercode()+"'))"
		+ " OR" + " (EXISTS" + "\n (SELECT 1 "
		+ "\n		 FROM sys_custlib x , sys_custlib_user y "
		+ "\n	WHERE y.custlibid = x.id AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND x.libtype = 'S' AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = o.salesid))) "
		
		+ "\n	OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_role y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid  AND z.isdelete = false AND z.id = o.salesid) " //组关联业务员的单，都能看到
		+ ")"
		
		+ ")";
		// 权限控制 neo 2016-07-24
		// 分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			//分公司过滤(忽略迪拜)
//			String corpfilter = "AND (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = o.corpid OR cor.id = o.corpidop)) " +
//					"\nOR o.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR o.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR o.salesid = "+AppUtils.getUserSession().getUserid() +
//					")";
//			m.put("corpfilter", corpfilter);
//		}
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = o.corpid OR x.corpid = o.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR o.salesid ="+AppUtils.getUserSession().getUserid()+") ";
		querySql = querySql + sql + corpfilter;
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		return list;
	}
	
	
	
	/**
	 * 订单生成入仓单
	 * @param pkVal
	 * @param userid
	 * @return
	 */
	public String createJobsin(Long pkVal , Long userid) {
		String sql = "SELECT f_bus_warehouinjobs('orderid="+pkVal+";userid="+userid+"') AS jobno";
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		
		if(map != null && 1 == map.size()){
			return StrUtils.getMapVal(map, "jobno");
		}
		return "''";
	}
	
	
	
	
}
