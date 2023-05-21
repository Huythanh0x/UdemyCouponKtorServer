package com.example.controller.crawler

import com.example.controller.base.CouponUrlCrawlerBase
import com.example.controller.fetcher.WebContentFetcher
import org.json.JSONArray
import org.json.JSONObject

class RealDiscountCrawler(private val maxCouponRequest: Int = 600) : CouponUrlCrawlerBase() {
    override val baseAPIUrl: String
        get() = "https://www.real.discount/api-web/all-courses/?store=Udemy&page=1&per_page=${maxCouponRequest}&orderby=undefined&free=0&search=&language=&cat="

    override fun getAllCouponUrl(): Set<String> {
        return fetchListJsonObjectFromAPI().mapNotNull { it as JSONObject }
            .map { couponUrl -> extractCouponUrl(couponUrl) }.toSet()
    }

    private fun extractCouponUrl(jsonObject: JSONObject): String {
        return jsonObject.getString("url").replace(
            "http://click.linksynergy.com/fs-bin/click?id=bnwWbXPyqPU&subid=&offerid=323058.1&type=10&tmpid=14537&RD_PARM1=",
            ""
        )
    }

    private fun fetchListJsonObjectFromAPI(): JSONArray {
        return WebContentFetcher.getJsonObjectFrom(baseAPIUrl)["results"] as JSONArray
    }
}