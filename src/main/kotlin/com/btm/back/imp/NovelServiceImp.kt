package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.Novel
import com.btm.back.repository.NovelRespository
import com.btm.back.service.NovelService
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
@Service
class NovelServiceImp :NovelService{

    @Autowired
    lateinit var novelRespository : NovelRespository
    private val logger: Logger = LoggerFactory.getLogger(NovelServiceImp::class.java)
    override fun getPageNovelList(body: ReqBody?): BaseResult {
        val pageable: Pageable = PageRequest.of(body?.page ?: 1, body?.pagesize ?: 10)
        if(body?.type?:8 >= 8){
            val pages: Page<Novel> = novelRespository.findAll(pageable)
            val iterator: MutableIterator<Novel> = pages.iterator()
            logger.debug(iterator.toString())
            return BaseResult.SECUESS(iterator)
        }else{
            val pages: Page<Novel> = novelRespository.findAllByNovel_type(body?.type?:8,pageable)
            val iterator: MutableIterator<Novel> = pages.iterator()
            logger.debug(iterator.toString())
            return BaseResult.SECUESS(iterator)
        }



    }

    override fun searchNovel(body: String?): BaseResult {
        val novel = novelRespository.findByNovel_nameLike(body)
        logger.debug(novel.toString())
        if (novel.isNullOrEmpty()){
            return  BaseResult.FAIL("没有搜索到")
        }else{
            return  BaseResult.SECUESS(novel)
        }
    }

    override fun getallnoveltype(body: ReqBody?): BaseResult {
        val novel = novelRespository.findById(body!!.novelId)
        logger.debug(novel.toString())
        if (novel.isPresent){
            return  BaseResult.SECUESS(novel)
        }else{
            return  BaseResult.FAIL("获取类型失败")
        }
    }

}
