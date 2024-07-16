package com.jiahe.px.common.core.utils.Retrofit2;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class RestDataSource {
    private Retrofit.Builder mRetrofit;

    public Retrofit.Builder getmRetrofit() {
        return mRetrofit;
    }

    public void setmRetrofit(Retrofit.Builder mRetrofit) {
        this.mRetrofit = mRetrofit;
    }

    public void connectInit(String url){
        if (url != null && "".equals(url)){
            throw new RuntimeException("URL不能为空！");
        }
        try{
            mRetrofit =
                    new Retrofit.Builder().baseUrl(url)
                            //注释原来的转换器
                            //.addConverterFactory(JacksonConverterFactory.create())
                            //在此处声明使用FastJsonConverter做为转换器
                            .addConverterFactory(FastJsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(okHttpClient());
        } catch ( Exception e){
            throw new RuntimeException("初始化远程站点发生错误：" + e.getMessage());
        }

    }


    private OkHttpClient okHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder =
                new OkHttpClient.Builder().addInterceptor(logging)//发布时要将这日记关掉，不然报内存溢出。
                        .readTimeout(30, TimeUnit.MINUTES);
        //        builder.sslSocketFactory(createBadSslSocketFactory());
        //        builder.sslSocketFactory(badSslSocketFactory(),new X509TrustManager() {
        //            @Override
        //            public void checkClientTrusted(X509Certificate[] chain, String authType)
        //                    throws CertificateException {
        //            }
        //            @Override
        //            public void checkServerTrusted(X509Certificate[] chain, String authType)
        //                    throws CertificateException {
        //            }
        //            @Override
        //            public X509Certificate[] getAcceptedIssuers() {
        //                return null;
        //            }
        //        });
        return builder.build();
    }

    private static SSLSocketFactory badSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            return SSLContext.getInstance("TLS").getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static SSLSocketFactory createBadSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            context.init(null, new TrustManager[] {permissive}, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

}
