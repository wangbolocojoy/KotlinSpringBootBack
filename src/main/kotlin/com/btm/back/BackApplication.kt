package com.btm.back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.beans.factory.annotation.Autowired
import javax.sql.DataSource

@SpringBootApplication
class BackApplication {

    @Autowired
    private lateinit var dataSource: DataSource

}

fun main(args: Array<String>) {
    runApplication<BackApplication>(*args)
}