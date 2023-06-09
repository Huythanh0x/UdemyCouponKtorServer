package com.example.controller

import com.example.data.model.CouponCourseData
import com.example.data.model.CouponJsonData
import com.example.data.model.CourseJsonData
import com.example.controller.fetcher.WebContentFetcher
import com.example.utils.UrlUtils
import org.json.JSONObject


class UdemyCouponCourseExtractor(private val couponUrl: String) {
    private var courseId = 0
    private var couponCode: String = ""

    init {
        courseId = extractCourseId()
        couponCode = extractCouponCode()
    }

    private fun extractCourseId(): Int {
        val document = WebContentFetcher.getHtmlDocumentFrom(couponUrl)
        return try {
            document.body().attr("data-clp-course-id").toInt()
        }catch (e: Exception){
            val udemyId = document.getElementById("udemy")
            udemyId?.attr("data-clp-course-id")?.toInt() ?: throw java.lang.Exception("CAN NOT FIND ELEMENT COURSE ID FROM WEB")
        }
    }

    private fun extractCouponCode(): String {
        return couponUrl.split("/?couponCode=")[1]
    }

    fun getFullCouponCodeData(): CouponCourseData? {

        val couponDataResult = extractDataCouponFromOfficialAPI(
            WebContentFetcher.getJsonObjectFrom(
                UrlUtils.getCouponAPI(
                    courseId, couponCode
                )
            )
        )
        val courseDataResult =
            extractCourseDataFromOfficialAPI(WebContentFetcher.getJsonObjectFrom(UrlUtils.getCourseAPI(courseId)))
        return combineCourseAndCouponData(couponDataResult, courseDataResult)
    }

    private fun combineCourseAndCouponData(couponData: CouponJsonData, courseData: CourseJsonData): CouponCourseData? {
        if (couponData.price != 0f) return null
        return CouponCourseData(
            courseId,
            courseData.category,
            courseData.subCategory,
            courseData.courseTitle,
            courseData.contentLength,
            courseData.level,
            courseData.author,
            courseData.rating,
            courseData.numberReviews,
            courseData.students,
            couponCode,
            couponData.previewImage,
            couponUrl,
            couponData.expiredDate,
            couponData.usesRemaining,
            courseData.headline,
            courseData.description,
            couponData.previewVideo,
            courseData.language
        )
    }

    private fun extractCourseDataFromOfficialAPI(courseObjectJson: JSONObject): CourseJsonData {
        var author = "Unknown"
        var category = "Unknown"
        var subCategory = "Unknown"

        val title: String = courseObjectJson.getString("title")
        val headline: String = courseObjectJson.getString("headline")
        val description: String = courseObjectJson.getString("description").trim().replace("\n", "")
        try {
            author = courseObjectJson.getJSONArray("visible_instructors").getJSONObject(0).getString("title")
        } catch (_: Exception) {
        }
        try {
            category = courseObjectJson.getJSONObject("primary_category").getString("title")
        } catch (_: Exception) {
        }

        try {
            subCategory = courseObjectJson.getJSONObject("primary_sub_category").getString("title")
        } catch (_: Exception) {
        }
        val language: String = courseObjectJson.getJSONObject("locale").getString("simple_english_title")
        val level: String = if (courseObjectJson.getString("instructional_level")
                .contains("Levels")
        ) courseObjectJson.getString("instructional_level") else courseObjectJson.getString("instructional_level")
            .replace(" Level", "")
        val students: Int = courseObjectJson.getInt("num_subscribers")
        val rating: Float = courseObjectJson.getFloat("avg_rating_recent")
        val numberReviews: Int = courseObjectJson.getInt("num_reviews")
        val contentLength: Int = courseObjectJson.getInt("estimated_content_length")


        return CourseJsonData(
            category,
            subCategory,
            title,
            level,
            author,
            contentLength,
            rating,
            numberReviews,
            students,
            language,
            headline,
            description
        )
    }

    private fun extractDataCouponFromOfficialAPI(couponJsonObject: JSONObject): CouponJsonData {
        var usesRemaining = 0

        val price: Float =
            couponJsonObject.getJSONObject("price_text").getJSONObject("data").getJSONObject("pricing_result")
                .getJSONObject("price").getFloat("amount")

        val expiredDate: String =
            try {
                couponJsonObject.getJSONObject("price_text").getJSONObject("data").getJSONObject("pricing_result")
                    .getJSONObject("campaign").getString("end_time")
            }catch (e: Exception){
                "2030-05-19 17:24:00+00:00"
            }

        val previewImage: String = couponJsonObject.getJSONObject("sidebar_container").getJSONObject("componentProps")
            .getJSONObject("introductionAsset").getJSONObject("images").getString("image_750x422")

        val previewVideo: String = couponJsonObject.getJSONObject("sidebar_container").getJSONObject("componentProps")
            .getJSONObject("introductionAsset").getString("course_preview_path").toString()

        try {
            usesRemaining =
                couponJsonObject.getJSONObject("price_text").getJSONObject("data").getJSONObject("pricing_result")
                    .getJSONObject("campaign").getInt("uses_remaining")
        } catch (_: Exception) {
        }

        return CouponJsonData(price, expiredDate, previewImage, previewVideo, usesRemaining)
    }

}