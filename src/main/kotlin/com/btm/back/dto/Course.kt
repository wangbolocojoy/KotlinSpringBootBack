package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * 课程实体类
 * @author Trae AI
 * @date 2023-06-01
 */
@Entity
@Table(name = "courses")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var courseId: Int? = null
    
    @Column(nullable = false)
    var courseName: String? = null
    
    @Column(columnDefinition = "TEXT")
    var courseDescription: String? = null
    
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null
}