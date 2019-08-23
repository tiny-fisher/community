package com.nowcoder.community;

import com.nowcoder.community.controller.AlphaController;
import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) // 运行时，以CommunityApplication.class 为配置类
public class CommunityApplicationTests implements ApplicationContextAware {    /// 哪个类想实现这个Spring 容器, 就实现这个接口

    private ApplicationContext applicationContext;
    // 用来接收 set方法得到的 applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //  入参这个: ApplicationContext 就是继承了 Spring 容器; 它是一个接口....
		//
		// 惊了, 接口可以继承多个接口;
        this.applicationContext = applicationContext;
	}

	@Test   // test 啥??? 要干啥???  打印 applicationContext
	public void testApplicationContext(){
		System.out.println("applicationContext 对象:"+applicationContext);

		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());


		alphaDao =  applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao.select()) ;
	}

	@Test
	public void testBeanManagement(){
		AlphaService alphaService  = applicationContext.getBean(AlphaService.class);
		System.out.println("applicationContext对象获取到的 alphaService对象"+ alphaService);

		alphaService  = applicationContext.getBean(AlphaService.class);
		System.out.println("applicationContext对象获取到的 alphaService对象"+ alphaService);

	}


    @Test
	public void testBeanConfi(){
		SimpleDateFormat simpleDateFormate =
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormate.format(new Date()));

	}

    // hope Spring collection can 注入...? 那我就可以直接使用这个属性;
	@Autowired
	@Qualifier("alphaHibernate")// Spring 就会把名为这个alphaHibernate 的Bean 注入给它alphaDao ;
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat ;

	@Autowired
	private AlphaController alphaController;


	@Test
	public void testDI(){
		System.out.println("control "+alphaController);
		System.out.println("control "+alphaController.getData());
		System.out.println("a! "+alphaDao);
		System.out.println("b+ "+alphaService);
		System.out.println("c+ "+simpleDateFormat);
	}







}
