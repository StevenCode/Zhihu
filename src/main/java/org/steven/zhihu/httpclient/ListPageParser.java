package org.steven.zhihu.httpclient;


import org.steven.zhihu.model.Page;

import java.util.List;

public interface ListPageParser extends Parser {
    List parseListPage(Page page);
}
