package com.btm.back.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 身份证合法性校验工具类
 * 18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 * @author Trae AI
 * @date 2023-06-01
 */
class IDCardUtil {
    // 第18位校检码
    private val verifyCode = arrayOf("1", "0", "X", "9", "8", "7", "6", "5",
            "4", "3", "2")

    /**
     * 18位身份证号码的基本数字和位数验校
     *
     * @param idcard 身份证号码
     * @return 是否合法
     */
    fun is18Idcard(idcard: String?): Boolean {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idcard)
    }

    companion object {
        // 每位加权因子
        private val power = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

        /**
         * 验证所有的身份证的合法性
         *
         * @param idcard 身份证号码
         * @return 是否合法
         */
        fun isValidatedAllIdcard(idcard: String?): Boolean {
            var idcard = idcard
            if (idcard!!.length == 15) {
                idcard = convertIdcarBy15bit(idcard)
            }
            return isValidate18Idcard(idcard)
        }

        /**
         * 将15位的身份证转成18位身份证
         *
         * @param idcard 15位身份证号码
         * @return 18位身份证号码
         */
        fun convertIdcarBy15bit(idcard: String): String {
            var idcard18 = ""
            if (idcard.length != 15) {
                return null!!
            }

            if (isDigital(idcard)) {
                // 获取出生年月日
                val birthday = idcard.substring(6, 12)
                val birthdate = Date()
                birthdate.year = Integer.parseInt("19" + birthday.substring(0, 2)) - 1900
                birthdate.month = Integer.parseInt(birthday.substring(2, 4)) - 1
                birthdate.date = Integer.parseInt(birthday.substring(4, 6))
                val cal = Calendar.getInstance()
                cal.time = birthdate
                // 获取出生年(完全表现形式,如：2010)
                val sYear = cal.get(Calendar.YEAR).toString()
                idcard18 = idcard.substring(0, 6) + sYear + idcard.substring(8)
                // 转换字符数组
                val cArr = idcard18.toCharArray()
                val iCard = IntArray(idcard18.length)
                var i = 0
                for (c in cArr) {
                    iCard[i] = c.toString().toInt()
                    i++
                }
                val iSum17 = getPowerSum(iCard)
                // 获取校验位
                val sVal = getCheckCode18(iSum17)
                idcard18 += sVal
            } else {
                return null!!
            }
            return idcard18
        }

        /**
         * 验证18位身份证的合法性
         *
         * @param idcard 身份证号码
         * @return 是否合法
         */
        fun isValidate18Idcard(idcard: String?): Boolean {
            // 非18位为假
            if (idcard == null || idcard.length != 18) {
                return false
            }
            // 获取前17位
            val idcard17 = idcard.substring(0, 17)
            // 获取第18位
            val idcard18Code = idcard.substring(17, 18)
            var c: Char
            var code: Int
            // 是否都为数字
            if (isDigital(idcard17)) {
                val iCard = IntArray(17)
                var i = 0
                for (element in idcard17) {
                    c = element
                    iCard[i] = c.toString().toInt()
                    i++
                }
                val iSum17 = getPowerSum(iCard)
                // 获取校验位
                val val_code = getCheckCode18(iSum17)
                if (idcard18Code.equals(val_code, ignoreCase = true)) {
                    return true
                }
            } else {
                return false
            }
            return false
        }

        /**
         * 数字验证
         *
         * @param str 待验证的字符串
         * @return 是否都是数字
         */
        fun isDigital(str: String): Boolean {
            return str.matches(Regex("^[0-9]*$"))
        }

        /**
         * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
         *
         * @param iArr 身份证号码的数字数组
         * @return 身份证编码
         */
        fun getPowerSum(iArr: IntArray): Int {
            var iSum = 0
            if (power.size == iArr.size) {
                for (i in iArr.indices) {
                    iSum += iArr[i] * power[i]
                }
            }
            return iSum
        }

        /**
         * 将power和值与11取模获得余数进行校验码判断
         *
         * @param iSum 加权和
         * @return 校验位
         */
        fun getCheckCode18(iSum: Int): String {
            var sCode = ""
            when (iSum % 11) {
                10 -> sCode = "2"
                9 -> sCode = "3"
                8 -> sCode = "4"
                7 -> sCode = "5"
                6 -> sCode = "6"
                5 -> sCode = "7"
                4 -> sCode = "8"
                3 -> sCode = "9"
                2 -> sCode = "x"
                1 -> sCode = "0"
                0 -> sCode = "1"
            }
            return sCode
        }
    }
}