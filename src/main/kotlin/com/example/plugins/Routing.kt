package com.example.plugins

import com.example.data.Repository
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
        route("{...}") {
            handle {
                val path = call.request.path()
                call.respond(HttpStatusCode.NotFound, "No route defined for $path")
            }
        }
    }
}

