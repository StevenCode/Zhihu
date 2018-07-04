package org.steven.zhihu;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Data;
import org.steven.zhihu.model.Paging;
import org.steven.zhihu.model.Table;
import org.steven.zhihu.service.Service;

import java.util.List;

public class AbstractPageTask implements Runnable{

    @Autowired
    private Service service;
    private String url;

    public AbstractPageTask(String url) {
        this.url = url;
    }

    public void deal() {

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.request(url, content -> {


            try {
                Activities activities = JSONObject.parseObject(content, Activities.class);
                Paging paging = activities.getPaging();
                Constant.tableQueue.add(activities);


                if (!paging.isIs_end()) {
                    Constant.urlQueue.add(paging.getNext());
                }else {
                    Constant.urlQueue.add("empty");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void run() {
        deal();
    }
}
