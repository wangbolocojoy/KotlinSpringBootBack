package com.btm.back.bean

import com.alibaba.fastjson.annotation.JSONField


data class ContextModel(
        @JSONField(name = "data")
        val `data`: Data? = Data(),
        @JSONField(name = "requestId")
        val requestId: String? = ""

) {
        override fun toString(): String {
                return "ContextModel(`data`=$`data`, requestId=$requestId)"
        }
}

data class Data(
        @JSONField(name = "elements")
        val elements: List<Element>? = listOf(),
        @JSONField(name = "frontResult")
        val frontResult: FrontResult? = FrontResult(),
        @JSONField(name = "backResult")
        val backResult: BackResult? = BackResult()

) {
        override fun toString(): String {
                return "Data(elements=$elements, frontResult=$frontResult, backResult=$backResult)"
        }
}

data class Element(
        @JSONField(name = "results")
        val results: List<Result>? = listOf(),
        @JSONField(name = "taskId")
        val taskId: String? = ""

) {
        override fun toString(): String {
                return "Element(results=$results, taskId=$taskId)"
        }
}

data class Result(
        @JSONField(name = "details")
        val details: List<Detail>? = listOf(),
        @JSONField(name = "label")
        val label: String? = "",
        @JSONField(name = "rate")
        val rate: Double? = 0.0,
        @JSONField(name = "suggestion")
        val suggestion: String? = ""

) {
        override fun toString(): String {
                return "Result(details=$details, label=$label, rate=$rate, suggestion=$suggestion)"
        }
}

data class Detail(
        @JSONField(name = "contexts")
        val contexts: List<Contexts>? = listOf(),
        @JSONField(name = "rate")
        val Label: String? = ""

) {
        override fun toString(): String {
                return "Detail(contexts=$contexts, Label=$Label)"
        }
}

data class Contexts(
        @JSONField(name = "contexts")
        val context: String? = ""

) {
        override fun toString(): String {
                return "Contexts(context=$context)"
        }
}






