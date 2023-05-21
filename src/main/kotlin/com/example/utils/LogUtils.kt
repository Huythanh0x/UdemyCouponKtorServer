package com.example.utils

import java.net.URL
import java.sql.Timestamp
import java.util.*

object LogUtils {
    fun getCurrentIpAddress(): String {
        var ipAddress: String? = null
        try {
            val url = URL("https://ipinfo.io/ip")
            val connection = url.openConnection()
            connection.connect()
            val reader = connection.getInputStream().bufferedReader()
            ipAddress = reader.readLine()
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ipAddress!!
    }
}