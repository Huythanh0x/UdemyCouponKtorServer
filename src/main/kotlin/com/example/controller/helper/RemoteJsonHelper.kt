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
            return JSONObject(allContent)
        }

        fun getJsonArrayFrom(urlString: String): JSONArray {
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
            return JSONArray(allContent)
        }

    }
}
