package org.steven.zhihu.proxy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.httpclient.ZhiHuHttpClient;
import org.steven.zhihu.model.Proxy;
import org.steven.zhihu.proxy.ProxyPool;
import org.steven.zhihu.util.Config;
import org.steven.zhihu.util.HttpClientUtil;
import org.steven.zhihu.util.ProxyUtil;

/**
 * 代理序列化
 */
public class ProxySerializeTask implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(ProxyPageTask.class);
    @Override
    public void run() {
        while (!ZhiHuHttpClient.isStop){
            try {
                Thread.sleep(1000 * 60 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Proxy[] proxyArray = null;
            ProxyPool.lock.readLock().lock();
            try {
                proxyArray = new Proxy[ProxyPool.proxySet.size()];
                int i = 0;
                for (Proxy p : ProxyPool.proxySet){
                    if (!ProxyUtil.isDiscardProxy(p)){
                        proxyArray[i++] = p;
                    }
                }
            } finally {
                ProxyPool.lock.readLock().unlock();
            }

            HttpClientUtil.serializeObject(proxyArray, Config.proxyPath);
            logger.info("成功序列化" + proxyArray.length + "个代理");
        }
    }
}
