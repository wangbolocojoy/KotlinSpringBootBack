package com.btm.back.imp

import com.btm.back.bean.PhotoBody
import com.btm.back.bean.PhotoInfoBody
import com.btm.back.dto.PhotoInfo
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PhotoInfoRespository
import com.btm.back.repository.PhotoTypeRespository
import com.btm.back.service.PhotoInfoService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.PhotoInfoVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
/**
 * 照片信息服务实现类
 * 负责照片信息的查询、处理等操作
 * @author Trae AI
 * @date 2023-06-01
 */
class PhotoInfoServiceImpl: PhotoInfoService {
    @Autowired
    lateinit var photoInfoRespository: PhotoInfoRespository

    @Autowired
    lateinit var photoTypeRespository: PhotoTypeRespository




    override fun getPhotoByType(body: PhotoBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = photoInfoRespository.findByPhotoclassificationid(body.type?:1,pageable=pageable)
        return if (list?.isEmpty() == true){
            BaseResult.FAIL("暂时没有照片")
        }else{
            val photos =ArrayList<PhotoInfoVo>()
            list?.forEach{
                //dto转vo包装类
                val s = CopierUtil.copyProperties(it, PhotoInfoVo::class.java)
                s?.let { it1 -> photos.add(it1) }
            }
            BaseResult.SUCCESS(photos)
        }
    }

    override fun getAllPhotos(body: PhotoBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = photoInfoRespository.findAll(pageable)
        return if (list.isEmpty() == true){
            BaseResult.FAIL("暂时没有照片")
        }else{
            val photos =ArrayList<PhotoInfoVo>()
            list.forEach{
                //dto转vo包装类
                val s = CopierUtil.copyProperties(it, PhotoInfoVo::class.java)
                s?.let { it1 -> photos.add(it1) }
            }
            BaseResult.SUCCESS(photos)
        }
    }

    override fun getAllPhotoTypes(): BaseResult {
        val list = photoTypeRespository.findAll()
        return BaseResult.SUCCESS(list)
    }

    override fun addPhoto(body: PhotoInfoBody): BaseResult {
        var a1 = PhotoInfo()
        a1.Artist = body.Artist
        a1.Copyright = body.Copyright
        a1.ExposureTime = body.ExposureTime
        a1.ISOSpeedRatings = body.ISOSpeedRatings
        a1.Lensmodel = body.Lensmodel
        a1.Make = body.Make
        a1.Model = body.Model
        a1.Photoclassification = body.Photoclassification
        a1.Photoclassificationid = body.Photoclassificationid
        a1.Photoshootingtime = body.Photoshootingtime
        a1.Photosize = body.Photosize
        a1.Photourl = body.Photourl
        a1.ResolutionUnit = body.ResolutionUnit
        a1.aperture = body.aperture
        a1.focallength = body.focallength
        photoInfoRespository.save(a1)
        val s = CopierUtil.copyProperties(a1, PhotoInfoVo::class.java)
        return BaseResult.SUCCESS("数据库保存照片成功",s)
    }


}