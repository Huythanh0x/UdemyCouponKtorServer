package com.example.controller.helper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.InputStreamReader

class RequestHtmlHelper {
    companion object {
        fun getHtmlDocument(couponUrl: String): Document {
            val command =
                """curl -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36" $couponUrl
            """.trimIndent()
            return Jsoup.parse(executeCurlCommand(command))
        }

        private fun executeCurlCommand(curlCommand: String): String {
            val process = Runtime.getRuntime().exec(curlCommand)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line)
            }

            process.waitFor()

            return output.toString()
        }
    }
}