package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "Address")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId:Int? = null
    var addressStartName: String? = null
    var addressEndName: String? = null
    var addressStartX: Double? = null
    var addressEndX: Double? = null
    var addressStartY: Double? = null
    var addressEndY: Double? = null


}
