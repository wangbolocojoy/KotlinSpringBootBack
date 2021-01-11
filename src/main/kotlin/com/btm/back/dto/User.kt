package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "User")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var realName: String? = null
    var phone: String? = null
    var nickName: String? = null
    var account: String? = null
    var password: String? = null
    var userSex:Boolean? = null
    var icon: String? = null
    var likeStarts: Int? = null
    var postNum:Int? = null
    var fances: Int? = null
    var follows: Int? = null
    var token: String? = null
    var easyInfo:String? = null
    var address:String? = null
    var originalFileName:String? = null
    var isFollow:Boolean? = null
    var creatTime:Date? = null
    var birthDay:String? = null
    var constellation:String? = null
    var userIdentifier:String? = null
    var province:String? = null
    var city:String? = null
    var isbanned:Boolean? = null
    var administrators:Boolean? = null
    var authentication:Boolean? = null






}
