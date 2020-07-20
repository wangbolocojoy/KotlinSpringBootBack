package com.btm.back.im.service

import cn.hutool.core.io.FileUtil
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.tio.utils.lock.ListWithLock
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock

/**
 * 美女图
 * @author tanyaowu
 * 2017年5月14日 上午9:48:03
 */
object ImgMnService {
    private val log = LoggerFactory.getLogger(ImgMnService::class.java)
    val imgListWithLock = ListWithLock(ArrayList<String>())
    const val dftimg = "http://images.rednet.cn/articleimage/2013/01/23/1403536948.jpg"
    const val filepath = "/page/imgs/mn.txt"
    const val maxSize = 100000
    var imgIndex = AtomicInteger()
    @JvmStatic
	fun nextImg(): String {
        val lock: Lock = imgListWithLock.lock.readLock()
        return try {
            lock.lock()
            val list = imgListWithLock.obj
            if (list.size == 0) {
                return dftimg
            }
            val index = imgIndex.incrementAndGet() % list.size // RandomUtil.randomInt(0, list.size() - 1);
            log.debug("图片index:$index")
            val imgsrc = list[index]
            if (StringUtils.isNotBlank(imgsrc)) {
                imgsrc
            } else nextImg()
        } catch (e1: Exception) {
            log.error(e1.toString(), e1)
            dftimg
        } finally {
            lock.unlock()
        }
    }

    fun start() {
        Thread(Runnable {
            while (true) {
                work()
                try {
                    Thread.sleep(60 * 1000 * 60.toLong()) //多长时间爬一次
                } catch (e: Exception) {
                    log.error(e.toString(), e)
                }
            }
        }, "get img url").start()
    }

    var pags = arrayOf(
            "http://www.mmonly.cc/wmtp/wmxz/list_27_1.html")

    fun work() {
        val basePath = Thread.currentThread().contextClassLoader.getResource("").path
        val file = File(basePath + filepath)
        if (file.exists()) {
            val list = FileUtil.readLines(file, "utf-8")
            if (list.size >= 1000) {
                imgListWithLock.obj.addAll(list)
                return
            }
        } else {
            FileUtil.touch(file) //创建文件
        }
        val list: MutableList<String> = ArrayList()
        list.addAll(imgListWithLock.obj)
        var sleeptime: Long = 500
        var isfirst = false
        if (list.size == 0) {
            isfirst = true
            sleeptime = 1
        }
        L1@ for (pag in pags) {
            try {
                var doc: Document? = null
                //创建页面对象
                doc = Jsoup.connect(pag).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36")
                        .timeout(10000).get()
                //根据标签和class id获取元素
                val div = doc.select("div.ABox")
                //根据标签获取元素
                val pages = div.select("a")
                var count = 0
                var invalidCount = 0
                for (e in pages) {
                    val page = e.attr("href")
                    try {
                        val imgdoc = Jsoup.connect(page)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36").timeout(10000)
                                .get()
                        val div22 = imgdoc.select(".totalpage")
                        val totalpageStr = div22[0].text()
                        val totalpage = totalpageStr.toInt()
                        if (totalpage > 0) {
                            var i = 1
                            while (i <= totalpage) {
                                val url = page.substring(0, page.length - 5) + "_" + i + ".html"
                                try {
                                    val imgpage = Jsoup.connect(url)
                                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36")
                                            .timeout(10000).get()
                                    val div2 = imgpage.select("#big-pic")
                                    val p = div2.select("p")
                                    val img = p.select("img")
                                    val src = img.attr("src")
                                    if (StringUtils.isBlank(src) || !StringUtils.startsWith(src, "http")) {
                                        i++
                                        continue
                                    }
                                    if (isfirst) {
                                        val f = savefile(src)
                                        if (!f) {
                                            break@L1
                                        }
                                    } else {
                                        list.add(src)
                                        log.error("{}、【{}】", list.size, src)
                                        while (list.size > maxSize) {
                                            break@L1
                                            //											list.remove(0);
//											log.error("删除一个元素后:{}", list.size());
                                        }
                                    }
                                    invalidCount++
                                    count++
                                    i++
                                    Thread.sleep(sleeptime)
                                } catch (e1: Exception) { //log.error("【"+url+"】爬取异常!");
                                }
                                i++
                            }
                        }
                    } catch (e1: IOException) { //log.error("【"+page+"】爬取异常!");
                    }
                }
                if (!isfirst) {
                    savefile(list)
                }
                log.debug("抓取图片地址，打完收工，本次共找到:{}, 其中有效数据:{}，", count, invalidCount)
            } catch (e: Exception) {
                log.error(e.toString(), e)
            }
        }
        val list1 = imgListWithLock.obj
        FileUtil.writeLines(list1, file.path, "utf-8")
    }

    fun savefile(srcs: List<String>) {
        val lock = imgListWithLock.lock.writeLock()
        try {
            lock.lock()
            val list = imgListWithLock.obj
            list.addAll(srcs)
        } catch (e1: Exception) {
            log.error(e1.toString(), e1)
        } finally {
            lock.unlock()
        }
    }

    fun savefile(src: String): Boolean {
        val lock = imgListWithLock.lock.writeLock()
        try {
            lock.lock()
            val list = imgListWithLock.obj
            list.add(src)
            log.debug("{}、【{}】", list.size, src)
            while (list.size > maxSize) {
                return false
            }
        } catch (e1: Exception) {
            log.error(e1.toString(), e1)
        } finally {
            lock.unlock()
        }
        return true
    }

    /**
     * @param args
     * @author: tanyaowu
     */
    @JvmStatic
    fun main(args: Array<String>) {
        start()
    }

    init {
        start()
    }
}
