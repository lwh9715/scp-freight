package com.scp.service.data;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.data.DatCntypeDao;
import com.scp.model.data.DatCntype;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class CntypeMgrService {

	@Resource
	public DatCntypeDao datcntypeDao;

	public void saveData(DatCntype data) {
		if (0 == data.getId()) {
			datcntypeDao.create(data);
		} else {
			datcntypeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatCntype data = datcntypeDao.findById(id);
		datcntypeDao.remove(data);
	}
	/**
	 * 获取其它箱型箱量类型(不包括20'GP,40'GP,40'HQ)
	 * @return
	 */
	public String getOtherCnyTypeForJson() {
		String sql = 
			"\nSELECT"+
			"\n	array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+
			"\nFROM"+
			"\n	("+
			"\n			SELECT"+
			"\n				code"+
			"\n			FROM"+
			"\n				dat_cntype"+
			"\n			WHERE"+
			"\n				isdelete = FALSE"+
			"\n				AND code <> '20''GP'"+
			"\n				AND code <> '40''GP'"+
			"\n				AND code <> '40''HQ'"+
			"\n			ORDER BY code"+
			"\n		) T;";
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
				.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if (map != null && 1 == map.size()) {
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}
}