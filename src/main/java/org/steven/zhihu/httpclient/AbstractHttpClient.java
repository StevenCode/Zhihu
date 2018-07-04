package org.steven.zhihu.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steven.zhihu.model.Page;
import org.steven.zhihu.util.HttpClientUtil;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractHttpClient {
    private Logger logger = LoggerFactory.getLogger(AbstractHttpClient.class);

    public InputStream getWepPageInputStream(String url) {
        try{
            CloseableHttpResponse response = HttpClientUtil.getResponse(url);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Page getWebPage(String url) throws IOException {
        return getWebPage(url, "UTF-8");
    }
    public Page getWebPage(String url, String charset) throws IOException {
        Page page = new Page();
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse(url);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(url);
        try {
            if(page.getStatusCode() == 200){
                page.setHtml(EntityUtils.toString(response.getEntity(), charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return page;
    }
    public Page getWebPage(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse(request);
        Page page = new Page();
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setHtml(EntityUtils.toString(response.getEntity()));
        page.setUrl(request.getURI().toString());
        return page;
    }
}
