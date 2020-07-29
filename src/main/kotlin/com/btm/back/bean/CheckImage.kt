package com.btm.back.bean

class CheckImage {
    val data:ResultData? = null
}
class ResultData{
   val results:Array<ResultInfo>? = null
}
class ResultInfo{
   val dataId:String? =null
    val imageURL:String? = null
    val subResults:Array<ImageInfo>? = null
}
class ImageInfo{
    val label:String? = null
    val scene:String? = null
    val suggestion:String? = null
    val rate:Double? = null

}
