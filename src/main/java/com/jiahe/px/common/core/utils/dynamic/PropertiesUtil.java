package com.jiahe.px.common.core.utils.dynamic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static void main(String[] args) throws Exception {
        String urlDynamic = prop.getProperty("dataSource.urlDynamic");
        urlDynamic = urlDynamic.replace("!", "192.168.26.155:5000");
        urlDynamic = urlDynamic.replace("#", "jkl99");
        urlDynamic = urlDynamic.replace("$", "jingyuan");
        System.out.println(urlDynamic);
    }

    private static Properties prop = null;

    static {
        prop = new Properties();
        InputStream input = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("config.properties");
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return prop;
    }

    public static int isPDInputForDealid() {
        try{
            return Integer.valueOf(prop.getProperty("isPDInputForDealid"));
        }catch (Exception e){
            return 0;
        }
    }

    public static int isAddLostReason() {
        try{
            return Integer.valueOf(prop.getProperty("isAddLostReason"));
        }catch (Exception e){
            return 0;
        }
    }

    public static int isPDInputForTeam() {
        try{
            return Integer.valueOf(prop.getProperty("isPDInputForTeam"));
        }catch (Exception e){
            return 0;
        }
    }

    public static int isCursor() {
        return Integer.valueOf(prop.getProperty("isCursor"));
    }

    public static int isFocus() {
        try {
            return Integer.valueOf(prop.getProperty("isFocus"));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int characterSet() {
        return Integer.valueOf(prop.getProperty("characterSet"));
    }

    public static String dynamicURL(String ip, String dataName,
                                    String serverName, String DataBaseType) {
        String urlDynamic = "";
        if ("oracle".equals(DataBaseType)) {
            urlDynamic = "jdbc:oracle:thin:@!:#";
        }else if ("mssql".equals(DataBaseType) || "sqlserver".equals(DataBaseType)){
            urlDynamic = "jdbc:jtds:sqlserver://!;DatabaseName=#";
        }
        urlDynamic = urlDynamic.replace("!", ip);
        urlDynamic = urlDynamic.replace("#", dataName);
            urlDynamic = urlDynamic.replace("$", serverName);
        return urlDynamic;
    }

}
