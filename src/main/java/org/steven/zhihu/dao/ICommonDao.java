package org.steven.zhihu.dao;

import org.springframework.stereotype.Repository;
import org.steven.zhihu.model.Paging;

@Repository
public interface ICommonDao {
    public void addPaging(Paging paging);
}
