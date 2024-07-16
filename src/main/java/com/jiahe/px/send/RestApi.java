package com.jiahe.px.send;

import lombok.extern.slf4j.Slf4j;

/**
 * @PackageName：com.jiahe.px.send
 * @ClassName：RestApi
 * @Description：
 * @date：2024/7/16 11:25
 */
@Slf4j
public class RestApi extends RestDataSource {
    private ISendHTTPTarget apiService;

    public ISendHTTPTarget getApiService(Class<ISendHTTPTarget> cls) {
        if (apiService == null) {
            Class<?>[] tempClass = cls.getInterfaces();
            if (tempClass != null) {
                apiService = getmRetrofit().build().create(cls);
            }
        }
        return apiService;
    }

    public ISendHTTPTarget getApiService() {
        return apiService;
    }

    public RestApi(String url, Class cls) {
        connectInit(url);
        getApiService(cls);
    }
}
