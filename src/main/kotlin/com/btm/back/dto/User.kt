package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable
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
    var userSex:Boolean? = false
    var icon: String? = null
    var likeStarts: Int? = 0
    var postNum:Int? = 0
    var fances: Int? = 0
    var follows: Int? = 0
    var token: String? = null
    var easyInfo:String? = null
    var address:String? = null
    var originalFileName:String? = null
    var isFollow:Boolean? = false
    var creatTime:Date? = null
    var birthDay:String? = null
    var constellation:String? = null
    var userIdentifier:String? = null
    var province:String? = null
    var city:String? = null
    var isItBanned:Boolean? = false
    var isAdministrators:Boolean? = false
    var isAuthentication:Boolean? = false






}
