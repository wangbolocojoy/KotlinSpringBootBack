package com.btm.back.utils

import GsonUtil
import com.alibaba.fastjson.JSON
import com.aliyun.oss.*
import com.aliyun.oss.common.auth.CredentialsProviderFactory
import com.aliyun.oss.common.comm.SignVersion
import com.aliyun.oss.model.ObjectMetadata
import com.aliyun.oss.model.PutObjectRequest
import com.aliyuncs.AcsResponse
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.IAcsClient
import com.aliyuncs.RpcAcsRequest
import com.aliyuncs.auth.ICredentialProvider
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.exceptions.ServerException
import com.aliyuncs.imageaudit.model.v20191230.ScanImageRequest
import com.aliyuncs.imageaudit.model.v20191230.ScanImageRequest.Task
import com.aliyuncs.imageaudit.model.v20191230.ScanImageResponse
import com.aliyuncs.imageaudit.model.v20191230.ScanTextRequest
import com.aliyuncs.imageaudit.model.v20191230.ScanTextResponse
import com.aliyuncs.ocr.model.v20191230.RecognizeIdentityCardRequest
import com.aliyuncs.ocr.model.v20191230.RecognizeIdentityCardResponse
import com.aliyuncs.profile.DefaultProfile
import com.btm.back.bean.CheckImageModel
import com.btm.back.bean.ContextModel
import com.btm.back.bean.IdCardModel
import com.btm.back.dto.UserFiles
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.UserFilesRespository
import com.btm.back.vo.UserFilesVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * 阿里云OSS工具类，提供文件上传、删除、内容审核等功能
 */
object AliYunOssUtil {
    private val logger: Logger = LoggerFactory.getLogger(AliYunOssUtil::class.java)
    private var client: IAcsClient? = null

    /**
     * 创建OSS客户端
     * 使用新的OSS客户端创建方式，支持V4签名
     */

