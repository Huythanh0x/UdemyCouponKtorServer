package com.example.plugins

import com.example.controller.helper.LocalFileHelper.Companion.getAllCouponCoursesJson
import com.example.controller.helper.LocalFileHelper.Companion.getNCouponCoursesJson
import com.example.controller.helper.LocalFileHelper.Companion.queryCouponCoursesJson
import com.example.data.dao.LogRequestCouponDao
import com.example.data.model.LogRequestCoupon
import com.example.utils.LogUtils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Welcome to the home page", status = HttpStatusCode.OK)
        }
        get("/fetch/all") {
            LogRequestCouponDao.insertLogRequestCoupon(
                LogRequestCoupon(
                    0,
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.OK.value,
                    "Response all coupons successfully",
                    LogUtils.getCurrentTimestamp()
                )
            )
            call.respondText(getAllCouponCoursesJson(), ContentType.Application.Json, status = HttpStatusCode.OK)
        }
        get("/fetch/{numberOfCourseRequest?}") {
            val numberOfCourseRequest = call.parameters["numberOfCourseRequest"]?.toIntOrNull()
            if (numberOfCourseRequest == null || numberOfCourseRequest <= 0) {
                LogRequestCouponDao.insertLogRequestCoupon(
                    LogRequestCoupon(
                        0,
                        call.request.origin.remoteHost,
                        call.request.path(),
                        HttpStatusCode.BadRequest.value,
                        "Bad request with quantity $numberOfCourseRequest",
                        LogUtils.getCurrentTimestamp()
                    )
                )
                return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)
            }
            LogRequestCouponDao.insertLogRequestCoupon(
                LogRequestCoupon(
                    0,
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.OK.value,
                    "Responded $numberOfCourseRequest coupons successfully",
                    LogUtils.getCurrentTimestamp()
                )
            )
            call.respondText(
                getNCouponCoursesJson(numberOfCourseRequest), ContentType.Application.Json, status = HttpStatusCode.OK
            )
        }
        get("/search/{query?}") {
            val searchQuery = call.parameters["query"]
            if (searchQuery.isNullOrEmpty()) {
                LogRequestCouponDao.insertLogRequestCoupon(
                    LogRequestCoupon(
                        0,
                        call.request.origin.remoteHost,
                        call.request.path(),
                        HttpStatusCode.BadRequest.value,
                        "Bad request with query $searchQuery",
                        LogUtils.getCurrentTimestamp()
                    )
                )
                return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)
            }
            LogRequestCouponDao.insertLogRequestCoupon(
                LogRequestCoupon(
                    0,
                    call.request.origin.remoteHost,
                    call.request.path(),
                    HttpStatusCode.OK.value,
                    "Responded coupons have $searchQuery successfully",
                    LogUtils.getCurrentTimestamp()
                )
            )
            call.respondText(
                queryCouponCoursesJson(searchQuery), ContentType.Application.Json, status = HttpStatusCode.OK
            )
        }
        route("{...}") {
            handle {
                val path = call.request.path()
                call.respond(HttpStatusCode.NotFound, "No route defined for $path")
            }
        }
    }
}

