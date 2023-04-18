package com.example.model

import org.json.JSONObject

data class CouponCourseData(
        val courseId: Int,
        val category: String,
        val subCategory: String,
        val title: String,
        val contentLength: Int,
        val level: String,
        val author: String,
        val rating: Float,
        val reviews: Int,
        val students: Int,
        val couponCode: String,
        val previewImage: String,
        val couponUrl: String,
        val expiredDate: String,
        val usesRemaining: Int,
        val heading: String,
        val description: String,
        val previewVideo: String,
        val language: String
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("courseId", courseId)
        json.put("category", category)
        json.put("subCategory", subCategory)
        json.put("title", title)
        json.put("contentLength", contentLength)
        json.put("level", level)
        json.put("author", author)
        json.put("rating", rating)
        json.put("reviews", reviews)
        json.put("students", students)
        json.put("couponCode", couponCode)
        json.put("previewImage", previewImage)
        json.put("couponUrl", couponUrl)
        json.put("expiredDate", expiredDate)
        json.put("usesRemaining", usesRemaining)
        json.put("heading", heading)
        json.put("description", description)
        json.put("previewVideo", previewVideo)
        json.put("language", language)
        return json
    }
    fun toCSVString(): String {
        return "$courseId|||$category|||$subCategory|||$title|||$contentLength|||$level|||$author|||$rating|||$reviews|||" +
                "$students|||$couponCode|||$previewImage|||$couponUrl|||$expiredDate|||$usesRemaining|||$heading|||" +
                "$description|||$previewVideo|||$language\n"
    }
}