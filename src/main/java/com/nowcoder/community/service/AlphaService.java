package com.nowcoder.community.service;


import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("singleton")//作用范围
//@Scope("prototype")// 每次访问bean  会创造一个新的实例;
// 因为 CommunityApplicationTests.java 中的 testBeanManagement getBean 两次? 所以打印了两次???;

public class AlphaService {


    @Autowired
    private AlphaDao alpahDao; // 这样就注入了;;;???

    public AlphaService(){
        System.out.println("构造器 实例化AlphaService");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    // 销毁方法
    public void destroy(){
        System.out.println("销毁Alpha");

    }


    // 下面写一个方法, 模拟实现查询的业务;
    public String find(){
        return alpahDao.select();
    }

}
