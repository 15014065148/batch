package com.eveb.saasops.batch.sys.util;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.sf.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class JsonUtil {
    /**
     * 对象转换成json字符串
     *
     * @param @param  obj
     * @param @return
     * @return String
     * @throws
     * @Description: TODO
     * @author william
     * @date 2017年12月8日
     */
    public String toJson(Object obj) {
        String result = "";
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            result = gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * json字符串转成对象
     *
     * @param @param  str
     * @param @param  type
     * @param @return
     * @return T
     * @throws
     * @Description: TODO
     * @author william
     * @date 2017年12月8日
     */
    public <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        try {
            T t = gson.fromJson(str, type);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转成对象
     *
     * @param @param  str
     * @param @param  type
     * @param @return
     * @return T
     * @throws
     * @Description: TODO
     * @author william
     * @date 2017年12月8日
     */
    public <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        try {
            T t = gson.fromJson(str, type);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param str Json字符串
     * @return Map
     * @创建人: william
     * @Method描述: 字符串返回成Map
     * @创建时间: 2017年12月8日
     */
    public Map<String, Object> toMap(String str) {
        Gson gson = new Gson();
        Map<String, Object> gsonMap = gson.fromJson(str, new TypeToken<Map<String, Object>>() {
        }.getType());
        return gsonMap;
    }

    /**
     * Json字符串转JsonArray对象
     *
     * @param @param  strJson
     * @param @return
     * @return JsonObject
     * @throws
     * @Description: TODO
     * @author william
     * @date 2017年12月8日
     */
    public JsonArray String2JsonArray(String strJson) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(strJson).getAsJsonArray();
    }

    /**
     * Json字符串转JsonObject对象
     *
     * @param @param  strJson
     * @param @return
     * @return JsonObject
     * @throws
     * @Description: TODO
     * @author william
     * @date 2017年12月8日
     */
    public JsonObject String2JsonObject(String strJson) {
        JsonParser jsonParser = new JsonParser();
        strJson = strJson.replace("[", "");
        strJson = strJson.replace("]", "");
        return jsonParser.parse(strJson).getAsJsonObject();
    }

    private <T> Map<String, Object> EntToMap(Object model, Class<T> t, Map<String, Object> map) {
        try {
            Field[] fields = t.getDeclaredFields();
            if (fields.length > 0 && map == null)
                map = new HashMap<String, Object>();
            for (Field f : fields) {
                String name = f.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
                Method m = model.getClass().getMethod("get" + name);
                String value = String.valueOf(m.invoke(model));
                if (map != null && value != null)
                    map.put(f.getName(), value);
                else
                    map.put(f.getName(), "");
            }
            if (t.getSuperclass() != null) {
                EntToMap(model, t.getSuperclass(), map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 实体类转成Map对象
     *
     * @param model
     * @return
     */
    public <T> Map<String, Object> Entity2Map(Object model) {
        Map<String, Object> map = null;
        try {
            map = EntToMap(model, model.getClass(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    private <T> JsonObject EntToObj(Object model, Class<T> t, JsonObject jsonObj) {
        try {
            Field[] fields = t.getDeclaredFields();
            if (fields.length > 0 && jsonObj == null)
                jsonObj = new JsonObject();
            for (Field f : fields) {
                String name = f.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
                Method m = model.getClass().getMethod("get" + name);
                String value = String.valueOf(m.invoke(model));
                if (jsonObj != null && value != null)
                    jsonObj.addProperty(f.getName(), value);
                else
                    jsonObj.addProperty(f.getName(), "");
            }
            if (t.getSuperclass() != null) {
                EntToObj(model, t.getSuperclass(), jsonObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    /**
     * 实体转为Json对象
     *
     * @param model
     * @return
     */
    public <T> JsonObject Entity2JsonObject(Object model) {
        JsonObject jsonObject = null;
        try {
            jsonObject = EntToObj(model, model.getClass(), jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        return res;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }
    /**
     * 将map装换为javabean对象
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    public static <T extends Object>  T transferMap2Bean(Map<String,Object> map,Class<T> clazz) throws Exception{
        T instance = clazz.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : descriptors) {
            String key=property.getName();
            if(map.containsKey(key)){
                Object value = map.get(key);
                Method setter = property.getWriteMethod();
                setter.invoke(instance, value);
            }

        }

        return instance;
    }

}
