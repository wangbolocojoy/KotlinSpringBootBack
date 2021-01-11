package com.btm.back


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


/**
 * @Description:
 * @Author: hero
 * @Date: 2020-07-01
 * @EnableCaching  允许使用注解的方式进行缓存操作
 * @CacheConfig
 * 这个注解在类上使用，用来描述该类中所有方法使用的缓存名称。
 * @Cacheable
 * 这个注解一般加在查询方法上，表示将一个方法的返回值缓存起来，默认情况下，缓存的key就是方法的参数，缓存的value就是方法的返回值。
 * @CachePut
 * 这个注解一般加在更新方法上，当数据库中的数据更新后，缓存中的数据也要跟着更新，使用该注解，可以将方法的返回值自动更新到已经存在的key上。
 * @CacheEvict
 * 这个注解一般加在删除方法上，当数据库中的数据删除后，相关的缓存数据也要自动清除。
 *
 **/
@SpringBootApplication
//@EnableCaching
class BackApplication

//fun initjim(){
//    val imServerConfig = PropertyImServerConfigBuilder("config/jim.properties").build()
//    //初始化SSL;(开启SSL之前,你要保证你有SSL证书哦...)
//    //初始化SSL;(开启SSL之前,你要保证你有SSL证书哦...)
//    initSsl(imServerConfig)
//    //设置群组监听器，非必须，根据需要自己选择性实现;
//    //设置群组监听器，非必须，根据需要自己选择性实现;
//    imServerConfig.imGroupListener = ImDemoGroupListener()
//    //设置绑定用户监听器，非必须，根据需要自己选择性实现;
//    //设置绑定用户监听器，非必须，根据需要自己选择性实现;
//    imServerConfig.imUserListener = ImDemoUserListener()
//    val jimServer = JimServer(imServerConfig)
//
//    /*****************start 以下处理器根据业务需要自行添加与扩展，每个Command都可以添加扩展,此处为demo中处理**********************************/
//
//    /*****************start 以下处理器根据业务需要自行添加与扩展，每个Command都可以添加扩展,此处为demo中处理 */
//    val handshakeReqHandler = CommandManager.getCommand(Command.COMMAND_HANDSHAKE_REQ, HandshakeReqHandler::class.java)
//    //添加自定义握手处理器;
//    //添加自定义握手处理器;
//    handshakeReqHandler.addMultiProtocolProcessor(DemoWsHandshakeProcessor())
//    val loginReqHandler = CommandManager.getCommand(Command.COMMAND_LOGIN_REQ, LoginReqHandler::class.java)
//    //添加登录业务处理器;
//    //添加登录业务处理器;
//    loginReqHandler.singleProcessor = LoginServiceProcessor()
//    //添加用户业务聊天记录处理器，用户自己继承抽象类BaseAsyncChatMessageProcessor即可，以下为内置默认的处理器！
//    //添加用户业务聊天记录处理器，用户自己继承抽象类BaseAsyncChatMessageProcessor即可，以下为内置默认的处理器！
//    val chatReqHandler = CommandManager.getCommand(Command.COMMAND_CHAT_REQ, ChatReqHandler::class.java)
//    chatReqHandler.singleProcessor = DefaultAsyncChatMessageProcessor()
//    /*****************end *******************************************************************************************/
//    /*****************end  */
//    jimServer.start()
//}
//@Throws(Exception::class)
//fun initSsl(imServerConfig: ImServerConfig) { //开启SSL
//    if (ImServerConfig.ON == imServerConfig.isSSL) {
//        val keyStorePath = PropUtil.get("jim.key.store.path")
//        val keyStorePwd = PropUtil.get("jim.key.store.pwd")
//        if (StringUtils.isNotBlank(keyStorePath) && StringUtils.isNotBlank(keyStorePath)) {
//            val sslConfig = SslConfig.forServer(keyStorePath, keyStorePath, keyStorePwd)
//            imServerConfig.sslConfig = sslConfig
//        }
//    }
//}
fun main(args: Array<String>) {

    runApplication<BackApplication>(*args)
//    initjim()
}





