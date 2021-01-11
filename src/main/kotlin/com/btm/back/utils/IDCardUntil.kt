package com.btm.back.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 类说明:身份证合法性校验
 * 18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 * @author hero
 */
class IDCardUntil {
    // 第18位校检码
    private val verifyCode = arrayOf("1", "0", "X", "9", "8", "7", "6", "5",
            "4", "3", "2")

    /**
     * 18位身份证号码的基本数字和位数验校
     *
     * @param idcard
     * @return
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
         * @param idcard
         * @return
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
         * @param idcard
         * @return
         */
        fun convertIdcarBy15bit(idcard: String?): String? {
            var idcard17: String? = null
            // 非15位身份证
            if (idcard!!.length != 15) {
                return null
            }
            if (isDigital(idcard)) { // 获取出生年月日
                val birthday = idcard.substring(6, 12)
                var birthdate: Date? = null
                try {
                    birthdate = SimpleDateFormat("yyMMdd").parse(birthday)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val cday = Calendar.getInstance()
                cday.time = birthdate
                val year = cday[Calendar.YEAR].toString()
                idcard17 = idcard.substring(0, 6) + year + idcard.substring(8)
                val c = idcard17.toCharArray()
                var checkCode: String? = ""
                var bit = IntArray(idcard17.length)
                // 将字符数组转为整型数组
                bit = converCharToInt(c)
                var sum17 = 0
                sum17 = getPowerSum(bit)
                // 获取和值与11取模得到余数进行校验码
                checkCode = getCheckCodeBySum(sum17)
                // 获取不到校验位
                if (null == checkCode) {
                    return null
                }
                // 将前17位与第18位校验码拼接
                idcard17 += checkCode
            } else { // 身份证包含数字
                return null
            }
            return idcard17
        }

        /**
         * @param idcard
         * @return
         */
        fun isValidate18Idcard(idcard: String?): Boolean { // 非18位为假
            if (idcard!!.length != 18) {
                return false
            }
            // 获取前17位
            val idcard17 = idcard.substring(0, 17)
            // 获取第18位
            val idcard18Code = idcard.substring(17, 18)
            var c: CharArray? = null
            var checkCode: String? = ""
            // 是否都为数字
            c = if (isDigital(idcard17)) {
                idcard17.toCharArray()
            } else {
                return false
            }
            var bit = IntArray(idcard17.length)
            bit = converCharToInt(c)
            var sum17 = 0
            sum17 = getPowerSum(bit)
            // 将和值与11取模得到余数进行校验码判断
            checkCode = getCheckCodeBySum(sum17)
            if (null == checkCode) {
                return false
            }
            // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
            if (!idcard18Code.equals(checkCode, ignoreCase = true)) {
                return false
            }
            return true
        }

        /**
         * 数字验证
         *
         * @param str
         * @return
         */
        fun isDigital(str: String?): Boolean {
            return if (str == null || "" == str) false else str.matches(Regex("^[0-9]*$"))
        }

        /**
         * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
         *
         * @param bit
         * @return
         */
        fun getPowerSum(bit: IntArray): Int {
            var sum = 0
            if (power.size != bit.size) {
                return sum
            }
            for (i in bit.indices) {
                for (j in power.indices) {
                    if (i == j) {
                        sum += bit[i] * power[j]
                    }
                }
            }
            return sum
        }

        /**
         * 将和值与11取模得到余数进行校验码判断
         * @param checkCode
         * @param sum17
         * @return 校验位
         */
        fun getCheckCodeBySum(sum17: Int): String? {
            var checkCode: String? = null
            when (sum17 % 11) {
                10 -> checkCode = "2"
                9 -> checkCode = "3"
                8 -> checkCode = "4"
                7 -> checkCode = "5"
                6 -> checkCode = "6"
                5 -> checkCode = "7"
                4 -> checkCode = "8"
                3 -> checkCode = "9"
                2 -> checkCode = "x"
                1 -> checkCode = "0"
                0 -> checkCode = "1"
            }
            return checkCode
        }

        /**
         * 将字符数组转为整型数组
         *
         * @param c
         * @return
         * @throws NumberFormatException
         */
        @Throws(NumberFormatException::class)
        fun converCharToInt(c: CharArray): IntArray {
            val a = IntArray(c.size)
            var k = 0
            for (temp in c) {
                a[k++] = temp.toString().toInt()
            }
            return a
        }
    }
}
