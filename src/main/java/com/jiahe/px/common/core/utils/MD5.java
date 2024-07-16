package com.jiahe.px.common.core.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5 {
    /**
     * MD5方法
     *
     * @param text 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text){
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MessageDigest不支持MD5Util", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();

    }

    /**
     * md5签名
     *
     * 按参数名称升序，将参数值进行连接 签名
     * @param appSecret
     * @param params
     * @return
     */
    public static String sign(String appSecret, String data) {
        StringBuilder paramValues = new StringBuilder();
        paramValues.append(data);
        paramValues.append(appSecret);
        log.info("待加密:" + paramValues.toString());
        log.info("生成sign:" + md5(paramValues.toString()));
        return md5(paramValues.toString());
    }

    /**
     * 请求参数签名验证
     *
     * @param appSecret
     * @param request
     * @return true 验证通过 false 验证失败
     * @throws Exception
     */
    public static boolean verifySign(String data,String appSecret, String sign) throws Exception {
        log.info("检验数据：" + data);
        if (!sign(appSecret, data).equals(sign)) {
            return false;
        }
        return true;
    }
}
