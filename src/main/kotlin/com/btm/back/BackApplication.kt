package com.btm.back

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.annotation.PreDestroy
import javax.sql.DataSource


@SpringBootApplication
class BackApplication {

    @Autowired
    private lateinit var dataSource: DataSource
    private val logger: Logger = LoggerFactory.getLogger(BackApplication::class.java)
    @PreDestroy
    fun onDestroy() {

        logger.info("应用关闭，释放资源...")
        // 手动关闭连接池（通常不需要）
    }

//    @Bean(destroyMethod = "close")
//    fun dataSource(): DataSource? {
//        // 初始化DataSource
//
//    }
}

fun main(args: Array<String>) {
    runApplication<BackApplication>(*args)
}
