package com.btm.back.utils

import com.btm.back.utils.ErrorCodeEnum

/**
 * @author hero
 */
class KtErrorException : RuntimeException {
    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return property value of errorCode
     */
    /** 错误码  */
    val errorCode: ErrorCode
    /**
     * 这个是和谐一些不必要的地方,冗余的字段
     * 尽量不要用
     */
    private val code: String? = null

    /**
     * 无参默认构造UNSPECIFIED
     */
    constructor() : super(ErrorCodeEnum.UNSPECIFIED.getDescription()) {
        errorCode = ErrorCodeEnum.UNSPECIFIED
    }

    /**
     * 指定错误码构造通用异常
     * @param errorCode 错误码
     */
    constructor(errorCode: ErrorCode) : super(errorCode.getDescription()) {
        this.errorCode = errorCode
    }

    /**
     * 指定详细描述构造通用异常
     * @param detailedMessage 详细描述
     */
    constructor(detailedMessage: String?) : super(detailedMessage) {
        errorCode = ErrorCodeEnum.UNSPECIFIED
    }

    /**
     * 指定导火索构造通用异常
     * @param t 导火索
     */
    constructor(t: Throwable?) : super(t) {
        errorCode = ErrorCodeEnum.UNSPECIFIED
    }

    /**
     * 构造通用异常
     * @param errorCode 错误码
     * @param detailedMessage 详细描述
     */
    constructor(errorCode: ErrorCode, detailedMessage: String?) : super(detailedMessage) {
        this.errorCode = errorCode
    }

    /**
     * 构造通用异常
     * @param errorCode 错误码
     * @param t 导火索
     */
    constructor(errorCode: ErrorCode, t: Throwable?) : super(errorCode.getDescription(), t) {
        this.errorCode = errorCode
    }

    /**
     * 构造通用异常
     * @param detailedMessage 详细描述
     * @param t 导火索
     */
    constructor(detailedMessage: String?, t: Throwable?) : super(detailedMessage, t) {
        errorCode = ErrorCodeEnum.UNSPECIFIED
    }

    /**
     * 构造通用异常
     * @param errorCode 错误码
     * @param detailedMessage 详细描述
     * @param t 导火索
     */
    constructor(errorCode: ErrorCode, detailedMessage: String?,
                t: Throwable?) : super(detailedMessage, t) {
        this.errorCode = errorCode
    }

    companion object {
        private const val serialVersionUID = -7864604160297181941L
    }
}
