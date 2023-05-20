package com.example.data.model

import java.sql.Timestamp


data class LogRequestCoupon(
    val id: Int,
    val ipAddress: String,
    val entryPoint: String,
    val statusCode: Int,
    val message: String,
    val dateTime: Timestamp
)