package org.example.crawler

import org.example.base.CouponUrlCrawlerBase
import org.example.helper.RemoteJsonHelper
import org.json.JSONArray
import org.json.JSONObject

class RealDiscountCrawler(private val maxCouponRequest:Int=600) : CouponUrlCrawlerBase() {
    override val baseAPIUrl: String
        get() = "https://www.real.discount/api-web/all-courses/?store=Udemy&page=1&per_page=${maxCouponRequest}&orderby=undefined&free=0&search=&language=&cat="

    override fun getAllCouponUrl(): List<String> {
        return fetchListJsonObjectFromAPI().mapNotNull { it as JSONObject }
            .map { couponUrl -> extractCouponUrl(couponUrl) }
    }

    private fun extractCouponUrl(jsonObject: JSONObject): String {
        return jsonObject.getString("url")
    }

    private fun fetchListJsonObjectFromAPI(): JSONArray {
        return RemoteJsonHelper.getJsonObjectFrom(baseAPIUrl)["results"] as JSONArray
    }
}