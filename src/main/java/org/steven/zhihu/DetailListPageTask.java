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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 知乎用户列表详情页task
 */
public class DetailListPageTask extends AbstractPageTask{
    private static Logger logger = LoggerFactory.getLogger(DetailListPageTask.class);
    /**
     * Thread-数据库连接
     */
    private static Map<Thread, Connection> connectionMap = new ConcurrentHashMap<>();




    public DetailListPageTask(HttpRequestBase request, boolean proxyFlag, long time) {
        super(request, proxyFlag, time);
    }



    @Override
    protected void retry() {
        zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, Config.isProxy, time));
    }

    @Override
    protected void handle(Page page) {

        if (page.getStatusCode() != Constants.HTTP_STAUTS_OK) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {


            }
            retry();
            return;
        }

        try {

            Activities activities = JSONObject.parseObject(page.getHtml(), Activities.class);
            Paging paging = activities.getPaging();
            paging.setCurrent(request.getURI().toURL().toString());
            activities.setTime(time+84600);
            activities.setPaging(paging);
            Constants.tableQueue.add(activities);
            if (!paging.isIs_end()) {
                if (time == 0) {
                    return;
                }
                String url = paging.getNext();

                Pattern pattern = Pattern.compile("^http.*after_id=(\\d+).*");
                Matcher matcher = pattern.matcher(url);
                if(matcher.find()){
                    String group = matcher.group(1);
                    long l = Long.parseLong(group);
                    if (l < time) {
                        return;
                    }
                    HttpGet nextRequest = new HttpGet(paging.getNext());
                    logger.info("paging.getNext():"+paging.getNext());
                    zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(nextRequest, Config.isProxy, time));
                }else {
                    System.out.println("not mathch:   "+url);
                }



            }
        } catch (Exception e) {
            logger.error(page.getHtml());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
            retry();
            return;

        }
    }



    public static Map<Thread, Connection> getConnectionMap() {
        return connectionMap;
    }


}
