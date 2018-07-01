package org.steven.zhihu;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.steven.zhihu.model.Paging;
import org.steven.zhihu.service.Service;


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
        Paging paging = new Paging();
        paging.setIs_end(false);
        paging.setNext("next");
        paging.setPrevious("previous");
        service.addPagin(paging);

    }
}
