package com.btm.back.repository

import com.btm.back.dto.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

/**
 * 定义一个接口CourseRepository，用于管理Course实体的数据库操作
 * 该接口继承了JpaRepository和JpaSpecificationExecutor，以获取丰富的数据库操作功能
 * JpaSpecificationExecutor的继承使得该接口支持复杂的查询构建
 */
interface CourseRepository : JpaRepository<Course, Int>, JpaSpecificationExecutor<Course> {
}