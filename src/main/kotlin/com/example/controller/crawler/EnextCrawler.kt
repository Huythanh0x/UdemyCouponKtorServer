package com.example.controller.crawler

import com.example.controller.base.CouponUrlCrawlerBase
import com.example.controller.fetcher.WebContentFetcher
import org.json.JSONArray
import org.json.JSONObject

class EnextCrawler(private val maxCouponRequest: Int = 600) : CouponUrlCrawlerBase() {
    override val baseAPIUrl: String = "https://jobs.e-next.in/public/assets/data/udemy.json"

    override fun getAllCouponUrl(): Set<String> {
        val allCouponsUrls = fetchListJsonObjectFromAPI().mapNotNull { it as JSONObject }
            .map { couponUrl -> extractCouponUrl(couponUrl) }
        return when (allCouponsUrls.size) {
            in 0..maxCouponRequest -> return allCouponsUrls.toSet()
            else -> return allCouponsUrls.subList(0, maxCouponRequest).toSet()
        }
    }

    private fun extractCouponUrl(jsonObject: JSONObject): String {
        return jsonObject.getString("site")
    }

    private fun fetchListJsonObjectFromAPI(): JSONArray {
        return WebContentFetcher.getJsonArrayFrom(baseAPIUrl)
    }

}