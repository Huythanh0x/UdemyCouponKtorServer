package com.example.helper

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class RemoteJsonHelper {
    companion object {
        fun getJsonObjectFrom(urlString: String): JSONObject {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val resultJsonString = connection.inputStream.bufferedReader().readText()
                connection.disconnect()
                return JSONObject(resultJsonString)
            } else {
                throw java.lang.Exception("CANNOT LOAD JSON PROPERLY")
            }
        }

        fun getJsonArrayFrom(urlString: String): JSONArray {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val resultJsonString = connection.inputStream.bufferedReader().readText()
                connection.disconnect()
                return JSONArray(resultJsonString)
            } else {
                throw java.lang.Exception("CANNOT LOAD JSON PROPERLY")
            }
        }

    }
}
