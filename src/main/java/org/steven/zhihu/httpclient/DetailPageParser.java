package org.steven.zhihu.httpclient;

import org.steven.zhihu.model.Page;
import org.steven.zhihu.model.Table;

public interface DetailPageParser extends Parser {
    Table parseDetailPage(Page page);
}
