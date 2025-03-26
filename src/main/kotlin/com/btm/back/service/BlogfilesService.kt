package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.dto.Blogfiles
import com.btm.back.utils.BaseResult
import org.springframework.web.multipart.MultipartFile

/**
 * 博客文件服务接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface BlogfilesService {
    /**
     * 创建新文件
     * @param blogfile 文件信息
     * @return 操作结果
     */
    fun createBlogfile(uploadFile: MultipartFile?): BaseResult
//
//    /**
//     * 更新文件信息
//     * @param blogfile 文件信息
//     * @return 操作结果
//     */
//    fun updateBlogfile(blogfile: Blogfiles): BaseResult
//
//    /**
//     * 删除文件
//     * @param blogfileId 文件ID
//     * @return 操作结果
//     */
//    fun deleteBlogfile(blogfileId: Int): BaseResult
//
//    /**
//     * 获取文件详情
//     * @param blogfileId 文件ID
//     * @return 文件详情
//     */
//    fun getBlogfileById(blogfileId: Int): BaseResult


}
