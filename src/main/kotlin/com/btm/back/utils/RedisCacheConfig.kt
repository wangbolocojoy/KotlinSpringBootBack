package com.btm.back.utils

import com.alibaba.fastjson.JSONObject
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.*
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.lang.reflect.Method
import java.time.Duration
import java.util.*

/**
 * @Description:
 * @Param: 参数
 * @return: 返回数据
 * @Author: hero
 * @Date: 2020-07-01
 * @Time: 17:47
 */
@EnableCaching
@Configuration
class RedisCacheConfig : CachingConfigurerSupport() {
    /**
     * 缓存生成key的策略
     */
    @Bean
    override fun keyGenerator(): KeyGenerator? {
        return KeyGenerator { target: Any, method: Method, params: Array<Any?> ->
            val joiner = StringJoiner(":", keyPrefix, "")
            joiner.add(target.javaClass.simpleName)
            joiner.add(method.name)
            for (param in params) {
                joiner.add(JSONObject.toJSONString(param))
            }
            joiner.toString()
        }
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    fun redisTemplate(factory: LettuceConnectionFactory?): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(factory!!)
        redisTemplate.keySerializer = keySerializer()
        redisTemplate.hashKeySerializer = keySerializer()
        redisTemplate.valueSerializer = valueSerializer()
        redisTemplate.hashValueSerializer = valueSerializer()
        return redisTemplate
    }

    /**
     * 管理缓存
     */
    @Bean
    fun cacheManager(factory: LettuceConnectionFactory?): CacheManager { //初始化一个RedisCacheWriter输出流
        val redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory!!)
        //采用Jackson2JsonRedisSerializer序列化机制
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//创建一个RedisSerializationContext.SerializationPair给定的适配器pair
        val pair = SerializationPair.fromSerializer(valueSerializer())
        //创建CacheConfig
        return RedisCacheManager(redisCacheWriter, redisCacheConfiguration())
    }
    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        val objectMapper =
                ObjectMapper()
                        .registerModule(KotlinModule())
                        .registerModule(JavaTimeModule())
                        .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                        .enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, "@class")

        val serializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
    }
    /**
     * key序列化
     */
    private fun keySerializer(): RedisSerializer<String> {
        return StringRedisSerializer()
    }

    /**
     * value序列化
     */
    private fun valueSerializer(): RedisSerializer<Any> {
        val om = ObjectMapper()
                .registerModule(KotlinModule())
                .registerModule(JavaTimeModule())
                .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Any::class.java)
                        .build(), ObjectMapper.DefaultTyping.EVERYTHING)
        return GenericJackson2JsonRedisSerializer(om)
    }

    companion object {
        /**
         * @description TODO 缓存key前缀
         */
        private const val keyPrefix = "CACHE:"
    }
}
