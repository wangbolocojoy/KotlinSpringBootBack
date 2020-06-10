package com.btm.back.service
import com.btm.back.bean.ReqBody
import com.btm.back.dto.User
import com.btm.back.utils.BaseResult
import org.springframework.web.multipart.MultipartFile

interface UserService {
    /**
     * 注册
     */
    fun register(body: ReqBody): BaseResult

    /**
     * 删除用户
     */
    fun deleteUser(id: Long) :BaseResult

    /**
     * 登录
     */
    fun login(body: ReqBody):BaseResult
    /**
     * 查找用户
     */
    fun findUserById(id: String):User?
    /**
     * 获取用户信息
     */
    fun getuserinfo(body: ReqBody):BaseResult
    /**
     * 更新用户信息
     */
    fun updateUser(body: ReqBody):BaseResult
    /**
     * 更新用户头像
     */
    fun updateIcon(id: Int,uploadType:String,uploadFile:MultipartFile?):BaseResult
    fun test(): BaseResult
    /**
     * 发消息
     */
    fun sendPost(body: ReqBody)

    fun searchfollow(body: ReqBody):BaseResult


}
