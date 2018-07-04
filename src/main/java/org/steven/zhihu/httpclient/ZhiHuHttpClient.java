package org.steven.zhihu.httpclient;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.util.Config;
import org.steven.zhihu.util.SimpleThreadPoolExecutor;

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
    public static AtomicInteger parseUserCount = new AtomicInteger(0);
    private static long startTime = System.currentTimeMillis();
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
    private ThreadPoolExecutor detailPageThreadPool;
    /**
     * 列表页下载线程池
     */
    private ThreadPoolExecutor listPageThreadPool;

    private static String authorization;
    private ZhiHuHttpClient() {
    }
    /**
     * 初始化HttpClient
     */
    @Override
    public void initHttpClient() {

    }

    private void intiThreadPool(){
        detailPageThreadPool = new SimpleThreadPoolExecutor(Config.downloadThreadSize,
                Config.downloadThreadSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                "detailPageThreadPool");
    }

    @Override
    public void startCrawl(){
        initHttpClient();
        intiThreadPool();

        detailListPageThreadPool.execute(new DetailListPageTask(request, Config.isProxy));

    }


}
