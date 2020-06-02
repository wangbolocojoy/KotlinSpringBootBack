package com.btm.back.helper

import org.springframework.cglib.beans.BeanCopier

object BeanCopyHelper{
    fun <T,S>getbean(cls: Class<T>,clas1:Class<S>): BeanCopier {
        val bean = BeanCopier.create(cls::class.java, clas1::class.java,false)
        return   bean
    }



}
