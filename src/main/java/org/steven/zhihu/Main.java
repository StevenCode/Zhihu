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


public class Main {
    public static void main(String[] args) {

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

        Service service = (Service) context.getBean("service");


        String url = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530072364" +
                "&desktop=True";
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.request(url, content -> {


            try {
                Activities activities = JSONObject.parseObject(content, Activities.class);
                Paging paging = activities.getPaging();

                long l = service.addPagin(paging);

                List<Data> datas = activities.getData();
                for (Data data : datas) {
                    Table table = new Table(l, data);
                    service.addTable(table);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });





    }
}
