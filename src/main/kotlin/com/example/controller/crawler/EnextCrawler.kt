package com.example.controller.base.crawler

import com.example.controller.base.CouponUrlCrawlerBase
import com.example.controller.helper.RemoteJsonHelper
import org.json.JSONArray
import org.json.JSONObject

class EnextCrawler : CouponUrlCrawlerBase() {
    override val baseAPIUrl: String = "https://jobs.e-next.in/public/assets/data/udemy.json"

    override fun getAllCouponUrl(): List<String> {
        return fetchListJsonObjectFromAPI().mapNotNull { it as JSONObject }
            .map { couponUrl -> extractCouponUrl(couponUrl) }
    }

    private fun extractCouponUrl(jsonObject: JSONObject): String {
        return jsonObject.getString("site")
    }

    private fun fetchListJsonObjectFromAPI(): JSONArray {
        return RemoteJsonHelper.getJsonArrayFrom(baseAPIUrl)
    }

}