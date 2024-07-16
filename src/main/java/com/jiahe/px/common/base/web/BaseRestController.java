package com.jiahe.px.common.base.web;

import com.alibaba.fastjson2.JSONObject;
import com.jiahe.px.common.base.BaseException;
import com.jiahe.px.common.context.SessionContext;
import com.jiahe.px.common.context.SessionContextHolder;
import com.jiahe.px.common.core.reflect.ComponentInvoker;
import com.jiahe.px.common.core.utils.SpringContextHolder;
import com.jiahe.px.common.session.SessionContextBuilder;
import com.jiahe.px.common.web.annotation.Version;
import com.jiahe.px.common.web.aop.support.ExposerContextHolder;
import com.jiahe.px.common.web.serializer.ResponseSerializer;
import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.common.web.support.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class BaseRestController {
    @Autowired
    protected ComponentInvoker componentInvoker;

    @Autowired
    protected SessionContextBuilder sessionContextBuilder;

    @Autowired
    protected ResponseSerializer responseSerializer;

    @Value("${spring.application.name:app}")
    protected String applicationName;

    @Value("${spring.application.message:hi}")
    protected String applicationMessage;

    @RequestMapping("/")
    @ResponseBody
    public String onRoot(){
        return applicationName + "<br>" + applicationMessage;
    }

    @RequestMapping("/version")
    @ResponseBody
    public String onVersion(){
        Map map = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(Version.class);
        if(CollectionUtils.isEmpty(map)){
            return "undefine version";
        }

        Version version = map.values().iterator().next().getClass().getAnnotation(Version.class);
        if(StringUtils.hasText(version.value())){
            return version.value();
        }

        return "undefine version";
    }


    //@RequestMapping(value = "/rest",  method = RequestMethod.POST)
    @ResponseBody
    public String onRest(@RequestParam(value="method",required=false) String method,
                         @RequestParam(value="session",required=false) String session,
                         @RequestParam(value="ent_id",required=false) String ent_id,
                         @RequestParam(value="user_id",required=false) String user_id,
                         @RequestParam(value="user_code",required=false) String user_code,
                         @RequestParam(value="user_name",required=false) String user_name,
                         @RequestParam(value="locale",required=false) String locale,
                         @RequestParam(value="token",required=false) String token,
                         @RequestBody String param) {
        BaseResponse baseResponse = null;

        try {
            setSession(ent_id, user_id, user_code, user_name, locale, session, token);

            JSONObject jsonObject = JSONObject.parseObject(param);

            Object result = componentInvoker.invoke(method, jsonObject);

            baseResponse = BaseResponse.buildSuccess(result);
        } catch (BaseException e){
            log.error(e.getMessage(), e);

            String code = StringUtils.hasText(e.getCode())?e.getCode(): ResponseCode.FAILURE;
            String msg = e.getMessage();

            baseResponse = BaseResponse.buildFailure(code, msg);
        }
        catch (Exception e){
            log.error(e.getMessage(), e);

            baseResponse = BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage());
        }finally {
            String responseStr = null;

            try {
                responseStr = responseSerializer.serialize(baseResponse);
            }catch (Exception e){
                responseStr = responseSerializer.serialize(BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage()));
            }

            SessionContextHolder.remove();
            ExposerContextHolder.remove();

            return responseStr;
        }
    }

    @RequestMapping(value = "/upload",  method = RequestMethod.POST)
    @ResponseBody
    public String upload(
            @RequestParam(value="method",required=false) String method,
            @RequestParam(value="session",required=false) String session,
            @RequestParam(value="ent_id",required=false) String ent_id,
            @RequestParam(value="user_id",required=false) String user_id,
            @RequestParam(value="user_code",required=false) String user_code,
            @RequestParam(value="user_name",required=false) String user_name,
            @RequestParam(value="locale",required=false) String locale,
            @RequestParam(value="token",required=false) String token,
            @RequestParam("file") MultipartFile file) {

        BaseResponse baseResponse = null;
        try{
            setSession(ent_id, user_id, user_code, user_name, locale, session, token);

            int lastDot = method.lastIndexOf(".");

            String componentName = method.substring(0, lastDot);
            String componentMethod = method.substring(lastDot + 1);

            Object result = componentInvoker.invoke(componentName, componentMethod, file);

            baseResponse= BaseResponse.buildSuccess(result);
        }
        catch (Exception e){
            log.error(e.getMessage(), e);

            baseResponse = BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage());
        }finally {
            String responseStr = null;

            try {
                responseStr = responseSerializer.serialize(baseResponse);
            }catch (Exception e){
                responseStr = responseSerializer.serialize(BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage()));
            }

            SessionContextHolder.remove();
            ExposerContextHolder.remove();

            return responseStr;
        }
    }

    @RequestMapping(value = "/handle",  method = RequestMethod.POST)
    public void handle(
            @RequestParam(value="method",required=false) String method,
            @RequestParam(value="session",required=false) String session,
            @RequestParam(value="ent_id",required=false) String ent_id,
            @RequestParam(value="user_id",required=false) String user_id,
            @RequestParam(value="user_code",required=false) String user_code,
            @RequestParam(value="user_name",required=false) String user_name,
            @RequestParam(value="locale",required=false) String locale,
            @RequestParam(value="token",required=false) String token,
            HttpServletRequest request,
            HttpServletResponse response) {

        try{
            setSession(ent_id, user_id, user_code, user_name, locale, session, token);

            int lastDot = method.lastIndexOf(".");

            String componentName = method.substring(0, lastDot);
            String componentMethod = method.substring(lastDot + 1);

            componentInvoker.invoke(componentName, componentMethod, request, response);
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            SessionContextHolder.remove();
            ExposerContextHolder.remove();
        }
    }

    /**
     *
     * 设置回话信息
     *
     * @param ent_id
     * @param user_id
     * @param user_name
     * @param locale
     * @param session
     */
    public void setSession(String ent_id,String user_id,String user_code,String user_name,String locale,String session, String token){
        log.info(">> enti_id:{}, user_id:{}, user_code:{}, user_name:{}, locale:{}, token:{}, session: {}",
                ent_id, user_id, user_code, user_name, locale, token, session);

        SessionContext sessionContext = sessionContextBuilder.build(ent_id, user_id, user_code, user_name, locale, session, token);

        SessionContextHolder.put(sessionContext);
    }
}
