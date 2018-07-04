package org.steven.zhihu;


import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.steven.zhihu.model.Activities;
import org.steven.zhihu.model.Data;
import org.steven.zhihu.model.Paging;
import org.steven.zhihu.model.Table;
import org.steven.zhihu.service.Service;

import java.util.List;
import java.util.Queue;
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
        Constant.context = context;

        BlockingQueue<String> urlQueue = Constant.urlQueue;
        urlQueue.add("https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364" +
                "&desktop=True");
        String url;

        new Thread(new Runnable() {
            Service service = (Service) Constant.context.getBean("service");


            @Override
            public void run() {
                Activities activities;
                try {
                    while ((activities = Constant.tableQueue.take()) != null) {
                        long l = service.addPagin(activities.getPaging());

                        List<Data> datas = activities.getData();
                        for (Data data : datas) {
                            Table table = new Table(l, data);
                            service.addTable(table);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while ((url = urlQueue.take() )!= null) {
            System.out.println(url);
            if (url.equals("empty")) {
                break;
            }

            Thread thread = new Thread(new AbstractPageTask(url));

            thread.start();
        }



    }
}
