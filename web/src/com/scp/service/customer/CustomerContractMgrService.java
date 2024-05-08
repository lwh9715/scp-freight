package com.scp.service.customer;

import com.scp.dao.sys.SysContractDao;
import com.scp.model.sys.SysContract;
import com.scp.util.StrUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author CIMC
 */
@Component
public class CustomerContractMgrService {

    @Resource
    public SysContractDao sysContractDao;

    public void saveData(SysContract data) {
        if (0 == data.getId()) {
            sysContractDao.create(data);
        } else {
            sysContractDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        SysContract data = sysContractDao.findById(id);
        sysContractDao.remove(data);
    }

    public void removeDatedel(Long pkid) {

        String sql = "update sys_contract set isdelete=TRUE WHERE id = " + pkid
                + ";";
        sysContractDao.executeSQL(sql);

    }

    public void delBatch(String[] ids) {
        String id = StrUtils.array2List(ids);
        String sql = "UPDATE sys_contract SET isdelete = true  WHERE id in (" + id + ")";
        sysContractDao.executeSQL(sql);

    }

}