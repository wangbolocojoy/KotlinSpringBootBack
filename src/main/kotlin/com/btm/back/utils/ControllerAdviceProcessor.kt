package com.btm.back.utils

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest

/**
 * @author hero
 */
@ControllerAdvice
class ControllerAdviceProcessor {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    @Autowired
    protected var messageSource: MessageSource? = null

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(request: HttpServletRequest?, ex: Exception): BaseResult {
        var code = "500"
        if (ex is HttpMessageNotReadableException) {
            code = "400"
        } else if (ex is HttpRequestMethodNotSupportedException) {
            code = "999"
        }
        var msg: String? = null
        if (ex is KtErrorException) {
            val bizException = ex
            msg = bizException.message
            code = bizException.errorCode.getCode().toString()
        } else if (ex is AccessDeniedException) {
            msg = "无权限访问"
            code = "403"
        }
        if (msg == null) {
            msg = ex.message
        }
        val resp = BaseResult()
        resp.status = code.toInt()
        resp.msg = msg
        logger.error("code: $code  msg: $msg", ex)
        return resp
    }
}
