package com.example

import com.example.crawler.EnextCrawler
import com.example.helper.LocalFileHelper
import com.example.model.CouponCourseData
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
//                    println("Before fetching the URL form e-next")
                    allCouponUrls.addAll(EnextCrawler().getAllCouponUrl())
                    println("Got the result after fetching with length ${allCouponUrls.size}")
//                    allCouponUrls.addAll(RealDiscountCrawler(1000).getAllCouponUrl())
                    val allCouponUrlsSet = filterValidCouponUrls(allCouponUrls)
                    println("Got the result after filtering by set with length ${allCouponUrls.size}")
                    println("Sample for the first url ${allCouponUrlsSet.toList()[0]}")

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
                println("Trying saving coupon course data $couponUrl")
                // submit a new thread to the executor
//                executor.submit {
//                    val couponCodeData = UdemyCouponCourseExtractor(couponUrl).getFullCouponCodeData()
//                    couponCodeData?.let {
//                        couponCourseArray.add(it)
//                        print(it)
//                    }
//                }
                val couponCodeData = UdemyCouponCourseExtractor(couponUrl).getFullCouponCodeData()
                couponCodeData?.let {
                    couponCourseArray.add(it)
                    print(it)
                }
            }
            // shutdown the executor once all threads are finished
            executor.shutdown()
            while (!executor.isTerminated) {
                // wait until all threads are finished
            }
            LocalFileHelper.dumpJsonToFile(couponCourseArray)
            LocalFileHelper.storeDataAsCsv(couponCourseArray)
            println("All threads finished")
        }

        private fun filterValidCouponUrls(couponUrls: Set<String>): Set<String> {
            return couponUrls.filter { it.contains("?couponCode=") }.toSet()
        }

    }
}