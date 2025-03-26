package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * 摄影作品实体类
 * @author Trae AI
 * @date 2023-06-01
 */
@Entity
@Table(name = "photography")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Photography {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var photographyId: Int? = null
    
    @Column(nullable = false)
    var title: String? = null
    
    @Column(nullable = false)
    var author: String? = null
    
    @Column(columnDefinition = "TEXT")
    var content: String? = null
    
    @Column(columnDefinition = "TEXT")
    var images: String? = null  // 存储图片URL的JSON字符串
    
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null
    
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date? = null
}