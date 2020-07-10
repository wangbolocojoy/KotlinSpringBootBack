package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.User
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.FollowRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.UserService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import com.btm.back.utils.RonglianConstants
import com.btm.back.utils.TokenService
import com.btm.back.vo.UserVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*
import kotlin.random.Random

@Transactional
@Service
//@CacheConfig(keyGenerator = "keyGenerator" ,cacheNames = ["UserServiceImp"]) //这是本类统一key生成策略
class UserServiceImp :UserService{
    @Autowired
    lateinit var userrepository: UserRespository
    @Autowired
    lateinit var followRespository: FollowRespository
    private val logger: Logger = LoggerFactory.getLogger(UserServiceImp::class.java)

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate
    /**
    * @Description: 注册
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:22
    **/
    override fun register(body: ReqBody): BaseResult {
        val u= body.phone?.let { userrepository.findByPhone(it) }
        return if (u!=null){
            BaseResult.FAIL("此号码已经注册请直接登录")
        }else{
            val code = body.phone?.let { redisTemplate.opsForValue().get(it) }
            if (code == null){
                BaseResult.FAIL("验证码以过期")
            }else{
                if (code == body.msgcode ?:""){
                    body.phone?.let { redisTemplate.delete(it) }
                    val user =User()
                    user.account = body.phone
                    user.phone = body.phone
                    user.password = body.password
                    user.token = TokenService.getToken(user)
                    user.likeStarts = 0
                    user.creatTime = Date()
                    user.userSex = false
                    userrepository.save(user)
                    val s = CopierUtil.copyProperties(user,UserVO::class.java)
                    logger.info("注册成功--- $s ")
                    BaseResult.SECUESS(s)
                }else{
                    BaseResult.FAIL("验证码错误")
                }
            }

        }
    }

    override fun sendmsg(body: ReqBody): BaseResult {
        val u= body.phone?.let { userrepository.findByPhone(it) }
        if (body.msgType == 1){
            return if (u!=null){
                BaseResult.FAIL("此号码已经注册请直接登录")
            }else{
                val code = Random.nextInt(100000,999999).toString()
                logger.info("验证码$code")
                val no = body.phone?.let { redisTemplate.opsForValue().set(it,code, Duration.ofMinutes(5)) }
                System.out.print("redis储存是否成功"+no)
                val phone = body.phone
                val data = arrayOf(code,"5")
                val result: HashMap<String, Any> = RonglianConstants.instance.sendTemplateSMS(phone,RonglianConstants.tempId,data)
                if ("000000" == result["statusCode"]) { //正常返回输出data包体信息（map）
                    BaseResult.SECUESS()
                } else { //异常返回输出错误码和错误信息
                    println("错误码=" + result["statusCode"] + " 错误信息= " + result["statusMsg"])
                    BaseResult.FAIL(result["statusMsg"])
                }

            }
        }else{
           return if ( u != null){
                val code = Random.nextInt(100000,999999).toString()
                logger.info("验证码$code")
                val no = body.phone?.let { redisTemplate.opsForValue().set(it,code, Duration.ofMinutes(5)) }
                System.out.print("redis储存是否成功"+no)
                val phone = body.phone
                val data = arrayOf(code,"5")
                val result: HashMap<String, Any> = RonglianConstants.instance.sendTemplateSMS(phone,RonglianConstants.tempId,data)
                if ("000000" == result["statusCode"]) { //正常返回输出data包体信息（map）
                    BaseResult.SECUESS()
                } else { //异常返回输出错误码和错误信息
                    println("错误码=" + result["statusCode"] + " 错误信息= " + result["statusMsg"])
                    BaseResult.FAIL(result["statusMsg"])
                }
            }else{
               BaseResult.FAIL("请先注册账号")
            }
        }

    }

    /**
    * @Description: 删除用户
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:22
    **/
    override fun deleteUser(id: Long) :BaseResult{
        val u= userrepository.findById(id)
        return if(u.isPresent){
            userrepository.deleteById(id)
            BaseResult.SECUESS("删除成功")
        }else{
            BaseResult.FAIL("未找到该用户")
        }


    }

    override fun updatePassWord(body: ReqBody): BaseResult {
        if (body.password == null){
            return  BaseResult.FAIL("密码不能为空")
        }
        val user = body.phone?.let { userrepository.findByPhone(it) }
        val code = body.phone?.let { redisTemplate.opsForValue().get(it) }
        return if (user != null ){
            if (body.msgcode == code){
                body.phone?.let { redisTemplate.delete(it) }
                user.password = body.password
                user.token = TokenService.getToken(user)
                userrepository.save(user)
                val s = CopierUtil.copyProperties(user,UserVO::class.java)
                BaseResult.SECUESS(s)
            }else {
                BaseResult.FAIL("验证码错误")
            }
        }else{
            BaseResult.FAIL("用户不存在")
        }
    }

    /**
    * @Description: 登录
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:23
    **/
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

    /**
    * @Description: 获取用户信息
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:24
    **/
    override fun findUserById(phone: String):User? {
        return userrepository.findByPhone(phone)
    }
    /**
     * @Description: 获取用户信息
     * @Param: 参数
     * @return: 返回数据
     * @Author: hero
     * @Date: 2020-06-26
     * @Time: 01:24
     **/
//    @Cacheable
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


    /**
     * @Description: 更新用户信息
     * @Param: 参数
     * @return: 返回数据
     * @Author: hero
     * @Date: 2020-06-26
     * @Time: 01:24
     **/
//    @CacheEvict(key = "#body.id")
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
            if (null != body.birthDay){
                u.birthDay = body.birthDay
            }
            if (null != body.constellation){
                u.constellation = body.constellation
            }
            if (null != body.province){
                u.province = body.province
            }
            if (null != body.city){
                u.city = body.city
            }

            userrepository.save(u)
            val s = CopierUtil.copyProperties(u,UserVO::class.java)
            logger.info("更新用户信息成功$s")
            return  BaseResult.SECUESS(s)
        }else{
            return BaseResult.FAIL("该用户不存在")
        }
    }


    /**
     * @Description: 更新用户头像
     * @Param: 参数
     * @return: 返回数据
     * @Author: hero
     * @Date: 2020-06-26
     * @Time: 01:24
     **/
//    @CacheEvict(key = "#id")
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



   /**
   * @Description: 查找用户
   * @Param: 参数
   * @return: 返回数据
   * @Author: hero
   * @Date: 2020-06-26
   * @Time: 01:26
   **/
//   @CachePut
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
