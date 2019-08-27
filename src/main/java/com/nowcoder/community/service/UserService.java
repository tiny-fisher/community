package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// 要干嘛呀他?  通过一个UserId  返回一个 名字
// 不对呀,   这是一个类呀...   通过使用... UserMapper...
@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    // 激活码  既要有域名 还要有项目名;

    // 因为是固定值, 不是bean  用 Value  就是引用 application.properties 里设置的变量;
    // 之前 @Value("${spring.mail.username}") 就是 注释了 主类的成员变量 from ,   发邮件功能的时候, from 直接就用在其他地方了.


    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
        return userMapper.selectById(id);

    }

    // 要写什么? 判断 填表是怎么样的.

// 为什么这里返回的是一个 Map??? 谁收到了?  那个, 先传给 LoginController, 然后再给  template/site/register 收到了呀...
// 这个 user 怎么传进来的?
    public Map<String, Object> register(User user) {

        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空! 我的理解是, 客户端传过来的是一个空对象? 这是什么原因呢?");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map; // ? 难道返回 json
        }
        if (StringUtils.isBlank(user.getPassword()  )) {
            map.put("passwordMsg", "密码不能为空");
            return map; // ? 难道返回 json
        }
        if (StringUtils.isBlank(user.getEmail()  )) {
            map.put("emailMsg", "邮箱不能为空");
            return map; // ? 难道返回 json
        }

        // 验证账号
        // 从数据库返回的对象   u

        User u = userMapper.selectByName(user.getUsername());
        // 如果数据库里不存在, 就是null,   如果存在就不是null;
        if (u != null) {
            // 我需要做什么?  我需要, 先把数据存进去, 然后发一封邮件.  不对呀, ...
                                                                             //时间根本不够用...(只剩  1/3天了...)    // 是的, t1 到不了要求的高度, 那就延长到 t2吧, //虞越希望理论派上用场了.
            // 不是发邮件  这里是验证账号的流程... 还没到添加数据的流程 只有不满足条件的才中间就打断
            // 存在
            map.put("usernameMsg", "该账号已被注册");
            return map; // ? 难道返回 json
        }
        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已注册过账号");
            return map; // ? 难道返回 json
        }

        //注册用户; 之后发邮件;
        //生成随机
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        // 这里 salt 要存吧, 不然下次登录怎么验证? , 如果下次生成新的, 这次存了有什么意义?
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        // 随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000) ));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 模板是?  要什么参数?
        // 把静态的 activation.html  改造成模板.

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // 设置需要处理的具体页面
        // http://localhost:8080/community/activation/101/code,
        String url  =domain + contextPath + "/activation/" + user.getId()+"/"+ user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        // 好了  service 搞定了  之后去Controller 处理前后端交互的逻辑.



        return map;
    }


    // 上面写了 register 的 public method
    // 下面写 激活注册的方法   好了, 我现在能不能自己写出来?

    // 是通过 userId 得到的user 对象,,,

    // 也就是说,   数据库已经更新这个对象了...

    // code是激活码, userId 是要查询的对象, 都要传进来...

    public int activation(int userId, String code) {
        // first query the id, then check if the activation is right;
        User user = userMapper.selectById(userId);
        // 这个传入的 user 的 private ActivationCode

        // then discuss in several situations:

        if (user.getStatus() == 1) {
            //  如果已经激活过了.
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            //  如果没被激活, 而且还一样,  那就在数据库里把它的激活状态改成激活呗
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            // 如果没被激活, 但是不一样,   那就failure了呀...
            return ACTIVATION_FAILURE;
        }

    }








}
