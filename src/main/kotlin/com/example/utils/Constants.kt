package com.example.utils

class Constants {
    companion object {
        const val dbName = "udemy_database"
        const val baseDBUrl = "jdbc:mysql://localhost:3306"
        const val dbUrl = "$baseDBUrl/$dbName"
        const val dbUser = "root"
        const val dbPassword = "password"
        const val couponTableName = "udemy_coupon_table"
        const val expiredCouponTableName = "expired_udemy_coupon_table"
        const val logRequestCouponTableName = "log_request_coupon_table"
        const val logFetchCouponTableName = "log_fetch_coupon_table"
        const val INTERVAL = 15 * 60 * 1000
    }
}