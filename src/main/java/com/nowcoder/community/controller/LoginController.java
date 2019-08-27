package com.nowcoder.community.controller;


import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
// 这个类就不加  RequestMapping 了.   为什么? 因为在下面加了.

public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);// 以当前的类名命名 logger 对象

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    // 内部 或者  外部 链接到   /community/register , 就 return 模板里的html, 给客户端
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {

        return "/site/register";// 返回的是模板的路径...
    }






    // 这个是填表单的,如果   用户在对应的页面提交表单的话...
// 这里的model...不说了.. 因为不懂,
// 这里的user 怎么传进来的?

// 哪里调用了  LoginController.register???

// 参考 HomeController
    // 方法调用前, SpringMVC会自动实例化Model 和 Page, 并将Page 注入Model.
    // 所以, thymeleaf 可以直接访问Page object 中的数据;

//  所以把 user, page 等数据库的实体表, 写成类放在entity 里面...


    // 所以逻辑是这样: 用户 get 方法登录 /community/register
    // spring 搜索 @controller, @...@.. 这些能被扫描的bean , 有没有  @RequestMapping path= /register, method = get 的...
    //ok 找到一个  getRegisterPage() 加上了这个注解,,, 它return 了 一个地址,,, 然后去 template 里面去找...显示到前台...

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);

        // 得到的  map 如果有问题, 是会有key-value 的;

        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功, 请尽快激活");
            // 倒数8秒后, 跳到哪里去的设置
            model.addAttribute("target", "/index");

            // 所以说,,, 这里model  添加的属性, 都是为了 return 的那个页面服务的;
            return "/site/operate-result";
        } else { //  map
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            // send model to ... 返回注册页面
            return "/site/register";// 然后又要回到html 去接受后端发来的东西;
        }

    }

    // http://localhost:8080/community/activation/101/code,
    // 当用户点击邮箱里的链接时, 会以 get 方法 访问 这个网址呀.
    // 所以, 这个mapping 就是写的网址呀...

    // 这个return的   ,   就是,,, 如果用户访问了这个页面,,  服务端返回哪个页面...就好像有时候我明明点了一个页面, 结果给我返回的是
    // 另一个网址的页面;

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            //  成功的情况
            model.addAttribute("msg", "激活成功!");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            //  失败情况之 重复的情况;
            model.addAttribute("msg", "该账号已被激活过了.");
            model.addAttribute("target", "/index");
        } else {
            //
            model.addAttribute("msg", "激活失败, 激活码不正确.");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";

    }



    // 加一个 login页面 的 访问处理
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {

        return "/site/login";// 返回的是模板的路径...
    }

    //generate the verification code,  need to 处理一个再次访问图片的请求.
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // generate code
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //验证码存入 session
        session.setAttribute("kaptcha", text);

        // send image to the client browser;
        // 声明返回给浏览器的数据类型
// 整个response 是 MVC 维护的, 会自动关闭;
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            // want to output image to the browser , we have a tool to use:
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            // 捕获了异常, 我们也解决不了呀...
            // 所以只是写一个logger   开头 注入一下;
            logger.error("响应验证码失败: " + e.getMessage());
        }
    }




}
