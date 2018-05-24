package com.example.jpa.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.example.jpa.controller.SysUserController;
import com.example.jpa.entity.GoodsCategory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanTest {

    @Test
    public void getJson() {
        Field[] declaredFields = GoodsCategory.class.getDeclaredFields();
        JSONObject object = new JSONObject();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            String name = field.getName();
            if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class)){
                object.put(name,RandomUtils.nextInt(100,20000));
            }else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)
                    || type.isAssignableFrom(BigDecimal.class)){
                object.put(name,RandomUtils.nextDouble(10.0,10000.0));
            }else if (type.isAssignableFrom(String.class)){
                object.put(name,RandomStringUtils.randomAlphabetic(6));
            }
        }
        System.out.println(object.toJSONString());
    }

    @Test
    public void getParams() {
        Field[] declaredFields = GoodsCategory.class.getDeclaredFields();
        StringBuilder str = new StringBuilder();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            String name = field.getName();
            if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class)){
                if (name.startsWith("is")){
                    str.append(".param(\""+name+"\" , \""+RandomUtils.nextInt(0,2)+"\")\n");
                }else {
                    str.append(".param(\""+name+"\" , \""+RandomUtils.nextInt(100,20000)+"\")\n");
                }
            }else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)
                    || type.isAssignableFrom(BigDecimal.class)){
                str.append(".param(\""+name+"\" , \""+RandomUtils.nextDouble(10.0,10000.0)+"\")\n");
            }else if (type.isAssignableFrom(String.class)){
                str.append(".param(\""+name+"\" , \""+RandomStringUtils.randomAlphabetic(6)+"\")\n");
            }else if (type.isAssignableFrom(Date.class)){
                str.append(".param(\""+name+"\" , \""+new Date()+"\")\n");
            }
        }
        System.out.println(str.toString());
    }

    @Test
    public void simple() throws Exception {
        MockMvcBuilders.standaloneSetup(new SysUserController()).build()
                .perform(get("/sysUser/findById").param("id", "4")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":4,\"name\":\"OsvfUP\",\"age\":23,\"password\":\"LCwleq\"}"))
                .andReturn();
    }

}
