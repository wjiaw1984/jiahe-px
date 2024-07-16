package com.jiahe.px.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by wzm on 2019/4/9.
 */
public class JsonUtils {
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认的处理时间
     *
     * @param jsonText
     * @return
     */
    public static String toJSON(Object jsonText) {
        return JSON.toJSONString(jsonText,
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 自定义时间格式
     *
     * @param jsonText
     * @return
     */
    public static String toJSON(String dateFormat, Object jsonText) {
        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));

        return JSON.toJSONString(jsonText, mapping);
    }
}
