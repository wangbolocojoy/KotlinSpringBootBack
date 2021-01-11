package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "novel")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Novel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var novel_id: Int? = null
    var novel_name: String? = null
    var novel_easyinfo: String? = null
    var novel_author: String? = null
    var novel_img: String? = null
    var novel_type: Int? = null
    var novel_typename: String? = null
    var novel_uptime: String? = null
    var novel_state: String? = null



}
