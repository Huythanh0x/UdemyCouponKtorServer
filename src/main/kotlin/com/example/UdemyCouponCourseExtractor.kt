package org.example

import org.example.data.CouponCourseData
import org.example.data.CouponJsonData
import org.example.data.CourseJsonData
import org.example.helper.RequestHtmlHelper
import org.example.helper.RemoteJsonHelper
import org.example.utils.UrlUtils
import org.json.JSONObject

class UdemyCouponCourseExtractor(private val couponUrl: String) {
    private var courseId = 0
    private var couponCode: String = ""

    init {
        courseId = extractCourseId()
        couponCode = extractCouponCode()
    }

    private fun extractCourseId(): Int {
        return try {
            RequestHtmlHelper.getHtmlDocument(couponUrl).getElementById("udemy")?.attr("data-clp-course-id")!!.toInt()
        } catch (e: Exception) {
            throw java.lang.Exception("CAN NOT FIND COURSE ID")
        }
    }

    private fun extractCouponCode(): String {
        return couponUrl.split("/?couponCode=")[1]
    }

    fun getFullCouponCodeData(): CouponCourseData? {

        val couponDataResult = extractDataCouponFromOfficialAPI(
            RemoteJsonHelper.getJsonObjectFrom(
                UrlUtils.getCouponAPI(
                    courseId, couponCode
                )
            )
        )
        val courseDataResult =
            extractCourseDataFromOfficialAPI(RemoteJsonHelper.getJsonObjectFrom(UrlUtils.getCourseAPI(courseId)))
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
        var subCategory = "None"

        val title: String = courseObjectJson.getString("title")
        val headline: String = courseObjectJson.getString("headline")
        val description: String = courseObjectJson.getString("description").trim()
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
        val level: String = courseObjectJson.getString("instructional_level")
        val students: Int = courseObjectJson.getInt("num_subscribers")
        val rating: Float = courseObjectJson.getFloat("avg_rating_recent")
        val numberReviews: Int = courseObjectJson.getInt("num_reviews")
        val contentLength: Int = courseObjectJson.getInt("estimated_content_length")


        val resultData = CourseJsonData(
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
        println(resultData)
        return resultData
    }

    private fun extractDataCouponFromOfficialAPI(couponJsonObject: JSONObject): CouponJsonData {
        var usesRemaining = 0

        val price: Float =
            couponJsonObject.getJSONObject("price_text").getJSONObject("data").getJSONObject("pricing_result")
                .getJSONObject("price").getFloat("amount")

        val expiredDate: String =
            couponJsonObject.getJSONObject("price_text").getJSONObject("data").getJSONObject("pricing_result")
                .getJSONObject("campaign").getString("end_time")

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