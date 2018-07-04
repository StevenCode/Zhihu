package org.steven.zhihu;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.httpclient.ZhiHuHttpClient;
import org.steven.zhihu.model.*;
import org.steven.zhihu.proxy.ProxyPool;
import org.steven.zhihu.util.Constants;
import org.steven.zhihu.util.HttpClientUtil;
import org.steven.zhihu.util.ProxyUtil;

import java.io.IOException;

public abstract class AbstractPageTask implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(AbstractPageTask.class);

    protected String url;
    protected HttpRequestBase request;
    protected long time;
    protected Proxy currentProxy;//当前线程使用的代理
    protected boolean proxyFlag;//是否通过代理下载
    protected static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();


    public AbstractPageTask(){

    }
    public AbstractPageTask(String url, boolean proxyFlag, long time){
        this.url = url;
        this.proxyFlag = proxyFlag;
        this.time = time;
    }

    public AbstractPageTask(HttpRequestBase request, boolean proxyFlag, long tim){
        this.request = request;
        this.proxyFlag = proxyFlag;
        this.time = time;
    }

    public void run(){
        long requestStartTime = 0l;
        HttpGet tempRequest = null;
        try {
            Page page = null;
            if(url != null){
                if (proxyFlag){
                    tempRequest = new HttpGet(url);
                    currentProxy = ProxyPool.proxyQueue.take();
                    if(!(currentProxy instanceof Direct)){
                        HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
                        tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
                    }
                    requestStartTime = System.currentTimeMillis();
                    page = zhiHuHttpClient.getWebPage(tempRequest);
                }else {
                    requestStartTime = System.currentTimeMillis();
                    page = zhiHuHttpClient.getWebPage(url);
                }
            } else if(request != null){
                if (proxyFlag){
                    currentProxy = ProxyPool.proxyQueue.take();
                    if(!(currentProxy instanceof Direct)) {
                        HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
                        request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
                    }
                    requestStartTime = System.currentTimeMillis();
                    page = zhiHuHttpClient.getWebPage(request);
                }else {
                    requestStartTime = System.currentTimeMillis();
                    page = zhiHuHttpClient.getWebPage(request);
                }
            }
            long requestEndTime = System.currentTimeMillis();
            page.setProxy(currentProxy);
            int status = page.getStatusCode();
            String logStr = Thread.currentThread().getName() + " " + currentProxy +
                    "  executing request " + page.getUrl()  + " response statusCode:" + status +
                    "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
            if(status == HttpStatus.SC_OK){
                if (page.getHtml().contains("zhihu") && !page.getHtml().contains("安全验证")){
                    logger.debug(logStr);
                    currentProxy.setSuccessfulTimes(currentProxy.getSuccessfulTimes() + 1);
                    currentProxy.setSuccessfulTotalTime(currentProxy.getSuccessfulTotalTime() + (requestEndTime - requestStartTime));
                    double aTime = (currentProxy.getSuccessfulTotalTime() + 0.0) / currentProxy.getSuccessfulTimes();
                    currentProxy.setSuccessfulAverageTime(aTime);
                    currentProxy.setLastSuccessfulTime(System.currentTimeMillis());
                    handle(page);
                }else {
                    /**
                     * 代理异常，没有正确返回目标url
                     */
                    logger.warn("proxy exception:" + currentProxy.toString());
                }

            }
            /**
             * 401--不能通过验证
             */
            else if(status == 404 || status == 401 ||
                    status == 410){
                logger.warn(logStr);
            }
            else {
                logger.error(logStr);
                Thread.sleep(100);
                retry();
            }
        } catch (InterruptedException e) {
            logger.error("InterruptedException", e);
        } catch (IOException e) {
            if(currentProxy != null){
                /**
                 * 该代理可用，将该代理继续添加到proxyQueue
                 */
                currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
            }

        } finally {
            if (request != null){
                request.releaseConnection();
            }
            if (tempRequest != null){
                tempRequest.releaseConnection();
            }
            if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
                currentProxy.setTimeInterval(Constants.TIME_INTERVAL);
                ProxyPool.proxyQueue.add(currentProxy);
            }
        }
    }

    /**
     * 子类实现page的处理
     * @param page
     */
    protected abstract void handle(Page page);

    /**
     * retry
     */
    protected abstract void retry();

}
