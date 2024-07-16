package com.jiahe.px.controller;

import com.alibaba.fastjson2.JSONObject;
import com.jiahe.px.common.base.BaseException;
import com.jiahe.px.common.base.response.BaseVO;
import com.jiahe.px.common.base.web.BaseRestController;
import com.jiahe.px.common.context.SessionContextHolder;
import com.jiahe.px.common.core.utils.Convert;
import com.jiahe.px.common.web.aop.support.ExposerContextHolder;
import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.common.web.support.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 批销接口
 * Date: 2024/7/16
 */

@Slf4j
@RestController
@RequestMapping(value = "/px")
public class PxController extends BaseRestController {
    @RequestMapping(value = "/rest",  method = RequestMethod.POST)
    public BaseResponse onRest(@RequestParam(value="method",required=false) String method,
                               @RequestBody String param) {
        BaseResponse baseResponse = null;
        Object result = null;
        try {
            //setSession(ent_id, user_id, user_code, user_name, locale, session, token);
            if (param == null | "".equals(param)) {
                throw new BaseException("10", "缺少必要参数");
            }
            JSONObject jsonObject = JSONObject.parseObject(param);
            result = componentInvoker.invoke(method, jsonObject);
            if (result.getClass().getName().equals("java.lang.String")) {
                jsonObject = JSONObject.parseObject(Convert.ToString(result));
                if ("0".equals(jsonObject.getString("returncode"))){
                    baseResponse = BaseResponse.buildSuccess(jsonObject.get("data"));
                }else {
                    baseResponse = BaseResponse.buildFailure(jsonObject.getString("returncode"),
                            jsonObject.getString("data"));
                }
            } else if(result.getClass() == BaseResponse.class){
                baseResponse = (BaseResponse)result;
            } else if(result.getClass() == BaseVO.class){
                baseResponse = new BaseResponse();
                baseResponse.setCode(((BaseVO) result).getCode());
                baseResponse.setData(((BaseVO) result).getData());
            }
            else {
                baseResponse = BaseResponse.buildSuccess(result);
            }
        } catch (BaseException e){
            log.error(e.getMessage(), e);

            String code = StringUtils.hasText(e.getCode())?e.getCode(): ResponseCode.FAILURE;
            String msg = e.getMessage();

            baseResponse = BaseResponse.buildFailure(code, msg);
        } catch (RuntimeException e){
            log.error(e.getMessage());
            baseResponse = BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            baseResponse = BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage());
        }finally {
            /*String responseStr = null;

            try {
                responseStr = responseSerializer.serialize(baseResponse);
            }catch (Exception e){
                responseStr = responseSerializer.serialize(BaseResponse.buildFailure(ResponseCode.FAILURE, e.getMessage()));
            }*/

            SessionContextHolder.remove();
            ExposerContextHolder.remove();

            return baseResponse;
        }
    }
}
