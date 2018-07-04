package org.steven.zhihu.util;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.UnknownHostException;
import java.util.Random;

public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static CloseableHttpClient httpClient;
    private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
    private static HttpHost proxy;
    private static RequestConfig requestConfig;

    static{
        init();
    }

    private static void init() {

        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > 2) {
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    return true;
                }
                if (exception instanceof ConnectTimeoutException) {
                    return true;
                }
                if (exception instanceof UnknownHostException) {
                    return true;
                }
                if (exception instanceof SSLException) {
                    return true;
                }
                HttpRequest request = HttpClientContext.adapt(context).getRequest();
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        HttpClientBuilder httpClientBuilder =
                HttpClients.custom().setRetryHandler(retryHandler).setUserAgent(userAgent);

        httpClient = httpClientBuilder.build();


        requestConfig = RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                setConnectTimeout(Constants.TIMEOUT).
                setConnectionRequestTimeout(Constants.TIMEOUT).build();
    }

    public static String getWebPage(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return getWebPage(request, "utf-8");
    }

    public static String getWebPage(HttpRequestBase request) throws IOException {
        return getWebPage(request, "utf-8");
    }

    /**
     * @param encoding 字符编码
     * @return 网页内容
     */
    public static String getWebPage(HttpRequestBase request
            , String encoding) throws IOException {
        CloseableHttpResponse response = null;
        response = getResponse(request);
        logger.info("status---" + response.getStatusLine().getStatusCode());
        String content = EntityUtils.toString(response.getEntity(),encoding);
        logger.info("content---" + content);
        request.releaseConnection();
        return content;
    }

    public static CloseableHttpResponse getResponse(HttpRequestBase request) throws IOException {
        if (request.getConfig() == null){
            request.setConfig(requestConfig);
        }
        request.setHeader("User-Agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)]);
        HttpClientContext httpClientContext = HttpClientContext.create();
        CloseableHttpResponse response = httpClient.execute(request, httpClientContext);
//		int statusCode = response.getStatusLine().getStatusCode();
//		if(statusCode != 200){
//			throw new IOException("status code is:" + statusCode);
//		}
        return response;
    }

    public static CloseableHttpResponse getResponse(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return getResponse(request);
    }

    /**
     * 序列化对象
     * @param object
     * @throws Exception
     */
    public static void serializeObject(Object object,String filePath){
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            logger.info("序列化成功");
            oos.flush();
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 反序列化对象
     * @param path
     * @throws Exception
     */
    public static Object deserializeObject(String path) throws Exception {
//		InputStream fis = HttpClientUtil.class.getResourceAsStream(name);
        File file = new File(path);
        InputStream fis = new FileInputStream(file);
        ObjectInputStream ois = null;
        Object object = null;
        ois = new ObjectInputStream(fis);
        object = ois.readObject();
        fis.close();
        ois.close();
        return object;
    }

    public static org.apache.http.client.config.RequestConfig.Builder getRequestConfigBuilder() {
        return RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                setConnectTimeout(Constants.TIMEOUT).
                setConnectionRequestTimeout(Constants.TIMEOUT).
                setCookieSpec(CookieSpecs.STANDARD);
    }
}
