package org.steven.zhihu.httpclient;



import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.AbstractPageTask;
import org.steven.zhihu.DetailListPageTask;
import org.steven.zhihu.util.Config;
import org.steven.zhihu.util.Constants;
import org.steven.zhihu.util.SimpleThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 用户抓取HttpClient
 */
public class ZhiHuHttpClient extends AbstractHttpClient implements IHttpClient{
    private static Logger logger = LoggerFactory.getLogger(ZhiHuHttpClient.class);
    private volatile static ZhiHuHttpClient instance;
    /**
     * 统计用户数量
     */
    public static volatile boolean isStop = false;

    public static ZhiHuHttpClient getInstance(){
        if (instance == null){
            synchronized (ZhiHuHttpClient.class){
                if (instance == null){
                    instance = new ZhiHuHttpClient();
                }
            }
        }
        return instance;
    }
    /**
     * 详情页下载线程池
     */
    private ThreadPoolExecutor detailListPageThreadPool;


    private ZhiHuHttpClient() {
    }
    /**
     * 初始化HttpClient
     */
    @Override
    public void initHttpClient() {

    }

    private void intiThreadPool(){
        detailListPageThreadPool = new SimpleThreadPoolExecutor(Config.downloadThreadSize,
                Config.downloadThreadSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2000),
                new ThreadPoolExecutor.DiscardPolicy(),
                "detailListPageThreadPool");
    }

    @Override
    public void startCrawl() throws InterruptedException {
        initHttpClient();
        intiThreadPool();

        BlockingQueue<String> urlQueue = Constants.urlQueue;
        urlQueue.add(Constants.USER_FOLLOWEES_URL);
        String url;
        while ((url = urlQueue.take() )!= null) {
            System.out.println(url);
            if (url.equals("empty")) {
                break;
            }
            HttpGet request = new HttpGet(url);
            detailListPageThreadPool.execute(new DetailListPageTask(request, Config.isProxy));
        }


    }

    public ThreadPoolExecutor getDetailListPageThreadPool() {
        return detailListPageThreadPool;
    }
}
