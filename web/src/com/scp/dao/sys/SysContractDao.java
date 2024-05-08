package com.scp.dao.sys;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysContract;
import com.scp.model.sys.SysCorpext;
import org.springframework.stereotype.Component;


/**
 * @author neo
 */
@Component
public class SysContractDao extends BaseDaoImpl<SysContract, Long> {

    protected SysContractDao(Class<SysContract> persistancesClass) {
        super(persistancesClass);
    }

    public SysContractDao() {
        this(SysContract.class);
    }
}