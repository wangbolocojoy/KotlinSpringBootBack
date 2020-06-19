package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.repository.FanceRespository
import com.btm.back.service.FanceService
import com.btm.back.utils.BaseResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FanceServiceImp :FanceService{
    @Autowired
    lateinit var fanceRespository: FanceRespository
    override fun getFanceList(body: ReqBody): BaseResult {
        val  fance = body.userId?.let { fanceRespository.findAllByUserId(it) }
        if (fance.isNullOrEmpty()){
            return BaseResult.FAIL("粉丝列表为空")
        }else{
            return  BaseResult.SECUESS(fance)
        }
    }

}
