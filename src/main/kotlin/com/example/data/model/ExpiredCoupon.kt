package com.example.data.model

import java.sql.Timestamp

data class ExpiredCoupon(
    val couponUrl: String,
    val timeStamp: Timestamp
)
