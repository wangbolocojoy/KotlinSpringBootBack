package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * 文章实体类
 * 用于映射数据库中的文章表
 *
 * @author Trae AI
 * @date 2023-06-01
 */
@Entity
@Table(name = "articles")
// 忽略一些内部使用的属性，以避免在序列化时出现问题
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Article {
    // 主键，自动生成
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var articleId: Int? = null

    // 文章标题，不能为空
    @Column(nullable = false)
    var title: String? = null

    // 作者，不能为空
    @Column(nullable = false)
    var author: String? = null

    // 文章内容，使用TEXT类型存储较大文本
    @Column(columnDefinition = "TEXT")
    var content: String? = null

    // 图片URL的JSON字符串，使用TEXT类型存储
    @Column(columnDefinition = "TEXT")
    var images: String? = null

    // 创建时间，使用时间戳格式存储
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    // 更新时间，使用时间戳格式存储
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date? = null
}
