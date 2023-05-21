package com.example.controller

import com.example.controller.crawler.EnextCrawler
import com.example.controller.crawler.RealDiscountCrawler
import com.example.controller.time.LastFetchTimeManager
import com.example.data.Repository
import com.example.data.model.CouponCourseData
import com.example.data.model.ExpiredCoupon
import com.example.utils.Constants
import com.example.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.math.max

object MainCrawler {
    fun startCrawler() {
        var startTime = LastFetchTimeManager.loadLasFetchedTimeInMilliSecond()
        GlobalScope.launch(Dispatchers.IO) {
            delayUntilTheNextRound(startTime)
            while (true) {
                clearExpiredCoupons()
                startTime = System.currentTimeMillis()
                val allCouponUrls = mutableSetOf<String>()
                allCouponUrls.addAll(EnextCrawler(1000).getAllCouponUrl())
                allCouponUrls.addAll(RealDiscountCrawler(1000).getAllCouponUrl())
                val allCouponUrlsSet = filterValidCouponUrls(allCouponUrls)
                println("Coupon URL set: ${allCouponUrlsSet.size}")
                File("logs/all_coupon_codes.log").writeText(allCouponUrlsSet.toList().joinToString("\n"))
                saveAllCouponData(allCouponUrlsSet, numberOfThread = 10)
                delayUntilTheNextRound(startTime)
            }
        }
    }

    private fun clearExpiredCoupons() {
        Repository.expiredCouponDao.deleteExpiredCoupons()
    }

    private suspend fun delayUntilTheNextRound(startTime: Long) {
        val runTime = System.currentTimeMillis() - startTime
        val delayTime = max(Constants.INTERVAL - runTime, 0)
        println("\u001B[32mWait for $delayTime milliseconds until the next run\u001B[32m")
        kotlinx.coroutines.delay(delayTime)
    }

    private fun saveAllCouponData(allCouponUrls: Set<String>, numberOfThread: Int = 40) {
        val validCoupons = mutableSetOf<CouponCourseData>()
        val failedToValidateCouponUrls = mutableSetOf<String>()
        val expiredCouponUrls = mutableSetOf<String>()
        val executor: ThreadPoolExecutor = Executors.newFixedThreadPool(numberOfThread) as ThreadPoolExecutor
        allCouponUrls.forEach { couponUrl ->
            // submit a new thread to the executor
            executor.submit {
                try {
                    val couponCodeData = UdemyCouponCourseExtractor(couponUrl).getFullCouponCodeData()
                    if (couponCodeData != null) {
                        validCoupons.add(couponCodeData)
                        println(couponCodeData.title)
                    } else {
                        expiredCouponUrls.add(couponUrl)
                    }
                } catch (e: Exception) {
                    println(e.toString())
                    failedToValidateCouponUrls.add("$couponUrl $e")
                }
            }
        }
        // shutdown the executor once all threads are finished
        executor.shutdown()
        while (!executor.isTerminated) {
            // wait until all threads are finished
        }
        println("All threads finished")
        LastFetchTimeManager.dumpFetchedTimeJsonToFile()
        keepLogsInTextFiles(validCoupons, failedToValidateCouponUrls, expiredCouponUrls)
        dumpDataToTheDatabase(validCoupons, failedToValidateCouponUrls, expiredCouponUrls)
    }

    private fun keepLogsInTextFiles(
        validCoupons: MutableSet<CouponCourseData>,
        failedToValidateCouponUrls: MutableSet<String>,
        expiredCoupons: MutableSet<String>
    ) {
        File("logs/udemy_courses.log").writeText(validCoupons.toList().joinToString("\n"))
        File("logs/udemy_errors.log").writeText(failedToValidateCouponUrls.toList().joinToString("\n"))
        File("logs/udemy_expired_courses.log").writeText(expiredCoupons.toList().joinToString("\n"))
    }

    private fun keepLogForInValidUrls(invalidUrls: List<String>) {
        File("logs/udemy_invalid_urls.log").writeText(invalidUrls.toList().joinToString("\n"))
    }

    private fun dumpDataToTheDatabase(
        validCoupons: MutableSet<CouponCourseData>,
        failedToValidateCouponUrls: MutableSet<String>,
        expiredCouponUrls: MutableSet<String>
    ) {
        Repository.couponDao.deleteAllCouponCourses()
        Repository.couponDao.insertCouponCourses(validCoupons)
        Repository.expiredCouponDao.insertExpiredCoupons(expiredCouponUrls.toSet())
        Repository.logFetchedCouponDao.insertLogFetchCoupon(
            validCoupons.size + expiredCouponUrls.size + failedToValidateCouponUrls.size,
            validCoupons.size,
            expiredCouponUrls.size,
            failedToValidateCouponUrls.size,
            LogUtils.getCurrentIpAddress()

        )
    }

    private fun filterValidCouponUrls(couponUrls: MutableSet<String>): Set<String> {
        keepLogForInValidUrls(couponUrls.filter { !it.contains("?couponCode=") })
        return couponUrls.filter { it.contains("?couponCode=") }
            .subtract(Repository.expiredCouponDao.getAllExpiredCoupons().map { it.couponUrl }.toSet())

    }
}