package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "novel")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Novel {
    @Id
    @GeneratedValue
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
    override fun toString(): String {
        return "Novel(id=$id, novel_id=$novel_id, novel_name=$novel_name, novel_easyinfo=$novel_easyinfo, novel_author=$novel_author, novel_img=$novel_img, novel_type=$novel_type, novel_typename=$novel_typename, novel_uptime=$novel_uptime, novel_state=$novel_state)"
    }


}