    private fun createOssClient(): OSS? {
            return try {
            val endpoint = "https://oss-cn-chengdu.aliyuncs.com"
            val region = "cn-chengdu"
            val credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider()
            val clientConfig = ClientBuilderConfiguration().apply {
                signatureVersion = SignVersion.V4
            }

            OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientConfig)
                .region(region)
                .build()
        } catch (e: Exception) {
            logger.error("创建OSS客户端失败：${e.message}")
            logger.info("创建OSS客户端失败：${e.message}")
            e.printStackTrace()
            null
        }

    }



    /**
     * 创建并配置ObjectMetadata
     */
    private fun createObjectMetadata(contentLength: Long, contentType: String, fileName: String): ObjectMetadata {
        return ObjectMetadata().apply {
            this.contentLength = contentLength
            this.cacheControl = "no-cache" // 设置不缓存
            setHeader("Pragma", "no-cache") // 设置页面不缓存
            this.contentType = contentType
            this.contentDisposition = "inline;filename=$fileName"
        }
    }

    /**
     * 上传文件到阿里云并生成URL
     */
    fun uploadToAliyun(pictureName: String, inputStream: InputStream?, suffix: String, type: String, user: String): String {
        logger.info("------------>文件名称为: $pictureName.$suffix")
        val ossClient = createOssClient()
        
        return try {
            inputStream?.use { stream ->
                val objectMetadata = createObjectMetadata(
                    contentLength = stream.available().toLong(),
                    contentType = getcontentType(suffix),
                    fileName = "$pictureName.$suffix"
                )
                var imagekey=generateUniqueId()
                ossClient?.putObject(OSSClientConstants.BACKET_NAME, imagekey, stream, objectMetadata)
                logger.info("imagekey"+imagekey)
                val url = URL(OSSClientConstants.OSSURL+"/"+imagekey)
                checkScanImage(url.toString())
                logger.info("save - success - pictureUrl -> $url")
               imagekey
            } ?: ""
        } catch (e: IOException) {
            logger.error("上传文件失败", e)
            ""
        } finally {
            ossClient?.shutdown()
        }
    }

    /**
     * 上传文件到阿里云上海区域并生成URL
     */
    fun uploadToAliyunSH(pictureName: String, inputStream: InputStream?, suffix: String, type: String, user: String): String {
        logger.info("------------>文件名称为: $pictureName.$suffix")
        val ossClient = createOssClient()
        
        return try {
            inputStream?.use { stream ->
                val objectMetadata = createObjectMetadata(
                    contentLength = stream.available().toLong(),
                    contentType = getcontentType(suffix),
                    fileName = "$pictureName.$suffix"
                )

                var imagekey=generateUniqueId()
                ossClient?.putObject(OSSClientConstants.BACKET_NAME, imagekey, stream, objectMetadata)
                val url = URL(OSSClientConstants.OSSURL+"/"+imagekey)
                logger.info("save - success - pictureUrl -> $url")
                if (checkScanImage(url.toString()))
                     imagekey
                else{
                   "图片内容违规"
                }

            } ?: "上传文件失败 请稍后重试"
        } catch (e: IOException) {
            logger.error("上传文件失败", e)
            ""
        } finally {
            ossClient?.shutdown()
        }
    }

    /**
     * 上传多个文件到阿里云并保存到数据库
     */
    fun uploadToAliyunFiles(
        id: Int,
        postid: Int,
        userFilesRespository: UserFilesRespository,
        uploadFiles: ArrayList<MultipartFile>?,
        userid: String
    ): ArrayList<UserFilesVO> {
        val ossClient = createOssClient()
        val resultList = ArrayList<UserFilesVO>()
        
        try {
            uploadFiles?.forEachIndexed { index, file ->
                if (index <= 9) { // 限制最多上传10个文件
                    try {
                        file.inputStream.use { stream ->
                            val objectMetadata = createObjectMetadata(
                                contentLength = stream.available().toLong(),
                                contentType = getcontentType(file.contentType ?: "jpg"),
                                fileName = "${file.originalFilename}${file.contentType}"
                            )

                            var imagekey=generateUniqueId()
                            ossClient?.putObject(OSSClientConstants.BACKET_NAME, imagekey, stream, objectMetadata)
                            val url = URL(OSSClientConstants.OSSURL+"/"+imagekey)
                            logger.info("save - success - pictureUrl -> $url")
                            
                            // 内容审核通过后保存到数据库
                            if (checkScanImage(url = url.toString())) {
                                val userFile = UserFiles().apply {
                                    originalFileName = file.originalFilename
                                    userId = id
                                    postId = postid
                                    fileType = file.contentType
                                    fileUrl = url.toString()
                                    fileLikes = 0
                                }
                                
                                userFilesRespository.save(userFile)
                                CopierUtil.copyProperties(userFile, UserFilesVO::class.java)?.let { resultList.add(it) }
                            }
                        }
                    } catch (e: IOException) {
                        logger.error("处理文件失败: ${file.originalFilename}", e)
                    }catch (oe: OSSException) {
                        logger.error("OSS异常: ${oe.errorMessage}, 错误码: ${oe.errorCode}, 请求ID: ${oe.requestId}")
                    } catch (ce: ClientException) {
                        logger.error("客户端异常: ${ce.message}")
                    } finally {
                        ossClient?.shutdown()
                    }
                }
            }
        } finally {
            ossClient?.shutdown()
        }
        
        return resultList
    }
    fun uploadImageToAliyunFiles(
    id: Int,
    postid: Int,
    userFilesRespository: UserFilesRespository,
    uploadFiles: ArrayList<MultipartFile>?,
    userid: String){
        val ossClient = createOssClient()
        val resultList = ArrayList<UserFilesVO>()
        try {
            uploadFiles?.forEachIndexed { index, file ->
                if (index <= 9) { // 限制最多上传10个文件
                    try {
                        file.inputStream.use { stream ->
                            val objectMetadata = createObjectMetadata(
                                contentLength = stream.available().toLong(),
                                contentType = getcontentType(file.contentType ?: "jpg"),
                                fileName = "${file.originalFilename}${file.contentType}"
                            )

                            var imagekey=generateUniqueId()
                            ossClient?.putObject(OSSClientConstants.BACKET_NAME, imagekey, stream, objectMetadata)
                            val url = URL(OSSClientConstants.OSSURL+"/"+imagekey)
                            logger.info("save - success - pictureUrl -> $url")

                            // 内容审核通过后保存到数据库
                            if (checkScanImage(url = url.toString())) {
                                val userFile = UserFiles().apply {
                                    originalFileName = file.originalFilename
                                    userId = id
                                    postId = postid
                                    fileType = file.contentType
                                    fileUrl = url.toString()
                                    fileLikes = 0
                                }

                                userFilesRespository.save(userFile)
                                CopierUtil.copyProperties(userFile, UserFilesVO::class.java)?.let { resultList.add(it) }
                            }
                        }
                    } catch (e: IOException) {
                        logger.error("处理文件失败: ${file.originalFilename}", e)
                    }
                }
            }
        }catch (e: IOException){
            logger.error("处理文件失败: ${e.message}", e)
        }catch (oe: OSSException) {
            logger.error("OSS异常: ${oe.errorMessage}, 错误码: ${oe.errorCode}, 请求ID: ${oe.requestId}")
        } catch (ce: ClientException) {
            logger.error("客户端异常: ${ce.message}")
        } finally {
            ossClient?.shutdown()
        }

    }

    /**
     * 根据key删除OSS服务器上的文件
     */
    fun deleteFile(key: String, id: String) {
        val ossClient = createOssClient()
        
        try {
            val fileKey = OSSClientConstants.PICTURE + id + "/" + key
            ossClient?.deleteObject(OSSClientConstants.BACKET_NAME, fileKey)
            logger.info("删除--> $fileKey <---成功")
        } catch (oe: OSSException) {
            logger.error("OSS异常: ${oe.errorMessage}, 错误码: ${oe.errorCode}, 请求ID: ${oe.requestId}")
        } catch (ce: ClientException) {
            logger.error("客户端异常: ${ce.message}")
        } finally {
            ossClient?.shutdown()
        }
    }



    /**
     * 批量删除文件
     */
    fun deleteFiles(id: String, files: List<UserFiles>?) {
        if (files.isNullOrEmpty()) return

        val ossClient = createOssClient()

        try {
            files.forEach { file ->
                val fileKey = OSSClientConstants.PICTURE + id + "/" + file.originalFileName
                ossClient?.deleteObject(OSSClientConstants.BACKET_NAME, fileKey)
                logger.info("删除--> $fileKey <---成功")
            }
        } catch (oe: OSSException) {
            logger.error("OSS异常: ${oe.errorMessage}, 错误码: ${oe.errorCode}, 请求ID: ${oe.requestId}")
        } catch (ce: ClientException) {
            logger.error("客户端异常: ${ce.message}")
        } finally {
            ossClient?.shutdown()
        }
    }



    /**
     * 判断OSS服务文件上传时文件的contentType
     */
    fun getcontentType(suffix: String): String {
        logger.info("------------>文件格式为: $suffix")
        return when {
            suffix.equals("bmp", ignoreCase = true) -> "image/bmp"
            suffix.equals("gif", ignoreCase = true) -> "image/gif"
            suffix.equals("jpeg", ignoreCase = true) || suffix.equals("jpg", ignoreCase = true) -> "image/jpeg"
            suffix.equals("png", ignoreCase = true) -> "image/png"
            suffix.equals("html", ignoreCase = true) -> "text/html"
            suffix.equals("txt", ignoreCase = true) -> "text/plain"
            suffix.equals("vsd", ignoreCase = true) -> "application/vnd.visio"
            suffix.equals("pptx", ignoreCase = true) || suffix.equals("ppt", ignoreCase = true) -> "application/vnd.ms-powerpoint"
            suffix.equals("docx", ignoreCase = true) || suffix.equals("doc", ignoreCase = true) -> "application/msword"
            suffix.equals("xml", ignoreCase = true) -> "text/xml"
            suffix.equals("mp3", ignoreCase = true) -> "audio/mp3"
            suffix.equals("mp4", ignoreCase = true) -> "audio/mp4"
            suffix.equals("amr", ignoreCase = true) -> "audio/amr"
            else -> "text/plain"
        }
    }

    /**
     * 图片内容检测
     * @return 是否违规
     */
    @Throws(Exception::class)
    fun checkScanImage(url: String): Boolean {
        logger.info("--------  内容审核 --------------")
        val req = ScanImageRequest().apply {
            scenes = mutableListOf("porn", "terrorism")
            tasks = mutableListOf(Task().apply {
                dataId = UUID.randomUUID().toString()
                imageURL = url
            })
        }
        
        val resp: ScanImageResponse = getAcsResponse(req)
        val json = GsonUtil.gsonToBean((JSON.toJSONString(resp)), CheckImageModel::class.java)
        var isValid = false
        
        logger.info(json.toString())
        if (json?.data?.results?.isNotEmpty() == true) {
            for (result in json.data!!.results!![0].subResults!!) {
                when (result.label) {
                    "normal" -> logger.info("检测类型${result.scene}正常图片")
                    "sexy" -> logger.info("检测类型${result.scene}性感图片")
                    "porn" -> logger.info("检测类型${result.scene}色情图片")
                    else -> logger.info("检测类型${result.scene}图片涉及政治敏感、暴力、武器、恐怖、血腥、爆炸等内容")
                }

                isValid = if (result.scene == "porn") {
                    // 检测色情图片
                    result.label != "porn"
                } else {
                    // 涉恐涉政识别
                    result.label == "normal"
                }
                
                if (isValid) {
                    logger.info("图片正常")
                } else {
                    logger.info("图片违规")
                    return false
                }
            }
        }
        return isValid
    }

    /**
     * 文本内容违规检测
     * @return 是否违规
     */
    @Throws(Exception::class)
    fun checkContext(context: String): Boolean {
        val request = ScanTextRequest().apply {
            regionId = "cn-chengdu"
            taskss = mutableListOf(ScanTextRequest.Tasks().apply { content = context })
            labelss = mutableListOf(
                ScanTextRequest.Labels().apply { label = "politics" },
                ScanTextRequest.Labels().apply { label = "abuse" },
                ScanTextRequest.Labels().apply { label = "terrorism" },
                ScanTextRequest.Labels().apply { label = "porn" }
            )
        }
        
        logger.info("开始文本识别内容---> $context")
        val resp: ScanTextResponse = getAcsResponse(request)
        logger.info("resp" + JSON.toJSON(resp))
        
        val json = GsonUtil.gsonToBean((JSON.toJSONString(resp)), ContextModel::class.java)
        logger.info("识别结果 ---> $json")
        
        var isValid = false
        if (json?.data != null && json.data.elements!!.isNotEmpty() && json.data.elements[0].results!!.isNotEmpty()) {
            for (result in json.data.elements[0].results!!) {
                isValid = result.suggestion == "pass"
                
                if (isValid) {
                    logger.info("文本内容正常")
                } else {
                    logger.info("文本内容违规")
                    return false
                }
            }
        } else {
            isValid = true
        }
        return isValid
    }

    /**
     * 识别身份证号码
     */
    @Throws(Exception::class)
    fun verificationCard(url: String, side: String): IdCardModel? {
        val request = RecognizeIdentityCardRequest().apply {
            imageURL = url
            this.side = side
        }
        
        if (side == "face") {
            logger.info("开始识别身份证人物面")
        } else {
            logger.info("开始识别身份证国徽面")
        }
        
        val response: RecognizeIdentityCardResponse = getAcsResponse(request)
        logger.info("识别结果response:$response")
        
        val json = GsonUtil.gsonToBean(JSON.toJSONString(response), IdCardModel::class.java)
        logger.info("识别结果json:$json")
        
        return json
    }

    /**
     * 调用阿里云内容安全sdk进行内容检测
     */
    @Throws(Exception::class)
    private fun <R : RpcAcsRequest<T>?, T : AcsResponse?> getAcsResponse(req: R): T {
        val profile = DefaultProfile.getProfile(
            "cn-chengdu"
        )
        client = DefaultAcsClient(profile)
        return try {
            client?.getAcsResponse(req)!!
        } catch (e: ServerException) { // 服务端异常
            logger.error("ServerException: errCode=${e.errCode}, errMsg=${e.errMsg}")
            throw e
        } catch (e: ClientException) { // 客户端错误
            logger.error("ClientException: errCode=${e.errCode}, errMsg=${e.errMsg}")
            throw e
        } catch (e: Exception) {
            logger.error("Exception: ${e.message}")
            throw e
        }
    }

    /**
     * 使用新的OSS客户端上传文件
     */
    fun updateFile(pictureName: String, inputStream: InputStream?, suffix: String, type: String, user: String): String {
        val ossClient = createOssClient()
        
        try {
            inputStream?.use { stream ->
                val content = "测试内容"
                val objectName = "$type/$user/$pictureName.$suffix"
                
                val putObjectRequest = PutObjectRequest(
                    OSSClientConstants.BACKET_NAME, 
                    objectName, 
                    ByteArrayInputStream(content.toByteArray())
                )
                
                val result = ossClient?.putObject(putObjectRequest)
                return result.toString()
            }
            return ""
        } catch (oe: OSSException) {
            logger.error("OSS异常: ${oe.errorMessage}, 错误码: ${oe.errorCode}, 请求ID: ${oe.requestId}")
        } catch (ce: ClientException) {
            logger.error("客户端异常: ${ce.message}")
        } finally {
            ossClient?.shutdown()
        }
        return ""
    }

    // 计数器，用于确保同一毫秒内生成的ID不重复
    private var counter = 0
    // 上次生成ID的时间戳
    private var lastTimestamp = -1L
    // 机器ID，可以根据实际情况设置，这里默认为1
    private const val MACHINE_ID = 1

    /**
     * 生成20位唯一ID
     * 组成：时间戳(13位) + 机器ID(2位) + 随机数(2位) + 计数器(3位)
     * @return 20位唯一ID字符串
     */
    @Synchronized
    fun generateUniqueId(): String {
        var timestamp = System.currentTimeMillis()

//        // 如果当前时间小于上次生成ID的时间戳，说明系统时钟回退过，抛出异常
//        if (timestamp < lastTimestamp) {
//            throw RuntimeException("Clock moved backwards. Refusing to generate id")
//        }

        // 如果是同一时间生成的，则进行计数器累加
        if (lastTimestamp == timestamp) {
            counter = (counter + 1) % 1000
            // 如果计数器溢出，则等待下一毫秒
            if (counter == 0) {
                timestamp = waitNextMillis(lastTimestamp)
            }
        } else {
            // 时间戳改变，计数器重置
            counter = 0
        }

        lastTimestamp = timestamp

        // 生成2位随机数
        val random = (Math.random() * 100).toInt()

        // 格式化各部分数据
        val timestampStr = timestamp.toString().padStart(13, '0')
        val machineIdStr = MACHINE_ID.toString().padStart(2, '0')
        val randomStr = random.toString().padStart(2, '0')
        val counterStr = counter.toString().padStart(3, '0')

        // 组合成20位唯一ID
        return timestampStr + machineIdStr + randomStr + counterStr +".jpg"
    }

    /**
     * 等待下一毫秒
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 下一毫秒的时间戳
     */
    private fun waitNextMillis(lastTimestamp: Long): Long {
        var timestamp = System.currentTimeMillis()
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis()
        }
        return timestamp
    }

}