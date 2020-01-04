package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "Address")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Address {
    @Id
    @GeneratedValue
    var id: Int? = null
    var addressStartName: String? = null
    var addressEndName: String? = null
    var addressStartX: Double? = null
    var addressEndX: Double? = null
    var addressStartY: Double? = null
    var addressEndY: Double? = null
    override fun toString(): String {
        return "Address(id=$id, addressStartName=$addressStartName, addressEndName=$addressEndName, addressStartX=$addressStartX, addressEndX=$addressEndX, addressStartY=$addressStartY, addressEndY=$addressEndY)"
    }


}
