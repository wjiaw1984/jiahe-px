package com.jiahe.px.common.web.support;

public interface ResponseCode {
    final String SUCCESS = "0";
    final String FAILURE = "10000";
    final String EXCEPTION = "50000";

    interface Exception
    {
        final String SESSION_IS_EMPTY = "50001";						   // Session为空
        final String ENTID_IS_EMPTY = "50002";							   // 企业ID为空
        final String PARAM_IS_EMPTY = "50003";                             // 参数为空
        final String SPECDATA_IS_EMPTY = "50004";                          // 指定数据为空
        final String NOT_EXIST_MATCHED = "50005";                          // 未找到匹配的请求
        final String PRIMARY_IS_EMPTY="50006";								//主键为空
        final String PRIMARY_IS_ERROR="50007";								//参数格式错误
    }

    interface Failure
    {
        final String ALREADY_EXISTS = "10001";                             // 数据已存在
        final String ALREADY_PUBLISH= "10002";                             // 数据已发布
        final String NOT_EXIST= "10003";                                   // 数据不存在
        final String BE_USE= "10004";                                      // 数据被使用
        final String DEPENDENCY_IS_ILLEGAL="10005";                        // 依赖KEY无效
        final String DEPENDENCY_NOT_EXIST="10006";                         // 依赖数据不存在
        final String IS_PARENT= "10007";                                   // 父级数据不能删除
        final String IS_NOT_LAST= "10008";                                 // 不是末级数据
        final String PROPERTY_MUST_SET= "10009";                           // 品类属性必须设置
        final String PROPERTY_VALUE_INVALID= "10010";                      // 品类属性值无效
        final String FAIL_UPDATE= "10011";                     				 // 更新失败
        final String FAIL_INSERT= "10012";									//插入失败
        final String FAIL_DELETE= "10013";									//删除失败
    }

    interface NetException
    {
        final String UNSUPPORTED_ENCODING_EXCEPTION = "60001";		       // 远程请求转码异常
        final String CLIENT_PROTOCOL_EXCEPTION = "60002";			       // 客户端提交给服务器的请求，不符合HTTP协议
        final String NETWORK_IO_EXCEPTION = "60003";					   // 网络IO异常
        final String RESPONSE_STATE_EXCEPTION = "60004";			       // 远程请求状态异常
        final String RESPONSE_DATA_EXCEPTION = "60005";					   // 远程请求状态异常
    }

    interface memberException
    {
        final String MEMBER_OTHER = "550000";                               //其它异常

        final String MEMBER_IS_EMPTY = "550101";                            //找不到会员资料
        final String MEMBER_NOT_MEMBERCARD = "550102";                      //找不到会员卡资料
        final String MEMBER_NOT_VALUECARD = "550103";                      //找不到会员面值卡资料
        final String MEMBER_EXT_OTHER = "550104";                          //会员扩展资料其它异常
        final String MEMBER_EXT_ADD = "550105";                            //会员扩展资料创建异常
        final String MEMBER_QRCODE_INVALID = "550106";                     //会员识别二维码失效;
        final String MEMBER_CARD_FREEZE = "550107";                        //会员卡冻结

        final String MEMBER_CARD_OVERDUE = "551103";                        //卡逾期
        final String MEMBER_CARD_DIFF = "551101";                           //会员资料与卡资料会员编码不一致
        final String MEMBER_CARD_NOT_AVAILABLE = "551102";                  //卡不可用


    }
}