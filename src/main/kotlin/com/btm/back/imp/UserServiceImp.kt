package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.User
import com.btm.back.repository.UserRespository
import com.btm.back.service.UserService
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
            userrepository.save(user)
            BaseResult.SECUESS(user)
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
        logger.debug(body.toString())
        val u= body.phone?.let { userrepository.findByAccount(it) }

        return if(u!=null){
            if(u.phone == body.phone&&u.password==body.password){
                BaseResult.SECUESS(u)
            }else{
                BaseResult.FAIL("账号或密码错误",null)
            }
        }else{
            BaseResult.FAIL("请先注册账号",null)
        }
    }

    override fun test() : BaseResult{
        var list:List<User?>?=null

        list= userrepository.findAll()
        return BaseResult.SECUESS(list)
    }


}
