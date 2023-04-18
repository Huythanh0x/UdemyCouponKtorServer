package org.example.helper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.HttpURLConnection
import java.net.URL

class RequestHtmlHelper {
    companion object {
        fun getHtmlDocument(urlString: String): Document {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.instanceFollowRedirects = true
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw RuntimeException("Failed to retrieve HTML content from $urlString: HTTP error code $responseCode")
            }

            val inputStream = connection.inputStream
            val content = inputStream.bufferedReader().use { it.readText() }

            connection.disconnect()

            return Jsoup.parse(content)
        }

    }
}