package com.example.helper

import com.google.gson.GsonBuilder
import org.json.JSONArray
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalJsonHelper {
    companion object {
        fun dumpJsonToFile(jsonArray: JSONArray, jsonFilePath: String = "udemy_coupon.json") {
            val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
            val resultJson = gson.toJsonTree(jsonArray).asJsonObject
            resultJson.apply {
                addProperty("localTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            }
            FileWriter(jsonFilePath).use { it.write(resultJson.toString()) }

        }
    }
}