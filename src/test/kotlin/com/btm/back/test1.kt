package com.btm.back

import kotlin.random.Random

object test1 {
    @JvmStatic
    fun main(args: Array<String>) { //
              val code = Random.nextInt(999999).toString()
        System.out.print(code)
    }
}
