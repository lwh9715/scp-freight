package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.EdiinttraphraseDao;
import com.scp.model.data.Ediinttraphrase;

@Component
public class EdiinttraphraseMgrService {

    @Resource
    public EdiinttraphraseDao ediinttraphraseDao;

    public void saveData(Ediinttraphrase data) {
        if (0 == data.getId()) {
            ediinttraphraseDao.create(data);
        } else {
            ediinttraphraseDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        Ediinttraphrase data = ediinttraphraseDao.findById(id);
        ediinttraphraseDao.remove(data);
    }
}