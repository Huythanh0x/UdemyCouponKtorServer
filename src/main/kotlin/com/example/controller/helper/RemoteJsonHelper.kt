package com.example.controller.helper

import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RemoteJsonHelper {
    companion object {
        fun getJsonObjectFrom(urlString: String): JSONObject {
//            val url = URL(urlString)
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//            connection.connect()
//
//            val responseCode = connection.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                val resultJsonString = connection.inputStream.bufferedReader().readText()
//                connection.disconnect()
//                return JSONObject(resultJsonString)
//            } else {
//                println(urlString)
//                println("${connection.responseCode}${connection.responseMessage}")
//                throw java.lang.Exception("CANNOT LOAD JSON PROPERLY")
//            }

            val command = listOf("curl", "-g", urlString)
            val processBuilder = ProcessBuilder(command)
            val process = processBuilder.start()

            val inputStream = process.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))

            var allContent = ""
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                allContent += line
            }
            process.waitFor()
            File("parse_api_debug.log").writeText(allContent)
            return JSONObject(allContent)
        }

        fun getJsonArrayFrom(urlString: String): JSONArray {
//            val url = URL(urlString)
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//            connection.connect()
//
//            val responseCode = connection.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                val resultJsonString = connection.inputStream.bufferedReader().readText()
//                connection.disconnect()
//                return JSONArray(resultJsonString)
//            } else {
//                println(urlString)
//                println("${connection.responseCode}${connection.responseMessage}")
//                throw java.lang.Exception("CANNOT LOAD JSON PROPERLY")
//            }
            val command = listOf("curl", "-g", urlString)

            val processBuilder = ProcessBuilder(command)
            val process = processBuilder.start()

            val inputStream = process.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))

            var allContent = ""
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                allContent += line
            }
            process.waitFor()
            File("parse_api_debug.log").writeText(allContent)
            return JSONArray(allContent)
        }

    }
}
