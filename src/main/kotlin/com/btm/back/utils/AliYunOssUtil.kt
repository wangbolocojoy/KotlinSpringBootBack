package com.btm.back.utils
import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.ObjectMetadata
import com.btm.back.dto.UserFiles
import com.btm.back.repository.UserFilesRespository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

object AliYunOssUtil {
    private var ossClient: OSSClient? = null
    private val logger: Logger = LoggerFactory.getLogger(AliYunOssUtil::class.java)
    /**
     * 上传文件到阿里云,并生成url
     *
     * @param filedir (key)文件名(不包括后缀)
     * @param in      文件字节流
     * @param suffix  文件后缀名
     * @return String 生成的文件url
     */
    fun uploadToAliyun(pictureName: String, `in`: InputStream?, suffix: String, type: String, user:String): String {
        logger.info("------------>文件名称为:  $pictureName.$suffix")
        ossClient = OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)
        var url: URL? = null

        var filepath = "home/picture/"
        try {
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = `in`!!.available().toLong()
            objectMetadata.cacheControl = "no-cache" //设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为:不经过本地缓存
            objectMetadata.setHeader("Pragma", "no-cache") //设置页面不缓存
            objectMetadata.contentType = getcontentType(suffix)
            objectMetadata.contentDisposition = "inline;filename=$pictureName.$suffix"
            filepath = if(type == "video"){
                OSSClientConstants.VIDEO+ createFolder(user)+"/"
            }else{
                OSSClientConstants.PICTURE+ createFolder(user)+"/"
            }
            var fileoder = filepath+pictureName
            // 上传文件
            val putResult= ossClient!!.putObject(OSSClientConstants.BACKET_NAME, fileoder, `in`, objectMetadata)
            logger.info("putResult---  $putResult")
            logger.info("putResult.eTag---" + putResult.eTag)
//            //生成过去的url
//            var expiration = Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10)
//            url = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, fileoder, expiration)
            url = URL("https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/$fileoder")
            logger.info("save - success - pictureUrl -> $url")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            ossClient!!.shutdown()
            try {
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return url.toString()
    }

    fun uploadToAliyunFiles(id:Int,postid:Int,userFilesRespository:UserFilesRespository,uploadFile: ArrayList<MultipartFile>? , userid:String): ArrayList<UserFiles> {
        ossClient = OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)
        var url: URL?
        var filepath :String?
        var list = ArrayList<UserFiles>()
        uploadFile?.forEachIndexed { index, multipartFile ->
           if (index <= 9) {
               try {
                   val objectMetadata = ObjectMetadata()
                   objectMetadata.contentLength = multipartFile.inputStream.available().toLong()
                   objectMetadata.cacheControl = "no-cache" //设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为:不经过本地缓存
                   objectMetadata.setHeader("Pragma", "no-cache") //设置页面不缓存
                   objectMetadata.contentType = getcontentType(multipartFile.contentType ?:"jpg")
                   objectMetadata.contentDisposition = "inline;filename="+multipartFile.originalFilename+multipartFile.contentType
                   filepath = if (multipartFile.contentType == "video") {
                       OSSClientConstants.VIDEO + createFolder(userid) + "/"
                   } else {
                       OSSClientConstants.PICTURE + createFolder(userid) + "/"
                   }
                   var fileoder = filepath + multipartFile.originalFilename
                   // 上传文件
                   val putResult = ossClient!!.putObject(OSSClientConstants.BACKET_NAME, fileoder, multipartFile.inputStream, objectMetadata)
                   logger.info("putResult.eTag --->     " + putResult.eTag)
//            //生成过去的url
//            var expiration = Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10)
//            url = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, fileoder, expiration)
                   url = URL("https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/$fileoder")
                   logger.info("save - success - pictureUrl -> $url")
                   val file = UserFiles()
                   file.originalFilename =  multipartFile.originalFilename
                   file.userid = id
                   file.postid = postid
                   file.filetype = multipartFile.contentType
                   file.fileurl = url.toString()
                   file.fileLikes = 0
                   file.fileseenum = 0
                   list.add(file)
                   userFilesRespository.save(file)
               } catch (e: IOException) {
                   e.printStackTrace()
               } finally {
                   ossClient!!.shutdown()
                   try {
                       multipartFile.inputStream.close()
                   } catch (e: IOException) {
                       e.printStackTrace()
                   }
               }

           }
       }
        return list
    }
    /**
     * 创建存储空间
     * @param ossClient      OSS连接
     * @param bucketName 存储空间
     * @return
     */
      fun  createBucketName( bucketName:String):String{
        //存储空间
       ossClient =OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)

