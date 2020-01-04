package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "noveltype")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class NovelType {
    @Id
    @GeneratedValue
    var id: Int? = null
    var typename: String? = null
    var typeimg: String? = null
    override fun toString(): String {
        return "NovelType(id=$id, typename=$typename, typeimg=$typeimg)"
    }


}
