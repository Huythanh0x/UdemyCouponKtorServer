package com.example.utils

class Constants {
    companion object {
        const val dbName = "udemy_database"
        const val baseDBUrl = "jdbc:mysql://localhost:3306"
        const val dbUrl = "jdbc:mysql://localhost:3306/$dbName"
        const val dbUser = "root"
        const val dbPassword = "password"
        const val tableName = "udemy_coupon_table"
    }
}