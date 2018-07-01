package org.steven.zhihu;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Data;
import org.steven.zhihu.model.Paging;

import java.util.List;

public class JsonParseTest {
    private HttpUtil httpUtil;

    @Before
    public void init() {
        httpUtil = new HttpUtil();
    }

    @Test
    public void getPagin() {
        String url = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364" +
                "&desktop=True";

        httpUtil.request(url, content -> {

//            System.out.print(content);
//            System.out.print("\n");

            try {
                Activities activities = JSONObject.parseObject(content, Activities.class);
                Paging paging = activities.getPaging();
                System.out.println(paging.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


    @Test
    public void getData() {
        String url = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364" +
                "&desktop=True";

        httpUtil.request(url, content -> {


            try {
                Activities activities = JSONObject.parseObject(content, Activities.class);
                List<Data> data = activities.getData();
                System.out.println(data.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
