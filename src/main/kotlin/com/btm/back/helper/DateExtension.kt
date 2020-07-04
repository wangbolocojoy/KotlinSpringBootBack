package com.btm.back.helper

import java.text.SimpleDateFormat
import java.util.*

fun String.toCreatTimeDate():Date{
    val dateForm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return  dateForm.parse(this)
}
fun String.toBirthdayDate():Date{
    val dateForm = SimpleDateFormat("yyyy-MM-dd")
    return dateForm.parse(this)
}
fun Date.toCreatTimeString():String{
    val dateForm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return  dateForm.format(this)
}
fun Date.toBirthdayString():String{
    val dateForm = SimpleDateFormat("yyyy-MM-dd")
    return dateForm.format(this)
}
