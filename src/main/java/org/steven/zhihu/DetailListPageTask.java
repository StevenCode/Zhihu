package org.steven.zhihu;



import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Page;
import org.steven.zhihu.model.Paging;
import org.steven.zhihu.util.Config;
import org.steven.zhihu.util.Constants;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 知乎用户列表详情页task
 */
public class DetailListPageTask extends AbstractPageTask{
    private static Logger logger = LoggerFactory.getLogger(DetailListPageTask.class);
    /**
     * Thread-数据库连接
     */
    private static Map<Thread, Connection> connectionMap = new ConcurrentHashMap<>();




    public DetailListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }



    @Override
    protected void retry() {
        zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, Config.isProxy));
    }

    @Override
    protected void handle(Page page) {

        if (page.getStatusCode() != Constants.HTTP_STAUTS_OK) {
            retry();
            return;
        }

        try {

            Activities activities = JSONObject.parseObject(page.getHtml(), Activities.class);
            Paging paging = activities.getPaging();
            Constants.tableQueue.add(activities);
            if (!paging.isIs_end()) {
                Constants.urlQueue.add(paging.getNext());
            }else {
                Constants.urlQueue.add("empty");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static Map<Thread, Connection> getConnectionMap() {
        return connectionMap;
    }

}
