package com.btm.back.helper

import org.springframework.cglib.beans.BeanCopier
import java.util.*
import java.util.stream.Collectors

object CopierUtil {
    /**
     * 单个对象属性拷贝
     * @param source 源对象
     * @param clazz 目标对象Class
     * @param <T> 目标对象类型
     * @param <M> 源对象类型
     * @return 目标对象
    </M></T> */
    fun <T, M : Any> copyProperties(source: M, clazz: Class<T>): T? {
        require(!(Objects.isNull(source) || Objects.isNull(clazz)))
        return copyProperties(source, clazz, null)
    }

    /**
     * 列表对象拷贝
     * @param sources 源列表
     * @param clazz 源列表对象Class
     * @param <T> 目标列表对象类型
     * @param <M> 源列表对象类型
     * @return 目标列表
    </M></T> */
    fun <T, M : Any> copyObjects(sources: List<M>, clazz: Class<T>): MutableList<T?>? {
        require(!(Objects.isNull(sources) || Objects.isNull(clazz) || sources.isEmpty()))
        val copier = BeanCopier.create(sources[0].javaClass, clazz, false)
        return Optional.of(sources)
                .orElse(ArrayList())
                .stream().map { m: M -> copyProperties(m, clazz, copier) }
                .collect(Collectors.toList())
    }

    /**
     * 单个对象属性拷贝
     * @param source 源对象
     * @param clazz 目标对象Class
     * @param copier copier
     * @param <T> 目标对象类型
     * @param <M> 源对象类型
     * @return 目标对象
    </M></T> */
    private fun <T, M : Any> copyProperties(source: M, clazz: Class<T>, copier: BeanCopier?): T? {
        var copier = copier
        if (null == copier) {
            copier = BeanCopier.create(source.javaClass, clazz, false)
        }
        var t: T? = null
        try {
            t = clazz.newInstance()
            copier!!.copy(source, t, null)
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return t
    }
}
