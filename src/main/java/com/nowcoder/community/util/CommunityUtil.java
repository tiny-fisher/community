package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // 随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    // MD5 加密
    // hello -> abc123def456 ;
    // nounce/ salt;
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            // key == null or "" or " ",
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());

    }




}
