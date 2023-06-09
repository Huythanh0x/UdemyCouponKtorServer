package com.example.data.model

data class CouponJsonData(
    val price: Float?,
    val expiredDate: String,
    val previewImage: String,
    val previewVideo: String,
    val usesRemaining: Int
)
