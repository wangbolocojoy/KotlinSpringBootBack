//package com.btm.back.utils
//import GsonUtil
//import com.aliyun.oss.*
//import com.aliyun.oss.common.auth.CredentialsProviderFactory
//import com.aliyun.oss.common.comm.SignVersion
//import com.aliyun.oss.model.ObjectMetadata
//import com.aliyun.oss.model.PutObjectRequest
//import com.aliyuncs.AcsResponse
//import com.aliyuncs.DefaultAcsClient
//import com.aliyuncs.IAcsClient
//import com.aliyuncs.RpcAcsRequest
//import com.aliyuncs.exceptions.ClientException
//import com.aliyuncs.exceptions.ServerException
//import com.aliyuncs.imageaudit.model.v20191230.ScanImageRequest
//import com.aliyuncs.imageaudit.model.v20191230.ScanImageRequest.Task
//import com.aliyuncs.imageaudit.model.v20191230.ScanImageResponse
//import com.aliyuncs.imageaudit.model.v20191230.ScanTextRequest
//import com.aliyuncs.imageaudit.model.v20191230.ScanTextResponse
//import com.aliyuncs.ocr.model.v20191230.RecognizeIdentityCardRequest
//import com.aliyuncs.ocr.model.v20191230.RecognizeIdentityCardResponse
//import com.aliyuncs.profile.DefaultProfile
//import com.btm.back.bean.CheckImageModel
//import com.btm.back.bean.ContextModel
//import com.btm.back.bean.IdCardModel
//import com.btm.back.dto.UserFiles
//import com.btm.back.helper.CopierUtil
//import com.btm.back.repository.UserFilesRespository
//import com.btm.back.vo.UserFilesVO
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.web.multipart.MultipartFile
//import java.io.ByteArrayInputStream
//import java.io.IOException
//import java.io.InputStream
//import java.net.URL
//import java.util.*
//
//
//object AliYunOssUtil {
//    public var ossClient: OSSClient? = null
//    private val logger: Logger = LoggerFactory.getLogger(AliYunOssUtil::class.java)
//    private var client: IAcsClient? = null
//
//    /**
//     * 上传文件到阿里云,并生成url
//     *
//     * @param filedir (key)文件名(不包括后缀)
//     * @param in      文件字节流
//     * @param suffix  文件后缀名
//     * @return String 生成的文件url
//     */
//    fun uploadToAliyun(pictureName: String, `in`: InputStream?, suffix: String, type: String, user: String): String {
//        logger.info("------------>文件名称为:  $pictureName.$suffix")
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        var url: URL? = null
//
//        var filepath = "home/picture/"
//        try {
//            val objectMetadata = ObjectMetadata()
//            objectMetadata.contentLength = `in`!!.available().toLong()
//            objectMetadata.cacheControl = "no-cache" //设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为:不经过本地缓存
//            objectMetadata.setHeader("Pragma", "no-cache") //设置页面不缓存
//            objectMetadata.contentType = getcontentType(suffix)
//            objectMetadata.contentDisposition = "inline;filename=$pictureName.$suffix"
//            filepath = if (type == "video") {
//                OSSClientConstants.VIDEO + createFolder(user) + "/"
//            } else {
//                OSSClientConstants.PICTURE + createFolder(user) + "/"
//            }
//            val fileoder = filepath + pictureName
//            // 上传文件
//            val putResult = ossClient!!.putObject(OSSClientConstants.BACKET_NAME, fileoder, `in`, objectMetadata)
////            //生成过去的url
////            url = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, fileoder, expiration)
//            url = URL("https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/$fileoder")
//            logger.info("save - success - pictureUrl -> $url")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            ossClient!!.shutdown()
//            try {
//                `in`?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return url.toString()
//    }
//
//    fun uploadToAliyunSH(pictureName: String, `in`: InputStream?, suffix: String, type: String, user: String): String {
//        logger.info("------------>文件名称为:  $pictureName.$suffix")
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        var url: URL? = null
//
//        var filepath = "home/picture/"
//        try {
//            val objectMetadata = ObjectMetadata()
//            objectMetadata.contentLength = `in`!!.available().toLong()
//            objectMetadata.cacheControl = "no-cache" //设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为:不经过本地缓存
//            objectMetadata.setHeader("Pragma", "no-cache") //设置页面不缓存
//            objectMetadata.contentType = getcontentType(suffix)
//            objectMetadata.contentDisposition = "inline;filename=$pictureName.$suffix"
//            filepath = if (type == "video") {
//                OSSClientConstants.VIDEO + createFolderSH(user) + "/"
//            } else {
//                OSSClientConstants.PICTURE + createFolderSH(user) + "/"
//            }
//            val fileoder = filepath + pictureName
//            // 上传文件
//            val putResult = ossClient!!.putObject(OSSClientConstants.BACKET_NAME, fileoder, `in`, objectMetadata)
////            //生成过去的url
////            url = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, fileoder, expiration)
//            url = URL("https://swiftktidcardinfo.oss-cn-shanghai.aliyuncs.com/$fileoder")
//            logger.info("save - success - pictureUrl -> $url")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            ossClient!!.shutdown()
//            try {
//                `in`?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return url.toString()
//    }
//
//    fun uploadToAliyunFiles(
//        id: Int,
//        postid: Int,
//        userFilesRespository: UserFilesRespository,
//        uploadFile: ArrayList<MultipartFile>?,
//        userid: String
//    ): ArrayList<UserFilesVO> {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        var url: URL?
//        var filepath: String?
//        var list = ArrayList<UserFilesVO>()
//        uploadFile?.forEachIndexed { index, multipartFile ->
//            if (index <= 9) {
//                try {
//                    val objectMetadata = ObjectMetadata()
//                    objectMetadata.contentLength = multipartFile.inputStream.available().toLong()
//                    objectMetadata.cacheControl = "no-cache" //设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为:不经过本地缓存
//                    objectMetadata.setHeader("Pragma", "no-cache") //设置页面不缓存
//                    objectMetadata.contentType = getcontentType(multipartFile.contentType ?: "jpg")
//                    objectMetadata.contentDisposition =
//                        "inline;filename=" + multipartFile.originalFilename + multipartFile.contentType
//                    filepath = if (multipartFile.contentType == "video") {
//                        OSSClientConstants.VIDEO + createFolder(userid) + "/"
//                    } else {
//                        OSSClientConstants.POSTPICTURE + createFolder(userid) + "/"
//                    }
//                    var fileoder = filepath + multipartFile.originalFilename
//                    // 上传文件
//                    val putResult = ossClient!!.putObject(
//                        OSSClientConstants.BACKET_NAME,
//                        fileoder,
//                        multipartFile.inputStream,
//                        objectMetadata
//                    )
////            //生成过去的url
////            url = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, fileoder, expiration)
//                    url = URL("https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/$fileoder")
//                    logger.info("save - success - pictureUrl -> $url")
//
//                    if (checkScanImage(url = url.toString())) {
//                        val file = UserFiles()
//                        file.originalFileName = multipartFile.originalFilename
//                        file.userId = id
//                        file.postId = postid
//                        file.fileType = multipartFile.contentType
//                        file.fileUrl = url.toString()
//                        file.fileLikes = 0
//                        val fvo = CopierUtil.copyProperties(file, UserFilesVO::class.java)
//                        fvo?.let { list.add(it) }
//                        userFilesRespository.save(file)
//                    }
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                } finally {
//                    ossClient!!.shutdown()
//                    try {
//                        multipartFile.inputStream.close()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//        }
//        return list
//    }
//
//
//    fun uploadToAliyunPhtots(
//        id: Int,
//        postid: Int,
//        userFilesRespository: UserFilesRespository,
//        uploadFile: ArrayList<MultipartFile>?,
//        userid: String
//    ): ArrayList<UserFilesVO> {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        var url: URL?
//        var filepath: String?
//        var list = ArrayList<UserFilesVO>()
//        uploadFile?.forEachIndexed { index, multipartFile ->
//            if (index <= 9) {
//                try {
//                    val objectMetadata = ObjectMetadata()
//                    objectMetadata.contentLength = multipartFile.inputStream.available().toLong()
//                    objectMetadata.cacheControl = "no-cache" //设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为:不经过本地缓存
//                    objectMetadata.setHeader("Pragma", "no-cache") //设置页面不缓存
//                    objectMetadata.contentType = getcontentType(multipartFile.contentType ?: "jpg")
//                    objectMetadata.contentDisposition =
//                        "inline;filename=" + multipartFile.originalFilename + multipartFile.contentType
//                    filepath = if (multipartFile.contentType == "video") {
//                        OSSClientConstants.VIDEO + createFolder(userid) + "/"
//                    } else {
//                        OSSClientConstants.POSTPICTURE + createFolder(userid) + "/"
//                    }
//                    var fileoder = filepath + multipartFile.originalFilename
//                    // 上传文件
//                    val putResult = ossClient!!.putObject(
//                        OSSClientConstants.BACKET_NAME,
//                        fileoder,
//                        multipartFile.inputStream,
//                        objectMetadata
//                    )
////            //生成过去的url
////            url = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, fileoder, expiration)
//                    url = URL("https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/$fileoder")
//                    logger.info("save - success - pictureUrl -> $url")
//
//                    if (checkScanImage(url = url.toString())) {
//                        val file = UserFiles()
//                        file.originalFileName = multipartFile.originalFilename
//                        file.userId = id
//                        file.postId = postid
//                        file.fileType = multipartFile.contentType
//                        file.fileUrl = url.toString()
//                        file.fileLikes = 0
//                        val fvo = CopierUtil.copyProperties(file, UserFilesVO::class.java)
//                        fvo?.let { list.add(it) }
//                        userFilesRespository.save(file)
//                    }
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                } finally {
//                    ossClient!!.shutdown()
//                    try {
//                        multipartFile.inputStream.close()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//        }
//        return list
//    }
//
//    /**
//     * 创建存储空间
//     * @param ossClient      OSS连接
//     * @param bucketName 存储空间
//     * @return
//     */
//    fun createBucketName(bucketName: String): String {
//        //存储空间
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//
//        val bucketNames = bucketName;
//        if (!ossClient!!.doesBucketExist(bucketName)) {
//            //创建存储空间
//            var bucket = ossClient!!.createBucket(bucketName)
//            logger.info("创建存储空间成功")
//            return bucket.getName()
//        }
//        return bucketNames
//    }
//
//    /**
//     * 删除存储空间buckName
//     * @param ossClient  oss对象
//     * @param bucketName  存储空间
//     */
//    fun deleteBucket(bucketName: String) {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//
//        ossClient!!.deleteBucket(bucketName)
//        logger.info("删除" + bucketName + "Bucket成功")
//    }
//
//    /**
//     * 创建模拟文件夹
//     * @param ossClient oss连接
//     * @param bucketName 存储空间
//     * @param folder   模拟文件夹名如"qj_nanjing/"
//     * @return  文件夹名
//     */
//    fun createFolder(folder: String): String {
//        //文件夹名
//        val keySuffixWithSlash = folder
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//
//        //判断文件夹是否存在，不存在则创建
//        if (!ossClient!!.doesObjectExist(OSSClientConstants.BACKET_NAME, keySuffixWithSlash)) {
//            //创建文件夹
//            ossClient!!.putObject(
//                OSSClientConstants.BACKET_NAME,
//                keySuffixWithSlash,
//                ByteArrayInputStream(byteArrayOf(0))
//            )
//            logger.info("创建文件夹成功")
//            //得到文件夹名
//            val objecta = ossClient!!.getObject(OSSClientConstants.BACKET_NAME, keySuffixWithSlash)
//            val fileDir = objecta.getKey()
//            return fileDir
//        }
//        return keySuffixWithSlash
//    }
//
//    fun createFolderSH(folder: String): String {
//        //文件夹名
//        val keySuffixWithSlash = folder
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//
//        //判断文件夹是否存在，不存在则创建
//        if (!ossClient!!.doesObjectExist(OSSClientConstants.BACKET_NAME, keySuffixWithSlash)) {
//            //创建文件夹
//            ossClient!!.putObject(
//                OSSClientConstants.BACKET_NAME,
//                keySuffixWithSlash,
//                ByteArrayInputStream(byteArrayOf(0))
//            )
//            logger.info("创建文件夹成功")
//            //得到文件夹名
//            val objecta = ossClient!!.getObject(OSSClientConstants.BACKET_NAME, keySuffixWithSlash)
//            val fileDir = objecta.getKey()
//            return fileDir
//        }
//        return keySuffixWithSlash
//    }
//
//    /**
//     * 根据key删除OSS服务器上的文件
//     * @param ossClient  oss连接
//     * @param bucketName  存储空间
//     * @param folder  模拟文件夹名 如"qj_nanjing/"
//     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
//     */
//    fun deleteFile(key: String, id: String) {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        ossClient!!.deleteObject(OSSClientConstants.BACKET_NAME, OSSClientConstants.PICTURE + id + "/" + key)
//        logger.info("删除-->  " + OSSClientConstants.PICTURE + "/" + id + "/" + key + "  <---成功")
//        ossClient!!.shutdown()
//    }
//
//    fun deleteFileSH(key: String, id: String) {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        ossClient!!.deleteObject(OSSClientConstants.BACKET_NAME, OSSClientConstants.PICTURE + id + "/" + key)
//        logger.info("删除-->  " + OSSClientConstants.PICTURE + "/" + id + "/" + key + "  <---成功")
//        ossClient!!.shutdown()
//    }
//
//    /**
//     * @Description: 批量删除文件
//     * @Param: 参数
//     * @return: 返回数据
//     * @Author: hero
//     * @Date: 2020-07-30
//     * @Time: 11:22
//     **/
//    fun deleteFiles(id: String, list: List<UserFiles>?) {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        list?.forEach {
//            ossClient!!.deleteObject(
//                OSSClientConstants.BACKET_NAME,
//                OSSClientConstants.PICTURE + id + "/" + it.originalFileName
//            )
//            logger.info("删除-->  " + OSSClientConstants.PICTURE + id + "/" + it.originalFileName + "  <---成功")
//        }
//        ossClient!!.shutdown()
//
//    }
//
//
//    /**
//     * 删除图片
//     *
//     * @param key
//     */
//    fun deletePicture(key: String) {
//        ossClient = OSSClient(
//            OSSClientConstants.ENDPOINT,
//            OSSClientConstants.ACCESS_KEY_ID,
//            OSSClientConstants.ACCESS_KEY_SECRET
//        )
//        ossClient!!.deleteObject(OSSClientConstants.BACKET_NAME, key)
//        logger.info("aliyundelete-------$key")
//        ossClient!!.shutdown()
//    }
//
//    /**
//     * Description: 判断OSS服务文件上传时文件的contentType
//     *
//     * @param suffix 文件后缀
//     * @return String HTTP Content-type
//     */
//    fun getcontentType(suffix: String): String {
//        logger.info("------------>文件格式为:  $suffix")
//        return if (suffix.equals("bmp", ignoreCase = true)) {
//            "image/bmp"
//        } else if (suffix.equals("gif", ignoreCase = true)) {
//            "image/gif"
//        } else if (suffix.equals("jpeg", ignoreCase = true) || suffix.equals("jpg", ignoreCase = true)) {
//            "image/jpeg"
//        } else if (suffix.equals("png", ignoreCase = true)) {
//            "image/png"
//        } else if (suffix.equals("html", ignoreCase = true)) {
//            "text/html"
//        } else if (suffix.equals("txt", ignoreCase = true)) {
//            "text/plain"
//        } else if (suffix.equals("vsd", ignoreCase = true)) {
//            "application/vnd.visio"
//        } else if (suffix.equals("pptx", ignoreCase = true) || suffix.equals("ppt", ignoreCase = true)) {
//            "application/vnd.ms-powerpoint"
//        } else if (suffix.equals("docx", ignoreCase = true) || suffix.equals("doc", ignoreCase = true)) {
//            "application/msword"
//        } else if (suffix.equals("xml", ignoreCase = true)) {
//            "text/xml"
//        } else if (suffix.equals("mp3", ignoreCase = true)) {
//            "audio/mp3"
//        } else if (suffix.equals("mp4", ignoreCase = true)) {
//            "audio/mp4"
//        } else if (suffix.equals("amr", ignoreCase = true)) {
//            "audio/amr"
//        } else {
//            "text/plain"
//        }
//    }
//
//    /**
//     * @Description: 图片内容检测
//     * @Param: 参数
//     * @return: 返回数据 是否违规
//     * @Author: hero
//     * @Date: 2020-07-30
//     * @Time: 11:21
//     **/
//    @Throws(Exception::class)
//    fun checkScanImage(url: String): Boolean {
//        println("--------  内容审核 --------------")
//        val req = ScanImageRequest()
//        val scenes: MutableList<String> = ArrayList()
//        scenes.add("porn")
//        scenes.add("terrorism")
//        req.scenes = scenes
//        val tasks: MutableList<Task> = ArrayList()
//        val task = Task()
//        task.dataId = UUID.randomUUID().toString()
//
//        task.imageURL = url
//        tasks.add(task)
//        req.tasks = tasks
//        val resp: ScanImageResponse = getAcsResponse(req)
//        val json = GsonUtil.gsonToBean((JSON.toJSONString(resp)), CheckImageModel::class.java)
//        var boolean: Boolean = false
//        logger.info(json.toString())
//        if (json?.data?.results?.size != 0) {
//            for (it in json?.data?.results!![0].subResults!!) {
//                when (it.label) {
//                    "normal" -> {
//                        logger.info("检测类型" + it.scene + "正常图片")
//                    }
//
//                    "sexy" -> {
//                        logger.info("检测类型" + it.scene + "性感图片")
//                    }
//
//                    "porn" -> {
//                        logger.info("检测类型" + it.scene + "色情图片")
//                    }
//
//                    else -> {
//                        logger.info("检测类型" + it.scene + "图片涉及政治敏感、暴力、武器、恐怖、血腥、爆炸等内容")
//                    }
//                }
//
//                boolean = if (it.scene == "porn") {
//                    //检测色情图片
//                    it.label != "porn"
//
//                } else {
//                    //涉恐涉政识别
//                    it.label == "normal"
//                }
//                if (boolean) {
//                    logger.info("图片正常")
//                } else {
//                    logger.info("图片违规")
//                    return false
//                }
//            }
//
//
//        }
//        return boolean
////        printResponse(req.sysActionName, resp.requestId, resp)
//
//    }
//
//    /**
//     * @Description: 文本内容违规检测
//     * @Param: 参数
//     * @return: 返回数据 是否违规
//     * @Author: hero
//     * @Date: 2020-07-30
//     * @Time: 11:20
//     **/
//    @Throws(Exception::class)
//    fun checkContext(context: String): Boolean {
//        val request = ScanTextRequest()
//        request.regionId = "cn-shanghai"
//        val tasksList: MutableList<ScanTextRequest.Tasks> = ArrayList()
//        val task = ScanTextRequest.Tasks()
//        task.content = context
//        tasksList.add(task)
//        request.taskss = tasksList
//        var lablist: MutableList<ScanTextRequest.Labels> = ArrayList()
//        val lab1 = ScanTextRequest.Labels()
//        lab1.label = "politics"
//        lablist.add(lab1)
//        val lab2 = ScanTextRequest.Labels()
//        lab2.label = "abuse"
//        lablist.add(lab2)
//        val lab3 = ScanTextRequest.Labels()
//        lab3.label = "terrorism"
//        lablist.add(lab3)
//        val lab4 = ScanTextRequest.Labels()
//        lab4.label = "porn"
//        lablist.add(lab4)
//        request.labelss = lablist
//        logger.info("开始文本识别内容---> $context")
//        val resp: ScanTextResponse = getAcsResponse(request)
//        logger.info("resp" + JSON.toJSON(resp))
//        val json = GsonUtil.gsonToBean((JSON.toJSONString(resp)), ContextModel::class.java)
//        logger.info("识别结果 ---> " + json.toString())
//        var boolean = false
//        if (json?.data != null && json.data.elements!!.isNotEmpty() && json.data.elements[0].results!!.isNotEmpty()) {
//            for (it in json.data.elements[0].results!!) {
//                boolean = it.suggestion == "pass"
//                if (boolean) {
//                    logger.info("文本内容正常")
//
//                } else {
//                    logger.info("文本内容违规")
//                    return false
//                }
//            }
//        } else {
//            boolean = true
//        }
//        return boolean
//
//
//    }
//
//    /**
//     * @Description: 识别身份证号码
//     * @Param: 参数
//     * @return: 返回数据
//     * @Author: hero
//     * @Date: 2020-07-30
//     * @Time: 11:49
//     **/
//    @Throws(Exception::class)
//    fun verificationCard(url: String, side: String): IdCardModel? {
//        val request = RecognizeIdentityCardRequest()
//        request.imageURL = url
//        request.side = side
//        if (side == "face") {
//            logger.info("开始识别身份证人物面")
//        } else {
//            logger.info("开始识别身份证国徽面")
//        }
//        val response: RecognizeIdentityCardResponse = getAcsResponse(request)
//        logger.info("识别结果response:$response")
//        val json = GsonUtil.gsonToBean(JSON.toJSONString(response), IdCardModel::class.java)
//        logger.info("识别结果json:$json")
//        return json
//    }
//
//
//    /**
//     * @Description: 调用阿里云内容安全sdk进行内容检测
//     * @Param: 参数
//     * @return: 返回数据
//     * @Author: hero
//     * @Date: 2020-07-30
//     * @Time: 11:22
//     **/
//    @Throws(Exception::class)
//    private fun <R : RpcAcsRequest<T>?, T : AcsResponse?> getAcsResponse(req: R): T {
//        val profile = DefaultProfile.getProfile(
//            "cn-shanghai",  //默认
//            OSSClientConstants.ACCESS_KEY_ID,  //您的AccessKeyID
//            OSSClientConstants.ACCESS_KEY_SECRET
//        ) //您的AccessKeySecret
//        client = DefaultAcsClient(profile)
//        return try {
//            client?.getAcsResponse(req)!!
//        } catch (e: ServerException) { // 服务端异常
//            logger.error(String.format("ServerException: errCode=%s, errMsg=%s", e.errCode, e.errMsg))
//            throw e
//        } catch (e: ClientException) { // 客户端错误
//            logger.error(String.format("ClientException: errCode=%s, errMsg=%s", e.errCode, e.errMsg))
//            throw e
//        } catch (e: java.lang.Exception) {
//            logger.error("Exception:" + e.message)
//            throw e
//        }
//    }
//
//    /**
//     * @Description: 调用阿里云内容安全sdk进行内容检测
//     * @Param: 参数
//     * @return: 返回数据
//     * @Author: hero
//     * @Date: 2020-07-30
//     * @Time: 11:22
//     **/
//    @Throws(Exception::class)
//    private fun <R : RpcAcsRequest<T>?, T : AcsResponse?> getAcsResponsesh(req: R): T {
//        val profile = DefaultProfile.getProfile(
//            "cn-shanghai",  //默认
//            OSSClientConstants.ACCESS_KEY_ID,  //您的AccessKeyID
//            OSSClientConstants.ACCESS_KEY_SECRET
//        ) //您的AccessKeySecret
//        client = DefaultAcsClient(profile)
//        return try {
//            client?.getAcsResponse(req)!!
//        } catch (e: ServerException) { // 服务端异常
//            logger.error(String.format("ServerException: errCode=%s, errMsg=%s", e.errCode, e.errMsg))
//            throw e
//        } catch (e: ClientException) { // 客户端错误
//            logger.error(String.format("ClientException: errCode=%s, errMsg=%s", e.errCode, e.errMsg))
//            throw e
//        } catch (e: java.lang.Exception) {
//            logger.error("Exception:" + e.message)
//            throw e
//        }
//    }
//
//    fun printResponse(actionName: String?, requestId: String?, data: AcsResponse?) {
//        println(
//            String.format(
//                "actionName=%s, requestId=%s, data=%s", actionName, requestId,
//                JSON.toJSONString(data)
//            )
//        )
//    }
//
//    fun updatefile(pictureName: String, `in`: InputStream?, suffix: String, type: String, user: String): String {
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        val endpoint = "https://oss-cn-chengdu.aliyuncs.com"
//        // 填写Endpoint对应的Region信息，例如cn-hangzhou。
//        // 填写Endpoint对应的Region信息，例如cn-hangzhou。
//        val region = "cn-chengdu"
//
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量。
//
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量。
//        val credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider()
//        val objectName = "exampledir/exampleobject.txt"
//        // 创建OSSClient实例。
//        // 创建OSSClient实例。
//        val clientBuilderConfiguration = ClientBuilderConfiguration()
//        // 显式声明使用 V4 签名算法
//        // 显式声明使用 V4 签名算法
//        clientBuilderConfiguration.signatureVersion = SignVersion.V4
//        val ossClient = OSSClientBuilder.create()
//            .endpoint(endpoint)
//            .credentialsProvider(credentialsProvider)
//            .clientConfiguration(clientBuilderConfiguration)
//            .region(region)
//            .build()
//        try {
//            // 填写字符串。
//            var content = "你啊dasd"
//            // 创建PutObjectRequest对象。
//            val putObjectRequest = PutObjectRequest("aicdicu", objectName, ByteArrayInputStream(content.toByteArray()))
//
//            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
//            // ObjectMetadata metadata = new ObjectMetadata();
//            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//            // metadata.setObjectAcl(CannedAccessControlList.Private);
//            // putObjectRequest.setMetadata(metadata);
//
//            // 上传字符串。
//            val result = ossClient!!.putObject(putObjectRequest)
//            return result.toString()
//        } catch (oe: OSSException) {
//            println(
//                "Caught an OSSException, which means your request made it to OSS, "
//                        + "but was rejected with an error response for some reason."
//            )
//            println("Error Message:" + oe.errorMessage)
//            println("Error Code:" + oe.errorCode)
//            println("Request ID:" + oe.requestId)
//            println("Host ID:" + oe.hostId)
//
//        } catch (ce: com.aliyun.oss.ClientException) {
//            println(
//                ("Caught an ClientException, which means the client encountered "
//                        + "a serious internal problem while trying to communicate with OSS, "
//                        + "such as not being able to access the network.")
//            )
//            println("Error Message:" + ce.message)
//        } finally {
//            ossClient?.shutdown()
//        }
//        return  ""
//    }
//
//
//
////    @JvmStatic
////    fun main(args: Array<String>) {
////        val params = "{}"
////      var json =   verificationCard(params)
////        logger.info(json.toString())
////    }
//
//}
//
//
//
