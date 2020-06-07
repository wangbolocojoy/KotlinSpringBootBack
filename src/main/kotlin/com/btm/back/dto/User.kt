package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "User")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0
    var relasename: String? = ""
    var phone: String? = ""
    var nickname: String? = ""
    var account: String? = ""
    var password: String? = ""
    var userSex:Boolean? =false
    var icon: String? = ""
    var likestarts: Int? = 0
    var fances: Int? = 0
    var token: String? = ""
    var seayinfo:String? = ""
    var address:String? = ""
    var originalFilename:String? = ""



}
