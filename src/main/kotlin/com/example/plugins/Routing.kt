package com.example.plugins

import com.example.controller.UdemyCouponCourseExtractor
import com.example.data.Repository
import com.example.utils.Constants
import com.example.utils.UrlUtils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Welcome to the home page", status = HttpStatusCode.OK)
        }
        get("/fetch/all") {
            Repository.logRequestCouponDao.insertLogRequestCoupon(
                call.request.origin.remoteHost,
                call.request.path(),
                HttpStatusCode.OK.value,
                "Response all coupons successfully"
            )
            call.respondText(
                Repository.getAllCouponCoursesJson(), ContentType.Application.Json, status = HttpStatusCode.OK
            )
        }
        get("/fetch/{numberOfCourseRequest?}") {
            val numberOfCourseRequest = call.parameters["numberOfCourseRequest"]?.toIntOrNull()
            if (numberOfCourseRequest == null || numberOfCourseRequest <= 0) {
                Repository.logRequestCouponDao.insertLogRequestCoupon(
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.BadRequest.value,
                    "Bad request with quantity $numberOfCourseRequest",
                )
                return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)
            }
            Repository.logRequestCouponDao.insertLogRequestCoupon(
                call.request.origin.remoteHost,
                call.request.path(),
                HttpStatusCode.OK.value,
                "Responded $numberOfCourseRequest coupons successfully",
            )
            call.respondText(
                Repository.getNCouponCoursesJson(numberOfCourseRequest),
                ContentType.Application.Json,
                status = HttpStatusCode.OK
            )
        }
        get("/search/{query?}") {
            val searchQuery = call.parameters["query"]
            if (searchQuery.isNullOrEmpty()) {
                Repository.logRequestCouponDao.insertLogRequestCoupon(
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.BadRequest.value,
                    "Bad request with query $searchQuery"
                )
                return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)
            }
            Repository.logRequestCouponDao.insertLogRequestCoupon(
                call.request.origin.remoteHost,
                call.request.path(),
                HttpStatusCode.OK.value,
                "Responded coupons have $searchQuery successfully"
            )
            call.respondText(
                Repository.queryCouponCoursesJson(searchQuery), ContentType.Application.Json, status = HttpStatusCode.OK
            )
        }

        get("/post") {
            val newCouponUrl = getOriginalUrlFromBase64String(call, Constants.NEW_COUPON_PARAMETER)
            if (newCouponUrl == null) {
                Repository.logRequestCouponDao.insertLogRequestCoupon(
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.BadRequest.value,
                    "Cannot parse base64 string"
                )
                call.respondText(
                    "Bad request with the base64 url",
                    ContentType.Application.Json,
                    status = HttpStatusCode.BadRequest
                )
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    UdemyCouponCourseExtractor(newCouponUrl).getFullCouponCodeData()?.let { couponData ->
                        Repository.couponDao.insertCouponCourses(listOf(couponData).toSet())
                    }
                }
                Repository.logRequestCouponDao.insertLogRequestCoupon(
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.OK.value,
                    "Posted coupons $newCouponUrl successfully"
                )
                call.respondText(
                    "Posted coupons $newCouponUrl successfully",
                    ContentType.Application.Json,
                    status = HttpStatusCode.OK
                )

            }
        }

        get("/delete") {
            val deleteCouponUrl = getOriginalUrlFromBase64String(call, Constants.DELETE_COUPON_PARAMETER)
            if (deleteCouponUrl == null) {
                Repository.logRequestCouponDao.insertLogRequestCoupon(
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.BadRequest.value,
                    "Cannot parse base64 string"
                )
                call.respondText(
                    "Bad request with the base64 url",
                    ContentType.Application.Json,
                    status = HttpStatusCode.BadRequest
                )
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    if (UdemyCouponCourseExtractor(deleteCouponUrl).getFullCouponCodeData() == null) {
                        Repository.couponDao.deleteCouponCourse(deleteCouponUrl)
                    }
                }
                Repository.logRequestCouponDao.insertLogRequestCoupon(
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.OK.value,
                    "Posted coupons $deleteCouponUrl successfully"
                )
                call.respondText(
                    "Posted coupons $deleteCouponUrl successfully",
                    ContentType.Application.Json,
                    status = HttpStatusCode.OK
                )
            }
        }


        route("{...}") {
            handle {
                val path = call.request.path()
                call.respond(HttpStatusCode.NotFound, "No route defined for $path")
            }
        }
    }
}

fun getOriginalUrlFromBase64String(call: ApplicationCall, parameterKey: String): String? {
    val newCouponUrlB64 = call.request.queryParameters[parameterKey]
    if (newCouponUrlB64.isNullOrEmpty()) {
        return null
    }
    return try {
        UrlUtils.decodeBase64String(newCouponUrlB64)
    } catch (e: Exception) {
        null
    }
}

