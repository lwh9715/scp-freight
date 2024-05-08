package com.scp.service.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaRpreqDao;
import com.scp.dao.finance.FinaRpreqdtlDao;
import com.scp.model.finance.FinaRpreq;
import com.scp.model.finance.FinaRpreqdtl;
import com.scp.util.AppUtilBase;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class RpReqMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaRpreqDao finaRpreqDao; 
	
	@Resource
	public FinaRpreqdtlDao finaRpreqdtlDao; 
	
	

	public void saveData(FinaRpreq data) {
		if(0 == data.getId()){
			finaRpreqDao.create(data);
		}else{
			finaRpreqDao.modify(data);
		}
	}
	
	public void saveData(FinaRpreqdtl data) {
		if(0 == data.getId()){
			finaRpreqdtlDao.create(data);
		}else{
			finaRpreqdtlDao.modify(data);
		}
	}



	public void removeDate(Long id) {
		String sql = "UPDATE fina_rpreq SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id;
		finaRpreqDao.executeSQL(sql);
	}



	public void saveDataDtl(FinaRpreq data, List<Map> modifiedData) {
		for (Map m : modifiedData) {
			String rpreqdtlid = StrUtils.getMapVal(m, "rpreqdtlid");
			String amtreq = StrUtils.getMapVal(m, "amtreq");
			String arapid = StrUtils.getMapVal(m, "arapid");
			
			if(StrUtils.isNull(amtreq)){
				continue;
			}
			
			FinaRpreqdtl finaRpreqdtl = new FinaRpreqdtl();
			if(rpreqdtlid.equals("0")){
				finaRpreqdtl.setRpreqid(data.getId());
			}else{
				finaRpreqdtl = (FinaRpreqdtl) finaRpreqdtlDao.findById(Long.valueOf(rpreqdtlid));
			}
			finaRpreqdtl.setAmtreq(new BigDecimal(amtreq));
			finaRpreqdtl.setArapid(Long.valueOf(arapid));
			
			saveData(finaRpreqdtl);
		}
	} 
	
//	//付款申请生成收付款
//	public void createRP(Long pkVal , String userCode) {
//		String sql = "SELECT f_fina_generate_rp('srctype=rpreq;id="+pkVal+";user="+userCode+"');";
//		finaRpreqdtlDao.executeQuery(sql);
//	}
	
	//多个付款申请生成收付款
	public long createRPmany(String[] ids , String userCode) {
		String id = StrUtils.array2List(ids);
		String sql = "SELECT f_fina_generate_rp_sum('id="+id+";user="+userCode+"') AS a;";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		//System.out.println(m.get("a"));
		return Long.parseLong(m.get("a")==null?"":m.get("a").toString());
	}
	
	//多个付款申请清除收付款
	public void delGenerates(String[] ids) {
		String id = StrUtils.array2List(ids);
//		String sql = "UPDATE fina_actpayrec a SET isdelete = TRUE  WHERE EXISTS(SELECT 1 FROM fina_rpreq x WHERE x.actpayrecid = a.id AND x.id in ("+id+"))";
		String sql = "SELECT f_fina_generate_rp_clear('id="+id+"') AS a;";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
	}
	
	//标记已收款/未收款
	public void setIspay(String[] ids,boolean Ispay) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_rpreq SET ispay = "+Ispay+" WHERE id IN ("+id+");"; 
		finaRpreqDao.executeSQL(sql);
	}
	
	

//	public void createRP(String sn , String userCode) {
//		FinaRpreq finaRpreq = finaRpreqDao.findByNo(sn);
//		createRP(finaRpreq.getId() , userCode);
//	}

	public void updateCheck(String[] ids , String updater) throws Exception {
//		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_rpreq SET ischeck = true ,checktime = NOW(),checkter = '"+updater+"' WHERE id in ("+id+")";
		finaRpreqDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_rpreq SET ischeck = false  WHERE id in ("+id+")";
		AppUtilBase.debug(sql);
		finaRpreqDao.executeSQL(sql);
	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE fina_rpreqdtl SET isdelete=TRUE ,amtreq = 0  WHERE id in (" + id
				+ ")";
		finaRpreqDao.executeSQL(sql);
		
	}
	
	
//	public void finishDate(String[] ids ,  String usercode ,boolean isConfirm) {
//		StringBuilder builder = new StringBuilder();
//		for (String id : ids){
//			if(isConfirm){
//				builder.append("\nUPDATE fina_rpreqdtl SET isconfirm = TRUE, confirmer = '"+usercode+"' , confirmdate = NOW() WHERE id='"+id+"';");
//			}else{
//				builder.append("\nUPDATE fina_rpreqdtl SET isconfirm = FALSE, confirmer = null , confirmdate = null WHERE id='"+id+"';");
//			}
//		}
//		finaRpreqDao.executeSQL(builder.toString());
//	}

	public void saveRemark(String remark, Long id, String userCode) {
		String sql = "update fina_rpreq set remark = '"+remark+"',updater = '"+userCode+"',updatetime = '"+new Date()+"' where id = "+id;
		finaRpreqDao.executeSQL(sql);
	}

	
	
	
}
