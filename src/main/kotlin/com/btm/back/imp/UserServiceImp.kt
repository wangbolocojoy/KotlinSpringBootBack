package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.User
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.FollowRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.UserService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import com.btm.back.utils.TokenService
import com.btm.back.vo.UserVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserServiceImp :UserService{
    @Autowired
    lateinit var userrepository: UserRespository
    @Autowired
    lateinit var followRespository: FollowRespository
    private val logger: Logger = LoggerFactory.getLogger(UserServiceImp::class.java)
    override fun register(body: ReqBody): BaseResult {
        val u= body.phone?.let { userrepository.findByPhone(it) }
        return if (u!=null){
            BaseResult.FAIL("此号码已经注册请直接登录")
        }else{
            val user =User()
            user.account = body.phone
            user.phone = body.phone
            user.password = body.password
            user.token = TokenService.getToken(user)
            userrepository.save(user)
            val s = CopierUtil.copyProperties(user,UserVO::class.java)
            logger.info("注册成功--- $s ")
            BaseResult.SECUESS(s)
        }
    }

    override fun deleteUser(id: Long) :BaseResult{
        val u= userrepository.findById(id)
        return if(u.isPresent){
            userrepository.deleteById(id)
            BaseResult.SECUESS("删除成功")
        }else{
            BaseResult.FAIL("未找到该用户")
        }


    }

    override fun login(body: ReqBody): BaseResult {
        val u= body.phone?.let { userrepository.findByAccount(it) }
        return if(u!=null){
            if(u.phone == body.phone&&u.password==body.password){
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                logger.info("登录成功--- $s ")
                BaseResult.SECUESS(s)
            }else{
                BaseResult.FAIL("账号或密码错误")
            }
        }else{
            BaseResult.FAIL("请先注册账号")
        }
    }

    override fun findUserById(phone: String):User? {
        val u=  userrepository.findByPhone(phone)
        return u
    }

    override fun getuserinfo(body: ReqBody): BaseResult {
        val u= body.id?.let { userrepository.findById(it) }
        return if (u != null) {
            val  s= CopierUtil.copyProperties(u,UserVO::class.java)
            logger.info("获取用户信息成功--- $s ")
            BaseResult.SECUESS(s)
        }else{
            BaseResult.FAIL("该用户不存在")
        }

    }



    override fun updateUser(body: ReqBody): BaseResult {
        val u= body.id?.let { userrepository.findById(it) }
        if (u != null){
            if (null !=body.userSex){
                u.userSex = body.userSex
            }
            if (null !=body.nickName){
                u.nickName = body.nickName
            }
            if (null !=body.realName){
                u.realName = body.realName
            }
            if (null !=body.easyInfo){
                u.easyInfo = body.easyInfo
            }
            if (null !=body.address){
                u.address = body.address
            }
            if (null !=body.fances){
                u.fances = body.fances
            }
            if (null !=body.likeStarts){
                u.likeStarts = body.likeStarts
            }
            if (null != body.postNum){
                u.postNum = body.postNum
            }
            if (null != body.userSex){
                u.userSex = body.userSex
            }
            userrepository.save(u)
            val s = CopierUtil.copyProperties(u,UserVO::class.java)
            logger.info("更新用户信息成功$s")
            return  BaseResult.SECUESS(s)
        }else{
            return BaseResult.FAIL("该用户不存在")
        }
    }



    override fun updateIcon(id: Int,uploadType:String, uploadFile:MultipartFile? ):BaseResult {
        val u=  userrepository.findById(id)
        if (null !=u ){
            return if (null!=uploadFile){
                val oldfilename = u.originalFileName
                val url =AliYunOssUtil.uploadToAliyun(uploadFile.originalFilename ?: "",uploadFile.inputStream,uploadFile.contentType ?: "jpg",uploadType,id.toString())
                u.icon = url
                u.originalFileName = uploadFile.originalFilename
                AliYunOssUtil.deleteFile(oldfilename ?: "",id.toString())
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                logger.info("头像修改成功$s")
                BaseResult.SECUESS("头像修改成功",s)
            }else{
                BaseResult.FAIL("文件不存在")
            }
        }else{
            return  BaseResult.FAIL("该用户不存在")
        }
    }

    override fun test() : BaseResult{
        val list:List<User?>?

        list= userrepository.findAll()
        return BaseResult.SECUESS(list)
    }

    override fun sendPost(body: ReqBody) {
    }

    /**
     * 查找关注
     */
    override fun searchfollow(body: ReqBody): BaseResult {
        val user = userrepository.findByPhone(body.phone ?:"")
        val follow = followRespository.findByFollowId(body.id?:0)
        val f =follow.any { user?.id == it.followId }
        user?.isFollow = f
        return if (user!= null){
            val s = CopierUtil.copyProperties(user, UserVO::class.java)
            logger.info("查找用户成功$s")
            BaseResult.SECUESS(s)
        }else{
            BaseResult.FAIL("用户不存在")
        }
    }


}
