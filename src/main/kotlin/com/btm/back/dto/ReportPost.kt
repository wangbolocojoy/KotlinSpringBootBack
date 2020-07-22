package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ReportPost")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class ReportPost{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var reportReason:String? = null
    var reportDescribe:String? = null
    var reportDateTime:Date? = null


}
