package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*
@Entity
@Table(name = "BackInfoPlist")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])

class BackInfoPlist {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null
        var userId :Int? = null
        var backId:Int? = null



}
