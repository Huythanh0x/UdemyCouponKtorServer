package com.example.controller.helper

import com.example.data.dao.CouponDAO
import com.example.data.model.CouponCourseData
import com.example.utils.Constants
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class LocalFileHelper {
    companion object {
        private val couponDAO = CouponDAO
        fun dumpFetchedTimeJsonToFile(jsonFilePath: String = "fetched_time.json") {
            val resultJson = JSONObject()
            resultJson.put("localTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            FileWriter(jsonFilePath).use { it.write(resultJson.toString()) }
        }

        fun loadLasFetchedTimeInMilliSecond(): Long {
            return try {
                val couponsJson = File("fetched_time.json").readText()
                val responseJsonObject = JSONObject(couponsJson)
                val dateTimeString = responseJsonObject.getString("localTime")
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
                localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis() - Constants.INTERVAL
            }
        }

        fun getAllCouponCoursesJson(): String {
            if (File("fetched_time.json").exists()) {
                val coupons = couponDAO.getAllCouponCourses()
                return createResponseJson(coupons).toString()
            }
            return "There is no data to fetch"
        }

        fun getNCouponCoursesJson(numberOfCourseRequest: Int): String {
            if (File("fetched_time.json").exists()) {
                val coupons = couponDAO.getNCouponCourses(numberOfCourseRequest)
                return createResponseJson(coupons).toString()
            }
            return "There is no data to fetch"
        }

        fun queryCouponCoursesJson(searchQuery: String): String {
            if (File("fetched_time.json").exists()) {
                val coupons = couponDAO.searchCouponsByKeyword(searchQuery)
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
}