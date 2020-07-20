package com.btm.back.im.listener

import com.alibaba.fastjson.JSONObject
import org.jim.core.ImChannelContext
import org.jim.core.exception.ImException
import org.jim.core.packets.User
import org.jim.server.listener.AbstractImUserListener
import org.slf4j.LoggerFactory

/**
 * @author WChao
 * @Desc
 * @date 2020-05-02 18:18
 */
class ImDemoUserListener : AbstractImUserListener() {
    @Throws(ImException::class)
    override fun doAfterBind(imChannelContext: ImChannelContext, user: User) {
        logger.info("绑定用户:{}", JSONObject.toJSONString(user))
    }

    @Throws(ImException::class)
    override fun doAfterUnbind(imChannelContext: ImChannelContext, user: User) {
        logger.info("解绑用户:{}", JSONObject.toJSONString(user))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ImDemoUserListener::class.java)
    }
}
