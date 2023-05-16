package com.example.controller

import com.example.controller.crawler.EnextCrawler
import com.example.controller.crawler.RealDiscountCrawler
import com.example.controller.helper.LocalFileHelper
import com.example.controller.helper.RemoteJsonHelper
import com.example.data.model.CouponCourseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.math.max

class MainCrawler {
    companion object {
        private const val INTERVAL = 10 * 60 * 1000
        fun startCrawler() {
            var startTime: Long
            GlobalScope.launch(Dispatchers.IO) {
                while (true) {
                    startTime = System.currentTimeMillis()
                    val allCouponUrls = mutableSetOf<String>()
                    allCouponUrls.addAll(EnextCrawler().getAllCouponUrl())
                    allCouponUrls.addAll(RealDiscountCrawler(1000).getAllCouponUrl())
                    val allCouponUrlsSet = filterValidCouponUrls(allCouponUrls)
                    File("udemy_coupon_urls.log").writeText(allCouponUrlsSet.joinToString("\n"))
                    saveAllCouponData(allCouponUrlsSet, numberOfThread = 20)
                    val runTime = System.currentTimeMillis() - startTime
                    val delayTime = max(INTERVAL - runTime, 0)
                    println("Wait for $delayTime milliseconds until the next run")
                    kotlinx.coroutines.delay(delayTime)
                }
            }
        }

        private fun saveAllCouponData(allCouponUrls: Set<String>, numberOfThread: Int = 40) {
            val couponCourseArray = mutableSetOf<CouponCourseData>()
            val executor: ThreadPoolExecutor = Executors.newFixedThreadPool(numberOfThread) as ThreadPoolExecutor

            allCouponUrls.forEach { couponUrl ->
                // submit a new thread to the executor
                executor.submit {
                    try {
                        val couponCodeData = UdemyCouponCourseExtractor(couponUrl).getFullCouponCodeData()
                        couponCodeData?.let {
                            couponCourseArray.add(it)
                            println(it.title)
                        }
                    } catch (e: Exception) {
                        println(e.toString())
                    }
                }
            }
            // shutdown the executor once all threads are finished
            executor.shutdown()
            while (!executor.isTerminated) {
                // wait until all threads are finished
            }
            val currentIpAddress = RemoteJsonHelper.getJsonObjectFrom("https://ipinfo.io/").getString("ip")
            println("current IP address $currentIpAddress")
            LocalFileHelper.dumpJsonToFile(couponCourseArray, currentIpAddress)
            LocalFileHelper.storeDataAsCsv(couponCourseArray)
//            LocalFileHelper.storeDataAsCsv(couponCourseArray)
//            val couponDao = CouponDAO(DatabaseProvider())
//            couponDao.insertCouponCourses(couponCourseArray.toList())
            println("All threads finished")
        }

        private fun filterValidCouponUrls(couponUrls: Set<String>): Set<String> {
            return couponUrls.filter { it.contains("?couponCode=") }.toSet()
        }

    }
}