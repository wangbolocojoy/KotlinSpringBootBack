package com.btm.back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

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

fun main(args: Array<String>) {
    runApplication<BackApplication>(*args)
}





