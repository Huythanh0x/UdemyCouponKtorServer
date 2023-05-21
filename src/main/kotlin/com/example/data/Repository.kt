package com.example.data

import com.example.data.dao.CouponDAO
import com.example.data.dao.ExpiredCouponDAO
import com.example.data.dao.LogFetchCouponDao
import com.example.data.dao.LogRequestCouponDao
import com.example.data.model.CouponCourseData
import org.json.JSONObject
import java.io.File

object Repository {
    val couponDao = CouponDAO
    val expiredCouponDao = ExpiredCouponDAO
    val logFetchedCouponDao = LogFetchCouponDao
    val logRequestCouponDao = LogRequestCouponDao
    fun getAllCouponCoursesJson(): String {
        if (File("fetched_time.json").exists()) {
            val coupons = couponDao.getAllCouponCourses()
            return createResponseJson(coupons).toString()
        }
        return "There is no data to fetch"
    }

    fun getNCouponCoursesJson(numberOfCourseRequest: Int): String {
        if (File("fetched_time.json").exists()) {
            val coupons = couponDao.getNCouponCourses(numberOfCourseRequest)
            return createResponseJson(coupons).toString()
        }
        return "There is no data to fetch"
    }

    fun queryCouponCoursesJson(searchQuery: String): String {
        if (File("fetched_time.json").exists()) {
            val coupons = couponDao.searchCouponsByKeyword(searchQuery)
            return createResponseJson(coupons).toString()
        }
        return "There is no data to fetch"
    }

    private fun createResponseJson(coupons: Set<CouponCourseData>): JSONObject {
        val couponsJson = File("fetched_time.json").readText()
        val responseJsonObject = JSONObject(couponsJson)
        val responseJson = JSONObject()
        responseJson.apply {
            put("coupons", coupons)
            put("localTime", responseJsonObject.getString("localTime"))
        }
        return responseJson
    }
}