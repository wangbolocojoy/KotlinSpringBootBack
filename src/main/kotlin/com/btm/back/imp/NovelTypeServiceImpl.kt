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
/**
 * 小说类型服务实现类
 * 负责小说类型的查询等操作
 * @author Trae AI
 * @date 2023-06-01
 */
class NovelTypeServiceImpl : NovelTypeService {
    @Autowired
    lateinit var novelTypeRespository: NovelTypeRespository
    private val logger: Logger = LoggerFactory.getLogger(NovelTypeServiceImpl::class.java)

    /**
     * 获取小说类型列表
     * @param body 请求体，可为空
     * @return 小说类型列表
     */
    override fun getNovelTypes(body: ReqBody?): BaseResult {
        val list = novelTypeRespository.findAll()
        return BaseResult.SUCCESS(list)
    }

}
