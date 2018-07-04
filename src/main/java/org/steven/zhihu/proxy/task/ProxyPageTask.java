package org.steven.zhihu.proxy.task;


import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.httpclient.ProxyHttpClient;
import org.steven.zhihu.model.Direct;
import org.steven.zhihu.model.Page;
import org.steven.zhihu.model.Proxy;
import org.steven.zhihu.proxy.ProxyListPageParser;
import org.steven.zhihu.proxy.ProxyPool;
import org.steven.zhihu.proxy.site.ProxyListPageParserFactory;
import org.steven.zhihu.util.Config;
import org.steven.zhihu.util.Constants;
import org.steven.zhihu.util.HttpClientUtil;

import java.io.IOException;
import java.util.List;

import static org.steven.zhihu.proxy.ProxyPool.proxyQueue;


/**
 * 下载代理网页并解析
 * 若下载失败，通过代理去下载代理网页
 */
public class ProxyPageTask implements Runnable{
	private static Logger logger = LoggerFactory.getLogger(ProxyPageTask.class);
	protected String url;
	private boolean proxyFlag;//是否通过代理下载
	private Proxy currentProxy;//当前线程使用的代理

	protected static ProxyHttpClient proxyHttpClient = ProxyHttpClient.getInstance();
	private ProxyPageTask(){

	}
	public ProxyPageTask(String url, boolean proxyFlag){
		this.url = url;
		this.proxyFlag = proxyFlag;
	}
	public void run(){
		long requestStartTime = System.currentTimeMillis();
		HttpGet tempRequest = null;
		try {
			Page page = null;
			if (proxyFlag){
				tempRequest = new HttpGet(url);
				currentProxy = proxyQueue.take();
				if(!(currentProxy instanceof Direct)){
					HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
					tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
				}
				page = proxyHttpClient.getWebPage(tempRequest);
			}else {
				page = proxyHttpClient.getWebPage(url);
			}
			page.setProxy(currentProxy);
			int status = page.getStatusCode();
			long requestEndTime = System.currentTimeMillis();
			String logStr = Thread.currentThread().getName() + " " + getProxyStr(currentProxy) +
					"  executing request " + page.getUrl()  + " response statusCode:" + status +
					"  request cost time:" + (requestEndTime - requestStartTime) + "ms";
			if(status == HttpStatus.SC_OK){
				logger.debug(logStr);
				handle(page);
			} else {
				logger.error(logStr);
				Thread.sleep(100);
				retry();
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		} catch (IOException e) {
			retry();
		} finally {
			if(currentProxy != null){
				currentProxy.setTimeInterval(Constants.TIME_INTERVAL);
				proxyQueue.add(currentProxy);
			}
			if (tempRequest != null){
				tempRequest.releaseConnection();
			}
		}
	}

	/**
	 * retry
	 */
	public void retry(){
		proxyHttpClient.getProxyDownloadThreadExecutor().execute(new ProxyPageTask(url, Config.isProxy));
	}

	public void handle(Page page){
		if (page.getHtml() == null || page.getHtml().equals("")){
			return;
		}

		ProxyListPageParser parser = ProxyListPageParserFactory.
				getProxyListPageParser(ProxyPool.proxyMap.get(url));
		List<Proxy> proxyList = parser.parse(page.getHtml());
		for(Proxy p : proxyList){
			ProxyPool.lock.readLock().lock();
			boolean containFlag = ProxyPool.proxySet.contains(p);
			ProxyPool.lock.readLock().unlock();
			if (!containFlag){
				ProxyPool.lock.writeLock().lock();
				ProxyPool.proxySet.add(p);
				ProxyPool.lock.writeLock().unlock();

				proxyHttpClient.getProxyTestThreadExecutor().execute(new ProxyTestTask(p));
			}
		}
	}

	private String getProxyStr(Proxy proxy){
		if (proxy == null){
			return "";
		}
		return proxy.getIp() + ":" + proxy.getPort();
	}
}
