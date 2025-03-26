package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * 课程章节实体类
 * @author Trae AI
 * @date 2023-06-01
 */
@Entity
@Table(name = "lessons")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var lessonId: Int? = null
    
    @Column(name = "course_id")
    var courseId: Int? = null
    
    @Column(nullable = false)
    var lessonName: String? = null

    @Column(nullable = false)
    var lessonCover: String? = null
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var lessonContent: String? = null
    
    @Column(nullable = false)
    var lessonOrder: Int? = null
    
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    var course: Course? = null
}