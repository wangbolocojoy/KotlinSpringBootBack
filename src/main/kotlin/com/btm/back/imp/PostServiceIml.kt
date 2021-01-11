package com.btm.back.imp

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.auth.sts.AssumeRoleRequest
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import com.aliyuncs.profile.IClientProfile
import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.bean.RestPostBody
import com.btm.back.dto.Post
import com.btm.back.dto.ReportPost
import com.btm.back.dto.UserFiles
import com.btm.back.helper.CopierUtil
import com.btm.back.helper.toCreatTimeString
import com.btm.back.repository.*
import com.btm.back.service.PostService
import com.btm.back.utils.*
import com.btm.back.vo.PostAuthorVo
import com.btm.back.vo.PostVO
import com.btm.back.vo.UserFilesVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Transactional
@Service
//@CacheConfig(keyGenerator = "keyGenerator") //这是本类统一key生成策略
class PostServiceIml:PostService{
    @Autowired
    lateinit var postRespository: PostRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var postStartRespository: PostStartRespository

    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var favoritesRespository: FavoritesRespository

    @Autowired
    lateinit var postMessageRespository: PostMessageRespository

    @Autowired
    lateinit var reportPostRespository: ReportPostRespository

    @Autowired
    lateinit var backInfoPlistRespository: BackInfoPlistRespository


    private val logger: Logger = LoggerFactory.getLogger(PostServiceIml::class.java)

    /**
    * @Description: 发帖
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:18
    **/
//    @CacheEvict(cacheNames = ["getPosts","getPostByUserId"],allEntries = true)
    override fun sendPost(body: PostBody): BaseResult {
        return if (body.userId != null) {
            if (AliYunOssUtil.checkContext( body.postDetail ?:"")){
                val post = Post()
                post.userId = body.userId
                post.postAddress = body.postAddress
                post.postDetail = body.postDetail
                post.postPublic = body.postPublic ?: true
                post.postStarts = body.postStart ?: 0
                post.longitude = body.longitude
                post.latitude = body.latitude
                post.postState = 1
                val smp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                post.creatTime = Date()
                post.postMessageNum = 0
                postRespository.save(post)
                val user = userRespository.findById(body.userId ?:0)
                logger.info("发送帖子----")
                user?.postNum =(user?.postNum?:0)+1
                user?.let { userRespository.save(it) }

                body.postimagelist?.forEach {
                  if(it.fileUrl?.let { it1 -> AliYunOssUtil.checkScanImage(it1) } == true){
                      val file = UserFiles()
                      file.fileUrl = it.fileUrl
                      file.userId = it.userId
                      file.originalFileName = it.originalFileName
                      file.postId = post.id
                      file.fileType = it.fileType
                      file.fileLikes = 0
                      userFilesRespository.save(file)
                  }else{
                      logger.info("图正不正常")
                  }

                }
                val s = CopierUtil.copyProperties(post,PostVO::class.java)

                logger.info("发布帖子成功$s")
                BaseResult.SECUESS(s)
            }else{
                BaseResult.FAIL("内容违规,请重新组织语言")
            }

        } else {
            BaseResult.FAIL("用户id不能为空")
        }

    }
    override fun getMyPosts(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = postRespository.findByUserIdOrderByCreatTimeDesc(body.userId ?:0,pageable)
        return if (list?.isEmpty == true){
            BaseResult.SECUESS("该用户暂时未发过帖子")
        }else{
            val images =ArrayList<PostVO>()
            val startList = postStartRespository.findByUserId(body.userId?:0)
            val collList = favoritesRespository.findByUserId(body.userId?:0)
            list?.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
                val poststartnum = postStartRespository.findByPostId(it.id?:0)
                val msgList = postMessageRespository.findByPostId(body.postId ?:0)
                val user =  userRespository.findById(it.userId ?: 0)
                var postAuth:PostAuthorVo? = null
                if (user!= null){
                    postAuth = CopierUtil.copyProperties(user,PostAuthorVo::class.java)
                }
                file?.map {it2 ->
                    val s = CopierUtil.copyProperties(it2,UserFilesVO::class.java)
                    s?.let { it3 -> listFvo.add(it3) }
                }
                val s =CopierUtil.copyProperties(it,PostVO::class.java)
                s?.postImages = listFvo
                s?.author = postAuth
                s?.msgNum = msgList?.size
                s?.postStarts = poststartnum?.size ?: 0
                s?.isStart = startList?.any { it5->
                    it5.postId == it.id
                }
                s?.isCollection = collList?.any { it6->
                    it6.postId == it.id
                }
                s?.creatTime = it.creatTime?.toCreatTimeString()
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取用户"+body.userId+"的"+images.size+"条")
            BaseResult.SECUESS(images)
        }
    }

