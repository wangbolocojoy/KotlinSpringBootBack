package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.UserServiceImpl
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
@RestController
@RequestMapping(value = ["/api/user"])
class UserController  {
    @Autowired
    lateinit var userServiceImp :UserServiceImpl

    @Operation(summary = "用户注册", description = "新用户注册接口")
    fun register(
        @Parameter(description = "用户注册信息", required = true)
        @Valid @RequestBody u: ReqBody
    ) = userServiceImp.register(u)

    @Operation(summary = "发送验证码", description = "向用户手机发送验证码")
    @RequestMapping(value = ["sendmsg"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun sendmsg(
        @Parameter(description = "发送验证码", required = true)
        @Valid @RequestBody u: ReqBody
    ) = userServiceImp.sendmsg(u)

    // 替换 @ApiOperation 为 @Operation，@ApiParam 为 @Parameter
    @Operation(summary = "用户登录", description = "用户登录接口，返回token")
    fun login(
        @Parameter(description = "用户登录信息", required = true) 
        @Valid @RequestBody u: ReqBody
    ) = userServiceImp.login(u)


    @Operation(summary = "修改密码", description = "用户修改密码接口")
    @PassToken
    @RequestMapping(value = ["updatepassword"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun updatePassWord(
        @Parameter(description = "密码修改参数", required = true) 
        @Valid @RequestBody u: ReqBody
    )
    = userServiceImp.updatePassWord(u)

    @Operation(summary = "获取用户信息", description = "获取当前用户信息")
    @UserLoginToken
    @RequestMapping(value = ["getuseinfo"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getUserInfo(
        @Parameter(description = "用户信息查询参数", required = true) 
        @Valid @RequestBody u: ReqBody
    ) = userServiceImp.getuserinfo(u)

    @Operation(summary = "修改用户信息", description = "修改用户信息接口")
    @UserLoginToken
    @RequestMapping(value = ["updateuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun updateUser(
        @Parameter(description = "用户信息修改参数", required = true) 
        @Valid @RequestBody u: ReqBody
    ) = userServiceImp.updateUser(u)

    @Operation(summary = "上传用户头像", description = "上传用户头像接口")
    @UserLoginToken
    @RequestMapping(value = ["uploadusericon"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun uploadIcon(
        @Parameter(description = "用户ID", required = true) 
        @RequestParam id:Int,
        @Parameter(description = "上传类型", required = true) 
        @RequestParam uploadtype:String,
        @Parameter(description = "上传文件", required = true) 
        @RequestPart("uploadFile")uploadFile: MultipartFile?
    )= userServiceImp.updateIcon(id,uploadtype,uploadFile)



    @UserLoginToken
    @RequestMapping(value = ["deleteall"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun deleteall(@RequestBody u: ReqBody) {}

    @UserLoginToken
    @RequestMapping(value = ["getalluser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getAllUser(@RequestBody u: ReqBody) = userServiceImp.getAllUser(u)

    @UserLoginToken
    @RequestMapping(value = ["searchfollow"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun searchfollow(@RequestBody u: ReqBody) =userServiceImp.searchfollow(u)

    @UserLoginToken
    @RequestMapping(value = ["sendfeedback"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun sendFeedBack(@RequestBody u: ReqBody) =userServiceImp.sendFeedBack(u)

    @UserLoginToken
    @RequestMapping(value = ["getfeedback"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getFeedBack(@RequestBody u: ReqBody) =userServiceImp.getFeedBack(u)

    @PassToken
    @RequestMapping(value = ["getdeveloperinfo"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getDeveloperInfo() =userServiceImp.getDeveloperInfo()


    @UserLoginToken
    @RequestMapping(value = ["uploadidcard"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun uploadIdCard(
        @Parameter(description = "用户ID", required = true) 
        @RequestParam  userId:Int, 
        @Parameter(description = "上传类型", required = true) 
        @RequestParam uploadType:String, 
        @Parameter(description = "上传文件", required = true) 
        @RequestPart("uploadFile")uploadFile: MultipartFile? 
    )= userServiceImp.uploadIdCard(userId,uploadType,uploadFile)

    @UserLoginToken
    @RequestMapping(value = ["getidcardinfo"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getIdCardInfo(@RequestBody u: ReqBody) =userServiceImp.getIdCardInfo(u)

    @UserLoginToken
    @RequestMapping(value = ["addbackinfoplist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun addBackInfoPlist(@RequestBody u: ReqBody) =userServiceImp.addBackInfoPlist(u)

    @UserLoginToken
    @RequestMapping(value = ["removebackinfoplist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun removeBackInfoPlist(@RequestBody u: ReqBody) =userServiceImp.removeBackInfoPlist(u)

    @UserLoginToken
    @RequestMapping(value = ["getbacklist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getBackList(@RequestBody u: ReqBody) =userServiceImp.getBackList(u)

    @PassToken
    @RequestMapping(value = ["getaliyuntoken"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getaliyuntoken(@RequestBody u: ReqBody) =userServiceImp.CreateVerifyScheme(u)

    @PassToken
    @RequestMapping(value = ["verftokenandlogin"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun verftokenandlogin(@RequestBody u: ReqBody) =userServiceImp.getVerifyCodeandResister(u)
}
