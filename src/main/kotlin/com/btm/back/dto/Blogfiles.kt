package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

// 定义一个实体类 Blogfiles，对应数据库中的博客文件信息表
@Entity
@Table(name = "Blogfiles")
// 忽略一些内部使用的属性，以避免在序列化和反序列化时出现问题
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Blogfiles{
    // 主键，自动增长
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    // 文件类型
    var fileType: String? = null
    // 原始文件名
    var originalFileName: String? = null
    // 文件URL
    var fileUrl:String?= null
    // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null
    // 关联的博客ID
    var blogId:Int? = null
}
