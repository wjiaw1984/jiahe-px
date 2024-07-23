package com.jiahe.px.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Date: 2024/7/16
 */

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Bean
    public FastJsonConfig fastJsonConfig() {
        //1.自定义配置...

        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd");
        //2.1配置序列化的行为
        //JSONWriter.Feature.PrettyFormat:格式化输出
        config.setWriterFeatures(JSONWriter.Feature.PrettyFormat,
                JSONWriter.Feature.WriteMapNullValue,
                JSONWriter.Feature.WriteNullListAsEmpty);
        //2.2配置反序列化的行为
        config.setReaderFeatures(JSONReader.Feature.FieldBased,
                JSONReader.Feature.SupportArrayToBean,
                JSONReader.Feature.EmptyStringAsNull
        );
        return config;
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1.转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setFastJsonConfig(fastJsonConfig()); converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(0, converter);
    }
}
