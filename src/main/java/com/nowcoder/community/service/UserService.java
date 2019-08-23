package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 要干嘛呀他?  通过一个UserId  返回一个 名字
// 不对呀,   这是一个类呀...   通过使用... UserMapper...
@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);

    }

}
