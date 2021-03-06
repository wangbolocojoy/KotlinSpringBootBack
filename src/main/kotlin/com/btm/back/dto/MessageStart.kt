package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import javax.persistence.*

@Entity
@Table(name = "MessageStart")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
//@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
class MessageStart  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var msgId:Int? = null


}
