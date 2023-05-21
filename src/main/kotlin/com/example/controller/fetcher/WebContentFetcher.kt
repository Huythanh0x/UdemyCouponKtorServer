package com.example.controller.fetcher

import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.InputStreamReader

class WebContentFetcher {
    companion object {
        fun getJsonObjectFrom(urlString: String): JSONObject {
            return JSONObject(getRawHTMLContentFrom(urlString))
        }

        fun getJsonArrayFrom(urlString: String): JSONArray {
            return JSONArray(getRawHTMLContentFrom(urlString))
        }

        fun getHtmlDocumentFrom(couponUrl: String): Document {
            return Jsoup.parse(getRawHTMLContentFrom(couponUrl))
        }

        private fun getRawHTMLContentFrom(urlString: String): String {
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
            return allContent
        }

    }
}
