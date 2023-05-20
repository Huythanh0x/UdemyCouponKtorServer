package com.example.controller.helper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class RequestHtmlHelper {
    companion object {
        fun getHtmlDocument(couponUrl: String): Document {
            val command = listOf("curl", couponUrl)

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
            return Jsoup.parse(allContent)
        }
    }
}