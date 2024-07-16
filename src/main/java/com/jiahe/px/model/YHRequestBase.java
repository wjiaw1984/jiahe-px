package com.jiahe.px.model;

import com.alibaba.fastjson2.JSONObject;
import com.jiahe.px.common.core.utils.MD5;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 永辉批销请求基类
 * Date: 2024/7/16
 */

@Data
@Slf4j
public class YHRequestBase implements Serializable {
    private String appId;
    private String version = "1.0";
    private String signType = "MD5";
    private String sign;
    private Long reqTime;
    private String customNo;

    public JSONObject toJSON(){
        JSONObject result = new JSONObject();
        result.put("appId", this.appId);
        result.put("version", this.version);
        result.put("signType", this.signType);
        result.put("sign", this.sign);
        result.put("reqTime", this.reqTime);
        result.put("customNo", this.customNo);
        return result;
    }

    public void buildSign(String appSecret){
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : toJSON().entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            if (null != entry.getValue() && !"".equals(entry.getValue())) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        sb.append("key=").append(appSecret);
        log.info("验签参数：{}", sb);
        this.setSign(Objects.requireNonNull(MD5.md5(sb.toString(), "UTF-8")).toUpperCase());
    }
}
