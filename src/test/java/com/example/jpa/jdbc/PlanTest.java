package com.example.jpa.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.example.jpa.entity.SysUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.lang.reflect.Field;

public class PlanTest {

    @Test
    public void getJson() {
        Field[] declaredFields = SysUser.class.getDeclaredFields();
        JSONObject object = new JSONObject();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            String name = field.getName();
            if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class)){
                object.put(name,RandomUtils.nextInt(100,20000));
            }else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)){
                object.put(name,RandomUtils.nextDouble(10.0,10000.0));
            }else if (type.isAssignableFrom(String.class)){
                object.put(name,RandomStringUtils.randomAlphabetic(6));
            }
        }
        System.out.println(object.toJSONString());
    }

}
