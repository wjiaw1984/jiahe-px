package com.jiahe.px.common.core.utils.osinfo;

import com.efuture.myshop.constant.PathConstant;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

/**
 * Created by wujiawen on 2019/11/12
 * 获取项目相关路径
 */
@Slf4j
public class GetPathUtil {
    public static void getProjectBasePath(){
        String os = GetOSInfo.getOSName();
        log.warn("本地系统是:" + os);
        if (os.startsWith("windows")) {
            // 本地是windows
            getProjectBasePathForWin();
        } else {
            // 本地是非windows系统 一般就是unix
            getProjectBasePathForLinux();
        }
    }

    public static void getProjectBasePathForWin(){
        String path = System.getProperty("user.dir");
        PathConstant.APP_BIN = Paths.get(path).toString();
        PathConstant.APP_Path = Paths.get(PathConstant.APP_BIN).getParent().toString();
        PathConstant.APP_CONF = Paths.get(PathConstant.APP_Path,"conf").toString();
        PathConstant.APP_LIB = Paths.get(PathConstant.APP_Path,"lib").toString();
    }

    public static void getProjectBasePathForLinux(){
        String path = System.getProperty("user.dir");
        PathConstant.APP_Path = Paths.get(path).toString();
        PathConstant.APP_BIN = Paths.get(PathConstant.APP_Path,"bin").toString();
        PathConstant.APP_CONF = Paths.get(PathConstant.APP_Path,"conf").toString();
        PathConstant.APP_LIB = Paths.get(PathConstant.APP_Path,"lib").toString();
    }
}
