package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * 国学篇章实体类
 * @author Trae AI
 * @date 2023-06-01
 */
@Entity
@Table(name = "classical_articles")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class ClassicalArticle {
    /**
     * 文章ID，主键，采用自增策略
     * 主键注解@Id标识此字段为实体的主键
     * @GeneratedValue注解指定主键的生成策略为自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var articleId: Int? = null

    /**
     * 文章标题，不允许为空
     * @Column注解用于指定数据库表中与该字段对应的列的属性，此处指定该列不允许为空
     */
    @Column(nullable = false)
    var title: String? = null

    /**
     * 作者姓名，不允许为空
     * 同样使用@Column注解指定数据库表中的列属性
     */
    @Column(nullable = false)
    var author: String? = null

    /**
     * 朝代信息，用来标识作者所处的时期，不允许为空
     */
    @Column(nullable = false)
    var dynasty: String? = null

    /**
     * 文章内容，使用TEXT类型存储较长的文本信息
     * columnDefinition属性用于指定列的SQL片段，这里使用TEXT类型
     */
    @Column(columnDefinition = "TEXT")
    var content: String? = null

    /**
     * 图片信息，存储与文章相关的图片URL的JSON字符串
     * 同样使用TEXT类型，因为可能存储较长的字符串信息
     */
    @Column(columnDefinition = "TEXT")
    var images: String? = null  // 存储图片URL的JSON字符串

    /**
     * 创建时间，用于记录文章创建的时间戳
     * @Temporal注解用于指定如何将日期或时间类型的字段映射为数据库中的类型
     * TemporalType.TIMESTAMP表示此字段映射为SQL中的TIMESTAMP类型
     */
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    /**
     * 更新时间，记录文章最后一次更新的时间戳
     * 同样使用@Temporal注解，指定时间戳类型
     */
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date? = null

}