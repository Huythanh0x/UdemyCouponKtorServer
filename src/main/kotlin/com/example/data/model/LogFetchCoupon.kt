package com.example.data.model

import java.sql.Timestamp

data class LogFetchCoupon(
    var id: Int,
    val time: Timestamp,
    val total: Int,
    val valid: Int,
    val expired: Int,
    val error: Int,
    val currentIpAddress: String
)