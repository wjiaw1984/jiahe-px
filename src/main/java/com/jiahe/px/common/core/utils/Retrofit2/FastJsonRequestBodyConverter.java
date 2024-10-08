package com.jiahe.px.common.core.utils.Retrofit2;

import com.alibaba.fastjson2.JSON;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.IOException;

public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}