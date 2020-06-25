package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*
@Entity
@Table(name = "Favorites")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Favorites {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
}
