package com.example.controller.time

import com.example.data.dao.CouponDAO
import com.example.utils.Constants
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class LastFetchTimeManager {
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
    }
}