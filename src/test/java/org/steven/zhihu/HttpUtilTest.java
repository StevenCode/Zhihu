package org.steven.zhihu;

import org.junit.Before;
import org.junit.Test;

public class HttpUtilTest {
    private HttpUtil httpUtil;

    @Before
    public void init() {
        httpUtil = new HttpUtil();
    }

    @Test
    public void testRequest() {
        String url = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364&desktop=True";

        httpUtil.request(url,
                content -> System.out.print(content));
    }


}
