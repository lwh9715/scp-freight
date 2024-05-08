package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpext;



/**
 *
 * @author neo
 */
@Component
public class SysCorpextDao extends BaseDaoImpl<SysCorpext, Long>{

    protected SysCorpextDao(Class<SysCorpext> persistancesClass) {
        super(persistancesClass);
    }

    public SysCorpextDao() {
        this(SysCorpext.class);
    }
}