package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.repository.NovelTypeRespository
import com.btm.back.service.NovelTypeService
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NovelTypeServiceImp : NovelTypeService {
    @Autowired
    lateinit var novelTypeRespository: NovelTypeRespository
    private val logger: Logger = LoggerFactory.getLogger(NovelTypeServiceImp::class.java)

    override fun getNovelTypes(body: ReqBody?): BaseResult {
        val list = novelTypeRespository.findAll()
        logger.debug(list.toString())
        return BaseResult(true, list)
    }

}
