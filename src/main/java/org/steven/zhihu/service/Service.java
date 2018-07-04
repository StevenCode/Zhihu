package org.steven.zhihu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.steven.zhihu.dao.ICommonDao;
import org.steven.zhihu.model.Paging;
import org.steven.zhihu.model.Table;


@org.springframework.stereotype.Service
public class Service {

    @Autowired
    public ICommonDao commonDao;

    public long addPagin(Paging paging) {
         commonDao.addPaging(paging);
        return paging.getId();
    }

    public void addTable(Table table) {
        commonDao.addTable(table);
    }
}
