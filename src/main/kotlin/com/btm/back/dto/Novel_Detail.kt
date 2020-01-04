package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "novel_detail")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Novel_Detail {
    @Id
    @GeneratedValue
    var id: Int? = null
    var novel_id: Int? = null
    var novel_content: String? = null
    var novel_chapter: String? = null
    override fun toString(): String {
        return "Novel_Detail(id=$id, novel_id=$novel_id, novel_content=$novel_content, novel_chapter=$novel_chapter)"
    }


}