        val  bucketNames=bucketName;
        if(!ossClient!!.doesBucketExist(bucketName)){
            //创建存储空间
            var bucket=ossClient!!.createBucket(bucketName)
            logger.info("创建存储空间成功")
            return bucket.getName()
        }
        return bucketNames
    }

    /**
     * 删除存储空间buckName
     * @param ossClient  oss对象
     * @param bucketName  存储空间
     */
    fun   deleteBucket(   bucketName:String){
       ossClient =OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)

        ossClient!!.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
      fun  createFolder( folder:String):String{
        //文件夹名
        val  keySuffixWithSlash =folder
         ossClient =OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)

        //判断文件夹是否存在，不存在则创建
        if(!ossClient!!.doesObjectExist(OSSClientConstants.BACKET_NAME, keySuffixWithSlash)){
            //创建文件夹
            ossClient!!.putObject(OSSClientConstants.BACKET_NAME, keySuffixWithSlash,  ByteArrayInputStream( byteArrayOf(0)))
            logger.info("创建文件夹成功")
            //得到文件夹名
            val objecta = ossClient!!.getObject(OSSClientConstants.BACKET_NAME, keySuffixWithSlash)
            val fileDir=objecta.getKey()
            return fileDir
        }
        return keySuffixWithSlash
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param ossClient  oss连接
     * @param bucketName  存储空间
     * @param folder  模拟文件夹名 如"qj_nanjing/"
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
     fun  deleteFile(    key:String ,id:String){
         ossClient =OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)
        ossClient!!.deleteObject(OSSClientConstants.BACKET_NAME, OSSClientConstants.PICTURE+id+"/" + key)
        logger.info("删除-->  " + OSSClientConstants.PICTURE+"/"+id+"/"+ key+ "  <---成功")
        ossClient!!.shutdown()
    }
    fun deleteFiles(id:String,list:List<UserFiles>?){
        ossClient =OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)
        list?.forEach {
            ossClient!!.deleteObject(OSSClientConstants.BACKET_NAME, OSSClientConstants.PICTURE+id+"/" + it.originalFilename)
            logger.info("删除-->  " + OSSClientConstants.PICTURE+"/"+id+"/"+ it.originalFilename + "  <---成功")
        }
        ossClient!!.shutdown()

    }





    /**
     * 删除图片
     *
     * @param key
     */
    fun deletePicture(key: String) {
        ossClient = OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)
        ossClient!!.deleteObject(OSSClientConstants.BACKET_NAME, key)
        logger.info("aliyundelete-------$key")
        ossClient!!.shutdown()
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param suffix 文件后缀
     * @return String HTTP Content-type
     */
    fun getcontentType(suffix: String): String {
        logger.info("------------>文件格式为:  $suffix")
        return if (suffix.equals("bmp", ignoreCase = true)) {
            "image/bmp"
        } else if (suffix.equals("gif", ignoreCase = true)) {
            "image/gif"
        } else if (suffix.equals("jpeg", ignoreCase = true) || suffix.equals("jpg", ignoreCase = true)) {
            "image/jpeg"
        } else if (suffix.equals("png", ignoreCase = true)) {
            "image/png"
        } else if (suffix.equals("html", ignoreCase = true)) {
            "text/html"
        } else if (suffix.equals("txt", ignoreCase = true)) {
            "text/plain"
        } else if (suffix.equals("vsd", ignoreCase = true)) {
            "application/vnd.visio"
        } else if (suffix.equals("pptx", ignoreCase = true) || suffix.equals("ppt", ignoreCase = true)) {
            "application/vnd.ms-powerpoint"
        } else if (suffix.equals("docx", ignoreCase = true) || suffix.equals("doc", ignoreCase = true)) {
            "application/msword"
        } else if (suffix.equals("xml", ignoreCase = true)) {
            "text/xml"
        } else if (suffix.equals("mp3", ignoreCase = true)) {
            "audio/mp3" }
        else if (suffix.equals("mp4", ignoreCase = true)) {
            "audio/mp4"
        } else if (suffix.equals("amr", ignoreCase = true)) {
            "audio/amr"
        } else {
            "text/plain"
        }
    }

    fun getimgurl(key: String?): String {
        val expiration = Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10)
        ossClient = OSSClient(OSSClientConstants.ENDPOINT, OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET)
        val imgurl = ossClient!!.generatePresignedUrl(OSSClientConstants.BACKET_NAME, key, expiration)
        return imgurl.toString()
    } //测试


}
