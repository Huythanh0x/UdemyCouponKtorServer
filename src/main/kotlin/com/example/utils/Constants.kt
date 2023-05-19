package com.example.utils

class Constants {
    companion object {
        const val dbName = "udemy_database"
        const val baseDBUrl = "jdbc:mysql://172.19.0.2:3306"
        const val dbUrl = "$baseDBUrl/$dbName"
        const val dbUser = "root"
        const val dbPassword = "password"
        const val tableName = "udemy_coupon_table"
        const val INTERVAL = 30 * 60 * 1000
    }
}