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
    fun sendmsg(body: ReqBody): BaseResult
    /**
     * 删除用户
     */
    fun deleteUser(id: Long) :BaseResult

    /**
    * @Description:  修改密码
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-06
    **/
    fun updatePassWord(body: ReqBody):BaseResult
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


    fun searchfollow(body: ReqBody):BaseResult


}
