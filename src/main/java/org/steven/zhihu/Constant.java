package org.steven.zhihu;

import org.springframework.context.ApplicationContext;
import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Table;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Constant {
    public static final int HTTP_STAUTS_OK = 200;
    public static final BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();
    public static final BlockingQueue<Activities> tableQueue = new LinkedBlockingQueue<Activities>();
    public static  ApplicationContext context;
}
