package com.btm.back.helper

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.btm.back.utils.BaseResult
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse

object JsonHelper {
    @Autowired
    lateinit var response: HttpServletResponse
    @Throws(Exception::class)
    fun toJson(result: BaseResult?) {
        response.contentType = "text/json"
        response.setHeader("Cache-Control", "no-cache")
        response.characterEncoding = "UTF-8"
        val writer = response.writer
        val json = JSONObject.toJSONString(result,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.DisableCircularReferenceDetect)
        writer.write(json)
        writer.close()
    }
}
