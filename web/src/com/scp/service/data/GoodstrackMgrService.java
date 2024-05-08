package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatGoodstrackDao;
import com.scp.model.data.DatGoodstrack;

@Component
public class GoodstrackMgrService {

    @Resource
    public DatGoodstrackDao datGoodstrackDao;

    public void saveData(DatGoodstrack data) {
        if (0 == data.getId()) {
            datGoodstrackDao.create(data);
        } else {
            datGoodstrackDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        DatGoodstrack data = datGoodstrackDao.findById(id);
        datGoodstrackDao.remove(data);
    }
}