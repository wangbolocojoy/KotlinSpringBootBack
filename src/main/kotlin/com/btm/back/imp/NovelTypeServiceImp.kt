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

    /**
    * @Description: 获取小说类型
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:25
    **/
    override fun getNovelTypes(body: ReqBody?): BaseResult {
        val list = novelTypeRespository.findAll()
        return BaseResult.SECUESS(list)
    }

}
