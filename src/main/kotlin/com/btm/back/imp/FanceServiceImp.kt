package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.repository.FanceRespository
import com.btm.back.service.FanceService
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FanceServiceImp :FanceService{
    @Autowired
    lateinit var fanceRespository: FanceRespository
    private val logger: Logger = LoggerFactory.getLogger(FanceServiceImp::class.java)
    /**
    * 获取粉丝列表
    * */
    override fun getFanceList(body: ReqBody): BaseResult {
        val  fance = body.id?.let { fanceRespository.findByUserId(it) }
        return if (fance.isNullOrEmpty()){
            logger.info("粉丝列表为空")
            BaseResult.FAIL("粉丝列表为空")
        }else{
            logger.info("粉丝列表$fance")
            BaseResult.SECUESS(fance)
        }
    }

}
