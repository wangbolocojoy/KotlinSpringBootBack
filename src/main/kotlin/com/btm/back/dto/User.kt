package com.btm.back.dto

import com.btm.back.utils.abstractObjectToString
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "User")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class User : abstractObjectToString() {
    @Id
    @GeneratedValue
    var id: Int? = 0
    var relasename: String? = ""
    var phone: String? = ""
    var nickname: String? = ""
    var account: String? = ""
    var password: String? = ""
    var icon: String? = ""
    var likestarts: Int? = 0
    var fances: Int? = 0
    var token: String? = ""
    override fun toString(): String {
        return "User(id=$id, relasename=$relasename, phone=$phone, nickname=$nickname, account=$account, password=$password, icon=$icon, likestarts=$likestarts, fances=$fances, token=$token)"
    }


}
