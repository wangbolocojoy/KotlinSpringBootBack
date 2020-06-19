package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "User")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var relasename: String? = null
    var phone: String? = null
    var nickname: String? = null
    var account: String? = null
    var password: String? = null
    var usersex:Boolean? = false
    var icon: String? = null
    var likestarts: Int? = 0
    var postnum:Int? = 0
    var fances: Int? = 0
    var follows: Int? = 0
    var token: String? = null
    var seayinfo:String? = null
    var address:String? = null
    var originalfilename:String? = null
    var isfollow:Boolean? = false



}
