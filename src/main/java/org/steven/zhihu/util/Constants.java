package org.steven.zhihu.util;

import org.springframework.context.ApplicationContext;
import org.steven.zhihu.model.Activities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Constants {
    public static final int HTTP_STAUTS_OK = 200;
    public final static int TIMEOUT = 10000;

    public final static String[] userAgentArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };

    public final static long TIME_INTERVAL = 2000;
    public final static String INDEX_URL = "https://www.zhihu.com";
    public final static String USER_FOLLOWEES_URL = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364&desktop=True";
    public final static String DETAIL_URL = "https://www.zhihu" +
            ".com/api/v4/members/excited-vczh/activities?limit=7&after_id=%s&desktop=True";

    public static final BlockingQueue<Activities> tableQueue = new LinkedBlockingQueue<Activities>();
    public static ApplicationContext context;
}
