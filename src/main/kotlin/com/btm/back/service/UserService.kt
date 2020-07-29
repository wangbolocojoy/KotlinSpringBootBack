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

    /**
    * @Description:
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-11
    * @Time: 00:34
    **/
    fun getDeveloperInfo():BaseResult

    fun sendFeedBack(body: ReqBody):BaseResult
    fun getFeedBack(body: ReqBody):BaseResult

    /**
    * @Description: 获取所以用户
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-23
    * @Time: 18:56
    **/
    fun getAllUser(body: ReqBody):BaseResult

    /**
    * @Description: 批量封号
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-23
    * @Time: 18:58
    **/
    fun setUserBanned(body: ReqBody):BaseResult

    /**
    * @Description: 实名认证
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-27
    * @Time: 14:34
    **/
    fun relaseNameAuthen (userId: Int?,cardName:String?,cardNumber:String,uploadFile: ArrayList<MultipartFile>?):BaseResult

    fun uploadIdCard(userId: Int?,uploadType:String,uploadFile:MultipartFile?)

}
