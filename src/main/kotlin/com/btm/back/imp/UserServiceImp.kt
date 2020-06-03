package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.User
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.UserRespository
import com.btm.back.service.UserService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import com.btm.back.utils.TokenService
import com.btm.back.vo.UserVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserServiceImp :UserService{
    @Autowired
    lateinit var userrepository: UserRespository

    private val logger: Logger = LoggerFactory.getLogger(UserServiceImp::class.java)
    override fun register(body: ReqBody): BaseResult {
        var u= body.phone?.let { userrepository.findByPhone(it) }
        logger.debug(u.toString())
        return if (u!=null){
            BaseResult.FAIL("此号码已经注册请直接登录")
        }else{
            val user =User()
            user.account = body.phone
            user.phone = body.phone
            user.password = body.password
            user.token = TokenService.getToken(user)
            userrepository.save(user)
            var s = CopierUtil.copyProperties(user,UserVo::class.java)
            logger.debug(s.toString())
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
                var s = CopierUtil.copyProperties(u,UserVo::class.java)
                BaseResult.SECUESS(s)
            }else{
                BaseResult.FAIL("账号或密码错误")
            }
        }else{
            BaseResult.FAIL("请先注册账号")
        }
    }

    override fun findUserById(phone: String):User? {
        var u=  userrepository.findByPhone(phone)
        return u
    }

    override fun getuserinfo(body: ReqBody): BaseResult {
        val u= body.id?.let { userrepository.findUserById(it) }
        return  BaseResult.SECUESS(u)
    }



    override fun updateUser(body: ReqBody): BaseResult {
        val u= body.id?.let { userrepository.findUserById(it) }
        if (u != null){
            u.icon = body.icon
            u.fances = body.fances
            u.likestarts = body.likestarts
            u.nickname = body.nickname
            u.relasename = body.relasename
//            u.phone = body.phone
            u.account = body.account
            userrepository.save(u)
            var s = CopierUtil.copyProperties(u,UserVo::class.java)
            return  BaseResult.SECUESS(s)
        }else{
            return BaseResult.FAIL("该用户不存在")
        }
    }

    override fun updateImages(id: Int,uploadType:String, files:List<MultipartFile?>? ):BaseResult {
        val u=  userrepository.findUserById(id)
        if (null !=u && !files.isNullOrEmpty()){

            var list:ArrayList<String>? = null
            for (file in files ) {
               var url =AliYunOssUtil.UploadToAliyun(file!!.name,file.inputStream,file.contentType ?: "jpg",uploadType,id.toString())
                list?.add(url)
            }
         return BaseResult.SECUESS(list)

        }else{
            return  BaseResult.FAIL("该用户不存在")
        }
    }

    override fun test() : BaseResult{
        var list:List<User?>?=null

        list= userrepository.findAll()
        return BaseResult.SECUESS(list)
    }


}
