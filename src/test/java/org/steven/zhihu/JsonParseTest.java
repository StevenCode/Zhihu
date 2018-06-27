package org.steven.zhihu;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Paging;

public class JsonParseTest {
    private HttpUtil httpUtil;

    @Before
    public void init() {
        httpUtil = new HttpUtil();
    }

    @Test
    public void getPagin() {
        String url = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364&desktop=True";

        httpUtil.request(url, content -> {

//            System.out.print(content);

            try {
                Activities activities = JSONObject.parseObject(content, Activities.class);
                Paging paging = activities.getPaging();
                System.out.println(paging.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
}
}
