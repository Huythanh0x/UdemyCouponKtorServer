package com.example.plugins

import com.example.helper.LocalFileHelper.Companion.getAllCouponCourseJson
import com.example.helper.LocalFileHelper.Companion.getCouponCourseJson
import com.example.helper.LocalFileHelper.Companion.queryCouponCourseJson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/fetch/all") {
            call.respondText(getAllCouponCourseJson(), ContentType.Application.Json)
        }
        get("/fetch/{numberOfCourseRequest?}") {
            val numberOfCourseRequest = call.parameters["numberOfCourseRequest"]?.toIntOrNull()
            if (numberOfCourseRequest == null || numberOfCourseRequest <= 0) {
                return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)
            }
            call.respondText(getCouponCourseJson(numberOfCourseRequest), ContentType.Application.Json)
        }
        get("/search/{query?}") {
            val searchQuery = call.parameters["query"]
            if (searchQuery.isNullOrEmpty()) {
                return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)
            }
            call.respondText(queryCouponCourseJson(searchQuery), ContentType.Application.Json)
        }
    }
}