    /**
    * @Description: 更新帖子状态
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-22
    * @Time: 20:43
    **/
    override fun updatePosts(body: PageBody): BaseResult {
        if ( body.postList?.isNotEmpty()!!){
            body.postList!!.forEach {
                var post = it.postId?.let { it1 -> postRespository.findById(it1) }
                if (post != null){
                    if (null!=it.postState && body.userId == 1){
                        post.postState = it.postState
                    }

                    if (null!= it.postPublic && body.userId == post.userId){
                        post.postPublic = it.postPublic
                    }
                    postRespository.save(post)
                }
            }
            logger.info("更新帖子"+body.postId+"成功")
            return BaseResult.SECUESS("更新成功")
        }else{
            return BaseResult.FAIL("传入要更新的数据")
        }

    }

    /**
    * @Description: 举报帖子
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-22
    * @Time: 20:01
    **/
    override fun reportPostByPostId(body: RestPostBody): BaseResult {
         if (body.userId == null|| body.postId == null){
             return BaseResult.FAIL("参数不能为空")
         }else{
             val post = body.postId?.let { postRespository.findById(it) }
             val reportPost = ReportPost()
             reportPost.postId = body.postId
             reportPost.userId = body.userId
             reportPost.reportDateTime = Date()
             reportPost.reportDescribe = body.reportDescribe
             reportPost.reportReason = body.reportReason
             reportPostRespository.save(reportPost)
             post?.postReport = post?.postReport?.plus(1)
             post?.let { postRespository.save(it) }
             logger.info("用户"+body.userId +"举报帖子"+body.postId+"成功")
             return BaseResult.SECUESS("举报成功")
         }

    }

