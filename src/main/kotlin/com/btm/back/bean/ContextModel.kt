package com.btm.back.bean
import com.alibaba.fastjson.annotation.JSONField


data class ContextModel(
    @JSONField(name = "data")
    val `data`: Data? = Data(),
    @JSONField(name = "requestId")
    val requestId: String? = ""
)

data class Data(
    @JSONField(name = "elements")
    val elements: List<Element>? = listOf()
)

data class Element(
    @JSONField(name = "results")
    val results: List<Result>? = listOf(),
    @JSONField(name = "taskId")
    val taskId: String? = ""
)

data class Result(
    @JSONField(name = "details")
    val details: List<Detail>? = listOf(),
    @JSONField(name = "label")
    val label: String? = "",
    @JSONField(name = "rate")
    val rate: Double? = 0.0,
    @JSONField(name = "suggestion")
    val suggestion: String? = ""
)
data class Detail(
        @JSONField(name = "contexts")
        val contexts: List<Contexts>? = listOf(),
        @JSONField(name = "rate")
        val Label: String? = ""
)
data class Contexts(
        @JSONField(name = "contexts")
        val context: String? = ""
)
