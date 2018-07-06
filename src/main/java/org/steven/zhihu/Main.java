package org.steven.zhihu;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.steven.zhihu.httpclient.ProxyHttpClient;
import org.steven.zhihu.httpclient.ZhiHuHttpClient;
import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Data;

import org.steven.zhihu.model.Table;
import org.steven.zhihu.proxy.ProxyPool;
import org.steven.zhihu.service.Service;
import org.steven.zhihu.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        String baseDir = "./conf/";

//        String log4jPath = baseDir + "log4j2.xml";
//
//        if(new File(log4jPath).exists()){
//
//            System.out.println("file exist.");
//        }else {
//            System.out.println("file is not exist");
//        }
//
//        System.setProperty(XmlConfigurationFactory.CONFIGURATION_FILE_PROPERTY, log4jPath);

        //加载spring
        String[] springFiles = new String[]{baseDir + "applicationContext.xml"};
        ApplicationContext context = new FileSystemXmlApplicationContext(springFiles);
        Constants.context = context;


        new Thread(() -> {

            while (true) {
                System.out.println("proxyQueue size:"+ProxyPool.proxyQueue.size());
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(new Runnable() {
            Service service = (Service) Constants.context.getBean("service");


            @Override
            public void run() {



                Activities activities;
                try {
                    while ((activities = Constants.tableQueue.take()) != null) {

                        long l = service.addPagin(activities.getPaging());

                        List<Data> datas = activities.getData();
                        long time = activities.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        String format = sdf.format(new Date(time * 1000L));
                        for (Data data : datas) {
                            Table table = new Table(l, data, format);
                            service.addTable(table);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ProxyHttpClient.getInstance().startCrawl();
        ZhiHuHttpClient.getInstance().startCrawl();






    }
}