    /**
    * @Description:  获取举报列表
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-22
    * @Time: 20:22
    **/
    override fun getReportList(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 3)
        val list = postRespository.findByPostReportGreaterThanOrderByCreatTimeDesc(postReport =  body.postReport ?:0, pageable = pageable)
       val pvoList = ArrayList<PostVO>()
        list?.forEach {
            val rep = it.id?.let { it1 -> reportPostRespository.findByPostId(it1) }
            val pvo = CopierUtil.copyProperties(it,PostVO::class.java)
            pvo?.postReports = rep
            pvo?.let { it1 -> pvoList.add(it1) }
        }
        return BaseResult.SECUESS()
    }

    /**
    * @Description:
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-07-22
    * @Time: 20:23
    **/
    override fun getExamineList(body: PageBody): BaseResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gettoken(): BaseResult {
        val endpoint = OSSClientConstants.STSSH
        val AccessKeyId = OSSClientConstants.ACCESS_KEY_IDSTS
        val accessKeySecret = OSSClientConstants.ACCESS_KEY_SECRETSTS
        val roleArn = "acs:ram::1032913586529687:role/appserver"
        val roleSessionName = "appserver"
        try { // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", endpoint)
            // 构造default profile（参数留空，无需添加region ID）
            val profile: IClientProfile = DefaultProfile.getProfile("", AccessKeyId, accessKeySecret)
            // 用profile构造client
            val client = DefaultAcsClient(profile)
            val request = AssumeRoleRequest()
            request.method = MethodType.POST
            request.roleArn = roleArn
            request.roleSessionName = roleSessionName
            request.durationSeconds = 1000L // 设置凭证有效时间
            val response = client.getAcsResponse(request)

            return BaseResult.SECUESS(response)
        } catch (e: ClientException) {
            println("Failed：")
            println("Error code: " + e.errCode)
            println("Error message: " + e.errMsg)
            println("RequestId: " + e.requestId)
        }
        return BaseResult.FAIL()
    }


    /**
    * @Description: 获取用户的帖子
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:18
    **/
    override fun getPostByUserId(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = postRespository.findByUserIdAndPostPublicAndPostStateOrderByCreatTimeDesc(userId = body.userId ?:0 ,postPublic = body.public ?: true,postState = body.postState ?:1, pageable = pageable)
        return if (list?.isEmpty() == true){
            BaseResult.SECUESS("该用户暂时未发过帖子")
        }else{
            val images =ArrayList<PostVO>()
            val startList = postStartRespository.findByUserId(body.userId?:0)
            val collList = favoritesRespository.findByUserId(body.userId?:0)
            list?.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
                val poststartnum = postStartRespository.findByPostId(it.id?:0)
                val msgList = postMessageRespository.findByPostId(body.postId ?:0)
                val user =  userRespository.findById(it.userId ?: 0)
                var postAuth:PostAuthorVo? = null
                if (user!= null){
                    postAuth = CopierUtil.copyProperties(user,PostAuthorVo::class.java)
                }
                file?.map {it2 ->
                    val s = CopierUtil.copyProperties(it2,UserFilesVO::class.java)
                    s?.let { it3 -> listFvo.add(it3) }
                }
                val s =CopierUtil.copyProperties(it,PostVO::class.java)
                s?.postImages = listFvo
                s?.author = postAuth
                s?.msgNum = msgList?.size
                s?.postStarts = poststartnum?.size ?: 0
                s?.isStart = startList?.any { it5->
                    it5.postId == it.id
                }
                s?.isCollection = collList?.any { it6->
                    it6.postId == it.id
                }
                s?.creatTime = it.creatTime?.toCreatTimeString()
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取用户"+body.userId+"的"+images.size+"条")
            BaseResult.SECUESS(images)
        }

    }

    /**
    * @Description: 根据状态分页获取公开的所有帖子
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:19
    **/
    override fun getPosts(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 3)
        val backlist = body.userId?.let { backInfoPlistRespository.findByUserId(it) }
        val listid = backlist?.map { it.backId ?: 0}
       val list = if (listid?.isNotEmpty()!!){
            postRespository.findByUserIdNotInAndPostPublicAndPostStateOrderByCreatTimeDesc(list = listid,postPublic = true,postState = body.postState ?:1, pageable = pageable)
        }else{
            postRespository.findByPostPublicAndPostStateOrderByCreatTimeDesc(postPublic = true,postState = body.postState ?:1, pageable = pageable)
        }
        return if (list.isNullOrEmpty()){
            BaseResult.SECUESS("暂时没有帖子")
        }else{
            val startList = postStartRespository.findByUserId(body.userId?:0)
            val collList = favoritesRespository.findByUserId(body.userId?:0)
            val images =ArrayList<PostVO>()
            list.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
                val msgList = postMessageRespository.findByPostId(it.id ?:0)
                val user =  userRespository.findById(it.userId ?: 0)
                var postAuth : PostAuthorVo? = null
                if (user!= null ){
                    postAuth = CopierUtil.copyProperties(user,PostAuthorVo::class.java)
                }
                 file?.map {it2 ->
                     val s = CopierUtil.copyProperties(it2,UserFilesVO::class.java)
                     s?.let { it3 -> listFvo.add(it3) }
                }
                val s =CopierUtil.copyProperties(it,PostVO::class.java)
                s?.author = postAuth
                s?.postImages = listFvo
                s?.msgNum = msgList?.size
                s?.isStart = startList?.any { it5->
                    it5.postId == it.id
                }
                s?.isCollection = collList?.any { it6->
                    it6.postId == it.id
                }
                s?.creatTime = it.creatTime?.toCreatTimeString()
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取帖子成功"+images.size+"条")
            BaseResult.SECUESS(images)
        }
    }

    /**
    * @Description: 用户删除帖子
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:19
    **/
    override fun deletePost(body: PageBody): BaseResult {
        val post = body.postId?.let { postRespository.findById(it) }
        return if (post != null&& body.userId != null){
            val starts = postStartRespository.findByPostId(body.postId?:0)
            starts?.let { postStartRespository.deleteAll(it) }
            val favs = favoritesRespository.findByPostId(body.postId?:0)
            favs?.let { favoritesRespository.deleteAll(it) }
            val list = userFilesRespository.findAllByPostId(body.postId ?:0)
            val msgs = postMessageRespository.findByPostId(body.postId ?:0)
            msgs?.let { postMessageRespository.deleteAll(it) }
            AliYunOssUtil.deleteFiles(body.userId.toString(),list)
            list?.let { userFilesRespository.deleteAll(it) }
            postRespository.delete(post)
            val user = userRespository.findById(body.userId ?: 0)
            if (user?.postNum?:0  >= 1){
                user?.postNum = (user?.postNum?:0) - 1
            }else{
                user?.postNum = 0
            }
            user?.let { userRespository.save(it) }
            logger.info("删除帖子成功")
            BaseResult.SECUESS("删除成功")
        }else{
            BaseResult.FAIL("该帖子不存在")
        }


    }

    /**
    * @Description: 更新帖子点赞数量
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:19
    **/
    override fun updatePostLikeStartt(body: PageBody): BaseResult {
        val user = body.userId?.let { userRespository.findById(it) }
        return if (user != null){
            val users = body.postId?.let { postStartRespository.findByPostId(it) }
            val has = users?.any { it.id == body.userId }
            BaseResult.FAIL()

        }else{
            BaseResult.FAIL("用户不存在")
        }
    }

    override fun isHaveNewPost(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 1)
        val post = postRespository.findByOrderByCreatTimeDesc(pageable)
        return if (post.isNullOrEmpty()){
            BaseResult.FAIL()
        }else{
            if (body.postId ?: 0 < post[0].id ?: 0){
                BaseResult.SECUESS()
            }else{
                BaseResult.FAIL()

            }
        }


    }


}
