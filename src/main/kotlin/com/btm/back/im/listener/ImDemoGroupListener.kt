package com.btm.back.im.listener

import org.jim.core.ImChannelContext
import org.jim.core.ImPacket
import org.jim.core.exception.ImException
import org.jim.core.packets.*
import org.jim.core.utils.JsonKit
import org.jim.server.JimServerAPI
import org.jim.server.listener.AbstractImGroupListener
import org.jim.server.protocol.ProtocolManager
import org.slf4j.LoggerFactory

/**
 * 群组绑定监听器
 * @author WChao
 * 2017年5月13日 下午10:38:36
 */
class ImDemoGroupListener : AbstractImGroupListener() {
    @Throws(ImException::class)
    override fun doAfterBind(imChannelContext: ImChannelContext, group: Group) {
        logger.info("群组:{},绑定成功!", JsonKit.toJSONString(group))
        val joinGroupRespBody = JoinGroupRespBody.success()
        //回一条消息，告诉对方进群结果
        joinGroupRespBody.setGroup(group.groupId)
        val respPacket = ProtocolManager.Converter.respPacket(joinGroupRespBody, imChannelContext)
        //Jim.send(imChannelContext, respPacket);
//发送进房间通知;
        joinGroupNotify(group, imChannelContext)
    }

    /**
     * @param imChannelContext
     * @param group
     * @throws Exception
     * @author: WChao
     */
    @Throws(ImException::class)
    override fun doAfterUnbind(imChannelContext: ImChannelContext, group: Group) { //发退出房间通知  COMMAND_EXIT_GROUP_NOTIFY_RESP
        val exitGroupNotifyRespBody = ExitGroupNotifyRespBody()
        exitGroupNotifyRespBody.group = group.groupId
        val clientUser = imChannelContext.sessionContext.imClientNode.user ?: return
        val notifyUser = User.newBuilder().userId(clientUser.userId).nick(clientUser.nick).build()
        exitGroupNotifyRespBody.user = notifyUser
        val respBody = RespBody(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, exitGroupNotifyRespBody)
        val imPacket = ImPacket(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, respBody.toByte())
        JimServerAPI.sendToGroup(group.groupId, imPacket)
    }

    /**
     * 发送进房间通知;
     * @param group 群组对象
     * @param imChannelContext
     */
    @Throws(ImException::class)
    fun joinGroupNotify(group: Group, imChannelContext: ImChannelContext) {
        val imSessionContext = imChannelContext.sessionContext
        val clientUser = imSessionContext.imClientNode.user
        val notifyUser = User.newBuilder().userId(clientUser.userId).nick(clientUser.nick).status(UserStatusType.ONLINE.status).build()
        val groupId = group.groupId
        //发进房间通知  COMMAND_JOIN_GROUP_NOTIFY_RESP
        val joinGroupNotifyRespBody = JoinGroupNotifyRespBody.success()
        joinGroupNotifyRespBody.setGroup(groupId).user = notifyUser
        JimServerAPI.sendToGroup(groupId, ProtocolManager.Converter.respPacket(joinGroupNotifyRespBody, imChannelContext))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ImDemoGroupListener::class.java)
    }
}
