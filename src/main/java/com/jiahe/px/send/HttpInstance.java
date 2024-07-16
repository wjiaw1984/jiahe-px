package com.jiahe.px.send;

import com.jiahe.px.common.core.utils.ExceptionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import retrofit2.Call;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：HttpInstance
 * @Description：
 * @date：2024/7/16 11:23
 */
@Data
@Slf4j
public class HttpInstance {
    private static Map<String, RestApi> RestMap;

    public static RestApi getInstance(String key) {
        if (RestMap == null) {
            RestMap = new HashMap<>();
        }
        if (!RestMap.containsKey(key)) {
            addPosRest(key, ISendHTTPTarget.class);
        }
        return RestMap.get(key);
    }

    public static synchronized void addPosRest(String key, Class cls){
        try {
            RestApi posRest = new RestApi(key,cls);
            RestMap.put(key, posRest);
        }catch (Exception ex){
            log.error("创建远程调用【" + cls.getName() + "】实例失败:" + ex.getMessage());
            ExceptionUtils.raise("创建远程调用【" + cls.getName() + "】实例失败!");
        }
    }

    /**
     * 反射调用方法
     * @param newObj 实例化的一个对象
     * @param methodName 对象的方法名
     * @param args 参数数组
     * @return 返回值
     * @throws Exception
     */
    public static Object invokeMethod(Object newObj, String methodName, Object[] args)throws Exception {
        Assert.notNull(newObj,"反射实例为空！");
        Class ownerClass = newObj.getClass();
        Class[] argsClass = new Class
                [args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(newObj, args);
    }

    public static Call<?> post(String url, String getFuncName, Object[] paramArray){
        Call<?> typeCall = null;
        try {
            typeCall = (Call<?>) invokeMethod(getInstance(url).getApiService(), getFuncName, paramArray);
        }catch (NoSuchMethodException e){
            log.error("TAG{}", "NoSuchMethodException e = " + e.getMessage());
            throw new RuntimeException("NoSuchMethodException e = " + e.getMessage());
        }catch (IllegalAccessException e){
            log.error("TAG{}", "IllegalAccessException e = " + e.getMessage());
            throw new RuntimeException("IllegalAccessException e = " + e.getMessage());
        }catch (InvocationTargetException e){
            log.error("TAG{}", "InvocationTargetException e = " + e.getMessage());
            throw new RuntimeException("InvocationTargetException e = " + e.getMessage());
        }catch (Exception e){
            log.error("TAG", "Exception e = " + e.getMessage());
            throw new RuntimeException("Exception e = " + e.getMessage());
        }
        return typeCall;
    }
}
