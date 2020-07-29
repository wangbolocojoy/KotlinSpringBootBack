package com.btm.back.bean

import java.io.Serializable

class CheckImageModel:Serializable {
    /**
     * data : {"results":[{"dataId":"8f7aafc0-61d0-438a-8989-3fd40b96d1c2","imageURL":"https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/fileserver/IMG_3010.jpg","subResults":[{"frames":[],"hintWordsInfoList":[],"label":"sexy","logoDataList":[],"oCRDataList":[],"programCodeDataList":[],"rate":62.11,"scene":"porn","sfaceDataList":[],"suggestion":"review"},{"frames":[],"hintWordsInfoList":[],"label":"normal","logoDataList":[],"oCRDataList":[],"programCodeDataList":[],"rate":99.99,"scene":"terrorism","sfaceDataList":[],"suggestion":"pass"}]}]}
     * requestId : 8BF5C6B4-5FE5-45B7-AAB5-41F2EF5AA2A8
     */
    var data: DataBean? = null
    var requestId: String? = null

    class DataBean {
        var results: List<ResultsBean>? = null

        class ResultsBean {
            /**
             * dataId : 8f7aafc0-61d0-438a-8989-3fd40b96d1c2
             * imageURL : https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/fileserver/IMG_3010.jpg
             * subResults : [{"frames":[],"hintWordsInfoList":[],"label":"sexy","logoDataList":[],"oCRDataList":[],"programCodeDataList":[],"rate":62.11,"scene":"porn","sfaceDataList":[],"suggestion":"review"},{"frames":[],"hintWordsInfoList":[],"label":"normal","logoDataList":[],"oCRDataList":[],"programCodeDataList":[],"rate":99.99,"scene":"terrorism","sfaceDataList":[],"suggestion":"pass"}]
             */
            var dataId: String? = null
            var imageURL: String? = null
            var subResults: List<SubResultsBean>? = null

            class SubResultsBean {
                /**
                 * frames : []
                 * hintWordsInfoList : []
                 * label : sexy
                 * logoDataList : []
                 * oCRDataList : []
                 * programCodeDataList : []
                 * rate : 62.11
                 * scene : porn
                 * sfaceDataList : []
                 * suggestion : review
                 *
                 */

                var label: String? = null
                var rate = 0.0
                var scene: String? = null
                var suggestion: String? = null
                var frames: List<*>? = null
                var hintWordsInfoList: List<*>? = null
                var logoDataList: List<*>? = null
                var oCRDataList: List<*>? = null
                var programCodeDataList: List<*>? = null
                var sfaceDataList: List<*>? = null
                override fun toString(): String {
                    return "SubResultsBean(label=$label, rate=$rate, scene=$scene, suggestion=$suggestion, frames=$frames, hintWordsInfoList=$hintWordsInfoList, logoDataList=$logoDataList, oCRDataList=$oCRDataList, programCodeDataList=$programCodeDataList, sfaceDataList=$sfaceDataList)"
                }

            }

            override fun toString(): String {
                return "ResultsBean(dataId=$dataId, imageURL=$imageURL, subResults=$subResults)"
            }

        }

        override fun toString(): String {
            return "DataBean(results=$results)"
        }
    }

    override fun toString(): String {
        return "CheckImageModel(data=$data, requestId=$requestId)"
    }

}
data class ContextModel(
    val Data: Data,
    val RequestId: String


) {
    override fun toString(): String {
        return "ContextModel(Data=$Data, RequestId='$RequestId')"
    }
}


data class Data(
    val Elements: List<Element>

) {
    override fun toString(): String {
        return "Data(Elements=$Elements)"
    }
}

data class Element(
    val Results: List<Result>,
    val TaskId: String

) {
    override fun toString(): String {
        return "Element(Results=$Results, TaskId='$TaskId')"
    }
}

data class Result(
    val Details: List<Detail>,
    val Label: String,
    val Rate: Double,
    val Suggestion: String

) {
    override fun toString(): String {
        return "Result(Details=$Details, Label='$Label', Rate=$Rate, Suggestion='$Suggestion')"
    }
}

data class Detail(
    val Contexts: List<Context>,
    val Label: String

) {
    override fun toString(): String {
        return "Detail(Contexts=$Contexts, Label='$Label')"
    }
}

data class Context(
    val Context: String

) {
    override fun toString(): String {
        return "Context(Context='$Context')"
    }
}
