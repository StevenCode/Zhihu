package org.steven.zhihu;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.steven.zhihu.util.Constants.HTTP_STAUTS_OK;

public class HttpUtil {
    public void request(String url, IHttpCallback iHttpCallback) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HTTP_STAUTS_OK) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                iHttpCallback.success(content);
            }else {
                System.out.println("statusCode:" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
