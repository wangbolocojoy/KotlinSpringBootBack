package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.*
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.*
import com.btm.back.service.UserService
import com.btm.back.utils.*
import com.btm.back.utils.RandomNickName.generateName
import com.btm.back.vo.AuthenticationVO
import com.btm.back.vo.FeedBackVO
import com.btm.back.vo.UserVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

@Transactional
@Service
//@CacheConfig(keyGenerator = "keyGenerator" ,cacheNames = ["UserServiceImpl"]) //这是本类统一key生成策略
/**
 * 用户服务实现类
 * 负责用户注册、登录、信息管理等操作
 * @author Trae AI
 * @date 2023-06-01
 */
class UserServiceImpl : UserService {
    @Autowired
    lateinit var userrepository: UserRespository

    @Autowired
    lateinit var backdinfoplistRespository: BackInfoPlistRespository

    @Autowired
    lateinit var developerRespository: DeveloperRespository

    @Autowired
    lateinit var followRespository: FollowRespository

    @Autowired
    lateinit var feedBackRespository: FeedBackRespository

    @Autowired
    lateinit var authenticationRespository: AuthenticationRespository

    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

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
                    user.isbanned = false
                    user.authentication = false
                    user.administrators = false
                    user.easyInfo = "我还没想好写什么"
                    user.nickName = generateName()
                    user.authentication = true
                    user.icon = "https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/home/picture/1/2020-07-31-00:19:34-0.png"
                    userrepository.save(user)
                    val s = CopierUtil.copyProperties(user,UserVO::class.java)
                    logger.info("注册成功---  "+s.toString())
                    val path = ""+s?.id + "/"
                    BaseResult.SUCCESS(s)
                }else{
                    BaseResult.FAIL("验证码错误")
                }
            }

        }
    }

    override fun sendmsg(body: ReqBody): BaseResult {
        logger.info(body.toString())
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
                val result: HashMap<String, Any> = phone?.let { RonglianConstants.instance.sendTemplateSMS(it,data) }!!
                if ("000000" == result["statusCode"]) { //正常返回输出data包体信息（map）

                    BaseResult.SUCCESS()
                } else { //异常返回输出错误码和错误信息
                    logger.error("错误码=" + result["statusCode"] + " 错误信息= " + result["statusMsg"])
                    BaseResult.FAIL(result["statusMsg"])
                }

            }
        }else{
           return if ( u != null){
                val code = Random.nextInt(100000,999999).toString()
                logger.info("验证码$code")
                val no = body.phone?.let { redisTemplate.opsForValue().set(it,code, Duration.ofMinutes(5)) }
               logger.info("redis储存验证码是否成功"+no)
                val phone = body.phone
                val data = arrayOf(code,"5")
                val result: HashMap<String, Any> = phone?.let { RonglianConstants.instance.sendTemplateSMS(it,data) }!!
                if ("000000" == result["statusCode"]) { //正常返回输出data包体信息（map）
                    BaseResult.SUCCESS()
                } else { //异常返回输出错误码和错误信息
                    logger.error("错误码=" + result["statusCode"] + " 错误信息= " + result["statusMsg"])
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
            BaseResult.SUCCESS("删除成功")
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
                logger.info("更新密码成功")
                BaseResult.SUCCESS(s)
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
                if (u.isbanned == true) BaseResult.FAIL("该账号已被封禁") else{
                    val s = CopierUtil.copyProperties(u,UserVO::class.java)
                    logger.info("登录成功---  "+s.toString())
                    BaseResult.SUCCESS(s)
                }
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
    override fun getuserinfo(body: ReqBody): BaseResult {
        val u= body.id?.let { userrepository.findById(it) }
          return if (u != null) {
              val folllist = body.id?.let { followRespository.findAllByUserId(it) }
              val fancelist = body.id?.let { followRespository.findAllByFollowId(it) }
              u.fances = fancelist?.size ?: 0
              u.follows = folllist?.size ?: 0
              userrepository.save(u)
              val  s = CopierUtil.copyProperties(u,UserVO::class.java)
            logger.info("获取用户信息成功--- "+s.toString())
            BaseResult.SUCCESS(s)
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
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                logger.info("更新用户信息成功$s")
              return  BaseResult.SUCCESS(s)
            }
            if (null !=body.nickName){
                 return if (AliYunOssUtil.checkContext(body.nickName ?:"")){
                    u.nickName = body.nickName
                    userrepository.save(u)
                    val s = CopierUtil.copyProperties(u,UserVO::class.java)
                    logger.info("更新用户信息成功$s")
                    BaseResult.SUCCESS(s)
                }else{
                    BaseResult.FAIL("内容违规,请重新组织语言")
                }

            }
            if (null !=body.realName){
                u.realName = body.realName
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                logger.info("更新用户信息成功$s")
                return BaseResult.SUCCESS(s)
            }
            if (null !=body.easyInfo){
                 return if (AliYunOssUtil.checkContext(body.easyInfo ?:"")){
                    u.easyInfo = body.easyInfo
                    userrepository.save(u)
                    val s = CopierUtil.copyProperties(u,UserVO::class.java)
                    BaseResult.SUCCESS(s)
                }else{
                    BaseResult.FAIL("内容违规,请重新组织语言")
                }

            }
            if (null !=body.address){
                u.address = body.address
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null !=body.fances){
                u.fances = body.fances
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null !=body.likeStarts){
                u.likeStarts = body.likeStarts
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null != body.postNum){
                u.postNum = body.postNum
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null != body.userSex){
                u.userSex = body.userSex
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null != body.birthDay){
                u.birthDay = body.birthDay
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null != body.constellation){
                u.constellation = body.constellation
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return  BaseResult.SUCCESS(s)
            }
            if (null != body.province){
                u.province = body.province
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return   BaseResult.SUCCESS(s)
            }
            if (null != body.city){
                u.city = body.city
                userrepository.save(u)
                val s = CopierUtil.copyProperties(u,UserVO::class.java)
                return   BaseResult.SUCCESS(s)
            }
            userrepository.save(u)
            val s = CopierUtil.copyProperties(u,UserVO::class.java)
            logger.info("更新用户信息成功$s")
            return  BaseResult.SUCCESS(s)

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
                if(AliYunOssUtil.checkScanImage(url)){
                    u.icon = url
                    u.originalFileName = uploadFile.originalFilename
                    AliYunOssUtil.deleteFile(oldfilename ?: "",id.toString())
                    userrepository.save(u)
                    val s = CopierUtil.copyProperties(u,UserVO::class.java)
                    logger.info("头像修改成功$s")
                    BaseResult.SUCCESS("头像修改成功",s)
                }else{
                    BaseResult.FAIL("检测图片中包含违规内容,请重新上传")
                }

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
       val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val user = userrepository.findByPhone(body.phone ?:"")
        val follow = followRespository.findByFollowId(body.id?:0,pageable)
        val f =follow?.any { user?.id == it.followId }
        user?.isFollow = f
        return if (user!= null){
            val s = CopierUtil.copyProperties(user, UserVO::class.java)
            logger.info("查找用户成功----"+s.toString())
            BaseResult.SUCCESS(s)
        }else{
            BaseResult.FAIL("用户不存在")
        }
    }

    override fun getDeveloperInfo(): BaseResult {
        var developer = developerRespository.findById(1)
        return BaseResult.SUCCESS(developer)
    }

    override fun sendFeedBack(body: ReqBody): BaseResult {
        return if (body.userId == null || body.feedMsg ==null){
            BaseResult.FAIL("参数不足")
        }else{
            val feedBack = FeedBack()
            feedBack.backTime = Date()
            feedBack.userId = body.userId
            feedBack.userMsg = body.feedMsg
            feedBackRespository.save(feedBack)
            logger.info("储存反馈信息成功")
            BaseResult.SUCCESS()
        }
    }

    override fun getFeedBack(body: ReqBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val feedBacks = feedBackRespository.findAll(pageable)
        val list = ArrayList<FeedBackVO>()
        feedBacks.forEach {
           val user = it.userId?.let { it1 -> userrepository.findById(it1) }
            val vo = CopierUtil.copyProperties(it,FeedBackVO::class.java)
            vo?.userNickName = user?.nickName
            vo?.userIcon = user?.icon
            vo?.let { it1 -> list.add(it1) }
        }
        logger.info("获取反馈列表"+list.size+"条")
        return  BaseResult.SUCCESS(list)
    }

    override fun getAllUser(body: ReqBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = userrepository.findByIsbannedFalse(isbanned = false,pageable = pageable)
        val listvo = ArrayList<UserVO>()
        list?.forEach {
            val vo = CopierUtil.copyProperties(it,UserVO::class.java)
            vo?.let { it1 -> listvo.add(it1) }
        }
        logger.info("分页获取全部用户"+listvo.size+"条")
        return BaseResult.SUCCESS(listvo)
    }

    override fun setUserBanned(body: ReqBody): BaseResult {
    TODO()
    }


    override fun relaseNameAuthen(userId: Int?,cardName:String?,cardNumber:String,uploadFile: ArrayList<MultipartFile>?):BaseResult {
      val user =userId?.let { userrepository.findById(it) }
       val  authen = user?.id?.let { authenticationRespository.findById(userId = it) }
        if (authen != null){
            return  BaseResult.FAIL("该账号已经实名认知")
        }else{
//            val param =
//            AliYunOssUtil.verificationCard()
            return  BaseResult.FAIL("该账号已经实名认知")
        }
    }

    override fun uploadIdCard(userId: Int?, uploadType: String, uploadFile: MultipartFile?):BaseResult {
        if (uploadFile == null) {return BaseResult.FAIL("身份证照片不能为空")}
        val u= userId?.let { userrepository.findById(it) } ?: return  BaseResult.FAIL("用户不存在")
        val url =AliYunOssUtil.uploadToAliyunSH(uploadFile.originalFilename ?: "",uploadFile.inputStream,uploadFile.contentType ?: "jpg","img",userId.toString())
        val model = AliYunOssUtil.verificationCard(url,uploadType)
        if (model?.data == null ){
            return BaseResult.FAIL("身份证识别错误,请重新上传")
        }else{
            logger.info("model--->"+model.toString())
            val authentication = userId.let { authenticationRespository.findByUserId(it) }
            if (authentication == null ){

                val authentica = Authentication()
                    authentica.userId = userId
                if (uploadType== "face"){
                    authentica.Address = model.data.frontResult?.address
                    authentica.Name = model.data.frontResult?.name
                    authentica.Nation = model.data.frontResult?.nationality
                    authentica.IdNum = model.data.frontResult?.iDNumber
                    authentica.Sex = model.data.frontResult?.gender
                    authentica.Birth = model.data.frontResult?.birthDate
                    authentica.FrontIdCard = url
                }else{
                    authentica.Authority = model.data.backResult?.issue
                    authentica.startDate = model.data.backResult?.startDate
                    authentica.endDate = model.data.backResult?.endDate
                    authentica.NationalIdCard = url
                }

                val vo = CopierUtil.copyProperties(authentica,AuthenticationVO::class.java)
                logger.info(vo.toString())
                vo?.authentication =  !(authentica.NationalIdCard.isNullOrEmpty() ||authentica.FrontIdCard.isNullOrEmpty())
                authentica.authentication =  vo?.authentication
                authenticationRespository.save(authentica)
                logger.info("保存用户身份信息"+vo.toString())
                return BaseResult.SUCCESS(vo)
            }else{
                if (uploadType== "face"){
                    authentication.Address = model.data.frontResult?.address
                    authentication.Name = model.data.frontResult?.name
                    authentication.Nation = model.data.frontResult?.nationality
                    authentication.IdNum = model.data.frontResult?.iDNumber
                    authentication.Sex = model.data.frontResult?.gender
                    authentication.Birth = model.data.frontResult?.birthDate
                    authentication.FrontIdCard = url
                }else{
                    authentication.Authority = model.data.backResult?.issue
                    authentication.startDate = model.data.backResult?.startDate
                    authentication.endDate = model.data.backResult?.endDate
                    authentication.NationalIdCard = url
                }
                u.authentication = !(authentication.NationalIdCard.isNullOrEmpty() ||authentication.FrontIdCard.isNullOrEmpty())
                userrepository.save(u)
                authentication.authentication = u.authentication
                authenticationRespository.save(authentication)
                val vo = CopierUtil.copyProperties(authentication,AuthenticationVO::class.java)
                vo?.authentication = u.authentication
                logger.info("保存用户身份信息"+vo.toString())
                return BaseResult.SUCCESS(vo)
            }
        }

    }

    override fun getIdCardInfo(body: ReqBody): BaseResult {
        val card = body.userId?.let { authenticationRespository.findByUserId(it) } ?: return BaseResult.FAIL("该用户没有实名认证信息")
        val cardvo = CopierUtil.copyProperties(card,AuthenticationVO::class.java)
        logger.info("获取用户身份信息"+cardvo.toString())
        return BaseResult.SUCCESS(cardvo)
    }

    override fun addBackInfoPlist(body: ReqBody): BaseResult {
        val back = body.userId?.let { backdinfoplistRespository.findByUserId(it) } ?: return BaseResult.FAIL("用户不存在")

        if (back.any { it.backId == body.backId }){
            return  BaseResult.FAIL("已经添加到黑名单")
        }else{
            val user = userrepository.findById(body.userId?:0)
            val fool = followRespository.findByUserIdAndFollowId(body.userId?:0,body.backId?:0)
            if (fool != null){
                user?.follows = user?.follows?.minus(1)
            }
            fool?.let { followRespository.delete(it) }
            val fance = followRespository.findByUserIdAndFollowId(body.backId?:0,body.userId?:0)
            if (fance != null){
                user?.fances = user?.fances?.minus(1)

            }
            user?.let { userrepository.save(it) }
            fance?.let { followRespository.delete(it) }
            val ds = BackInfoPlist()
            ds.backId = body.backId
            ds.userId = body.userId
            backdinfoplistRespository.save(ds)

            return  BaseResult.SUCCESS("添加到黑名单成功")
        }

    }

    override fun removeBackInfoPlist(body: ReqBody): BaseResult {
        val back = body.userId?.let { backdinfoplistRespository.findByUserIdAndBackId(it,body.backId?:0) }
        return if (back == null){
            BaseResult.FAIL("未加入黑名单")
        }else{
            backdinfoplistRespository.delete(back[0])
            BaseResult.SUCCESS("以移除黑名单")
        }
    }

    override fun getBackList(body: ReqBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val back = body.userId?.let { backdinfoplistRespository.findByUserId(it) } ?: return BaseResult.FAIL("用户不存在")
        val idlist=   back.map {  it.backId ?: 0}
        val userlist =  userrepository.findByIdIn(list = idlist,pageable = pageable)
        val volist = ArrayList<UserVO>()
        userlist?.forEach {
            val u = CopierUtil.copyProperties(it,UserVO::class.java)
            u?.let { it1 -> volist.add(it1) }
        }
        logger.info("获取黑名单列表成功"+volist.size+"条")
        return BaseResult.SUCCESS(volist)
    }


}
