package com.btm.back.utils

import org.apache.commons.codec.binary.StringUtils

enum class ErrorCodeEnum
/**
 * @param code 错误码
 * @param description 描述
 */(
        /** 错误码  */
        private val code: String,
        /** 描述  */
        private val description: String) : ErrorCode {
    UNSPECIFIED("500", "网络异常，请稍后再试"),
    TOKENERROR("434", "token验证错误"),
    TOKENISNULL("434", "无token，请重新登录"),
    TOKENJSONERROR("434", "token解析错误"),
    USERISNOTFOUND("434", "用户不存在，请重新登录"),
    USERISBANDE("454", "该账号已被封禁"),
    NO_SERVICE("404", "网络异常, 服务器熔断"),  // 通用异常
    REQUEST_ERROR("400", "入参异常,请检查入参后再次调用"), PAGE_NUM_IS_NULL("4001", "页码不能为空"), PAGE_SIZE_IS_NULL("4002", "页数不能为空"), ID_IS_NULL("4003", "ID不能为空"), SEARCH_IS_NULL("4004", "搜索条件不能为空"),  // 短信相关
    SEND_MASSAGE_FAIL("30001", "发送短消息失败"), SEND_MASSAGE_OFTEN("30002", "操作发送短消息太频繁,请稍后再试"), MESSAGE_TEMPLATE_UNDEFINED("30003", "短信模板未定义"),  //支付相关
    CREATE_PAY_ORDER_FAIL("40001", "创建订单支付失败"), UPDATE_PAY_ORDER_FAIL("40002", "更新支付订单失败"), DEL_PAY_ORDER_FAIL("40003", "更新支付订单失败"), PAY_ORDER_NO_EXISTS("40004", "支付订单不存在"), REFUND_APPLY_NO_EXISTS("40005", "退款申请不存在"), VERIFY_NOT_PASS("40006", "验签"), RES_FAIL("40007", "响应失败"), PAY_CHANNEL_IS_NULL("40008", "支付渠道不能为空"), PAY_CHANNEL_PARAM_ERROR("40009", "支付订单渠道参数错误");

    override fun getCode(): String = code
    override fun getDescription(): String = description

    companion object {
        /**
         * 根据编码查询枚举。
         *
         * @param code 编码。
         * @return 枚举。
         */
        fun getByCode(code: String?): ErrorCodeEnum {
            for (value in values()) {
                if (StringUtils.equals(code, value.getCode())) {
                    return value
                }
            }
            return UNSPECIFIED
        }

        /**
         * 枚举是否包含此code
         * @param code 枚举code
         * @return 结果
         */
        operator fun contains(code: String?): Boolean {
            for (value in values()) {
                if (StringUtils.equals(code, value.getCode())) {
                    return true
                }
            }
            return false
        }
    }

}
