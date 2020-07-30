package com.btm.back.bean
import com.alibaba.fastjson.annotation.JSONField


data class IdCardModel(
    @JSONField(name = "data")
    val `data`: Data? = Data(),
    @JSONField(name = "requestId")
    val requestId: String? = ""

) {
    override fun toString(): String {
        return "IdCardModel(`data`=$`data`, requestId=$requestId)"
    }
}

data class FrontResult(
        @JSONField(name = "address")
        val address: String? = "",
        @JSONField(name = "birthDate")
        val birthDate: String? = "",
        @JSONField(name = "cardAreas")
        val cardAreas: List<Any>? = listOf(),
        @JSONField(name = "faceRectVertices")
        val faceRectVertices: List<Any>? = listOf(),
        @JSONField(name = "faceRectangle")
        val faceRectangle: FaceRectangle? = FaceRectangle(),
        @JSONField(name = "gender")
        val gender: String? = "",
        @JSONField(name = "iDNumber")
        val iDNumber: String? = "",
        @JSONField(name = "name")
        val name: String? = "",
        @JSONField(name = "nationality")
        val nationality: String? = ""

) {
    override fun toString(): String {
        return "FrontResult(address=$address, birthDate=$birthDate, cardAreas=$cardAreas, faceRectVertices=$faceRectVertices, faceRectangle=$faceRectangle, gender=$gender, iDNumber=$iDNumber, name=$name, nationality=$nationality)"
    }
}
data class BackResult(
        @JSONField(name = "endDate")
        val endDate: String? = "",
        @JSONField(name = "issue")
        val issue: String? = "",
        @JSONField(name = "startDate")
        val startDate: String? = ""

) {
    override fun toString(): String {
        return "BackResult(endDate=$endDate, issue=$issue, startDate=$startDate)"
    }
}

data class FaceRectangle(
        @JSONField(name = "center")
        val center: Center? = Center(),
        @JSONField(name = "size")
        val size: Size? = Size()

) {
    override fun toString(): String {
        return "FaceRectangle(center=$center, size=$size)"
    }
}

class Center(
)

class Size(
)
