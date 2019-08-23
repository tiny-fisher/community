package com.nowcoder.community.dao;


import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 提供7个方法,,,所以user-mapper.xml  写了7个,, 累死了.

    User selectById (int id);

    User selectByName (String username);

    User selectByEmail (String email);

    int insertUser (User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);




}
