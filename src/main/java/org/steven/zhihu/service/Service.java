package org.steven.zhihu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.steven.zhihu.dao.ICommonDao;
import org.steven.zhihu.model.Paging;


@org.springframework.stereotype.Service
public class Service {

    @Autowired
    public ICommonDao commonDao;

    public void addPagin(Paging paging) {
        commonDao.addPaging(paging);
    }
}
