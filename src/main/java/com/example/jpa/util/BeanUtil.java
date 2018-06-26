package com.example.jpa.util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * bean 转换工具类
 *
 * @Author: 刘峰
 * @Date: 2018/6/1 17:10
 */
public class BeanUtil {

    private static final ConcurrentMap<Class,List<Field>> FIELDS_CACHE = new ConcurrentReferenceHashMap<>(16);


    /**
     * 实体类转换为vo
     *
     * @param source           数据源
     * @param targetClazz      目标对象class
     * @param ignoreProperties 忽略字段
     * @return 实体类转换为vo
     * @Author: 刘峰
     * @Date: 2018/6/1 17:10
     */
    public static <T> T toBean(Object source, Class<T> targetClazz, String... ignoreProperties) {
        try {
            if (source == null) {
                return null;
            }
            final T target = targetClazz.newInstance();
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanCreationException("BeanUtil 转换异常");
        }
    }

    /**
     * 实体类转换为list vo
     *
     * @param sources          数据源
     * @param targetClazz      目标对象class
     * @param ignoreProperties 忽略字段
     * @return 实体类转换为vo
     * @Author: 刘峰
     * @Date: 2018/6/1 17:10
     */
    public static <T> List<T> toListBean(List<?> sources, final Class<T> targetClazz, String... ignoreProperties) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }
        List<T> targets = new ArrayList<>(sources.size());
        sources.forEach(overdueRule -> targets.add(toBean(overdueRule, targetClazz, ignoreProperties)));
        return targets;
    }

    /**
     * 获取本地服务器名称
     *
     * @return
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取map中的字符串
     *
     * @return 获取map中的字符串
     * @Author: 刘峰
     * @Date: 2018/6/20 14:40
     */
    public static String getString(Map<String, Object> map, String key) {
        return map == null ? null : Objects.toString(map.get(key),null);
    }

    /**
     * 获取类的字段信息
     *
     * @param clazz 类
     * @return 获取类的字段信息
     * @Author: 刘峰
     * @Date: 2018/6/23 15:14
     */
    public static List<Field> getFields(Class clazz){
        List<Field> fields = FIELDS_CACHE.get(clazz);
        if (fields == null){
            fields = FieldUtils.getAllFieldsList(clazz);
            FIELDS_CACHE.put(clazz,fields);
        }
        return fields;
    }
}
