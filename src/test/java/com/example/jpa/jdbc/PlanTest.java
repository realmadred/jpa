package com.example.jpa.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.example.jpa.controller.SysUserController;
import com.example.jpa.entity.GoodsCategory;
import com.example.jpa.entity.QCostTypeParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

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
            if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class)) {
                object.put(name, RandomUtils.nextInt(100, 20000));
            } else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)
                    || type.isAssignableFrom(BigDecimal.class)) {
                object.put(name, RandomUtils.nextDouble(10.0, 10000.0));
            } else if (type.isAssignableFrom(String.class)) {
                object.put(name, RandomStringUtils.randomAlphabetic(6));
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
            if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class)) {
                if (name.startsWith("is")) {
                    str.append(".param(\"" + name + "\" , \"" + RandomUtils.nextInt(0, 2) + "\")\n");
                } else {
                    str.append(".param(\"" + name + "\" , \"" + RandomUtils.nextInt(100, 20000) + "\")\n");
                }
            } else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)
                    || type.isAssignableFrom(BigDecimal.class)) {
                str.append(".param(\"" + name + "\" , \"" + RandomUtils.nextDouble(10.0, 10000.0) + "\")\n");
            } else if (type.isAssignableFrom(String.class)) {
                str.append(".param(\"" + name + "\" , \"" + RandomStringUtils.randomAlphabetic(6) + "\")\n");
            } else if (type.isAssignableFrom(Date.class)) {
                str.append(".param(\"" + name + "\" , \"" + new Date() + "\")\n");
            }
        }
        System.out.println(str.toString());
    }

    @Test
    public void getFields() {
        final Class<QCostTypeParam> aClass = QCostTypeParam.class;
        Field[] declaredFields = aClass.getDeclaredFields();
        final String bean = StringUtils.uncapitalize(aClass.getSimpleName());
        final String beanStatic = StringUtils.uncapitalize(bean.substring(1));
        StringBuilder str = new StringBuilder();
        for (Field field : declaredFields) {
            String name = field.getName();
            if (Objects.equals("serialVersionUID", name) || Objects.equals(beanStatic, name)) {
                continue;
            }
            str.append(bean).append("." + name).append(",");
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

    private void sort(int[] arr){
        final int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testSort() {
        sort(new int[]{10,22,33,44,55,213,445,66,7,67,89,2,56,12,100,56});
    }

}
