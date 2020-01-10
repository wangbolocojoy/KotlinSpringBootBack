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
        var u= userrepository.findByPhone(body.phone)
        logger.debug(u.toString())
        System.out.println(u.toString())
        if (u!=null){
            return BaseResult.FAIL("此号码已经注册请直接登录")
        }else{
            var user =User()
            user.account = body.phone
            user.phone = body.phone
            user.password = body.password
            userrepository.save(user)
            return BaseResult.SECUESS(user)
        }
    }

    override fun deleteUser(id: Long) :BaseResult{
        var u= userrepository.findById(id)
        System.out.println(u.toString())
        if(u.isEmpty){
            userrepository.deleteById(id)
            return BaseResult.SECUESS("删除成功")
        }else{
            return BaseResult.FAIL("未找到该用户")
        }


    }

    override fun login(body: ReqBody): BaseResult {
        logger.debug(body.toString())
        var u= userrepository.findByAccount(body.phone)

        logger.debug(u.toString())
        System.out.println(u.toString())
        if(u!=null){
            if(u.phone == body.phone&&u.password==body.password){
                return BaseResult.SECUESS(u)
            }else{
                return BaseResult.FAIL("账号或密码错误",null)
            }
        }else{
            return BaseResult.FAIL("请先注册账号",null)
        }
    }

    override fun test() : BaseResult{
        var list:List<User?>?=null

        list= userrepository.findAll()
        return BaseResult.SECUESS(list)
    }


}
