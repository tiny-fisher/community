package com.nowcoder.community.dao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Annotation;

@Repository
@Primary
public class AlphaDaoMyBatisImpl implements AlphaDao {



    @Override
    public String select(){
        return "MyBatis";

    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
