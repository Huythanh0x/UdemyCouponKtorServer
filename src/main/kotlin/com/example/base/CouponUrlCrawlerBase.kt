package com.example.base

abstract class CouponUrlCrawlerBase {
    abstract val baseAPIUrl: String
    abstract fun getAllCouponUrl(): List<String>
}