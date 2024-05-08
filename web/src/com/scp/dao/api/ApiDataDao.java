package com.scp.dao.api;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiData;


/**
 * @author neo
 */
@Component
public class ApiDataDao extends BaseDaoImpl<ApiData, Long> {


    protected ApiDataDao(Class<ApiData> aClass) {
        super(aClass);
    }

    public ApiDataDao() {
        this(ApiData.class);
    }
}