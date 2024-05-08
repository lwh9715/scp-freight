package com.scp.service.bus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.bus.BusShipScheduleDao;
import com.scp.model.bus.BusShipSchedule;
import com.scp.util.StrUtils;
import com.scp.view.comp.common.ExcelCell;
import com.scp.view.comp.common.ExcelHeader;

@Component
public class ShipScheduleService{
	
	@Resource
	public BusShipScheduleDao busShipScheduleDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveData(BusShipSchedule data) {
		if(0 == data.getId()){
			busShipScheduleDao.create(data);
		}else{
			busShipScheduleDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipSchedule data = busShipScheduleDao.findById(id);
		busShipScheduleDao.remove(data);
	}

	/**
	 * 批量导入查询船期数据
	 * @param shipline
	 * @param yearno
	 * @param monthno
	 * @param detail
	 * @throws IOException
	 */
	public void saveDataBatch(String shipline, String yearno, String monthno,
			String detail) throws IOException {
		StringReader reader = new StringReader(detail);
		BufferedReader br = new BufferedReader(reader);
		String[] headers = null; 
		Map<Integer,List<String>> cells = new HashMap<Integer, List<String>>();
		
		String line = null;
		int index = 0;
		while ((line = br.readLine()) != null) {
			String[] str = line.split("\t");//
			if(index==0){
				headers=str;
			}else{
				List<String> list = Arrays.asList(str);
				cells.put(index, list);
			}
			index ++;
		}
		List<ExcelHeader> listHead = ExcelHeader.analysisHeader(headers);
		index = 0;
		
		Map<Integer, List<ExcelCell>> mapCell = ExcelCell.analysisCell(cells);
		
		List<BusShipSchedule> list = new ArrayList<BusShipSchedule>();
		
		for (int i = 3; i < listHead.size(); i++) {
			String head = listHead.get(i).getCelVal();
			if(head.indexOf("-")>0) {
				String[] perfix = head.split("-");
				String pol = perfix[0];
				
				String headNext = "1-1";
				String polNext;
				if(i != listHead.size()-1) {
					headNext = listHead.get(i+1).getCelVal();
					polNext = headNext.split("-")[0];
				}else {
					polNext = "";
				}
				
				if(pol.equals(polNext)) {
					for (int j=1; j<=mapCell.size(); j++) {
						List<ExcelCell> excelCells = mapCell.get(j);
						BusShipSchedule bean = new BusShipSchedule();
						
						bean.setCarrier(excelCells.get(0).getCellVal());
						bean.setVes(excelCells.get(1).getCellVal());
						bean.setVoy(excelCells.get(2).getCellVal());
						
						bean.setShipline(shipline);
						bean.setYearno(Integer.parseInt(yearno));
						bean.setMonthno(Integer.parseInt(monthno));
						
						bean.setPol(pol);
						String cls = excelCells.get(i).getCellVal();
						//AppUtils.debug("cls:"+cls);
						bean.setCls(stringToDate(cls));
						bean.setEtd(stringToDate(excelCells.get(i+1).getCellVal()));
						
						list.add(bean);
					}
					i = i + 1;
				}else {
					for (int j=1; j<=mapCell.size(); j++) {
						List<ExcelCell> excelCells = mapCell.get(j);
						BusShipSchedule bean2 = new BusShipSchedule();
						
						bean2.setCarrier(excelCells.get(0).getCellVal());
						bean2.setVes(excelCells.get(1).getCellVal());
						bean2.setVoy(excelCells.get(2).getCellVal());
						
						bean2.setShipline(shipline);
						bean2.setYearno(Integer.parseInt(yearno));
						bean2.setMonthno(Integer.parseInt(monthno));
						
						bean2.setPod(pol);
						bean2.setEtd(stringToDate(excelCells.get(i-1).getCellVal()));
						
						list.add(bean2);
					}
				}
			}
		}
		//AppUtils.debug(list);
		String delSql = "UPDATE bus_shipschedule SET isdelete = TRUE WHERE schtype = 'Q' AND yearno = "+yearno+"AND monthno = "+monthno+" AND shipline = '"+shipline+"';";
		busShipScheduleDao.executeSQL(delSql);
		for (BusShipSchedule b : list) {
			busShipScheduleDao.create(b);
		}
	}
	
	
	public Date stringToDate(String dateStr){
		String formatStr = "yyyy-MM-dd";//注意必须要用MM大写,否则会转换少几个月
		DateFormat sdf=new SimpleDateFormat(formatStr);
		Date date=null;
		if(StrUtils.isNull(dateStr)||dateStr.equals("*")){
			return date;
		}else{
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
			
		}
		
	}
	
	
	/**
	 * 批量删除
	 * @param ids
	 * @param usercode
	 */
	public void removeDate(String[] ids,String usercode) {
		String sql = "UPDATE bus_shipschedule SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		busShipScheduleDao.executeSQL(sql);
	}
	/**
	 * 根据船公司abbr查找id
	 * @param abbr
	 * @return
	 */
	public Long findShipIdByAbbr(String abbr) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT a.id");
		sb.append("\n 	FROM sys_corporation AS a ");
		sb.append("\n WHERE ");
		sb.append("\n 		a.isdelete = FALSE");
		sb.append("\n AND	a.iscarrier = TRUE");
		sb.append("\n AND a.abbr='"+abbr+"'");
		Map map =  daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		return map!=null?Long.parseLong(map.get("id").toString()):null;
	}
}
