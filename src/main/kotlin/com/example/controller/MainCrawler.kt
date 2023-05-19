package com.example.controller

import com.example.controller.crawler.EnextCrawler
import com.example.controller.crawler.RealDiscountCrawler
import com.example.controller.helper.LocalFileHelper
import com.example.data.dao.CouponDAO
import com.example.data.model.CouponCourseData
import com.example.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.math.max

class MainCrawler {
    companion object {
        fun startCrawler() {
            var startTime: Long
            GlobalScope.launch(Dispatchers.IO) {
                while (true) {
                    startTime = System.currentTimeMillis()
                    val allCouponUrls = mutableSetOf<String>()
                    allCouponUrls.addAll(EnextCrawler().getAllCouponUrl())
//                    allCouponUrls.addAll(RealDiscountCrawler(1000).getAllCouponUrl())
                    val allCouponUrlsSet = filterValidCouponUrls(allCouponUrls)
                    println("Coupon URL set: ${allCouponUrlsSet.size}")
                    saveAllCouponData(allCouponUrlsSet, numberOfThread = 20)
                    val runTime = System.currentTimeMillis() - startTime
                    val delayTime = max(Constants.INTERVAL - runTime, 0)
                    println("Wait for $delayTime milliseconds until the next run")
                    kotlinx.coroutines.delay(delayTime)
                }
            }
        }

        private fun saveAllCouponData(allCouponUrls: Set<String>, numberOfThread: Int = 40) {
            val couponCourseArray = mutableSetOf<CouponCourseData>()
            val executor: ThreadPoolExecutor = Executors.newFixedThreadPool(numberOfThread) as ThreadPoolExecutor
            val listFailToValidate = mutableMapOf<String, String>()
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
                        listFailToValidate[couponUrl] = e.toString()
                    }
                }
            }
            // shutdown the executor once all threads are finished
            executor.shutdown()
            while (!executor.isTerminated) {
                // wait until all threads are finished
            }
            println("All threads finished")
            LocalFileHelper.dumpFetchedTimeJsonToFile()
            LocalFileHelper.storeDataAsCsv(couponCourseArray)
            CouponDAO.deleteAllCouponCourses()
            CouponDAO.insertCouponCourses(couponCourseArray.toList())
            File("udemy_courses.log").writeText(couponCourseArray.toList().joinToString("\n"))
            File("udemy_errors.log").writeText(listFailToValidate.toList().joinToString("\n"))
        }

        private fun filterValidCouponUrls(couponUrls: Set<String>): Set<String> {
            return couponUrls.filter { it.contains("?couponCode=") }.toSet()
        }

    }
}