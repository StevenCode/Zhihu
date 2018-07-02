package org.steven.zhihu.dao;

import org.springframework.stereotype.Repository;
import org.steven.zhihu.model.Paging;
import org.steven.zhihu.model.Table;

@Repository
public interface ICommonDao {
    public long addPaging(Paging paging);

    public void addTable(Table table);
}
