package com.example.helper

import com.example.model.CouponCourseData
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

class LocalFileHelper {
    companion object {
        fun dumpJsonToFile(
            couponCourseArray: MutableSet<CouponCourseData>, jsonFilePath: String = "udemy_coupon.json"
        ) {
            val jsonArray = JSONArray()
            for (coupon in couponCourseArray) {
                jsonArray.put(coupon.toJson())
            }
            val resultJson = JSONObject()
            resultJson.put("coupons", jsonArray)
            resultJson.put("localTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            FileWriter(jsonFilePath).use { it.write(resultJson.toString()) }
        }

        fun storeDataAsCsv(couponCourseArray: MutableSet<CouponCourseData>, csvFilePath: String = "udemy_coupon.csv") {
            val writer = FileWriter(File(csvFilePath))
            couponCourseArray.forEach { couponCourseData ->
                writer.append(couponCourseData.toCSVString())
            }
            writer.flush()
            writer.close()
        }

        fun getAllCouponCourseJson(): String {
            return File("udemy_coupon.json").readText()
        }

        fun getCouponCourseJson(numberOfCourseRequest: Int): String {
            val couponsJson = File("udemy_coupon.json").readText()
            val responseJsonObject = JSONObject(couponsJson)
            val responseJsonArray = responseJsonObject.getJSONArray("coupons")
            val numberOfCourseResponse = min(numberOfCourseRequest, responseJsonArray.length())

            val slicedArray = responseJsonArray.toList().subList(0, numberOfCourseResponse).let { JSONArray(it) }
            val responseJson = JSONObject()
            responseJson.apply {
                put("coupons", slicedArray)
                put("localTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            }
            return responseJson.toString()
        }

        fun queryCouponCourseJson(searchQuery: String): String {
            val couponsJson = File("udemy_coupon.json").readText()
            val responseJsonObject = JSONObject(couponsJson)
            val responseJsonArray = responseJsonObject.getJSONArray("coupons")
            val slicedArray =
                responseJsonArray.toList().filter { it.toString().lowercase().contains(searchQuery.lowercase()) }
                    .let { JSONArray(it) }
            val responseJson = JSONObject()
            responseJson.apply {
                put("coupons", slicedArray)
                put("localTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            }
            return responseJson.toString()
        }
    }
}